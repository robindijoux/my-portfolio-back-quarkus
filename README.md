# 🚀 Portfolio API Quarkus

Native Quarkus REST API deployed on AWS Lambda with Function URL.

## 🏗️ Architecture

This API uses:
- **Quarkus** for the REST framework
- **Native compilation** with GraalVM (cold start < 500ms)
- **AWS Lambda** with Function URL
- **ARM64 architecture** for performance

## 🚀 Quick start

### Local development
```bash
# Development mode
./mvnw quarkus:dev

# Tests
./mvnw test
```

### AWS deployment
```bash
# Configuration (first time)
cp .env.template .env
# Edit .env with your AWS parameters

# Complete deployment
./scripts/deploy.sh
```

## 📋 Available endpoints

| Endpoint | Method | Description |
|----------|---------|-------------|
| `/hello` | GET | Welcome message |
| `/hello` | GET | Simple greeting |
| `/q/health` | GET | Health check endpoint |

## 🔧 Configuration

Main variables in `.env`:
- `AWS_ACCOUNT_ID`: Your AWS account
- `AWS_REGION`: Deployment region (default: eu-west-3)
- `LAMBDA_FUNCTION_NAME`: Lambda function name
- `ECR_REPOSITORY_NAME`: ECR repository for the image

## 📚 Documentation

- [Deployment guide](docs/DEPLOYMENT.md)
- [API documentation](docs/API.md)
- [Adding endpoints](docs/ENDPOINTS.md)

---

**Generated with Quarkus Lambda bootstrap on Wed Oct  8 15:29:52 CEST 2025**
