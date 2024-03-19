FROM openjdk:8
EXPOSE 19195

ENV CAS_APPLICATION_NAME="ca-mfd-mom"
ENV CAS_ENV="development"
ENV PAAS_CSE_SC_ENDPOINT="http://10.23.1.152:30100"
ENV PAAS_CSE_CC_ENDPOINT="http://10.23.1.152:30110"

VOLUME /tmp
ADD ca-mfd-prc-audit-service.jar /app.jar
RUN bash -c 'touch /app.jar'
ENTRYPOINT ["java","-jar","/app.jar"]
