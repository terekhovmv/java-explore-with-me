# The context for this Docker file must be the root project directory ../
FROM amazoncorretto:11-alpine
ENV JAVA_TOOL_OPTIONS -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:9091
COPY stats-server/target/stats-server*.jar stats-server.jar
ENTRYPOINT ["java","-jar","/stats-server.jar"]