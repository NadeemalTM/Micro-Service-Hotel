# 🚀 AWS Deployment - Quick Start Guide

**Project:** Hotel Management System  
**Date:** March 6, 2026  
**Status:** ✅ Ready for Deployment

---

## 📊 Your Current Setup

✅ **AWS CLI**: Installed and configured (Account: 870258691607)  
✅ **Maven**: Installed and working  
✅ **Java 17**: Ready  
✅ **All Services**: Built and tested locally  
✅ **Frontend**: Built and optimized  
⚠️ **Docker**: Not installed (required for Option 2 only)

---

## 🎯 Choose Your Deployment Method

### 🥇 **Option 1: Elastic Beanstalk** (RECOMMENDED FOR YOU)

**Why this is best for you:**
- ✅ No Docker installation needed
- ✅ Fastest deployment (30-45 minutes)
- ✅ Easiest to manage
- ✅ Built-in database provisioning
- ✅ Automatic load balancing
- ✅ Perfect for getting started

**Cost:** ~$170/month  
**Difficulty:** ⭐ Easy  
**Time to deploy:** 30-45 minutes

**👉 Start now:**
```powershell
cd "d:\DEA Project\Hotel Micro"
.\deploy-aws.ps1
# Select Option 1
```

**📖 Detailed guide:** [ELASTIC_BEANSTALK_DEPLOYMENT.md](ELASTIC_BEANSTALK_DEPLOYMENT.md)

---

### 🥈 **Option 2: ECS Fargate** (Best for Production)

**Why choose this:**
- 🐳 Containerized deployment
- 📈 Better scalability
- 🔧 More control over infrastructure
- 💪 Production-ready
- 🎯 Industry standard

**Cost:** ~$195-230/month  
**Difficulty:** ⭐⭐⭐ Moderate  
**Time to deploy:** 60-90 minutes

**Prerequisites:**
1. Install Docker Desktop first
2. Build all services
3. Push images to ECR

**👉 Start:**
1. Install Docker: See [DOCKER_INSTALLATION_GUIDE.md](DOCKER_INSTALLATION_GUIDE.md)
2. Then run: `.\deploy-aws.ps1` and select Option 2
3. Follow: [AWS_DEPLOYMENT_GUIDE.md](AWS_DEPLOYMENT_GUIDE.md)

---

### 🥉 **Option 3: Local Docker Testing** (Before Cloud Deployment)

Test everything locally with Docker Compose first:

**Prerequisites:**
- Install Docker Desktop

**👉 Start:**
```powershell
cd "d:\DEA Project\Hotel Micro"

# Start all services
docker-compose up -d

# Check status
docker-compose ps

# View logs
docker-compose logs -f

# Access application
# Frontend: http://localhost:3000
# Backend APIs: http://localhost:8085-8091

# Stop all services
docker-compose down
```

---

## 📋 Step-by-Step: Elastic Beanstalk Deployment (RECOMMENDED)

### Phase 1: Install EB CLI (5 minutes)

```powershell
# Install EB CLI
pip install awsebcli --upgrade --user

# Verify installation
eb --version
```

### Phase 2: Build All Services (10 minutes)

```powershell
cd "d:\DEA Project\Hotel Micro"

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
}
```

### Phase 3: Deploy Services (25-30 minutes)

**Deploy Employee Service (with authentication):**
```powershell
cd "backend\employeeManagementService"

# Initialize
eb init hotel-employee --platform java-17 --region us-east-1

# Create environment with database
eb create hotel-employee-env `
    --instance-type t3.micro `
    --database `
    --database.engine postgres `
    --database.username admin `
    --envvars SPRING_PROFILES_ACTIVE=prod,JWT_SECRET=YourSecureJWTSecretKey2026!

# Wait for deployment (takes ~8-10 minutes)
# Once done, get URL
eb status
cd "..\..\"
```

**Deploy Room Service:**
```powershell
cd "backend\roomManagementService"
eb init hotel-room --platform java-17 --region us-east-1
eb create hotel-room-env --instance-type t3.micro --database --database.engine postgres --database.username admin --envvars SPRING_PROFILES_ACTIVE=prod
cd "..\..\"
```

**Deploy Remaining Services (same pattern):**
- Reservation Service
- Restaurant Service
- Kitchen Service
- Inventory Service
- Event Service

### Phase 4: Deploy Frontend (5 minutes)

```powershell
cd "Example employee-management"

# Get backend URLs
cd "..\backend\employeeManagementService"
$employeeUrl = (eb status | Select-String "CNAME:" | ForEach-Object { $_.Line.Split(":")[1].Trim() })
cd "..\..\Example employee-management"

# Create production config
@"
VITE_EMPLOYEE_API=http://$employeeUrl
VITE_ROOM_API=http://<room-url>
VITE_RESERVATION_API=http://<reservation-url>
VITE_RESTAURANT_API=http://<restaurant-url>
VITE_KITCHEN_API=http://<kitchen-url>
VITE_INVENTORY_API=http://<inventory-url>
VITE_EVENT_API=http://<event-url>
"@ | Out-File ".env.production" -Encoding UTF8

# Build frontend
npm run build

# Create S3 bucket
$bucket = "hotel-frontend-$(Get-Random -Minimum 1000 -Maximum 9999)"
aws s3 mb s3://$bucket

# Enable website hosting
aws s3 website s3://$bucket --index-document index.html

# Upload files
aws s3 sync dist/ s3://$bucket --acl public-read

