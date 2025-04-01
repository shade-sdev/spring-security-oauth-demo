pipeline {
    agent {
        kubernetes {
            inheritFrom 'jenkins-agent'
        }
    }

    tools {
        jdk 'openjdk-17'
        maven 'maven3'
        docker 'docker'
    }

    environment {
        DOCKER_REGISTRY = 'registry-server.devops-tools.svc.cluster.local:5000'
    }

    stages {
        stage('Ping Registry') {
            steps {
                script {
                    sh "docker ps"
                    def registryHost = 'registry-server.devops-tools.svc.cluster.local'
                    def registryPort = '5000'
                    sh "ping -c 3 ${registryHost} || echo 'Ping failed, moving to curl test'"
                    sh "curl -v http://${registryHost}:${registryPort}/v2/_catalog || echo 'Registry is not reachable'"
                }
            }
        }

        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Extract Project Info') {
            steps {
                script {
                    env.IMAGE_NAME = sh(script: "mvn help:evaluate -Dexpression=project.artifactId -q -DforceStdout", returnStdout: true).trim()
                    env.IMAGE_VERSION = sh(script: "mvn help:evaluate -Dexpression=project.version -q -DforceStdout", returnStdout: true).trim()
                }
            }
        }

        stage('Build') {
            steps {
                sh ''
            }
        }

        stage('Build Docker Image using Jib') {
            steps {
                container('dind') {
                    sh "mvn compile jib:dockerBuild"
                }
            }
        }

        stage('Push Docker Image') {
            steps {
                script {
                    container('dind') {
                        def fullImageName = "${DOCKER_REGISTRY}/${env.IMAGE_NAME}:${env.IMAGE_VERSION}"
                        sh "docker tag ${env.IMAGE_NAME}:${env.IMAGE_VERSION} ${fullImageName}"
                        sh "docker push ${fullImageName}"
                    }
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
