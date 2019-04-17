# docker build -t shekhar/productcatalogue:1.0 .
FROM openjdk:8-jre
ADD target/productcatalogue-1.0.jar app.jar
ADD product-catalogue.yml app-config.yml
EXPOSE 8020
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","app.jar", "server", "app-config.yml"]
