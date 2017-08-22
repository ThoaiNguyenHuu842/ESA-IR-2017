package luceneutil;

import java.util.ArrayList;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import crawler.V100;
import vn.hus.nlp.tokenizer.VietTokenizer;

/**
 * @author ThoaiNH
 * create Aug 21, 2017
 */
public class ApplicationUtils {
	private static Logger log = Logger.getLogger(ApplicationUtils.class);
	private static final VietTokenizer vietTokenizer = new VietTokenizer();
	public static synchronized String getTokenString(String s){
		String[] tokens = vietTokenizer.tokenize(s);
		String rs = null;
		if (tokens != null && tokens.length > 0)
			rs = tokens[0];
		else 
			rs = s;
		log.info("rs getTokenString:"+rs);
		return rs;
	}
	public static void showGrowlMessage(String message){
		FacesContext context = FacesContext.getCurrentInstance();
        context.addMessage(null, new FacesMessage("Message", message) );
	}
	public static String getTextUrlForIndex(String orgUrl) {
		String url = orgUrl.replace("http", "").replace("/", " ")
				.replace(".", " ").replace(":", "");
		return url;
	}
	/*
	 * lay noi dung update lai v100
	 */
	public static void getInforFromURL(V100 v100) {
		try {
			System.out.println("--conecting to:" + v100.getVv101());
			log.info("--Conecting to:" + v100.getVv101());
			if (Jsoup.connect(v100.getVv101()) != null) {
				Document doc = Jsoup.connect(v100.getVv101()).get();

				if (doc != null) {
					// lay abs url chon index
					Element link = doc.select("a").first();
					String absHref = link.attr("abs:href");
					if (absHref != null && absHref.length() > 1)
						v100.setUrlIndex(getTextUrlForIndex(absHref));
					else
						v100.setUrlIndex(getTextUrlForIndex(v100.getVv101()));

					// lay noi dung
					v100.setVv103(doc.text());
					System.out
							.println("---------token:" + v100.getVv103token());
					// lay title
					v100.setVv102(doc.title());
					// lay link
					List<String> subUrls = new ArrayList<String>();
					Elements links = doc.select("a[href]");
					for (Element l : links) {
						String s = l.attr("abs:href");
						subUrls.add(s);
					}
					v100.setSubUrls(subUrls);
				} else {
					log.info("--Can'nt conect to url:"
							+ v100.getVv101());
				}
			} else {
				log.info("--Can'nt conect to url:"
						+ v100.getVv101());
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			/*
			 * khi loi connect thi xoa trong db va xoa chi muc
			 */
			System.out.println("--loi khi get url:" + v100.getVv101());
			log.info("--Can'nt conect to url:"
					+ v100.getVv101());
			e.printStackTrace();
			return;
		}
	}
}
