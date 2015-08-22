package demoiselle.xDemo.business;

import java.util.List;

import demoiselle.xDemo.entity.Bookmark;
import demoiselle.xDemo.persistence.BookmarkDAO;
import br.gov.frameworkdemoiselle.lifecycle.Startup;
import br.gov.frameworkdemoiselle.stereotype.BusinessController;
import br.gov.frameworkdemoiselle.template.DelegateCrud;
import br.gov.frameworkdemoiselle.transaction.Transactional;

@BusinessController
public class BookmarkBC extends DelegateCrud<Bookmark, Long, BookmarkDAO> {

	private static final long serialVersionUID = 1L;

	@Startup
	@Transactional
	public void load() {
		if (findAll().isEmpty()) {
			insert(new Bookmark("Tim Cook",13233.5,"Apple", "http://www.apple.com"));
			insert(new Bookmark("Virginia Rometty",12545.76,"IBM", "http://www.ibm.com"));
			insert(new Bookmark("Tim Armstrong ",4546.56,"AOL", "http://www.aol.com"));
			insert(new Bookmark("Dennis Crowley",8979.45,"Foursquare", "http://www.foursquare.com"));
			insert(new Bookmark("Jack Dorsey", 2442.09, "Twitter", "http://www.twitter.com"));
			insert(new Bookmark("Larry Page", 42344.34, "Google", "http://www.google.com"));
			insert(new Bookmark("Steve Ballmer", 25434.97,"Microsoft", "http://www.microsoft.com"));
			insert(new Bookmark("Jeff Bezos",45652.00,"Amazon", "http://www.amazon.com"));
			insert(new Bookmark("Michael Dell", 34543.43, "DELL", "http://www.dell.com"));
			insert(new Bookmark("Mark Zuckerberg", 34345.74, "Facebook", "http://www.facebook.com"));
			insert(new Bookmark("Jack Ma", 24344.54, "Alibaba", "http://www.alibaba.com"));
			insert(new Bookmark("Marc Benioff", 12334.43, "Sales Force", "http://www.salesforce.com"));
		}
	}

	public List<Bookmark> find(String filter) {
		return getDelegate().find(filter);
	}
}
