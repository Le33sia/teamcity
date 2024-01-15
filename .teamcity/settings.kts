version = "2023.05"
 
project {
 
    buildType(Teamcity_Build)
}
 
object Teamcity_Build : BuildType({
    id("Build")
    name = "Build"
 
    vcs {
        root(DslContext.settingsRoot)
    }
    steps {
        dockerCommand {
            commandType = build {
                source = file {
                    path = "Dockerfile"
                }
            }
        }
    }
    triggers {
        vcs {
        }
    }
 
    features {
        perfmon {
        }
    }
})


