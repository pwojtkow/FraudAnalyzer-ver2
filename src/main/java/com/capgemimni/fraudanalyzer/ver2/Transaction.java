package com.capgemimni.fraudanalyzer.ver2;

import org.joda.time.DateTime;

public class Transaction {

	private Integer id;
	private Integer senderId;
	private Integer recipientId;
	private int moneyAmount;
	private DateTime date;
	
	public Transaction() {
		
	}
	
	public Transaction(Integer id, Integer senderId, Integer recipientId,
			int moneyAmount, DateTime date) {
		this.id = id;
		this.setSenderId(senderId);
		this.setRecipientId(recipientId);
		this.setMoneyAmount(moneyAmount);
		this.date = date;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((date == null) ? 0 : date.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + getMoneyAmount();
		result = prime * result
				+ ((getRecipientId() == null) ? 0 : getRecipientId().hashCode());
		result = prime * result
				+ ((getSenderId() == null) ? 0 : getSenderId().hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Transaction other = (Transaction) obj;
		if (date == null) {
			if (other.date != null)
				return false;
		} else if (!date.equals(other.date))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (getMoneyAmount() != other.getMoneyAmount())
			return false;
		if (getRecipientId() == null) {
			if (other.getRecipientId() != null)
				return false;
		} else if (!getRecipientId().equals(other.getRecipientId()))
			return false;
		if (getSenderId() == null) {
			if (other.getSenderId() != null)
				return false;
		} else if (!getSenderId().equals(other.getSenderId()))
			return false;
		return true;
	}

	public Integer getSenderId() {
		return senderId;
	}

	public void setSenderId(Integer senderId) {
		this.senderId = senderId;
	}

	public DateTime getDate() {
		return date;
	}

	public int getMoneyAmount() {
		return moneyAmount;
	}

	public void setMoneyAmount(int moneyAmount) {
		this.moneyAmount = moneyAmount;
	}

	public Integer getRecipientId() {
		return recipientId;
	}

	public void setRecipientId(Integer recipientId) {
		this.recipientId = recipientId;
	}
	
	
	
}
