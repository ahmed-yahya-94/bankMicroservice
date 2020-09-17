package greeting.spring.boot.model;

public class ConsumptionView {
	
	private String name;
	
	private String account_number;
	
	private String debit_balance;

	private String credit_balance;

	private String maxium_credit;
	
	private String account_summary;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAccount_number() {
		return account_number;
	}

	public void setAccount_number(String account_number) {
		this.account_number = account_number;
	}

	public String getDebit_balance() {
		return debit_balance;
	}

	public void setDebit_balance(String debit_balance) {
		this.debit_balance = debit_balance;
	}

	public String getCredit_balance() {
		return credit_balance;
	}

	public void setCredit_balance(String credit_balance) {
		this.credit_balance = credit_balance;
	}

	public String getMaxium_credit() {
		return maxium_credit;
	}

	public void setMaxium_credit(String maxium_credit) {
		this.maxium_credit = maxium_credit;
	}

	public String getAccount_summary() {
		return account_summary;
	}

	public void setAccount_summary(String account_summary) {
		this.account_summary = account_summary;
	}
}
