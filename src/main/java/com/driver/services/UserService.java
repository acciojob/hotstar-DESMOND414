package com.driver.services;


import com.driver.model.Subscription;
import com.driver.model.SubscriptionType;
import com.driver.model.User;
import com.driver.model.WebSeries;
import com.driver.repository.UserRepository;
import com.driver.repository.WebSeriesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    WebSeriesRepository webSeriesRepository;


    public Integer addUser(User user){

        //Jut simply add the user to the Db and return the userId returned by the repository
        User savedUser = userRepository.save(user);

        // Return the userId returned by the repository
        return savedUser.getId();
    }

    public Integer getAvailableCountOfWebSeriesViewable(Integer userId){

        //Return the count of all webSeries that a user can watch based on his ageLimit and subscriptionType
        //Hint: Take out all the Webseries from the WebRepository

        User user = userRepository.findById(userId)
                .orElse(null);

        if (user == null) {
            return 0; // User not found, return 0 count
        }

        // Get the user's age limit and subscription type
        int userAgeLimit = user.getAge();
        SubscriptionType userSubscriptionType = user.getSubscription().getSubscriptionType();

        // Retrieve all web series from the webSeriesRepository
        List<WebSeries> webSeriesList = webSeriesRepository.findAll();

        // Count the number of web series viewable by the user based on age limit and subscription type
        int viewableWebSeriesCount = 0;
        for (WebSeries webSeries : webSeriesList) {
            if (webSeries.getAgeLimit() <= userAgeLimit && webSeries.getSubscriptionType().ordinal() <= userSubscriptionType.ordinal()) {
                viewableWebSeriesCount++;
            }
        }

        return viewableWebSeriesCount;

    }


}
