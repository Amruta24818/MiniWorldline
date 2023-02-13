package com.example.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFCreationHelper;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

import com.example.model.PaymentRecord;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Component
public class ExcelDataHandler {
	private static String accountDBFile;
	private List<PaymentRecord> objects = new ArrayList<>();
	static Logger logger = LogManager.getLogger("ExcelDataHandler");

	public ExcelDataHandler() throws FileNotFoundException, IOException {
		this.accountDBFile = "paymentDB.xlsx";
		onInit();
	}

	private static void onInit() {

		try {
			File newFile = new File(accountDBFile);

			if (newFile.createNewFile()) {
				logger.info("New file is ready to write Data ");

			} else {
				logger.info("File is already present");
			}
		} catch (Exception e) {
			logger.error(e);
		}

	}

	public List<PaymentRecord> readData() throws IOException {

		try (XSSFWorkbook workbook = new XSSFWorkbook("paymentDB.xlsx")) {

			XSSFSheet sheet = workbook.getSheetAt(0);

			Iterator<Row> rowIterator = sheet.iterator();

			// Skip the first row (the header row)
			rowIterator.next();

			while (rowIterator.hasNext()) {

				Row row = rowIterator.next();
				Iterator<Cell> cellIterator = row.cellIterator();
				String cardNo = cellIterator.next().getStringCellValue();
				String transactionId = cellIterator.next().getStringCellValue();
				String paymentInitiate = cellIterator.next().getStringCellValue();
				String authorization = cellIterator.next().getStringCellValue();
				String paymentResult = cellIterator.next().getStringCellValue();

				PaymentRecord record = new PaymentRecord(cardNo, transactionId, paymentInitiate, authorization,
						paymentResult);

				objects.add(record);
			}

		} catch (Exception e) {
			logger.error(e);
		}
		return objects;

	}

	public void appendData(List<PaymentRecord> records) throws IOException {
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
			for (PaymentRecord record : records) {

				Row row = sheet.createRow(rowNum++);
				row.createCell(0).setCellValue(record.getCardNo());
				row.createCell(1).setCellValue(record.getTransactionId());
				row.createCell(2).setCellValue(record.getPaymentInitiate());
				row.createCell(3).setCellValue(record.getAuthorization());
				row.createCell(4).setCellValue(record.getPaymentResult());

			}

			// Write the file
			try (FileOutputStream newData = new FileOutputStream("paymentDB.xlsx")) {
				workbook.write(newData);
			}
			// Close the workbook
			workbook.close();
		} catch (Exception e) {
			logger.error(e);
		}
	}

	public void updateCard(PaymentRecord record) throws IOException {

		XSSFWorkbook workbook;
		try (FileInputStream fis = new FileInputStream(accountDBFile)) {
			workbook = new XSSFWorkbook(fis);
			XSSFSheet sheet = workbook.getSheetAt(0);

			Iterator<Row> rowIterator = sheet.iterator();

			// Skip the first row (the header row)
			rowIterator.next();

			while (rowIterator.hasNext()) {

				Row row = rowIterator.next();
				String trasId = row.getCell(1).getStringCellValue();

				if (trasId.compareTo(record.getTransactionId()) == 0) {
					row.createCell(2).setCellValue(record.getPaymentInitiate());
					row.createCell(3).setCellValue(record.getAuthorization());
					row.createCell(4).setCellValue(record.getPaymentResult());
				}
			}
			try (FileOutputStream newData = new FileOutputStream("paymentDB.xlsx");) {

				workbook.write(newData);
			}
			workbook.close();
		} catch (Exception e) {
			logger.error(e);
		}
	}

	public void cardDetail(PaymentRecord records) throws IOException {
		// Open the Excel file
		XSSFWorkbook workbook;

		try (FileInputStream fis = new FileInputStream(accountDBFile)) {
			workbook = new XSSFWorkbook(fis);

			XSSFSheet sheet = workbook.getSheetAt(0);

			// Write the data rows
			int rowNum = sheet.getLastRowNum() + 1;

			Row row = sheet.createRow(rowNum++);
			row.createCell(0).setCellValue(records.getCardNo());
			row.createCell(1).setCellValue(records.getTransactionId());
			row.createCell(2).setCellValue(records.getPaymentInitiate());
			row.createCell(3).setCellValue(records.getAuthorization());
			row.createCell(4).setCellValue(records.getPaymentResult());

			// Write the file
			try (FileOutputStream newData = new FileOutputStream("paymentDB.xlsx")) {
				workbook.write(newData);
			}
			// Close the workbook
			workbook.close();
		} catch (Exception e) {
			logger.error(e);
		}
	}

}
