package com.driver.services;


import com.driver.EntryDto.SubscriptionEntryDto;
import com.driver.model.Subscription;
import com.driver.model.SubscriptionType;
import com.driver.model.User;
import com.driver.repository.SubscriptionRepository;
import com.driver.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class SubscriptionService {

    @Autowired
    SubscriptionRepository subscriptionRepository;

    @Autowired
    UserRepository userRepository;

    private int calculateTotalSubscriptionAmount(Subscription subscription) {
        // Calculate the total amount based on the subscription type and number of screens required
//        int totalAmount = subscription.getSubscriptionType().calculateTotalAmount(subscription.getNoOfScreensSubscribed());
//        subscription.setTotalAmountPaid(totalAmount);
//        subscriptionRepository.save(subscription);
//        return totalAmount;
//        int totalAmount = subscription.getSubscriptionType().getPrice() * subscription.getNoOfScreensSubscribed();
//        subscription.setTotalAmountPaid(totalAmount);
//        subscriptionRepository.save(subscription);
//        return totalAmount;
        SubscriptionType subscriptionType = subscription.getSubscriptionType();
        int totalAmount;

        switch (subscriptionType) {
            case BASIC:
                totalAmount = 500 * subscription.getNoOfScreensSubscribed();
                break;
            case PRO:
                totalAmount = 800 * subscription.getNoOfScreensSubscribed();
                break;
            case ELITE:
                totalAmount = 1000 * subscription.getNoOfScreensSubscribed();
                break;
            default:
                totalAmount = 0;
                break;
        }

        subscription.setTotalAmountPaid(totalAmount);
        subscriptionRepository.save(subscription);
        return totalAmount;
    }
    public Integer buySubscription(SubscriptionEntryDto subscriptionEntryDto){

        //Save The subscription Object into the Db and return the total Amount that user has to pay
        User user = userRepository.findById(subscriptionEntryDto.getUserId()).orElse(null);
        if (user == null) {
            // Handle the case when user is not found
            return null;
        }

        // Create a new Subscription object
        Subscription subscription = new Subscription(
                subscriptionEntryDto.getSubscriptionType(),
                subscriptionEntryDto.getNoOfScreensRequired(),
                new Date(), 0);

        // Set the user of the subscription
        subscription.setUser(user);

//        // Calculate the total amount to be paid
//        int totalAmountToPay = calculateTotalAmount(subscriptionEntryDto.getSubscriptionType(), subscriptionEntryDto.getNoOfScreensRequired());
//
//        // Set the total amount paid in the subscription
//        subscription.setTotalAmountPaid(totalAmountToPay);
//
//        // Save the new Subscription to the database
//        Subscription savedSubscription = subscriptionRepository.save(subscription);
//
//        // Return the total amount that the user has to pay
//        return savedSubscription.getTotalAmountPaid();

//        Subscription subscription = new Subscription(
//                subscriptionEntryDto.getSubscriptionType(),
//                subscriptionEntryDto.getNoOfScreensRequired(),
//                new Date(), 0);

        // Save the new Subscription to the database
        Subscription savedSubscription = subscriptionRepository.save(subscription);

        // Return the total amount that the user has to pay
        return calculateTotalSubscriptionAmount(savedSubscription);

    }
    public Integer upgradeSubscription(Integer userId)throws Exception{

        //If you are already at an ElITE subscription : then throw Exception ("Already the best Subscription")
        //In all other cases just try to upgrade the subscription and tell the difference of price that user has to pay
        //update the subscription in the repository
//        User user = userRepository.findById(userId)
//                .orElseThrow(() -> new Exception("User not found"));
//
//        SubscriptionType currentSubscriptionType = user.getSubscription().getSubscriptionType();
//
//        // Check if the current subscription type is ELITE
//        if (currentSubscriptionType == SubscriptionType.ELITE) {
//            throw new Exception("Already the best Subscription");
//        }
//
//        // Calculate the difference in price between the current and next subscription type
//        int priceDifference = calculatePriceDifference(currentSubscriptionType, currentSubscriptionType.next());
//
//        // Update the user's subscription type to the next level
//        user.getSubscription().setSubscriptionType(currentSubscriptionType.next());
//
//        // Save the updated user to the database
//        userRepository.save(user);
//
//        // Return the difference in price that the user has to pay
//        return priceDifference;

        User user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            // Handle the case when user is not found
            return null;
        }

        // Check if the user is already at ELITE subscription
        SubscriptionType currentSubscriptionType = user.getSubscription().getSubscriptionType();
        if (currentSubscriptionType == SubscriptionType.ELITE) {
            throw new Exception("Already the best Subscription");
        }

        // Calculate the price difference between the current subscription and ELITE subscription
        int priceDifference = calculatePriceDifference(currentSubscriptionType);

        // Update the user's subscription to ELITE
        user.getSubscription().setSubscriptionType(SubscriptionType.ELITE);
        userRepository.save(user);

        // Return the price difference that the user has to pay
        return priceDifference;

    }

    private int calculatePriceDifference(SubscriptionType currentSubscriptionType) {
        int currentPrice;
        int elitePrice = 1000; // Assuming ELITE subscription price is 1000

        switch (currentSubscriptionType) {
            case BASIC:
                currentPrice = 500;
                break;
            case PRO:
                currentPrice = 800;
                break;
            default:
                currentPrice = 0;
                break;
        }

        return elitePrice - currentPrice;
    }

    public Integer calculateTotalRevenueOfHotstar(){

        //We need to find out total Revenue of hotstar : from all the subscriptions combined
        //Hint is to use findAll function from the SubscriptionDb
        List<Subscription> subscriptions = subscriptionRepository.findAll();

        // Calculate the total revenue
        int totalRevenue = 0;
        for (Subscription subscription : subscriptions) {
            totalRevenue += calculateTotalSubscriptionAmount(subscription);
        }

        return totalRevenue;

    }

}
