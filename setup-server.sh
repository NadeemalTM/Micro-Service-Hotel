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