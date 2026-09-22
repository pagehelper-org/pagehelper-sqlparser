/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2014-2023 abel533@gmail.com
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.github.pagehelper.sql;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.parser.CountJSqlParser51;
import com.github.pagehelper.parser.CountSqlParser;
import com.github.pagehelper.parser.SqlParserUtil;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.select.Select;
import org.junit.Assert;
import org.junit.Test;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Set;

/**
 * Test cases for CountJSqlParser51, adapted from SqlTest.java for sqlparser4.7.
 */
public class SqlTest {

    CountSqlParser countSqlParser = new CountJSqlParser51();

    @Test
    public void testSqlParser() {
        // Example test case adapted for CountJSqlParser51
        Assert.assertEquals("SELECT count(0) FROM user",
                countSqlParser.getSmartCountSql("SELECT * FROM user"));
    }

    @Test
    public void testSmartCountSqlKeepsHintComment() {
        String countSql = countSqlParser.getSmartCountSql("/*+ INDEX(user idx_user_name) */ SELECT * FROM user");
        Assert.assertTrue(countSql.startsWith("/*+ INDEX(user idx_user_name) */"));
        Assert.assertTrue(countSql.toUpperCase().contains("COUNT(0)"));
    }

    @Test
    public void testCountParserBytecodeDoesNotReferenceSimpleNode() throws IOException {
        try (InputStream inputStream = CountJSqlParser51.class.getResourceAsStream("CountJSqlParser51.class")) {
            Assert.assertNotNull(inputStream);
            Set<String> constants = readConstantPoolUtf8(inputStream);
            Assert.assertFalse(constants.contains("net/sf/jsqlparser/parser/SimpleNode"));
        }
    }

    private Set<String> readConstantPoolUtf8(InputStream inputStream) throws IOException {
        DataInputStream classStream = new DataInputStream(inputStream);
        if (classStream.readInt() != 0xCAFEBABE) {
            throw new IllegalArgumentException("Invalid class file");
        }
        classStream.readUnsignedShort(); // minor version
        classStream.readUnsignedShort(); // major version
        int constantPoolCount = classStream.readUnsignedShort();
        Set<String> utf8Constants = new HashSet<>();
        for (int i = 1; i < constantPoolCount; i++) {
            int tag = classStream.readUnsignedByte();
            switch (tag) {
                case 1:
                    utf8Constants.add(classStream.readUTF());
                    break;
                case 3:
                case 4:
                    classStream.readInt();
                    break;
                case 5:
                case 6:
                    classStream.readLong();
                    i++;
                    break;
                case 7:
                case 8:
                case 16:
                case 19:
                case 20:
                    classStream.readUnsignedShort();
                    break;
                case 9:
                case 10:
                case 11:
                case 12:
                case 17:
                case 18:
                    classStream.readUnsignedShort();
                    classStream.readUnsignedShort();
                    break;
                case 15:
                    classStream.readUnsignedByte();
                    classStream.readUnsignedShort();
                    break;
                default:
                    throw new IllegalArgumentException("Unsupported constant pool tag: " + tag);
            }
        }
        return utf8Constants;
    }
}
