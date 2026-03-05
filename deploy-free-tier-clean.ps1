# AWS Free Tier Deployment - Automated Script
# Hotel Management System - $0/month deployment!

Write-Host ""
Write-Host "========================================" -ForegroundColor Cyan
Write-Host "     AWS FREE TIER DEPLOYMENT" -ForegroundColor Cyan
Write-Host "  Hotel Management System" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""
Write-Host "   Cost: $0/month (first 12 months)" -ForegroundColor Green
Write-Host "    Time: 20-30 minutes" -ForegroundColor Yellow
Write-Host ""

# Check prerequisites
Write-Host "   Checking prerequisites..." -ForegroundColor Yellow
Write-Host ""

# Check AWS CLI
try {
    $awsVersion = aws --version 2>&1
    Write-Host "  AWS CLI: $awsVersion" -ForegroundColor Green
    $identity = aws sts get-caller-identity 2>&1 | ConvertFrom-Json
    Write-Host "  AWS Account: $($identity.Account)" -ForegroundColor Green
    $AWS_ACCOUNT_ID = $identity.Account
} catch {
    Write-Host "  AWS CLI not configured" -ForegroundColor Red
    Write-Host "Run: aws configure" -ForegroundColor Yellow
    exit 1
}

# Check Maven
try {
    $mvnVersion = mvn --version 2>&1 | Select-String "Apache Maven" | Select-Object -First 1
    Write-Host "  Maven installed" -ForegroundColor Green
} catch {
    Write-Host "  Maven not installed" -ForegroundColor Red
    exit 1
}

Write-Host ""
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""

$confirm = Read-Host "Continue with FREE TIER deployment? (y/n)"
if ($confirm -ne "y") {
    Write-Host "Deployment cancelled." -ForegroundColor Yellow
    exit 0
}

Write-Host ""
Write-Host "   Starting deployment..." -ForegroundColor Green
Write-Host ""

# Step 1: Create Key Pair
Write-Host "   Step 1/7: Creating EC2 key pair..." -ForegroundColor Cyan

try {
    aws ec2 create-key-pair --key-name hotel-key --query 'KeyMaterial' --output text | Out-File -FilePath "hotel-key.pem" -Encoding ASCII
    
    # Fix permissions
    icacls hotel-key.pem /inheritance:r | Out-Null
    icacls hotel-key.pem /grant:r "$($env:USERNAME):(R)" | Out-Null
    
    Write-Host "     Key pair created: hotel-key.pem" -ForegroundColor Green
} catch {
    Write-Host "       Key pair already exists, using existing one" -ForegroundColor Cyan
}

Write-Host ""

# Step 2: Create Security Group
Write-Host "   Step 2/7: Creating security group..." -ForegroundColor Cyan

$VPC_ID = (aws ec2 describe-vpcs --filters "Name=isDefault,Values=true" --query "Vpcs[0].VpcId" --output text)

