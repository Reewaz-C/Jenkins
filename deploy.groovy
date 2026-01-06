pipeline {
    agent any
    
    parameters {
        string(
            name: 'SERVER_IP',
            defaultValue: '34.205.8.149',
            description: 'Enter server IP address'
        )
    }
    
    environment {
        SSH_KEY_ID = 'EC2_SSH_KEY'
        SERVER_IP = "${params.SERVER_IP}"
    }
    
    triggers {
        githubPush()
    }
    
    stages {

        stage("Build Docker Image") {
            steps {
                sh 'docker build -t rexxx9865/jenkinsnodeapp:1.0.1 .'
            }
        }
        stage("Docker Login") {
            steps {
                withCredentials([usernamePassword(
                    credentialsId: 'dockerhub-creds',
                    usernameVariable: 'DOCKER_USER',
                    passwordVariable: 'DOCKER_PASS'
                )]) {
                    sh '''
                        echo "$DOCKER_PASS" | docker login -u "$DOCKER_USER" --password-stdin
                    '''
                }
            }
        }
        stage("Push Docker Image") {
            steps {
                sh 'docker push rexxx9865/jenkinsnodeapp:1.0.1'
            }
        }
    
        stage('Deploy') {
            steps {
                sshagent(['EC2_SSH_KEY']) {
                    sh """
                        ssh -p 22 -o StrictHostKeyChecking=no ubuntu@${SERVER_IP} '
                        docker pull rexxx9865/jenkinsnodeapp:1.0.1
                        docker compose down || true
                        docker compose up -d --build
                        '
                    """
                }
            }
        }
    }
}

