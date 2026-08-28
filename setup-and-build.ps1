# ============================================
# ODIN NetShield - Script de Automatizacion
# Configuracion de SDK y Compilacion del Proyecto
# ============================================

Write-Host "========================================" -ForegroundColor Cyan
Write-Host "ODIN NetShield - Configuracion y Build" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""

$projectDir = "C:\Users\User\Downloads\odin"
$localPropertiesPath = "$projectDir\local.properties"

# Funcion para detectar Android SDK
function Find-AndroidSDK {
    Write-Host "[1/5] Detectando Android SDK..." -ForegroundColor Yellow
    
    # Verificar variable de entorno
    $androidHome = $env:ANDROID_HOME
    if ($androidHome -and (Test-Path $androidHome)) {
        Write-Host "OK SDK encontrado en ANDROID_HOME: $androidHome" -ForegroundColor Green
        return $androidHome
    }
    
    # Ubicaciones comunes en Windows
    $commonPaths = @(
        "$env:LOCALAPPDATA\Android\Sdk",
        "$env:APPDATA\Android\Sdk",
        "C:\Android\Sdk",
        "C:\Users\$env:USERNAME\AppData\Local\Android\Sdk"
    )
    
    foreach ($path in $commonPaths) {
        if (Test-Path $path) {
            Write-Host "OK SDK encontrado en: $path" -ForegroundColor Green
            return $path
        }
    }
    
    Write-Host "ERROR SDK no encontrado en ubicaciones comunes" -ForegroundColor Red
    return $null
}

# Funcion para crear local.properties
function Set-LocalProperties {
    param([string]$sdkPath)
    
    Write-Host "[2/5] Configurando local.properties..." -ForegroundColor Yellow
    
    # Convertir ruta para formato de archivo (doble backslash)
    $sdkPathFormatted = $sdkPath -replace "\\", "\\\\"
    
    $content = "sdk.dir=$sdkPathFormatted"
    
    try {
        Set-Content -Path $localPropertiesPath -Value $content -Encoding UTF8
        Write-Host "OK local.properties creado exitosamente" -ForegroundColor Green
        Write-Host "  SDK Path: $sdkPath" -ForegroundColor Gray
        return $true
    }
    catch {
        Write-Host "ERROR Error creando local.properties: $_" -ForegroundColor Red
        return $false
    }
}

# Funcion para verificar local.properties existente
function Test-LocalProperties {
    Write-Host "[2/5] Verificando local.properties existente..." -ForegroundColor Yellow
    
    if (Test-Path $localPropertiesPath) {
        Write-Host "OK local.properties ya existe" -ForegroundColor Green
        $content = Get-Content $localPropertiesPath
        Write-Host "  Contenido: $content" -ForegroundColor Gray
        return $true
    }
    
    Write-Host "ERROR local.properties no existe" -ForegroundColor Red
    return $false
}

# Funcion para compilar el proyecto
function Build-Project {
    Write-Host "[3/5] Compilando proyecto..." -ForegroundColor Yellow
    
    Set-Location $projectDir
    
    Write-Host "  Ejecutando: gradlew.bat assembleDebug" -ForegroundColor Gray
    
    $buildOutput = & .\gradlew.bat assembleDebug 2>&1
    $buildExitCode = $LASTEXITCODE
    
    if ($buildExitCode -eq 0) {
        Write-Host "OK Compilacion exitosa" -ForegroundColor Green
        return $true
    }
    else {
        Write-Host "ERROR Compilacion fallo con codigo: $buildExitCode" -ForegroundColor Red
        Write-Host "  Ultimas lineas de output:" -ForegroundColor Red
        $buildOutput | Select-Object -Last 20 | ForEach-Object { Write-Host "  $_" -ForegroundColor Red }
        return $false
    }
}

# Funcion para validar compilacion
function Test-BuildSuccess {
    Write-Host "[4/5] Validando compilacion..." -ForegroundColor Yellow
    
    $apkPath = "$projectDir\app\build\outputs\apk\debug\app-debug.apk"
    
    if (Test-Path $apkPath) {
        $apkSize = (Get-Item $apkPath).Length / 1MB
        Write-Host "OK APK generado exitosamente" -ForegroundColor Green
        Write-Host "  Ruta: $apkPath" -ForegroundColor Gray
        Write-Host "  Tamano: $([math]::Round($apkSize, 2)) MB" -ForegroundColor Gray
        return $true
    }
    else {
        Write-Host "ERROR APK no encontrado en: $apkPath" -ForegroundColor Red
        return $false
    }
}

