#!/bin/bash

cd shopfront
mvn clean install
if docker build -t shekhar/shopfront . ; then
  docker push shekhar/shopfront
fi
cd ..

cd productcatalogue
mvn clean install
if docker build -t shekhar/productcatalogue . ; then
  docker push shekhar/productcatalogue
fi
cd ..

cd stockmanager
mvn clean install
if docker build -t shekhar/stockmanager . ; then
  docker push shekhar/stockmanager
fi
cd ..