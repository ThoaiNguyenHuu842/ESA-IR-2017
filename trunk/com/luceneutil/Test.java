package luceneutil;

import java.io.File;
import java.util.List;

public class Test {
	public static boolean deleteDirectory(File directory) {
	    if(directory.exists()){
	        File[] files = directory.listFiles();
	        if(null!=files){
	            for(int i=0; i<files.length; i++) {
	                if(files[i].isDirectory()) {
	                    deleteDirectory(files[i]);
	                }
	                else {
	                    files[i].delete();
	                }
	            }
	        }
	    }
	    return(directory.delete());
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			String indexPath = "D:/learning/HK2/IR/seminar/LSI/DEMO/lucene/index";
			MyLuncene luncene = new MyLuncene(indexPath);
			List<String> result = luncene.search(QueryFactory.create("TEXT", "là"), 100);
			String txtResult = "";
			for (String s : result) {
				txtResult += s;
				txtResult += "\n";
			}
			System.out.println(txtResult);
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("xong");
	}

}
