$ErrorActionPreference = "Stop"

$ffmpeg = Get-Command ffmpeg -ErrorAction SilentlyContinue
if (-not $ffmpeg) {
    Write-Host "FFmpeg was not found in PATH."
    Write-Host "Install FFmpeg or record the run manually using OBS/Windows screen recorder."
    exit 1
}

New-Item -ItemType Directory -Force -Path "videos" | Out-Null

$timestamp = Get-Date -Format "yyyyMMdd_HHmmss"
$videoPath = Join-Path (Resolve-Path "videos") "orangehrm_test_$timestamp.mp4"

$psi = New-Object System.Diagnostics.ProcessStartInfo
$psi.FileName = $ffmpeg.Source
$psi.Arguments = "-y -f gdigrab -framerate 15 -i desktop -c:v libx264 -preset ultrafast -pix_fmt yuv420p `"$videoPath`""
$psi.UseShellExecute = $false
$psi.RedirectStandardInput = $true
$psi.RedirectStandardError = $true
$psi.CreateNoWindow = $true

$recorder = New-Object System.Diagnostics.Process
$recorder.StartInfo = $psi
$recorder.Start() | Out-Null

Start-Sleep -Seconds 2

try {
    mvn clean test
    $testExitCode = $LASTEXITCODE
}
finally {
    if (-not $recorder.HasExited) {
        $recorder.StandardInput.WriteLine("q")
        $recorder.WaitForExit(10000) | Out-Null

        if (-not $recorder.HasExited) {
            $recorder.Kill()
        }
    }
}

Write-Host ""
Write-Host "Video: $videoPath"
exit $testExitCode
