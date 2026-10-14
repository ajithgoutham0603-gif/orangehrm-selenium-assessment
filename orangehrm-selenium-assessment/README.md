# OrangeHRM QA Automation Assessment

Complete Selenium + Java solution for the OrangeHRM Employee Lifecycle assessment.

## Coverage

- Valid login and Dashboard assertion
- JSON-driven Add Employee flow
- Unique Employee ID
- Profile-picture upload
- Employee search by Employee ID
- Job Title and Employment Status update
- UI assertions
- Authenticated API/UI cross-validation
- UI deletion + API deletion verification
- Logout + protected-page session invalidation
- Invalid-login negative test
- Extent HTML report
- Failure screenshot capture
- JMeter performance smoke test
- AI-usage documentation
- Video-recording helper/instructions

## Stack

- Java 17+
- Maven
- Selenium WebDriver
- JUnit 5
- Rest Assured
- Jackson
- ExtentReports
- Apache JMeter 5.6.x

## Project structure

```text
orangehrm-qa-assessment/
├── pom.xml
├── README.md
├── AI_USAGE.md
├── SUBMISSION_CHECKLIST.md
├── src/test/java/com/assessment/orangehrm/
│   ├── api/
│   ├── base/
│   ├── config/
│   ├── data/
│   ├── pages/
│   ├── report/
│   ├── tests/
│   └── utils/
├── src/test/resources/
│   ├── config.properties
│   └── test-data/
├── performance/
│   ├── orangehrm-smoke.jmx
│   └── README.md
├── scripts/
│   ├── run-performance.bat
│   └── run-with-video.ps1
├── docs/
│   └── TEST_STRATEGY.md
├── reports/
└── videos/
```

## Prerequisites

Check:

```cmd
java -version
mvn -version
```

Use Java 17 or newer. Chrome must be installed.

Selenium Manager handles the ChromeDriver binary automatically.

## Configuration

Edit:

`src/test/resources/config.properties`

Default assessment values:

```properties
base.url=https://opensource-demo.orangehrmlive.com
username=Admin
password=admin123
headless=false
```

Employee data is in:

`src/test/resources/test-data/employee.json`

The Employee ID is generated from the configured prefix plus a timestamp suffix so repeat runs do not collide.

## Run functional + API tests

```cmd
mvn clean test
```

Run headless:

```cmd
mvn clean test -Dheadless=true
```

Run against another URL:

```cmd
mvn clean test -Dbase.url=https://your-environment.example
```

## Reports

After execution:

`reports/extent-report.html`

JUnit/Surefire output is also available under:

`target/surefire-reports/`

A screenshot is captured to `reports/screenshots/` when a test fails while the browser session is active.

## API validation

The test reuses authenticated Selenium cookies and calls:

```text
GET /web/index.php/api/v2/pim/employees/{empNumber}
GET /web/index.php/api/v2/pim/employees/{empNumber}/job-details
```

After UI deletion, the employee endpoint is checked again.

The framework intentionally reads Job Title and Employment Status from the Job Details endpoint instead of expecting them from the basic employee endpoint.

## Performance testing

Apache JMeter is included under `performance/`.

Run:

```cmd
scripts\run-performance.bat
```

or:

```cmd
jmeter -n -t performance\orangehrm-smoke.jmx -l reports\jmeter-results.jtl -e -o reports\jmeter-html
```

Open:

`reports/jmeter-html/index.html`

The included test is a light smoke load: 10 users, 10-second ramp-up, 5 loops.

Do not turn this into a heavy load test against the shared public OrangeHRM demo.

## Test execution video

The assessment requires a real test-run video. Source code alone cannot truthfully substitute for one.

Option 1: record `mvn clean test` with OBS/Windows screen recorder.

Option 2: if FFmpeg is installed and available in PATH:

```powershell
powershell -ExecutionPolicy Bypass -File scripts\run-with-video.ps1
```

The video is written to `videos/`.

Before submission, record a real passing run on your machine. Do not submit a fabricated video.

## AI usage

See:

`AI_USAGE.md`

Update it to reflect the AI tools you actually used. Keep screenshots/evidence if requested by the evaluator.

## Important shared-demo note

The public OrangeHRM instance is shared. Other users can change Job Titles, Employment Status values, or application data. If `QA Engineer` or `Full-Time Permanent` is unavailable, use values that currently exist in the assessment environment and update `employee.json`.

## Final submission

Before pushing to GitHub:

1. Run `mvn clean test`.
2. Open `reports/extent-report.html`.
3. Run the JMeter test and open `reports/jmeter-html/index.html`.
4. Record a real execution video.
5. Review `AI_USAGE.md`.
6. Follow `SUBMISSION_CHECKLIST.md`.
7. Push the complete project to the required public GitHub repository.
