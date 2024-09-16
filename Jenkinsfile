pipeline {
    agent {
        label 'kubeagent'
    }

    environment {
        registry = 'costvin15/educamotion'
        registryCredential = 'dockerhub_id'
        dockerImage = ''
    }

    stages {
        stage('Checkout') {
            steps {
                git branch: 'main', url: 'https://github.com/costvin15/educamotion-backend'
            }
        }
        stage('Performing tests') {
            options {
                timeout(time: 150, unit: 'MINUTES')
            }
            steps {
                sh 'mvn -fn clean test verify'
            }
        }
        stage('Generate build') {
            options {
                timeout(time: 150, unit: 'MINUTES')
            }
            steps {
                sh 'mvn -fn clean package'
            }
        }
        stage('Build Docker image') {
            steps {
                script {
                    dockerImage = docker.build registry + ":$BUILD_NUMBER"
                }
            }
        }
        stage('Reports') {
            steps {
                archiveArtifacts artifacts: 'target/jacoco-report/index.html', fingerprint: false
                publishHTML target: [
                    allowMissing: false,
                    alwaysLinkToLastBuild: false,
                    keepAll: true,
                    reportDir: 'target/jacoco-report/',
                    reportFiles: 'index.html',
                    reportName: 'JaCoCo Report'
                ]
            }
        }
        stage('SonarQube Analysis') {
            steps {
                withSonarQubeEnv(installationName: 'educamotion-sonarqube') {
                    sh 'mvn org.sonarsource.scanner.maven:sonar-maven-plugin:3.11.0.3922:sonar'
                }
            }
        }
    }

    post {
        always {
            archiveArtifacts artifacts: 'target/*.jar', fingerprint: true
            junit 'target/surefire-reports/**/*.xml'
        }
    }
}
