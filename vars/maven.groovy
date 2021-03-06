def call(){

        if(isIcOrRelease()=='CI'){
        env.PIPELINE='CI'
        figlet 'CI'
        stage('compile') {
            STAGE=env.STAGE_NAME
            figlet 'compile'
            sh 'chmod a+x mvnw'
            sh './mvnw clean compile -e'
        }
        
        stage('unitTest') {
            env.STAGE=env.STAGE_NAME
            figlet 'unitTest'
            sh 'chmod a+x mvnw'
            sh './mvnw clean test -e'
        }
        
        stage('jar') {
            env.STAGE=env.STAGE_NAME
            figlet 'jar'
            sh 'chmod a+x mvnw'
            sh './mvnw clean package -e'
        }
        
        stage('sonar') {
            env.STAGE=env.STAGE_NAME
            figlet env.STAGE
            scannerHome = tool 'sonar-scanner'
            withSonarQubeEnv('sonarqube-server') {
                sh "${scannerHome}/bin/sonar-scanner -Dsonar.projectKey=ejemplo-maven -Dsonar.java.binaries=build"
            }
        }
        
        stage('nexusUpload') {
            env.STAGE=env.STAGE_NAME
            figlet env.STAGE
            nexusPublisher nexusInstanceId: 'nexus_test', nexusRepositoryId: 'test-nexus', packages: [[$class: 'MavenPackage', mavenAssetList: [[classifier: '', extension: '', filePath: 'build/DevOpsUsach2020-0.0.1.jar']], mavenCoordinate: [artifactId: 'ejemplo-maven-feature-sonar', groupId: 'com.devopsusach2020', packaging: 'jar', version: '0.0.1']]]
        }
        if(getBranchType()=='develop'){
            stage('gitCreateRelease') {
                //solo rama dev
                env.STAGE=env.STAGE_NAME
                figlet env.STAGE
                String rama="release-v1-0-2"
                createBranch('develop',rama)

            }
        }
    }
    else if(getBranchType()=='release'){

        
        env.PIPELINE='release'
        figlet 'CD'
        stage('gitDiff') {
            
            env.STAGE=env.STAGE_NAME
            figlet env.STAGE
            // opcional 
            getDiff(env.GIT_BRANCH,'main')
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
            sh 'nohup bash mvnw spring-boot:run &'
            sh 'ps -fea|grep mvnw'
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
            merge(env.GIT_BRANCH,'main')
            
        }
        stage('gitMergeDevelop') {
            env.STAGE=env.STAGE_NAME
            figlet env.STAGE
            merge(env.GIT_BRANCH,'develop')
            
        }
        stage('gitTagMaster') {
            env.STAGE=env.STAGE_NAME
            figlet env.STAGE
            tag(env.GIT_BRANCH,'main')
            
        }
    }
     
}

def getBranchType(){
    String gitBranch=env.GIT_BRANCH
    if(gitBranch.contains('feature-') ){
        return 'feature'
    }
    else if(gitBranch.contains('develop') ){
        return 'develop'
    }
    else if(gitBranch.contains('release-') ){
        return 'release'
    }
    error "Undefined branch name"

}
def isIcOrRelease(){
    String branchType=getBranchType()
    if( branchType=='feature' ||  
        branchType=='develop'){
            return 'CI'
    }
    else if(branchType=='release'){
        return 'Release'
    }
        
    error "Undefined branch type ${branchType}"
    

}
def createBranch(String ramaOrigen,String ramaNueva){
    checkout(ramaOrigen)
    sh """
		git checkout -b ${ramaNueva}
		git push origin ${ramaNueva}
		
	"""
}
def getDiff(String ramaOrigen,String ramaDestino){
    println ramaOrigen
    
    checkout(ramaOrigen)

    println ramaDestino
    
        
    sh """
        pwd
        git checkout ${ramaOrigen}
        git pull origin ${ramaDestino}
        git checkout ${ramaDestino}
        git pull origin ${ramaOrigen}
        git checkout ${ramaOrigen}
        git branch
		git diff ${ramaOrigen}...${ramaDestino}
		git status
		
	"""
}

def merge(String ramaOrigen, String ramaDestino){
	println "Este m??todo realiza un merge ${ramaOrigen} y ${ramaDestino}"

	//checkout(ramaOrigen)
	//checkout(ramaDestino)

	sh """
	    pwd
	    ls -ltr
	    rm -rf *
	    ls -ltr
	    git clone https://github.com/diplomado-grupo6/ms-iclab.git
	    git branch
	   	git checkout ${ramaOrigen}
	   	git branch
	   	git config remote.origin.fetch '+refs/heads/*:refs/remotes/origin/*'
        git pull origin ${ramaOrigen}
        git pull origin ${ramaDestino}
	   	git checkout ${ramaDestino}
		git branch
		git merge ${ramaOrigen}
		git push origin ${ramaDestino}
	"""
}

def tag(String ramaOrigen, String ramaDestino){
	println "Este m??todo realiza un tag en master de ${ramaOrigen}"

	if (ramaOrigen.contains('release-v')){
		checkout(ramaDestino)
		def tagValue = ramaOrigen.split('release-')[1]
		def tagList = sh (
                script: 'git tag',
                returnStdout: true
                ).trim()
                
        println 'tagList:'+tagList
		
		if(tagList!=null && tagList.contains(tagValue)){
		    
            sh """
		        git tag -d ${tagValue} 
		    """
		}
				
		sh """
		    git tag ${tagValue}
			git push origin ${tagValue}
		"""

	} else {
		error "La rama ${ramaOrigen} no cumple con nomenclatura definida para rama release (release-v(major)-(minor)-(patch))."
	}
}

def checkout(String rama){
	sh "git reset --hard HEAD; git checkout ${rama}; git pull origin ${rama}"
}


return this;
