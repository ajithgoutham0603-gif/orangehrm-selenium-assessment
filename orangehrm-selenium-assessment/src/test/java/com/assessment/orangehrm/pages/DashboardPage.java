package com.assessment.orangehrm.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class DashboardPage extends BasePage {
    private final By dashboardHeading = By.xpath("//h6[normalize-space()='Dashboard']");
    private final By pimMenu = By.xpath("//span[normalize-space()='PIM']/ancestor::a");
    private final By userDropdown = By.cssSelector(".oxd-userdropdown-tab");
    private final By logoutLink = By.xpath("//a[normalize-space()='Logout']");

    public DashboardPage(WebDriver driver) {
        super(driver);
    }

    public boolean isDisplayed() {
        return isVisible(dashboardHeading);
    }

    public PimPage openPim() {
        click(pimMenu);
        return new PimPage(driver);
    }

    public LoginPage logout() {
        click(userDropdown);
        click(logoutLink);
        return new LoginPage(driver);
    }
}
