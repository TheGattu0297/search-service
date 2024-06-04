FROM amazoncorretto:21-alpine-jdk
ARG JAR_FILE=target/*.jar
<<<<<<< HEAD
COPY  ./target/search-service.jar search-service.jar
ENTRYPOINT ["java","-jar","search-service.jar"]
RUN apk add curl
EXPOSE 8080
=======
COPY  ./target/*.jar search-service.jar
ENTRYPOINT ["java","-jar","search-service.jar"]
RUN apk add curl
EXPOSE 8080
>>>>>>> 9244f69 (N-Gram Analyzer and Code cleanup and versioning changes)
