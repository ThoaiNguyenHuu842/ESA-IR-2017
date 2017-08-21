package be.vanoosten.esa;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.apache.commons.compress.compressors.bzip2.BZip2CompressorInputStream;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StoredField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.util.Version;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import luceneutil.ApplicationConstant;
import luceneutil.ApplicationUtils;

/**
 *
 * @author Philip van Oosten
 */
public class WikiIndexer extends DefaultHandler implements AutoCloseable {
	private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(WikiIndexer.class);
    private final SAXParserFactory saxFactory;
    private boolean inPage;
    private boolean inPageTitle;
    private boolean inPageText;
    private StringBuilder content = new StringBuilder();
    private String wikiTitle;
    public static int numIndexed = 0;
    public static int numTotal = 0;

    public static final String TEXT_FIELD = "text";
    public static final String TITLE_FIELD = "title";
    Pattern pat;

    IndexWriter indexWriter;

    int minimumArticleLength;

    /**
     * Gets the minimum length of an article in characters that should be
     * indexed.
     *
     * @return
     */
    public int getMinimumArticleLength() {
        return minimumArticleLength;
    }

    /**
     * Sets the minimum length of an article in characters for it to be indexed.
     *
     * @param minimumArticleLength
     */
    public final void setMinimumArticleLength(int minimumArticleLength) {
        this.minimumArticleLength = minimumArticleLength;
    }

    public WikiIndexer(Analyzer analyzer, Directory directory) throws IOException {
        saxFactory = SAXParserFactory.newInstance();
        saxFactory.setNamespaceAware(true);
        saxFactory.setValidating(true);
        saxFactory.setXIncludeAware(true);

        IndexWriterConfig indexWriterConfig = new IndexWriterConfig(Version.LUCENE_48, analyzer);
        indexWriter = new IndexWriter(directory, indexWriterConfig);
        String regex = "^[a-zA-z]+:.*";
        pat = Pattern.compile(regex);
        setMinimumArticleLength(ApplicationConstant.MINIMUM_ARTICLE_LENGTH);
    }

    public void parseXmlDump(String path) {
        parseXmlDump(new File(path));
    }

    public void parseXmlDump(File file) {
        try {
            SAXParser saxParser = saxFactory.newSAXParser();
            InputStream wikiInputStream = new FileInputStream(file);
            wikiInputStream = new BufferedInputStream(wikiInputStream);
            wikiInputStream = new BZip2CompressorInputStream(wikiInputStream, true);
            saxParser.parse(wikiInputStream, this);
        } catch (ParserConfigurationException | SAXException | FileNotFoundException ex) {
            Logger.getLogger(WikiIndexer.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(WikiIndexer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
    	//index only n concepts to test tokenizer VietName sentences
    	if(numIndexed == 70000)
    	{
    		try {
				indexWriter.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    		throw new SAXException();
    	}
        if ("page".equals(localName)) {
            inPage = true;
        } else if (inPage && "title".equals(localName)) {
            inPageTitle = true;
            content = new StringBuilder();
        } else if (inPage && "text".equals(localName)) {
            inPageText = true;
            content = new StringBuilder();
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
    	
        if (inPage && inPageTitle && "title".equals(localName)) {
            inPageTitle = false;
            wikiTitle = content.toString();
        } else if (inPage && inPageText && "text".equals(localName)) {
            inPageText = false;
            String wikiText = content.toString();
            try {
                numTotal++;
	            log.info("process for wiki article:" + numTotal + "\t" + wikiTitle);
                boolean rs = index(wikiTitle, wikiText);
                log.info("--rs indexing:"+rs);
                if (rs) {
                    numIndexed++;
                    log.info("indexed:" + numIndexed + "\t/ " + numTotal + "\t" + wikiTitle);
                }
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        } else if (inPage && "page".equals(localName)) {
            inPage = false;
        }
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        content.append(ch, start, length);
    }

    /**
     * Thoai Nguyen note: THEM TUNG BAI VIET VAO INDEX
     * @param title
     * @param wikiText
     * @return
     * @throws IOException
     */
    boolean index(String title, String wikiText) throws IOException {
        Matcher matcher = pat.matcher(title);
        if (matcher.find() || title.startsWith("Bản mẫu:") || title.startsWith("Danh sách") 
        		|| title.startsWith("Chủ đề:") || title.startsWith("Lijst van ") || title.startsWith("Thể loại:") || title.startsWith("Tập tin:") ||
        		wikiText.length() < getMinimumArticleLength() || numTotal == 48230 || numTotal == 26680) {
        	log.info("---index return false");
            return false;
        }
        //đánh từ chị mục thứ 8000
        if(numIndexed > 55000){
	        Document doc = new Document();
	        doc.add(new StoredField(TITLE_FIELD, title));
	        Analyzer analyzer = indexWriter.getAnalyzer();
	        if(ApplicationConstant.TOKENIZED_WIKI_CONTENT){
	        	log.info("---getTokenString");
	        	String docToken = ApplicationUtils.getTokenString(wikiText);
	        	try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	        	doc.add(new TextField(TEXT_FIELD, docToken, Field.Store.NO));
	        }
	        else
	        	doc.add(new TextField(TEXT_FIELD, wikiText, Field.Store.NO));
	        log.info("---save new document");
	        indexWriter.addDocument(doc);
        }
        return true;
    }

    @Override
    public void close() throws IOException {
    	log.info("close sax parser");
        indexWriter.close();
    }

}
