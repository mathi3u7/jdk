/*
 * Copyright (c) 2009, 2024, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
 * or visit www.oracle.com if you need additional information or have any
 * questions.
 */

import java.lang.annotation.*;
import java.io.*;
import java.net.URL;
import java.util.List;

import java.lang.classfile.*;

/*
 * @test Wildcards
 * @bug 6843077 8006775
 * @summary test that annotations target wildcards get emitted to classfile
 */
public class Wildcards extends ClassfileTestHelper {
    public static void main(String[] args) throws Exception {
        new Wildcards().run();
    }

    public void run() throws Exception {
        expected_tinvisibles = 3;
        expected_tvisibles = 0;

        ClassModel cm = getClassFile("Wildcards$Test.class");
        test(cm);
        for (FieldModel fm : cm.fields()) {
            test(fm);
        }
        for (MethodModel mm: cm.methods()) {
            test(mm,false);
        }

        countAnnotations();

        if (errors > 0)
            throw new Exception(errors + " errors found");
        System.out.println("PASSED");
    }

    /*********************** Test class *************************/
    static class Test {
        @Target({ElementType.TYPE_USE, ElementType.TYPE_PARAMETER})
        @interface A {}

        List<? extends @A Number> f;

        List<? extends @A Object> test(List<? extends @A Number> p) {
            List<? extends @A Object> l;  // not counted... gets optimized away
            return null;
        }
    }
}
