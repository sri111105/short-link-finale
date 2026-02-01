$ErrorActionPreference = "Stop"

Write-Host "Checking environment..."

# 1. Check Java
if (Get-Command "java" -ErrorAction SilentlyContinue) {
    java -version
    Write-Host "Java found."
}
else {
    Write-Error "Java is not installed or not in PATH. Please install Java 8 or higher."
    exit 1
}

# 2. Check/Install Maven locally
$mvnDir = "$PSScriptRoot\.mvn_dist"
$mvnBin = "$mvnDir\apache-maven-3.9.6\bin\mvn.cmd"

if (-not (Test-Path $mvnBin)) {
    Write-Host "Maven not found locally. Installing..."
    
    $mavenUrl = "https://archive.apache.org/dist/maven/maven-3/3.9.6/binaries/apache-maven-3.9.6-bin.zip"
    $zipPath = "$PSScriptRoot\maven.zip"
    
    # Download
    Write-Host "Downloading Maven from $mavenUrl..."
    Invoke-WebRequest -Uri $mavenUrl -OutFile $zipPath
    
    # Extract
    Write-Host "Extracting Maven..."
    Expand-Archive -Path $zipPath -DestinationPath $mvnDir -Force
    
    # Cleanup
    Remove-Item $zipPath
    Write-Host "Maven installed to $mvnDir"
}
else {
    Write-Host "Using local Maven from $mvnDir"
}

# 3. Setup JAVA_HOME if missing or invalid
$jdkPaths = @(Get-ChildItem -Path "C:\Program Files\Java\jdk*" -ErrorAction SilentlyContinue | Sort-Object Name -Descending)

if (-not $env:JAVA_HOME -or -not (Test-Path "$env:JAVA_HOME\bin\javac.exe")) {
    Write-Host "Checking for JDKs..."
    if ($jdkPaths.Count -gt 0) {
        $foundJdk = $jdkPaths[0].FullName
        $env:JAVA_HOME = $foundJdk
        Write-Host "Found JDK: $foundJdk"
        Write-Host "Setting JAVA_HOME to $foundJdk"
        
        # Add to PATH temporarily for this session
        $env:PATH = "$foundJdk\bin;$env:PATH"
    }
    else {
        Write-Error "Could not find a JDK in C:\Program Files\Java. Maven requires a JDK (not just JRE). Please install a JDK."
        # We try to proceed anyway in case JAVA_HOME was somehow set but check failed, but likely will fail.
    }
}
else {
    Write-Host "Using existing JAVA_HOME: $env:JAVA_HOME"
}

# 4. Run the project
Write-Host "Starting URL Shortener Service..."
& $mvnBin spring-boot:run
