param(
    [switch]$Jar,
    [switch]$Maven
)


# プロジェクト外の .env.local
$envFile = "C:\Secrets\todo_app\.env.local"

if (-not (Test-Path $envFile)) {
    Write-Error ".env.local が見つかりません: $envFile"
    exit 1
}

# .env.local を読み込む
Get-Content $envFile |
Where-Object {
    $_ -and
    -not $_.StartsWith("#") -and
    $_ -match "="
} |
ForEach-Object {
    $name, $value = $_ -split "=", 2
    [System.Environment]::SetEnvironmentVariable($name, $value, "Process")
}

# Spring Boot 起動
if ($Jar) {
    java -jar target\todo-app-0.0.1-SNAPSHOT.jar
}
elseif ($Maven) {
    ./mvnw spring-boot:run
}