def call(){
	
        if(validaciones.isIcOrRelease()=='CI'){
        env.PIPELINE='CI'
	
	
        figlet PIPELINE
        stage('compile-unitTest-jar') {
	    env.STAGE=env.STAGE_NAME
		
	    
            figlet 'compile'
            figlet 'unitTest'
            figlet 'jar'

            sh 'chmod a+x gradlew'
            sh './gradlew build'
        }
        
        stage('sonar') {
            env.STAGE=env.STAGE_NAME
            figlet STAGE
            scannerHome = tool 'sonar-scanner'
            withSonarQubeEnv('sonarqube-server') {
                sh "${scannerHome}/bin/sonar-scanner -Dsonar.projectKey=ejemplo-maven -Dsonar.java.binaries=build"
            }
        }
        
        stage('nexusUpload') {
            env.STAGE=env.STAGE_NAME
            figlet env.STAGE
            nexusPublisher nexusInstanceId: 'nexus_test', nexusRepositoryId: 'test-nexus', packages: [[$class: 'MavenPackage', mavenAssetList: [[classifier: '', extension: '', filePath: 'build/libs/DevOpsUsach2020-0.0.1.jar']], mavenCoordinate: [artifactId: 'ejemplo-maven-feature-sonar', groupId: 'com.devopsusach2020', packaging: 'jar', version: '0.0.1']]]
        }
        if(validaciones.getBranchType()=='develop'){
            stage('gitCreateRelease') {
                //solo rama dev
                env.STAGE=env.STAGE_NAME
                figlet env.STAGE
                String rama="release-v1-0-2"
                validaciones.createBranch('develop',rama)

            }
        }
    }
    else if(validaciones.getBranchType()=='release'){

        
        PIPELINE='release'
        figlet 'CD'
        stage('gitDiff') {
            
            env.STAGE=env.STAGE_NAME
            figlet STAGE
            // opcional 
            validaciones.getDiff(env.GIT_BRANCH.split('/')[1],'main')
            sh 'pwd'
            
        }
        
        stage('nexusDownload') {
            env.STAGE=env.STAGE_NAME
            figlet env.STAGE
            sh 'curl -X GET -u admin:L1m1t2rm., http://localhost:8082/repository/test-nexus/com/devopsusach2020/DevOpsUsach2020/0.0.1/DevOpsUsach2020-0.0.1.jar -O'
        }
        
        stage('run') {
            sh 'ls'
            env.STAGE=env.STAGE_NAME
            figlet env.STAGE
            sh 'nohup bash gradlew bootRun &'
            sh 'ps -fea|grep gradle'
            sleep(20)
        }
        stage('test') {
            env.STAGE=env.STAGE_NAME
            figlet env.STAGE
            sh """curl -X GET 'http://localhost:8081/rest/mscovid/test?msg=testing'"""
        }
        stage('gitMergeMaster') {
            env.STAGE=env.STAGE_NAME
            figlet env.STAGE
            validaciones.merge(env.GIT_BRANCH,'main')
            
        }
        stage('gitMergeDevelop') {
            env.STAGE=env.STAGE_NAME
            figlet env.STAGE
            validaciones.merge(env.GIT_BRANCH,'develop')
            
        }
        stage('gitTagMaster') {
            env.STAGE=env.STAGE_NAME
            figlet env.STAGE
            validaciones.tag(env.GIT_BRANCH,'main')
            
        }
    }
	    
    
     
}
return this
