pipeline {
    agent {
        docker {
            image 'instrumentisto/rsync-ssh'
            args '-u 0:0'
        }
    }
    
    parameters {
        string(
            name: 'SERVER_IP',
            defaultValue: '3.238.148.94',
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
                    sh '''
                    rsync -av --delete --exclude='.git' \
                      -e "ssh -p 22 -o StrictHostKeyChecking=no" \
                      ./ ubuntu@${SERVER_IP}:/var/www/html/        
                    
                    # Added the same flag here for the restart command
                    ssh -p 22 -o StrictHostKeyChecking=no ubuntu@${SERVER_IP} 'sudo systemctl restart nginx'
                    '''
                }
            }
        }
    }
}

