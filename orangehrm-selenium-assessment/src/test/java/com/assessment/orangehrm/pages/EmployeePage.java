package com.assessment.orangehrm.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.nio.file.Path;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EmployeePage extends BasePage {

    private static final Pattern EMP_NUMBER_PATTERN =
            Pattern.compile("/empNumber/(\\d+)");

    private final By firstName =
            By.cssSelector("input[name='firstName']");

    private final By lastName =
            By.cssSelector("input[name='lastName']");

    private final By employeeId =
            By.xpath("//label[normalize-space()='Employee Id']" +
                    "/ancestor::div[contains(@class,'oxd-input-group')]//input");

    private final By profileUpload =
            By.cssSelector("input[type='file']");

    private final By saveEmployee =
            By.xpath("//button[@type='submit' and normalize-space()='Save']");

    private final By personalDetailsHeading =
            By.xpath("//h6[normalize-space()='Personal Details']");

    private final By jobTab =
            By.xpath("//a[normalize-space()='Job']");

    private final By jobHeading =
            By.xpath("//h6[normalize-space()='Job Details']");

    private final By jobTitleDropdown =
            By.xpath("//label[normalize-space()='Job Title']" +
                    "/ancestor::div[contains(@class,'oxd-input-group')]" +
                    "//div[contains(@class,'oxd-select-text')]");

    private final By employmentStatusDropdown =
            By.xpath("//label[normalize-space()='Employment Status']" +
                    "/ancestor::div[contains(@class,'oxd-input-group')]" +
                    "//div[contains(@class,'oxd-select-text')]");

    private final By jobSaveButton =
            By.xpath("//form//button[@type='submit' and normalize-space()='Save']");

    public EmployeePage(WebDriver driver) {
        super(driver);
    }

    public EmployeePage createEmployee(
            String first,
            String last,
            String id,
            Path picture) {

        type(firstName, first);
        type(lastName, last);
        type(employeeId, id);

        // File input is hidden in OrangeHRM.
        // Wait for presence instead of visibility.
        wait.until(ExpectedConditions.presenceOfElementLocated(profileUpload))
                .sendKeys(picture.toAbsolutePath().toString());

        click(saveEmployee);

        wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        personalDetailsHeading
                )
        );

        return this;
    }

    public boolean isPersonalDetailsDisplayed() {
        return isVisible(personalDetailsHeading);
    }

    public int getEmployeeNumberFromUrl() {

        wait.until(d ->
                EMP_NUMBER_PATTERN.matcher(d.getCurrentUrl()).find());

        Matcher matcher =
                EMP_NUMBER_PATTERN.matcher(driver.getCurrentUrl());

        if (!matcher.find()) {
            throw new IllegalStateException(
                    "Could not extract employee number from URL: "
                            + driver.getCurrentUrl()
            );
        }

        return Integer.parseInt(matcher.group(1));
    }

    public EmployeePage openJobDetails() {

        click(jobTab);

        wait.until(
                ExpectedConditions.visibilityOfElementLocated(jobHeading)
        );

        return this;
    }

    public EmployeePage updateJobDetails(
            String jobTitle,
            String employmentStatus) {

        selectOxdOption(jobTitleDropdown, jobTitle);

        selectOxdOption(
                employmentStatusDropdown,
                employmentStatus
        );

        click(jobSaveButton);

        waitForSuccessToast();

        return this;
    }

    public String getSelectedJobTitle() {
        return visible(jobTitleDropdown)
                .getText()
                .trim();
    }

    public String getSelectedEmploymentStatus() {
        return visible(employmentStatusDropdown)
                .getText()
                .trim();
    }
}