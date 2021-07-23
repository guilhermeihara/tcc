package hyperledger.cefetmg.tcc.dto;

import hyperledger.cefetmg.tcc.models.Transaction;

public class DtoTransaction {

	private String assetName;
	private String oldValue;
	private String proposedValue;
	private String newValue;
	private String origin;
	private String transactionId;
	private String timestamp;
	private String status;
	
	public DtoTransaction(String assetName, String oldValue, String proposedValue, String newValue, String origin,
			String transactionId, String timestamp, String status) {
		super();
		this.assetName = assetName;
		this.oldValue = oldValue;
		this.proposedValue = proposedValue;
		this.newValue = newValue;
		this.origin = origin;
		this.transactionId = transactionId;
		this.timestamp = timestamp;
		this.status = status;
	}
	
	public DtoTransaction(Transaction transaction) {
		super();
		this.assetName = transaction.getAssetName();
		this.oldValue = transaction.getOldValue();
		this.proposedValue = transaction.getCurrentValue();
		this.newValue = transaction.getNewValue();
		this.origin = transaction.getOrigin();
		this.transactionId = transaction.getOrigin();
		this.timestamp = transaction.getTimestamp().toString();
		this.status = transaction.getStatus();
	}
	
	public String getAssetName() {
		return assetName;
	}
	public void setAssetName(String assetName) {
		this.assetName = assetName;
	}
	public String getOldValue() {
		return oldValue;
	}
	public void setOldValue(String oldValue) {
		this.oldValue = oldValue;
	}
	public String getProposedValue() {
		return proposedValue;
	}
	public void setProposedValue(String proposedValue) {
		this.proposedValue = proposedValue;
	}
	public String getNewValue() {
		return newValue;
	}
	public void setNewValue(String newValue) {
		this.newValue = newValue;
	}
	public String getOrigin() {
		return origin;
	}
	public void setOrigin(String origin) {
		this.origin = origin;
	}
	public String getTransactionId() {
		return transactionId;
	}
	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}
	public String getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
	
	
}
