pipeline {
  agent any
  options {
    buildDiscarder(logRotator(numToKeepStr: '3'))
  }
  environment {
    DOCKERHUB_CREDENTIALS = credentials('dockerhub')
    repository = "pbear41/data-collect-server"
    dockerImage = ''
    isDockerBuildSuccess = false;
    version_value = ''
    version = ''
    imageName = ''
  }
  stages {
    stage('get version from gradle') {
      steps {
        script {
          version_value = sh(returnStdout: true, script: "cat build.gradle | grep -o 'version = [^,]*' | tr -d \\'").trim()
          sh "echo Project in version value: $version_value"
          version = version_value.split(/=/)[1].trim()
          sh "echo final version: $version"
          imageName = repository + ":" + version
        }
      }
    }
    stage('get release properties file') {
      steps {
        sh 'cp /mnt/datadisk/data/properties/data-collect-server/application-release.yml ./src/main/resources/application-release.yml'
      }
    }
    stage('Gradle Build') {
      steps {
        sh './gradlew clean build'
      }
    }
    stage('Docker Build') {
      steps {
        script {
        sh 'whoami'
            sh 'echo imageName: ' + imageName
            dockerImage = docker.build imageName
            isDockerBuildSuccess = true
        }
      }
    }
    stage('Login') {
      steps {
        sh 'echo $DOCKERHUB_CREDENTIALS_PSW | docker login -u $DOCKERHUB_CREDENTIALS_USR --password-stdin'
      }
    }
    stage('Push') {
      steps {
        sh 'docker push ' + imageName
      }
    }
  }
  post {
    always {
      sh 'docker logout'
    }
    success {
      script {
        if (isDockerBuildSuccess == true) {
          sh 'docker rmi ' + imageName
        }
      }
    }
  }
}