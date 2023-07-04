package com.driver.services;


import com.driver.EntryDto.ProductionHouseEntryDto;
import com.driver.model.ProductionHouse;
import com.driver.repository.ProductionHouseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductionHouseService {

    @Autowired
    ProductionHouseRepository productionHouseRepository;

    public Integer addProductionHouseToDb(ProductionHouseEntryDto productionHouseEntryDto){


        ProductionHouse productionHouse = new ProductionHouse(productionHouseEntryDto.getName());

        // Save the new ProductionHouse to the database
        ProductionHouse savedProductionHouse = productionHouseRepository.save(productionHouse);

        // Return the ID of the newly created ProductionHouse
        return savedProductionHouse.getId();

    }



}
