package luceneutil;

import java.io.File;

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
		System.out.println((String.join(File.separator, "D://learning//Master final project//Code//index-vi")));
		System.out.println("xong");
	}

}
