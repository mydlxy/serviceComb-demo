FROM openjdk:8
EXPOSE 19193

VOLUME /tmp
ADD ca-mfd-prc-bdc-service.jar /app.jar
RUN bash -c 'touch /app.jar'
ENTRYPOINT ["java","-jar","/app.jar"]
