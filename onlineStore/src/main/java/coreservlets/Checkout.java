package coreservlets;

import java.io.*;

import javax.servlet.*;
import javax.servlet.http.*;

import java.util.*;
import java.text.*;

/**
 * Shows all items currently in ShoppingCart. Clients have their own session
 * that keeps track of which ShoppingCart is theirs. If this is their first
 * visit to the order page, a new shopping cart is created. Usually, people come
 * to this page by way of a page showing catalog entries, so this page adds an
 * additional item to the shopping cart. But users can also bookmark this page,
 * access it from their history list, or be sent back to it by clicking on the
 * "Update Order" button after changing the number of items ordered.
 * <P>
 * Taken from Core Servlets and JavaServer Pages 2nd Edition from Prentice Hall
 * and Sun Microsystems Press, http://www.coreservlets.com/. &copy; 2003 Marty
 * Hall; may be freely used or adapted.
 */

public class Checkout extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String title;

	public Checkout() {
		setTitle("Bill");
	}

	private void setTitle(String title) {
		this.title = title;
	}

	public String getTitle() {
		return title;
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession();
		session.removeAttribute("shoppingCart");
		response.setContentType("text/html");
		response.getWriter().println(
				"<h1>You have paid " + request.getParameter("total_price")
						+ "</h1>");
		response.getWriter()
				.println(
						"<P>Click to go back to <A HREF=\"index.html\">the index page</P>");
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession();
		ShoppingCart cart;
		synchronized (session) {
			cart = (ShoppingCart) session.getAttribute("shoppingCart");
			// New visitors get a fresh shopping cart.
			// Previous visitors keep using their existing cart.
			if (cart == null) {
				cart = new ShoppingCart();
				session.setAttribute("shoppingCart", cart);
			}
			String itemID = request.getParameter("itemID");
			if (itemID != null) {
				String numItemsString = request.getParameter("numItems");
				if (numItemsString == null) {
					// If request specified an ID but no number,
					// then customers came here via an "Add Item to Cart"
					// button on a catalog page.
					cart.addItem(itemID);
				} else {
					// If request specified an ID and number, then
					// customers came here via an "Update Order" button
					// after changing the number of items in order.
					// Note that specifying a number of 0 results
					// in item being deleted from cart.
					int numItems;
					try {
						numItems = Integer.parseInt(numItemsString);
					} catch (NumberFormatException nfe) {
						numItems = 1;
					}
					cart.setNumOrdered(itemID, numItems);
				}
			}
		}
		// Whether or not the customer changed the order, show
		// order status.
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		String title = "Bill";
		String docType = "<!DOCTYPE HTML>\n";
		out.println(docType + "<HTML>\n" + "<HEAD><TITLE>" + title
				+ "</TITLE></HEAD>\n" + "<BODY BGCOLOR=\"#FDF5E6\">\n"
				+ "<H1 ALIGN=\"CENTER\">" + title + "</H1>");
		synchronized (session) {
			List<ItemOrder> itemsOrdered = cart.getItemsOrdered();
			if (itemsOrdered.size() == 0) {
				out.println("<H2><I>No items in your cart... This causes that you can't checkout</I></H2><P>Please go back to <A HREF=\"index.html\">the index page</P>");
			} else {
				// If there is at least one item in cart, show table
				// of items ordered.
				out.println("<TABLE BORDER=1 ALIGN=\"CENTER\">\n"
						+ "<TR BGCOLOR=\"#FFAD00\">\n"
						+ "  <TH>Item ID<TH>Description\n"
						+ "  <TH>Unit Cost<TH>Number<TH>Total Cost");
				ItemOrder order;
				// Rounds to two decimal places, inserts dollar
				// sign (or other currency symbol), etc., as
				// appropriate in current Locale.
				NumberFormat formatter = NumberFormat.getCurrencyInstance();
				// For each entry in shopping cart, make
				// table row showing ID, description, per-item
				// cost, number ordered, and total cost.
				// Put number ordered in textfield that user
				// can change, with "Update Order" button next
				// to it, which resubmits to this same page
				// but specifying a different number of items.
				double bill = 0;
				for (int i = 0; i < itemsOrdered.size(); i++) {
					order = itemsOrdered.get(i);
					out.println("<TR>\n" + "  <TD>" + order.getItemID() + "\n"
							+ "  <TD>" + order.getShortDescription() + "\n"
							+ "  <TD>" + formatter.format(order.getUnitCost())
							+ "\n" + "  <TD>" + order.getNumItems() + "  <TD>"
							+ formatter.format(order.getTotalCost()));
					bill += order.getTotalCost();
				}
				// Show total amount
				out.println("<TR><TD COLSPAN=\"5\"> Total: "
						+ formatter.format(bill) + "");
				// "Proceed to Checkout" button below table
				out.println("</TABLE>\n"
						+ "<FORM ACTION=\"#\" method=\"post\"\">\n"
						+ "<BIG><CENTER>\n"
						+ "<INPUT TYPE=\"SUBMIT\"\n"
						+ "       VALUE=\"Pay now!\">\n"
						+ "<INPUT TYPE=\"hidden\" name=\"total_price\" value=\""
						+ formatter.format(bill) + "\">"
						+ "</CENTER></BIG></FORM>");
			}
			out.println("</BODY></HTML>");
		}
	}

	public String getPage(HttpServletRequest request,
			HttpServletResponse response) {
		String res = "";
		if (request.getMethod().equals("POST")) {
			HttpSession session = request.getSession();
			session.removeAttribute("shoppingCart");
			res +=(
					"<h1>You have paid " + request.getParameter("total_price")
							+ "</h1>");
			res +=(
							"<P>Click to go back to <A HREF=\"index.html\">the index page</P>");
		} else {

			HttpSession session = request.getSession();
			ShoppingCart cart;
			synchronized (session) {
				cart = (ShoppingCart) session.getAttribute("shoppingCart");
				// New visitors get a fresh shopping cart.
				// Previous visitors keep using their existing cart.
				if (cart == null) {
					cart = new ShoppingCart();
					session.setAttribute("shoppingCart", cart);
				}
				String itemID = request.getParameter("itemID");
				if (itemID != null) {
					String numItemsString = request.getParameter("numItems");
					if (numItemsString == null) {
						// If request specified an ID but no number,
						// then customers came here via an "Add Item to Cart"
						// button on a catalog page.
						cart.addItem(itemID);
					} else {
						// If request specified an ID and number, then
						// customers came here via an "Update Order" button
						// after changing the number of items in order.
						// Note that specifying a number of 0 results
						// in item being deleted from cart.
						int numItems;
						try {
							numItems = Integer.parseInt(numItemsString);
						} catch (NumberFormatException nfe) {
							numItems = 1;
						}
						cart.setNumOrdered(itemID, numItems);
					}
				}
			}
			// Whether or not the customer changed the order, show
			// order status.
			synchronized (session) {
				List<ItemOrder> itemsOrdered = cart.getItemsOrdered();
				if (itemsOrdered.size() == 0) {
					res += ("<H2><I>No items in your cart... This causes that you can't checkout</I></H2><P>Please go back to <A HREF=\"index.html\">the index page</P>");
				} else {
					// If there is at least one item in cart, show table
					// of items ordered.
					res += ("<TABLE BORDER=1 ALIGN=\"CENTER\">\n"
							+ "<TR BGCOLOR=\"#FFAD00\">\n"
							+ "  <TH>Item ID<TH>Description\n"
							+ "  <TH>Unit Cost<TH>Number<TH>Total Cost");
					ItemOrder order;
					// Rounds to two decimal places, inserts dollar
					// sign (or other currency symbol), etc., as
					// appropriate in current Locale.
					NumberFormat formatter = NumberFormat.getCurrencyInstance();
					// For each entry in shopping cart, make
					// table row showing ID, description, per-item
					// cost, number ordered, and total cost.
					// Put number ordered in textfield that user
					// can change, with "Update Order" button next
					// to it, which resubmits to this same page
					// but specifying a different number of items.
					double bill = 0;
					for (int i = 0; i < itemsOrdered.size(); i++) {
						order = itemsOrdered.get(i);
						res += ("<TR>\n" + "  <TD>" + order.getItemID() + "\n"
								+ "  <TD>" + order.getShortDescription() + "\n"
								+ "  <TD>"
								+ formatter.format(order.getUnitCost()) + "\n"
								+ "  <TD>" + order.getNumItems() + "  <TD>" + formatter
								.format(order.getTotalCost()));
						bill += order.getTotalCost();
					}
					// Show total amount
					res += ("<TR><TD COLSPAN=\"5\"> Total: "
							+ formatter.format(bill) + "");
					// "Proceed to Checkout" button below table
					res += ("</TABLE>\n"
							+ "<FORM ACTION=\"#\" method=\"post\"\">\n"
							+ "<BIG><CENTER>\n"
							+ "<INPUT TYPE=\"SUBMIT\"\n"
							+ "       VALUE=\"Pay now!\">\n"
							+ "<INPUT TYPE=\"hidden\" name=\"total_price\" value=\""
							+ formatter.format(bill) + "\">" + "</CENTER></BIG></FORM>");
				}
			}
		}
		return res;
	}

}
