package com.intellij.whileLang;

import org.jetbrains.org.objectweb.asm.MethodVisitor;

import java.io.IOException;

/**
 * Created by Mikhail on 06.11.2015.
 */
public class Test {

    int fd = 11;
    public int test() throws IOException {
        int x = 25;
        int c = 3;
        int z = 1;
        if (x >= c && x > z) {z = x - c;} else { z = x + c;}
        System.out.println(c);
        return x+z-c;
    }
    public boolean test2() {
        int x = 0;
        while (x < 5) x++;
        return true;
    }
}

