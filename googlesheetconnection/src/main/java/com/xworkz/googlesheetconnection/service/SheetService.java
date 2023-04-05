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
import com.google.api.services.sheets.v4.model.UpdateValuesResponse;
import com.google.api.services.sheets.v4.model.ValueRange;
import com.xworkz.googlesheetconnection.dto.StudentDTO;
import com.xworkz.googlesheetconnection.util.GoogleSheetUtil;

@Service
public class SheetService {

	private static final String APPLICATION_NAME = "Google sheet new ";
	private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
	private static final List<String> SCOPES = Collections.singletonList(SheetsScopes.SPREADSHEETS);
	private static final String CREDENTIALS_FILE_PATH = "C:\\Users\\Dell\\Desktop\\New folder\\x-workz\\googlesheetconnection\\src\\main\\resources\\credentials.json";
	private static Sheets sheet;
	String range = "A2:E";
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

	// inserting data into Google Sheet
	public String writeData(String SheetId, StudentDTO dto) {
		List<List<Object>> objectlist = new ArrayList<>();
		List<Object> list = new ArrayList<>();
		list.add(dto.getStudentName());
		list.add(dto.getEmail());
		list.add(dto.getContactNumber());
		list.add(dto.getAddress());
		list.add(dto.getDisabled());
		objectlist.add(list);
		ValueRange body = new ValueRange().setValues(objectlist);
		try {
			AppendValuesResponse writeResult = sheet.spreadsheets().values().append(SheetId, range, body)
					.setValueInputOption("Raw").execute();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "Data saved Successfully";

	}

	// read single value from Google Sheet
	public List<StudentDTO> getValues(String sheetId) throws IOException {
		ValueRange result = sheet.spreadsheets().values().get(sheetId, range).execute();
		List<List<Object>> list = result.getValues();
		List<StudentDTO> sdto = new ArrayList<StudentDTO>();
		if (list != null && list.size() != 0) {
			for (List<Object> value : list) {
				StudentDTO dto = new StudentDTO();
				dto.setStudentName((String) value.get(0));
				dto.setEmail((String) value.get(1));
				dto.setContactNumber((String) value.get(2));
				dto.setAddress((String) value.get(3));
				String status = (String) value.get(4);
				Boolean statusconvert = Boolean.valueOf(status);
				dto.setDisabled(statusconvert);
				sdto.add(dto);
			}

			return sdto;
		} else {
			System.out.println("Sheet is empty");
		}
		return null;
	}

	// update by properties
	public String listUpdate(String sheetId, StudentDTO dto) throws IOException {
		ValueRange result = sheet.spreadsheets().values().get(sheetId, range).execute();
		List<List<Object>> valueList = result.getValues();
		int updateIndex = 2;
		for (List<Object> list : valueList) {
			if (list.get(0).equals(dto.getStudentName())) {
				String columnLetter = GoogleSheetUtil.getColumnLetter(1) + updateIndex;
				String updateRange = columnLetter + ":D";
				List<Object> updateList = new ArrayList<Object>();
				updateList.add(dto.getStudentName());
				updateList.add(dto.getEmail());
				updateList.add(dto.getContactNumber());
				updateList.add(dto.getAddress());
				List<List<Object>> lists = new ArrayList();
				lists.add(updateList);
				ValueRange body = new ValueRange().setValues(lists);
				UpdateValuesResponse updateResponse = sheet.spreadsheets().values().update(sheetId, updateRange, body)
						.setValueInputOption("Raw").execute();
				return "update Successfully done";
			}
			updateIndex++;
		}
		return "update failed";
	}

	// find by Name return list<Object>
	public List<StudentDTO> findByName(String sheetId, String name) throws IOException {
		ValueRange result = sheet.spreadsheets().values().get(sheetId, range).execute();
		List<List<Object>> valueList = result.getValues();
		List<StudentDTO> nameList = new ArrayList<StudentDTO>();

		if (valueList != null && valueList.size() != 0) {
			for (List<Object> list : valueList) {
				if (list.get(0).equals(name)) {
					StudentDTO dto = new StudentDTO();
					dto.setStudentName((String) list.get(0));
					dto.setEmail((String) list.get(1));
					dto.setContactNumber((String) list.get(2));
					dto.setAddress((String) list.get(3));
					nameList.add(dto);
				} else {
					System.out.println("Name not present");
				}
			}

		}
		return nameList;
	}

	// Find By Address return List<Object>
	public List<StudentDTO> findByAddress(String sheetId, String address) throws IOException {
		ValueRange result = sheet.spreadsheets().values().get(sheetId, range).execute();
		List<List<Object>> valueList = result.getValues();
		List<StudentDTO> addressList = new ArrayList<StudentDTO>();

		if (valueList != null && valueList.size() != 0) {
			for (List<Object> list : valueList) {
				if (list.get(3).equals(address)) {
					StudentDTO dto = new StudentDTO();
					dto.setStudentName((String) list.get(0));
					dto.setEmail((String) list.get(1));
					dto.setContactNumber((String) list.get(2));
					dto.setAddress((String) list.get(3));
					addressList.add(dto);
				}
			}
		}
		return addressList;
	}

	// Find By Email it returns DTO matching with Email
	public StudentDTO findByEmail(String sheetId, String email) throws IOException {
		ValueRange result = sheet.spreadsheets().values().get(sheetId, range).execute();
		List<List<Object>> valueList = result.getValues();
		StudentDTO dto = new StudentDTO();
		if (valueList != null && valueList.size() != 0) {
			for (List<Object> list : valueList) {
				if (list.get(1).equals(email)) {
					dto.setStudentName((String) list.get(0));
					dto.setEmail((String) list.get(1));
					dto.setContactNumber((String) list.get(2));
					dto.setAddress((String) list.get(3));
				}
			}
		}
		return dto;
	}

	// Find by Mobile number it return DTO matching with Mobile number
	public StudentDTO findByMobileNumber(String sheetId, String mobileNumber) throws IOException {
		ValueRange result = sheet.spreadsheets().values().get(sheetId, range).execute();
		List<List<Object>> valueList = result.getValues();
		StudentDTO dto = new StudentDTO();
		if (valueList != null && valueList.size() != 0) {
			for (List<Object> list : valueList) {
				if (list.get(2).equals(mobileNumber)) {
					dto.setStudentName((String) list.get(0));
					dto.setEmail((String) list.get(1));
					dto.setContactNumber((String) list.get(2));
					dto.setAddress((String) list.get(3));
				} else {
					System.out.println("Mobile number Not found");
				}
			}
		}
		return dto;
	}

	// updateDisableByEmail
	public String updateDisableByEmail(String sheetId, String email) throws IOException {
		range = "A2:E";
		ValueRange result = sheet.spreadsheets().values().get(sheetId, range).execute();
		List<List<Object>> valueList = result.getValues();
		int updateIndex = 2;
		if (valueList != null && valueList.size() != 0) {
			for (List<Object> list : valueList) {
				String updateRange = GoogleSheetUtil.getColumnLetter(5) + updateIndex;
				if (list.get(1).equals(email)) {
					Object disableStatus = list.get(4);
					if (!disableStatus.equals("TRUE")) {
						disableStatus = "TRUE";
						List<Object> objlist = new ArrayList<Object>();
						objlist.add(disableStatus);
						List<List<Object>> updateList = new ArrayList<>();
						updateList.add(objlist);
						ValueRange body = new ValueRange().setValues(updateList);
						UpdateValuesResponse updateResponse = sheet.spreadsheets().values()
								.update(sheetId, updateRange, body).setValueInputOption("Raw").execute();
					} else {
						return "Account Already disabled";
					}
				}
				updateIndex++;
			}
		} else {
			return "Data not Exists";
		}
		return "Successfully Disabled";
	}

	// find By active students
	public List<StudentDTO> findByEnabled(String sheetId) throws IOException {
		ValueRange result = sheet.spreadsheets().values().get(sheetId, range).execute();
		List<List<Object>> valueList = result.getValues();
		List<StudentDTO> activeStudent = new ArrayList<StudentDTO>();
		for (List<Object> list : valueList) {
			String disableStatus = (String) list.get(4);
			Boolean activeStatus = Boolean.valueOf(disableStatus);
			if (!activeStatus.equals(true)) {
				StudentDTO dto = new StudentDTO();
				dto.setStudentName((String) list.get(0));
				dto.setEmail((String) list.get(1));
				dto.setContactNumber((String) list.get(2));
				dto.setAddress((String) list.get(3));
				activeStudent.add(dto);
			} else {
				System.out.println("account disabled");
			}
		}
		return activeStudent;
	}

}
