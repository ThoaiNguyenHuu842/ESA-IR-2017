package test;

import static org.apache.lucene.util.Version.LUCENE_48;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;

import org.apache.log4j.Logger;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.util.CharArraySet;

import be.vanoosten.esa.EnwikiFactory;
import be.vanoosten.esa.WikiAnalyzer;
import be.vanoosten.esa.WikiFactory;
import be.vanoosten.esa.tools.ConceptVector;
import be.vanoosten.esa.tools.SemanticSimilarityTool;
import be.vanoosten.esa.tools.Vectorizer;

public class MainAnalyst {
	private static Logger log = Logger.getLogger(MainAnalyst.class);
	public static void main(String[] args) {
		try {
			WikiFactory factory = new EnwikiFactory();
	        File indexPath = factory.getIndexRootPath();
	        CharArraySet stopWords = factory.getStopWords();
	        Analyzer analyzer = new WikiAnalyzer(LUCENE_48, stopWords);
			Vectorizer vectorizer = new Vectorizer(indexPath, analyzer);

			SemanticSimilarityTool semanticSimilarityTool = new SemanticSimilarityTool(vectorizer);
			/*
			 * text similarity 
			 */
			//log.info(semanticSimilarityTool.findSemanticSimilarity("pretty", "beautiful"));
			/*log.info(semanticSimilarityTool.findSemanticSimilarity("bạc liêu", "mỹ tho"));
			log.info(semanticSimilarityTool.findSemanticSimilarity("bạc liêu", "cà mau"));
			log.info(semanticSimilarityTool.findSemanticSimilarity("bạc liêu", "minh hải"));
			log.info(semanticSimilarityTool.findSemanticSimilarity("lê trọng tấn", "hoàng minh thảo"));
			log.info(semanticSimilarityTool.findSemanticSimilarity("lê trọng tấn", "trận đồng xoài"));*/
			ConceptVector conceptVector = vectorizer.vectorize("linkin park");
			log.info(conceptVector.topConcepts(10));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
