# The context for this Docker file must be the root project directory ../
FROM amazoncorretto:11-alpine
ENV JAVA_TOOL_OPTIONS -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:8081
COPY ewm-server/target/ewm-server*.jar ewm-server.jar
COPY stats-server-client/target/stats-server-client*.jar stats-server-client.jar
ENTRYPOINT ["java","-jar","/ewm-server.jar", "-cp", "/stats-server-client.jar"]
