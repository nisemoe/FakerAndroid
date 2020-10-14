package com.facker.toolchain.api.xbase;

import java.io.File;

public class SourceCode {

    private File sourceRoot;

    public SourceCode(File sourceRoot) {
        this.sourceRoot = sourceRoot;
    }

    public File getSourceRoot() {
        return sourceRoot;
    }

    public File getJavaScaffodingLibs(){
        return new File(sourceRoot,"libs-javaScaffoding");
    }
    public File getJavaScaffodingJava(){
        return new File(sourceRoot,"java-javaScaffoding");
    }
    public File ManifestjavaScaffoding(){
        return new File(sourceRoot,"Manifest-javaScaffoding");
    }
    public File getSrc(){
        return new File(sourceRoot,"src");
    }


    public File getBuildGame(){
        return new File(sourceRoot,"build-app");
    }
    public File getBuildJavaScffoing(){
        return new File(sourceRoot,"build-javaScaffoding");
    }

    public File getBuildProject(){
        return new File(sourceRoot,"build-project");
    }

    public File getCpp(boolean isIl2cpp){
        if(isIl2cpp){
            return new File(sourceRoot,"il2cpp-cpp");
        }
        return new File(sourceRoot,"cpp");
    }
    public File getJava(boolean isIl2cpp){
        if(isIl2cpp){
            return new File(sourceRoot,"il2cpp-java");
        }
        return new File(sourceRoot,"java");
    }

    public File getJniLibs(){
        return new File(sourceRoot,"jniLibs");
    }
    public File getAssets(){
        return new File(sourceRoot,"assets");
    }

    public File getRes(){
        return new File(sourceRoot,"res");
    }
    public File getManifest(){
        return new File(sourceRoot,"AndroidManifest.xml");
    }

    public File getScaffolding_cpp(){
        return new File(sourceRoot,"scaffolding-cpp");
    }
}
