package com.xworkz.googlesheetconnection.controller;

import java.io.IOException;

import javax.websocket.server.PathParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.google.api.services.sheets.v4.model.AppendValuesResponse;
import com.google.api.services.sheets.v4.model.BatchUpdateSpreadsheetResponse;
import com.google.api.services.sheets.v4.model.UpdateValuesResponse;
import com.google.api.services.sheets.v4.model.ValueRange;
import com.xworkz.googlesheetconnection.dto.StudentDTO;
import com.xworkz.googlesheetconnection.service.SheetService;

@RestController
@RequestMapping("/")
public class SheetController {
	@Autowired
	SheetService sheetService;

	//writing data to Google sheet
	@PostMapping("/save/{sheetId}")
	public AppendValuesResponse onSave(@RequestBody StudentDTO dto,@PathVariable String sheetId) throws IOException {
		System.err.println("this is save method");
		return this.sheetService.writeData(sheetId,dto);
	}
	
	//reading data from Google sheet
	@PostMapping("/getdata/{sheetId}")
	public Object getData(@PathVariable String sheetId) throws IOException {
		return this.sheetService.getValues(sheetId);
	}
	
	//updating the sheet title
	@PostMapping("/update/{sheetId}")
	public BatchUpdateSpreadsheetResponse onUpdate(@PathVariable String sheetId,@RequestBody String title) throws IOException {
		return this.sheetService.update(sheetId,title);
	}
	
	@PostMapping("/updatedto/{sheetId}/{range}")
	public UpdateValuesResponse update(@RequestBody StudentDTO dto,@PathVariable String sheetId,@PathVariable String range) {
		return this.sheetService.updateData(sheetId, dto,range);
	}
	
	@PostMapping("/updatebyname/{sheetId}/{name}")
	public BatchUpdateSpreadsheetResponse updateByName(@PathVariable String sheetId,@PathVariable String name,@RequestBody String replacement) throws IOException {
		return this.sheetService.updateByName(sheetId, name, replacement);
	}
	
	
	@PostMapping("/delete/{sheetId}/{range}")
	public String onDelete(@PathVariable String sheetId,@PathVariable String range) throws IOException {
		return this.sheetService.delete(sheetId,range);
	}
}
