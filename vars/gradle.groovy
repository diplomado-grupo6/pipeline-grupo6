def call(){

	
		stage('build')
		{
			figlet 'build'
			STAGE=env.STAGE_NAME
			sh 'env'
            		sh './gradlew clean build'
			println "Stage: ${env.STAGE_NAME}"
					
		}
		
		stage('sonar'){
			figlet 'sonar'	
			STAGE=env.STAGE_NAME
			def scannerHome = tool 'sonar-scanner';
			withSonarQubeEnv('sonar-server') {
			sh "${scannerHome}/bin/sonar-scanner -Dsonar.projectKey=ejemplo-gradle -Dsonar.java.binaries=build -Dsonar.sources=src"
			}
         	}
	
	
		stage('run'){
			figlet 'run'
			STAGE=env.STAGE_NAME
			println "Stage: ${env.STAGE_NAME}"
            		sh "nohup bash gradlew bootRun & "
            		sleep 80
		}
						
	
	
		stage('rest'){
			figlet 'rest'
			STAGE=env.STAGE_NAME
			println "Stage: ${env.STAGE_NAME}"
            		sh "curl -X GET 'http://localhost:8085/rest/mscovid/test?msg=testing'"
		}
			
	
	
		stage('nexusci') {
			figlet 'nexusci'
			STAGE=env.STAGE_NAME
			nexusPublisher nexusInstanceId: 'test-repo',
				nexusRepositoryId: 'test-repo',
				packages: [
				[
					$class: 'MavenPackage',
					mavenAssetList: [
						[classifier: '', extension: '', filePath: 'build/libs/DevOpsUsach2020-0.0.1.jar']
					],
					mavenCoordinate: [
						artifactId: 'DevOpsUsach2020',
						groupId: 'com.devopsusach2020',
						packaging: 'jar',
						version: '0.0.1'
					]
				]
				]
		}
	
	
		stage('downloadnexus') {
			figlet 'downloadnexus'
			sh "curl -X GET -u admin:L1m1t2rm., http://localhost:8081/repository/test-nexus/com/devopsusach2020/DevOpsUsach2020/0.0.1/DevOpsUsach2020-0.0.1.jar -O"		
			
			
		}
	
	
		stage('rundownloadjar') {
			figlet 'rundownloadjar'
			sh "nohup java -jar DevOpsUsach2020-0.0.1.jar &"
			sleep 60
			
		}
	
	
	
		stage('nexuscd') {
			figlet 'nexuscd'
					
			STAGE=env.STAGE_NAME
			nexusPublisher nexusInstanceId: 'test-repo',
				nexusRepositoryId: 'test-repo',
				packages: [
				[
					$class: 'MavenPackage',
					mavenAssetList: [
						[classifier: '', extension: '', filePath: 'build/libs/DevOpsUsach2020-0.0.1.jar']
					],
					mavenCoordinate: [
						artifactId: 'DevOpsUsach2020',
						groupId: 'com.devopsusach2020',
						packaging: 'jar',
						version: '1.0.0'
					]
				]
				]
		}
	
            
        
        
}
return this;

