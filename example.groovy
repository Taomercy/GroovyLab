pipelineJob('job_name_1') {
    concurrentBuild(false)
    logRotator(-1, 30, 1, -1)

    parameters {
        stringParam ('GERRIT_PROJECT','','Use this parameter to select your api project (i.e. Taomercy/Python/django/api).')
        stringParam ('GERRIT_BRANCH','master','Use this parameter to select your repository BRANCH.')
        stringParam ('GERRIT_REFSPEC','master','Use same value as GERRIT_BRANCH.')
    }

    triggers {
        gerrit {
            events {
                patchsetCreated()
            }

            project('ant:Taomercy/Python/*/api', ["ANT:**"])

            configure { gerritTrigger ->
                new groovy.util.Node(gerritTrigger, 'serverName', 'hss')
            }

            buildSuccessful(null, null)
        }
    }

    definition {
        cpsScm {
            scm {
                git {
                    remote {
                        url ('https://taomercy@${COMMON_GERRIT_URL}/a/Taomercy/Python/django/api')
                        credentials ('userpwd-taomercy')
                        branch('master')
                        refspec('master')
                    }
                    extensions {
                        wipeOutWorkspace()
                    }
                }
                lightweight (false)
                scriptPath ("Jenkinsfile.api_check")
            }
        }
    }
}
