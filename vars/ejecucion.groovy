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
            
        environment {
            STAGEMP = ''
            PIPELINEMP=''
        }
            
            steps{
              script{
                               
                println 'Pipeline'
                  if (params.buildTool == "gradle") {
                      println "antes de gradle"
                      gradle.call()
                      println "despues de gradle"
                      //println "despues de gradle:"+STAGE
                      /*def variables = sh (
                        script: 'env',
                        returnStdout: true
                      ).trim()           
                      println variables*/
                      println "${STAGEMP}"
                     
                      
                  } else {
                      maven()
                  }
                                 
              }
                            
            }
          }
        }

        post {
          success {
            //figlet 'pipeline:'+ PIPELINE
            //[${PIPELINE}][Rama: ${GIT_BRANCH}][Stage: ${STAGE}][Resultado: Ok]
            slackSend color: 'good', message: "[Grupo6][Rama: ${env.GIT_BRANCH}][Stage: ${STAGE}][Resultado: Ok]"
          }

          failure {
            //[${PIPELINE}][Rama: ${GIT_BRANCH}][Stage: ${STAGE}][Resultado: No Ok]
            slackSend color: 'danger', message: "[Grupo6][Rama: ${env.GIT_BRANCH}][Stage: ${STAGE}][Resultado: No Ok]"
            error "Ejecución fallida en stage ${STAGE}"
          }
        }
        
  }
}
return this
