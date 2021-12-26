package entity.payment;

public class CreditCard extends PaymentCard{
	private int cvvCode;

	public CreditCard(String cardCode, String owner, int cvvCode, String dateExpired) {
		super(cardCode, owner, dateExpired);
		this.cvvCode = cvvCode;
	}
}
