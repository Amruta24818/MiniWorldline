package com.example.dto;

import com.example.model.CardDetail;

public class MerchantReqDTO {
	private float amount;
	private CardDetail cardDetail;

	public MerchantReqDTO(float amount, CardDetail cardDetail) {
		super();
		this.amount = amount;
		this.cardDetail = cardDetail;
	}

	public float getAmount() {
		return amount;
	}

	public void setAmount(float amount) {
		this.amount = amount;
	}

	public CardDetail getCardDetail() {
		return cardDetail;
	}

	public void setCardDetail(CardDetail cardDetail) {
		this.cardDetail = cardDetail;
	}

	@Override
	public String toString() {
		return "AmountDTO [amount=" + amount + ", cardDetail=" + cardDetail + "]";
	}

}
