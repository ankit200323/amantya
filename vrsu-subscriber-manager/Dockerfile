FROM maven:3.6.3-openjdk-11 as BUILD
MAINTAINER taran
COPY pom.xml /build/
COPY src /build/src/
WORKDIR /build/
RUN mvn clean package -DskipTests
FROM tomcat:9.0.65-jdk17-temurin-jammy
COPY --from=BUILD /build/target/vrsu.war /usr/local/tomcat/webapps/vrsu.war
#ENV TZ=America/Los_Angeles
#EXPOSE 8089
