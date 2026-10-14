# Test Strategy

## Functional automation

1. Login with the assessment credentials and verify Dashboard.
2. Navigate to PIM and add an employee from JSON test data.
3. Use a unique Employee ID so repeated runs do not collide.
4. Upload a profile picture.
5. Search the employee by Employee ID.
6. Open Job details and update Job Title and Employment Status.
7. Verify the updated values in the UI.
8. Reuse the authenticated browser cookies for API calls.
9. Cross-check Employee ID, first name, last name, Job Title and Employment Status through the API.
10. Delete the employee through the UI.
11. Verify that the employee is absent in UI search and through the API.
12. Logout and verify that the protected Dashboard redirects to Login.
13. Run a negative authentication test with an invalid password.

## Design

- Selenium WebDriver
- Java
- JUnit 5
- Page Object Model
- JSON data-driven input
- Rest Assured for API validation
- ExtentReports HTML report
- Screenshot on test failure
- JMeter smoke performance test

## API deletion behavior

The shared demo may return `404` for a deleted employee. Some deployments may return `422` when the deleted internal employee number is no longer accepted. The framework treats either status as an absent resource, while still failing if a successful API response returns the same employee.

## Risks

- The OrangeHRM public demo is shared and its reference data can change.
- Job Title/Employment Status options may be modified by other users.
- Public demo availability and latency are outside the test framework's control.
