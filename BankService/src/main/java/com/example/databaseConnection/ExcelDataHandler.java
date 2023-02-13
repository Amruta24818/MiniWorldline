package com.example.databaseConnection;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
//import org.apache.commons.collections4.map.HashedMap;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFCreationHelper;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.model.Account;
import com.example.model.CardDetail;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@Component
public class ExcelDataHandler {
	private String filePath;
	private static Integer rowNumber;
	private static String accountDBFile;
	private List<Account> objects = new ArrayList<>();
	static Logger logger = LogManager.getLogger("ExcelDataHandler");

	public ExcelDataHandler() throws FileNotFoundException, IOException {
		this.accountDBFile = "book1.xlsx";
		onInit();
	}

	private static void onInit() {

		try {
			File newFile = new File(accountDBFile);

			if (newFile.createNewFile()) {
				logger.info("New Excel File is created");

			} else {
				logger.info("File is already present");
			}
		} catch (Exception e) {
			logger.error(e);
		}

	}

	public List<Account> readData() throws IOException {

		try (XSSFWorkbook workbook = new XSSFWorkbook("book1.xlsx")) {

			XSSFSheet sheet = workbook.getSheetAt(0);

			Iterator<Row> rowIterator = sheet.iterator();

			// Skip the first row (the header row)
			rowIterator.next();

			while (rowIterator.hasNext()) {
				Row row = rowIterator.next();
				Iterator<Cell> cellIterator = row.cellIterator();

				String accountNumber = cellIterator.next().getStringCellValue();
				String accountHolderName = cellIterator.next().getStringCellValue();
				String ifsc = cellIterator.next().getStringCellValue();
				float balance = (float) cellIterator.next().getNumericCellValue();
				String branch = cellIterator.next().getStringCellValue();
				String cardNumber = cellIterator.next().getStringCellValue();
				String cardHolderName = (String) cellIterator.next().getStringCellValue();
				Date cardExpirDate = cellIterator.next().getDateCellValue();
				String cvv = cellIterator.next().getStringCellValue();

				CardDetail cardDetails = new CardDetail(cardNumber, cardHolderName, cvv, cardExpirDate);

				Account account = new Account(accountNumber, accountHolderName, ifsc, balance, branch, cardDetails);
				objects.add(account);
			}

		} catch (Exception e) {
			logger.error(e);
		}

		return objects;

	}

	public void appendData(List<Account> accounts) throws IOException {
		// Open the Excel file
		XSSFWorkbook workbook;

		try (FileInputStream fis = new FileInputStream(accountDBFile)) {
			workbook = new XSSFWorkbook(fis);

			XSSFSheet sheet = workbook.getSheetAt(0);

			XSSFCreationHelper createHelper = workbook.getCreationHelper();
			XSSFCellStyle cellStyle = workbook.createCellStyle();
			cellStyle.setDataFormat(createHelper.createDataFormat().getFormat("MMMM dd, yyyy"));

			// Write the data rows
			int rowNum = sheet.getLastRowNum() + 1;
			for (Account account : accounts) {

				Row row = sheet.createRow(rowNum++);
				row.createCell(0).setCellValue(account.getAccountNo());
				row.createCell(1).setCellValue(account.getAccountHolderName());
				row.createCell(2).setCellValue(account.getIfsc());
				row.createCell(3).setCellValue(account.getBalance());
				row.createCell(4).setCellValue(account.getBranch());
				row.createCell(5).setCellValue(account.cardDetails.getCardNo());
				row.createCell(6).setCellValue(account.cardDetails.getCardHolderName());
				Cell cell = row.createCell(7);
				cell.setCellValue(account.cardDetails.getExpireDate());
				cell.setCellStyle(cellStyle);
				row.createCell(8).setCellValue(account.cardDetails.getCvv());
			}

			// Write the file
			try (FileOutputStream newData = new FileOutputStream("book1.xlsx")) {
				workbook.write(newData);
			}
			// Close the workbook
			workbook.close();
		} catch (Exception e) {
			logger.error(e);
		}
	}

	public String subtractAmount(Account account, float amount) throws IOException, Exception {
		String paymentStatus = null;

		XSSFWorkbook workbook;
		try (FileInputStream fis = new FileInputStream(accountDBFile)) {
			workbook = new XSSFWorkbook(fis);
			XSSFSheet sheet = workbook.getSheetAt(0);

			Iterator<Row> rowIterator = sheet.iterator();

			// Skip the first row (the header row)
			rowIterator.next();

			while (rowIterator.hasNext()) {

				Row row = rowIterator.next();
				String cardNumber = row.getCell(5).getStringCellValue();
				float balance = (float) row.getCell(3).getNumericCellValue();

				if (cardNumber.compareTo(account.cardDetails.getCardNo()) == 0) {
					float amt = balance - amount;
					row.createCell(3).setCellValue(amt);
					paymentStatus = "Payment Successful";
				}
			}
			try (FileOutputStream newData = new FileOutputStream("book1.xlsx");) {

				workbook.write(newData);
			}
			workbook.close();
		} catch (Exception e) {
			logger.error(e);
		}

		return paymentStatus;

	}
}
