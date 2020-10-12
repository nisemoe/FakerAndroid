package com.facker.toolchain.utils;

import java.io.*;
import java.lang.reflect.Method;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CodingErrorAction;
import java.nio.charset.StandardCharsets;
import java.util.*;

class PasswordRetriever implements AutoCloseable {
    public static final String SPEC_STDIN = "stdin";
    private final Charset mConsoleEncoding = getConsoleEncoding();
    private final Map<File, InputStream> mFileInputStreams = new HashMap();
    private boolean mClosed;

    PasswordRetriever() {
    }

    public List<char[]> getPasswords(String spec, String description, Charset... additionalPwdEncodings) throws IOException {
        this.assertNotClosed();
        if (spec.startsWith("pass:")) {
            char[] pwd = spec.substring("pass:".length()).toCharArray();
            return this.getPasswords(pwd, additionalPwdEncodings);
        } else if ("stdin".equals(spec)) {
            Console console = System.console();
            if (console != null) {
                char[] pwd = console.readPassword(description + ": ", new Object[0]);
                if (pwd == null) {
                    throw new IOException("Failed to read " + description + ": console closed");
                } else {
                    return this.getPasswords(pwd, additionalPwdEncodings);
                }
            } else {
                System.out.println(description + ": ");
                byte[] encodedPwd = readEncodedPassword(System.in);
                if (encodedPwd.length == 0) {
                    throw new IOException("Failed to read " + description + ": standard input closed");
                } else {
                    return this.getPasswords(encodedPwd, Charset.defaultCharset(), additionalPwdEncodings);
                }
            }
        } else {
            String name;
            if (spec.startsWith("file:")) {
                name = spec.substring("file:".length());
                File file = (new File(name)).getCanonicalFile();
                InputStream in = (InputStream)this.mFileInputStreams.get(file);
                if (in == null) {
                    in = new FileInputStream(file);
                    this.mFileInputStreams.put(file, in);
                }

                byte[] encodedPwd = readEncodedPassword((InputStream)in);
                if (encodedPwd.length == 0) {
                    throw new IOException("Failed to read " + description + " : end of file reached in " + file);
                } else {
                    return this.getPasswords(encodedPwd, Charset.defaultCharset(), additionalPwdEncodings);
                }
            } else if (spec.startsWith("env:")) {
                name = spec.substring("env:".length());
                String value = System.getenv(name);
                if (value == null) {
                    throw new IOException("Failed to read " + description + ": environment variable " + value + " not specified");
                } else {
                    return this.getPasswords(value.toCharArray(), additionalPwdEncodings);
                }
            } else {
                throw new IOException("Unsupported password spec for " + description + ": " + spec);
            }
        }
    }

    private List<char[]> getPasswords(char[] pwd, Charset... additionalEncodings) {
        List<char[]> passwords = new ArrayList(3);
        this.addPasswords(passwords, pwd, additionalEncodings);
        return passwords;
    }

    private List<char[]> getPasswords(byte[] encodedPwd, Charset encodingForDecoding, Charset... additionalEncodings) {
        ArrayList passwords = new ArrayList(4);

        try {
            char[] pwd = decodePassword(encodedPwd, encodingForDecoding);
            this.addPasswords(passwords, pwd, additionalEncodings);
        } catch (IOException var6) {
        }

        addPassword(passwords, castBytesToChars(encodedPwd));
        return passwords;
    }

    private void addPasswords(List<char[]> passwords, char[] pwd, Charset... additionalEncodings) {
        if (additionalEncodings != null && additionalEncodings.length > 0) {
            Charset[] var4 = additionalEncodings;
            int var5 = additionalEncodings.length;

            for(int var6 = 0; var6 < var5; ++var6) {
                Charset encoding = var4[var6];

                try {
                    char[] encodedPwd = castBytesToChars(encodePassword(pwd, encoding));
                    addPassword(passwords, encodedPwd);
                } catch (IOException var11) {
                }
            }
        }

        addPassword(passwords, pwd);
        char[] encodedPwd;
        if (this.mConsoleEncoding != null) {
            try {
                encodedPwd = castBytesToChars(encodePassword(pwd, this.mConsoleEncoding));
                addPassword(passwords, encodedPwd);
            } catch (IOException var10) {
            }
        }

        try {
            encodedPwd = castBytesToChars(encodePassword(pwd, Charset.defaultCharset()));
            addPassword(passwords, encodedPwd);
        } catch (IOException var9) {
        }

    }

