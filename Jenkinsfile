pipeline {
    agent any

    environment {
        IMAGE_NAME = "config-service"
        DOCKER_COMPOSE_PATH = "." // Path to docker-compose.yml (root)
    }

    stages {

        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Build Maven Project') {
            steps {
                sh 'mvn clean install -DskipTests'
            }
        }

        stage('Build Docker Image for Config Service') {
            steps {
                dir('config-service') {
                    sh "docker build -t ${IMAGE_NAME}:latest ."
                }
            }
        }

        stage('Deploy All Services') {
            steps {
                dir("${DOCKER_COMPOSE_PATH}") {
                    sh 'docker-compose down || true'
                    sh 'docker-compose up -d --build'
                }
            }
        }
    }

    post {
        success {
            echo '✅ Deployment successful.'
        }
        failure {
            echo '❌ Deployment failed.'
        }
    }
}
