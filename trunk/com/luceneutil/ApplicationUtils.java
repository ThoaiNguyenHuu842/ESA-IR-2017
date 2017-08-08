package luceneutil;

import org.apache.log4j.Logger;

import test.MainAnalyst;
import vn.hus.nlp.tokenizer.VietTokenizer;

public class ApplicationUtils {
	private static Logger log = Logger.getLogger(ApplicationUtils.class);
	private static final VietTokenizer vietTokenizer = new VietTokenizer();
	public static synchronized String getTokenString(String s){
		synchronized (s) {
			
		}
		String[] tokens = vietTokenizer.tokenize(s);
		String rs = null;
		if (tokens != null && tokens.length > 0)
			rs = tokens[0];
		else 
			rs = s;
		log.info("rs getTokenString:"+rs);
		return rs;
	}
}
