package com.xworkz.googlesheetconnection.controller;

import java.io.IOException;

import javax.websocket.server.PathParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.api.services.sheets.v4.model.AppendValuesResponse;
import com.google.api.services.sheets.v4.model.UpdateValuesResponse;
import com.google.api.services.sheets.v4.model.ValueRange;
import com.xworkz.googlesheetconnection.service.SheetService;

@RestController
@RequestMapping("/")
public class SheetController {
	@Autowired
	SheetService sheetService;
	/*
	@GetMapping("/service")
	public String getService() {
		return "this is sheet controller";
	}
	
	@PostMapping("/newsheet/{sheetName}")
	public String getSheet(@PathVariable String sheetName) throws IOException {	
		return this.sheetService.createSheet(sheetName);
	}
	*/
	
	
	@PostMapping("/save/{sheetId}")
	public UpdateValuesResponse onSave(@PathVariable String sheetId) throws IOException {
		System.err.println("this is save method");
		return this.sheetService.writeData(sheetId);
	}
	
	@PostMapping("/getdata/{sheetId}")
	public Object getData(@PathVariable String sheetId) throws IOException {
		return this.sheetService.getValues(sheetId);
	}
	
	@PostMapping("/update")
	public UpdateValuesResponse onUpdate() throws IOException {
		return this.sheetService.update();
	}
	
	@PostMapping("/delete")
	public String onDelete() throws IOException {
		return this.sheetService.delete();
	}
}
