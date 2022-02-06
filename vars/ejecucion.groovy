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
                               
                println 'Pipeline'
                  if (params.buildTool == "gradle") {
                      println "antes de gradle"
                      gradle()
                      //def ejecucionGradle = load '/vars/gradle.groovy'
                      //ejecucionGradle.call()
                    
                    println "pipeline:"
                     
                      
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
            slackSend color: 'good', message: "[Grupo6][${PIPELINE}][Rama: ${GIT_BRANCH}][Stage: ${STAGE}][Resultado: Ok]"
          }

          failure {
            slackSend color: 'danger', message: "[Grupo6][${PIPELINE}][Rama: ${GIT_BRANCH}][Stage: ${STAGE}][Resultado: No Ok]"
            error "Ejecución fallida en stage ${STAGE}"
          }
        }
        
  }
}
return this;
