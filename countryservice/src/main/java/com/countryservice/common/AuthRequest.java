package com.countryservice.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthRequest {

    private String username ;
    private String password;
    
	public String getUsername() {
		// TODO Auto-generated method stub
		return this.username;
	}
	public String getPassword() {
		return this.password;
	}
}

