pipeline {
    agent any

    stages {
        stage('SCM') {
            steps {
                git branch: 'main', url: 'https://github.com/costvin15/educamotion-backend'
            }
        }
        stage('Build and test') {
            steps {
                withMaven(maven: 'Maven 3.8') {
                    sh 'mvn -fn clean test verify'
                }
            }
        }
        stage('Generate reports') {
            steps {
                withMaven(maven: 'Maven 3.8') {
                    sh 'mvn -f pom.xml package'
                }
            }
        }
        stage('Publish reports') {
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
        stage('SonarQube analysis') {
            steps {
                withSonarQubeEnv(installationName: 'educamotion-sonarqube') {
                    withMaven(maven: 'Maven 3.8') {
                        sh 'mvn clean package sonar:sonar'
                    }
                }
            }
        }
    }

    post {
        always {
            deleteDir()
        }
    }
}
