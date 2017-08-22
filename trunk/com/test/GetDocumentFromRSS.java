package test;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.apache.lucene.queryparser.classic.QueryParser;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import com.google.gson.JsonArray;

public class GetDocumentFromRSS {
	private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(GetDocumentFromRSS.class);
	public static boolean deleteDirectory(File directory) {
	    if(directory.exists()){
	        File[] files = directory.listFiles();
	        if(null!=files){
	            for(int i=0; i<files.length; i++) {
	                if(files[i].isDirectory()) {
	                    deleteDirectory(files[i]);
	                }
	                else {
	                    files[i].delete();
	                }
	            }
	        }
	    }
	    return(directory.delete());
	}
	public static void main(String[] args) throws Exception {
		/*
		 * read rss
		 */
		RSS rss = new RSS("http://vnexpress.net/rss/so-hoa.rss");
		rss.run();
		log.info(rss.getListURL().size());
		/*
		 * add to JSON
		 */
		JsonArray jsonArray = new JsonArray();
		for(String s: rss.getListURL()){
			log.info("--connecting to:"+s);
			Document doc = Jsoup.connect(s).timeout(30000).get();
			String text = doc.select(".fck_detail").text().trim();
			//log.info(text);
			if(text != null && text.trim().length() > 0)
				jsonArray.add(QueryParser.escape(text.replaceAll("[^\\p{L}\\d\\s_]"," ")));
		}
		/*
		 * save to file
		 */
		BufferedWriter writer = null;
		try
		{
		    writer = new BufferedWriter( new FileWriter("D://learning//Master final project//doc-vnexpress//so-hoa.txt"));
		    writer.write(jsonArray.toString());

		}
		catch ( IOException e)
		{
			e.printStackTrace();
		}
		finally
		{
		    try
		    {
		        if ( writer != null)
		        writer.close( );
		    }
		    catch ( IOException e)
		    {
		    	e.printStackTrace();
		    }
		}
		log.info(jsonArray);
		log.info("---xong---");
	}

}
