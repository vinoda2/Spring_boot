package com.xworkz.googlesheetconnection.dto;


public class StudentDTO {

	
	private String studentName;
	private String email;
	private String contactNumber;
	private String address;
	private Boolean disabled=false;
	
	public Boolean getDisabled() {
		return disabled;
	}
	public void setDisabled(Boolean disabled) {
		this.disabled = disabled;
	}
	public String getStudentName() {
		return studentName;
	}
	public void setStudentName(String studentName) {
		this.studentName = studentName;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getContactNumber() {
		return contactNumber;
	}
	public void setContactNumber(String contactNumber) {
		this.contactNumber = contactNumber;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	@Override
	public String toString() {
		return "StudentDTO [studentName=" + studentName + ", email=" + email + ", contactNumber=" + contactNumber
				+ ", address=" + address + "]";
	}
}
