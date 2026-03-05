# Quick Start Deployment Script for Hotel Management System
# This script will guide you through AWS deployment step by step

Write-Host "========================================" -ForegroundColor Cyan
Write-Host "  Hotel Management System - AWS Deployer" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""

# Configuration
$AWS_REGION = "us-east-1"
$PROJECT_NAME = "hotel-management"

# Check prerequisites
Write-Host "📋 Checking prerequisites..." -ForegroundColor Yellow
Write-Host ""

# 1. Check AWS CLI
Write-Host "1️⃣ Checking AWS CLI..." -ForegroundColor White
try {
    $awsVersion = aws --version 2>&1
    Write-Host "   ✅ AWS CLI installed: $awsVersion" -ForegroundColor Green
    
    # Check credentials
    $identity = aws sts get-caller-identity 2>&1 | ConvertFrom-Json
    Write-Host "   ✅ AWS Account: $($identity.Account)" -ForegroundColor Green
    $AWS_ACCOUNT_ID = $identity.Account
} catch {
    Write-Host "   ❌ AWS CLI not configured properly" -ForegroundColor Red
    Write-Host "   Run: aws configure" -ForegroundColor Yellow
    exit 1
}

# 2. Check Docker
Write-Host ""
Write-Host "2️⃣ Checking Docker..." -ForegroundColor White
try {
    $dockerVersion = docker --version 2>&1
    Write-Host "   ✅ Docker installed: $dockerVersion" -ForegroundColor Green
    $DOCKER_AVAILABLE = $true
} catch {
    Write-Host "   ⚠️  Docker not installed" -ForegroundColor Yellow
    Write-Host "   Docker is required for containerized deployment" -ForegroundColor Yellow
    $DOCKER_AVAILABLE = $false
}

# 3. Check Maven
Write-Host ""
Write-Host "3️⃣ Checking Maven..." -ForegroundColor White
try {
    $mvnVersion = mvn --version 2>&1 | Select-String "Apache Maven" | Select-Object -First 1
    Write-Host "   ✅ Maven installed: $mvnVersion" -ForegroundColor Green
} catch {
    Write-Host "   ❌ Maven not installed" -ForegroundColor Red
    Write-Host "   Install from: https://maven.apache.org/download.cgi" -ForegroundColor Yellow
    exit 1
}

Write-Host ""
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""

# Deployment options
Write-Host "🚀 Select Deployment Method:" -ForegroundColor Cyan
Write-Host ""
Write-Host "1. AWS Elastic Beanstalk (Easiest - No Docker required)" -ForegroundColor White
Write-Host "   - Quick deployment" -ForegroundColor Gray
Write-Host "   - Minimal configuration" -ForegroundColor Gray
Write-Host "   - Good for getting started" -ForegroundColor Gray
Write-Host ""
Write-Host "2. AWS ECS Fargate (Recommended - Requires Docker)" -ForegroundColor White
Write-Host "   - Containerized deployment" -ForegroundColor Gray
Write-Host "   - Better scalability" -ForegroundColor Gray
Write-Host "   - Production-ready" -ForegroundColor Gray
Write-Host ""
Write-Host "3. AWS Elastic Container Service on EC2" -ForegroundColor White
Write-Host "   - More control" -ForegroundColor Gray
Write-Host "   - Cost-effective for larger deployments" -ForegroundColor Gray
Write-Host ""

$deploymentChoice = Read-Host "Enter your choice (1, 2, or 3)"

