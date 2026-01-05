pipeline {
    agent any
    
    parameters {
        string(
            name: 'SERVER_IP',
            defaultValue: '3.238.148.94',
            description: 'Enter server IP address'
        )
    }
    
    environment {
        SSH_KEY_ID = 'EC2_SSH_KEY'
    }
    
    triggers {
        githubPush()
    }
    
    stages {
    
        stage('Configure SSH') {
            steps {
                sh '''
                mkdir -p ~/.ssh
                chmod 700 ~/.ssh
                cat <<EOF > ~/.ssh/config
Host *
    StrictHostKeyChecking no
EOF
                chmod 600 ~/.ssh/config
                touch ~/.ssh/known_hosts
                chmod 600 ~/.ssh/known_hosts
                '''
            }
        }
        stage('Deploy') {
            steps {
                sshagent(['EC2_SSH_KEY']) {
                    sh '''
                    rsync -av --delete --exclude='.git' -e "ssh -p 22" ./ ubuntu@${SERVER_IP}:/var/www/html/        
                    
                    ssh -p 22 ubuntu@${SERVER_IP} 'sudo systemctl restart nginx'
                    '''
                }
            }
        }
    }
}