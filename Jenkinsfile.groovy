pipeline {
    agent {
        kubernetes {
            label 'safe-agent'
            yaml '''
                apiVersion: v1
                kind: Pod
                spec:
                  containers:
                  - name: jnlp
                    image: jenkins/inbound-agent:alpine-jdk11
                    args: ['\$(JENKINS_SECRET)', '\$(JENKINS_NAME)']
                  - name: builder
                    image: maven:3.8.6-jdk-11
                    command: ['cat']
                    tty: true
                    volumeMounts:
                    - name: docker-sock
                      mountPath: /var/run/docker.sock
                  volumes:
                  - name: docker-sock
                    hostPath:
                      path: /var/run/docker.sock
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
