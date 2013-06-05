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
import de.medieninf.webanw.sakila.Film;
import de.medieninf.webanw.sakila.Inventory;
import de.medieninf.webanw.sakila.SakilaBean;
import de.medieninf.webanw.sakila.Staff;

/**
 * Zeigt die Filmdetails sowie die Kopien des Films an
 * leitet den Besucher zum Login weiter, wenn er nicht angemeldet ist
 * 
 * @author shard001
 */
@WebServlet("/KundeFilmDetail")
@SessionScoped
public class KundeFilmDetail extends HttpServlet {
	
	private static final long serialVersionUID = -4108089349508449746L;
	private SakilaBean sb = new SakilaBean();
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		
		// noch nicht angemeldet?
		if (session.getAttribute("customerID") == null) {
			RequestDispatcher rd = request.getRequestDispatcher("/kundeLogin.jsp");
			rd.forward(request, response);
			return;
			
		} else if (session.getAttribute("customerID") != null) {
			
			if (request.getParameter("id") != null) {
				session.setAttribute("filmID", request.getParameter("id"));
			}
			
			RequestDispatcher rd = request.getRequestDispatcher("/kundeFilmDetail.jsp");
			rd.forward(request, response);
			return;
		}
	}
	
	/**
	 * leiht den gewünschten Film aus, wenn noch freie Kopien vorhanden sind
	 * der Kunde muss dazu angemeldet sein
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		
		// angemeldet?
		if (session.getAttribute("customerID") != null) {
			Customer customer = sb.getCustomer((Integer)session.getAttribute("customerID")); // aktueller kunde
			
			if (request.getParameter("ausleihen") != null && request.getParameter("rentFilmID") != null) {
				Staff staff = sb.getStaff(1); // TODO erstbester staff
				Film film = sb.getFilm(Integer.parseInt(request.getParameter("rentFilmID"))); // gewählter film
				
				// erstbeste freie kopie wählen
				Inventory inventory = null;
				for (Inventory i : film.getInventories()) {
					InventoryWrapper inv = new InventoryWrapper(i);
					if (!inv.isRented()) {
						inventory = i;
						break;
					}
				}
				
				/*
				 * nur, wenn freie kopie vorhanden
				 * ist via SakilaBean transaktional gesichert - deren Exception wird in der Bean gefangen
				 * 
				 * wenn zwischendurch alle Kopien weg sind, verschwindet der Ausleihen-Button und wird
				 * durch eine Meldung ersetzt, dass z.Z. alle Kopien ausgeliehen sind.
				 * -> Sollte als Fehlermeldung reichen
				 */
				if (inventory != null) {
					sb.rent(inventory, customer, staff); // ausleihen
				}
				
			}
			
			// wieder zurück zur detailansicht
			RequestDispatcher rd = request.getRequestDispatcher("/kundeFilmDetail.jsp");
			rd.forward(request, response);
			return;
		}
	}

}
