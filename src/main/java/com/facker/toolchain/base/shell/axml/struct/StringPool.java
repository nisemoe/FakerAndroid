package com.facker.toolchain.base.shell.axml.struct;

public interface StringPool {

    public int IS_UTF8 = 1 << 8;

    /**
     * 获取当前池内容字节
     * @return
     */
    byte[] getBytes();


    /**
     * 获取字符串数量
     * @return
     */
    int getStringCount();

    int findStringId(String content);

    /**
     * 必定返回有效StringId
     * @param content
     * @return
     */
    int getOrCreateString(String content);

    String getString(int id);

    void setStringById(int id, String content);
}
