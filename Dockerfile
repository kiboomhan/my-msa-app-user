FROM openjdk:16-jdk-slim
VOLUME /tmp
COPY target/msa-app-user-1.0.jar msa-app-user.jar
ENTRYPOINT ["java", "-jar", "msa-app-user.jar"]