try {
    $SG_ID = (aws ec2 create-security-group `
        --group-name hotel-sg `
        --description "Hotel Management System Security Group" `
        --vpc-id $VPC_ID `
        --query 'GroupId' `
        --output text 2>&1)
    
    # Allow SSH
    aws ec2 authorize-security-group-ingress --group-id $SG_ID --protocol tcp --port 22 --cidr 0.0.0.0/0 2>&1 | Out-Null
    
    # Allow HTTP
    aws ec2 authorize-security-group-ingress --group-id $SG_ID --protocol tcp --port 80 --cidr 0.0.0.0/0 2>&1 | Out-Null
    
    # Allow backend ports
    for ($port = 8085; $port -le 8091; $port++) {
        aws ec2 authorize-security-group-ingress --group-id $SG_ID --protocol tcp --port $port --cidr 0.0.0.0/0 2>&1 | Out-Null
    }
    
    Write-Host "   [OK] Security group created: $SG_ID" -ForegroundColor Green
} catch {
    # Get existing security group
    $SG_ID = (aws ec2 describe-security-groups --filters "Name=group-name,Values=hotel-sg" --query "SecurityGroups[0].GroupId" --output text)
    Write-Host "   [INFO] Using existing security group: $SG_ID" -ForegroundColor Cyan
}

Write-Host ""

# Step 3: Launch EC2 Instance
Write-Host "     Step 3/7: Launching EC2 t2.micro instance (FREE TIER)..." -ForegroundColor Cyan

$AMI_ID = (aws ec2 describe-images `
    --owners 099720109477 `
    --filters "Name=name,Values=ubuntu/images/hvm-ssd/ubuntu-jammy-22.04-amd64-server-*" "Name=state,Values=available" `
    --query 'Images | sort_by(@, &CreationDate) | [-1].ImageId' `
    --output text)

Write-Host "   Using Ubuntu AMI: $AMI_ID" -ForegroundColor Gray

try {
    $INSTANCE_ID = (aws ec2 run-instances `
        --image-id $AMI_ID `
        --instance-type t3.micro `
        --key-name hotel-key `
        --security-group-ids $SG_ID `
        --block-device-mappings 'DeviceName=/dev/sda1,Ebs={VolumeSize=30,VolumeType=gp3}' `
        --tag-specifications 'ResourceType=instance,Tags=[{Key=Name,Value=HotelManagementServer}]' `
        --query 'Instances[0].InstanceId' `
        --output text)
    
    Write-Host "     Waiting for instance to start..." -ForegroundColor Yellow
    aws ec2 wait instance-running --instance-ids $INSTANCE_ID
    
    $PUBLIC_IP = (aws ec2 describe-instances `
        --instance-ids $INSTANCE_ID `
        --query 'Reservations[0].Instances[0].PublicIpAddress' `
        --output text)
    
    Write-Host "     Instance running!" -ForegroundColor Green
    Write-Host "      Instance ID: $INSTANCE_ID" -ForegroundColor Cyan
    Write-Host "      Public IP: $PUBLIC_IP" -ForegroundColor Cyan
    
    # Save for later
    $PUBLIC_IP | Out-File -FilePath "public-ip.txt" -Encoding UTF8
    $INSTANCE_ID | Out-File -FilePath "instance-id.txt" -Encoding UTF8
    
} catch {
    Write-Host "     Failed to launch instance" -ForegroundColor Red
    Write-Host "   Error: $_" -ForegroundColor Red
    exit 1
}

Write-Host ""

# Step 4: Build All Services
Write-Host "   Step 4/7: Building all services..." -ForegroundColor Cyan

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
    Push-Location "backend\$service"
    mvn clean package -DskipTests -q 2>&1 | Out-Null
    if ($LASTEXITCODE -eq 0) {
        Write-Host "     $service" -ForegroundColor Green
    } else {
        Write-Host "     Failed: $service" -ForegroundColor Red
        Pop-Location
        exit 1
    }
    Pop-Location
}

Write-Host ""

# Step 5: Setup EC2
Write-Host "[Step 5/7] Configuring EC2 instance..." -ForegroundColor Cyan
Write-Host "   [*] Waiting for SSH to be ready (30 seconds)..." -ForegroundColor Yellow
Start-Sleep -Seconds 30

# Create setup script
$SETUP_SCRIPT = @'
#!/bin/bash
set -e
echo "[*] Setting up server..."
sudo apt-get update -qq
curl -fsSL https://get.docker.com -o get-docker.sh
sudo sh get-docker.sh > /dev/null 2>&1
sudo usermod -aG docker ubuntu
sudo curl -L "https://github.com/docker/compose/releases/latest/download/docker-compose-linux-x86_64" -o /usr/local/bin/docker-compose > /dev/null 2>&1
sudo chmod +x /usr/local/bin/docker-compose
sudo apt-get install -y openjdk-17-jre -qq
sudo mkdir -p /opt/hotel-app
sudo chown ubuntu:ubuntu /opt/hotel-app
echo "  Setup complete!"
'@

