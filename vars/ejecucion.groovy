def call(){
pipeline {
  
        agent any

        environment {
            STAGE=' '
            PIPELINE=' '
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
                    
                     //figlet "a ${STAGE}"
                    //figlet "a ${PIPELINE}"
                      gradle.call()
                      println "despues de gradle"
                    
                      figlet "b ${STAGE}"
                      figlet "b ${PIPELINE}"
                    
                      
                  } else {
                      maven()
                  }
                                 
              }
                            
            }
          }
        }

        post {
          success {
            
            //[${PIPELINE}][Rama: ${GIT_BRANCH}][Stage: ${STAGE}][Resultado: Ok]
            slackSend color: 'good', message: "[Grupo6][${PIPELINE}][Rama: ${env.GIT_BRANCH}][Stage: ${STAGE}][Resultado: Ok]"
          }

          failure {
            //[${PIPELINE}][Rama: ${GIT_BRANCH}][Stage: ${STAGE}][Resultado: No Ok]
            slackSend color: 'danger', message: "[Grupo6][${PIPELINE}][Rama: ${env.GIT_BRANCH}][Stage: ${STAGE}][Resultado: No Ok]"
            error "Ejecución fallida"
           
          }
        }
        
  }
}
return this
