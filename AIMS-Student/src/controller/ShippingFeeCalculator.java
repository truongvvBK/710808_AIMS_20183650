package controller;

import entity.order.Order;

public interface ShippingFeeCalculator {
    public abstract int calculateShippingFee(Order order, boolean rushOrder);
}
