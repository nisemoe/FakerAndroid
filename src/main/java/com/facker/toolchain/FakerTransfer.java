package com.facker.toolchain;

import com.facker.toolchain.base.shell.api.xbase.Importer;
import com.facker.toolchain.base.shell.api.xbase.SourceCode;
import com.facker.toolchain.base.shell.api.xbase.XSrcTarget;

import java.io.File;
import java.io.IOException;

public class FakerTransfer {
    public static void translate(String apkFilePath) {
        File sourceCodeDir = new File("patch");
        SourceCode sourceCode = new SourceCode(sourceCodeDir);
        File targetFile = new File(apkFilePath);//废弃
        XSrcTarget xSrcTarget = new XSrcTarget(targetFile);
        Importer importer = new Importer(xSrcTarget,sourceCode);
        try {
            importer.doImport();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