# Get URL
Write-Host "Frontend: http://$bucket.s3-website-us-east-1.amazonaws.com"
```

---

## 🎬 Quick Start - Run Now!

**If you're ready to deploy right now:**

```powershell
# Navigate to project
cd "d:\DEA Project\Hotel Micro"

# Run deployment script
.\deploy-aws.ps1
```

The script will:
1. ✅ Check all prerequisites
2. ✅ Guide you through deployment options
3. ✅ Automate most steps
4. ✅ Provide clear feedback
5. ✅ Give you access URLs

---

## 📊 Cost Comparison

| Method | Monthly Cost | Setup Time | Complexity |
|--------|-------------|------------|------------|
| **Elastic Beanstalk** | ~$170 | 30-45 min | ⭐ Easy |
| **ECS Fargate** | ~$195-230 | 60-90 min | ⭐⭐⭐ Moderate |
| **EC2 + Docker** | ~$100-150 | 90+ min | ⭐⭐⭐⭐ Advanced |

---

## 🎯 What Happens After Deployment?

### 1. Backend Services Running
- ✅ 7 microservices on AWS
- ✅ PostgreSQL databases
- ✅ Auto-scaling enabled
- ✅ Load balancing configured
- ✅ CloudWatch monitoring active

### 2. Frontend Deployed
- ✅ Static site on S3
- ✅ Global CDN (optional)
- ✅ HTTPS available (with CloudFront)

### 3. Monitoring & Management
- 📊 CloudWatch logs and metrics
- 🔔 Health checks and alarms
- 📈 Auto-scaling based on load
- 💾 Automated backups

---

## 🔧 Post-Deployment Management

### View Logs
```powershell
cd "backend\<service-name>"
eb logs
```

### Update a Service
```powershell
# Make code changes
mvn clean package -DskipTests

# Deploy update
eb deploy
```

### Scale Services
```powershell
# Scale to 2 instances
eb scale 2
```

### Monitor Health
```powershell
eb health
```

### Check Costs
```powershell
# View cost explorer in AWS Console
aws ce get-cost-and-usage --time-period Start=2026-03-01,End=2026-03-31 --granularity MONTHLY --metrics BlendedCost
```

---

## 📚 All Documentation

Created deployment guides for you:

1. 📄 **[AWS_DEPLOYMENT_GUIDE.md](AWS_DEPLOYMENT_GUIDE.md)**  
   Complete guide for ECS Fargate deployment

2. 📄 **[ELASTIC_BEANSTALK_DEPLOYMENT.md](ELASTIC_BEANSTALK_DEPLOYMENT.md)**  
   Simple deployment without Docker (RECOMMENDED)

3. 📄 **[DOCKER_INSTALLATION_GUIDE.md](DOCKER_INSTALLATION_GUIDE.md)**  
   How to install Docker Desktop for Windows

4. 📄 **[docker-compose.yml](docker-compose.yml)**  
   Local testing with Docker Compose

5. 📄 **[deploy-aws.ps1](deploy-aws.ps1)**  
   Automated deployment script

6. 📁 **Dockerfiles**  
   Created for all 7 services in backend folders

---

## ✅ Deployment Checklist

Before you start:

- [x] AWS CLI installed and configured
- [x] Maven installed
- [x] Java 17 installed
- [x] All services built locally
- [x] Frontend tested locally
- [ ] Choose deployment method
- [ ] Install EB CLI (for Option 1) OR Docker (for Option 2)
- [ ] Run deployment script
- [ ] Configure frontend with backend URLs
- [ ] Test deployed application
- [ ] Set up monitoring and alerts

---

## 🚀 Ready to Deploy?

### Option 1: Automated (Easiest)
```powershell
cd "d:\DEA Project\Hotel Micro"
.\deploy-aws.ps1
```

### Option 2: Manual Step-by-Step
Follow: [ELASTIC_BEANSTALK_DEPLOYMENT.md](ELASTIC_BEANSTALK_DEPLOYMENT.md)

### Option 3: Test Locally First
```powershell
# First install Docker Desktop
# Then:
docker-compose up -d
```

---

## 💡 Recommendations

**For your situation (Docker not installed):**

1. 🥇 **Start with Elastic Beanstalk** (Option 1)
   - No Docker needed
   - Fastest path to production
   - Easy to learn and manage
   - Good for MVP and testing

2. 🥈 **Later migrate to ECS Fargate** (if needed)
   - Better for scaling
   - More production-grade
   - Containerized benefits

---

## 📞 Need Help?

**Common Issues:**
- EB CLI not found → Make sure Python Scripts folder is in PATH
- Build failures → Run `mvn clean install` in project root first
- Database connection issues → Check security groups in AWS Console
- CORS errors → Update Spring Boot CORS configuration

**Support Resources:**
- AWS Documentation: https://docs.aws.amazon.com/
- Elastic Beanstalk Guide: https://docs.aws.amazon.com/elasticbeanstalk/
- Your deployment logs: Run `eb logs` in service folder

---

## 🎉 Summary

**You have everything ready:**
- ✅ Project code complete
- ✅ AWS account configured
- ✅ Deployment scripts created
- ✅ Documentation prepared
- ✅ Multiple deployment options

**Time to deploy: 30-45 minutes**

**Next step:** Run the deployment script!

```powershell
cd "d:\DEA Project\Hotel Micro"
.\deploy-aws.ps1
```

**Good luck! 🚀**

---

**Created:** March 6, 2026  
**Status:** ✅ Ready for Production Deployment
