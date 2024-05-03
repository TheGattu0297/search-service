FROM amazoncorretto:21-alpine-jdk
ENTRYPOINT ["java","-jar","search-service.jar"]
RUN apk add curl
EXPOSE 8080