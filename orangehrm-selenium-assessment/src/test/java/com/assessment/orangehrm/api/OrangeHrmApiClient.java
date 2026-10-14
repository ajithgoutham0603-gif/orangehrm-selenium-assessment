package com.assessment.orangehrm.api;

import com.assessment.orangehrm.config.ConfigReader;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;

import java.util.HashMap;
import java.util.Map;

public class OrangeHrmApiClient {
    private final Map<String, String> sessionCookies = new HashMap<>();

    public OrangeHrmApiClient(WebDriver driver) {
        RestAssured.baseURI = ConfigReader.baseUrl();
        for (Cookie cookie : driver.manage().getCookies()) {
            sessionCookies.put(cookie.getName(), cookie.getValue());
        }
    }

    public Response getEmployee(int employeeNumber) {
        return RestAssured.given()
                .cookies(sessionCookies)
                .accept("application/json")
                .queryParam("model", "detailed")
                .when()
                .get("/web/index.php/api/v2/pim/employees/{empNumber}", employeeNumber);
    }

    public Response getJobDetails(int employeeNumber) {
        return RestAssured.given()
                .cookies(sessionCookies)
                .accept("application/json")
                .when()
                .get("/web/index.php/api/v2/pim/employees/{empNumber}/job-details", employeeNumber);
    }
}
