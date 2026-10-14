package com.assessment.orangehrm.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class PimPage extends BasePage {

	private final By addEmployeeTopNav = By.xpath("//a[normalize-space()='Add Employee']");

	private final By employeeIdSearch = By.xpath(
			"//label[normalize-space()='Employee Id']" + "/ancestor::div[contains(@class,'oxd-input-group')]//input");

	private final By searchButton = By.xpath("//button[@type='submit' and normalize-space()='Search']");

	private final By resetButton = By.xpath("//button[@type='button' and normalize-space()='Reset']");

	private final By confirmDelete = By.xpath("//button[normalize-space()='Yes, Delete']");

	private final By noRecordsMessage = By.xpath("//*[contains(normalize-space(),'No Records Found')]");

	public PimPage(WebDriver driver) {
		super(driver);
	}

	public EmployeePage openAddEmployee() {

		click(addEmployeeTopNav);

		return new EmployeePage(driver);
	}

	public PimPage searchByEmployeeId(String employeeId) {

		if (isVisible(resetButton)) {
			click(resetButton);
		}

		type(employeeIdSearch, employeeId);

		click(searchButton);

		// Wait until either employee appears or "No Records Found" appears
		wait.until(driver -> !driver.findElements(rowByEmployeeId(employeeId)).isEmpty()
				|| !driver.findElements(noRecordsMessage).isEmpty());

		return this;
	}

	public boolean isEmployeePresent(String employeeId) {

		return !elements(rowByEmployeeId(employeeId)).isEmpty();
	}

	public EmployeePage openEmployeeForEdit(String employeeId) {

		By editButton = By.xpath(rowXpath(employeeId) + "//button[.//i[contains(@class,'bi-pencil-fill')]]");

		clickFreshElement(editButton);

		// Wait until Edit Employee / Personal Details page opens
		wait.until(ExpectedConditions.urlContains("viewPersonalDetails"));

		return new EmployeePage(driver);
	}

	public void deleteEmployee(String employeeId) {

		By deleteButton = By.xpath(rowXpath(employeeId) + "//button[.//i[contains(@class,'bi-trash')]]");

		clickFreshElement(deleteButton);

		// Confirm delete popup
		clickFreshElement(confirmDelete);

		// Verify success toast
		waitForSuccessToast();

		// Verify employee disappears from table
		wait.until(ExpectedConditions.invisibilityOfElementLocated(rowByEmployeeId(employeeId)));
	}

	/**
	 * Used for dynamic OrangeHRM elements. If React refreshes the DOM and the
	 * element becomes stale, Selenium finds the element again and retries.
	 */
	private void clickFreshElement(By locator) {

		wait.until(driver -> {

			try {

				WebElement element = driver.findElement(locator);

				if (element.isDisplayed() && element.isEnabled()) {

					element.click();

					return true;
				}

			} catch (StaleElementReferenceException | NoSuchElementException e) {

				// DOM changed.
				// WebDriverWait will retry automatically.
				return false;
			}

			return false;
		});
	}

	private By rowByEmployeeId(String employeeId) {

		return By.xpath(rowXpath(employeeId));
	}

	private String rowXpath(String employeeId) {

		return "//div[contains(@class,'oxd-table-card')]" + "[.//*[normalize-space()='" + employeeId + "']]";
	}
}