switch ($deploymentChoice) {
    "1" {
        Write-Host ""
        Write-Host "📦 Deploying with Elastic Beanstalk..." -ForegroundColor Green
        Write-Host ""
        
        # Build all services
        Write-Host "🔨 Building all services..." -ForegroundColor Yellow
        $services = @("employeeManagementService", "roomManagementService", "reservationManagementService", 
                     "restaurantManagementService", "kitchenManagementService", "inventoryManagementService", 
                     "eventManagementService")
        
        foreach ($service in $services) {
            Write-Host "   Building $service..." -ForegroundColor White
            Set-Location "backend\$service"
            mvn clean package -DskipTests -q
            if ($LASTEXITCODE -eq 0) {
                Write-Host "   ✅ $service built successfully" -ForegroundColor Green
            } else {
                Write-Host "   ❌ Failed to build $service" -ForegroundColor Red
                exit 1
            }
            Set-Location "..\.."
        }
        
        Write-Host ""
        Write-Host "🌍 Creating Elastic Beanstalk environments..." -ForegroundColor Yellow
        Write-Host ""
        
        # Check if EB CLI is installed
        try {
            eb --version | Out-Null
            $EB_INSTALLED = $true
        } catch {
            Write-Host "   ⚠️  EB CLI not installed. Installing..." -ForegroundColor Yellow
            pip install awsebcli --upgrade --user
            $EB_INSTALLED = $true
        }
        
        # Initialize EB for each service
        foreach ($service in $services) {
            Write-Host "   Creating environment for $service..." -ForegroundColor White
            Set-Location "backend\$service"
            
            # Initialize EB
            eb init -p java-17 $PROJECT_NAME-$service --region $AWS_REGION
            
            # Create environment
            eb create $PROJECT_NAME-$service-env --instance-type t3.micro --single
            
            if ($LASTEXITCODE -eq 0) {
                Write-Host "   ✅ $service deployed successfully" -ForegroundColor Green
            } else {
                Write-Host "   ❌ Failed to deploy $service" -ForegroundColor Red
            }
            
            Set-Location "..\.."
        }
        
        Write-Host ""
        Write-Host "✅ Deployment completed!" -ForegroundColor Green
        Write-Host ""
        Write-Host "📝 Get your application URLs:" -ForegroundColor Cyan
        Write-Host "   Run: eb status" -ForegroundColor White
    }
    
    "2" {
        if (-not $DOCKER_AVAILABLE) {
            Write-Host ""
            Write-Host "❌ Docker is required for ECS Fargate deployment" -ForegroundColor Red
            Write-Host ""
            Write-Host "Please install Docker Desktop:" -ForegroundColor Yellow
            Write-Host "1. Download from: https://www.docker.com/products/docker-desktop/" -ForegroundColor White
            Write-Host "2. Install Docker Desktop" -ForegroundColor White
            Write-Host "3. Start Docker Desktop" -ForegroundColor White
            Write-Host "4. Run this script again" -ForegroundColor White
            Write-Host ""
            exit 1
        }
        
        Write-Host ""
        Write-Host "🐳 Deploying with ECS Fargate..." -ForegroundColor Green
        Write-Host ""
        Write-Host "This will take approximately 30-45 minutes to complete." -ForegroundColor Yellow
        Write-Host ""
        
        $confirm = Read-Host "Continue? (y/n)"
        if ($confirm -ne "y") {
            Write-Host "Deployment cancelled." -ForegroundColor Yellow
            exit 0
        }
        
        # Run the full deployment
        Write-Host ""
        Write-Host "📋 Please follow the detailed guide in AWS_DEPLOYMENT_GUIDE.md" -ForegroundColor Cyan
        Write-Host ""
        Write-Host "Key steps:" -ForegroundColor White
        Write-Host "1. Build JAR files (mvn clean package)" -ForegroundColor Gray
        Write-Host "2. Create ECR repositories" -ForegroundColor Gray
        Write-Host "3. Build and push Docker images" -ForegroundColor Gray
        Write-Host "4. Create VPC and Security Groups" -ForegroundColor Gray
        Write-Host "5. Create RDS databases" -ForegroundColor Gray
        Write-Host "6. Create ALB and target groups" -ForegroundColor Gray
        Write-Host "7. Create ECS cluster and services" -ForegroundColor Gray
        Write-Host "8. Deploy frontend to S3" -ForegroundColor Gray
        Write-Host ""
        
        # Start with ECR creation
        Write-Host "🏗️  Creating ECR repositories..." -ForegroundColor Yellow
        
        $services = @("employee", "room", "reservation", "restaurant", "kitchen", "inventory", "event")
        
        foreach ($service in $services) {
            $repoName = "hotel-$service-service"
            Write-Host "   Creating repository: $repoName" -ForegroundColor White
            
            $existing = aws ecr describe-repositories --repository-names $repoName --region $AWS_REGION 2>&1
            if ($LASTEXITCODE -ne 0) {
                aws ecr create-repository --repository-name $repoName --region $AWS_REGION --image-scanning-configuration scanOnPush=true | Out-Null
                Write-Host "   ✅ Created $repoName" -ForegroundColor Green
            } else {
                Write-Host "   ℹ️  $repoName already exists" -ForegroundColor Cyan
            }
        }
        
        Write-Host ""
        Write-Host "✅ ECR repositories created!" -ForegroundColor Green
        Write-Host ""
        Write-Host "📝 Next steps:" -ForegroundColor Cyan
        Write-Host "1. Build the JAR files: cd backend/<service> && mvn clean package" -ForegroundColor White
        Write-Host "2. Build Docker images: docker build -t hotel-<service>-service ." -ForegroundColor White
        Write-Host "3. Push to ECR (see AWS_DEPLOYMENT_GUIDE.md for complete commands)" -ForegroundColor White
        Write-Host ""
    }
    
    "3" {
        Write-Host ""
        Write-Host "🖥️  EC2 deployment requires manual setup." -ForegroundColor Yellow
        Write-Host ""
        Write-Host "Please refer to AWS_DEPLOYMENT_GUIDE.md for detailed instructions." -ForegroundColor White
        Write-Host ""
    }
    
    default {
        Write-Host ""
        Write-Host "❌ Invalid choice. Please run the script again." -ForegroundColor Red
        exit 1
    }
}

Write-Host ""
Write-Host "========================================" -ForegroundColor Cyan
Write-Host "  Deployment script completed" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""
Write-Host "📚 For detailed instructions, see: AWS_DEPLOYMENT_GUIDE.md" -ForegroundColor Cyan
Write-Host "❓ Need help? Check CloudWatch logs for errors" -ForegroundColor Cyan
Write-Host ""
