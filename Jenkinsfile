pipeline {
    agent any
    tools {
        maven 'maven'
    }
    stages {
        stage('Build Maven') {
            steps {
                sh 'mvn clean install'
            }
        }
        stage('Build Project') {
            steps {
                sh 'mvn package -Dnative'
            }
        }
        stage('Build Docker Image') {
            steps {
                script {
                    sh 'docker build -t educamotion/backend .'
                }
            }
        }
        stage('Publish Image') {
            steps {
                script {
                    withCredentials([string(credentialsId: 'dockerhub-pwd', variable: 'DOCKERHUB_PASSWORD'), string(credentialsId: 'dockerhub-username', variable: 'DOCKERHUB_USERNAME')]) {
                        sh "docker login -u $DOCKERHUB_USERNAME -p $DOCKERHUB_PASSWORD"
                        sh 'docker push educamotion/backend'
                    }
                }
            }
        }
    }
}
