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
                    sh 'ssh -o StrictHostKeyChecking=no jenkins@35.202.81.113 uptime'
                    sh 'scp /bitnami/jenkins/jenkins_home/workspace/Ader_master/Backend/target/backend-1.0-SNAPSHOT.jar jenkins@35.202.81.113:/ader/backend'
                    sh 'scp -rp /bitnami/jenkins/jenkins_home/workspace/Ader_master/Frontend/src/target jenkins@35.202.81.113:/ader/frontend'
                    sh 'java -jar /ader/backend/backend-1.0-SNAPSHOT.jar -Dspring.profiles.active=prod'
                    sh 'ng serve --prod /ader/frontend/'
                }
            }
        }
    }
}
