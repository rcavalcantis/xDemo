package demoiselle.xDemo.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LoginServlet  extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		try {
			
			if (request.getSession().getAttribute("imagem") != null && request.getSession().getAttribute("imagem") instanceof byte[]) {
				
				byte[] dados = (byte[]) request.getSession().getAttribute("imagem");
				if (null != dados) {
					response.reset();
					response.setContentType(request.getSession().getAttribute("contentType") + "");
	
					response.getOutputStream().write(dados);
					response.getOutputStream().flush();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
