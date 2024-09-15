pipeline {
    agent {
        kubernetes {
            yaml'''
                apiVersion: v1
                kind: Pod
                spec:
                    containers:
                    - name: maven
                      image: maven:3.8.3-openjdk-17
                      command:
                      - cat
                      tty: true
                      volumeMounts:
                      - mountPath: /var/run/docker.sock
                        name: docker-sock
                    volumes:
                    - name: docker-sock
                      path: /var/run/docker.sock
            '''
        }
    }

    stages {
        stage('Checkout') {
            steps {
                container('maven') {
                    git branch: 'main', url: 'https://github.com/costvin15/educamotion-backend'
                }
            }
        }
        stage('JaCoCo Run') {
            options {
                timeout(time: 150, unit: 'MINUTES')
            }
            steps {
                container('maven') {
                    sh 'mvn -fn test clean verify'
                }
            }
        }
        stage('JaCoCo Report') {
            options {
                timeout(time: 5, unit: 'MINUTES')
            }
            steps {
                container('maven') {
                    sh 'mvn -f pom.xml package'
                }
            }
        }
        stage('Reports') {
            steps {
                container('maven') {
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
        }
        stage('SonarQube Analysis') {
            steps {
                container('maven') {
                    withSonarQubeEnv(installationName: 'educamotion-sonarqube') {
                        sh 'mvn org.sonarsource.scanner.maven:sonar-maven-plugin:3.11.0.3922:sonar'
                    }
                }
            }
        }
    }
}
