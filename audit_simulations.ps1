# Simulation Audit Script for ODIN APK
# This script searches for TODO comments, hardcoded test data, and simulated data in the codebase

param(
    [string]$ProjectPath = "c:\Users\User\Downloads\odin"
)

$ErrorActionPreference = "Stop"

Write-Host "========================================" -ForegroundColor Cyan
Write-Host "  ODIN Simulation Audit Script" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""

$auditResults = @{
    "TODO_Comments" = @()
    "Mock_References" = @()
    "Fake_Data" = @()
    "Hardcoded_Test_Data" = @()
    "Simulated_Data" = @()
}

# Function to search for patterns in files
function Search-PatternInFiles {
    param(
        [string]$Pattern,
        [string]$Description,
        [string[]]$Extensions = @(".kt", ".java"),
        [hashtable]$Results
    )
    
    Write-Host "Searching for: $Description" -ForegroundColor Yellow
    
    $files = Get-ChildItem -Path $ProjectPath -Recurse -Include $Extensions -File | Where-Object {
        $_.FullName -notlike "*\test\*" -and
        $_.FullName -notlike "*\androidTest\*" -and
        $_.FullName -notlike "*\build\*"
    }
    
    foreach ($file in $files) {
        $content = Get-Content $file.FullName -Raw
        if ($content -match $Pattern) {
            $matches = [regex]::Matches($content, $Pattern, [System.Text.RegularExpressions.RegexOptions]::Multiline)
            foreach ($match in $matches) {
                $lineNumber = ($content.Substring(0, $match.Index).Split("`n").Count)
                $Results[$Description] += @{
                    File = $file.FullName.Replace($ProjectPath, "")
                    Line = $lineNumber
                    Match = $match.Value.Trim()
                }
            }
        }
    }
    
    $count = $Results[$Description].Count
    Write-Host "  Found $count occurrences" -ForegroundColor $(if ($count -eq 0) { "Green" } else { "Red" })
    Write-Host ""
}

# Search for TODO comments related to simulations
Search-PatternInFiles -Pattern "(?i)TODO.*(?:simulado|mock|fake|dummy|placeholder)" -Description "TODO_Comments" -Results $auditResults

# Search for mock references
Search-PatternInFiles -Pattern "(?i)(mockk|mockito|mock\(|fake\(|dummy\()" -Description "Mock_References" -Results $auditResults

# Search for fake data patterns
Search-PatternInFiles -Pattern "(?i)(fake.*data|dummy.*data|test.*data)" -Description "Fake_Data" -Results $auditResults

# Search for hardcoded test data
Search-PatternInFiles -Pattern "(?i)(192\.168\.[0-9]+\.[0-9]+|10\.0\.0\.[0-9]+|127\.0\.0\.1|localhost)" -Description "Hardcoded_Test_Data" -Results $auditResults

# Search for simulated data comments
Search-PatternInFiles -Pattern "(?i)(simulated|simulado|placeholder.*data)" -Description "Simulated_Data" -Results $auditResults

# Generate audit report
Write-Host "========================================" -ForegroundColor Cyan
Write-Host "  AUDIT REPORT" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""

$totalIssues = 0
foreach ($key in $auditResults.Keys) {
    $count = $auditResults[$key].Count
    $totalIssues += $count
    
    if ($count -gt 0) {
        Write-Host "$key : $count issues" -ForegroundColor Red
        foreach ($issue in $auditResults[$key]) {
            Write-Host "  - $($issue.File):$($issue.Line)" -ForegroundColor DarkRed
            Write-Host "    $($issue.Match)" -ForegroundColor Gray
        }
        Write-Host ""
    } else {
        Write-Host "$key : $count issues" -ForegroundColor Green
        Write-Host ""
    }
}

Write-Host "========================================" -ForegroundColor Cyan
Write-Host "  SUMMARY" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan
Write-Host "Total Issues Found: $totalIssues" -ForegroundColor $(if ($totalIssues -eq 0) { "Green" } else { "Red" })
Write-Host ""

# Check ViewModels for error handling
Write-Host "Checking ViewModels for error handling..." -ForegroundColor Yellow
$viewModelFiles = Get-ChildItem -Path "$ProjectPath\app\src\main\java\com\odin\core\ui\viewmodel" -Filter "*.kt" -File

