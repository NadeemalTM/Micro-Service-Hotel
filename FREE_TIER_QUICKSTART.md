# 🆓 Quick Start: AWS Free Tier Deployment

**Cost:** $0/month for 12 months  
**Time:** 20-30 minutes  
**Difficulty:** ⭐ Easy (fully automated)

---

## ✅ What You Need

- ✅ AWS account (with free tier available)
- ✅ AWS CLI configured (you have this!)
- ✅ Maven installed (you have this!)
- ✅ Internet connection

**No Docker required!** ✨

---

## 🚀 Deploy in 3 Steps

### Step 1: Open PowerShell in Project Directory

```powershell
cd "d:\DEA Project\Hotel Micro"
```

### Step 2: Run the Deployment Script

```powershell
.\deploy-free-tier.ps1
```

### Step 3: Wait 20-30 Minutes

The script will automatically:
1. ✅ Create EC2 t2.micro instance (free tier)
2. ✅ Build all 7 microservices
3. ✅ Deploy to EC2 with Docker Compose
4. ✅ Deploy frontend to S3 (free tier)
5. ✅ Give you access URLs
6. ✅ Open your app in browser

**That's it!** 🎉

---

## 📋 What You'll Get

### Backend (All 7 Services on EC2)
- ✅ Employee Management (JWT auth)
- ✅ Room Management
- ✅ Reservation Management
- ✅ Restaurant Management
- ✅ Kitchen Management
- ✅ Inventory Management
- ✅ Event Management

**Running on:** Single EC2 t2.micro instance  
**Database:** H2 (in-memory, no extra cost)  
**Access:** http://ec2-public-ip:8085-8091

### Frontend (React on S3)
- ✅ Full React application
- ✅ Static website hosting
- ✅ Public access

**Hosted on:** S3 bucket  
**Access:** http://bucket-name.s3-website-us-east-1.amazonaws.com

---

## 💰 Cost Breakdown

| Resource | Free Tier | Your Usage | Cost |
|----------|-----------|------------|------|
| EC2 t2.micro | 750 hrs/mo | 720 hrs/mo | $0 ✅ |
| EBS Storage | 30 GB | 30 GB | $0 ✅ |
| S3 Storage | 5 GB | ~100 MB | $0 ✅ |
| Data Transfer | 100 GB/mo | ~5-10 GB | $0 ✅ |
| **TOTAL** | | | **$0/mo** 🎉 |

**Valid for 12 months from AWS account creation!**

---

## 🔐 Default Login

After deployment, log in with:

- **Username:** admin
- **Password:** admin123

---

## 📱 Access Your Application

After deployment completes, you'll see:

```
✅ DEPLOYMENT COMPLETE!

📋 Your Application URLs:
   🌐 Frontend: http://hotel-mgmt-xxxxx.s3-website-us-east-1.amazonaws.com
   🔧 Backend: http://ec2-xx-xx-xx-xx.compute.amazonaws.com:8085-8091

🔐 Default Login:
   Username: admin
   Password: admin123
```

The script will automatically open your frontend in a browser!

---

## 🛠️ Management Commands

### View Logs
```powershell
ssh -i hotel-key.pem ubuntu@YOUR-PUBLIC-IP "cd /opt/hotel-app && docker-compose logs -f"
```

### Restart Services
```powershell
ssh -i hotel-key.pem ubuntu@YOUR-PUBLIC-IP "cd /opt/hotel-app && docker-compose restart"
```

### Stop Services (to save hours)
```powershell
ssh -i hotel-key.pem ubuntu@YOUR-PUBLIC-IP "cd /opt/hotel-app && docker-compose down"
```

### Check Status
```powershell
ssh -i hotel-key.pem ubuntu@YOUR-PUBLIC-IP "cd /opt/hotel-app && docker-compose ps"
```

---

## ⚠️ Important Notes

### Free Tier Limits
- **750 hours/month** = One t2.micro running 24/7 is within limits
- **Valid for 12 months** from AWS account creation
- **Monitor usage** in AWS Billing Dashboard

