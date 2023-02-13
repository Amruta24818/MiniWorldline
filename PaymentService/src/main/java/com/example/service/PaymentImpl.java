package com.example.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.model.CardDetail;
import com.example.model.PaymentRecord;
import com.example.utils.ExcelDataHandler;

@Service
public class PaymentImpl implements IPayment {
	@Autowired
	ExcelDataHandler excelDataHandler;

	@Override
	public List<PaymentRecord> record() {
		List<PaymentRecord> list = new ArrayList<>();
		try {
			list = excelDataHandler.readData();
			return list;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public UUID generateTranId() {
		UUID uuid = UUID.randomUUID(); // Generates random UUID
		return uuid;
	}

	@Override
	public boolean Update(PaymentRecord record) {
		try {
			excelDataHandler.cardDetail(record);
			return true;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public void paymentDetails(PaymentRecord record) {
		try {
			record.setCardNo(Base64.getEncoder().encodeToString(record.getCardNo().getBytes()));
			excelDataHandler.updateCard(record);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void updateInfo(PaymentRecord paymentRecord) {
		try {
			paymentRecord.setCardNo(Base64.getEncoder().encodeToString(paymentRecord.getCardNo().getBytes()));
			paymentDetails(paymentRecord);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public boolean CheckAuthorized(CardDetail cardDetail) {
		try {
			List<PaymentRecord> list = new ArrayList<>();
			list = record();
			PaymentRecord record = list.stream()
					.filter(records -> new String(Base64.getDecoder().decode(records.getCardNo()))
							.equals(cardDetail.getCardNo()))
					.findAny().orElse(null);
			System.out.println(record + " " + cardDetail);
			if (record != null) {
				if ((record.getAuthorization() != "Unauthorize" || record.getAuthorization() == null)) {
					return true;
				} else {
					return false;
				}
			} else {
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

}
