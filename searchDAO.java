package Solr.SolrSp;

import java.io.IOException;
import java.util.List;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.params.SolrParams;

import Solr.SolrSp.Search;

public class searchDAO {

	
	public List<Search> findSearch(String Searchvalue) 
	{
		
		List<Search> searchList = null;
		Search search = null;
		SolrClient client = new HttpSolrClient.Builder("http://localhost:8983/solr/movies").build();

		SolrQuery query = new SolrQuery();
		query.setQuery("writer_name:*"+Searchvalue+"*"+" "+"OR"+" "+"lyric_title:*"+Searchvalue+"*" +" "+"OR"+" "+"movie_name:*"+Searchvalue+"*");
		//query.addFilterQuery("writer_name:*an*","lyric_title:*dhoo*");
		query.setFields("id", "writer_name", "movie_name", "lyric_content", "lyric_title");
		query.setStart(0);
		//query.setRows(50);  //to specify max number of rows to be retrieved
		//query.setTermsMaxCount(25);
		query.set("defType", "edismax");

		QueryResponse response = null;
		try {
			response = client.query(query);
		} catch (SolrServerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		SolrDocumentList documents = response.getResults();
		//assertEquals(NUM_INDEXED_DOCUMENTS, documents.getNumFound());
		for(SolrDocument document : documents) {
			search = new Search();
		 search.setMovie_id((int) document.getFieldValue("movie_id")) ;
		 search.setWriter_name((String) document.getFieldValue("writer_name")) ;
		 search.setLyric_title((String) document.getFieldValue("lyric_title")) ;
		 search.setMovie_name((String) document.getFieldValue("movie_name")) ;
		 searchList.add(search);
		  //assertTrue(document.getFieldNames().contains("name"));
		}
	
	return searchList;
}}
