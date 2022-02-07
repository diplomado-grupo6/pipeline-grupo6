def call(){
pipeline {
  
        agent any
       
        parameters {
                choice(name: 'buildTool', choices: ['gradle', 'maven'], description: 'Indicar herramienta de construcción')
        }

        stages{
          stage('Pipeline'){
                              
            steps{
              script{
                               
                println  "Stages: ${env.stagesSelected}"
                if (params.buildTool == "gradle") {
                                          
                      env.STAGE=''
                      env.PIPELINE=''
                    
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
            
            
            slackSend color: 'good', message: "[Grupo6][${PIPELINE}][Rama: ${env.GIT_BRANCH}][Stage: ${STAGE}][Resultado: Ok]"
          }

          failure {
         
            slackSend color: 'danger', message: "[Grupo6][${PIPELINE}][Rama: ${env.GIT_BRANCH}][Stage: ${STAGE}][Resultado: No Ok]"
            error "Ejecución fallida"
           
          }
        }
        
  }
}
return this