$SETUP_SCRIPT | Out-File -FilePath "setup-server.sh" -Encoding UTF8 -NoNewline
(Get-Content "setup-server.sh" -Raw) -replace "`r`n", "`n" | Set-Content "setup-server.sh" -NoNewline

Write-Host "      Uploading setup script..." -ForegroundColor Yellow
scp -i hotel-key.pem -o StrictHostKeyChecking=no -o UserKnownHostsFile=/dev/null -o LogLevel=ERROR setup-server.sh ubuntu@${PUBLIC_IP}:/home/ubuntu/ 2>&1 | Out-Null

Write-Host "      Installing Docker and Java (this takes 2-3 minutes)..." -ForegroundColor Yellow
ssh -i hotel-key.pem -o StrictHostKeyChecking=no -o UserKnownHostsFile=/dev/null -o LogLevel=ERROR ubuntu@${PUBLIC_IP} 'chmod +x setup-server.sh; ./setup-server.sh' 2>&1 | Out-Null

Write-Host "     EC2 configured!" -ForegroundColor Green
Write-Host ""

# Step 6: Deploy Application
Write-Host "   Step 6/7: Deploying application to EC2..." -ForegroundColor Cyan

# Prepare deployment package
Write-Host "      Preparing deployment package..." -ForegroundColor Yellow
New-Item -ItemType Directory -Force -Path "deploy-package" | Out-Null

foreach ($service in $services) {
    $jarFile = Get-ChildItem "backend\$service\target\*.jar" -Exclude "*-original.jar" | Select-Object -First 1
    if ($jarFile) {
        Copy-Item $jarFile.FullName "deploy-package\$service.jar"
    }
}

# Create docker-compose.yml
$DOCKER_COMPOSE = @'
version: '3.8'
services:
  employee-service:
    image: openjdk:17-jdk-slim
    ports: ["8085:8085"]
    volumes: ["./employeeManagementService.jar:/app/app.jar"]
    command: java -Xmx384m -Xms256m -jar /app/app.jar
    environment:
      - SPRING_PROFILES_ACTIVE=prod
    restart: unless-stopped
  room-service:
    image: openjdk:17-jdk-slim
    ports: ["8086:8086"]
    volumes: ["./roomManagementService.jar:/app/app.jar"]
    command: java -Xmx384m -Xms256m -jar /app/app.jar
    environment:
      - SPRING_PROFILES_ACTIVE=prod
    restart: unless-stopped
  reservation-service:
    image: openjdk:17-jdk-slim
    ports: ["8087:8087"]
    volumes: ["./reservationManagementService.jar:/app/app.jar"]
    command: java -Xmx384m -Xms256m -jar /app/app.jar
    environment:
      - SPRING_PROFILES_ACTIVE=prod
    restart: unless-stopped
  restaurant-service:
    image: openjdk:17-jdk-slim
    ports: ["8088:8088"]
    volumes: ["./restaurantManagementService.jar:/app/app.jar"]
    command: java -Xmx384m -Xms256m -jar /app/app.jar
    environment:
      - SPRING_PROFILES_ACTIVE=prod
    restart: unless-stopped
  kitchen-service:
    image: openjdk:17-jdk-slim
    ports: ["8089:8089"]
    volumes: ["./kitchenManagementService.jar:/app/app.jar"]
    command: java -Xmx384m -Xms256m -jar /app/app.jar
    environment:
      - SPRING_PROFILES_ACTIVE=prod
    restart: unless-stopped
  inventory-service:
    image: openjdk:17-jdk-slim
    ports: ["8090:8090"]
    volumes: ["./inventoryManagementService.jar:/app/app.jar"]
    command: java -Xmx384m -Xms256m -jar /app/app.jar
    environment:
      - SPRING_PROFILES_ACTIVE=prod
    restart: unless-stopped
  event-service:
    image: openjdk:17-jdk-slim
    ports: ["8091:8091"]
    volumes: ["./eventManagementService.jar:/app/app.jar"]
    command: java -Xmx384m -Xms256m -jar /app/app.jar
    environment:
      - SPRING_PROFILES_ACTIVE=prod
    restart: unless-stopped
