@echo off
setlocal

if not exist reports mkdir reports

if exist reports\jmeter-html (
  rmdir /s /q reports\jmeter-html
)

jmeter -n ^
  -t performance\orangehrm-smoke.jmx ^
  -l reports\jmeter-results.jtl ^
  -e ^
  -o reports\jmeter-html

if errorlevel 1 (
  echo.
  echo JMeter execution failed.
  exit /b 1
)

echo.
echo Performance test completed.
echo Open reports\jmeter-html\index.html
