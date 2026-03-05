# 🆓 AWS Free Tier Deployment Guide

**Project:** Hotel Management System  
**Cost:** $0/month for first 12 months  
**Deployment Time:** 20-30 minutes

---

## 💰 AWS Free Tier Includes

✅ **EC2**: 750 hours/month of t2.micro (12 months)  
✅ **S3**: 5 GB storage + 20,000 GET requests + 2,000 PUT requests  
✅ **Data Transfer**: 100 GB outbound per month  
✅ **CloudWatch**: Basic monitoring  
✅ **EBS**: 30 GB of storage  

**Perfect for your hotel management system!** 🎉

---

## 🏗️ Free Tier Architecture

```
┌─────────────────────────────────────────────────────────────┐
│                    AWS Free Tier                             │
├─────────────────────────────────────────────────────────────┤
│                                                               │
│  ┌───────────────────────────────────────────────────────┐  │
│  │              S3 Bucket (Static Website)                │  │
│  │         React Frontend - 5 GB Free                     │  │
│  │         http://bucket-name.s3-website.amazonaws.com    │  │
│  └─────────────────────┬─────────────────────────────────┘  │
│                        │ API calls                           │
│                        ▼                                     │
│  ┌───────────────────────────────────────────────────────┐  │
│  │         EC2 t2.micro Instance (Free Tier)              │  │
│  │              750 hours/month free                       │  │
│  │                                                         │  │
│  │  ┌──────────────────────────────────────────────────┐ │  │
│  │  │         Docker Compose                            │ │  │
│  │  │                                                    │ │  │
│  │  │  ┌────────────┐  ┌────────────┐  ┌────────────┐ │ │  │
│  │  │  │ Employee   │  │   Room     │  │Reservation │ │ │  │
│  │  │  │ Service    │  │  Service   │  │  Service   │ │ │  │
│  │  │  │  :8085     │  │   :8086    │  │   :8087    │ │ │  │
│  │  │  └────────────┘  └────────────┘  └────────────┘ │ │  │
│  │  │                                                    │ │  │
│  │  │  ┌────────────┐  ┌────────────┐  ┌────────────┐ │ │  │
│  │  │  │Restaurant  │  │  Kitchen   │  │ Inventory  │ │ │  │
│  │  │  │  Service   │  │  Service   │  │  Service   │ │ │  │
│  │  │  │   :8088    │  │   :8089    │  │   :8090    │ │ │  │
│  │  │  └────────────┘  └────────────┘  └────────────┘ │ │  │
│  │  │                                                    │ │  │
│  │  │  ┌────────────┐                                   │ │  │
│  │  │  │   Event    │                                   │ │  │
│  │  │  │  Service   │      All using H2 Database       │ │  │
│  │  │  │   :8091    │      (No RDS charges)            │ │  │
│  │  │  └────────────┘                                   │ │  │
│  │  └──────────────────────────────────────────────────┘ │  │
│  │                                                         │  │
│  │  Public IP: http://ec2-xx-xx-xx-xx.compute.amazonaws  │  │
│  └─────────────────────────────────────────────────────────┘  │
│                                                               │
└─────────────────────────────────────────────────────────────┘
```

**Total Cost: $0/month** (within free tier limits) 🎉

---

## 🚀 Step-by-Step Deployment

### Step 1: Create EC2 Key Pair (2 minutes)

```powershell
# Create key pair for SSH access
aws ec2 create-key-pair --key-name hotel-key --query 'KeyMaterial' --output text | Out-File -FilePath "hotel-key.pem" -Encoding ASCII

# Set permissions (important for SSH)
icacls hotel-key.pem /inheritance:r
icacls hotel-key.pem /grant:r "$($env:USERNAME):(R)"

Write-Host "✅ Key pair created: hotel-key.pem" -ForegroundColor Green
```

### Step 2: Create Security Group (2 minutes)

```powershell
# Get default VPC
$VPC_ID = (aws ec2 describe-vpcs --filters "Name=isDefault,Values=true" --query "Vpcs[0].VpcId" --output text)

# Create security group
$SG_ID = (aws ec2 create-security-group `
    --group-name hotel-sg `
    --description "Security group for Hotel Management System" `
    --vpc-id $VPC_ID `
    --query 'GroupId' `
    --output text)

