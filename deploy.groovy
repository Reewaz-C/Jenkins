pipeline {
    agent any
    
    parameters {
        string(
            name: 'SERVER_IP',
            defaultValue: '44.212.49.108',
            description: 'Enter server IP address'
        )
    }
    
    environment {
        SSH_KEY_ID = 'EC2_SSH_KEY'
        SERVER_IP = "${params.SERVER_IP}"
        IMAGE_NAME = "rexxx9865/jenkinsnodeapp"
        IMAGE_TAG = "1.0.1"
    }
    
    triggers {
        githubPush()
    }
    
    stages {

        stage("Build Docker Image") {
            steps {
                sh 'docker build -t ${IMAGE_NAME}:${IMAGE_TAG} .'
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
                sh 'docker push ${IMAGE_NAME}:${IMAGE_TAG}'
            }
        }
    
        stage('Deploy') {
            steps {
                sshagent(['EC2_SSH_KEY']) {
                    sh """
                        ssh -p 22 -o StrictHostKeyChecking=no ubuntu@${SERVER_IP} '
                        sudo docker system prune -af &&
                        sudo docker pull ${IMAGE_NAME}:${IMAGE_TAG}
                        sudo docker compose -f ${IMAGE_NAME}:${IMAGE_TAG} up -d
                        '
                    """
                }
            }
        }
    }
}

