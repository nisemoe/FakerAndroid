package com.facker.toolchain.base.packer;

import com.google.common.io.LittleEndianDataInputStream;
import com.google.common.io.LittleEndianDataOutputStream;
import com.facker.toolchain.utils.FileUtil;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class Packer {

    public final byte HEADER[] = new byte[]{(byte)0x90, (byte)0x56, (byte)0x43, (byte)0x89};
    private Map<String, File> files = new HashMap<>();
    private final File srcFile;

    public Packer(File file){
        this.srcFile = file;
    }

    public void addFile(String name, File file) throws IOException {
        if( !file.exists() || !file.isFile() ){
            throw new IOException(String.format("%s File Not Exist Or File Is Dir.", name));
        }else {
            files.put(name, file);
        }
    }

    public boolean unpackFromFile(File parentPath) throws IOException {
        if(srcFile == null){
            throw new RuntimeException("SrcFile Field Not Set, use Packer((File)file)");
        }
        if(!srcFile.exists() || !srcFile.isFile()){
            throw new IOException("File Not Exist Or File Is Dir.");
        }
        final LittleEndianDataInputStream stream = new LittleEndianDataInputStream(new FileInputStream(this.srcFile));
        byte header[] = new byte[4];
        stream.read(header, 0, 4);
        if(header[0] != HEADER[0] ||header[1] != HEADER[1] ||header[2] != HEADER[2] ||header[3] != HEADER[3] ){
            throw new IOException("Not A Std Packer File");
        }
        while (stream.available() != 0){
            byte buffer[] = new byte[8096];
            String name = SafeString.read(stream);
            long crc = stream.readLong();
            long size = stream.readLong();
            FileOutputStream out = new FileOutputStream(new File(parentPath, name));
            while (size > 0){
                long readSize = Math.min(8096, size);
                int realLen = stream.read(buffer, 0, (int)readSize);
                size -= realLen;
                if(realLen > 0){
                    out.write(buffer, 0, realLen);
                }
            }
            out.close();
        }
        stream.close();
        return true;
    }

    public boolean saveToPath() throws IOException {
        File file = File.createTempFile("custom-", "" + Math.random());
        final LittleEndianDataOutputStream stream = new LittleEndianDataOutputStream(new FileOutputStream(file));
        stream.write(HEADER, 0, 4);
        for (Map.Entry<String, File> entry : files.entrySet()) {
            long crc = FileUtil.calcFileCRC32(entry.getValue());
            SafeString.write(stream, entry.getKey());
            stream.writeLong(crc);
            stream.writeLong(entry.getValue().length());

            BufferedInputStream inputStream = new BufferedInputStream(new FileInputStream(entry.getValue()));
            byte data[] = new byte[8096];
            int len;
            while ((len = inputStream.read(data, 0, 8096)) != -1){
                stream.write(data, 0, len);
            }
            inputStream.close();
        }
        stream.close();
        if(srcFile.exists()){
            srcFile.delete();
        }
        return file.renameTo(srcFile);
    }

    public static void main(String[] args) throws IOException {
        Packer packer = new Packer(new File("C:\\Users\\Efaker\\Desktop\\demo\\build\\outputs\\apk\\debug\\demo-debug\\dex"));
        packer.addFile("k1.dex",new File("C:\\Users\\Efaker\\Desktop\\demo\\build\\outputs\\apk\\debug\\demo-debug\\classes.dex"));
        packer.saveToPath();
    }
}
