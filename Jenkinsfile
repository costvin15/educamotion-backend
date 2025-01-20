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
    }
}
