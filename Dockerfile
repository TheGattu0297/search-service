FROM amazoncorretto:21-alpine-jdk
ARG JAR_FILE=target/*.jar
COPY  ./target/search-service.jar search-service.jar
ENTRYPOINT ["java","-jar","search-service.jar"]
RUN apk add curl
EXPOSE 8080
