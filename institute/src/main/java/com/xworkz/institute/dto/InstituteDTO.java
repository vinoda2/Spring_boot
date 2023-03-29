package com.xworkz.institute.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import lombok.Data;

@Data
public class InstituteDTO {
	private int id;
	@NotBlank
	@NotEmpty
	@Size(min=3,message="Institute name should be more then 3 letters")
	@Pattern(regexp = "[^0-9]*", message = "must not contain special characters")
	private String instituteName;
	@NotBlank
	@NotEmpty
	@Email
	private String email;
	private String contactNumber;
	@NotBlank
	@NotEmpty
	@Size(min=8,max=16,message="password should be more then 8 letters and less then 16 letters")
	private String password;
	@NotBlank
	@NotEmpty
	@Size(min=8,max=16,message="password should be more then 8 letters and less then 16 letters")
	private String confirmPassword;
}
