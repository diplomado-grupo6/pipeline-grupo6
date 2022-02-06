def call(){
pipeline {
  
        agent any

        environment {
            STAGE='z'
            PIPELINE=''
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
                    figlet "a ${env.STAGE}"
                    figlet "a ${STAGE}"
                      gradle.call()
                      println "despues de gradle"
                    
                      figlet "b ${STAGE}"
                    
                      
                  } else {
                      maven()
                  }
                                 
              }
                            
            }
          }
        }

        post {
          success {
            figlet 'pipeline:'
            //[${PIPELINE}][Rama: ${GIT_BRANCH}][Stage: ${STAGE}][Resultado: Ok]
            //slackSend color: 'good', message: "[Grupo6][Rama: ${env.GIT_BRANCH}][Stage: ${STAGE}][Resultado: Ok]"
          }

          failure {
            //[${PIPELINE}][Rama: ${GIT_BRANCH}][Stage: ${STAGE}][Resultado: No Ok]
            //slackSend color: 'danger', message: "[Grupo6][Rama: ${env.GIT_BRANCH}][Stage: ${STAGE}][Resultado: No Ok]"
            //error "Ejecución fallida"
            figlet 'error'
          }
        }
        
  }
}
return this
