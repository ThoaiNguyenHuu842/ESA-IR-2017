package test;

import static org.apache.lucene.util.Version.LUCENE_48;

import java.io.File;
import java.io.IOException;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.util.CharArraySet;

import be.vanoosten.esa.EnwikiFactory;
import be.vanoosten.esa.WikiAnalyzer;
import be.vanoosten.esa.WikiFactory;
import be.vanoosten.esa.tools.ConceptVector;
import be.vanoosten.esa.tools.SemanticSimilarityTool;
import be.vanoosten.esa.tools.Vectorizer;
import luceneutil.ApplicationUtils;
import vn.hus.nlp.tokenizer.VietTokenizer;

public class Test {
	private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(Test.class);
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
		VietTokenizer vietTokenizer = new VietTokenizer();
		String s = "Lê Thánh Tông được xem là một vị hoàng đế anh minh thời Hậu Lê";
		System.out.println(ApplicationUtils.getTokenString(s));
		log.info("---xong---");
	}

}
