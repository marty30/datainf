package coreservlets;

/**
 * A specialization of the CatalogPage servlet that displays a page selling
 * three famous kids-book series. Orders are sent to the OrderPage servlet.
 * <P>
 * Taken from Core Servlets and JavaServer Pages 2nd Edition from Prentice Hall
 * and Sun Microsystems Press, http://www.coreservlets.com/. &copy; 2003 Marty
 * Hall; may be freely used or adapted.
 */

public class KidsBooksPage extends CatalogPage {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public KidsBooksPage(){
		init();
	}
	
	public void init() {
		String[] ids = { "lewis001", "alexander001", "rowling001" };
		setItems(ids);
		setTitle("All-Time Best Children's Fantasy Books");
	}
	
	public String getTitle(){
		return super.getTitle();
	}
	
	public String getPage() {
		return super.getPage();
	}
}
