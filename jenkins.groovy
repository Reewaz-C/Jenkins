pipeline {
    agent any

    environment {
        SSH_KEY64 = credentials('SSH_KEY64') // Base64-encoded PEM key
    }

    parameters {
        string(
            name: 'SERVER_IP',
            defaultValue: '44.192.127.149',
            description: 'Enter the server IP address'
        )
    }

    stages {

        stage('Configure SSH') {
            steps {
                sh '''
                    mkdir -p ~/.ssh
                    chmod 700 ~/.ssh

                    cat > ~/.ssh/config <<EOF
Host *
    StrictHostKeyChecking no
    UserKnownHostsFile=/dev/null
EOF

                    chmod 600 ~/.ssh/config
                '''
            }
        }

        stage('Prepare SSH Key') {
            steps {
                sh """
                    mkdir -p /tmp/jenkins_keys
                    echo "$SSH_KEY64" | base64 --decode > /tmp/jenkins_keys/Riwaj-Key.pem
                    chmod 600 /tmp/jenkins_keys/Riwaj-Key.pem
                    ssh-keygen -R ${params.SERVER_IP} || true
                """
            }
        }

        stage('Deploy Code to Server') {
            steps {
                sh """
                    ssh -p 22 -i /tmp/jenkins_keys/Riwaj-Key.pem \
                    ubuntu@${params.SERVER_IP} \
                    'cd /var/www/html && git pull origin main'
                """
            }
        }
    }

    post {
        always {
            sh 'rm -rf /tmp/jenkins_keys'
        }
    }
}
