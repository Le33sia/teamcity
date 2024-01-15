import jetbrains.buildServer.configs.kotlin.*
import jetbrains.buildServer.configs.kotlin.buildFeatures.perfmon
import jetbrains.buildServer.configs.kotlin.triggers.vcs
import jetbrains.buildServer.configs.kotlin.buildSteps.dockerCommand

version = "2023.05"
 
project {
 
    buildType(Teamcity_Build)
    buildType(Teamcity_Test)
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

object Teamcity_Test : BuildType({
    id("Test")
    name = "Test"

    dependencies {
        dependency(Teamcity_Build) {
            snapshot {
                onDependencyFailure = FailureAction.FAIL_TO_START
            }
        }
    }

    steps {
        script {
            name = "Testing"
            scriptContent = "echo 'It\'s testing phase'"
        }
    }
})
