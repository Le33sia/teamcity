import jetbrains.buildServer.configs.kotlin.*
import jetbrains.buildServer.configs.kotlin.buildFeatures.perfmon
import jetbrains.buildServer.configs.kotlin.triggers.vcs
import jetbrains.buildServer.configs.kotlin.buildSteps.dockerCommand

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
                platform = DockerCommandStep.ImagePlatform.Linux
                namesAndTags = "myImage:1.0"}
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
