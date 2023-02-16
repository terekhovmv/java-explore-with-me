FROM amazoncorretto:11-alpine
ENV JAVA_TOOL_OPTIONS -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:9091
COPY target/stats-server*.jar stats-server.jar
ENTRYPOINT ["java","-jar","/stats-server.jar"]
