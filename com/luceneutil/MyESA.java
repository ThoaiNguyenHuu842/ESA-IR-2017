package luceneutil;

import static org.apache.lucene.util.Version.LUCENE_48;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.util.CharArraySet;
import org.apache.lucene.queryparser.classic.ParseException;

import be.vanoosten.esa.EnwikiFactory;
import be.vanoosten.esa.WikiAnalyzer;
import be.vanoosten.esa.WikiFactory;
import be.vanoosten.esa.tools.ConceptVector;
import be.vanoosten.esa.tools.Vectorizer;
import tblucene2014.MyBean;

/**
 * @author ThoaiNH
 * create Jul 18, 2017
 */
public class MyESA implements Serializable{
	private static Logger log = Logger.getLogger(MyESA.class);
	//conept index
	public static List<ConceptDoc> conceptIndex = new ArrayList<ConceptDoc>();
	private Vectorizer vectorizer;
	/**
	 * 
	 */
	public MyESA() {
		// TODO Auto-generated constructor stub
		WikiFactory factory = new EnwikiFactory();
        File indexPath = factory.getIndexRootPath();
        CharArraySet stopWords = factory.getStopWords();
        Analyzer analyzer = new WikiAnalyzer(LUCENE_48, stopWords);
		try {
			vectorizer = new Vectorizer(indexPath, analyzer);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * @param text
	 */
	public void addToIndex(String text, int id){
		try {
			ConceptVector conceptVector = vectorizer.vectorize(text);
			ConceptDoc conceptDoc = new ConceptDoc(text, conceptVector, 0, id);
			log.info(conceptVector.topConcepts(10));
			conceptIndex.add(conceptDoc);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * @param text
	 * @return
	 */
	public List<ConceptDoc> search(String text){
		List<ConceptDoc> result = new ArrayList<ConceptDoc>();
		try {
			ConceptVector conceptQuery = vectorizer.vectorize(text);
			for(ConceptDoc conceptDoc: conceptIndex){
				float score = conceptDoc.getC().dotProduct(conceptQuery);
				if(score > 0){
					conceptDoc.setScore(score);
					result.add(conceptDoc);
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		Collections.sort(result);
		return result;
	}
	public void deleteAll(){
		conceptIndex.clear();
	}
	public Vectorizer getVectorizer() {
		return vectorizer;
	}
	public void setVectorizer(Vectorizer vectorizer) {
		this.vectorizer = vectorizer;
	}
	
}