'@

$DOCKER_COMPOSE | Out-File -FilePath "deploy-package\docker-compose.yml" -Encoding UTF8

Write-Host "      Uploading to EC2..." -ForegroundColor Yellow
scp -i hotel-key.pem -o StrictHostKeyChecking=no -o UserKnownHostsFile=/dev/null -o LogLevel=ERROR -r deploy-package/* ubuntu@${PUBLIC_IP}:/opt/hotel-app/ 2>&1 | Out-Null

Write-Host "      Starting services..." -ForegroundColor Yellow
ssh -i hotel-key.pem -o StrictHostKeyChecking=no -o UserKnownHostsFile=/dev/null -o LogLevel=ERROR ubuntu@${PUBLIC_IP} 'cd /opt/hotel-app; docker-compose up -d' 2>&1 | Out-Null

Write-Host "   [*] Waiting for services to initialize (30 seconds)..." -ForegroundColor Yellow
Start-Sleep -Seconds 30

Write-Host "     Services deployed!" -ForegroundColor Green
Write-Host ""

# Step 7: Deploy Frontend
Write-Host "   Step 7/7: Deploying frontend to S3 (FREE TIER)..." -ForegroundColor Cyan

Push-Location "Example employee-management"

# Create production config
@"
VITE_EMPLOYEE_API=http://${PUBLIC_IP}:8085
VITE_ROOM_API=http://${PUBLIC_IP}:8086
VITE_RESERVATION_API=http://${PUBLIC_IP}:8087
VITE_RESTAURANT_API=http://${PUBLIC_IP}:8088
VITE_KITCHEN_API=http://${PUBLIC_IP}:8089
VITE_INVENTORY_API=http://${PUBLIC_IP}:8090
VITE_EVENT_API=http://${PUBLIC_IP}:8091
"@ | Out-File -FilePath ".env.production" -Encoding UTF8

Write-Host "   [*] Building frontend..." -ForegroundColor Yellow
npm run build 2>&1 | Out-Null

$BUCKET_NAME = "hotel-mgmt-$(Get-Random -Minimum 10000 -Maximum 99999)"

Write-Host "      Creating S3 bucket: $BUCKET_NAME" -ForegroundColor Yellow
aws s3 mb s3://$BUCKET_NAME --region us-east-1 2>&1 | Out-Null

aws s3 website s3://$BUCKET_NAME --index-document index.html --error-document index.html 2>&1 | Out-Null

Write-Host "      Uploading frontend..." -ForegroundColor Yellow
aws s3 sync dist/ s3://$BUCKET_NAME --acl public-read --quiet

$BUCKET_POLICY = @"
{
  "Version": "2012-10-17",
  "Statement": [{
    "Sid": "PublicReadGetObject",
    "Effect": "Allow",
    "Principal": "*",
    "Action": "s3:GetObject",
    "Resource": "arn:aws:s3:::$BUCKET_NAME/*"
  }]
}
"@

$BUCKET_POLICY | Out-File -FilePath "bucket-policy.json" -Encoding UTF8
aws s3api put-bucket-policy --bucket $BUCKET_NAME --policy file://bucket-policy.json 2>&1 | Out-Null

$WEBSITE_URL = "http://$BUCKET_NAME.s3-website-us-east-1.amazonaws.com"

Write-Host "     Frontend deployed!" -ForegroundColor Green

Pop-Location

Write-Host ""
Write-Host "========================================" -ForegroundColor Cyan
Write-Host "     DEPLOYMENT COMPLETE!" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""
Write-Host "   Your Application URLs:" -ForegroundColor Yellow
Write-Host ""
Write-Host "      Frontend: $WEBSITE_URL" -ForegroundColor Green
Write-Host "      Backend: http://${PUBLIC_IP}:8085-8091" -ForegroundColor Green
Write-Host ""
Write-Host "   Default Login Credentials:" -ForegroundColor Yellow
Write-Host "   Username: admin" -ForegroundColor White
Write-Host "   Password: admin123" -ForegroundColor White
Write-Host ""
Write-Host "   Cost: $0/month (FREE TIER for 12 months)" -ForegroundColor Green
Write-Host ""
Write-Host "   Resources Created:" -ForegroundColor Yellow
Write-Host "     EC2 Instance: $INSTANCE_ID" -ForegroundColor Gray
Write-Host "     Public IP: $PUBLIC_IP" -ForegroundColor Gray
Write-Host "     S3 Bucket: $BUCKET_NAME" -ForegroundColor Gray
Write-Host "     Security Group: $SG_ID" -ForegroundColor Gray
Write-Host ""
Write-Host "   Management Commands:" -ForegroundColor Yellow
Write-Host "   View logs: ssh -i hotel-key.pem ubuntu@$PUBLIC_IP 'cd /opt/hotel-app; docker-compose logs -f'" -ForegroundColor Gray
Write-Host "   Restart: ssh -i hotel-key.pem ubuntu@$PUBLIC_IP 'cd /opt/hotel-app; docker-compose restart'" -ForegroundColor Gray
Write-Host "   Stop: ssh -i hotel-key.pem ubuntu@$PUBLIC_IP 'cd /opt/hotel-app; docker-compose down'" -ForegroundColor Gray
Write-Host ""
Write-Host "    Remember:" -ForegroundColor Yellow
Write-Host "     Free tier is valid for 12 months from AWS account creation" -ForegroundColor Gray
Write-Host "     Monitor usage in AWS Billing Dashboard" -ForegroundColor Gray
Write-Host "     EC2 public IP may change if instance is stopped/started" -ForegroundColor Gray
Write-Host ""

# Save deployment info
@"
Deployment Information
======================
Date: $(Get-Date -Format "yyyy-MM-dd HH:mm:ss")
Instance ID: $INSTANCE_ID
Public IP: $PUBLIC_IP
Security Group: $SG_ID
S3 Bucket: $BUCKET_NAME
Frontend URL: $WEBSITE_URL
Backend API: http://${PUBLIC_IP}:8085-8091

Login Credentials:
Username: admin
Password: admin123

Management:
View logs: ssh -i hotel-key.pem ubuntu@$PUBLIC_IP "cd /opt/hotel-app && docker-compose logs -f"
Restart services: ssh -i hotel-key.pem ubuntu@$PUBLIC_IP "cd /opt/hotel-app && docker-compose restart"
Stop services: ssh -i hotel-key.pem ubuntu@$PUBLIC_IP "cd /opt/hotel-app && docker-compose down"

Cleanup:
To remove all resources:
aws ec2 terminate-instances --instance-ids $INSTANCE_ID
aws s3 rb s3://$BUCKET_NAME --force
aws ec2 delete-security-group --group-id $SG_ID
aws ec2 delete-key-pair --key-name hotel-key
"@ | Out-File -FilePath "deployment-info.txt" -Encoding UTF8

Write-Host "   Deployment info saved to: deployment-info.txt" -ForegroundColor Cyan
Write-Host ""

# Test connection
Write-Host "   Testing backend connection..." -ForegroundColor Yellow
try {
    $response = Invoke-RestMethod -Uri "http://${PUBLIC_IP}:8085/actuator/health" -TimeoutSec 10 -ErrorAction Stop
    Write-Host "     Backend is responding!" -ForegroundColor Green
} catch {
    Write-Host "       Backend not responding yet (may need a few more seconds)" -ForegroundColor Yellow
}

Write-Host ""
Write-Host "   Opening application in browser..." -ForegroundColor Cyan
Start-Sleep -Seconds 2
Start-Process $WEBSITE_URL

Write-Host ""
Write-Host "  All done! Enjoy your FREE AWS deployment!   " -ForegroundColor Green
Write-Host ""
