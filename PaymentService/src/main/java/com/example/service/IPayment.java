package com.example.service;

import java.util.List;
import java.util.UUID;

import com.example.model.CardDetail;
import com.example.model.PaymentRecord;

public interface IPayment {
	public List<PaymentRecord> record();

	public UUID generateTranId();

	public boolean Update(PaymentRecord record);

	public void paymentDetails(PaymentRecord record);

	public boolean CheckAuthorized(CardDetail cardDetail);

	public void updateInfo(PaymentRecord paymentRecord);

}
