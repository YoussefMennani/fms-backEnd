pipeline {
    agent any

    stages {
        stage('Pull Code') {
            steps {
                git 'https://github.com/YoussefMennani/fms-backEnd.git'
            }
        }

        stage('Build & Deploy') {
            steps {
                sh '''
                    docker-compose down
                    docker-compose build
                    docker-compose up -d
                '''
            }
        }
    }
}
