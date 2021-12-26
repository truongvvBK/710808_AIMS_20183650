package controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.logging.Logger;

import entity.cart.Cart;
import entity.cart.CartMedia;
import common.exception.InvalidDeliveryInfoException;
import entity.invoice.Invoice;
import entity.order.Order;
import entity.order.OrderMedia;
import views.screen.popup.PopupScreen;

/**
 * This class controls the flow of place order usecase in our AIMS project
 * @author nguyenlm
 */
public class PlaceOrderController extends BaseController{

    private final int PHONE_NUMBER_LENGTH;

    {
        PHONE_NUMBER_LENGTH = 10;
    }

    /**
     * Just for logging purpose
     */
    private static Logger LOGGER = utils.Utils.getLogger(PlaceOrderController.class.getName());

    /**
     * This method checks the avalibility of product when user click PlaceOrder button
     * @throws SQLException
     */
    public void placeOrder() throws SQLException{
        Cart.getCart().checkAvailabilityOfProduct();
    }

    /**
     * This method creates the new Order based on the Cart
     * @return Order
     * @throws SQLException
     */
    public Order createOrder() throws SQLException{
        Order order = new Order();
        for (Object object : Cart.getCart().getListMedia()) {
            CartMedia cartMedia = (CartMedia) object;
            OrderMedia orderMedia = new OrderMedia(cartMedia.getMedia(), 
                                                   cartMedia.getQuantity(), 
                                                   cartMedia.getPrice());    
            order.getlstOrderMedia().add(orderMedia);
        }
        return order;
    }

    /**
     * This method creates the new Invoice based on order
     * @param order
     * @return Invoice
     */
    public Invoice createInvoice(Order order) {
        return new Invoice(order);
    }

    /**
     * This method takes responsibility for processing the shipping info from user
     * @param info
     * @throws InterruptedException
     * @throws IOException
     */
    public void processDeliveryInfo(HashMap info) throws InterruptedException, IOException{
        LOGGER.info("Process Delivery Info");
        LOGGER.info(info.toString());
        validateDeliveryInfo(info);
    }
    
    /**
   * The method validates the info
   * @param info
   * @throws InterruptedException
   * @throws IOException
   */
    public void validateDeliveryInfo(HashMap<String, String> info) throws InterruptedException, IOException{
        if(!validateName(info.get("name"))){
            throw new InterruptedException("Name is invalid");
        }
        if(!validateAddress(info.get("address"))){
            throw new InterruptedException("Address is invalid");
        }
        if(!validatePhoneNumber(info.get("phone"))){
            throw new InterruptedException("Phone number is invalid");
        }

        if(info.get("rushOrder").equals("true")) {
            if(!validateProvince(info.get("province"))) {
                throw new InterruptedException("Province does not support rush order");
            }
        }
    }

    public boolean validatePhoneNumber(String phoneNumber) {
    	if(phoneNumber.length() != PHONE_NUMBER_LENGTH) return false;
    	if(!phoneNumber.startsWith("0")) return false;
    	try{
    	    Integer.parseInt(phoneNumber);
        } catch (Exception e) {
    	    return false;
        }
    	return true;
    }
    
    public boolean validateName(String name) {
        if(name == null || name.isEmpty() ){
            return false;
        }
        return name.matches("^[a-zA-Z\\s]*$");
    }
    
    public boolean validateAddress(String address) {
        if(address == null || address.isEmpty()) {
            return false;
        }
        return address.matches("^[a-zA-Z0-9\\s]*$");
    }

    public boolean validateProvince(String province){
        if(province == null){
            return false;
        }
        return province.equals("Hà Nội") || province.equals("Đà Nẵng") || province.equals("Hồ Chí Minh");
    }


    private final ShippingFeeCalculator shipFeeCalculator = new AlternativeWeightShipFee();
    /**
     * This method calculates the shipping fees of order
     * @param order
     * @return shippingFee
     */
    public int calculateShippingFee(Order order, boolean rushOrder){
        return shipFeeCalculator.calculateShippingFee(order, rushOrder);
    }

}
