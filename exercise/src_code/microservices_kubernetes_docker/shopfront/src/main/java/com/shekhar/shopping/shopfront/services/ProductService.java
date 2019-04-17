package com.shekhar.shopping.shopfront.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.shekhar.shopping.shopfront.model.Product;
import com.shekhar.shopping.shopfront.repo.StockRepo;
import com.shekhar.shopping.shopfront.repo.ProductRepo;
import com.shekhar.shopping.shopfront.services.dto.ProductDTO;
import com.shekhar.shopping.shopfront.services.dto.StockDTO;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ProductService {

    @Autowired
    private StockRepo stockRepo;

    @Autowired
    private ProductRepo productRepo;


    public List<Product> getProducts() {
        Map<String, ProductDTO> productDTOs = productRepo.getProductDTOs();
        Map<String, StockDTO> stockDTOMap = stockRepo.getStockDTOs();

        // Merge productDTOs and stockDTOs to a List of Products
        return productDTOs.values().stream()
                .map(productDTO -> {
                    StockDTO stockDTO = stockDTOMap.get(productDTO.getId());
                    if (stockDTO == null) {
                        stockDTO = StockDTO.DEFAULT_STOCK_DTO;
                    }
                    return new Product(productDTO.getId(), stockDTO.getSku(), productDTO.getName(), productDTO.getDescription(), productDTO.getPrice(), stockDTO.getAmountAvailable());
                })
                .collect(Collectors.toList());
    }

    public List<Product> productsNotFound() {
        return Collections.EMPTY_LIST;
    }
}