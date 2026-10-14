package com.assessment.orangehrm.pages;

import com.assessment.orangehrm.config.ConfigReader;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class LoginPage extends BasePage {
    private final By username = By.cssSelector("input[name='username']");
    private final By password = By.cssSelector("input[name='password']");
    private final By loginButton = By.cssSelector("button[type='submit']");
    private final By loginHeading = By.xpath("//h5[normalize-space()='Login']");

    public LoginPage(WebDriver driver) {
        super(driver);
    }

    public LoginPage open() {
        driver.get(ConfigReader.loginUrl());
        return this;
    }

    public DashboardPage login(String user, String pass) {
        type(username, user);
        type(password, pass);
        click(loginButton);
        return new DashboardPage(driver);
    }

    public boolean isDisplayed() {
        return isVisible(loginHeading) && isVisible(loginButton);
    }
}
