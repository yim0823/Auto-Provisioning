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
        def appImage = "registry.hub.docker.com/yim0823"
        def appName = "auto-provisioning"

        def myRepo = checkout scm
        def gitCommit = myRepo.GIT_COMMIT
        def gitBranch = myRepo.GIT_BRANCH
        def shortGitCommit = "${gitCommit[0..10]}"
        def previousGitCommit = sh(script: "git rev-parse ${gitCommit}~", returnStdout: true)

        stage('Test') {
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
        }

        stage('Build') {
            container('gradle') {
                sh "gradle build"
            }
        }

        stage('Create docker-images') {
            container('docker') {
                withCredentials([[
                    $class: 'UsernamePasswordMultiBinding',
                    credentialsId: 'dockerhub',
                    usernameVariable: 'yim0823',
                    passwordVariable: 'hyoung0823'
                ]]) {
                    sh """
                        docker login -u ${DOCKER_HUB_USER} -p ${DOCKER_HUB_PASSWORD}
                        docker build -t namespace/my-image:${gitCommit} .
                        docker push namespace/my-image:${gitCommit}
                    """
                }
            }
        }

        stage('Run kubectl') {
            container('kubectl') {
                sh "kubectl get pods"
            }
        }


    }
}
