package crawler;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import luceneutil.ApplicationUtils;

public class V100 implements Serializable {
	private int pv100;
	private String urlIndex;
	private String vv101; // url
	private String vv102; // title
	private String vv103;// content
	private String vv103token;// content da tach tu
	private String subVV103;// sub content
	private List<String> subUrls = new ArrayList<String>();// list link
	private boolean hasContent;

	public int getPv100() {
		return pv100;
	}

	public void setPv100(int pv100) {
		this.pv100 = pv100;
	}

	public String getVv101() {
		return vv101;
	}

	public void setVv101(String vv101) {
		this.vv101 = vv101;
	}

	public String getVv102() {
		return vv102;
	}

	public void setVv102(String vv102) {
		this.vv102 = vv102;
	}

	public String getVv103() {
		return vv103;
	}

	public void setVv103(String vv103) {
		this.vv103 = vv103;
	}

	public String getSubVV103() {
		return subVV103;

	}

	public void setSubVV103(String subVV103) {
		this.subVV103 = subVV103;
	}

	public List<String> getSubUrls() {
		return subUrls;
	}

	public void setSubUrls(List<String> subUrls) {
		this.subUrls = subUrls;
	}

	public String getVv103token() {
		vv103token = ApplicationUtils.getTokenString(vv103);
		return vv103token;
	}

	public void setVv103token(String vv103token) {
		this.vv103token = vv103token;
	}

	public String getUrlIndex() {
		urlIndex = ApplicationUtils.getTextUrlForIndex(vv101);
		return urlIndex;
	}

	public void setUrlIndex(String urlIndex) {
		this.urlIndex = urlIndex;
	}

	/*
	 * xet quyen edit v100 o admin
	 */
	public boolean isHasContent() {
		if (vv103 == null || vv103.length() == 0)
			return false;
		else
			return true;
	}

	public void setHasContent(boolean hasContent) {
		this.hasContent = hasContent;
	}

}
