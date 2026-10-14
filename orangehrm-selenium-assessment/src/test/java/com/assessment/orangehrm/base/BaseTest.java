package com.assessment.orangehrm.base;

import com.assessment.orangehrm.reporting.ExtentManager;
import com.assessment.orangehrm.utils.ScreenshotUtils;
import org.junit.jupiter.api.*;
import org.openqa.selenium.WebDriver;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public abstract class BaseTest {
    protected WebDriver driver;

    @BeforeAll
    void startReport() {
        ExtentManager.init();
    }

    @BeforeEach
    void setUp(TestInfo testInfo) {
        driver = DriverFactory.createDriver();
        ExtentManager.createTest(testInfo.getDisplayName());
    }

    @AfterEach
    void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @AfterAll
    void finishReport() {
        ExtentManager.flush();
    }

    protected void attachScreenshot(String label) {
        String path = ScreenshotUtils.capture(driver, label);
        try {
            ExtentManager.test().addScreenCaptureFromPath(path);
        } catch (Exception e) {
            ExtentManager.test().warning("Screenshot saved but could not be embedded: " + path);
        }
    }
}