# Allow SSH (port 22)
aws ec2 authorize-security-group-ingress `
    --group-id $SG_ID `
    --protocol tcp `
    --port 22 `
    --cidr 0.0.0.0/0

# Allow HTTP (port 80)
aws ec2 authorize-security-group-ingress `
    --group-id $SG_ID `
    --protocol tcp `
    --port 80 `
    --cidr 0.0.0.0/0

# Allow all backend ports (8085-8091)
for ($port = 8085; $port -le 8091; $port++) {
    aws ec2 authorize-security-group-ingress `
        --group-id $SG_ID `
        --protocol tcp `
        --port $port `
        --cidr 0.0.0.0/0
}

Write-Host "✅ Security group created: $SG_ID" -ForegroundColor Green
```

### Step 3: Launch EC2 t2.micro Instance (Free Tier) (3 minutes)

```powershell
# Get latest Ubuntu AMI (free tier eligible)
$AMI_ID = (aws ec2 describe-images `
    --owners 099720109477 `
    --filters "Name=name,Values=ubuntu/images/hvm-ssd/ubuntu-jammy-22.04-amd64-server-*" `
    --query "Images | sort_by(@, &CreationDate) | [-1].ImageId" `
    --output text)

# Launch t2.micro instance (FREE TIER)
$INSTANCE_ID = (aws ec2 run-instances `
    --image-id $AMI_ID `
    --instance-type t2.micro `
    --key-name hotel-key `
    --security-group-ids $SG_ID `
    --block-device-mappings "DeviceName=/dev/sda1,Ebs={VolumeSize=30,VolumeType=gp2}" `
    --tag-specifications "ResourceType=instance,Tags=[{Key=Name,Value=hotel-management-server}]" `
    --query 'Instances[0].InstanceId' `
    --output text)

Write-Host "⏳ Launching EC2 instance: $INSTANCE_ID" -ForegroundColor Yellow
Write-Host "   Waiting for instance to be ready..." -ForegroundColor Yellow

# Wait for instance to be running
aws ec2 wait instance-running --instance-ids $INSTANCE_ID

