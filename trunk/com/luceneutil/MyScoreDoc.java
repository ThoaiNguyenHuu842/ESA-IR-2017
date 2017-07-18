package luceneutil;

import java.io.Serializable;

import org.apache.lucene.search.ScoreDoc;


/**
 * @author ThoaiNH
 * create Jun 27, 2017
 */
public class MyScoreDoc implements Serializable{
	private ScoreDoc scoreDoc;
	
	public MyScoreDoc(ScoreDoc scoreDoc) {
		super();
		this.scoreDoc = scoreDoc;
	}

	public ScoreDoc getScoreDoc() {
		return scoreDoc;
	}

	public void setScoreDoc(ScoreDoc scoreDoc) {
		this.scoreDoc = scoreDoc;
	}
	
}
