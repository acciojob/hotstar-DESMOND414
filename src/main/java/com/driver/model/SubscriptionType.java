package com.driver.model;

public enum SubscriptionType {
//    BASIC,
//    PRO,
//    ELITE,
BASIC(500, 200),
    PRO(800, 250),
    ELITE(1000, 350);

    private final int basePrice;
    private final int pricePerScreen;

    SubscriptionType(int basePrice, int pricePerScreen) {
        this.basePrice = basePrice;
        this.pricePerScreen = pricePerScreen;
    }

    public int getBasePrice() {
        return basePrice;
    }

    public int getPricePerScreen() {
        return pricePerScreen;
    }

    public int calculateTotalAmount(int noOfScreens) {
        return basePrice + (pricePerScreen * noOfScreens);
    }

    public SubscriptionType next() {
        if (this == BASIC) {
            return PRO;
        } else if (this == PRO) {
            return ELITE;
        } else {
            return ELITE; // Assuming ELITE is the highest subscription level
        }
    }
}
