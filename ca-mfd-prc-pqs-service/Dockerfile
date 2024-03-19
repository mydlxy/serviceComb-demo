FROM swr.cn-north-4.myhuaweicloud.com/ca-ybgc/openjdk:8-jdk-alpine as builder
############## build stage ##############
# The application's jar file
ARG JAR_FILE=target/*.jar

# Add the application's jar to the build stage
COPY ${JAR_FILE} /tmp/

# Extract the JAR contents into a directory for each layer
RUN mkdir -p /builder/extracted
RUN java -Djarmode=layertools -jar /tmp/*.jar extract --destination /builder/extracted

############## runtime stage ##############
FROM swr.cn-north-4.myhuaweicloud.com/ca-ybgc/openjdk8-alpine-mp3.0:v1.0

# Java runtime options
ENV _JAVA_OPTIONS "-Xms1024m -Xmx1024m -XX:+UseG1GC -XX:MaxGCPauseMillis=200 -XX:+UnlockExperimentalVMOptions -XX:+UseCGroupMemoryLimitForHeap -Djava.awt.headless=true -Duser.timezone=Asia/Shanghai"

# Add author Info
LABEL maintainer="youcloud"

# Add a volume pointing to /tmp
VOLUME /tmp



# Copy local files and buildstage artifacts
WORKDIR app
COPY --chown=youcloud:youcloud --from=builder /builder/extracted/dependencies/ ./
COPY --chown=youcloud:youcloud --from=builder /builder/extracted/spring-boot-loader/ ./
COPY --chown=youcloud:youcloud --from=builder /builder/extracted/snapshot-dependencies/ ./
RUN true
COPY --chown=youcloud:youcloud --from=builder /builder/extracted/application/ ./

# Run the service
#ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","org.springframework.boot.loader.JarLauncher"]
CMD ["java","-Djava.security.egd=file:/dev/./urandom", "org.springframework.boot.loader.JarLauncher"]
