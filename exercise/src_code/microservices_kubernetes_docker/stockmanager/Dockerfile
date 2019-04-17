# docker build -t shekhar/stockmanager:1.0 .

FROM openjdk:8-jre
ADD target/stockmanager-1.0.jar app.jar
EXPOSE 8030
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]
