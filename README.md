# DCOS - AP(Auto Provisioning)
사용자는 Web console 를 통해 작성한 Excel 혹은 Script 내용을 load 하고 작성한 내용을 기반으로 AWS/GCP Resources 관리(생성, 변경)하는 플랫폼이다.
 

## 주기능
 - Excel 혹은 Script 에 기입한 생성/변경 정보를 읽어드린다. (Interpreter)
 - 정보를 객체화하고 DB 에 이력을 남긴다.
   - 진행 상태를 확인할 수 있게 진행 단계를 함께 남긴다. 
 - Cloud product type 에 따라 Rest APIs request 를 보낸다. (Sender)
 
## 환경
 - Java 11
 - Spring-boot-gradle.2.1.5.RELEASE
 - JPA 2.1.5.RELEASE
   - Hikari
 - MariaDB 10.3
 - Undertow
 - Zipkin & Seuth 2.1.2.RELEASE
 - Kafka 2.2.7.RELEASE 
 - Flyway 5.2.4
 - Rest Doc 2.0.3.RELEASE

 ## 특징
 1. Kubernetes 환경에서 서비스될 것이므로 그에 맞는 `개발, 배포 환경`을 구성한다.
    1. 개발에 사용중인 IDE(Intellij)에서 container image 를 빌드하고 자체 repository 에 push 한다.
       1. Docker builder 를 위해 jib plugin 를 활용했다.
          - jib 은, 
            - java application 를 Docker 와 OCI image 로 build 를 위한 maven 혹은 gradle plugin 이다.
            - Dockerfile 를 필요로하지 않는다.
            - Docker image 를 즉시 생성하고 배포한다.
            - 과정,
            
            ![compare steps](https://user-images.githubusercontent.com/3222837/63830443-1a54d780-c9a7-11e9-8673-6481df864227.png)
            
    2. kubernetes resource (deployment, service)를 작성한다.
    3. Intellij 의 Terminal 를 통해 kubernetes cluster 에 배포를 한다.  
       1. kubectl create -f auto-provisioning-deployment.yaml --namespace dcos
       2. kubectl create -f auto-provisioning-service.yaml --namespace dcos
       
2. Dockerfile 과 jib 비교
- Dockerfile:  
```aidl
FROM alpine:3.10 as packager
MAINTAINER Taehyoung Yim <yim0823@naver.com>

RUN apk --no-cache add openjdk11-jdk openjdk11-jmods

ENV JAVA_MINIMAL="/opt/java-minimal"

# build minimal JRE
RUN /usr/lib/jvm/java-11-openjdk/bin/jlink \
    --verbose \
    --add-modules \
        java.base,java.sql,java.naming,java.desktop,java.management,java.security.jgss,java.instrument \
    --compress 2 --strip-debug --no-header-files --no-man-pages \
    --release-info="add:IMPLEMENTOR=radistao:IMPLEMENTOR_VERSION=radistao_JRE" \
    --output "$JAVA_MINIMAL"

FROM alpine:3.10

ENV JAVA_HOME=/opt/java-minimal
ENV PATH="$PATH:$JAVA_HOME/bin"

ENV ARTIFACT_NAME=Auto-Provisioning-0.0.1.jar

COPY --from=packager "$JAVA_HOME" "$JAVA_HOME"
COPY build/libs/$ARTIFACT_NAME /opt/apps/

EXPOSE 30001
CMD java \
    -XX:+HeapDumpOnOutOfMemoryError \
    -XX:HeapDumpPath=/opt/apps/hprof/heapdump.hprof \
    -jar /opt/apps/$ARTIFACT_NAME \
    --spring.config.name=application
```

- jib 
```aidl
# in build.gradle

plugins {
    ...
    id "com.google.cloud.tools.jib" version "1.5.1"
}
.
.
.
jib {
    allowInsecureRegistries = true
    from {
        image = "hirokimatsumoto/alpine-openjdk-11"
    }
    to {
        image = "yim0823/${project.name}:${version}".toLowerCase()
        tags = ["0.0.1"]
        auth {
            username = "yim0823"
            password = "hyoung0823"
        }
    }
    container {
        creationTime = "USE_CURRENT_TIMESTAMP"

        // Set JVM options.
        jvmFlags = ['-XX:+HeapDumpOnOutOfMemoryError', '-XX:HeapDumpPath=/opt/apps/hprof/heapdump.hprof', '-Xms1g']

        // Additional program arguments appended to command to start the container
        args = ['-Dserver.port=8080', '--spring.config.name=application', '--spring.datasource.url=jdbc:mariadb://10.100.61.108:3306/dcos?createDatabaseIfNotExist=true&useMysqlMetadata=true']

        // Expose different port.
        ports = ['30001']

        // Add labels.
        labels = [maintainer: 'dcos', subtainer: 'auto-provisioning-api']
    }
}
```

```aidl
# buidl command

$ gradlew clean bootJar jibDockerBuild

```

3. 이점

![benefits](https://user-images.githubusercontent.com/3222837/63833583-5b041f00-c9ae-11e9-807d-13ade533f4ac.png)

## 서비스 구조
![architecture](https://user-images.githubusercontent.com/3222837/63831911-a4526f80-c9aa-11e9-831f-e0d461db8d67.png)
