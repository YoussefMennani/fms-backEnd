pipeline {
    agent any

    environment {
        REGISTRY = "your-vps-ip-or-domain"  // optional, if you push images to a registry
        CONFIG_IMAGE = "config-service"
        DISCOVERY_IMAGE = "discovery-service"
        GATEWAY_IMAGE = "gateway-service"
        POSITION_IMAGE = "position-service"
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Build All Maven Projects') {
            steps {
                sh 'mvn clean install -DskipTests'
            }
        }

        stage('Build Docker Images') {
            steps {
                // Build config-service image
                dir('config-service') {
                    sh 'docker build -t ${CONFIG_IMAGE}:latest .'
                }
                // Build discovery-service image
                dir('discovery-service') {
                    sh 'docker build -t ${DISCOVERY_IMAGE}:latest .'
                }
                // Build gateway-service image
                dir('gateway-service') {
                    sh 'docker build -t ${GATEWAY_IMAGE}:latest .'
                }
                // Build position-service image
                dir('position-service') {
                    sh 'docker build -t ${POSITION_IMAGE}:latest .'
                }
            }
        }

        stage('Deploy with Docker Compose') {
            steps {
                sh 'docker-compose down || true'
                sh 'docker-compose up -d --build'
            }
        }
    }

    post {
        failure {
            echo '❌ Deployment failed.'
        }
        success {
            echo '✅ All services built and deployed successfully!'
        }
    }
}
