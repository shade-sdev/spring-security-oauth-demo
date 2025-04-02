pipeline {
    agent {
        kubernetes {
            inheritFrom 'jenkins-agent'
        }
    }

    tools {
        jdk 'openjdk-17'
        maven 'maven3'
        dockerTool 'docker'
    }

    stages {

        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Build') {
            steps {
                sh ''
            }
        }

        stage('Build Docker Image using Jib') {
            steps {
                script {
                    sh 'mvn compile jib:build'
                }
            }
        }

    }

    post {
        always {
            cleanWs()
        }

        success {
            echo '✅ Pipeline completed successfully!'
        }

        failure {
            echo '❌ Pipeline failed. Please check the logs!'
        }
    }
}
