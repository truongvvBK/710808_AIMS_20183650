package entity.payment;

public class DomesticCreditCard extends PaymentCard{

    String bankName;

    public DomesticCreditCard(String cardCode, String owner, String dateExpired, String bankName) {
        super(cardCode, owner, dateExpired);
        this.bankName = bankName;
    }
}
