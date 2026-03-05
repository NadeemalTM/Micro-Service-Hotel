# Elastic Beanstalk Deployment - Simple & Fast (No Docker Required)

## 📋 Prerequisites

1. ✅ AWS CLI installed and configured (Already done!)
2. ✅ Maven installed (Already done!)
3. ✅ Java 17 installed

## 🚀 Quick Deployment Steps

### Step 1: Install Elastic Beanstalk CLI

```powershell
# Install EB CLI using pip
pip install awsebcli --upgrade --user

# Add to PATH if needed
$env:Path += ";$env:USERPROFILE\AppData\Roaming\Python\Python<version>\Scripts"

# Verify installation
eb --version
```

### Step 2: Build All Services

```powershell
cd "d:\DEA Project\Hotel Micro"

# Build each service
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
    Write-Host "Building $service..." -ForegroundColor Yellow
    cd "backend\$service"
    mvn clean package -DskipTests
    cd "..\..\"
    Write-Host "✅ $service built!" -ForegroundColor Green
}
```

### Step 3: Deploy Employee Service (First Service)

```powershell
cd "backend\employeeManagementService"

# Initialize EB application
eb init hotel-employee-service --platform java-17 --region us-east-1

# Create environment with RDS database
eb create hotel-employee-env `
    --instance-type t3.micro `
    --database `
    --database.engine postgres `
    --database.username admin `
    --envvars SPRING_PROFILES_ACTIVE=prod,JWT_SECRET=YourSecureJWTSecretKey2026!

# Wait for environment to be ready (takes ~10 minutes)
eb status

# Once deployed, get the URL
eb open
```

### Step 4: Deploy Room Service

```powershell
cd "..\roomManagementService"

eb init hotel-room-service --platform java-17 --region us-east-1

eb create hotel-room-env `
    --instance-type t3.micro `
    --database `
    --database.engine postgres `
    --database.username admin `
    --envvars SPRING_PROFILES_ACTIVE=prod

eb open
```

### Step 5: Deploy Remaining Services

Repeat for each service:

```powershell
# Reservation Service
cd "..\reservationManagementService"
eb init hotel-reservation-service --platform java-17 --region us-east-1
eb create hotel-reservation-env --instance-type t3.micro --database --database.engine postgres --database.username admin --envvars SPRING_PROFILES_ACTIVE=prod
cd "..\..\"

# Restaurant Service
cd "backend\restaurantManagementService"
eb init hotel-restaurant-service --platform java-17 --region us-east-1
eb create hotel-restaurant-env --instance-type t3.micro --database --database.engine postgres --database.username admin --envvars SPRING_PROFILES_ACTIVE=prod
cd "..\..\"

# Kitchen Service
cd "backend\kitchenManagementService"
eb init hotel-kitchen-service --platform java-17 --region us-east-1
eb create hotel-kitchen-env --instance-type t3.micro --database --database.engine postgres --database.username admin --envvars SPRING_PROFILES_ACTIVE=prod
cd "..\..\"

# Inventory Service
cd "backend\inventoryManagementService"
eb init hotel-inventory-service --platform java-17 --region us-east-1
eb create hotel-inventory-env --instance-type t3.micro --database --database.engine postgres --database.username admin --envvars SPRING_PROFILES_ACTIVE=prod
cd "..\..\"

# Event Service
cd "backend\eventManagementService"
eb init hotel-event-service --platform java-17 --region us-east-1
eb create hotel-event-env --instance-type t3.micro --database --database.engine postgres --database.username admin --envvars SPRING_PROFILES_ACTIVE=prod
cd "..\..\"
```

### Step 6: Get All Service URLs

```powershell
cd "d:\DEA Project\Hotel Micro"

Write-Host "`n📋 Service URLs:`n" -ForegroundColor Cyan

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
    cd "backend\$service"
    $url = eb status | Select-String "CNAME:" | ForEach-Object { $_.ToString().Split(":")[1].Trim() }
    Write-Host "$service : http://$url" -ForegroundColor Green
    cd "..\..\"
}
```

### Step 7: Update Frontend Configuration

Create `.env.production` file:

