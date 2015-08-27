package demoiselle.xDemo.filter;

import java.io.IOException;
import java.security.Principal;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.frameworkdemoiselle.security.Credentials;
import br.gov.frameworkdemoiselle.util.Beans;

//@WebFilter("*")
public class LoginFilter { //implements Filter {
//
//	@Override
//	public void init(FilterConfig filterConfig) throws ServletException {
//		// TODO Auto-generated method stub
//	}
//
//	@Override
//	public void doFilter(ServletRequest request, ServletResponse response,
//			FilterChain chain) throws IOException, ServletException {
//
//		HttpServletResponse res = (HttpServletResponse) response;
//		HttpServletRequest req = (HttpServletRequest) request;
//
//		String principal =  (String) req.getSession().getAttribute("principal");
//		String token =  (String) req.getParameter("token");
//
//		String uri = req.getRequestURI();
//
//		Credentials credentials = Beans.getReference(Credentials.class);
//		
//		if (principal == null 
//				&& !uri.startsWith(req.getContextPath() + "/javax.faces.resource") 
//				&& !uri.startsWith(req.getContextPath() + "/ApresentarFiguraServlet.jsf") 
//				&& !uri.endsWith("/login.html" )
//				&& !uri.endsWith(".css" )
//				&& !uri.endsWith(".js" )
//				&& !uri.startsWith(req.getContextPath() + "/api")
//				) {
//
//	//		System.out.println("REDIRECT LOGIN: " + req.getRequestURI());
//			
////            res.sendRedirect("./login.html");
//			chain.doFilter(request, response);
//          
//		} else {
//			chain.doFilter(request, response);
//		}
//	}
//
//	
//	@Override
//	public void destroy() {
//
//	}
//
}
