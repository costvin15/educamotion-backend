pipeline {
    agent {
        docker {
            image 'eclipse-temurin:17.0.9_9-jdk-jammy'
            args '--network host -u root -v /var/run/docker.sock:/var/run/docker.sock'
        }
    }

    tools {
        jdk 'jdk17'
    }

    stages {
        stage('Checkout') {
            steps {
                git branch: 'main', url: 'https://github.com/costvin15/educamotion-backend'
            }
        }
        stage('JaCoCo Run') {
            options {
                timeout(time: 150, unit: 'MINUTES')
            }
            steps {
                sh './mvnw -fn test clean verify'
            }
        }
        stage('JaCoCo Report') {
            options {
                timeout(time: 5, unit: 'MINUTES')
            }
            steps {
                sh './mvnw -f pom.xml package'
            }
        }
        stage('Reports') {
            steps {
                archiveArtifacts artifacts: 'target/site/jacoco-report/index.html', fingerprint: false
                publishHTML target: [
                    allowMissing: false,
                    alwaysLinkToLastBuild: false,
                    keepAll: true,
                    reportDir: 'target/site/jacoco-report/',
                    reportFiles: 'index.html',
                    reportName: 'JaCoCo Report'
                ]
            }
        }
    }

    post {
        always {
            deleteDir()
        }
    }
}
