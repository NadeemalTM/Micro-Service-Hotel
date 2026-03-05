# 🚀 AWS Deployment Guide - Hotel Management System

**Date:** March 6, 2026  
**Status:** Ready for Deployment  
**System:** 7 Microservices + React Frontend

---

## 📋 Table of Contents

1. [Prerequisites](#prerequisites)
2. [Architecture Overview](#architecture-overview)
3. [Deployment Options](#deployment-options)
4. [Step-by-Step Deployment](#step-by-step-deployment)
5. [Configuration](#configuration)
6. [Testing](#testing)
7. [Monitoring](#monitoring)
8. [Cost Estimation](#cost-estimation)
9. [Troubleshooting](#troubleshooting)

---

## 🔧 Prerequisites

### Required Tools
```powershell
# Install AWS CLI
winget install Amazon.AWSCLI

# Verify installation
aws --version

# Install Docker Desktop (for Windows)
winget install Docker.DockerDesktop

# Verify Docker
docker --version
docker-compose --version

# Install Node.js (if not already installed)
winget install OpenJS.NodeJS

# Install Maven (if not already installed)
winget install Apache.Maven
```

### AWS Account Setup
1. **Create AWS Account**: https://aws.amazon.com/
2. **Create IAM User** with these permissions:
   - AmazonEC2FullAccess
   - AmazonECS_FullAccess
   - AmazonRDSFullAccess
   - AmazonS3FullAccess
   - CloudWatchLogsFullAccess
   - IAMFullAccess (for role creation)

3. **Configure AWS CLI**:
```powershell
aws configure
# AWS Access Key ID: [Enter your key]
# AWS Secret Access Key: [Enter your secret]
# Default region name: us-east-1
# Default output format: json
```

4. **Verify Configuration**:
```powershell
aws sts get-caller-identity
```

---

## 🏗️ Architecture Overview

### Deployment Architecture

```
┌─────────────────────────────────────────────────────────────┐
│                    AWS Cloud Infrastructure                  │
├─────────────────────────────────────────────────────────────┤
│                                                               │
│  ┌──────────────────────────────────────────────────────┐  │
│  │              CloudFront (CDN)                         │  │
│  │         (Frontend Distribution)                       │  │
│  └────────────────────┬──────────────────────────────────┘  │
│                       │                                      │
│  ┌────────────────────▼──────────────────────────────────┐  │
│  │              S3 Bucket                                 │  │
│  │         (React Frontend Static Files)                  │  │
│  └───────────────────────────────────────────────────────┘  │
│                                                               │
│  ┌───────────────────────────────────────────────────────┐  │
│  │        Application Load Balancer (ALB)                 │  │
│  │              Port 80/443                               │  │
│  └────────────────────┬──────────────────────────────────┘  │
│                       │                                      │
│  ┌────────────────────▼──────────────────────────────────┐  │
│  │           ECS Cluster (Fargate)                        │  │
│  │                                                         │  │
│  │  ┌────────────┐  ┌────────────┐  ┌────────────┐      │  │
│  │  │ Employee   │  │   Room     │  │ Restaurant │      │  │
│  │  │ Service    │  │  Service   │  │  Service   │      │  │
│  │  │  :8085     │  │   :8086    │  │   :8088    │      │  │
│  │  └────────────┘  └────────────┘  └────────────┘      │  │
│  │                                                         │  │
│  │  ┌────────────┐  ┌────────────┐  ┌────────────┐      │  │
│  │  │ Reservation│  │  Kitchen   │  │ Inventory  │      │  │
│  │  │  Service   │  │  Service   │  │  Service   │      │  │
│  │  │   :8087    │  │   :8089    │  │   :8090    │      │  │
│  │  └────────────┘  └────────────┘  └────────────┘      │  │
│  │                                                         │  │
│  │  ┌────────────┐                                        │  │
│  │  │   Event    │                                        │  │
│  │  │  Service   │                                        │  │
│  │  │   :8091    │                                        │  │
│  │  └────────────┘                                        │  │
│  └─────────────────────────────────────────────────────────┘  │
│                       │                                      │
│  ┌────────────────────▼──────────────────────────────────┐  │
│  │           Amazon RDS (PostgreSQL)                      │  │
│  │         Multi-AZ Deployment                            │  │
│  │                                                         │  │
│  │  ┌──────────┐ ┌──────────┐ ┌──────────┐              │  │
│  │  │ Employee │ │   Room   │ │Restaurant│  ... (7 DBs) │  │
│  │  │    DB    │ │    DB    │ │    DB    │              │  │
│  │  └──────────┘ └──────────┘ └──────────┘              │  │
│  └─────────────────────────────────────────────────────────┘  │
│                                                               │
│  ┌───────────────────────────────────────────────────────┐  │
│  │         CloudWatch (Monitoring & Logs)                 │  │
│  └───────────────────────────────────────────────────────┘  │
│                                                               │
└─────────────────────────────────────────────────────────────┘
```

---

## 🎯 Deployment Options

### Option 1: AWS ECS Fargate (Recommended) ⭐
- **Pros**: Serverless containers, auto-scaling, managed infrastructure
- **Cons**: Slightly higher cost than EC2
- **Best for**: Production environments, minimal DevOps effort
- **Estimated Cost**: $150-300/month

### Option 2: AWS Elastic Beanstalk
- **Pros**: Easy deployment, integrated with RDS, auto-scaling
- **Cons**: Less control, limited customization
- **Best for**: Quick deployments, prototypes
- **Estimated Cost**: $100-250/month

### Option 3: AWS EC2 with Docker Compose
- **Pros**: Full control, cost-effective for small scale
- **Cons**: Manual management, scaling complexity
- **Best for**: Development/testing, learning
- **Estimated Cost**: $50-150/month

### Option 4: AWS EKS (Kubernetes)
- **Pros**: Advanced orchestration, enterprise-grade
- **Cons**: Complex setup, higher cost, steep learning curve
- **Best for**: Large-scale production, microservices expertise
- **Estimated Cost**: $300-500/month

**📌 We'll proceed with Option 1: ECS Fargate (Most suitable for your system)**

---

## 📝 Step-by-Step Deployment

### Phase 1: Prepare Docker Images

#### Step 1.1: Create Dockerfiles for Backend Services

For each service, create a `Dockerfile` in the service directory:

**Template Dockerfile** (use for all 7 services):
```dockerfile
# Use official OpenJDK 17 image
FROM openjdk:17-jdk-slim

# Set working directory
WORKDIR /app

# Copy the JAR file
COPY target/*.jar app.jar

# Expose service port
EXPOSE 8085

# Set environment variables
ENV SPRING_PROFILES_ACTIVE=prod

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
```

**PowerShell commands to create Dockerfiles:**
```powershell
# Navigate to project root
cd "d:\DEA Project\Hotel Micro"

# Create Dockerfile for Employee Service
@"
FROM openjdk:17-jdk-slim
WORKDIR /app
COPY target/*.jar app.jar
EXPOSE 8085
ENV SPRING_PROFILES_ACTIVE=prod
ENTRYPOINT ["java", "-jar", "app.jar"]
"@ | Out-File -FilePath "backend/employeeManagementService/Dockerfile" -Encoding UTF8

# Create Dockerfile for Room Service
@"
FROM openjdk:17-jdk-slim
WORKDIR /app
COPY target/*.jar app.jar
EXPOSE 8086
ENV SPRING_PROFILES_ACTIVE=prod
ENTRYPOINT ["java", "-jar", "app.jar"]
"@ | Out-File -FilePath "backend/roomManagementService/Dockerfile" -Encoding UTF8

# Create Dockerfile for Reservation Service
@"
FROM openjdk:17-jdk-slim
WORKDIR /app
COPY target/*.jar app.jar
EXPOSE 8087
ENV SPRING_PROFILES_ACTIVE=prod
ENTRYPOINT ["java", "-jar", "app.jar"]
"@ | Out-File -FilePath "backend/reservationManagementService/Dockerfile" -Encoding UTF8

# Create Dockerfile for Restaurant Service
@"
FROM openjdk:17-jdk-slim
WORKDIR /app
COPY target/*.jar app.jar
EXPOSE 8088
ENV SPRING_PROFILES_ACTIVE=prod
ENTRYPOINT ["java", "-jar", "app.jar"]
"@ | Out-File -FilePath "backend/restaurantManagementService/Dockerfile" -Encoding UTF8

# Create Dockerfile for Kitchen Service
@"
FROM openjdk:17-jdk-slim
WORKDIR /app
COPY target/*.jar app.jar
EXPOSE 8089
ENV SPRING_PROFILES_ACTIVE=prod
ENTRYPOINT ["java", "-jar", "app.jar"]
"@ | Out-File -FilePath "backend/kitchenManagementService/Dockerfile" -Encoding UTF8

# Create Dockerfile for Inventory Service
@"
FROM openjdk:17-jdk-slim
WORKDIR /app
COPY target/*.jar app.jar
EXPOSE 8090
ENV SPRING_PROFILES_ACTIVE=prod
ENTRYPOINT ["java", "-jar", "app.jar"]
"@ | Out-File -FilePath "backend/inventoryManagementService/Dockerfile" -Encoding UTF8

# Create Dockerfile for Event Service
@"
FROM openjdk:17-jdk-slim
WORKDIR /app
COPY target/*.jar app.jar
EXPOSE 8091
ENV SPRING_PROFILES_ACTIVE=prod
ENTRYPOINT ["java", "-jar", "app.jar"]
"@ | Out-File -FilePath "backend/eventManagementService/Dockerfile" -Encoding UTF8

Write-Host "✅ All Dockerfiles created successfully!" -ForegroundColor Green
```

#### Step 1.2: Build JAR Files

```powershell
# Build all services
cd "d:\DEA Project\Hotel Micro\backend"

# Employee Service
cd employeeManagementService
mvn clean package -DskipTests
cd ..

# Room Service
cd roomManagementService
mvn clean package -DskipTests
cd ..

# Reservation Service
cd reservationManagementService
mvn clean package -DskipTests
cd ..

# Restaurant Service
cd restaurantManagementService
mvn clean package -DskipTests
cd ..

# Kitchen Service
cd kitchenManagementService
mvn clean package -DskipTests
cd ..

# Inventory Service
cd inventoryManagementService
mvn clean package -DskipTests
cd ..

# Event Service
cd eventManagementService
mvn clean package -DskipTests
cd ..

Write-Host "✅ All services built successfully!" -ForegroundColor Green
```

#### Step 1.3: Create ECR Repositories and Push Images

```powershell
# Set variables
$AWS_REGION = "us-east-1"
$AWS_ACCOUNT_ID = (aws sts get-caller-identity --query Account --output text)

# Login to ECR
aws ecr get-login-password --region $AWS_REGION | docker login --username AWS --password-stdin "$AWS_ACCOUNT_ID.dkr.ecr.$AWS_REGION.amazonaws.com"

# Create ECR repositories
$services = @(
    "hotel-employee-service",
    "hotel-room-service",
    "hotel-reservation-service",
    "hotel-restaurant-service",
    "hotel-kitchen-service",
    "hotel-inventory-service",
    "hotel-event-service"
)

foreach ($service in $services) {
    aws ecr create-repository --repository-name $service --region $AWS_REGION
    Write-Host "✅ Created ECR repository: $service" -ForegroundColor Green
}
```

#### Step 1.4: Build and Push Docker Images

```powershell
cd "d:\DEA Project\Hotel Micro\backend"

# Build and push Employee Service
cd employeeManagementService
docker build -t hotel-employee-service .
docker tag hotel-employee-service:latest "$AWS_ACCOUNT_ID.dkr.ecr.$AWS_REGION.amazonaws.com/hotel-employee-service:latest"
docker push "$AWS_ACCOUNT_ID.dkr.ecr.$AWS_REGION.amazonaws.com/hotel-employee-service:latest"
cd ..

# Build and push Room Service
cd roomManagementService
docker build -t hotel-room-service .
docker tag hotel-room-service:latest "$AWS_ACCOUNT_ID.dkr.ecr.$AWS_REGION.amazonaws.com/hotel-room-service:latest"
docker push "$AWS_ACCOUNT_ID.dkr.ecr.$AWS_REGION.amazonaws.com/hotel-room-service:latest"
cd ..

# Build and push Reservation Service
cd reservationManagementService
docker build -t hotel-reservation-service .
docker tag hotel-reservation-service:latest "$AWS_ACCOUNT_ID.dkr.ecr.$AWS_REGION.amazonaws.com/hotel-reservation-service:latest"
docker push "$AWS_ACCOUNT_ID.dkr.ecr.$AWS_REGION.amazonaws.com/hotel-reservation-service:latest"
cd ..

# Build and push Restaurant Service
cd restaurantManagementService
docker build -t hotel-restaurant-service .
docker tag hotel-restaurant-service:latest "$AWS_ACCOUNT_ID.dkr.ecr.$AWS_REGION.amazonaws.com/hotel-restaurant-service:latest"
docker push "$AWS_ACCOUNT_ID.dkr.ecr.$AWS_REGION.amazonaws.com/hotel-restaurant-service:latest"
cd ..

# Build and push Kitchen Service
cd kitchenManagementService
docker build -t hotel-kitchen-service .
docker tag hotel-kitchen-service:latest "$AWS_ACCOUNT_ID.dkr.ecr.$AWS_REGION.amazonaws.com/hotel-kitchen-service:latest"
docker push "$AWS_ACCOUNT_ID.dkr.ecr.$AWS_REGION.amazonaws.com/hotel-kitchen-service:latest"
cd ..

# Build and push Inventory Service
cd inventoryManagementService
docker build -t hotel-inventory-service .
docker tag hotel-inventory-service:latest "$AWS_ACCOUNT_ID.dkr.ecr.$AWS_REGION.amazonaws.com/hotel-inventory-service:latest"
docker push "$AWS_ACCOUNT_ID.dkr.ecr.$AWS_REGION.amazonaws.com/hotel-inventory-service:latest"
cd ..

# Build and push Event Service
cd eventManagementService
docker build -t hotel-event-service .
docker tag hotel-event-service:latest "$AWS_ACCOUNT_ID.dkr.ecr.$AWS_REGION.amazonaws.com/hotel-event-service:latest"
docker push "$AWS_ACCOUNT_ID.dkr.ecr.$AWS_REGION.amazonaws.com/hotel-event-service:latest"
cd ..

Write-Host "✅ All Docker images pushed to ECR!" -ForegroundColor Green
```

---

### Phase 2: Setup AWS Infrastructure

#### Step 2.1: Create VPC and Security Groups

```powershell
# Create VPC
$VPC_ID = (aws ec2 create-vpc --cidr-block 10.0.0.0/16 --query Vpc.VpcId --output text)
aws ec2 create-tags --resources $VPC_ID --tags Key=Name,Value=hotel-vpc

# Create Internet Gateway
$IGW_ID = (aws ec2 create-internet-gateway --query InternetGateway.InternetGatewayId --output text)
aws ec2 attach-internet-gateway --vpc-id $VPC_ID --internet-gateway-id $IGW_ID

# Create Subnets (2 public subnets for high availability)
$SUBNET1_ID = (aws ec2 create-subnet --vpc-id $VPC_ID --cidr-block 10.0.1.0/24 --availability-zone us-east-1a --query Subnet.SubnetId --output text)
$SUBNET2_ID = (aws ec2 create-subnet --vpc-id $VPC_ID --cidr-block 10.0.2.0/24 --availability-zone us-east-1b --query Subnet.SubnetId --output text)

aws ec2 create-tags --resources $SUBNET1_ID --tags Key=Name,Value=hotel-subnet-1
aws ec2 create-tags --resources $SUBNET2_ID --tags Key=Name,Value=hotel-subnet-2

# Create Route Table
$RT_ID = (aws ec2 create-route-table --vpc-id $VPC_ID --query RouteTable.RouteTableId --output text)
aws ec2 create-route --route-table-id $RT_ID --destination-cidr-block 0.0.0.0/0 --gateway-id $IGW_ID
aws ec2 associate-route-table --subnet-id $SUBNET1_ID --route-table-id $RT_ID
aws ec2 associate-route-table --subnet-id $SUBNET2_ID --route-table-id $RT_ID

# Create Security Group for ALB
$ALB_SG_ID = (aws ec2 create-security-group --group-name hotel-alb-sg --description "Security group for ALB" --vpc-id $VPC_ID --query GroupId --output text)
aws ec2 authorize-security-group-ingress --group-id $ALB_SG_ID --protocol tcp --port 80 --cidr 0.0.0.0/0
aws ec2 authorize-security-group-ingress --group-id $ALB_SG_ID --protocol tcp --port 443 --cidr 0.0.0.0/0

# Create Security Group for ECS Services
$ECS_SG_ID = (aws ec2 create-security-group --group-name hotel-ecs-sg --description "Security group for ECS services" --vpc-id $VPC_ID --query GroupId --output text)
aws ec2 authorize-security-group-ingress --group-id $ECS_SG_ID --protocol tcp --port 8085 --source-group $ALB_SG_ID
aws ec2 authorize-security-group-ingress --group-id $ECS_SG_ID --protocol tcp --port 8086 --source-group $ALB_SG_ID
aws ec2 authorize-security-group-ingress --group-id $ECS_SG_ID --protocol tcp --port 8087 --source-group $ALB_SG_ID
aws ec2 authorize-security-group-ingress --group-id $ECS_SG_ID --protocol tcp --port 8088 --source-group $ALB_SG_ID
aws ec2 authorize-security-group-ingress --group-id $ECS_SG_ID --protocol tcp --port 8089 --source-group $ALB_SG_ID
aws ec2 authorize-security-group-ingress --group-id $ECS_SG_ID --protocol tcp --port 8090 --source-group $ALB_SG_ID
aws ec2 authorize-security-group-ingress --group-id $ECS_SG_ID --protocol tcp --port 8091 --source-group $ALB_SG_ID

# Create Security Group for RDS
$RDS_SG_ID = (aws ec2 create-security-group --group-name hotel-rds-sg --description "Security group for RDS" --vpc-id $VPC_ID --query GroupId --output text)
aws ec2 authorize-security-group-ingress --group-id $RDS_SG_ID --protocol tcp --port 5432 --source-group $ECS_SG_ID

Write-Host "✅ VPC and Security Groups created!" -ForegroundColor Green
Write-Host "VPC ID: $VPC_ID" -ForegroundColor Cyan
Write-Host "Subnet 1 ID: $SUBNET1_ID" -ForegroundColor Cyan
Write-Host "Subnet 2 ID: $SUBNET2_ID" -ForegroundColor Cyan
Write-Host "ALB Security Group: $ALB_SG_ID" -ForegroundColor Cyan
Write-Host "ECS Security Group: $ECS_SG_ID" -ForegroundColor Cyan
Write-Host "RDS Security Group: $RDS_SG_ID" -ForegroundColor Cyan
```

#### Step 2.2: Create RDS Databases

```powershell
# Create DB Subnet Group
aws rds create-db-subnet-group `
    --db-subnet-group-name hotel-db-subnet-group `
    --db-subnet-group-description "Subnet group for hotel databases" `
    --subnet-ids $SUBNET1_ID $SUBNET2_ID

# Create PostgreSQL instances for each service
$services = @(
    @{Name="employee"; Port=5432},
    @{Name="room"; Port=5433},
    @{Name="reservation"; Port=5434},
    @{Name="restaurant"; Port=5435},
    @{Name="kitchen"; Port=5436},
    @{Name="inventory"; Port=5437},
    @{Name="event"; Port=5438}
)

foreach ($service in $services) {
    aws rds create-db-instance `
        --db-instance-identifier "hotel-$($service.Name)-db" `
        --db-instance-class db.t3.micro `
        --engine postgres `
        --master-username admin `
        --master-user-password "HotelAdmin2026!" `
        --allocated-storage 20 `
        --vpc-security-group-ids $RDS_SG_ID `
        --db-subnet-group-name hotel-db-subnet-group `
        --backup-retention-period 7 `
        --preferred-backup-window "03:00-04:00" `
        --preferred-maintenance-window "Mon:04:00-Mon:05:00" `
        --no-publicly-accessible `
        --db-name "$($service.Name)db"
    
    Write-Host "✅ Creating RDS instance: hotel-$($service.Name)-db" -ForegroundColor Green
}

Write-Host "⏳ Waiting for databases to be available (this takes ~10 minutes)..." -ForegroundColor Yellow
```

#### Step 2.3: Create Application Load Balancer

```powershell
# Create ALB
$ALB_ARN = (aws elbv2 create-load-balancer `
    --name hotel-alb `
    --subnets $SUBNET1_ID $SUBNET2_ID `
    --security-groups $ALB_SG_ID `
    --scheme internet-facing `
    --type application `
    --ip-address-type ipv4 `
    --query 'LoadBalancers[0].LoadBalancerArn' `
    --output text)

# Create Target Groups for each service
$targetGroups = @{}

$services = @("employee", "room", "reservation", "restaurant", "kitchen", "inventory", "event")
$ports = @(8085, 8086, 8087, 8088, 8089, 8090, 8091)

for ($i = 0; $i -lt $services.Length; $i++) {
    $tg_arn = (aws elbv2 create-target-group `
        --name "hotel-$($services[$i])-tg" `
        --protocol HTTP `
        --port $ports[$i] `
        --vpc-id $VPC_ID `
        --target-type ip `
        --health-check-enabled `
        --health-check-path "/actuator/health" `
        --health-check-interval-seconds 30 `
        --health-check-timeout-seconds 5 `
        --healthy-threshold-count 2 `
        --unhealthy-threshold-count 3 `
        --query 'TargetGroups[0].TargetGroupArn' `
        --output text)
    
    $targetGroups[$services[$i]] = $tg_arn
    Write-Host "✅ Created target group: hotel-$($services[$i])-tg" -ForegroundColor Green
}

# Create Listener with routing rules
$LISTENER_ARN = (aws elbv2 create-listener `
    --load-balancer-arn $ALB_ARN `
    --protocol HTTP `
    --port 80 `
    --default-actions Type=forward,TargetGroupArn=$($targetGroups["employee"]) `
    --query 'Listeners[0].ListenerArn' `
    --output text)

# Create listener rules for path-based routing
$priority = 1
foreach ($service in $services) {
    if ($service -ne "employee") {
        aws elbv2 create-rule `
            --listener-arn $LISTENER_ARN `
            --priority $priority `
            --conditions Field=path-pattern,Values="/api/$service/*" `
            --actions Type=forward,TargetGroupArn=$($targetGroups[$service])
        
        $priority++
    }
}

Write-Host "✅ Application Load Balancer created!" -ForegroundColor Green
Write-Host "ALB ARN: $ALB_ARN" -ForegroundColor Cyan
```

#### Step 2.4: Create ECS Cluster

```powershell
# Create ECS Cluster
aws ecs create-cluster --cluster-name hotel-cluster

# Create IAM Role for ECS Task Execution
$TRUST_POLICY = @"
{
  "Version": "2012-10-17",
  "Statement": [
    {
      "Effect": "Allow",
      "Principal": {
        "Service": "ecs-tasks.amazonaws.com"
      },
      "Action": "sts:AssumeRole"
    }
  ]
}
"@

$TRUST_POLICY | Out-File -FilePath "trust-policy.json" -Encoding UTF8

aws iam create-role `
    --role-name ecsTaskExecutionRole `
    --assume-role-policy-document file://trust-policy.json

aws iam attach-role-policy `
    --role-name ecsTaskExecutionRole `
    --policy-arn arn:aws:iam::aws:policy/service-role/AmazonECSTaskExecutionRolePolicy

Write-Host "✅ ECS Cluster created!" -ForegroundColor Green
```

---

### Phase 3: Deploy Services to ECS

#### Step 3.1: Create Task Definitions

Create a PowerShell script to generate task definitions:

```powershell
cd "d:\DEA Project\Hotel Micro"

# Get RDS endpoints (wait for them to be available)
$rdsEndpoints = @{}
$services = @("employee", "room", "reservation", "restaurant", "kitchen", "inventory", "event")

foreach ($service in $services) {
    $endpoint = (aws rds describe-db-instances `
        --db-instance-identifier "hotel-$service-db" `
        --query 'DBInstances[0].Endpoint.Address' `
        --output text)
    $rdsEndpoints[$service] = $endpoint
}

# Function to create task definition
function Create-TaskDefinition {
    param(
        [string]$ServiceName,
        [int]$Port,
        [string]$DbEndpoint
    )
    
    $taskDef = @"
{
  "family": "hotel-$ServiceName-task",
  "networkMode": "awsvpc",
  "requiresCompatibilities": ["FARGATE"],
  "cpu": "256",
  "memory": "512",
  "executionRoleArn": "arn:aws:iam::${AWS_ACCOUNT_ID}:role/ecsTaskExecutionRole",
  "containerDefinitions": [
    {
      "name": "$ServiceName-container",
      "image": "${AWS_ACCOUNT_ID}.dkr.ecr.${AWS_REGION}.amazonaws.com/hotel-$ServiceName-service:latest",
      "portMappings": [
        {
          "containerPort": $Port,
          "protocol": "tcp"
        }
      ],
      "essential": true,
      "environment": [
        {
          "name": "SPRING_PROFILES_ACTIVE",
          "value": "prod"
        },
        {
          "name": "SPRING_DATASOURCE_URL",
          "value": "jdbc:postgresql://${DbEndpoint}:5432/${ServiceName}db"
        },
        {
          "name": "SPRING_DATASOURCE_USERNAME",
          "value": "admin"
        },
        {
          "name": "SPRING_DATASOURCE_PASSWORD",
          "value": "HotelAdmin2026!"
        },
        {
          "name": "JWT_SECRET",
          "value": "YourSecureJWTSecretKey2026!@#$%"
        }
      ],
      "logConfiguration": {
        "logDriver": "awslogs",
        "options": {
          "awslogs-create-group": "true",
          "awslogs-group": "/ecs/hotel-$ServiceName",
          "awslogs-region": "${AWS_REGION}",
          "awslogs-stream-prefix": "ecs"
        }
      }
    }
  ]
}
"@
    
    $taskDef | Out-File -FilePath "task-def-$ServiceName.json" -Encoding UTF8
    
    aws ecs register-task-definition --cli-input-json file://task-def-$ServiceName.json
    Write-Host "✅ Registered task definition: hotel-$ServiceName-task" -ForegroundColor Green
}

# Create task definitions for all services
$servicePorts = @{
    "employee" = 8085
    "room" = 8086
    "reservation" = 8087
    "restaurant" = 8088
    "kitchen" = 8089
    "inventory" = 8090
    "event" = 8091
}

foreach ($service in $services) {
    Create-TaskDefinition -ServiceName $service -Port $servicePorts[$service] -DbEndpoint $rdsEndpoints[$service]
}

Write-Host "✅ All task definitions registered!" -ForegroundColor Green
```

#### Step 3.2: Create ECS Services

```powershell
# Create ECS services for each microservice
foreach ($service in $services) {
    aws ecs create-service `
        --cluster hotel-cluster `
        --service-name "hotel-$service-service" `
        --task-definition "hotel-$service-task" `
        --desired-count 1 `
        --launch-type FARGATE `
        --network-configuration "awsvpcConfiguration={subnets=[$SUBNET1_ID,$SUBNET2_ID],securityGroups=[$ECS_SG_ID],assignPublicIp=ENABLED}" `
        --load-balancers "targetGroupArn=$($targetGroups[$service]),containerName=$service-container,containerPort=$($servicePorts[$service])"
    
    Write-Host "✅ Created ECS service: hotel-$service-service" -ForegroundColor Green
}

Write-Host "🎉 All backend services deployed to ECS!" -ForegroundColor Green
```

---

### Phase 4: Deploy Frontend to S3 + CloudFront

#### Step 4.1: Update Frontend API Configuration

Update the frontend to point to ALB:

```powershell
# Get ALB DNS name
$ALB_DNS = (aws elbv2 describe-load-balancers --names hotel-alb --query 'LoadBalancers[0].DNSName' --output text)

Write-Host "ALB DNS: http://$ALB_DNS" -ForegroundColor Cyan
```

Create production environment file:

```powershell
cd "d:\DEA Project\Hotel Micro\Example employee-management"

@"
VITE_API_BASE_URL=http://$ALB_DNS
VITE_EMPLOYEE_API=http://$ALB_DNS
VITE_ROOM_API=http://$ALB_DNS
VITE_RESERVATION_API=http://$ALB_DNS
VITE_RESTAURANT_API=http://$ALB_DNS
VITE_KITCHEN_API=http://$ALB_DNS
VITE_INVENTORY_API=http://$ALB_DNS
VITE_EVENT_API=http://$ALB_DNS
"@ | Out-File -FilePath ".env.production" -Encoding UTF8
```

#### Step 4.2: Build Frontend for Production

```powershell
# Build frontend
npm run build

Write-Host "✅ Frontend built successfully!" -ForegroundColor Green
```

#### Step 4.3: Create S3 Bucket and Deploy

```powershell
# Create S3 bucket for frontend
$BUCKET_NAME = "hotel-management-frontend-$(Get-Random -Minimum 1000 -Maximum 9999)"

aws s3 mb s3://$BUCKET_NAME --region $AWS_REGION

# Enable static website hosting
aws s3 website s3://$BUCKET_NAME --index-document index.html --error-document index.html

# Upload build files
aws s3 sync dist/ s3://$BUCKET_NAME --delete

# Make bucket public
$BUCKET_POLICY = @"
{
  "Version": "2012-10-17",
  "Statement": [
    {
      "Sid": "PublicReadGetObject",
      "Effect": "Allow",
      "Principal": "*",
      "Action": "s3:GetObject",
      "Resource": "arn:aws:s3:::$BUCKET_NAME/*"
    }
  ]
}
"@

$BUCKET_POLICY | Out-File -FilePath "bucket-policy.json" -Encoding UTF8
aws s3api put-bucket-policy --bucket $BUCKET_NAME --policy file://bucket-policy.json

Write-Host "✅ Frontend deployed to S3!" -ForegroundColor Green
Write-Host "Website URL: http://$BUCKET_NAME.s3-website-$AWS_REGION.amazonaws.com" -ForegroundColor Cyan
```

#### Step 4.4: Create CloudFront Distribution (Optional - for HTTPS and better performance)

```powershell
# Create CloudFront distribution
$CF_CONFIG = @"
{
  "CallerReference": "hotel-frontend-$(Get-Date -Format 'yyyyMMddHHmmss')",
  "Comment": "Hotel Management Frontend",
  "Enabled": true,
  "Origins": {
    "Quantity": 1,
    "Items": [
      {
        "Id": "S3-$BUCKET_NAME",
        "DomainName": "$BUCKET_NAME.s3-website-$AWS_REGION.amazonaws.com",
        "CustomOriginConfig": {
          "HTTPPort": 80,
          "HTTPSPort": 443,
          "OriginProtocolPolicy": "http-only"
        }
      }
    ]
  },
  "DefaultRootObject": "index.html",
  "DefaultCacheBehavior": {
    "TargetOriginId": "S3-$BUCKET_NAME",
    "ViewerProtocolPolicy": "redirect-to-https",
    "AllowedMethods": {
      "Quantity": 2,
      "Items": ["GET", "HEAD"]
    },
    "ForwardedValues": {
      "QueryString": false,
      "Cookies": {
        "Forward": "none"
      }
    },
    "MinTTL": 0,
    "TrustedSigners": {
      "Enabled": false,
      "Quantity": 0
    }
  }
}
"@

$CF_CONFIG | Out-File -FilePath "cloudfront-config.json" -Encoding UTF8
$CF_DIST_ID = (aws cloudfront create-distribution --distribution-config file://cloudfront-config.json --query 'Distribution.Id' --output text)

Write-Host "✅ CloudFront distribution created!" -ForegroundColor Green
Write-Host "Distribution ID: $CF_DIST_ID" -ForegroundColor Cyan
Write-Host "⏳ CloudFront deployment takes 15-20 minutes..." -ForegroundColor Yellow
```

---

## ✅ Verification & Testing

### Step 5.1: Check Service Health

```powershell
# Check ECS services status
aws ecs describe-services --cluster hotel-cluster --services `
    hotel-employee-service `
    hotel-room-service `
    hotel-reservation-service `
    hotel-restaurant-service `
    hotel-kitchen-service `
    hotel-inventory-service `
    hotel-event-service `
    --query 'services[*].[serviceName,status,runningCount]' --output table

# Check ALB target health
aws elbv2 describe-target-health --target-group-arn $($targetGroups["employee"]) --output table
```

### Step 5.2: Test API Endpoints

```powershell
# Get ALB DNS
$ALB_DNS = (aws elbv2 describe-load-balancers --names hotel-alb --query 'LoadBalancers[0].DNSName' --output text)

# Test employee service
curl "http://$ALB_DNS/api/employees" -Headers @{"Accept"="application/json"}

# Test authentication
$loginBody = @{
    username = "admin"
    password = "admin123"
} | ConvertTo-Json

Invoke-RestMethod -Uri "http://$ALB_DNS/api/auth/login" -Method POST -Body $loginBody -ContentType "application/json"
```

### Step 5.3: Access Frontend

```powershell
# Get S3 website URL
$S3_URL = "http://$BUCKET_NAME.s3-website-$AWS_REGION.amazonaws.com"
Write-Host "Frontend URL: $S3_URL" -ForegroundColor Green

# Open in browser
Start-Process $S3_URL
```

---

## 📊 Monitoring & Logs

### CloudWatch Logs

```powershell
# View logs for a service
aws logs tail /ecs/hotel-employee --follow

# View all log groups
aws logs describe-log-groups --log-group-name-prefix "/ecs/hotel-"
```

### CloudWatch Metrics

```powershell
# View ECS service metrics
aws cloudwatch get-metric-statistics `
    --namespace AWS/ECS `
    --metric-name CPUUtilization `
    --dimensions Name=ServiceName,Value=hotel-employee-service Name=ClusterName,Value=hotel-cluster `
    --start-time (Get-Date).AddHours(-1).ToString("yyyy-MM-ddTHH:mm:ss") `
    --end-time (Get-Date).ToString("yyyy-MM-ddTHH:mm:ss") `
    --period 300 `
    --statistics Average
```

---

## 💰 Cost Estimation

### Monthly Cost Breakdown (Estimated)

| Service | Component | Cost |
|---------|-----------|------|
| **ECS Fargate** | 7 services × 0.25 vCPU × 0.5 GB × $0.04048/hr | ~$50/month |
| **RDS PostgreSQL** | 7 × db.t3.micro (20GB storage) | ~$90/month |
| **Application Load Balancer** | 1 ALB + data transfer | ~$25/month |
| **S3** | Frontend hosting + CloudFront | ~$5/month |
| **CloudWatch Logs** | Log storage and queries | ~$10/month |
| **Data Transfer** | Outbound data transfer | ~$15/month |
| **NAT Gateway** | (if using private subnets) | ~$35/month |
| **Total** | | **$195-230/month** |

### Cost Optimization Tips

1. **Use Reserved Instances** for predictable workloads (save 30-60%)
2. **Enable Auto Scaling** to scale down during low traffic
3. **Use Spot Instances** for non-critical services (save 70%)
4. **Implement caching** to reduce database queries
5. **Use CloudFront** to reduce data transfer costs
6. **Clean up unused resources** regularly

---

## 🐛 Troubleshooting

### Common Issues

#### 1. Services not starting
```powershell
# Check service events
aws ecs describe-services --cluster hotel-cluster --services hotel-employee-service --query 'services[0].events[0:5]'

# Check task logs
aws logs tail /ecs/hotel-employee --since 10m
```

#### 2. Database connection issues
```powershell
# Verify security group allows ECS to RDS
aws ec2 describe-security-groups --group-ids $RDS_SG_ID

# Test from ECS task (use AWS Systems Manager Session Manager)
aws ecs execute-command --cluster hotel-cluster --task <task-id> --container employee-container --interactive --command "/bin/sh"
```

#### 3. ALB health checks failing
```powershell
# Check target health
aws elbv2 describe-target-health --target-group-arn <target-group-arn>

# Verify actuator endpoint
curl http://$ALB_DNS/actuator/health
```

#### 4. Frontend can't connect to backend
- Check CORS configuration in Spring Boot
- Verify ALB security group allows inbound traffic
- Check frontend environment variables
- Verify JWT token is being sent correctly

---

## 🔄 CI/CD Pipeline (Optional)

### Setup GitHub Actions for Auto-Deploy

Create `.github/workflows/deploy.yml`:

```yaml
name: Deploy to AWS

on:
  push:
    branches: [ main ]

jobs:
  deploy-backend:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v2
      
      - name: Configure AWS credentials
        uses: aws-actions/configure-aws-credentials@v1
        with:
          aws-access-key-id: ${{ secrets.AWS_ACCESS_KEY_ID }}
          aws-secret-access-key: ${{ secrets.AWS_SECRET_ACCESS_KEY }}
          aws-region: us-east-1
      
      - name: Login to Amazon ECR
        id: login-ecr
        uses: aws-actions/amazon-ecr-login@v1
      
      - name: Build and push Docker images
        env:
          ECR_REGISTRY: ${{ steps.login-ecr.outputs.registry }}
        run: |
          # Build and push all services
          cd backend/employeeManagementService
          docker build -t $ECR_REGISTRY/hotel-employee-service:latest .
          docker push $ECR_REGISTRY/hotel-employee-service:latest
          # Repeat for other services...
      
      - name: Update ECS services
        run: |
          aws ecs update-service --cluster hotel-cluster --service hotel-employee-service --force-new-deployment
          # Repeat for other services...
  
  deploy-frontend:
    runs-on: ubuntu-latest
    needs: deploy-backend
    steps:
      - uses: actions/checkout@v2
      
      - name: Setup Node.js
        uses: actions/setup-node@v2
        with:
          node-version: '18'
      
      - name: Build frontend
        run: |
          cd frontend
          npm install
          npm run build
      
      - name: Deploy to S3
        run: |
          aws s3 sync frontend/dist/ s3://${{ secrets.S3_BUCKET }} --delete
      
      - name: Invalidate CloudFront
        run: |
          aws cloudfront create-invalidation --distribution-id ${{ secrets.CF_DIST_ID }} --paths "/*"
```

---

## 📝 Deployment Checklist

- [ ] AWS CLI installed and configured
- [ ] Docker Desktop installed and running
- [ ] All services built successfully (JAR files)
- [ ] ECR repositories created
- [ ] Docker images pushed to ECR
- [ ] VPC and subnets created
- [ ] Security groups configured
- [ ] RDS databases created and available
- [ ] Application Load Balancer created
- [ ] Target groups configured
- [ ] ECS cluster created
- [ ] Task definitions registered
- [ ] ECS services deployed
- [ ] Services passing health checks
- [ ] Frontend built for production
- [ ] S3 bucket created
- [ ] Frontend deployed to S3
- [ ] CloudFront distribution created (optional)
- [ ] API endpoints tested
- [ ] Frontend can connect to backend
- [ ] Authentication working
- [ ] All CRUD operations functional
- [ ] CloudWatch monitoring enabled
- [ ] Cost alerts configured

---

## 🎉 Post-Deployment

### Access URLs

After deployment, you'll have:

1. **Backend API**: `http://<ALB-DNS>/api/*`
2. **Frontend**: `http://<S3-BUCKET>.s3-website-<region>.amazonaws.com`
3. **CloudFront** (if configured): `https://<cloudfront-domain>.cloudfront.net`

### Next Steps

1. **Custom Domain**: Register domain and configure Route 53
2. **SSL Certificate**: Use AWS Certificate Manager for HTTPS
3. **Monitoring**: Set up CloudWatch alarms
4. **Backups**: Configure automated RDS backups
5. **Scaling**: Configure auto-scaling policies
6. **Security**: Implement WAF, enable VPC Flow Logs
7. **Cost Optimization**: Review and optimize resource usage

---

**🎯 Deployment Status**: Ready to Execute  
**📅 Created**: March 6, 2026  
**👥 Team**: NSBM Group 03

**Need Help?** Contact AWS Support or review CloudWatch logs for troubleshooting.
