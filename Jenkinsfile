pipeline {
   agent any
   environment {
       registry = "nouralhuda95/demo-cicd-k8s"
   }
   options {
       skipStagesAfterUnstable()
   }

   stages {
        stage('Build') {
            agent { dockerfile true }
            steps {
                sh 'mvn -B -DskipTests clean package'
            }
        }
        stage('Test') {
            agent { dockerfile true }
            steps {
                sh 'mvn test'
            }
            post {
                always {
                    junit 'target/surefire-reports/*.xml'
                }
            }
        }

        stage('Publish Docker Image') {
           steps{
               script {
                   def appimage = docker.build(registry + ":$BUILD_NUMBER", "-f deploy/Dockerfile .")
                   docker.withRegistry( '', 'docker-token' ) {
                       appimage.push()
                   }
               }
           }
        }
       stage ('Deploy') {
           steps {
               script{
                   def image_id = registry + ":$BUILD_NUMBER"
                   sh "ansible-playbook  playbook.yml --extra-vars \"image_id=${image_id}\""
               }
           }
       }
   }
}
