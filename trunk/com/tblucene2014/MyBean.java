package tblucene2014;

import java.io.File;
import java.io.Serializable;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import luceneutil.M150Luncene;
import luceneutil.QueryFactory;

/**
 * @author ThoaiNH create Jul 7, 2016
 */
@ManagedBean(name = "myBean")
@ViewScoped
public class MyBean implements Serializable {
	private String indexPath = "D:/learning/HK2/IR/seminar/LSI/DEMO/lucene/index";
	private String docs;
	private String txtSearch;
	private String txtResult;

	public void actionIndex() {
		try {
			M150Luncene luncene = new M150Luncene(indexPath);
			luncene.createIndex();
			luncene.createIndexWriter();
			System.out.println(docs);
			org.json.JSONArray jsonArray = new org.json.JSONArray(docs.trim());
			// myWriterManager.luceneUtil.createIndexWriter();
			/**
			 * add data to index.
			 */
			for (int i = 0; i < jsonArray.length(); i++) {
				String text = jsonArray.getJSONObject(i).getString("contentSegment");
				int id = jsonArray.getJSONObject(i).getInt("id");
				System.out.println("---indexing:" + text);
				luncene.add(text, id);
			}
			luncene.closeIndexWriter();

		} catch (Exception e) {
			e.printStackTrace(); // To change body of catch statement use File |
									// Settings | File Templates.
		}
	}

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

	public void doSearch() {
		try {
			M150Luncene luncene = new M150Luncene(indexPath);
			List<String> result = luncene.search(QueryFactory.create("TEXT", txtSearch), 100);
			txtResult = "";
			for (String s : result) {
				txtResult += s;
				txtResult += "\n";
			}
			System.out.println(txtResult);
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
