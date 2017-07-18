package tblucene2014;

import java.io.File;
import java.io.Serializable;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.apache.log4j.Logger;

import luceneutil.MyLuncene;
import luceneutil.QueryFactory;

/**
 * @author ThoaiNH 
 * create Jul 7, 2016
 * controller  for all function
 */
@ManagedBean(name = "myBean")
@ViewScoped
public class MyBean implements Serializable {
	private static Logger log = Logger.getLogger(MyBean.class);
	private String indexPath = "D:/learning/HK2/IR/seminar/LSI/DEMO/lucene/index2";
	private String docs;//input documents to index (JSON - String )
	private String txtSearch;
	private String txtResult;
	
	/**
	 * create index
	 */
	public void actionIndex() {
		try {
			MyLuncene luncene = new MyLuncene(indexPath);
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
				luncene.add(text, id);
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
		try {
			File directory = new File("D:\\learning\\HK2\\IR\\seminar\\LSI\\DEMO\\lucene\\index");
			deleteDirectory(directory);
			this.docs = null;
			this.txtResult = null;
			this.txtSearch = null;
		} catch (Exception e) {

			e.printStackTrace();

		}
	}

	/**
	 * retrieval docs
	 */
	public void doSearch() {
		try {
			MyLuncene luncene = new MyLuncene(indexPath);
			List<String> result = luncene.search(QueryFactory.create("TEXT", txtSearch), 100);
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

	public String getIndexPath() {
		return indexPath;
	}

	public void setIndexPath(String indexPath) {
		this.indexPath = indexPath;
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
