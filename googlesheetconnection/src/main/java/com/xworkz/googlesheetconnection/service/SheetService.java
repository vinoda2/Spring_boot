package com.xworkz.googlesheetconnection.service;

import java.io.FileInputStream;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;

import java.util.Collections;
import java.util.List;
import org.springframework.stereotype.Service;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.SheetsScopes;
import com.google.api.services.sheets.v4.model.AppendValuesResponse;
import com.google.api.services.sheets.v4.model.BatchUpdateSpreadsheetRequest;
import com.google.api.services.sheets.v4.model.BatchUpdateSpreadsheetResponse;
import com.google.api.services.sheets.v4.model.ClearValuesRequest;
import com.google.api.services.sheets.v4.model.ClearValuesResponse;
import com.google.api.services.sheets.v4.model.FindReplaceRequest;
import com.google.api.services.sheets.v4.model.FindReplaceResponse;
import com.google.api.services.sheets.v4.model.Request;
import com.google.api.services.sheets.v4.model.Spreadsheet;
import com.google.api.services.sheets.v4.model.SpreadsheetProperties;
import com.google.api.services.sheets.v4.model.UpdateSpreadsheetPropertiesRequest;
import com.google.api.services.sheets.v4.model.UpdateValuesResponse;
import com.google.api.services.sheets.v4.model.ValueRange;
import com.xworkz.googlesheetconnection.dto.StudentDTO;
import com.xworkz.googlesheetconnection.util.GoogleSheetUtil;

@Service
public class SheetService {

	private static final String APPLICATION_NAME = "Google sheet new ";
	private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
	private static final List<String> SCOPES = Collections.singletonList(SheetsScopes.SPREADSHEETS);
	private static final String CREDENTIALS_FILE_PATH = "F:\\x-workz\\googlesheetconnection\\src\\main\\resources\\credentials.json";
	private static Sheets sheet;

	int startRow = 2;
	int endRow = 50;
	int startColumn = 1;
	int endColumn = 4;
	String range = String.format("Sheet1!%s%d:%s%d", GoogleSheetUtil.getColumnLetter(startColumn), startRow,
			GoogleSheetUtil.getColumnLetter(endColumn), endRow);

	GoogleCredential credential;

	// loading the connection
	public SheetService() throws IOException, GeneralSecurityException {
		System.err.println("this is Sheet service");

		credential = GoogleCredential.fromStream(new FileInputStream(CREDENTIALS_FILE_PATH)).createScoped(SCOPES);
		sheet = new Sheets.Builder(new NetHttpTransport(), JSON_FACTORY, credential)
				.setApplicationName(APPLICATION_NAME).build();
		System.err.println("sheet created");
		System.err.println("Base url:" + sheet.getBaseUrl());
	}

	// creating new google sheet but we are not using it
	public static String createSheet(String sheetName) throws IOException {
		Spreadsheet spreadsheet = new Spreadsheet().setProperties(new SpreadsheetProperties().setTitle(sheetName));
		spreadsheet = sheet.spreadsheets().create(spreadsheet).setFields("spreadsheetId").execute();
		// Prints the new spreadsheet id
		System.out.println("Spreadsheet ID: " + spreadsheet.getSpreadsheetId());
		return spreadsheet.getSpreadsheetId();
	}

	// inserting data into Google Sheet
	public AppendValuesResponse writeData(String SheetId, StudentDTO dto) {

		AppendValuesResponse writeResult = null;
		String valueInputOption = "Raw";
		List<List<Object>> objectlist = new ArrayList<>();

		List<Object> list = new ArrayList<>();

		Object name = dto.getStudentName();
		Object email = dto.getEmail();
		Object contactNumbet = dto.getContactNumber();
		Object address = dto.getAddress();
		list.add(name);
		list.add(email);
		list.add(contactNumbet);
		list.add(address);
		objectlist.add(list);
		System.out.println(objectlist);
		ValueRange body = new ValueRange().setValues(objectlist);
		try {
			writeResult = sheet.spreadsheets().values().append(SheetId, range, body)
					.setValueInputOption(valueInputOption).execute();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return writeResult;

	}

	// read single value from Google Sheet
	public List<StudentDTO> getValues(String sheetId) throws IOException {
		System.err.println("Read method");
		ValueRange result = null;
		result = sheet.spreadsheets().values().get(sheetId, range).execute();
		List<List<Object>> list = result.getValues();
		List<StudentDTO> sdto = new ArrayList<StudentDTO>();
		for (List<Object> value : list) {
			StudentDTO dto = new StudentDTO();
			Object name = value.get(0);
			Object email = value.get(1);
			Object contactNumber = value.get(2);
			Object address = value.get(3);
			dto.setStudentName((String) name);
			dto.setEmail((String) email);
			dto.setContactNumber((String) contactNumber);
			dto.setAddress((String) address);
			sdto.add(dto);
		}
		return sdto;
	}
	//update with range
	public UpdateValuesResponse updateData(String SheetId, StudentDTO dto, String range) {
		UpdateValuesResponse updateResponse = null;
		String valueInputOption = "Raw";
		List<List<Object>> objectlist = new ArrayList<>();

		List<Object> list = new ArrayList<>();

		Object name = dto.getStudentName();
		Object email = dto.getEmail();
		Object contactNumbet = dto.getContactNumber();
		Object address = dto.getAddress();
		list.add(name);
		list.add(email);
		list.add(contactNumbet);
		list.add(address);
		objectlist.add(list);
		System.out.println(objectlist);
		ValueRange body = new ValueRange().setValues(objectlist);
		try {
			updateResponse = sheet.spreadsheets().values().update(SheetId, range, body)
					.setValueInputOption(valueInputOption).execute();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return updateResponse;

	}

	// updating or renaming the google sheet
	public BatchUpdateSpreadsheetResponse update(String spreadsheetId, String title) throws IOException {
		List<Request> requests = new ArrayList<>();
		BatchUpdateSpreadsheetResponse response = null;
		// Changing the spread sheet title
		requests.add(new Request().setUpdateSpreadsheetProperties(new UpdateSpreadsheetPropertiesRequest()
				.setProperties(new SpreadsheetProperties().setTitle(title)).setFields("title")));
		BatchUpdateSpreadsheetRequest body = new BatchUpdateSpreadsheetRequest().setRequests(requests);
		response = sheet.spreadsheets().batchUpdate(spreadsheetId, body).execute();
		return response;
	}

	//it's not working properly check once
	public BatchUpdateSpreadsheetResponse updateByName(String sheetId, String name, String replacement)
			throws IOException {

		List<Request> requests = new ArrayList<>();
		BatchUpdateSpreadsheetResponse response = null;
		// Find and replace text.
		requests.add(new Request()
				.setFindReplace(new FindReplaceRequest().setFind(name).setReplacement(replacement).setAllSheets(true)));

		BatchUpdateSpreadsheetRequest body = new BatchUpdateSpreadsheetRequest().setRequests(requests);
		response = sheet.spreadsheets().batchUpdate(sheetId, body).execute();
		FindReplaceResponse findReplaceResponse = response.getReplies().get(2).getFindReplace();
		return response;

	}

	//deletion with range
	public String delete(String sheetId, String range) throws IOException {
		System.err.println("delete method");
		// Assign values to desired fields of `requestBody`:
		ClearValuesRequest requestBody = new ClearValuesRequest();
		Sheets.Spreadsheets.Values.Clear request = sheet.spreadsheets().values().clear(sheetId, range, requestBody);
		ClearValuesResponse response = request.execute();

		return "cell cleared";
	}

}
