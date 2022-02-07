
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
	println "Este método realiza un merge ${ramaOrigen} y ${ramaDestino}"

	//checkout(ramaOrigen)
	//checkout(ramaDestino)

	sh """
	    pwd
	    //ls -ltr
	    rm -rf *
	    //ls -ltr
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
	println "Este método realiza un tag en master de ${ramaOrigen}"

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

return this
