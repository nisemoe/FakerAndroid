package com.facker.toolchain.arsc;

import java.io.IOException;

public interface BuildAble {
    byte[] build() throws IOException;
}
