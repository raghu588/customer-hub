pipeline {
    agent any

    stages{
        stage('Build with Gradle'){
            steps{
                // The Git URL has been updated to the new repository.
                checkout([$class: 'GitSCM', branches: [[name: '*/main']], extensions: [], userRemoteConfigs: [[url: 'https://github.com/raghu588/customer-hub']]])

                // Use the Gradle Wrapper to build the project.
                sh './gradlew clean build'
            }
        }

        stage('Build docker image'){
            steps{
                script{
                    sh 'docker build -t ${imagename} .'
                }
            }
        }

        stage('Push image to Hub'){
            steps{
                script{
                    withCredentials([string(credentialsId: 'dockerhub-pwd', variable: 'dockerhubpwd')]) {
                        sh 'docker login -u ${username} -p ${dockerhubpwd}'
                    }
                    sh 'docker push ${docker repo}'
                }
            }
        }

        stage('Deploy to k8s'){
            steps{
                script{
                    kubernetesDeploy (configs: 'app-deployment.yaml',kubeconfigId: 'k8sconfigpwd')
                }
            }
        }
    }
}