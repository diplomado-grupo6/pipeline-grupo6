
def validateStages(String stages){
    def stagesSelected=params.split(';').toList()
    def stagesForExecute=[]
    if(isIcOrRelease()=='IC'){
        def defStagesForIC=['compile','unitTest','jar','sonar','nexusUpload']
        stagesSelected.each{ val-> 
            if(stages.any{it==val}==false)
            {
                figlet "stage not found"
                error "stage not found for ${ciOrCd}"
                return
            }
        }
    }
    else if(isIcOrRelease()=='Release'){
        def defStagesForRelease=['gitDiff','nexusDownload','run','test','gitMergeMaster','gitMergeDevelop','gitTagMaster']
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
def getBranchNameWithFormat(){

    return 'ms-iclab-feature-estadomundial'
}
def isMasterOrMain(String branchName)
{
    String gitBranch=env.GIT_BRANCH
    if(gitBranch=='master' || gitBranch=='main'){
        return true
    }

    return false
}
def getTech()
{
    String branchName=env.GIT_BRANCH
    if(branchName.contains('ms')){
        return ''
    }
    else if(branchName.contains('front')){
        return 'frontend'
    }
    else if(branchName.contains('bff')){
        return ''
    }
    else if(branchName.contains('etc')){
        return ''
    }
    return ''
} 
def isIcOrRelease(){
    String branchType=getBranchType()
    if( branchType=='feature' ||  
        branchType=='develop'){
            return 'IC'
    }
    else if(branchType=='release'){
        return 'Release'
    }
        
    error "Undefined branch type ${branchType}"
    

}
def validateFilesMavenOrGradle(){

}
return this;
