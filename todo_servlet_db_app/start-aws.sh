#!/bin/bash
#AWSのEC2上でJARファイルからSpringアプリコンテナを起動するスクリプト
set -e

# Secrets ManagerからDB認証情報を取得
SECRET_JSON=$(aws secretsmanager get-secret-value \
  --secret-id todo-app/db \
  --query SecretString \
  --output text)

# DB認証情報
export DB_USER_NAME=$(echo "$SECRET_JSON" | jq -r '.username')
export DB_PASSWORD=$(echo "$SECRET_JSON" | jq -r '.password')

# RDS接続情報
export DB_HOST=todo-app-db.cxqk006262in.ap-northeast-1.rds.amazonaws.com
export DB_PORT=3306
export DB_NAME=todo_db

# Dockerイメージをビルド
docker build \
  -f Dockerfile.aws \
  -t todo-app .

# Dockerコンテナ起動
docker run \
  --name todo-spring-app \
  -p 8080:8080 \
  -e DB_HOST="$DB_HOST" \
  -e DB_PORT="$DB_PORT" \
  -e DB_NAME="$DB_NAME" \
  -e DB_USER_NAME="$DB_USER_NAME" \
  -e DB_PASSWORD="$DB_PASSWORD" \
  todo-app