package be.vanoosten.esa;

import java.io.File;

import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.core.StopAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.util.CharArraySet;
import org.apache.lucene.util.Version;

import luceneutil.ApplicationConstant;

/**
 *
 * @author Philip van Oosten
 */
public class EnwikiFactory extends WikiFactory {
	public static CharArraySet charArraySetVi;
	static {
    	CharArraySet stopSet = CharArraySet.copy(Version.LUCENE_48, StandardAnalyzer.STOP_WORDS_SET);
    	String ss[] = ApplicationConstant.STOP_WORDS_VI.split(",");
    	for(String stopWord: ss)
    		stopSet.add(stopWord);
	}
    public EnwikiFactory() {
        super(indexRootPath(),
                new File(indexRootPath(), ApplicationConstant.WIKI_PATH_ESA),
                StopAnalyzer.ENGLISH_STOP_WORDS_SET);
    }

    private static File indexRootPath() {
        return new File(ApplicationConstant.INDEX_PATH_ESA);
    }
    
    public static void main(String[] args) {
    	CharArraySet stopSet = CharArraySet.copy(Version.LUCENE_48, StandardAnalyzer.STOP_WORDS_SET);
    	String ss[] = ApplicationConstant.STOP_WORDS_VI.split(",");
    	for(String stopWord: ss)
    		stopSet.add(stopWord);
    	System.out.println(stopSet.toArray().length);
	}
}
