package controller;

import entity.order.Order;

public class AlternativeWeightShipFee implements ShippingFeeCalculator{
    @Override
    public int calculateShippingFee(Order order, boolean rushOrder) {
        int fees = order.getAmount();
        int alternativeWeight = (int) (fees * 0.6);
        fees += alternativeWeight;
        if(rushOrder) {
            double RUSH_ORDER_FEE_INCREASE_PERCENT = 0.3;
            fees = (int) (fees * (1 + RUSH_ORDER_FEE_INCREASE_PERCENT));
        }
        return fees;
    }
}
