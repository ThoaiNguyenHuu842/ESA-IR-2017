package test;

import static org.apache.lucene.util.Version.LUCENE_48;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;

import org.apache.log4j.Logger;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.util.CharArraySet;

import be.vanoosten.esa.EnwikiFactory;
import be.vanoosten.esa.WikiAnalyzer;
import be.vanoosten.esa.WikiFactory;
import be.vanoosten.esa.tools.ConceptVector;
import be.vanoosten.esa.tools.SemanticSimilarityTool;
import be.vanoosten.esa.tools.Vectorizer;
import luceneutil.ApplicationUtils;

public class MainAnalyst {
	private static Logger log = Logger.getLogger(MainAnalyst.class);
	public static void main(String[] args) {
		try {
			WikiFactory factory = new EnwikiFactory();
	        File indexPath = factory.getIndexRootPath();
	        CharArraySet stopWords = factory.getStopWords();
	        Analyzer analyzer = new WikiAnalyzer(LUCENE_48, stopWords);
			Vectorizer vectorizer = new Vectorizer(indexPath, analyzer);

			SemanticSimilarityTool semanticSimilarityTool = new SemanticSimilarityTool(vectorizer);
			/*
			 * text similarity 
			 */
			//log.info(semanticSimilarityTool.findSemanticSimilarity("pretty", "beautiful"));
			
			/*log.info(semanticSimilarityTool.findSemanticSimilarity("bạc liêu", "cà mau"));
			log.info(semanticSimilarityTool.findSemanticSimilarity("bạc liêu", "minh hải"));
			log.info(semanticSimilarityTool.findSemanticSimilarity("lê trọng tấn", "hoàng minh thảo"));
			log.info(semanticSimilarityTool.findSemanticSimilarity("lê trọng tấn", "trận đồng xoài"));*/
			String s = "Idris Hylton ở St Albans Hertfordshire Anh đã viết một bức thư cho NASA Cơ quan Hàng không và Vũ trụ Mỹ yêu cầu sử dụng thiết kế tên lửa của mình thậm chí còn đề nghị giúp đưa sản phẩm cuối cùng vào không gian Theo The Sun ngày 198 trong bức thư ngắn gọn cậu bé bốn tuổi xin được cấp giấy phép phi hành gia Để thuyết phục NASA Idris khẳng định tên lửa của mình sẽ bay nhanh hơn bất kỳ thiết bị nào khác từng được thiết kế bởi cơ quan này Thiết kế tên lửa của Idris Hylton Bố Idris Jamal Hylton cho biết con trai phát điên khi nhận được phản hồi bởi thiết kế của em được khen ngợi Em gọi điện cho bố trong giờ làm việc và hét lên Bố ơi NASA trả lời rồi Kevin DeBruin kỹ sư hệ thống từ phòng thí nghiệm Jet Propulsion của NASA viết Tạo sản phẩm như thế này là bước khởi đầu của phi hành gia tuyệt vời trong tương lai người có thể điều khiển tên lửa Cháu hãy giữ tinh thần đó nhé Làm việc với các thiết bị không gian đòi hỏi sự chăm chỉ và cống hiến Điều này có nghĩa cháu phải tập trung cho việc học ở trường và luôn luôn nỗ lực hết sức Những việc cháu làm bây giờ sẽ khiến công việc sau này dễ dàng hơn nhiều cơ hội rộng mở hơn Cháu hãy tiếp tục quan tâm đến không gian vũ trụ và tên lửa Với sự nhiệt tình và chăm chỉ hy vọng cháu có thể đóng góp cho một trong những chương trình thú vị của NASA trong tương lai Chúc cháu may mắn trong hành trình vào không gian Cậu bé bốn tuổi được NASA khuyến khích theo đuổi đam mê Vài tháng đầu tiên sau khi bức thư được gửi đi NASA không phản hồi Cơ quan của Mỹ chú ý đến bức thư khi Jamal đăng lên Twitter Hai bố con đọc đi đọc lại bức thư vô số lần và Idris rất háo hức trở lại trường vào tháng 9 để khoe với giáo viên Thằng bé đang ao ước trở thành phi hành gia hoặc kỹ sư Bức thư từ Kevin DeBruin đã truyền cảm hứng khiến nó tin đó là giấc mơ có thật Jamal nói";
			System.out.println(ApplicationUtils.getTokenString(s));
			ConceptVector conceptVector = vectorizer.vectorize(s);
			log.info(conceptVector.topConcepts(10));
			ConceptVector conceptVector2 = vectorizer.vectorize("Mạc Thái Tổ");
			log.info(conceptVector2.topConcepts(10));
			log.info(semanticSimilarityTool.findSemanticSimilarity(s, "Mạc Thái Tổ"));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
