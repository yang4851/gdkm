package com.insilicogen.exception;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.DispatcherServlet;

public class DispatcherServletDelegate extends DispatcherServlet{

	private static final long serialVersionUID = -4602406467004720944L;
	
	@Override
	protected void doOptions(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		super.doOptions(request, response);
		
		if(response.containsHeader("Allow")) {
			response.setHeader("Allow", "");
		}
	}
}
