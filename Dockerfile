FROM hirokimatsumoto/alpine-openjdk-11
MAINTAINER Taehyoung Yim <yim0823@naver.com>

ENV ARTIFACT_NAME=Auto-Provisioning-0.0.1.jar

COPY build/libs/$ARTIFACT_NAME /opt/apps/

EXPOSE 30001
CMD java \
    -XX:+HeapDumpOnOutOfMemoryError \
    -XX:HeapDumpPath=/opt/apps/hprof/heapdump.hprof \
    -jar /opt/apps/$ARTIFACT_NAME \
    -Dspring.profiles.active=dev
#    --spring.config.name=application

#ENTRYPOINT ["java","-jar","/app.jar"]