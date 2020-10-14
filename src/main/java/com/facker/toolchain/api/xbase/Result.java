package com.facker.toolchain.api.xbase;

public class Result {

    public String getErroCode() {
        return erroCode;
    }

    public void setErroCode(String erroCode) {
        this.erroCode = erroCode;
    }

    public String getOutPath() {
        return outPath;
    }

    public void setOutPath(String outPath) {
        this.outPath = outPath;
    }

    public String getErroMsg() {
        return erroMsg;
    }

    public void setErroMsg(String erroMsg) {
        this.erroMsg = erroMsg;
    }

    String erroCode;

    String outPath;

    String erroMsg;

    @Override
    public String toString() {
        return "Result{" +
                "erroCode='" + erroCode + '\'' +
                ", outPath='" + outPath + '\'' +
                ", erroMsg='" + erroMsg + '\'' +
                '}';
    }
}
