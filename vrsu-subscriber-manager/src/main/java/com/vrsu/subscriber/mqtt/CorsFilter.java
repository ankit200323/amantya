package com.vrsu.subscriber.mqtt;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;

@Component
public class CorsFilter {

	private final List<String> allowedOrigins = Arrays.asList("http://localhost:4200");

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletResponse res = (HttpServletResponse) response;
		HttpServletRequest req = (HttpServletRequest) request;

		String origin = req.getHeader("Origin");

		res.setHeader("Access-Control-Allow-Origin", allowedOrigins.contains(origin) ? origin : "*");
		res.setHeader("Access-Control-Allow-Credentials", "true");
		res.setHeader("Vary", "Origin");
		res.setHeader("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT, OPTIONS");
		res.setHeader("Access-Control-Allow-Headers", "*");

		if (req.getMethod().equals("OPTIONS")) {
			res.setHeader("Access-Control-Max-Age", "86400");
			res.setStatus(HttpServletResponse.SC_OK);
			return;
		}

		chain.doFilter(request, response);
	}
}