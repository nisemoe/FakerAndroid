package com.facker.toolchain.api.xbase.action;

import com.facker.toolchain.base.packer.Packer;
import com.facker.toolchain.utils.FileUtil;
import com.facker.toolchain.base.utils.IOUtil;
import com.facker.toolchain.api.xbase.ManifestEditor;
import com.facker.toolchain.api.xbase.WorkerAction;
import com.facker.toolchain.api.xbase.XModel;
import com.facker.toolchain.api.xbase.XTarget;
import org.dom4j.DocumentException;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class OrlderXTargetAndXModelsDex extends WorkerAction {
    private XTarget xTarget;
    private List<XModel> xModels;
    public OrlderXTargetAndXModelsDex(XTarget xTarget, List<XModel> xModels){
        this.xTarget = xTarget;
        this.xModels = xModels;
    }

    @Override
    public void action() throws Exception {

        if(xTarget.isShell()){
            shell();
        }else{
            muiti();
        }
    }
    private void muiti() throws IOException, DocumentException {
        FileUtil.deleteDir(xTarget.getMETAINF());
        List<File> targetDexList = xTarget.getDexsFiles();

        File tmpDexDir = new File(xTarget.getDecodeDir(),"temDexDir");
        tmpDexDir.mkdir();
        for (int i=0;i<targetDexList.size();i++ ) {
            File file = targetDexList.get(i);
            IOUtil.copyFile(file,new File(tmpDexDir,file.getName()));
            file.delete();
        }
        File dexes[] = tmpDexDir.listFiles();

        int num = 2;
        for (int i= 0; i<dexes.length; i++) {
            IOUtil.copyFile(dexes[i],new File(xTarget.getDecodeDir(),"classes"+num+".dex"));
            num++;
        }
        delete(tmpDexDir);
        //SDK DEX 多个SDK
        for (XModel xModel :xModels) {
            List<File> files =xModel.getDexsFiles();
            for (File file: files) {
                IOUtil.copyFile(file,new File(xTarget.getDecodeDir(),"classes"+num+".dex"));
                num++;
            }
        }
    }
    public boolean delete(File file){
        if(!file.exists()){
            return false;
        }
        if(file.isFile()){
            return file.delete();
        }
        File[] files = file.listFiles();
        for (File f : files) {
            if(f.isFile()){
                if(!f.delete()){
                    System.out.println(f.getAbsolutePath()+" delete error!");
                    return false;
                }
            }else{
                if(!this.delete(f)){
                    return false;
                }
            }
        }
        return file.delete();
    }

    private void shell() throws IOException, DocumentException {
        FileUtil.deleteDir(xTarget.getMETAINF());
        Packer packer = new Packer(xTarget.getPackerDexFile());
        List<File> targetDexList = xTarget.getDexsFiles();
        //原应用DEX
        for (File f : targetDexList) {
            String name = f.getName();
            packer.addFile(name, f);
        }
        //SDK DEX 多个SDK
        for (XModel xModel :xModels){
            List<File> files =xModel.getDexsFiles();
            File manifestFile = xModel.getManifestFile();
            ManifestEditor manifestMod = new ManifestEditor(manifestFile);
            String packageName = manifestMod.getPackagenName();
            for (File f:files) {
                String name = f.getName();
                packer.addFile("plug_"+packageName+"_"+name, f);
            }
        }
        //删除原来的残留
        packer.saveToPath();
        for (File f:targetDexList){
            f.delete();
        }
    }
}
