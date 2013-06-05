package a4;

import java.io.IOException;

import javax.faces.bean.SessionScoped;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import de.medieninf.webanw.sakila.Customer;
import de.medieninf.webanw.sakila.SakilaBean;

/**
 * Servlet für den ersten Aufruf des Kundenportals
 * Leitet zum Login oder zur Filmübersicht weiter
 * Loggt den Kunden ein (Session), falls gefordert 
 * 
 * @author shard001
 */
@WebServlet("/Kunde")
@SessionScoped
public class Kunde extends HttpServlet {
	
	private static final long serialVersionUID = 3104145768909815921L;
	private SakilaBean sb = new SakilaBean();

	/**
	 * leitet den Besucher passend weiter, falls er eingeloggt ist
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		
		// noch nicht angemeldet?
		if (session.getAttribute("customerID") == null) {
			RequestDispatcher rd = request.getRequestDispatcher("/kundeLogin.jsp");
			rd.forward(request, response);
			return;
		} else if (session.getAttribute("customerID") != null) {
			RequestDispatcher rd = request.getRequestDispatcher("/kundeFilme.jsp");
			rd.forward(request, response);
			return;
		}
		
	}
	
	/**
	 * loggt den Kunden ein, falls Formular abgesendet wurde
	 * regelt ebenfalls den Logout
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		
		// nicht nicht angemeldet und soll einloggen?
		if (session.getAttribute("customerID") == null && request.getParameter("username") != "" && request.getParameter("username").equals(request.getParameter("password"))) {
			// suche customer anhand der kundennummer
			Customer c = sb.getCustomer(Integer.parseInt(request.getParameter("username")));
			
			// wenn ein Ergebnis gefunden wurde, ist der Login korrekt
			if (c != null) {
				session.setAttribute("customerID", c.getCustomerId());
				doGet(request, response);
			}
			
		} else if (session.getAttribute("customerID") != null && request.getParameter("logout") != null) {
			// abmelden?
			session.removeAttribute("customerID");
			doGet(request, response);
		}else {
			doGet(request, response);
		}
	}
}