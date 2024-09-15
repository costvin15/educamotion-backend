pipeline {
    agent any

    tools {
        jdk 'jdk17'
    }

    stages {
        stage('Checkout') {
            steps {
                git branch: 'main', url: 'https://github.com/costvin15/educamotion-backend'
            }
        }
    }
}
