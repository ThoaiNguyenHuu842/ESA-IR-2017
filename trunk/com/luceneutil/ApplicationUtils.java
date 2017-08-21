package luceneutil;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import org.apache.log4j.Logger;

import test.MainAnalyst;
import vn.hus.nlp.tokenizer.VietTokenizer;

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
}
