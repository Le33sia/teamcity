import jetbrains.buildServer.configs.kotlin.*
import jetbrains.buildServer.configs.kotlin.buildFeatures.perfmon
import jetbrains.buildServer.configs.kotlin.triggers.vcs
import jetbrains.buildServer.configs.kotlin.buildSteps.dockerCommand

version = "2023.05"

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
    name = "Testing Stage"

    dependencies {
        dependency(Teamcity_Build) {
            snapshot {
            }
        }
    }

    steps {
        script {
            name = "Run Testing Script"
            scriptContent = "echo 'Running tests in the testing stage...'"
        }
    }
})

object Teamcity_Cleanup : BuildType({
    id("Cleanup")
    name = "Cleanup Stage"

    dependencies {
        dependency(Teamcity_Test) {
            snapshot {
            }
        }
    }

    steps {
        script {
            name = "Cleanup Docker"
            scriptContent = "docker system prune -af"
        }
    }
})
