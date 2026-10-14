# Performance Testing

Tool: Apache JMeter 5.6.x

This assessment uses a deliberately light smoke load against the public OrangeHRM demo environment.

## Scenario

- Endpoint: `GET /web/index.php/auth/login`
- Virtual users: 10
- Ramp-up: 10 seconds
- Loops per user: 5
- Total planned requests: 50
- Assertions:
  - HTTP 200
  - Response contains `OrangeHRM`
  - Individual response completes within 3000 ms

The 3-second assertion is an assessment threshold, not an OrangeHRM contractual SLA.

## Run

Windows:

```cmd
scripts\run-performance.bat
```

Or directly:

```cmd
jmeter -n -t performance\orangehrm-smoke.jmx -l reports\jmeter-results.jtl -e -o reports\jmeter-html
```

Open:

`reports/jmeter-html/index.html`

Review response time, throughput, error percentage, and percentile values.

> Do not increase the load aggressively against the shared public demo site.
