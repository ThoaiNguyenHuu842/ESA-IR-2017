package luceneutil;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

/**
 * @author ThoaiNH
 * create 09/09/2014
 * base indexer
 */
public class QbLuceneBase {
	protected IndexWriter writer;
	protected String indexPath;
	public QbLuceneBase(String indexPath) {
		super();
		this.indexPath = indexPath;
		System.out.println("---path:" + indexPath);
	}
	/*
	 * create index 
	 */
	public void createIndex() {
		// TODO Auto-generated constructor stub
		try {
			boolean create = true;
			File indexDirFile = new File(indexPath);
			if (indexDirFile.exists() && indexDirFile.isDirectory()) {
				create = false;
			}

			Directory dir = FSDirectory.open(indexDirFile);
			Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_46);
			IndexWriterConfig iwc = new IndexWriterConfig(Version.LUCENE_46,
					analyzer);
			if (create) {
				// Create a new index in the directory, removing any
				// previously indexed documents:
				iwc.setOpenMode(IndexWriterConfig.OpenMode.CREATE);
			}
			else
				iwc.setOpenMode(IndexWriterConfig.OpenMode.APPEND);
			IndexWriter writer = new IndexWriter(dir, iwc);
			writer.commit();
			writer.close(true);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/*
	 * create index writer
	 */
	public void createIndexWriter() {
		try {
			File indexDirFile = new File(indexPath);
			Directory dir = FSDirectory.open(indexDirFile);
			Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_46);
			IndexWriterConfig iwc = new IndexWriterConfig(Version.LUCENE_46,
					analyzer);
			iwc.setOpenMode(IndexWriterConfig.OpenMode.CREATE_OR_APPEND);
			this.writer = new IndexWriter(dir, iwc);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * @param query
	 * @param maxHit
	 * @param mode = 1: search by web (save session), 2: search by service
	 * @return list scoreDoc
	 */
	public List<OhhayScoreDoc> search(Query query, int maxHit, int mode) {
		List<OhhayScoreDoc> listScoreDocs = new ArrayList<OhhayScoreDoc>();
		// search lucene
		Directory directory;
		try {
			System.out.println("--index path:" + indexPath);
			directory = FSDirectory.open(new File(indexPath));
			IndexReader indexReader = DirectoryReader.open(directory);
			IndexSearcher indexSearcher = new IndexSearcher(indexReader);
			TopScoreDocCollector collector = TopScoreDocCollector
					.create(maxHit, true);
			indexSearcher.search(query, collector);
			ScoreDoc[] hitsDocs = collector.topDocs().scoreDocs;
			for (ScoreDoc scoreDoc : hitsDocs) {
				OhhayScoreDoc doc = new OhhayScoreDoc(scoreDoc);
				listScoreDocs.add(doc);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return listScoreDocs;
	}

	public void closeIndexWriter() {
		try {
			this.writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
