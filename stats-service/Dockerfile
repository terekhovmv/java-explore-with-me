FROM amazoncorretto:11-alpine
ENV JAVA_TOOL_OPTIONS -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:9091
COPY target/stats-service*.jar stats-service.jar
ENTRYPOINT ["java","-jar","/stats-service.jar"]
