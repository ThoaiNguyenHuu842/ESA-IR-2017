package be.vanoosten.esa;

import static org.apache.lucene.util.Version.LUCENE_48;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.util.CharArraySet;

import be.vanoosten.esa.tools.ConceptVector;
import be.vanoosten.esa.tools.SemanticSimilarityTool;
import be.vanoosten.esa.tools.Vectorizer;

public class MainAnalyst {
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
			//System.out.println(semanticSimilarityTool.findSemanticSimilarity("pretty", "beautiful"));
			/*System.out.println(semanticSimilarityTool.findSemanticSimilarity("bạc liêu", "mỹ tho"));
			System.out.println(semanticSimilarityTool.findSemanticSimilarity("bạc liêu", "cà mau"));
			System.out.println(semanticSimilarityTool.findSemanticSimilarity("bạc liêu", "minh hải"));
			System.out.println(semanticSimilarityTool.findSemanticSimilarity("lê trọng tấn", "hoàng minh thảo"));
			System.out.println(semanticSimilarityTool.findSemanticSimilarity("lê trọng tấn", "trận đồng xoài"));*/
			ConceptVector conceptVector = vectorizer.vectorize("linkin park");
			System.out.println(conceptVector.topConcepts(10));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
