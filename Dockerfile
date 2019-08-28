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

#ENTRYPOINT ["java","-jar","/app.jar"]