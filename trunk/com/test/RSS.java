package test;

import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.*;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.xml.sax.*;
import org.xml.sax.helpers.*;

public class RSS {
	private List<String> listURL = new ArrayList<String>();
	private String url;
	public RSS(String url){
		this.url = url;
	}
    public void run() {
        try {
            SAXParserFactory spf = SAXParserFactory.newInstance();
            SAXParser sp = spf.newSAXParser();
            sp.parse(url, new RSSHandler());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private class RSSHandler extends DefaultHandler {

        private int count = 0;
        private String tagName = "";

        @Override
        public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
            if (qName.equals("item")) {
                this.count++;
                //System.out.println("==========================");
            }
            if (qName.equals("title")) {
                tagName = qName;
            }
            if (qName.equals("description")) {
                tagName = qName;
            }

        }

        @Override
        public void characters(char[] ch, int start, int length) throws SAXException {
            if (tagName.equals("title")) {
                //System.out.println("Item: " + (new String(ch, start, length)));
                this.tagName = "";
            }
            if (tagName.equals("description")) {
            	String html = new String(ch, start, length);
                //System.out.println("HTML: " + html);
                Document document = Jsoup.parse(html);
                Elements links = document.select("a[href]");
                if(links.size() > 0)
                	listURL.add(links.get(0).attr("abs:href"));
                this.tagName = "";
            }
        }

        @Override
        public void startDocument() throws SAXException {
            System.out.println("========="+url+"========");
        }

        @Override
        public void endDocument() throws SAXException {
            System.out.println("total item: " + this.count);
        }

    }

	public List<String> getListURL() {
		return listURL;
	}
	public void setListURL(List<String> listURL) {
		this.listURL = listURL;
	}
    
}