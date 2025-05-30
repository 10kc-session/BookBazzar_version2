package com.example.demo.filters;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.demo.Services.CustomUserDetailService;
import com.example.demo.utility.JwtToken;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtFilter extends OncePerRequestFilter{
	
	@Autowired
	JwtToken jwt;
	
	@Autowired
	CustomUserDetailService cds;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		String token=null;
		String userName = null;
		
		String authHead = request.getHeader("Authorization");
		
		if(authHead!=null && authHead.startsWith("Bearer ")) {
			token = authHead.substring(7);
			userName = jwt.extractUserName(token);
			System.out.print(jwt.extractUserName(token));
		}
		
		if(userName!=null && SecurityContextHolder.getContext().getAuthentication()==null) {
			
			UserDetails usd = cds.loadUserByUsername(userName);
			
			if(jwt.validate(userName,usd,token)) {
				UsernamePasswordAuthenticationToken upa = new UsernamePasswordAuthenticationToken(usd.getUsername(),usd.getPassword(),usd.getAuthorities());
				upa.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				SecurityContextHolder.getContext().setAuthentication(upa);
			}
		}
		
		System.out.println("Incoming request to: " + request.getRequestURI());

		filterChain.doFilter(request, response);
		
	}

}