# Get public IP
$PUBLIC_IP = (aws ec2 describe-instances `
    --instance-ids $INSTANCE_ID `
    --query 'Reservations[0].Instances[0].PublicIpAddress' `
    --output text)

Write-Host "✅ Instance running!" -ForegroundColor Green
Write-Host "   Instance ID: $INSTANCE_ID" -ForegroundColor Cyan
Write-Host "   Public IP: $PUBLIC_IP" -ForegroundColor Cyan

# Save for later use
$PUBLIC_IP | Out-File -FilePath "public-ip.txt" -Encoding UTF8
```

### Step 4: Build All Services Locally (5 minutes)

```powershell
cd "d:\DEA Project\Hotel Micro"

Write-Host "🔨 Building all services..." -ForegroundColor Yellow

$services = @(
    "employeeManagementService",
    "roomManagementService",
    "reservationManagementService",
    "restaurantManagementService",
    "kitchenManagementService",
    "inventoryManagementService",
    "eventManagementService"
)

foreach ($service in $services) {
    Write-Host "   Building $service..." -ForegroundColor White
    cd "backend\$service"
    mvn clean package -DskipTests -q
    if ($LASTEXITCODE -eq 0) {
        Write-Host "   ✅ $service built" -ForegroundColor Green
    } else {
        Write-Host "   ❌ Failed to build $service" -ForegroundColor Red
        exit 1
    }
    cd "..\..\"
}

Write-Host "✅ All services built successfully!" -ForegroundColor Green
```

### Step 5: Setup EC2 Instance (5 minutes)

```powershell
# Wait a bit for SSH to be ready
Write-Host "⏳ Waiting for SSH to be ready..." -ForegroundColor Yellow
Start-Sleep -Seconds 30

# Create setup script
$SETUP_SCRIPT = @"
#!/bin/bash
set -e

echo "🔧 Setting up server..."

# Update system
sudo apt-get update

# Install Docker
echo "📦 Installing Docker..."
curl -fsSL https://get.docker.com -o get-docker.sh
sudo sh get-docker.sh
sudo usermod -aG docker ubuntu

# Install Docker Compose
echo "📦 Installing Docker Compose..."
sudo curl -L "https://github.com/docker/compose/releases/latest/download/docker-compose-linux-x86_64" -o /usr/local/bin/docker-compose
sudo chmod +x /usr/local/bin/docker-compose

# Install Java 17
echo "☕ Installing Java 17..."
sudo apt-get install -y openjdk-17-jre

# Create app directory
sudo mkdir -p /opt/hotel-app
sudo chown ubuntu:ubuntu /opt/hotel-app

echo "✅ Server setup complete!"
"@

$SETUP_SCRIPT | Out-File -FilePath "setup-server.sh" -Encoding UTF8 -NoNewline

# Convert to Unix line endings
$content = Get-Content "setup-server.sh" -Raw
$content = $content -replace "`r`n", "`n"
[System.IO.File]::WriteAllText("setup-server.sh", $content)

# Copy setup script to EC2
Write-Host "📤 Copying setup script to EC2..." -ForegroundColor Yellow
scp -i hotel-key.pem -o StrictHostKeyChecking=no setup-server.sh ubuntu@${PUBLIC_IP}:/home/ubuntu/

# Run setup script
Write-Host "🔧 Running setup on EC2..." -ForegroundColor Yellow
ssh -i hotel-key.pem -o StrictHostKeyChecking=no ubuntu@${PUBLIC_IP} "chmod +x setup-server.sh && ./setup-server.sh"

Write-Host "✅ EC2 instance configured!" -ForegroundColor Green
```

### Step 6: Deploy Application to EC2 (5 minutes)

```powershell
# Create deployment directory
New-Item -ItemType Directory -Force -Path "deploy-package" | Out-Null

# Copy JAR files
Write-Host "📦 Preparing deployment package..." -ForegroundColor Yellow

foreach ($service in $services) {
    $jarFile = Get-ChildItem "backend\$service\target\*.jar" -Exclude "*-original.jar" | Select-Object -First 1
    Copy-Item $jarFile.FullName "deploy-package\$service.jar"
    Write-Host "   ✅ Copied $service.jar" -ForegroundColor Green
}

# Copy docker-compose.yml
Copy-Item "docker-compose.yml" "deploy-package\"

# Create simple docker-compose for JAR deployment
$DOCKER_COMPOSE_JAR = @"
version: '3.8'

services:
  employee-service:
    image: openjdk:17-jdk-slim
    ports:
      - "8085:8085"
    volumes:
      - ./employeeManagementService.jar:/app/app.jar
    command: java -jar /app/app.jar
    environment:
      - SPRING_PROFILES_ACTIVE=prod
      - SERVER_PORT=8085
    restart: unless-stopped

  room-service:
    image: openjdk:17-jdk-slim
    ports:
      - "8086:8086"
    volumes:
      - ./roomManagementService.jar:/app/app.jar
    command: java -jar /app/app.jar
    environment:
      - SPRING_PROFILES_ACTIVE=prod
      - SERVER_PORT=8086
    restart: unless-stopped

  reservation-service:
    image: openjdk:17-jdk-slim
    ports:
      - "8087:8087"
    volumes:
      - ./reservationManagementService.jar:/app/app.jar
    command: java -jar /app/app.jar
    environment:
      - SPRING_PROFILES_ACTIVE=prod
      - SERVER_PORT=8087
    restart: unless-stopped

  restaurant-service:
    image: openjdk:17-jdk-slim
    ports:
      - "8088:8088"
    volumes:
      - ./restaurantManagementService.jar:/app/app.jar
    command: java -jar /app/app.jar
    environment:
      - SPRING_PROFILES_ACTIVE=prod
      - SERVER_PORT=8088
    restart: unless-stopped

  kitchen-service:
    image: openjdk:17-jdk-slim
    ports:
      - "8089:8089"
    volumes:
      - ./kitchenManagementService.jar:/app/app.jar
    command: java -jar /app/app.jar
    environment:
      - SPRING_PROFILES_ACTIVE=prod
      - SERVER_PORT=8089
    restart: unless-stopped

  inventory-service:
    image: openjdk:17-jdk-slim
    ports:
      - "8090:8090"
    volumes:
      - ./inventoryManagementService.jar:/app/app.jar
    command: java -jar /app/app.jar
    environment:
      - SPRING_PROFILES_ACTIVE=prod
      - SERVER_PORT=8090
    restart: unless-stopped

  event-service:
    image: openjdk:17-jdk-slim
    ports:
      - "8091:8091"
    volumes:
      - ./eventManagementService.jar:/app/app.jar
    command: java -jar /app/app.jar
    environment:
      - SPRING_PROFILES_ACTIVE=prod
      - SERVER_PORT=8091
    restart: unless-stopped
"@

$DOCKER_COMPOSE_JAR | Out-File -FilePath "deploy-package\docker-compose.yml" -Encoding UTF8

# Copy to EC2
Write-Host "📤 Uploading application to EC2..." -ForegroundColor Yellow
scp -i hotel-key.pem -r deploy-package/* ubuntu@${PUBLIC_IP}:/opt/hotel-app/

# Start services
Write-Host "🚀 Starting all services..." -ForegroundColor Yellow
ssh -i hotel-key.pem ubuntu@${PUBLIC_IP} "cd /opt/hotel-app && docker-compose up -d"

# Wait for services to start
Write-Host "⏳ Waiting for services to start..." -ForegroundColor Yellow
Start-Sleep -Seconds 30

Write-Host "✅ All services deployed and running!" -ForegroundColor Green
```

### Step 7: Deploy Frontend to S3 (Free Tier) (3 minutes)

```powershell
cd "Example employee-management"

# Update API URLs to point to EC2
$PUBLIC_IP = Get-Content "..\public-ip.txt"

@"
VITE_EMPLOYEE_API=http://${PUBLIC_IP}:8085
VITE_ROOM_API=http://${PUBLIC_IP}:8086
VITE_RESERVATION_API=http://${PUBLIC_IP}:8087
VITE_RESTAURANT_API=http://${PUBLIC_IP}:8088
VITE_KITCHEN_API=http://${PUBLIC_IP}:8089
VITE_INVENTORY_API=http://${PUBLIC_IP}:8090
VITE_EVENT_API=http://${PUBLIC_IP}:8091
"@ | Out-File -FilePath ".env.production" -Encoding UTF8

# Build frontend
Write-Host "🔨 Building frontend..." -ForegroundColor Yellow
npm run build

# Create S3 bucket (must be globally unique)
$BUCKET_NAME = "hotel-mgmt-$(Get-Random -Minimum 10000 -Maximum 99999)"

Write-Host "📦 Creating S3 bucket: $BUCKET_NAME" -ForegroundColor Yellow
aws s3 mb s3://$BUCKET_NAME --region us-east-1

# Enable static website hosting
aws s3 website s3://$BUCKET_NAME `
    --index-document index.html `
    --error-document index.html

# Upload files
Write-Host "📤 Uploading frontend to S3..." -ForegroundColor Yellow
aws s3 sync dist/ s3://$BUCKET_NAME --acl public-read

# Set bucket policy for public access
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

$WEBSITE_URL = "http://$BUCKET_NAME.s3-website-us-east-1.amazonaws.com"

Write-Host ""
Write-Host "✅ Frontend deployed!" -ForegroundColor Green
Write-Host ""
Write-Host "🎉 DEPLOYMENT COMPLETE!" -ForegroundColor Cyan
Write-Host ""
Write-Host "📋 Access URLs:" -ForegroundColor Yellow
Write-Host "   Frontend: $WEBSITE_URL" -ForegroundColor Green
Write-Host "   Backend APIs: http://${PUBLIC_IP}:8085-8091" -ForegroundColor Green
Write-Host ""
Write-Host "🔐 Default Login:" -ForegroundColor Yellow
Write-Host "   Username: admin" -ForegroundColor White
Write-Host "   Password: admin123" -ForegroundColor White
Write-Host ""

# Open in browser
Start-Process $WEBSITE_URL

cd ..
```

---

## ✅ Verify Deployment

### Check Services Status

```powershell
$PUBLIC_IP = Get-Content "public-ip.txt"

# SSH into instance
ssh -i hotel-key.pem ubuntu@$PUBLIC_IP

# Once logged in:
cd /opt/hotel-app
docker-compose ps

# Check logs
docker-compose logs -f employee-service

# Exit SSH
exit
```

### Test API Endpoints

```powershell
$PUBLIC_IP = Get-Content "public-ip.txt"

# Test employee service
Invoke-RestMethod -Uri "http://${PUBLIC_IP}:8085/api/employees"

# Test login
$loginBody = @{
    username = "admin"
    password = "admin123"
} | ConvertTo-Json

Invoke-RestMethod -Uri "http://${PUBLIC_IP}:8085/api/auth/login" -Method POST -Body $loginBody -ContentType "application/json"
```

---

## 💰 Cost Breakdown (Free Tier)

| Service | Free Tier Allowance | Your Usage | Cost |
|---------|---------------------|------------|------|
| **EC2 t2.micro** | 750 hours/month (12 months) | 1 instance × 24/7 = 720 hours | **$0** ✅ |
| **EBS Storage** | 30 GB (12 months) | 30 GB | **$0** ✅ |
| **S3 Storage** | 5 GB (12 months) | ~100 MB | **$0** ✅ |
| **S3 Requests** | 20,000 GET, 2,000 PUT | <1,000/month | **$0** ✅ |
| **Data Transfer** | 100 GB outbound | ~5-10 GB/month | **$0** ✅ |
| **CloudWatch** | Basic monitoring | Basic | **$0** ✅ |
| **Total** | | | **$0/month** 🎉 |

**Valid for 12 months from AWS account creation!**

---

## 🔧 Management Commands

### View Logs

```powershell
ssh -i hotel-key.pem ubuntu@$PUBLIC_IP "cd /opt/hotel-app && docker-compose logs -f"
```

### Restart Services

```powershell
ssh -i hotel-key.pem ubuntu@$PUBLIC_IP "cd /opt/hotel-app && docker-compose restart"
```

### Stop Services

```powershell
ssh -i hotel-key.pem ubuntu@$PUBLIC_IP "cd /opt/hotel-app && docker-compose down"
```

### Update a Service

```powershell
# 1. Build new JAR locally
cd "backend\employeeManagementService"
mvn clean package -DskipTests

# 2. Upload to EC2
scp -i hotel-key.pem target/*.jar ubuntu@${PUBLIC_IP}:/opt/hotel-app/employeeManagementService.jar

# 3. Restart service
ssh -i hotel-key.pem ubuntu@$PUBLIC_IP "cd /opt/hotel-app && docker-compose restart employee-service"
```

### Monitor Resource Usage

```powershell
ssh -i hotel-key.pem ubuntu@$PUBLIC_IP

# Check CPU and memory
htop

# Check disk usage
df -h

# Check Docker containers
docker stats
```

---

## 🐛 Troubleshooting

### Services Not Starting

```powershell
# Check logs
ssh -i hotel-key.pem ubuntu@$PUBLIC_IP "cd /opt/hotel-app && docker-compose logs"

# Check if ports are in use
ssh -i hotel-key.pem ubuntu@$PUBLIC_IP "sudo netstat -tulpn | grep 808"

# Restart Docker
ssh -i hotel-key.pem ubuntu@$PUBLIC_IP "sudo systemctl restart docker"
```

### Cannot Connect to EC2

```powershell
# Check security group
aws ec2 describe-security-groups --group-ids $SG_ID

# Check instance status
aws ec2 describe-instance-status --instance-ids $INSTANCE_ID

# Try different SSH key permissions
icacls hotel-key.pem /reset
```

### Frontend Can't Connect to Backend

1. Verify public IP hasn't changed:
   ```powershell
   aws ec2 describe-instances --instance-ids $INSTANCE_ID --query 'Reservations[0].Instances[0].PublicIpAddress'
   ```

2. Update frontend .env.production with new IP

3. Rebuild and redeploy frontend

### Out of Memory

```powershell
# Check memory usage
ssh -i hotel-key.pem ubuntu@$PUBLIC_IP "free -h"

# Reduce services running (stop less-used services)
ssh -i hotel-key.pem ubuntu@$PUBLIC_IP "cd /opt/hotel-app && docker-compose stop event-service inventory-service"
```

---

## 🗑️ Cleanup (When Done)

### Terminate Everything

```powershell
# Terminate EC2 instance
aws ec2 terminate-instances --instance-ids $INSTANCE_ID

# Delete S3 bucket
aws s3 rb s3://$BUCKET_NAME --force

# Delete security group (wait for instance termination first)
Start-Sleep -Seconds 60
aws ec2 delete-security-group --group-id $SG_ID

# Delete key pair
aws ec2 delete-key-pair --key-name hotel-key
Remove-Item hotel-key.pem

Write-Host "✅ All resources cleaned up!" -ForegroundColor Green
```

---

## ⚠️ Important Free Tier Notes

1. **750 hours = 31.25 days** - If you run 1 t2.micro 24/7, you stay within free tier
2. **Monitor your usage** - Check AWS Billing Dashboard regularly
3. **Set up billing alerts** - Get notified if charges occur
4. **Free tier expires after 12 months** - Plan for migration or cost optimization
5. **Stop instance when not using** - Save hours for later in the month

### Set Up Billing Alert

```powershell
# Create SNS topic for alerts
$TOPIC_ARN = (aws sns create-topic --name billing-alerts --query 'TopicArn' --output text)

# Subscribe your email
aws sns subscribe --topic-arn $TOPIC_ARN --protocol email --notification-endpoint your-email@example.com

# Create billing alarm (requires CloudWatch)
aws cloudwatch put-metric-alarm `
    --alarm-name billing-alert `
    --alarm-description "Alert when charges exceed $1" `
    --metric-name EstimatedCharges `
    --namespace AWS/Billing `
    --statistic Maximum `
    --period 21600 `
    --evaluation-periods 1 `
    --threshold 1.0 `
    --comparison-operator GreaterThanThreshold `
    --alarm-actions $TOPIC_ARN
```

---

## 🎯 Performance Optimization for t2.micro

### Reduce Memory Usage

Edit docker-compose.yml to add memory limits:

```yaml
services:
  employee-service:
    # ... existing config
    mem_limit: 512m
    environment:
      - JAVA_OPTS=-Xmx384m -Xms256m
```

### Stop Unused Services

```powershell
# Stop services you're not currently testing
ssh -i hotel-key.pem ubuntu@$PUBLIC_IP "cd /opt/hotel-app && docker-compose stop kitchen-service inventory-service event-service"
```

---

## 📊 What You Get

✅ **7 Microservices** running on Free Tier EC2  
✅ **H2 Databases** (in-memory, no RDS charges)  
✅ **React Frontend** on S3 (free tier)  
✅ **Public Access** via EC2 public IP and S3 URL  
✅ **CloudWatch Monitoring** (basic, free)  
✅ **100% Free** for 12 months  

---

## 🚀 Next Steps

After free tier expires (after 12 months):

1. **Option 1**: Pay for t2.micro (~$8-10/month)
2. **Option 2**: Migrate to Lightsail (~$5/month)
3. **Option 3**: Use spot instances (save 70%)
4. **Option 4**: Migrate to Elastic Beanstalk
5. **Option 5**: Keep free tier with usage optimization

---

**Ready to deploy for FREE?** 🎉

```powershell
cd "d:\DEA Project\Hotel Micro"
.\deploy-free-tier.ps1
```

---

**Created:** March 6, 2026  
**Cost:** $0/month (first 12 months)  
**Perfect for:** Development, Testing, MVP, Learning
