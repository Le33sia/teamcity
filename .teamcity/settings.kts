import jetbrains.buildServer.configs.kotlin.*
import jetbrains.buildServer.configs.kotlin.buildFeatures.perfmon
import jetbrains.buildServer.configs.kotlin.triggers.vcs
import jetbrains.buildServer.configs.kotlin.buildSteps.dockerCommand
import jetbrains.buildServer.configs.kotlin.buildSteps.script

version = "2023.05"
 
project {
 
    buildType(Build)
    buildType(Test)
}
 
object Build : BuildType({
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

object Test : BuildType({
    id("Test")
    name = "Test"

    dependencies {
        dependency(Build) {
            snapshot {
                onDependencyFailure = FailureAction.FAIL_TO_START
            }
        }
    }

    triggers {
        build {
            id = "${Build.id}"
            successfulOnly = true
        }
    }
    
    steps {
        script {
            name = "Testing"
            scriptContent = """
                echo "Testing stage is running..."
            """.trimIndent()        }
    }
})
    
