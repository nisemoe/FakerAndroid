package com.facker.toolchain.test;

import brut.androlib.AndrolibException;
import brut.androlib.mod.SmaliMod;
import brut.androlib.src.SmaliBuilder;
import brut.directory.DirectoryException;
import brut.directory.ExtFile;
import org.antlr.runtime.RecognitionException;
import org.jf.dexlib2.Opcodes;
import org.jf.dexlib2.writer.builder.DexBuilder;
import org.jf.dexlib2.writer.io.FileDataStore;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.logging.Logger;

public class MSmaliBuilder {
    private final ExtFile mSmaliDir;
    private final File mDexFile;
    private int mApiLevel = 0;
    private File javaSrc;
    private static final Logger LOGGER = Logger.getLogger(SmaliBuilder.class.getName());

    public static void build(ExtFile smaliDir, File dexFile, int apiLevel, File javaSrc) throws AndrolibException {
        (new MSmaliBuilder(smaliDir, dexFile, apiLevel,javaSrc)).build();
    }

    private MSmaliBuilder(ExtFile smaliDir, File dexFile, int apiLevel, File javaSrc) {
        this.mSmaliDir = smaliDir;
        this.mDexFile = dexFile;
        this.mApiLevel = apiLevel;
        this.javaSrc =javaSrc;
    }

    private void build() throws AndrolibException {
        try {
            DexBuilder dexBuilder;
            if (this.mApiLevel > 0) {
                dexBuilder = new DexBuilder(Opcodes.forApi(this.mApiLevel));
            } else {
                dexBuilder = new DexBuilder(Opcodes.getDefault());
            }

            Iterator var2 = this.mSmaliDir.getDirectory().getFiles(true).iterator();

            while(var2.hasNext()) {//TODO 过滤
                String fileName = (String)var2.next();
                //System.out.println(fileName);
                File fileJava = new File(javaSrc,fileName.replace(".smali",".java"));
                //System.out.println(fileJava.getAbsolutePath());
                if(fileJava.exists()){
                    System.out.println(fileJava.getAbsolutePath());
                    continue;
                }
                this.buildFile(fileName, dexBuilder);
            }
            dexBuilder.writeTo(new FileDataStore(new File(this.mDexFile.getAbsolutePath())));
        } catch (DirectoryException | IOException var4) {
            throw new AndrolibException(var4);
        }
    }

    private void buildFile(String fileName, DexBuilder dexBuilder) throws AndrolibException, IOException {
        File inFile = new File(this.mSmaliDir, fileName);
        InputStream inStream = new FileInputStream(inFile);
        if (fileName.endsWith(".smali")) {
            try {
                if (!SmaliMod.assembleSmaliFile(inFile, dexBuilder, this.mApiLevel, false, false)) {
                    throw new AndrolibException("Could not smali file: " + fileName);
                }
            } catch (RecognitionException | IOException var6) {
                throw new AndrolibException(var6);
            }
        } else {
            LOGGER.warning("Unknown file type, ignoring: " + inFile);
        }
        inStream.close();
    }
}
