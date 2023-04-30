package luceneutil;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder.In;

import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

/**
 * @author ThoaiNH 
 * create 06/03/2015 
 * topic indexer
 */
public class MyLuncene extends MyLuceneBase {
	public MyLuncene(String indexPath) {
		super(indexPath);
		// TODO Auto-generated constructor stub
	}

	public int add(String text, int id) {
		try {
			org.apache.lucene.document.Document document = new org.apache.lucene.document.Document();
			document.add(new TextField("TEXT", text, Field.Store.YES));
			document.add(new TextField("ID", String.valueOf(id), Field.Store.YES));
			writer.addDocument(document);
			writer.commit();
			return 1;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return 0;
		}

	}

	/*public List<String> search(Query query, int maxHit) {
		List<String> listResult = new ArrayList<String>();
		// search lucene
		Directory directory;
		try {
			directory = FSDirectory.open(new File(indexPath));
			IndexReader indexReader = DirectoryReader.open(directory);
			IndexSearcher indexSearcher = new IndexSearcher(indexReader);
			TopScoreDocCollector collector = TopScoreDocCollector.create(maxHit, true);
			indexSearcher.search(query, collector);
			ScoreDoc[] hitsDocs = collector.topDocs().scoreDocs;
			for (ScoreDoc scoreDoc : hitsDocs) {
				org.apache.lucene.document.Document document = indexSearcher.doc(scoreDoc.doc);
				String text = document.get("TEXT").toString();
				int length = text.split(" ").length;
				listResult.add(text + "; sim = " + scoreDoc.score);
			}
			indexReader.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return listResult;
	}*/
	
	public List<ConceptDoc> search(Query query, int maxHit) {
		List<ConceptDoc> listResult = new ArrayList<ConceptDoc>();
		// search lucene
		Directory directory;
		try {
			directory = FSDirectory.open(new File(indexPath));
			IndexReader indexReader = DirectoryReader.open(directory);
			IndexSearcher indexSearcher = new IndexSearcher(indexReader);
			TopScoreDocCollector collector = TopScoreDocCollector.create(maxHit, true);
			indexSearcher.search(query, collector);
			ScoreDoc[] hitsDocs = collector.topDocs().scoreDocs;
			for (ScoreDoc scoreDoc : hitsDocs) {
				org.apache.lucene.document.Document document = indexSearcher.doc(scoreDoc.doc);
				String text = document.get("TEXT").toString();
				int id = Integer.parseInt(document.get("ID").toString());
				ConceptDoc conceptDoc = new ConceptDoc(text, null, scoreDoc.score, id);
				listResult.add(conceptDoc);
			}
			indexReader.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return listResult;
	}
}
