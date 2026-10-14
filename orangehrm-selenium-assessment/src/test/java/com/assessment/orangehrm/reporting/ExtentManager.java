package com.assessment.orangehrm.reporting;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;

import java.nio.file.Files;
import java.nio.file.Path;

public final class ExtentManager {
    private static ExtentReports extentReports;
    private static final ThreadLocal<ExtentTest> CURRENT_TEST = new ThreadLocal<>();

    private ExtentManager() {
    }

    public static synchronized void init() {
        if (extentReports != null) {
            return;
        }
        try {
            Files.createDirectories(Path.of("reports"));
        } catch (Exception e) {
            throw new IllegalStateException("Could not create report directory", e);
        }

        ExtentSparkReporter spark = new ExtentSparkReporter("reports/extent-report.html");
        spark.config().setDocumentTitle("OrangeHRM Automation Report");
        spark.config().setReportName("Employee Lifecycle - Selenium JUnit");

        extentReports = new ExtentReports();
        extentReports.attachReporter(spark);
        extentReports.setSystemInfo("Framework", "Selenium + Java + JUnit + POM");
        extentReports.setSystemInfo("Application", "OrangeHRM Demo");
    }

    public static void createTest(String name) {
        CURRENT_TEST.set(extentReports.createTest(name));
    }

    public static ExtentTest test() {
        return CURRENT_TEST.get();
    }

    public static void info(String message) {
        test().info(message);
    }

    public static void pass(String message) {
        test().pass(message);
    }

    public static void fail(Throwable throwable) {
        test().fail(throwable);
    }

    public static synchronized void flush() {
        if (extentReports != null) {
            extentReports.flush();
        }
    }
}
