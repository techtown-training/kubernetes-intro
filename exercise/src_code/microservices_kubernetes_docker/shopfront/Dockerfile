# docker build -t shekhar/shopfront:1.0 .

FROM openjdk:8-jre
ADD target/shopfront-1.0.jar app.jar
EXPOSE 8010
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]