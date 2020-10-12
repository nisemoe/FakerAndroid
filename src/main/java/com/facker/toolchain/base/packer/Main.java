package com.facker.toolchain.base.packer;

import java.io.File;
import java.io.IOException;

class Main {
    private static final int a = 0x12345678;
    private static final int b = 0x78563412;
    public static void test(){
        char ch = (char)a;
        if(0x12==ch){
            System.out.println("小端!");
        }else{
            System.out.println("大端!");
        }
    }

    public static void main(String[] args) throws IOException {
        Packer packer = new Packer(new File("src\\main\\assets\\dex"));
        packer.addFile("kl.dex", new File("Desktop\\xkp\\kl"));
        packer.saveToPath();
        //packer.unpackFromFile(new File("Desktop\\xkp\\kl"));
        




        
        
    }
}
