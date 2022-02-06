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
            //[${PIPELINE}][Rama: ${GIT_BRANCH}][Stage: ${STAGE}][Resultado: Ok]
            slackSend color: 'good', message: "[Grupo6]"
          }

          failure {
            //[${PIPELINE}][Rama: ${GIT_BRANCH}][Stage: ${STAGE}][Resultado: No Ok]
            slackSend color: 'danger', message: "[Grupo6]"
            error "Ejecución fallida en stage ${STAGE}"
          }
        }
        
  }
}
return this;
