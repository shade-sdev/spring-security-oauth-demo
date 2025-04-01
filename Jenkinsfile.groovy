pipeline {
    agent {
        kubernetes {
            label 'dind-agent'
            yaml '''
                apiVersion: v1
                kind: Pod
                metadata:
                  labels:
                    jenkins: agent
                spec:
                  containers:
                  - name: jnlp
                    image: jenkins/inbound-agent:4.11-1-alpine
                    args: ['\$(JENKINS_SECRET)', '\$(JENKINS_NAME)']
                    resources:
                      limits:
                        cpu: "500m"
                        memory: "1Gi"
                  - name: dind
                    image: docker:24.0-dind-alpine3.18
                    securityContext:
                      privileged: true
                    env:
                    - name: DOCKER_TLS_CERTDIR
                      value: ""
                    resources:
                      limits:
                        cpu: "1000m"
                        memory: "2Gi"
                  - name: maven
                    image: maven:3.8.6-openjdk-17
                    command: ['cat']
                    tty: true
                    env:
                    - name: DOCKER_HOST
                      value: "tcp://dind:2375"
                    resources:
                      limits:
                        cpu: "1000m"
                        memory: "2Gi"
                  volumes:
                  - name: docker-sock
                    emptyDir: {}
            '''
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
                sh 'mvn clean package -DskipTests'
            }
        }

        stage('Build Docker Image using Jib') {
            steps {
                sh "mvn compile jib:dockerBuild"
            }
        }

        stage('Push Docker Image') {
            steps {
                script {
                    def fullImageName = "${DOCKER_REGISTRY}/${env.IMAGE_NAME}:${env.IMAGE_VERSION}"
                    sh "docker tag ${env.IMAGE_NAME}:${env.IMAGE_VERSION} ${fullImageName}"
                    sh "docker push ${fullImageName}"
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
