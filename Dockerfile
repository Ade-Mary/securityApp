FROM openjdk:21
LABEL maintainer="maryj.net"
ADD target/SecurityApp-0.0.1-SNAPSHOT.jar securedapp.jar
ENTRYPOINT ["java","jar", "securedapp.jar"]