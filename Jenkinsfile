pipeline {
    agent {
        docker {
            image 'maven:3.8.4-openjdk-17-slim'
            args '-v /var/run/docker.sock:/var/run/docker.sock -u root'
        }
    }

    tools {
        jdk 'openjdk-17'
        maven 'maven3'
    }

    environment {
        DOCKER_REGISTRY = 'localhost:5002'
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

        stage('Build Docker Image') {
            steps {
                script {
                    // Install Docker CLI
                    sh '''
                        apt-get update && apt-get install -y docker.io
                    '''

                    // Read project details from pom.xml
                    def pom = readMavenPom file: 'pom.xml'
                    def imageName = pom.artifactId
                    def imageVersion = pom.version

                    // Build image using Jib (local build only)
                    sh "mvn compile jib:buildTar"

                    // Tag and push to local registry
                    sh """
                        docker tag ${imageName}:${imageVersion} ${DOCKER_REGISTRY}/${imageName}:${imageVersion}
                        docker push ${DOCKER_REGISTRY}/${imageName}:${imageVersion}
                    """
                }
            }
        }
    }

    post {
        always {
            cleanWs()
        }

        success {
            echo 'Pipeline completed successfully!'
        }

        failure {
            echo 'Pipeline failed. Please check the logs!'
        }
    }
}