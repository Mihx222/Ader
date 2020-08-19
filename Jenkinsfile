pipeline {
    agent any
    tools {
        maven 'Maven 3.6.0'
        jdk 'jdk11'
    }
    stages {
        stage ('Initialize') {
            steps {
                sh '''
                    echo "PATH = ${PATH}"
                    echo "M2_HOME = ${M2_HOME}"
                '''
            }
        }

        stage ('Build') {
            steps {
                sh 'mvn clean install -DskipTests=true'
            }
        }

        stage ('Deploy') {
            steps{
                sshagent(credentials : ['prod-vm-credentials']) {
                    sh("""sudo scp -i /opt/bitnami/jenkins/jenkins_home/.ssh/id_rsa /bitnami/jenkins/jenkins_home/workspace/Ader_master/Backend/target/backend-1.0-SNAPSHOT.jar jenkins@ader-prod-vm:/home/jenkins/ader/backend
                          sudo scp -i /opt/bitnami/jenkins/jenkins_home/.ssh/id_rsa -rp /bitnami/jenkins/jenkins_home/workspace/Ader_master/Frontend/src/dist/ader-frontend jenkins@ader-prod-vm:/var/www/html
                          ssh -o StrictHostKeyChecking=no jenkins@ader-prod-vm << EOF
                          cd /home/jenkins/ader/backend
                          java -jar /home/jenkins/ader/backend/backend-1.0-SNAPSHOT.jar --spring.profiles.active=prod
                          sudo systemctl restart nginx
                          exit
                         EOF
                    """)
                }
            }
        }
    }
}
