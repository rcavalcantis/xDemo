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
			insert(new Bookmark("Apple",3233,"Vale do Silício", "http://www.apple.com"));
			insert(new Bookmark("IBM",8903,"Hilston", "http://www.ibm.com"));
			insert(new Bookmark("AOL",6340,"Chicago", "http://www.aol.com"));
			insert(new Bookmark("Foursquare",350,"Detroit", "http://www.foursquare.com"));
			insert(new Bookmark("Twitter", 1403, "Boston", "http://www.twitter.com"));
			insert(new Bookmark("Google Inc", 7930, "California", "http://www.google.com"));
			insert(new Bookmark("Microsoft", 25434,"Michigan", "http://www.microsoft.com"));
			insert(new Bookmark("Amazon",4565,"Nova York", "http://www.amazon.com"));
			insert(new Bookmark("Dell", 3454, "Arizona", "http://www.dell.com"));
			insert(new Bookmark("Facebook", 3434, "California", "http://www.facebook.com"));
			insert(new Bookmark("Alibaba", 24344, "Hong Kong", "http://www.alibaba.com"));
			insert(new Bookmark("Sales Force", 1233, "Paris", "http://www.salesforce.com"));
		}
	}

	public List<Bookmark> find(String filter) {
		return getDelegate().find(filter);
	}
}