### Public IP Changes
If you stop and start the instance, the public IP will change:
1. Get new IP: `aws ec2 describe-instances --instance-ids YOUR-INSTANCE-ID --query 'Reservations[0].Instances[0].PublicIpAddress'`
2. Update frontend and redeploy

### Saving Hours
To preserve free tier hours when not using:
```powershell
# Stop instance (saves hours, costs resume when restarted)
aws ec2 stop-instances --instance-ids YOUR-INSTANCE-ID

# Start instance when needed
aws ec2 start-instances --instance-ids YOUR-INSTANCE-ID
```

---

## 🗑️ Cleanup (When Done)

To remove everything and stop all charges:

```powershell
# Get your instance ID
$INSTANCE_ID = Get-Content "instance-id.txt"

# Terminate instance
aws ec2 terminate-instances --instance-ids $INSTANCE_ID

# Delete S3 bucket
aws s3 rb s3://YOUR-BUCKET-NAME --force

# Delete security group (wait 60 seconds after termination)
Start-Sleep -Seconds 60
aws ec2 delete-security-group --group-name hotel-sg

# Delete key pair
aws ec2 delete-key-pair --key-name hotel-key
Remove-Item hotel-key.pem
```

---

## 🐛 Troubleshooting

### Script Fails
- Make sure AWS CLI is configured: `aws configure`
- Check you have Maven: `mvn --version`
- Ensure you're in project directory

### Services Not Starting
```powershell
# Check logs
ssh -i hotel-key.pem ubuntu@YOUR-IP "cd /opt/hotel-app && docker-compose logs"

# Restart
ssh -i hotel-key.pem ubuntu@YOUR-IP "cd /opt/hotel-app && docker-compose restart"
```

### Can't Connect
- Check security group allows ports 8085-8091
- Verify instance is running: `aws ec2 describe-instances --instance-ids YOUR-INSTANCE-ID`
- Wait 1-2 minutes after deployment for services to fully start

### Out of Free Tier Hours
- Check usage in AWS Billing Dashboard
- Stop instance when not using
- Free tier resets monthly (750 hours each month)

---

## 📊 Monitor Your Usage

### Set Up Billing Alerts

```powershell
# Create SNS topic
$TOPIC_ARN = (aws sns create-topic --name billing-alerts --query 'TopicArn' --output text)

# Subscribe your email
aws sns subscribe --topic-arn $TOPIC_ARN --protocol email --notification-endpoint your-email@example.com

# Create $1 threshold alarm
aws cloudwatch put-metric-alarm `
    --alarm-name free-tier-alert `
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

### Check Current Usage

1. Go to: https://console.aws.amazon.com/billing/
2. Click "Bills" in left menu
3. Look for "Free Tier Usage"
4. Monitor EC2 hours used

---

## 🎯 After Free Tier Expires

After 12 months, you'll be charged ~$10-15/month for:
- EC2 t2.micro: ~$8/month
- EBS 30GB: ~$3/month
- S3 + transfer: ~$2/month

**Options:**
1. **Keep running** - Pay ~$10-15/month
2. **Migrate to Lightsail** - Fixed $5/month
3. **Use spot instances** - Save 70-90%
4. **Stop when not using** - Only pay for uptime
5. **Downsize** - Use smaller services

---

## 📚 Full Documentation

For more details, see:
- **[AWS_FREE_TIER_DEPLOYMENT.md](AWS_FREE_TIER_DEPLOYMENT.md)** - Complete guide
- **[DEPLOYMENT_QUICKSTART.md](DEPLOYMENT_QUICKSTART.md)** - All deployment options

---

## ✅ Ready to Deploy?

```powershell
cd "d:\DEA Project\Hotel Micro"
.\deploy-free-tier.ps1
```

**Sit back and relax!** The script handles everything automatically. ☕

---

**Questions?** Check the full guide: **[AWS_FREE_TIER_DEPLOYMENT.md](AWS_FREE_TIER_DEPLOYMENT.md)**

**Need help?** Contact AWS Support or check CloudWatch logs

---

**🎉 Enjoy your FREE hotel management system deployment!**

**Created:** March 6, 2026  
**Cost:** $0/month (12 months)  
**Status:** ✅ Ready to deploy!
