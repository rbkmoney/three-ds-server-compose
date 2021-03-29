#
# Build stage
#
FROM maven:3.6.0-jdk-11-slim AS build
COPY src /home/ds-simple-mock/src
COPY pom.xml /home/ds-simple-mock
ENV POM_XML=/home/ds-simple-mock/pom.xml
RUN mvn -f $POM_XML clean package

#
# Package stage
#
FROM adoptopenjdk/openjdk11:jdk-11.0.9.1_1-alpine-slim
COPY --from=build /home/ds-simple-mock/target/ds-simple-mock-1.0.0-SNAPSHOT.jar /usr/local/lib/ds-simple-mock.jar
EXPOSE 8081
ENTRYPOINT ["java","-jar","/usr/local/lib/ds-simple-mock.jar"]
