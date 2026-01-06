pipeline {
    agent {
        docker {
            image 'yamanshakya/ssh-client'
            args '-u 0:0'
        }
    }
    
    parameters {
        string(
            name: 'SERVER_IP',
            defaultValue: '44.192.127.149',
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
    
        stage('Deploy') {
            steps {
                sshagent(['EC2_SSH_KEY']) {
                    sh """
                        ssh -p 22 -o StrictHostKeyChecking=no ubuntu@${SERVER_IP} '
                        cd /var/www/html &&
                        git pull origin main &&
                        sudo systemctl restart nginx
                        '
                    """
                }
            }
        }
    }
}

