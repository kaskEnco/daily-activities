package Solr.SolrSp;


import java.util.List;

import javax.transaction.Transactional;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import Solr.SolrSp.searchDAO;;

@RestController
public class SolrPra {

	@GetMapping(value = "/search/{Searchvalue}", produces = "application/json")
	@Transactional
	public List<Search> findSearch(@PathVariable String Searchvalue) {
		List<Search> searchOp = new searchDAO().findSearch(Searchvalue);
		return searchOp;
	}
	
	
}