```powershell
cd "Example employee-management"

# Get the employee service URL
cd "..\backend\employeeManagementService"
$employeeUrl = eb status | Select-String "CNAME:" | ForEach-Object { $_.ToString().Split(":")[1].Trim() }
cd "..\..\Example employee-management"

# Repeat for other services...
# Create the .env.production file with all URLs
@"
VITE_EMPLOYEE_API=http://$employeeUrl
VITE_ROOM_API=http://$roomUrl
VITE_RESERVATION_API=http://$reservationUrl
VITE_RESTAURANT_API=http://$restaurantUrl
VITE_KITCHEN_API=http://$kitchenUrl
VITE_INVENTORY_API=http://$inventoryUrl
VITE_EVENT_API=http://$eventUrl
"@ | Out-File -FilePath ".env.production" -Encoding UTF8
```

### Step 8: Deploy Frontend to S3

```powershell
# Build frontend
npm run build

# Create S3 bucket
$bucketName = "hotel-management-frontend-$(Get-Random -Minimum 1000 -Maximum 9999)"
aws s3 mb s3://$bucketName --region us-east-1

# Enable static website hosting
aws s3 website s3://$bucketName --index-document index.html --error-document index.html

# Upload files
aws s3 sync dist/ s3://$bucketName --acl public-read

# Get website URL
$websiteUrl = "http://$bucketName.s3-website-us-east-1.amazonaws.com"
Write-Host "`n✅ Frontend deployed at: $websiteUrl`n" -ForegroundColor Green

# Open in browser
Start-Process $websiteUrl
```

## 📊 Monitoring and Management

### View Logs

```powershell
cd "backend\<serviceName>"

# View recent logs
eb logs

# Stream logs in real-time
eb logs --stream
```

### Check Health

```powershell
# Check environment health
eb health

# Get detailed status
eb status
```

### Update a Service

```powershell
# After making code changes
mvn clean package -DskipTests

# Deploy update
eb deploy
```

### Scale Services

```powershell
# Scale to 2 instances
eb scale 2

# Enable auto-scaling
eb config
# Then set MinSize and MaxSize in the configuration file
```

## 💰 Cost Estimation (Elastic Beanstalk)

| Component | Cost |
|-----------|------|
| 7 × t3.micro EC2 instances | ~$50/month |
| 7 × RDS PostgreSQL (db.t3.micro) | ~$90/month |
| Application Load Balancer | ~$25/month |
| S3 + Data Transfer | ~$5/month |
| **Total** | **~$170/month** |

## 🔧 Troubleshooting

### Service Won't Start

```powershell
# Check logs
eb logs

# SSH into instance
eb ssh

# Check health
eb health --refresh
```

### Database Connection Issues

```powershell
# Get environment properties (including DB connection string)
eb printenv

# Set environment variables
eb setenv SPRING_DATASOURCE_URL=jdbc:postgresql://...
```

### CORS Issues

Update Spring Boot application to allow EB domain:

```java
@CrossOrigin(origins = {"*.elasticbeanstalk.com", "*.amazonaws.com"})
```

Then redeploy:

```powershell
eb deploy
```

## 🗑️ Cleanup (When Done Testing)

```powershell
# Terminate all environments
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
    cd "backend\$service"
    eb terminate --all
    cd "..\..\"
}

# Delete S3 bucket
aws s3 rb s3://$bucketName --force
```

## ✅ Advantages of Elastic Beanstalk

1. **No Docker Required** - Deploy JAR files directly
2. **Automatic Load Balancing** - Built-in ALB
3. **Auto-scaling** - Handle traffic spikes
4. **Managed Updates** - Easy platform updates
5. **Integrated Monitoring** - CloudWatch integration
6. **Easy Rollback** - Version management
7. **Quick Setup** - Deploy in minutes

## 🎯 Summary

This is the **fastest way** to deploy your hotel management system to AWS:

1. ✅ No Docker installation needed
2. ✅ Simple EB CLI commands
3. ✅ Integrated database provisioning
4. ✅ Automatic load balancing
5. ✅ Easy monitoring and scaling
6. ✅ Cost-effective for small to medium workloads

**Total deployment time: 30-45 minutes** (mostly waiting for AWS resources)

---

**Ready to deploy?** Run the deployment script:

```powershell
cd "d:\DEA Project\Hotel Micro"
.\deploy-aws.ps1
```

Then select **Option 1: Elastic Beanstalk** for the easiest deployment!
