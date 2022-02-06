def call(){
pipeline {
  
        agent any

        environment {
            STAGE = ''
            PIPELINE=''
        }

        parameters {
                choice(name: 'buildTool', choices: ['gradle', 'maven'], description: 'Indicar herramienta de construcción')
        }

        stages{
          stage('Pipeline'){
            steps{
              script{
                try{
                
                println 'Pipeline'
                  if (params.buildTool == "gradle") {
                      gradle.call()
                      figlet 'pipeline:'+env.PIPELINE
                      
                  } else {
                      maven()
                  }
                  figlet 'pipeline ok:'+env.PIPELINE
                }
                catch(Exception e){
                  figlet 'pipeline nok :'+env.PIPELINE
                
                }
                
              }
              
              
            }
          }
        }
/*
        post {
          success {
            figlet 'pipeline:'+env.PIPELINE
            //slackSend color: 'good', message: "[Grupo6][${env.PIPELINE}][Rama: ${GIT_BRANCH}][Stage: ${env.STAGE}][Resultado: Ok]"
          }

          failure {
            //slackSend color: 'danger', message: "[Grupo6][${env.PIPELINE}][Rama: ${GIT_BRANCH}][Stage: ${env.STAGE}][Resultado: No Ok]"
            error "Ejecución fallida en stage ${env.STAGE}"
          }
        }
        */
  }
}
