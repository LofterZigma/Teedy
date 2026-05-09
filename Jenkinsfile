pipeline {
    agent any

    options {
        timestamps()
        disableConcurrentBuilds()
        buildDiscarder(logRotator(numToKeepStr: '20', artifactNumToKeepStr: '10'))
    }

    stages {
        stage('Clean') {
            steps {
                sh 'mvn -B -ntp clean'
            }
        }

        stage('Compile') {
            steps {
                sh 'mvn -B -ntp compile'
            }
        }

        stage('Test') {
            steps {
                sh 'mvn -B -ntp test -Dmaven.test.failure.ignore=true'
            }
        }

        stage('PMD') {
            steps {
                sh 'mvn -B -ntp pmd:pmd'
            }
        }

        stage('JaCoCo') {
            steps {
                sh 'mvn -B -ntp jacoco:report'
            }
        }

        stage('Javadoc') {
            steps {
                sh '''
                    set -e
                    if command -v javadoc >/dev/null 2>&1; then
                        export JAVA_HOME="$(dirname "$(dirname "$(readlink -f "$(command -v javadoc)")")")"
                        mvn -B -ntp javadoc:javadoc
                    else
                        echo "Skipping Javadoc stage because no JDK/javadoc is available on this agent."
                    fi
                '''
            }
        }

        stage('Site') {
            steps {
                sh 'mvn -B -ntp site'
            }
        }

        stage('Package') {
            steps {
                sh 'mvn -B -ntp package -DskipTests'
            }
        }
    }

    post {
        always {
            archiveArtifacts artifacts: '**/target/site/**/*.*', fingerprint: true
            archiveArtifacts artifacts: '**/target/**/*.jar', fingerprint: true
            archiveArtifacts artifacts: '**/target/**/*.war', fingerprint: true
            junit '**/target/surefire-reports/*.xml'
        }
    }
}
