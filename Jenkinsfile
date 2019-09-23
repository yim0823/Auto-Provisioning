def SERVICE_GROUP = "dcos"
def SERVICE_NAME = "auto-provisioning"
def IMAGE_NAME = "${SERVICE_GROUP}-${SERVICE_NAME}"
def REPOSITORY_URL = "https://github.com/yim0823/Auto-Provisioning.git"
def REPOSITORY_SECRET = ""

//def VERSION = "0.0.1"
//def VALUES_HOME = "charts"
//def PROFILE = "dev"
//def BRANCH_NAME = "master"

def label = "worker-${UUID.randomUUID().toString()}"

/* -------- functions ---------- */
def prepare(name = "sample", version = "", values_home =".") {
    // image name
    this.name = name

    echo "# name: ${name}"

    // -- Read the environment variables file to set variables
    def props = readProperties  file:"pipeline.properties"
    def VERSION = props["version"]
    def PROFILE = props["profile"]
    def BRANCH_NAME = props["branch_name"]
    def VALUES_HOME = props["values_home"]

    set_version(version)
    set_values_home(values_home)

    this.namespace = ""
    this.sub_domain = ""
    this.chartmuseum = ""
    this.extra_values = ""
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
    //setup helm connectivity to Kubernetes API and Tiller
    println "initiliazing helm client"
    sh "helm init --client-only"

    println "checking client/server version"
    sh "helm version"

    sh "helm repo list && helm repo update"
}

def chartmeseum_init() {
    sh "helm repo add chartmuseum https://${chartmuseum}"

    // helm plugin
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

    // check namespace
    count = sh(script: "kubectl get ns ${namespace} 2>&1 | grep Active | grep ${namespace} | wc -l", returnStdout: true).trim()
    if ("$count" == "0") {
        sh "kubectl create namespace ${namespace}"
    }
}

def  get_replicas(namespace = "") {
    env_namespace(namespace)

    // Keep latest pod count
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

    // latest version
    if (version == "latest") {
        version = sh(script: "helm search chartmuseum/${name} | grep ${name} | head -1 | awk '{print \$2}'", returnStdout: true).trim()
        if (version == "") {
            echo "deploy:latest version is null."
            throw new RuntimeException("latest version is null.")
        }
    }

    // values_path
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

    // helm install
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
/* ------------------------------ */

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
        stage("Prepare") {
            container("gradle") {
                prepare(IMAGE_NAME, VERSION, VALUES_HOME)
            }
        }

    }
}