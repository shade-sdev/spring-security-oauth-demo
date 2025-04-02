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

    environment {
        REGISTRY = "registry-server.devops-tools.svc.cluster.local:5000"
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Build and Push Docker Image using Jib') {
            steps {
                script {
                    def artifactId = sh(script: 'mvn help:evaluate -Dexpression=project.artifactId -q -DforceStdout', returnStdout: true).trim()
                    def version = sh(script: 'mvn help:evaluate -Dexpression=project.version -q -DforceStdout', returnStdout: true).trim()
                    def image = "${env.REGISTRY}/${artifactId}:${version}"

                    echo "Building and pushing image: ${image}"
                    sh "mvn compile jib:build -Djib.to.image=${image}"

                    // Store image name for later use
                    env.IMAGE_NAME = image
                }
            }
        }

        stage('Deploy Pod using Kubernetes Plugin') {
            steps {
                script {
                   container('kubectl') {
                       def artifactId = sh(script: 'mvn help:evaluate -Dexpression=project.artifactId -q -DforceStdout', returnStdout: true).trim()
                       def version = sh(script: 'mvn help:evaluate -Dexpression=project.version -q -DforceStdout', returnStdout: true).trim()
                       def image = "${env.REGISTRY}/${artifactId}:${version}"

                       env.IMAGE_NAME = image

                       sh """
                        kubectl set image deployment/spring-security-oauth-demo app=${env.IMAGE_NAME} --namespace=devops-tools
                        kubectl rollout status deployment/spring-security-oauth-demo --namespace=devops-tools
                    """
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
