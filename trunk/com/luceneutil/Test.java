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
		// TODO Auto-generated method stub
		File file = new File("D:\\learning\\HK2\\IR\\seminar\\LSI\\DEMO\\lucene\\index");
deleteDirectory(file);
		System.out.println("xong");
	}

}
