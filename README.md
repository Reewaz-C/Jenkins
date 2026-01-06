# Node Calculator – CI/CD with Jenkins, Docker & Nginx Reverse Proxy

This project demonstrates a **complete CI/CD pipeline** for a simple **Node.js Calculator application** using **Jenkins, Docker, Docker Hub, Docker Compose, and Nginx as a reverse proxy**.

The goal of this task is to show how a Node.js application can be **built, containerized, pushed, and deployed automatically to an EC2 instance** using DevOps best practices.

---

## 🚀 Tech Stack

* **Node.js & Express** – Application backend
* **HTML / CSS / JavaScript** – Simple UI
* **Docker** – Containerization
* **Docker Hub** – Image registry
* **Docker Compose** – Multi-container orchestration
* **Nginx** – Reverse proxy (Port 80 → Node Port 3000)
* **Jenkins** – CI/CD automation
* **AWS EC2 (Ubuntu)** – Deployment server

---

## 🏗 Architecture Overview

```
GitHub
  ↓ 
Jenkins
  ↓
Docker Build & Push
  ↓
Docker Hub
  ↓
EC2 Server
  ↓
Nginx (Port 80)
  ↓
Node.js App (Port 3000)
```

---

## 📁 Project Structure

```
.
├── Dockerfile
├── Jenkinsfile
├── docker-compose.yml
├── package.json
├── server.js
├── public/
│   ├── index.html
│   ├── style.css
│   └── script.js
└── nginx/
    └── nginx.conf
```

---

## 🐳 Docker Configuration

### Dockerfile (Node.js App)

* Uses **Node 18 Alpine** image
* Installs dependencies using `npm ci`
* Exposes port `3000`
* Runs the app using `npm start`

---

### Docker Compose

* **node-app**: Runs the Node.js calculator
* **nginx**: Acts as a reverse proxy
* Nginx listens on **port 80** and forwards traffic to Node on **port 3000**

---

## 🌐 Nginx Reverse Proxy

Nginx is used to:

* Expose the application on **port 80**
* Hide the Node.js internal port
* Prepare the setup for HTTPS and scaling

Traffic flow:

```
Browser → Nginx (80) → Node App (3000)
```

---

## 🔁 Jenkins CI/CD Pipeline

### Pipeline Stages

1. **Checkout Code** from GitHub
2. **Build Docker Image**
3. **Docker Login** using Jenkins credentials
4. **Push Image** to Docker Hub
5. **Deploy to EC2** using SSH and Docker Compose

---

### Jenkins Credentials Used

* **Docker Hub credentials** (username + access token)
* **EC2 SSH key** for secure deployment

---

## 🚀 Deployment Flow

1. Code is pushed to GitHub
2. Manually trigger pipeline
3. Jenkins builds Docker image
4. Image is pushed to Docker Hub
5. Jenkins connects to EC2 via SSH
6. EC2 pulls the latest image
7. Docker Compose runs Node + Nginx containers

---

## 🧠 Key DevOps Concepts Demonstrated

* CI/CD automation with Jenkins
* Docker image versioning
* Secure credentials handling
* Reverse proxy using Nginx
* Infrastructure reproducibility
* Idempotent deployments using `|| true`

---

## 🧪 How to Access the App

Once deployed, open your browser:

```
http://<EC2_PUBLIC_IP>
```

The calculator UI will be served via **Nginx**.

---

## 📌 Notes

* `|| true` is used to prevent pipeline failure when stopping non-existing containers
