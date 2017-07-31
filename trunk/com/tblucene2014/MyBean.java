package tblucene2014;

import java.io.File;
import java.io.Serializable;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.apache.log4j.Logger;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.Query;
import org.apache.lucene.util.Version;

import luceneutil.ApplicationConstant;
import luceneutil.MyLuncene;

/**
 * @author ThoaiNH 
 * create Jul 7, 2016
 * controller  for all function
 */
@ManagedBean(name = "myBean")
@ViewScoped
public class MyBean implements Serializable {
	private static Logger log = Logger.getLogger(MyBean.class);
	private String docs;//input documents to index (JSON - String )
	private String txtSearch;
	private String txtResult;
	private MyESA myESA = new MyESA();
	/**
	 * create index
	 */
	public void actionIndex() {
		try {
			File directory = new File(ApplicationConstant.INDEX_PATH);
			deleteDirectory(directory);
			Thread.sleep(1000);
			MyLuncene luncene = new MyLuncene(ApplicationConstant.INDEX_PATH);
			luncene.createIndex();
			luncene.createIndexWriter();
			log.info(docs);
			org.json.JSONArray jsonArray = new org.json.JSONArray(docs.trim());
			// myWriterManager.luceneUtil.createIndexWriter();
			/**
			 * add data to index.
			 */
			for (int i = 0; i < jsonArray.length(); i++) {
				String text = jsonArray.getJSONObject(i).getString("contentSegment");
				int id = jsonArray.getJSONObject(i).getInt("id");
				log.info("---indexing:" + text);
				luncene.add(text, id);//BOW index
				myESA.addToIndex(text);//ESA index limit 100 concepts
			}
			luncene.closeIndexWriter();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * remove all file in directory
	 * @param directory
	 * @return
	 */
	public static boolean deleteDirectory(File directory) {
		log.info("---deleteDirectory");
		MyESA.conceptIndex.clear();
		if (directory.exists()) {
			File[] files = directory.listFiles();
			if (null != files) {
				for (int i = 0; i < files.length; i++) {
					if (files[i].isDirectory()) {
						deleteDirectory(files[i]);
					} else {
						files[i].delete();
					}
				}
			}
		}
		return (directory.delete());
	}

	/**
	 * remove index directory
	 */
	public void removeIndex() {
		log.info("---removeIndex");
		try {
			File directory = new File(ApplicationConstant.INDEX_PATH);
			deleteDirectory(directory);
			this.docs = null;
			this.txtResult = null;
			this.txtSearch = null;
		} catch (Exception e) {

			e.printStackTrace();

		}
	}

	/**
	 * retrieval docs BOW
	 */
	public void doSearch() {
		log.info("---doSearch:" + txtSearch);
		try {
			MyLuncene luncene = new MyLuncene(ApplicationConstant.INDEX_PATH);			
			String query = "TEXT" + ":(" + txtSearch + ")";
			QueryParser queryParser = new QueryParser(Version.LUCENE_48, null, new StandardAnalyzer(Version.LUCENE_48));
			Query q = queryParser.parse(query);
			List<String> result = luncene.search(q, 100);
			txtResult = "";
			for (String s : result) {
				txtResult += s;
				txtResult += "\n";
			}
			log.info(txtResult);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * retrieval docs ESA
	 */
	public void doSearchESA() {
		log.info("---doSearchESA:" + txtSearch);
		try {
			
			List<ConceptDoc> result = myESA.search(txtSearch);
			txtResult = "";
			for (ConceptDoc c : result) {
				txtResult += c.getText() + "; sim = " + c.getScore();
				txtResult += "\n";
			}
			log.info(txtResult);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public String getDocs() {
		return docs;
	}

	public void setDocs(String docs) {
		this.docs = docs;
	}

	public String getTxtSearch() {
		return txtSearch;
	}

	public void setTxtSearch(String txtSearch) {
		this.txtSearch = txtSearch;
	}

	public String getTxtResult() {
		return txtResult;
	}

	public void setTxtResult(String txtResult) {
		this.txtResult = txtResult;
	}

}