$viewModelsWithoutErrorHandling = @()
foreach ($vmFile in $viewModelFiles) {
    $content = Get-Content $vmFile.FullName -Raw
    if ($content -notmatch "try\s*\{" -or $content -notmatch "catch\s*\(") {
        $viewModelsWithoutErrorHandling += $vmFile.Name
    }
}

if ($viewModelsWithoutErrorHandling.Count -gt 0) {
    Write-Host "  ViewModels without proper error handling:" -ForegroundColor Red
    foreach ($vm in $viewModelsWithoutErrorHandling) {
        Write-Host "    - $vm" -ForegroundColor DarkRed
    }
} else {
    Write-Host "  All ViewModels have error handling" -ForegroundColor Green
}
Write-Host ""

# Check Repositories for real service integration
Write-Host "Checking Repositories for real service integration..." -ForegroundColor Yellow
$repositoryFiles = Get-ChildItem -Path "$ProjectPath\app\src\main\java\com\odin\core\data\repository" -Filter "*Impl.kt" -File

$repositoriesWithMocks = @()
foreach ($repoFile in $repositoryFiles) {
    $content = Get-Content $repoFile.FullName -Raw
    if ($content -match "(?i)(mock|fake|dummy)" -and $content -notmatch "(?i)(ActivityManager|SentryCore|ThreatDetectionEngine|DynamicRuleEngine|QuarantineManager)") {
        $repositoriesWithMocks += $repoFile.Name
    }
}

if ($repositoriesWithMocks.Count -gt 0) {
    Write-Host "  Repositories potentially using mocks:" -ForegroundColor Red
    foreach ($repo in $repositoriesWithMocks) {
        Write-Host "    - $repo" -ForegroundColor DarkRed
    }
} else {
    Write-Host "  All Repositories use real services" -ForegroundColor Green
}
Write-Host ""

# Generate markdown report
$reportPath = "$ProjectPath\SIMULATION_AUDIT_REPORT.md"
$reportContent = @"

# ODIN Simulation Audit Report

**Generated:** $(Get-Date -Format "yyyy-MM-dd HH:mm:ss")
**Project Path:** $ProjectPath

## Summary

- **Total Issues Found:** $totalIssues
- **ViewModels without error handling:** $($viewModelsWithoutErrorHandling.Count)
- **Repositories potentially using mocks:** $($repositoriesWithMocks.Count)

## Detailed Findings

"@

foreach ($key in $auditResults.Keys) {
    $count = $auditResults[$key].Count
    $reportContent += "### $key ($count issues)`n`n"
    
    if ($count -gt 0) {
        foreach ($issue in $auditResults[$key]) {
            $reportContent += "- **File:** $($issue.File)`n"
            $reportContent += "  **Line:** $($issue.Line)`n"
            $reportContent += "  **Match:** ``$($issue.Match)```n`n"
        }
    } else {
        $reportContent += "No issues found.`n`n"
    }
}

$reportContent += @"

## ViewModel Error Handling

"@

if ($viewModelsWithoutErrorHandling.Count -gt 0) {
    $reportContent += "The following ViewModels may lack proper error handling:`n`n"
    foreach ($vm in $viewModelsWithoutErrorHandling) {
        $reportContent += "- $vm`n"
    }
} else {
    $reportContent += "All ViewModels have proper error handling.`n`n"
}

$reportContent += @"

## Repository Service Integration

"@

if ($repositoriesWithMocks.Count -gt 0) {
    $reportContent += "The following Repositories may be using mock services:`n`n"
    foreach ($repo in $repositoriesWithMocks) {
        $reportContent += "- $repo`n"
    }
} else {
    $reportContent += "All Repositories use real service integrations.`n`n"
}

$reportContent += @"

## Recommendations

"@

if ($totalIssues -eq 0) {
    $reportContent += "- No simulation-related issues found. The codebase appears to use real data and services.`n"
} else {
    $reportContent += "- Review and address the simulation-related issues listed above.`n"
    $reportContent += "- Ensure all ViewModels have proper error handling with try-catch blocks.`n"
    $reportContent += "- Verify that all Repositories use real service integrations instead of mocks.`n"
    $reportContent += "- Remove or replace any hardcoded test data with real data sources.`n"
}

$reportContent | Out-File -FilePath $reportPath -Encoding UTF8

Write-Host "========================================" -ForegroundColor Cyan
Write-Host "  Audit Complete" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan
Write-Host "Report saved to: $reportPath" -ForegroundColor Green
Write-Host ""
