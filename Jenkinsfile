def SERVICE_GROUP = "dcos"
def SERVICE_NAME = "auto-provisioning"
def IMAGE_NAME = "${SERVICE_GROUP}-${SERVICE_NAME}"
def REPOSITORY_URL = "https://github.com/yim0823/Auto-Provisioning.git"
def REPOSITORY_SECRET = ""
def BRANCH_NAME = "master"

def VERSION
def VALUES_HOME
def PROFILE

def label = "worker-${UUID.randomUUID().toString()}"

/* -------- functions ---------- */
def prepare(name = "sample") {
    this.name = name

    echo "# name: ${name}"

    // -- Read the environment variables file to set variables
    if (!fileExists('pipeline.properties')) {
        echo '### No pipeline.properties.'
        exit
    }

    def props = readProperties  file:"pipeline.properties"

    VERSION = props['deploy.app.version']
    PROFILE = props['deploy.app.profile']
    VALUES_HOME = props['deploy.chart.values_home']

    echo "# VERSION : ${VERSION}"
    echo "# PROFILE : ${PROFILE}"
    echo "# VALUES_HOME : ${VALUES_HOME}"

    set_version(VERSION)
    set_values_home(VALUES_HOME)

    this.namespace = ""
    this.sub_domain = ""
    this.chartmuseum = ""
    this.extra_values = ""
}

def set_version(version = "") {
    if (!version) {
        date = (new Date()).format('yyyyMMdd-HHmm')
        version = "v0.0.1-${date}"
    }

    this.version = version

    echo "# version: ${version}"
}

def set_values_home(values_home = "") {
    this.values_home = values_home

    echo "# values_home: ${values_home}"
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

    if (chartmuseum) {
        chartmeseum_init()
    }

    dir("${path}") {
        //Examines a chart for possible issues
        sh "helm lint ."

        if (chartmuseum) {
            sh "helm push . chartmuseum"
        } else {
            sh "helm package ."
        }
    }

    sh """
        helm repo update && \
        helm search ${name}
    """
}

def helm_init() {
    // -- setup helm connectivity to Kubernetes API and Tiller
    println "initiliazing helm client"
    sh "helm init --client-only"

    println "checking client/server version"
    sh "helm version"

    sh "helm repo list && helm repo update"
}

def chartmeseum_init() {
    sh "helm repo add chartmuseum https://${chartmuseum}"

    // -- helm plugin
    count = sh(script: "helm plugin list | grep 'Push chart package' | wc -l", returnStdout: true).trim()
    if ("${count}" == "0") {
        sh """
            helm plugin install https://github.com/chartmuseum/helm-push && \
            helm plugin list
        """
    }
}

def env_namespace(namespace = "") {
    if (!namespace) {
        echo "env_namespace:namespace is null."
        throw new RuntimeException("namespace is null.")
    }

    this.namespace = namespace

    // -- check namespace
    count = sh(script: "kubectl get ns ${namespace} 2>&1 | grep Active | grep ${namespace} | wc -l", returnStdout: true).trim()
    if ("$count" == "0") {
        sh "kubectl create namespace ${namespace}"
    }
}

def  get_replicas(namespace = "") {
    env_namespace(namespace)

    // -- Keep latest pod count
    desired = sh(script: "kubectl get deploy -n ${namespace} | grep ${name} | head -1 | awk '{print \$3}'", returnStdout: true).trim()
    if (desired != "") {
        // extra_values (format = --set KEY=VALUE)
        this.extra_values = "--set replicaCount=${desired}"
    }
}

def deploy(sub_domain = "", profile = "", values_path = "") {
    if (!name) {
        echo "deploy:name is null."
        throw new RuntimeException("name is null.")
    }
    if (!version) {
        echo "deploy:version is null."
        throw new RuntimeException("version is null.")
    }
    if (!namespace) {
        echo "deploy:namespace is null."
        throw new RuntimeException("namespace is null.")
    }
    if (!sub_domain) {
        sub_domain = "${name}-${namespace}"
    }
    if (!profile) {
        profile = namespace
    }

    helm_init()
    this.sub_domain = sub_domain

    if (version == "latest") {
        version = sh(script: "helm search chartmuseum/${name} | grep ${name} | head -1 | awk '{print \$2}'", returnStdout: true).trim()
        if (version == "") {
            echo "deploy:latest version is null."
            throw new RuntimeException("latest version is null.")
        }
    }

    if (!values_path) {
        values_path = ""
        if (values_home) {
            count = sh(script: "ls ${values_home}/${name} | grep '${namespace}.yaml' | wc -l", returnStdout: true).trim()
            if ("${count}" == "0") {
                throw new RuntimeException("values_path not found.")
            } else {
                values_path = "${values_home}/${name}/${namespace}.yaml"
            }
        } else {
            values_path = "charts/${name}/${namespace}.yaml"
        }
    }

    // -- helm install
    if (values_path) {
        sh """
            helm upgrade --install ${name}-${namespace} charts/${name} \
                --version ${version} --namespace ${namespace} --devel \
                --values ${values_path} \
                --set namespace=${namespace} \
                --set profile=${profile} \
                ${extra_values}
        """
    } else {
        sh """
            helm upgrade --install ${name}-${namespace} charts/${name} \
                --version ${version} --namespace ${namespace} --devel \
                --set fullnameOverride=${name} \
                --set ingress.subdomain=${sub_domain} \
                --set ingress.basedomain=${base_domain} \
                --set namespace=${namespace} \
                --set profile=${profile} \
                ${extra_values}
        """
    }

    sh """
        helm search ${name} && \
        helm history ${name}-${namespace} --max 10
    """
}

def notify_slack(STATUS, COLOR) {
    slackSend(color: COLOR, message: STATUS + " : " + "${env.JOB_NAME} [${env.BUILD_NUMBER}] (${env.BUILD_URL})")
}

notifySlack("STARTED", "#FFFF00")

podTemplate(
    label: label,
    containers: [
        containerTemplate(name: 'gradle', image: 'gradle:5.6.1-jdk11', ttyEnabled: true, command: 'cat'),
        containerTemplate(name: 'docker', image: 'docker', ttyEnabled: true, command: 'cat', resourceLimitMemory: '64Mi'),
        containerTemplate(name: 'helm', image: 'lachlanevenson/k8s-helm:latest', ttyEnabled: true, command: 'cat'),
        containerTemplate(name: 'kubectl', image: 'lachlanevenson/k8s-kubectl:latest', command: 'cat', ttyEnabled: true)
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

        stage("Prepare") {
            container("gradle") {
                prepare(IMAGE_NAME)
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
                    sh "gradle clean build -x test -Pprofile=dev"
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

            stage("Deploy Dev") {
                container("kubectl") {
                    try {
                        get_replicas("${SERVICE_GROUP}-dev")
                    } catch (exc) {
                        println "Failed to deploy on dev - ${currentBuild.fullDisplayName}"
                        throw(exc)
                    }
                }
                container("helm") {
                    try {
                        // -- deploy(sub_domain, profile, values_path)
                        deploy("${IMAGE_NAME}-dev", PROFILE)

                        notifySlack("${currentBuild.currentResult}", "#00FF00")
                    } catch (exc) {
                        currentBuild.result = "FAILED"
                        notifySlack("${currentBuild.currentResult}", "#FF0000")

                        println "Failed to deploy on dev - ${currentBuild.fullDisplayName}"
                        throw(exc)
                    }
                }
            }

        }
    }
}