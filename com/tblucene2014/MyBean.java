package tblucene2014;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import org.apache.log4j.Logger;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.Query;
import org.apache.lucene.util.Version;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

import luceneutil.ApplicationConstant;
import luceneutil.ApplicationUtils;
import luceneutil.ConceptDoc;
import luceneutil.MyESA;
import luceneutil.MyLuncene;

/**
 * @author ThoaiNH create Jul 7, 2016 controller for all function
 */
@ManagedBean(name = "myBean")
@ViewScoped
public class MyBean implements Serializable {
	private static Logger log = Logger.getLogger(MyBean.class);
	private String txtSearch;
	private String txtResult;
	private MyESA myESA = new MyESA();
	private org.json.JSONArray jsonArrayDocs;
	public void handleFileUpload(FileUploadEvent event) {
        try {
			InputStream is = event.getFile().getInputstream();
			try {
				log.info(event.getFile().getFileName());
				StringBuilder docs = new StringBuilder();
				String str = null;
				BufferedReader reader = new BufferedReader(new InputStreamReader(is));
				if (reader != null) {
					while ((str = reader.readLine()) != null)
						docs.append(str);
				}				
				jsonArrayDocs = new org.json.JSONArray(docs.toString().trim());
				ApplicationUtils.showGrowlMessage("Loaded succesfully " + jsonArrayDocs.length() + " docs");
			} catch (Exception e) {
				ApplicationUtils.showGrowlMessage("Documents file must be in JSON array format");
				e.printStackTrace();
			}
			finally {
				try {
					is.close();
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			ApplicationUtils.showGrowlMessage("Please select documents file");
			e1.printStackTrace();
		}
    } 
	/**
	 * create index BOW and SESA
	 */
	public void actionIndex(ActionEvent actionEvent) {
		try {
			try {
				if(jsonArrayDocs == null || jsonArrayDocs.length() == 0)
					ApplicationUtils.showGrowlMessage("Please select documents file");
				else {
					File directory = new File(ApplicationConstant.INDEX_PATH);
					deleteDirectory(directory);
					Thread.sleep(1000);
					MyLuncene luncene = new MyLuncene(ApplicationConstant.INDEX_PATH);
					luncene.createIndex();
					luncene.createIndexWriter();
					/**
					 * add data to index.
					 */
					for (int i = 0; i < jsonArrayDocs.length(); i++) {
						String text = null;
						if (ApplicationConstant.TOKENIZED_WIKI_CONTENT)
							text = ApplicationUtils
									.getTokenString(jsonArrayDocs.getString(i));
						else
							text = jsonArrayDocs.getString(i);
						log.info("---indexing:" + text);
						log.info("--text length:"+text.length());
						luncene.add(text, i + 1);// BOW index
						myESA.addToIndex(text, i + 1);// ESA index limit 100 concepts
					}
					luncene.closeIndexWriter();
					ApplicationUtils.showGrowlMessage("Indexed successfully for " + jsonArrayDocs.length() + " docs");
				}
			} catch (Exception e) {
				ApplicationUtils.showGrowlMessage("Index error");
				e.printStackTrace();
			}
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			ApplicationUtils.showGrowlMessage("Please select documents file");
			e1.printStackTrace();
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
					}
					else {
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
	public void removeIndex(ActionEvent actionEvent) {
		log.info("---removeIndex");
		try {
			File directory = new File(ApplicationConstant.INDEX_PATH);
			deleteDirectory(directory);
			this.txtResult = null;
			this.txtSearch = null;
			ApplicationUtils.showGrowlMessage("Removed successfully");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * retrieval BOW
	 */
	public void doSearch(ActionEvent actionEvent) {
		log.info("---doSearch:" + txtSearch);
		String queryText = null;
		if (ApplicationConstant.TOKENIZED_WIKI_CONTENT)
			queryText = ApplicationUtils.getTokenString(txtSearch);
		else
			queryText = txtSearch;
		try {
			MyLuncene luncene = new MyLuncene(ApplicationConstant.INDEX_PATH);
			String query = "TEXT" + ":(" + queryText + ")";
			QueryParser queryParser = new QueryParser(Version.LUCENE_48, null,
					new StandardAnalyzer(Version.LUCENE_48));
			Query q = queryParser.parse(query);
			List<ConceptDoc> result = luncene.search(q, ApplicationConstant.N);
			if(result.size() == 0)
				ApplicationUtils.showGrowlMessage("No result found");
			else {
				txtResult = "";
				for (ConceptDoc c : result) {
					txtResult += c.getText() + "; sim = " + c.getScore();
					txtResult += "\n";
					txtResult += "\n";
				}
				log.info(txtResult);
			}
		} catch (Exception e) {
			ApplicationUtils.showGrowlMessage("Index file is emply");
			e.printStackTrace();
		}
	}

	/**
	 * retrieval ESA
	 */
	public void doSearchESA(ActionEvent actionEvent) {
		log.info("---doSearchESA:" + txtSearch);
		String queryText = null;
		if (ApplicationConstant.TOKENIZED_WIKI_CONTENT)
			queryText = ApplicationUtils.getTokenString(txtSearch);
		else
			queryText = txtSearch;
		try {

			List<ConceptDoc> result = myESA.search(queryText);
			if(result.size() == 0)
				ApplicationUtils.showGrowlMessage("No result found");
			else {
				txtResult = "";
				for (ConceptDoc c : result) {
					txtResult += c.getText() + "; sim = " + c.getScore();
					txtResult += "\n";
				}
				log.info(txtResult);
			}
		} catch (Exception e) {
			ApplicationUtils.showGrowlMessage("Index file is emply");
			e.printStackTrace();
		}
	}

	/**
	 * retrieval ESA
	 */
	public void doSearchESAandBOW(ActionEvent actionEvent) {
		log.info("---doSearchESAandBOW:" + txtSearch);
		String queryText = null;
		if (ApplicationConstant.TOKENIZED_WIKI_CONTENT)
			queryText = ApplicationUtils.getTokenString(txtSearch);
		else
			queryText = txtSearch;
		try {
			List<ConceptDoc> resultESA = myESA.search(queryText);
			/*
			 * search BOW
			 */
			MyLuncene luncene = new MyLuncene(ApplicationConstant.INDEX_PATH);
			String query = "TEXT" + ":(" + queryText + ")";
			QueryParser queryParser = new QueryParser(Version.LUCENE_48, null,
					new StandardAnalyzer(Version.LUCENE_48));
			Query q = queryParser.parse(query);
			List<ConceptDoc> resultBOW = luncene
					.search(q, ApplicationConstant.N);
			/*
			 * fusion result
			 */
			List<ConceptDoc> result = new ArrayList<ConceptDoc>();
			for (ConceptDoc cESA : resultESA) {
				int added = 0;
				for (ConceptDoc cBOW : resultBOW) {
					if (cESA.getId() == cBOW.getId()) {
						System.out.println(cESA.getScore() + ","
								+ cBOW.getScore());
						cESA.setScore(ApplicationConstant.W * cESA.getScore()
								+ (1 - ApplicationConstant.W)
										* cBOW.getScore());
						result.add(cESA);
						added = 1;
						break;
					}
				}
				if (added == 0)
					result.add(cESA);
			}
			for (ConceptDoc cBOW : resultBOW) {
				int added = 0;
				for (ConceptDoc cResult : result) {
					if (cBOW.getId() == cResult.getId()) {
						added = 1;
						break;
					}
				}
				if (added == 0)
					result.add(cBOW);
			}
			/*
			 * show result
			 */
			if(result.size() == 0)
				ApplicationUtils.showGrowlMessage("No result found");
			else {
				txtResult = "";
				for (ConceptDoc c : result) {
					txtResult += c.getText() + "; sim = " + c.getScore();
					txtResult += "\n";
				}
				log.info(txtResult);
			}
		} catch (Exception e) {
			ApplicationUtils.showGrowlMessage("Index file is emply");
			e.printStackTrace();
		}
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
