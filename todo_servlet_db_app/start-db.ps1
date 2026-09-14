
# プロジェクト外の .env
$envFile = "C:\Secrets\todo_app\.env"

if (-not (Test-Path $envFile)) {
    Write-Error ".env が見つかりません: $envFile"
    exit 1
}

# MySQLコンテナを起動
docker compose --env-file $envFile up -d db

