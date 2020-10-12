package com.facker.toolchain.base.beta;

import javax.swing.*;
import java.io.File;
import java.util.Scanner;

public class PathUtil {
    @Deprecated
    public static String readPath(String desc) {
        return getString(desc);
    }

    private static String getString(String desc) {
        System.out.print(desc);
        Scanner scanner = new Scanner(System.in);
        String targetString = scanner.next();
        scanner.close();
        return targetString;
    }

    public static File inputFile(String desc, int mode, boolean isSave){
        JFileChooser fd = new JFileChooser("\\");
        fd.setFileSelectionMode(mode);
        int result = isSave ? fd.showSaveDialog(null): fd.showOpenDialog(null);
        if(JFileChooser.APPROVE_OPTION == result){
            return fd.getSelectedFile();
        }else {
            return inputFile(desc, mode, isSave);
        }
    }
    public static File inputFile(String desc){
        return inputFile(desc, 0, false);
    }

    public static String getFileNameNoEx(String filePath) {
		 String filename = getFileName(filePath);
       if ((filename != null) && (filename.length() > 0)) {   
           int dot = filename.lastIndexOf('.');   
           if ((dot >-1) && (dot < (filename.length()))) {   
               return filename.substring(0, dot);   
           }   
       }   
       return filename;   
   }   
    
    private static String getFileName(String filePath) {
		File tempFile =new File(filePath.trim());  
	    String fileName = tempFile.getName();
	    return fileName;
	}
}
