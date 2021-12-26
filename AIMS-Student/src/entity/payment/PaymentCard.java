package entity.payment;

public class PaymentCard {
    protected String cardCode;
    protected String owner;
    protected String dateExpired;

    public PaymentCard(String cardCode, String owner, String dateExpired) {
        this.cardCode = cardCode;
        this.owner = owner;
        this.dateExpired = dateExpired;
    }

}
