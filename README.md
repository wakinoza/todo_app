# Todo App on AWS


Spring Bootで作成したTodoアプリをAWSへ段階的にデプロイすることを目的とした学習プロジェクトです。

小規模な構成からAWSサービスを一つずつ導入し、各サービスの役割や採用理由を理解しながら、実務を意識したシステム構成へ段階的に改善していきます。

完成したシステムだけではなく、
設計理由・構築手順・改善の過程もポートフォリオとして公開します。

## 開発方針

- 段階的にAWS構成へ移行する
- 実務で一般的なベストプラクティスを意識する

## 現在の進捗

✅ 開発環境構築

⬜ GitHub Actions

⬜ EC2デプロイ

⬜ RDS導入

⬜ Docker化

⬜ ALB導入

⬜ ECS(Fargate)導入

## システム構成

現在

```plain
+-------------+
| Spring Boot |
+-------------+
       │
       ▼
+-------------+
|    MySQL    |
+-------------+
```

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
- GitHub Actions

## ローカル開発環境（VS Code）

### 前提

- Java 21
- Docker Desktop
- VS Code(Extension Pack for Java)

### 初回セットアップ

1. リポジトリをクローン
2. `.env.example` を `.env` にコピー
3. `.env.local.example` を `.env.local` にコピー
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

### 動作確認
「http://localhost:8080」へアクセスします。

ユーザー名は「yamada」、パスワードは「yamada_password」
