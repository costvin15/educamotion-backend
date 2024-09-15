pipeline {
    agent any

    tools {
        jdk 'graalvm'
    }

    stages {
        stage('Checkout') {
            steps {
                git branch: 'main', url: 'https://github.com/costvin15/educamotion-backend'
            }
        }
    }
}
