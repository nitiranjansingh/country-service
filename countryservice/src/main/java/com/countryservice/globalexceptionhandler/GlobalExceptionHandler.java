package com.countryservice.globalexceptionhandler;

import java.io.IOException;
import java.net.URISyntaxException;

import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.security.SignatureException;

@RestControllerAdvice
public class GlobalExceptionHandler {
	
	@ExceptionHandler(Exception.class)
    public ProblemDetail handleSecurityException(Exception ex) {
		
        ProblemDetail errorDetail = null;
        
        if (ex instanceof BadCredentialsException) {
            errorDetail = ProblemDetail
                    .forStatusAndDetail(HttpStatusCode.valueOf(401), ex.getMessage());
            errorDetail.setProperty("access_denied_reason", "Authentication Failure");
        }

        if (ex instanceof AccessDeniedException) {
            errorDetail = ProblemDetail
                    .forStatusAndDetail(HttpStatusCode.valueOf(403), ex.getMessage());
            errorDetail.setProperty("access_denied_reason", "not_authorized!");

        }

        if (ex instanceof SignatureException) {
            errorDetail = ProblemDetail
                    .forStatusAndDetail(HttpStatusCode.valueOf(403), ex.getMessage());
            errorDetail.setProperty("access_denied_reason", "JWT Signature not valid");
        }
        
        if (ex instanceof ExpiredJwtException) {
            errorDetail = ProblemDetail
                    .forStatusAndDetail(HttpStatusCode.valueOf(403), ex.getMessage());
            errorDetail.setProperty("access_denied_reason", "JWT Token already expired !");
        }
        
        if (ex instanceof URISyntaxException) {
        	System.out.println(ex);
            errorDetail = ProblemDetail
                    .forStatusAndDetail(HttpStatusCode.valueOf(403), ex.getMessage());
            errorDetail.setProperty("Error_reason", "please check the syntax !");
        }
        
        if (ex instanceof IOException) {
            errorDetail = ProblemDetail
                    .forStatusAndDetail(HttpStatusCode.valueOf(403), ex.getMessage());
            errorDetail.setProperty("Error_reason", "Some IO Exception happen !");
        }
        
        if (ex instanceof InterruptedException) {
            errorDetail = ProblemDetail
                    .forStatusAndDetail(HttpStatusCode.valueOf(403), ex.getMessage());
            errorDetail.setProperty("Error_reason", "Http call got Interrupted !");
        }
        
        if (ex instanceof IllegalArgumentException) {
            errorDetail = ProblemDetail
                    .forStatusAndDetail(HttpStatusCode.valueOf(403), ex.getMessage());
            errorDetail.setProperty("Error_reason", "please Check the url !");
        }
        
        if (ex instanceof MissingServletRequestParameterException) {
            errorDetail = ProblemDetail
                    .forStatusAndDetail(HttpStatusCode.valueOf(403), ex.getMessage());
            errorDetail.setProperty("Error_reason", "Required request parameter is not present !");
        }
        
        else{
        	errorDetail = ProblemDetail
                    .forStatusAndDetail(HttpStatusCode.valueOf(403), ex.getMessage());
            errorDetail.setProperty("Error_reason", "Something unexpected happen !");
        }

        
        
        return errorDetail;
    }

}
