package com.driver.services;

import com.driver.EntryDto.WebSeriesEntryDto;
import com.driver.model.ProductionHouse;
import com.driver.model.WebSeries;
import com.driver.repository.ProductionHouseRepository;
import com.driver.repository.WebSeriesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WebSeriesService {

    @Autowired
    WebSeriesRepository webSeriesRepository;

    @Autowired
    ProductionHouseRepository productionHouseRepository;

    public Integer addWebSeries(WebSeriesEntryDto webSeriesEntryDto)throws  Exception{

        //Add a webSeries to the database and update the ratings of the productionHouse
        //Incase the seriesName is already present in the Db throw Exception("Series is already present")
        //use function written in Repository Layer for the same
        //Dont forget to save the production and webseries Repo
//        boolean isWebSeriesExists = webSeriesRepository.existsBySeriesName(webSeriesEntryDto.getSeriesName());
        WebSeries isWebSeriesExists=webSeriesRepository.findBySeriesName(webSeriesEntryDto.getSeriesName());
        if (isWebSeriesExists!=null) {
            throw new Exception("Series is already present");
        }

        // Create a new WebSeries object
        WebSeries webSeries = new WebSeries();
        webSeries.setSeriesName(webSeriesEntryDto.getSeriesName());
        webSeries.setRating(webSeriesEntryDto.getRating());
        webSeries.setAgeLimit(webSeriesEntryDto.getAgeLimit());
        webSeries.setSubscriptionType(webSeriesEntryDto.getSubscriptionType());

        // Retrieve the production house from the productionHouseRepository using the given productionHouseId
        ProductionHouse productionHouse = productionHouseRepository.findById(webSeriesEntryDto.getProductionHouseId())
                .orElseThrow(() -> new Exception("Production House not found"));

        // Update the production house's ratings
        int totalWebSeriesCount = productionHouse.getWebSeriesList().size();
        double totalRatings = productionHouse.getRatings() * totalWebSeriesCount;
        totalRatings += webSeriesEntryDto.getRating();
        totalWebSeriesCount++;
        double averageRatings = totalRatings / totalWebSeriesCount;
        productionHouse.setRatings(averageRatings);

        // Save the production house to update the ratings
        productionHouseRepository.save(productionHouse);

        // Set the production house for the web series
        webSeries.setProductionHouse(productionHouse);

        // Save the web series to the database
        WebSeries savedWebSeries = webSeriesRepository.save(webSeries);

        // Return the ID of the newly created web series
        return savedWebSeries.getId();


    }

}
