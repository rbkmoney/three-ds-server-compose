#
# Build stage
#
FROM maven:3.6.0-jdk-11-slim AS build
COPY src /home/pseudo-schedulator/src
COPY pom.xml /home/pseudo-schedulator
ENV POM_XML=/home/pseudo-schedulator/pom.xml
RUN mvn -f $POM_XML clean package

#
# Package stage
#
FROM adoptopenjdk/openjdk11:jdk-11.0.9.1_1-alpine-slim
COPY --from=build /home/pseudo-schedulator/target/pseudo-schedulator-1.0.0-SNAPSHOT.jar /usr/local/lib/pseudo-schedulator.jar
EXPOSE 8081
ENTRYPOINT ["java","-jar","/usr/local/lib/pseudo-schedulator.jar"]
