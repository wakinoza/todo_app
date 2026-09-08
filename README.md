# Todo App on AWS


Spring Bootで作成したTodoアプリをAWSへ段階的にデプロイすることを目的とした学習プロジェクトです。

小規模な構成からAWSサービスを一つずつ導入し、各サービスの役割や採用理由を理解しながら、実務を意識したシステム構成へ段階的に改善していきます。

完成したシステムだけではなく、
設計理由・構築手順・改善の過程もポートフォリオとして公開します。


## 現在の進捗

✅ 開発環境構築

✅  EC2デプロイ

✅ RDS導入

⬜ Docker化

⬜ ALB導入

⬜ ECS(Fargate)導入

## AWSの構成

現在

```plain
Internet
    │
    ▼
Internet Gateway
    │
    ▼
Public Subnet
    │
    ▼
EC2
└─ Spring Boot
       │
       │ TCP 3306
       ▼
Private Subnet
└─ RDS(MySQL)

## 使用技術

### Backend

- Java 21
- Spring Boot 4
- Maven

### Database

- MySQL

### Development

- Git
- GitHub

### AWS

- Amazon VPC
- Amazon EC2
‐ Amazon RDS for MySQL
- AWS Secrets Manager
- AWS Identity and Access Management（IAM）


## ローカル開発環境（VS Code）

### 前提

- Java 21
- Docker Desktop
- VS Code(Extension Pack for Java)

### 初回セットアップ

1. リポジトリをクローン
2. `.env.example` をコピーし、必要項目を記入して`.env`を作成。プロジェクト外（例 C:\Secrets\todo_app\.env）へ配置してください。
3. `.env.local.example` をコピーし、必要項目を記入して`.env.local`を作成。プロジェクト外（例 C:\Secrets\todo_app\.env.local）へ配置してください。
4. 必要に応じて接続情報を編集

### MySQLの起動

```bash
docker compose up -d db
```

### Spring Bootの起動

VS Codeの「実行とデバッグ」から以下を選択して実行します

```bash
Spring Boot (Local)
```

`launch.json` の `envFile` により、`.env.local` が自動的に読み込まれます。

`launch.json` の envFile は VS Code の「実行とデバッグ」で起動した場合のみ有効です。`java -jar` で直接起動した場合は適用されないため、ローカルでは run-local.ps1 を利用してください。

### JARファイルの起動

JARファイルを直接実行する場合は、`launch.json` は利用されないため、`.env.local` は自動では読み込まれません。

ローカル環境では、プロジェクトルートに用意した起動スクリプトを利用します。

```powershell
.\run-local.ps1 -Jar
```

このスクリプトは以下を自動で行います。

- `.env.local` の読み込み
- 環境変数の設定（現在のPowerShellプロセスのみ）
- Spring Boot JARの起動

### 動作確認

ブラウザで以下へアクセスします。

```
http://localhost:8080
```

ログイン情報

- ユーザー名：`yamada`
- パスワード：`yamada_password`
