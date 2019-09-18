def appName = "auto-provisioning"
def label = "${appName}-${UUID.randomUUID().toString()}"

def BRANCH_NAME = "master"
def REPOSITORY_URL = "https://github.com/yim0823/Auto-Provisioning.git"
def REPOSITORY_SECRET = ""
def VERSION = ""

podTemplate(
    label: label,
    containers: [
        containerTemplate(name: 'gradle', image: 'gradle:5.6.1-jdk11', ttyEnabled: true, command: 'cat'),
        containerTemplate(name: 'docker', image: 'docker', ttyEnabled: true, command: 'cat', resourceLimitMemory: '64Mi'),
        containerTemplate(name: 'helm', image: 'lachlanevenson/k8s-helm:v2.14.3', ttyEnabled: true, command: 'cat')
    ],
    volumes: [
        hostPathVolume(mountPath: '/home/gradle/.gradle', hostPath: '/tmp/jenkins/.gradle'),
        hostPathVolume(mountPath: '/var/run/docker.sock', hostPath: '/var/run/docker.sock'),
        hostPathVolume(mountPath: "/home/jenkins/.draft", hostPath: "/home/jenkins/.draft"),
        hostPathVolume(mountPath: "/home/jenkins/.helm", hostPath: "/home/jenkins/.helm")
    ]
)
{
    node(label) {
        stage("Prepare") {
            container("gradle") {
                prepare(appName, VERSION)
            }
        }

        /* def myRepo = checkout scm
        def gitCommit = myRepo.GIT_COMMIT
        def gitBranch = myRepo.GIT_BRANCH
        def shortGitCommit = "${gitCommit[0..10]}"
        def previousGitCommit = sh(script: "git rev-parse ${gitCommit}~", returnStdout: true) */

        stage("Checkout") {
            container("gradle") {
                try {
                    if (REPOSITORY_SECRET) {
                        git(url: REPOSITORY_URL, branch: BRANCH_NAME, credentialsId: REPOSITORY_SECRET)
                    } else {
                        git(url: REPOSITORY_URL, branch: BRANCH_NAME)
                    }
                } catch (exc) {
                    throw(exc)
                }
            }
        }

        /* stage("Tests") {
            try {
                container("gradle") {
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

        stage("Gradle build") {
            container("gradle") {
                try {
                    sh "gradle build -x test"
                } catch (exc) {
                    println "Failed to gradle - ${currentBuild.fullDisplayName}"
                    throw(exc)
                }
            }
        }

        if (BRANCH_NAME == "master") {
            stage("Build docker-image") {
                parallel(
                    "Build Docker": {
                        container('docker') {
                            withCredentials([[
                                $class: 'UsernamePasswordMultiBinding',
                                credentialsId: 'dockerhub',
                                usernameVariable: 'DOCKER_HUB_USER',
                                passwordVariable: 'DOCKER_HUB_PASSWORD'
                            ]]) {
                                try {
                                    sh """
                                        docker login -u ${DOCKER_HUB_USER} -p ${DOCKER_HUB_PASSWORD}
                                        docker build -t ${DOCKER_HUB_USER}/${name}:${version} .
                                        docker push ${DOCKER_HUB_USER}/${name}:${version}
                                    """
                                } catch (exc) {
                                    println "Failed to build docker - ${currentBuild.fullDisplayName}"
                                    throw(exc)
                                }
                            }
                        }
                    },
                    "Build Charts": {
                        container("helm") {
                            try {
                                build_chart()
                            } catch (exc) {
                                println "Failed to build Chart - ${currentBuild.fullDisplayName}"
                                throw(exc)
                            }
                        }
                    }
                )
            }

        }
    }
}

def prepare(name = "sample", version = "") {
    // image name
    this.name = name

    echo "# name: ${name}"

    set_version(version)

    this.cluster = ""
    this.namespace = ""
    this.sub_domain = ""
    this.values_home = ""

}

def set_version(version = "") {
    // version
    if (!version) {
        date = (new Date()).format('yyyyMMdd-HHmm')
        version = "v0.0.1-${date}"
    }

    this.version = version

    echo "# version: ${version}"
}

def build_chart(path = "") {
    if (!name) {
        echo "build_chart:name is null."
        throw new RuntimeException("name is null.")
    }
    if (!version) {
        echo "build_chart:version is null."
        throw new RuntimeException("version is null.")
    }
    if (!path) {
        path = "charts/${name}"
    }

    helm_init()

    // helm plugin
    count = sh(script: "helm plugin list | grep 'Push chart package' | wc -l", returnStdout: true).trim()
    if ("${count}" == "0") {
        sh """
            helm plugin install https://github.com/chartmuseum/helm-push && \
            helm plugin list
        """
    }

    // helm push
    dir("${path}") {
        sh "helm lint ."

        if (chartmuseum) {
            sh "helm push . chartmuseum"
        }
    }

    // helm repo
    sh """
        helm repo update && \
        helm search ${name}
    """
}

def helm_init() {
    //setup helm connectivity to Kubernetes API and Tiller
    println "initiliazing helm client"
    sh "helm init --upgrade --service-account tiller worked"

    println "checking client/server version"
    sh "helm version"

    // sh "helm init && helm version"

    if (chartmuseum) {
        sh "helm repo add chartmuseum https://${chartmuseum}"
    }

    sh "sudo helm repo list && helm repo update"
}