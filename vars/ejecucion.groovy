def call(){
pipeline {
  
        agent any

        environment {
            STAGE = ''
            PIPELINE='CI'
        }

        parameters {
                choice(name: 'buildTool', choices: ['gradle', 'maven'], description: 'Indicar herramienta de construcción')
        }

        stages{
          stage('Pipeline'){
            steps{
              script{
                println 'Pipeline'
                  if (params.buildTool == "gradle") {
                      gradle.call()
                      
                  } else {
                      maven()
                  }
              }
            }
          }
        }

        post {
          success {
            figlet 'pipeline:'+env.PIPELINE
            slackSend color: 'good', message: "[Grupo6][${PIPELINE}][Rama: ${GIT_BRANCH}][Stage: ${STAGE}][Resultado: Ok]"
          }

          failure {
            slackSend color: 'danger', message: "[Grupo6][${PIPELINE}][Rama: ${GIT_BRANCH}][Stage: ${STAGE}][Resultado: No Ok]"
            error "Ejecución fallida en stage ${STAGE}"
          }
        }
  }
}
