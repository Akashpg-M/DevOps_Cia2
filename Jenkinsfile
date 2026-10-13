pipeline {
    agent any

    options {
        timestamps()
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Test') {
            steps {
                sh 'mvn clean test'
            }
        }

        stage('Package') {
            steps {
                sh 'mvn package -DskipTests'
            }
        }
    }

    post {
        always {
            junit(
                testResults: 'course-registration-app/target/surefire-reports/TEST-*.xml',
                allowEmptyResults: true
            )
        }

        success {
            archiveArtifacts(
                artifacts: 'course-registration-app/target/*.jar',
                fingerprint: true
            )
        }
    }
}