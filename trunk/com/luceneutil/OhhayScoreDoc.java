package luceneutil;

import java.io.Serializable;

import org.apache.lucene.search.ScoreDoc;

/**
 * @author ThoaiNH
 *
 */
public class OhhayScoreDoc implements Serializable{
	private ScoreDoc scoreDoc;
	
	public OhhayScoreDoc(ScoreDoc scoreDoc) {
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
