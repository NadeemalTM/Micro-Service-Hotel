# Docker Desktop Installation Guide for Windows

## 📋 Why Install Docker?

Docker is required if you want to:
- Deploy using **AWS ECS Fargate** (containerized deployment)
- Run the application locally using **docker-compose**
- Build container images for production

## 🚀 Installation Steps

### Step 1: Download Docker Desktop

1. Visit: https://www.docker.com/products/docker-desktop/
2. Click **"Download for Windows"**
3. Save the installer file (Docker Desktop Installer.exe)

### Step 2: System Requirements

**Minimum Requirements:**
- Windows 10 64-bit: Pro, Enterprise, or Education (Build 19041 or higher)
- OR Windows 11 64-bit
- 4 GB RAM (8 GB recommended)
- BIOS-level hardware virtualization support enabled
- WSL 2 feature enabled

### Step 3: Enable WSL 2

Open PowerShell as Administrator and run:

```powershell
# Enable WSL
dism.exe /online /enable-feature /featurename:Microsoft-Windows-Subsystem-Linux /all /norestart

# Enable Virtual Machine Platform
dism.exe /online /enable-feature /featurename:VirtualMachinePlatform /all /norestart

# Restart your computer
Restart-Computer

# After restart, set WSL 2 as default
wsl --set-default-version 2

# Install Ubuntu distro (optional but recommended)
wsl --install -d Ubuntu
```

### Step 4: Install Docker Desktop

1. Run **Docker Desktop Installer.exe**
2. Follow the installation wizard:
   - ✅ Check "Use WSL 2 instead of Hyper-V"
   - ✅ Check "Add shortcut to desktop"
3. Click **"Install"**
4. Wait for installation to complete (~5 minutes)
5. Click **"Close and restart"**

### Step 5: Start Docker Desktop

1. Launch **Docker Desktop** from Start Menu
2. Accept the Docker Subscription Service Agreement
3. Wait for Docker Engine to start (you'll see "Docker is running" in system tray)
4. Verify installation:

```powershell
# Check Docker version
docker --version

# Check Docker Compose version
docker-compose --version

# Test Docker with hello-world
docker run hello-world
```

### Step 6: Configure Docker Settings (Optional)

1. Right-click Docker icon in system tray → **Settings**
2. **Resources → WSL Integration**:
   - ✅ Enable integration with default WSL distro
3. **Resources → Advanced**:
   - Set CPUs: 4 (or half of your available CPUs)
   - Set Memory: 4 GB (or adjust based on your system)
4. Click **"Apply & Restart"**

## ✅ Verification

Run these commands to verify Docker is working:

```powershell
# 1. Check Docker version
docker --version
# Expected: Docker version 24.x.x or higher

# 2. Check Docker Compose
docker-compose --version
# Expected: Docker Compose version 2.x.x or higher

# 3. Test Docker is working
docker run hello-world
# Expected: "Hello from Docker!" message

# 4. Check Docker is running
docker ps
# Expected: Empty list (no containers running yet)

# 5. Check available images
docker images
# Expected: List with hello-world image
```

## 🐳 Test with Your Hotel Management System

Once Docker is installed, you can test locally:

```powershell
cd "d:\DEA Project\Hotel Micro"

# Build all services first
cd backend\employeeManagementService
mvn clean package -DskipTests
cd ..\..

# Build Docker image for employee service
cd backend\employeeManagementService
docker build -t hotel-employee-service .

# Verify image was created
docker images | Select-String "hotel-employee"

# Run the container
docker run -d -p 8085:8085 --name employee-service hotel-employee-service

# Check if container is running
docker ps

# View logs
docker logs employee-service

# Stop and remove container
docker stop employee-service
docker rm employee-service
```

## 🚀 Deploy to AWS with Docker

Once Docker is installed and working, you can proceed with **ECS Fargate deployment**:

```powershell
cd "d:\DEA Project\Hotel Micro"

# Run the deployment script
.\deploy-aws.ps1

# Select: Option 2 - AWS ECS Fargate
```

## 🔧 Troubleshooting

### Issue: "Docker is not running"

**Solution:**
1. Open Docker Desktop from Start Menu
2. Wait for "Docker Desktop starting..." to complete
3. Check system tray - you should see Docker icon
4. If still not working, restart Docker Desktop

### Issue: "WSL 2 installation is incomplete"

**Solution:**
1. Download WSL 2 kernel update: https://aka.ms/wsl2kernel
2. Install the update
3. Restart Docker Desktop

### Issue: "Hardware assisted virtualization is not available"

**Solution:**
1. Restart computer and enter BIOS/UEFI settings (usually F2, F10, or Del during startup)
2. Find "Virtualization Technology" or "Intel VT-x" or "AMD-V"
3. Enable it
4. Save and exit BIOS
5. Restart and try Docker again

### Issue: "Docker Desktop requires a newer WSL kernel version"

**Solution:**
```powershell
# Update WSL
wsl --update

# Restart Docker Desktop
```

### Issue: Permission denied errors

**Solution:**
```powershell
# Add your user to docker-users group
net localgroup docker-users "Your-Username" /add

# Restart computer
```

### Issue: Slow performance

**Solution:**
1. Open Docker Desktop Settings
2. Resources → Advanced
3. Increase Memory to 4-8 GB
4. Increase CPUs to 4-6
5. Apply & Restart

### Issue: "Cannot connect to Docker daemon"

**Solution:**
```powershell
# Restart Docker Desktop
Stop-Process -Name "Docker Desktop"
Start-Process "C:\Program Files\Docker\Docker\Docker Desktop.exe"

# Wait for Docker to start
Start-Sleep -Seconds 30

# Test
docker ps
```

## 📚 Alternative: Docker without Docker Desktop

If you can't install Docker Desktop (e.g., Windows Home edition limitations), you can use cloud-based alternatives:

### Option 1: AWS Cloud9

1. Go to AWS Console → Cloud9
2. Create environment
3. Select t3.small instance
4. Docker is pre-installed
5. Clone your repository and build images there

### Option 2: AWS EC2

1. Launch Ubuntu EC2 instance (t3.medium)
2. Install Docker:
```bash
sudo apt-get update
sudo apt-get install docker.io -y
sudo systemctl start docker
sudo usermod -aG docker ubuntu
```
3. Upload your code and build images

### Option 3: GitHub Actions

Use GitHub Actions to build and push images automatically (see CI/CD section in AWS_DEPLOYMENT_GUIDE.md)

## 💡 Tips for Using Docker

1. **Keep Docker Desktop running** - It must be running to use Docker commands
2. **Regular cleanup** - Run `docker system prune` periodically to free up space
3. **Monitor resources** - Watch CPU/Memory usage in Docker Desktop dashboard
4. **Use .dockerignore** - Exclude unnecessary files from Docker builds

## 🎯 Next Steps

After Docker is installed:

1. ✅ **Test Docker** - Run `docker --version`
2. ✅ **Test Build** - Build one service image
3. ✅ **Test Locally** - Run with docker-compose
4. ✅ **Deploy to AWS** - Use ECS Fargate deployment

---

## 📞 Need Help?

- Docker Documentation: https://docs.docker.com/desktop/windows/
- Docker Forums: https://forums.docker.com/
- Stack Overflow: https://stackoverflow.com/questions/tagged/docker

---

**Ready to install?** Download Docker Desktop now:
👉 https://www.docker.com/products/docker-desktop/

**Already have Docker?** Proceed with deployment:
```powershell
cd "d:\DEA Project\Hotel Micro"
.\deploy-aws.ps1
```
