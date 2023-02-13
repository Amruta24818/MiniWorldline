package com.example.Service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.*;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;

import com.example.BankService.BankServiceApplication;
import com.example.databaseConnection.ExcelDataHandler;
import com.example.model.Account;
import com.example.model.CardDetail;
import com.example.service.AccountImpl;
import com.example.service.IAccount;

@RunWith(SpringRunner.class)
@ContextConfiguration
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = BankServiceApplication.class)
public class AccountImplTest {

	 IAccount accountService;
	
	@Autowired
	ExcelDataHandler excelDataHandler;
	
	List<Account> list = new ArrayList<>();
	
	public AccountImplTest(){
		this.accountService = new AccountImpl();
	}
	
	@Test
	void testAddAccount() {
		System.out.println("befortest");
		Account account = new Account("123456", "Anita", "SBI123", (float) 45000.0,"Jaysingpur",
				new CardDetail("12345685", "Anita","123" ,new Date(2010, 1, 3)));
		list.add(account);
		try {
			excelDataHandler.appendData(list);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("test");
		assertThat((int)account.getBalance()).isGreaterThan(0);
	}
}
