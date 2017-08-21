package luceneutil;

/**
 * @author ThoaiNH
 * create Jul 31, 2017
 */
public class ApplicationConstant {
	public static final int MINIMUM_ARTICLE_LENGTH = 500;
	public static final int S = 10; // giới hạn số khái niệm lấy ra từ ESA
	public static final int N = 100; //số lượng tài liệu có điểm số cao nhất từ BOW cần lấy
	public static final float W = (float)0.5; //trọng số chuẩn hóa kết quả truy hồi từ BOW và ESA
	/*
	 * ESA index
	 */
	/*public static final String INDEX_PATH = "D:/learning/HK2/IR/seminar/LSI/DEMO/lucene/index4";
	public static final String INDEX_PATH_ESA = "D://learning//Master final project//Code//index-en";
	public static final String WIKI_PATH_ESA = "D://learning//Master final project//Code//index-en//dump//enwiki-20170420-pages-articles-multistream.xml.bz2";*/
	public static final String INDEX_PATH = "D://learning//Master final project//Code//index-BOW";
	public static final String INDEX_PATH_ESA = "D://learning//Master final project//Code//index-vi-45000c-token";
	public static final String WIKI_PATH_ESA = "//dump//viwiki-20170701-pages-articles-multistream.xml.bz2";
    public static final String STOP_WORDS_VI = "bị,bởi,cả,các,cái,cần,càng,chỉ,chiếc,cho,chứ,chưa,chuyện,có,có_thể,cứ,của,cùng,cũng,đã,đang,đây,để,đến_nỗi,đều,điều,do,đó,được,dưới,gì,khi,không,là,lại,lên,lúc,mà,mỗi,một_cách,này,nên,nếu,ngay,nhiều,như,nhưng,những,nơi,nữa,phải,qua,ra,rằng,rằng,rất,rất,rồi,sau,sẽ,so,sự,tại,theo,thì,trên,trước,từ,từng,và,vẫn,vào,vậy,vì,việc,với,vừa";
    public static final boolean TOKENIZED_WIKI_CONTENT = true;
    /*
	 * directory to run vntokenizer
	 */
	public static final String VNTOKENIZER_PROPERTIES_FILE="D:\\Document\\LUANVAN\\torun\\tokenizer.properties";
	public static final String SRC_TORUN_VNTOKENIZER="D:/Document/LUANVAN/torun/";
}
