# Microservices Monolithic Docker
This project demonstrate a sample retail application using Java, JAX Rest API and Docker <br>
This is a monolithic project, in the next part we will convert this to Microservices.

## Structure of the Project
#### MVC architecture
The app use redis database, to store all user data in database 01 and to store all product data in database 02

#### Controller - com.app.core.RetailController.java
Contains all code for the backend. This class implements the business logic, such as <br> 
> get stock details <br>
> get product details <br>
> get product details using both databases <br>

#### POJO - com.app.core.Product.java
This class is the plain old java object and describes the attributes of individual products

#### POJO - com.app.core.Stock.java
This class is the plain old java object and describes the attributes of individual stocks

#### JAX Rest Beans - com.app.restfulws.resource.MyJax*.java
These classes implements the beans of Rest api

#### JAX Rest APIS - com.app.restfulws.resource.RetailAction.java
This is the main Rest API class that interacts with the APIs and calls relavant methods in the controller to get appropriate response.


# Please follow below steps to run the application

https://github.com/shekhar2010us/microservices_monolithic_docker/blob/master/setup_notes.md


References:-
https://www.oreilly.com/ideas/how-to-manage-docker-containers-in-kubernetes-with-java
https://github.com/danielbryantuk/oreilly-docker-java-shopping
