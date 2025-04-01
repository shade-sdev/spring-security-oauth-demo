pipeline {
    agent {
        kubernetes {
            inheritFrom 'jenkins-agent'
        }
    }

    tools {
        jdk 'openjdk-17'
        maven 'maven3'
    }

    environment {
        DOCKER_REGISTRY = 'registry-server.devops-tools.svc.cluster.local:5000'
    }

    stages {
        stage('Ping Registry') {
            steps {
                script {
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
                sh 'mvn clean package -DskipTests'
            }
        }

        stage('Prepare Maven and pom.xml for Docker') {
            steps {
                // Copy the pom.xml and target directory to a location that can be mounted in the dind container
                sh """
                    mkdir -p /tmp/maven-project
                    cp pom.xml /tmp/maven-project/
                    mkdir -p /tmp/maven-project/target
                    cp -r target/* /tmp/maven-project/target/ || echo "No compiled files found"
                """
            }
        }

        stage('Build Docker Image using Jib') {
            steps {
                container('dind') {
                    sh """
                        # Install OpenJDK 17
                        apt-get update || apk update
                        apt-get install -y openjdk-17-jdk || apk add openjdk17
                        
                        # Set JAVA_HOME
                        export JAVA_HOME=\$(readlink -f \$(which java) | sed "s:/bin/java::")
                        echo "JAVA_HOME set to \$JAVA_HOME"
                        
                        # Install Maven
                        apt-get install -y maven || apk add maven
                        
                        # Verify tools
                        java -version
                        mvn -version
                        
                        # Navigate to the project directory
                        cd /tmp/maven-project
                        
                        # Run Jib
                        mvn compile jib:dockerBuild
                    """
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