    private static void addPassword(List<char[]> passwords, char[] password) {
        Iterator var2 = passwords.iterator();

        char[] existingPassword;
        do {
            if (!var2.hasNext()) {
                passwords.add(password);
                return;
            }

            existingPassword = (char[])var2.next();
        } while(!Arrays.equals(password, existingPassword));

    }

    private static byte[] encodePassword(char[] pwd, Charset cs) throws IOException {
        ByteBuffer pwdBytes = cs.newEncoder().onMalformedInput(CodingErrorAction.REPLACE).onUnmappableCharacter(CodingErrorAction.REPLACE).encode(CharBuffer.wrap(pwd));
        byte[] encoded = new byte[pwdBytes.remaining()];
        pwdBytes.get(encoded);
        return encoded;
    }

    private static char[] decodePassword(byte[] pwdBytes, Charset encoding) throws IOException {
        CharBuffer pwdChars = encoding.newDecoder().onMalformedInput(CodingErrorAction.REPLACE).onUnmappableCharacter(CodingErrorAction.REPLACE).decode(ByteBuffer.wrap(pwdBytes));
        char[] result = new char[pwdChars.remaining()];
        pwdChars.get(result);
        return result;
    }

    private static char[] castBytesToChars(byte[] bytes) {
        if (bytes == null) {
            return null;
        } else {
            char[] chars = new char[bytes.length];

            for(int i = 0; i < bytes.length; ++i) {
                chars[i] = (char)(bytes[i] & 255);
            }

            return chars;
        }
    }

    private static boolean isJava9OrHigherErrOnTheSideOfCaution() {
        String versionString = System.getProperty("java.specification.version");
        if (versionString == null) {
            return true;
        } else {
            return !versionString.startsWith("1.");
        }
    }

    private static Charset getConsoleEncoding() {
        if (isJava9OrHigherErrOnTheSideOfCaution()) {
            return null;
        } else {
            String consoleCharsetName = null;

            try {
                Method encodingMethod = Console.class.getDeclaredMethod("encoding");
                encodingMethod.setAccessible(true);
                consoleCharsetName = (String)encodingMethod.invoke((Object)null);
            } catch (ReflectiveOperationException var3) {
                return null;
            }

            if (consoleCharsetName == null) {
                return Charset.defaultCharset();
            } else {
                try {
                    return getCharsetByName(consoleCharsetName);
                } catch (IllegalArgumentException var2) {
                    return null;
                }
            }
        }
    }

    public static Charset getCharsetByName(String charsetName) throws IllegalArgumentException {
        return "cp65001".equalsIgnoreCase(charsetName) ? StandardCharsets.UTF_8 : Charset.forName(charsetName);
    }

    private static byte[] readEncodedPassword(InputStream in) throws IOException {
        ByteArrayOutputStream result;
        int b;
        for(result = new ByteArrayOutputStream(); (b = ((InputStream)in).read()) != -1 && b != 10; result.write(b)) {
            if (b == 13) {
                int next = ((InputStream)in).read();
                if (next == -1 || next == 10) {
                    break;
                }

                if (!(in instanceof PushbackInputStream)) {
                    in = new PushbackInputStream((InputStream)in);
                }

                ((PushbackInputStream)in).unread(next);
            }
        }

        return result.toByteArray();
    }

    private void assertNotClosed() {
        if (this.mClosed) {
            throw new IllegalStateException("Closed");
        }
    }

    public void close() {
        Iterator var1 = this.mFileInputStreams.values().iterator();

        while(var1.hasNext()) {
            InputStream in = (InputStream)var1.next();

            try {
                in.close();
            } catch (IOException var4) {
            }
        }

        this.mFileInputStreams.clear();
        this.mClosed = true;
    }
}
