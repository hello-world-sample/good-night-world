pipeline {
    agent any

    options {
        overrideIndexTriggers(false)
    }

    environment {
        DOCKER_IMAGE = 'adamko034/good-night-world'
        GIT_CREDENTIALS_ID = 'github-pat'
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Maven build') {
            steps {
                sh 'mvn clean compile'
            }
        }

        stage('Maven test') {
            steps {
                sh 'mvn test'
            }
        }

        stage('Maven package') {
            steps {
                sh 'mvn package -DskipTests'
            }
        }

        stage('Feature branch done') {
            when {
                allOf {
                    not { branch 'develop' }
                    not { branch 'master' }
                }
            }
            steps {
                echo "Feature branch ${env.BRANCH_NAME}: Maven only — no Docker/deploy."
            }
        }

        stage('Master: CI gate') {
            when { branch 'master' }
            steps {
                echo 'Master CI gate passed. Release via good-night-world-release; promote via deploy-qa / deploy-prod.'
            }
        }

        stage('Develop: Docker build & push') {
            when { branch 'develop' }
            steps {
                script {
                    env.IMAGE_TAG = sh(
                        script: "mvn -q -DforceStdout help:evaluate -Dexpression=project.version",
                        returnStdout: true
                    ).trim()
                    echo "Develop image tag: ${env.IMAGE_TAG}"

                    docker.withRegistry('', 'dockerhub-cred') {
                        def image = docker.build("${env.DOCKER_IMAGE}:${env.IMAGE_TAG}")
                        image.push()
                    }
                }
            }
        }

        stage('Develop: Deploy DEV') {
            when { branch 'develop' }
            environment {
                KUBECONFIG = credentials('minikube-kubeconfig')
            }
            steps {
                sh '''
                    helm upgrade --install good-night-world ./helm/good-night-world \
                      -n good-night-world-dev \
                      -f ./helm/good-night-world/values-dev.yaml \
                      --set image.repository=${DOCKER_IMAGE} \
                      --set image.tag=${IMAGE_TAG} \
                      --create-namespace
                '''
            }
        }
    }
}
