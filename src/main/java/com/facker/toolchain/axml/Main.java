package com.facker.toolchain.axml;

import com.facker.toolchain.base.beta.PathUtil;
import com.facker.toolchain.base.utils.IOUtil;
import com.facker.toolchain.axml.struct.AttributeData;

import javax.swing.*;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

public class Main {


    public static void main(String[] args) throws Exception {

        File dir = PathUtil.inputFile("", JFileChooser.FILES_AND_DIRECTORIES, false);
        BinXmlParser editor = new BinXmlParser(dir);
//        editor.resDump();
        String applicationName = editor.memReadAttr("application", "application", "name");
        List<AttributeData> data = editor.memListAttr("application");
        int i = editor.lookingForGoodPosition(data, "networkSecurityConfig");
        editor.memAddAttr("application", editor.createAttributeData("networkSecurityConfig",
                "@7F00CCCC"),
                editor.lookingForGoodPosition(editor.memListAttr("application"),
                        "networkSecurityConfig"));
        FileOutputStream fileOutputStream = new FileOutputStream(new File(dir.getParent(),"z.xml"));
        ByteArrayInputStream inputStream = new ByteArrayInputStream(editor.memSave());
        IOUtil.copy(inputStream, fileOutputStream);
    }


    @Deprecated
    public static String readPath(String desc) {
        return "";
    }

    public static File inputFile(String desc, int mode, boolean isSave){
        JFileChooser fd = new JFileChooser();
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
}
