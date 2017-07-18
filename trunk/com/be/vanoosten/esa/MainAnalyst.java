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
			/*System.out.println(semanticSimilarityTool.findSemanticSimilarity("american", "american"));
			System.out.println(semanticSimilarityTool.findSemanticSimilarity("american", "vietnam"));
			System.out.println(semanticSimilarityTool.findSemanticSimilarity("american", "ho chi minh"));
			System.out.println(semanticSimilarityTool.findSemanticSimilarity("vietnam", "ho chi minh"));
			System.out.println(semanticSimilarityTool.findSemanticSimilarity("sai gon", "ho chi minh"));
			System.out.println(semanticSimilarityTool.findSemanticSimilarity("le duan", "ho chi minh"));
			System.out.println(semanticSimilarityTool.findSemanticSimilarity("nguyen ai quoc", "ho chi minh"));*/
			
			vectorizer.vectorize("Leopard attacking and killing different animals").topConcepts(5);
			System.out.println(semanticSimilarityTool.findSemanticSimilarity("black panther", "x-men"));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