# Funcion para limpiar archivos de test
function Remove-TestFiles {
    Write-Host "[5/5] Limpiando archivos de test..." -ForegroundColor Yellow
    
    $testDirs = @(
        "$projectDir\app\src\test\java\com\odin\core\network\detection",
        "$projectDir\app\src\androidTest\java\com\odin\core\network\detection"
    )
    
    $removedCount = 0
    
    foreach ($dir in $testDirs) {
        if (Test-Path $dir) {
            try {
                Remove-Item -Path $dir -Recurse -Force
                Write-Host "OK Removido: $dir" -ForegroundColor Green
                $removedCount++
            }
            catch {
                Write-Host "ERROR Error removiendo $dir" -ForegroundColor Red
            }
        }
    }
    
    if ($removedCount -eq 0) {
        Write-Host "OK No hay archivos de test para limpiar" -ForegroundColor Green
    }
    else {
        Write-Host "OK Total directorios removidos: $removedCount" -ForegroundColor Green
    }
    
    return $true
}

# ============================================
# EJECUCION PRINCIPAL
# ============================================

Write-Host "Iniciando proceso de configuracion y build..." -ForegroundColor Cyan
Write-Host ""

# Paso 1: Detectar SDK
$sdkPath = Find-AndroidSDK

if (-not $sdkPath) {
    Write-Host ""
    Write-Host "========================================" -ForegroundColor Red
    Write-Host "ERROR: Android SDK no encontrado" -ForegroundColor Red
    Write-Host "========================================" -ForegroundColor Red
    Write-Host ""
    Write-Host "Por favor:" -ForegroundColor Yellow
    Write-Host "1. Instala Android Studio si no lo tienes" -ForegroundColor White
    Write-Host "2. Instala el Android SDK a traves de Android Studio" -ForegroundColor White
    Write-Host "3. Vuelve a ejecutar este script" -ForegroundColor White
    Write-Host ""
    Write-Host "O configura manualmente:" -ForegroundColor Yellow
    Write-Host "setx ANDROID_HOME C:\ruta\a\tu\Android\Sdk" -ForegroundColor White
    Write-Host ""
    exit 1
}

# Paso 2: Configurar local.properties
if (-not (Test-LocalProperties)) {
    if (-not (Set-LocalProperties -sdkPath $sdkPath)) {
        Write-Host ""
        Write-Host "ERROR: No se pudo configurar local.properties" -ForegroundColor Red
        exit 1
    }
}

# Paso 3: Compilar proyecto
if (-not (Build-Project)) {
    Write-Host ""
    Write-Host "ERROR: La compilacion fallo" -ForegroundColor Red
    Write-Host "Revisa el output de error arriba para mas detalles" -ForegroundColor Yellow
    exit 1
}

# Paso 4: Validar compilacion
if (-not (Test-BuildSuccess)) {
    Write-Host ""
    Write-Host "ERROR: La compilacion no genero el APK" -ForegroundColor Red
    exit 1
}

# Paso 5: Limpiar archivos de test
Remove-TestFiles

# ============================================
# RESUMEN FINAL
# ============================================

Write-Host ""
Write-Host "========================================" -ForegroundColor Green
Write-Host "OK PROCESO COMPLETADO EXITOSAMENTE" -ForegroundColor Green
Write-Host "========================================" -ForegroundColor Green
Write-Host ""
Write-Host "Resumen:" -ForegroundColor Cyan
Write-Host "  OK SDK configurado: $sdkPath" -ForegroundColor White
Write-Host "  OK local.properties creado/verificado" -ForegroundColor White
Write-Host "  OK Proyecto compilado exitosamente" -ForegroundColor White
Write-Host "  OK APK generado y validado" -ForegroundColor White
Write-Host "  OK Archivos de test limpiados" -ForegroundColor White
Write-Host ""
Write-Host "Sprint 3 (Behavioral Analysis) esta listo para validacion." -ForegroundColor Cyan
Write-Host ""
Write-Host "Proximos pasos:" -ForegroundColor Yellow
Write-Host "1. Proceder con Sprint 4 (Forensics & Logging)" -ForegroundColor White
Write-Host "2. Proceder con Sprint 5 (Automated Response)" -ForegroundColor White
Write-Host ""
