product-catalogue
=================

java -jar target/productcatalogue-1.0.jar server product-catalogue.yml

docker build -t shekhar/product .
docker run -p 9010:9010 -p 9011:9011 -d shekhar/product