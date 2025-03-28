pipeline {
    agent any

    tools {
        jdk 'openjdk-17'
        maven 'maven3'
    }

    environment {
        DOCKER_REGISTRY = 'localhost:5002'
        PROJECT_NAME = sh(returnStdout: true, script: 'basename $(pwd)').trim()
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Build') {
            steps {
                sh 'mvn clean package -DskipTests'
            }
        }

        stage('Build Docker Image with Jib') {
            steps {
                sh 'mvn compile jib:build -Dimage=${PROJECT_NAME}'
            }
        }

        stage('Push to Registry') {
            steps {
                script {
                    // Get the image name and version from the pom.xml
                    def pom = readMavenPom file: 'pom.xml'
                    def imageName = pom.artifactId
                    def imageVersion = pom.version

                    // Docker login (if needed)
                    // withDockerRegistry([credentialsId: 'docker-registry-credentials', url: "${DOCKER_REGISTRY}"]) {
                        sh """
                            docker tag ${imageName}:${imageVersion} ${DOCKER_REGISTRY}/${imageName}:${imageVersion}
                            docker push ${DOCKER_REGISTRY}/${imageName}:${imageVersion}
                        """
                    // }
                }
            }
        }
    }

    post {
        always {
            // Clean up local images
            sh 'docker image prune -f'
            cleanWs()
        }

        success {
            echo 'Pipeline completed successfully!'
        }

        failure {
            echo 'Pipeline failed. Please check the logs.'
        }
    }
}