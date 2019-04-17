package com.shekhar.shopping.stockmanager.repositories;

import org.springframework.data.repository.CrudRepository;
import com.shekhar.shopping.stockmanager.model.Stock;

public interface StockRepository extends CrudRepository<Stock, String> {
}