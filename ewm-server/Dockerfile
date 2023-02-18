FROM amazoncorretto:11-alpine
ENV JAVA_TOOL_OPTIONS -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:8081
COPY target/ewm-server*.jar ewm-server.jar
ENTRYPOINT ["java","-jar","/ewm-server.jar"]
