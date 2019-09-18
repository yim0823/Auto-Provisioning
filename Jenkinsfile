def appName = "auto-provisioning"
def label = "${appName}-${UUID.randomUUID().toString()}"

podTemplate(
    label: label,
    containers: [
        containerTemplate(name: 'gradle', image: 'gradle:5.6.1-jdk11', ttyEnabled: true, command: 'cat'),
        containerTemplate(name: 'docker', image: 'docker', ttyEnabled: true, command: 'cat', resourceLimitMemory: '64Mi'),
        containerTemplate(name: 'kubectl', image: 'lachlanevenson/k8s-kubectl:v1.8.8', ttyEnabled: true, command: 'cat'),
        containerTemplate(name: 'helm', image: 'lachlanevenson/k8s-helm:latest', ttyEnabled: true, command: 'cat')
    ],
    volumes: [
        hostPathVolume(mountPath: '/home/gradle/.gradle', hostPath: '/tmp/jenkins/.gradle'),
        hostPathVolume(mountPath: '/var/run/docker.sock', hostPath: '/var/run/docker.sock')
    ]
)
{
    node(label) {
        stage("Checkout") {
            container('builder') {
                def myRepo = checkout scm
                def gitCommit = myRepo.GIT_COMMIT
                def gitBranch = myRepo.GIT_BRANCH
                def shortGitCommit = "${gitCommit[0..10]}"
                def previousGitCommit = sh(script: "git rev-parse ${gitCommit}~", returnStdout: true)
            }
        }

        /* stage('Test') {
            try {
                container('gradle') {
                    sh """
                        pwd
                        echo "GIT_BRANCH=${gitBranch}" >> /etc/environment
                        echo "GIT_COMMIT=${gitCommit}" >> /etc/environment
                        gradle test
                    """
                }
            } catch (exc) {
                println "Failed to test - ${currentBuild.fullDisplayName}"
                throw(exc)
            }
        } */

        stage('Gradle build') {
            container('gradle') {
                try {
                    sh "gradle build -x test"
                } catch (exc) {
                    throw(exc)
                }
            }
        }

        if (gitBranch.contain("master")) {
            stage('Build docker-image') {
                parallel(
                    "Build Docker": {
                        container('builder') {
                            withCredentials([[
                                $class: 'UsernamePasswordMultiBinding',
                                credentialsId: 'dockerhub',
                                usernameVariable: 'DOCKER_HUB_USER',
                                passwordVariable: 'DOCKER_HUB_PASSWORD'
                            ]]) {
                                try {
                                    sh """
                                        docker login -u ${DOCKER_HUB_USER} -p ${DOCKER_HUB_PASSWORD}
                                        docker build -t ${DOCKER_HUB_USER}/${appName}:${gitCommit} .
                                        docker push ${DOCKER_HUB_USER}/${appName}:${gitCommit}
                                    """
                                } catch (exc) {
                                    throw(exc)
                                }
                            }
                        }
                    }
                )

            }
        }

        stage('Run kubectl') {
            container('kubectl') {
                sh "kubectl get pods"
            }
        }


    }
}