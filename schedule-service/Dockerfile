# job-schedule-api
#
# VERSION               1.0-SNAPSHOT
FROM relateiq/oracle-java8
MAINTAINER Cyril Neftali <cneftali@gmail.com>
LABEL Description="This image is used to start the job-schedule-api executable" Vendor="" Version="1.0-SNAPSHOT"

VOLUME ["/tmp", "/data"]
ADD target/job-schedule-api-1.0-SNAPSHOT.jar /opt/job-schedule-api/job-schedule-api.jar
EXPOSE 8080
EXPOSE 1221

WORKDIR /opt/job-schedule-api/
RUN bash -c 'touch job-schedule-api.jar'
CMD java -Djava.rmi.server.hostname=localhost -Dcom.sun.management.jmxremote -Dcom.sun.management.jmxremote.authenticate=false -Dcom.sun.management.jmxremote.port=1221 -Dspring.profiles.active=docker -Xmx1024m -jar job-schedule-api.jar