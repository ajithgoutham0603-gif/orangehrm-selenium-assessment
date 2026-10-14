package com.assessment.orangehrm.tests;

import com.assessment.orangehrm.api.OrangeHrmApiClient;
import com.assessment.orangehrm.base.BaseTest;
import com.assessment.orangehrm.config.ConfigReader;
import com.assessment.orangehrm.data.EmployeeData;
import com.assessment.orangehrm.pages.DashboardPage;
import com.assessment.orangehrm.pages.EmployeePage;
import com.assessment.orangehrm.pages.LoginPage;
import com.assessment.orangehrm.pages.PimPage;
import com.assessment.orangehrm.reporting.ExtentManager;
import com.assessment.orangehrm.utils.JsonDataReader;
import com.assessment.orangehrm.utils.ResourceUtils;

import io.restassured.response.Response;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

class EmployeeLifecycleTest extends BaseTest {

	@Test
	@DisplayName("Employee lifecycle: login, create, edit, API validate, delete and logout")
	void employeeLifecycle() {

		EmployeeData data = JsonDataReader.read("testdata/employee.json", EmployeeData.class);

		String employeeId = data.getEmployeeIdPrefix() + Instant.now().toEpochMilli() % 10_000_000;

		try {

			
			// 1. LOGIN
			

			ExtentManager.info("1. Login with valid administrator credentials");

			DashboardPage dashboard = new LoginPage(driver).open().login(ConfigReader.get("username"),
					ConfigReader.get("password"));

			assertTrue(dashboard.isDisplayed(), "Dashboard should be visible after successful login");

			
			// 2. CREATE EMPLOYEE
		

			ExtentManager.info("2. Create a new employee from JSON data with profile picture");

			PimPage pim = dashboard.openPim();

			EmployeePage employeePage = pim.openAddEmployee().createEmployee(data.getFirstName(), data.getLastName(),
					employeeId, ResourceUtils.path(data.getProfilePicture()));

			assertTrue(employeePage.isPersonalDetailsDisplayed(),
					"Personal Details page should be displayed after employee creation");

			int employeeNumber = employeePage.getEmployeeNumberFromUrl();

			assertTrue(employeeNumber > 0, "A valid internal employee number should be generated");

			System.out.println("Employee ID: " + employeeId);

			System.out.println("Employee Number: " + employeeNumber);

			
			// 3. SEARCH AND UPDATE JOB DETAILS
			

			ExtentManager.info("3. Search newly created employee and update job details");

			pim = dashboard.openPim().searchByEmployeeId(employeeId);

			assertTrue(pim.isEmployeePresent(employeeId),
					"Employee should be searchable by Employee ID: " + employeeId);

			employeePage = pim.openEmployeeForEdit(employeeId).openJobDetails().updateJobDetails(data.getJobTitle(),
					data.getEmploymentStatus());

			String uiJobTitle = employeePage.getSelectedJobTitle();

			String uiEmploymentStatus = employeePage.getSelectedEmploymentStatus();

			System.out.println("UI Job Title: " + uiJobTitle);

			System.out.println("UI Employment Status: " + uiEmploymentStatus);

			assertEquals(data.getJobTitle(), uiJobTitle, "Job Title shown in UI should match requested value");

			assertEquals(data.getEmploymentStatus(), uiEmploymentStatus,
					"Employment Status shown in UI should match requested value");

			
			// 4. API VALIDATION
		

			ExtentManager.info("4. Validate employee using OrangeHRM REST API");

			OrangeHrmApiClient api = new OrangeHrmApiClient(driver);

			
			// EMPLOYEE API
			
			Response employeeResponse = api.getEmployee(employeeNumber);

			System.out.println();
			System.out.println("========== EMPLOYEE API RESPONSE ==========");

			System.out.println(employeeResponse.asPrettyString());

			System.out.println("===========================================");

			assertEquals(200, employeeResponse.statusCode(), "Employee API should return HTTP 200");

			assertEquals(data.getFirstName(), employeeResponse.jsonPath().getString("data.firstName"),
					"API First Name should match UI/test data");

			assertEquals(data.getLastName(), employeeResponse.jsonPath().getString("data.lastName"),
					"API Last Name should match UI/test data");

			assertEquals(employeeId, employeeResponse.jsonPath().getString("data.employeeId"),
					"API Employee ID should match UI Employee ID");

			
			// JOB DETAILS API
			

			Response jobResponse = api.getJobDetails(employeeNumber);

			System.out.println();
			System.out.println("========== JOB API RESPONSE ==========");

			System.out.println(jobResponse.asPrettyString());

			System.out.println("======================================");

			assertEquals(200, jobResponse.statusCode(), "Job Details API should return HTTP 200");

			String apiJobTitle = getFirstAvailableString(jobResponse, "data.title", "data.jobTitle.title",
					"data.jobTitle.name", "data.jobTitle");

			String apiEmploymentStatus = getFirstAvailableString(jobResponse, "data.status", "data.empStatus.name",
					"data.employmentStatus.name", "data.empStatus");

			System.out.println("Expected Job Title: " + data.getJobTitle());

			System.out.println("API Job Title: " + apiJobTitle);

			System.out.println("Expected Employment Status: " + data.getEmploymentStatus());

			System.out.println("API Employment Status: " + apiEmploymentStatus);

			assertNotNull(apiJobTitle, "Job Title was not found in API response");

			assertNotNull(apiEmploymentStatus, "Employment Status was not found in API response");

			assertEquals(data.getJobTitle(), apiJobTitle, "API Job Title should match UI Job Title");

			assertEquals(data.getEmploymentStatus(), apiEmploymentStatus,
					"API Employment Status should match UI Employment Status");

			
			// 5. DELETE EMPLOYEE
			

			ExtentManager.info("5. Delete employee from UI and validate deletion");

			pim = dashboard.openPim().searchByEmployeeId(employeeId);

			assertTrue(pim.isEmployeePresent(employeeId), "Employee must exist before deletion");

			pim.deleteEmployee(employeeId);

			// Search again
			pim.searchByEmployeeId(employeeId);

			assertFalse(pim.isEmployeePresent(employeeId), "Employee should not appear in UI after deletion");

			
			// API VALIDATION AFTER DELETE
			

			Response deletedEmployeeResponse = api.getEmployee(employeeNumber);

			int deleteStatus = deletedEmployeeResponse.statusCode();

			System.out.println();
			System.out.println("========== DELETE API RESPONSE ==========");

			System.out.println("HTTP Status: " + deleteStatus);

			System.out.println(deletedEmployeeResponse.asPrettyString());

			System.out.println("=========================================");

			/*
			 * OrangeHRM demo may return:
			 *
			 * 404 -> employee not found 422 -> employee number no longer valid
			 *
			 * Both confirm the employee cannot be retrieved.
			 */

			assertTrue(deleteStatus == 404 || deleteStatus == 422,
					"After deletion API should return 404 or 422, but returned: " + deleteStatus);

			ExtentManager.info("Employee deletion verified successfully. API returned HTTP " + deleteStatus);

			
			// 6. LOGOUT
			

			ExtentManager.info("6. Logout and verify session invalidation");

			LoginPage loginPage = dashboard.logout();

			assertTrue(loginPage.isDisplayed(), "Login page should be displayed after logout");

			// Try accessing dashboard again

			driver.get(ConfigReader.baseUrl() + "/web/index.php/dashboard/index");

			assertTrue(new LoginPage(driver).isDisplayed(), "Dashboard should not be accessible after logout");

			
			// SUCCESS
			

			attachScreenshot("employee-lifecycle-completed");

			ExtentManager.pass("Employee lifecycle workflow completed successfully");

		} catch (Throwable failure) {

			
			// FAILURE
			

			attachScreenshot("employee-lifecycle-failure");

			ExtentManager.fail(failure);

			if (failure instanceof Error error) {
				throw error;
			}

			if (failure instanceof RuntimeException runtimeException) {
				throw runtimeException;
			}

			throw new RuntimeException(failure);
		}
	}

	
	// JSON HELPER METHOD
	

	private String getFirstAvailableString(Response response, String... jsonPaths) {

		for (String jsonPath : jsonPaths) {

			try {

				Object value = response.jsonPath().get(jsonPath);

				if (value != null) {

					String result = String.valueOf(value).trim();

					if (!result.isEmpty() && !result.equalsIgnoreCase("null")) {

						return result;
					}
				}

			} catch (Exception ignored) {

				// Try next JSON path
			}
		}

		return null;
	}
}