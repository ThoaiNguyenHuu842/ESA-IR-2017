package luceneutil;

import org.apache.log4j.Logger;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.util.Version;

/**
 * @author ThoaiNH
 * create 09/09/2014
 * query builder
 */
public class QueryFactory {
	private static Logger log = Logger.getLogger(QueryFactory.class);
	/**
	 * @param field
	 * @param value
	 * @return
	 */
	public static Query create(String field, String value) {
		try {
			//search by phone, name
			String query = field + ":"+ value + "";
			log.info(query);
			QueryParser queryParser = new QueryParser(Version.LUCENE_46, null,
					new StandardAnalyzer(Version.LUCENE_46));
			Query q;
			q = queryParser.parse(query);
			BooleanQuery qry = new BooleanQuery();
			qry.add(q, BooleanClause.Occur.SHOULD);
			return qry;
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
}
