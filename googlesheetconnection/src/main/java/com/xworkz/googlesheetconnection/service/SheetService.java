package com.xworkz.googlesheetconnection.service;

import java.io.FileInputStream;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Service;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.Sheets.Spreadsheets.Values.Update;
import com.google.api.services.sheets.v4.SheetsScopes;
import com.google.api.services.sheets.v4.model.AppendValuesResponse;
import com.google.api.services.sheets.v4.model.BatchGetValuesResponse;
import com.google.api.services.sheets.v4.model.BatchUpdateSpreadsheetRequest;
import com.google.api.services.sheets.v4.model.BatchUpdateSpreadsheetResponse;
import com.google.api.services.sheets.v4.model.DeleteDimensionRequest;
import com.google.api.services.sheets.v4.model.DimensionRange;
import com.google.api.services.sheets.v4.model.Request;
import com.google.api.services.sheets.v4.model.Spreadsheet;
import com.google.api.services.sheets.v4.model.SpreadsheetProperties;
import com.google.api.services.sheets.v4.model.UpdateValuesResponse;
import com.google.api.services.sheets.v4.model.ValueRange;
import com.xworkz.googlesheetconnection.dto.StudentDTO;

@Service
public class SheetService {
	private static final String APPLICATION_NAME = "Google sheet new ";
	private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
	private static final String TOKENS_DIRECTORY_PATH = "F:\\x-workz\\googlesheetconnection\\src\\main\\resources\\credentials.json";
	private static final List<String> SCOPES = Collections.singletonList(SheetsScopes.SPREADSHEETS);
	private static final String CREDENTIALS_FILE_PATH = "F:\\x-workz\\googlesheetconnection\\src\\main\\resources\\credentials.json";
	private final String SheetId = "1WiZVpFrIsl_Wf_mpAG8LV-ObF2Gmwb8Wjw9Bev6qmY4";
	private static Sheets sheet;
	private static String range="A2:D2";
	GoogleCredential credential ;
	// loading the connection
	public SheetService() throws IOException, GeneralSecurityException {
		System.err.println("this is Sheet service");

		credential = GoogleCredential.fromStream(new FileInputStream(CREDENTIALS_FILE_PATH))
				.createScoped(SCOPES);
		sheet = new Sheets.Builder(new NetHttpTransport(), JSON_FACTORY, credential)
				.setApplicationName(APPLICATION_NAME).build();
		System.err.println("sheet created");
		System.err.println("Base url:" + sheet.getBaseUrl());
	}

	//creating new google sheet but we are not using it 
	public static String createSheet(String sheetName) throws IOException {
		Spreadsheet spreadsheet = new Spreadsheet().setProperties(new SpreadsheetProperties().setTitle(sheetName));
		spreadsheet = sheet.spreadsheets().create(spreadsheet).setFields("spreadsheetId").execute();
		// Prints the new spreadsheet id
		System.out.println("Spreadsheet ID: " + spreadsheet.getSpreadsheetId());
		return spreadsheet.getSpreadsheetId();
	}
	
	//inserting data into Google Sheet
	public UpdateValuesResponse writeData(String SheetId)
			throws IOException {
		UpdateValuesResponse writeResult=null;
		
		sheet = new Sheets.Builder(new NetHttpTransport(), JSON_FACTORY, credential)
				.setApplicationName(APPLICATION_NAME).build();
		ValueRange writeText = new ValueRange() .setValues(Arrays.asList(
				  Arrays.asList("Punya", "punya@gmail.com","Java FullStack")
				  //,Arrays.asList("Java", "")
				));
		writeResult = sheet.spreadsheets().values()
				  .update(SheetId, "A4", writeText).setValueInputOption("RAW")
				  .execute();
		return writeResult;
	}
	//read single value from Google Sheet
	public  Object getValues(String sheetId) throws IOException{
		System.err.println("Read method");
		sheet = new Sheets.Builder(new NetHttpTransport(), JSON_FACTORY, credential)
				.setApplicationName(APPLICATION_NAME).build();
		
	        List<String> ranges = Arrays.asList("A2");
	        BatchGetValuesResponse readResult = sheet.spreadsheets().values().batchGet(sheetId)
	                .setRanges(ranges).execute();
	       Object returnedText = readResult.getValueRanges().get(0).getValues().get(0).get(0);
		return returnedText;
	}
	public UpdateValuesResponse update() throws IOException {
		UpdateValuesResponse response = null;
	return response;
	}
	
	public String delete() throws IOException {
		System.err.println("delete method");
	sheet.spreadsheets().batchUpdate("your spreadsheet ID", new BatchUpdateSpreadsheetRequest()
		    .setRequests(Arrays.asList(new Request()
		        .setDeleteDimension(new DeleteDimensionRequest()
		            .setRange(new DimensionRange()
		                .setSheetId(0)
		                .setDimension("ROWS")
		                .setStartIndex(1)
		                .setEndIndex(2)
		            )
		        )
		    ))
		).execute();
	System.err.println(sheet);
	return SheetId;
	}
	
	

}
