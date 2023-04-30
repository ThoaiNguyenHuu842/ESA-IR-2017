package luceneutil;

import be.vanoosten.esa.tools.ConceptVector;

/**
 * @author ThoaiNH
 * create Jul 18, 2017
 * document index storage ESA-based Retrieval
 */
public class ConceptDoc implements Comparable<ConceptDoc>{
	private String text;
	private ConceptVector c;
	private float score;
	private int id;
	public ConceptDoc(String text, ConceptVector c, float score, int id) {
		super();
		this.text = text;
		this.c = c;
		this.score = score;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public ConceptVector getC() {
		return c;
	}
	public void setC(ConceptVector c) {
		this.c = c;
	}
	public float getScore() {
		return score;
	}
	public void setScore(float score) {
		this.score = score;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	@Override
	public int compareTo(ConceptDoc o) {
		// TODO Auto-generated method stub
		if(this.score > o.score)
			return -1;
		else if(this.score < o.score)
			return 1;
		return 0;
	}
	
}
