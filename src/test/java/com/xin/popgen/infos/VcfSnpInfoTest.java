/*
	Copyright (c) 2018 Xin Huang

	This file is part of SeleDiff.

	Permission is hereby granted, free of charge, to any person obtaining a copy
	of this software and associated documentation files (the "Software"), to deal
	in the Software without restriction, including without limitation the rights
	to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
	copies of the Software, and to permit persons to whom the Software is
	furnished to do so, subject to the following conditions:

	The above copyright notice and this permission notice shall be included in all
	copies or substantial portions of the Software.

	THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
	IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
	FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
	AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
	LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
	OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
	SOFTWARE.
 */
package com.xin.popgen.infos;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class VcfSnpInfoTest {

    /*private final SnpInfo vcfSnpInfo = new VcfSnpInfo("examples/data/example.vcf");
    private final SnpInfo gzVcfSnpInfo = new VcfSnpInfo("examples/compressed_data/example.vcf.gz");

    @Test
    public void testGetSkipNum() {
        // test uncompressed data
        assertEquals(30, vcfSnpInfo.getSkipNum());

        // test compressed data
        assertEquals(30, gzVcfSnpInfo.getSkipNum());
    }

    @Test
    public void testGetSnpNum() {
        // test uncompressed data
        assertEquals(20309, vcfSnpInfo.getSnpNum());

        // test compressed data
        assertEquals(20309, gzVcfSnpInfo.getSnpNum());
    }

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void testReadFile() throws IOException {
        thrown.expect(IOException.class);
        vcfSnpInfo.readFile(new BufferedReader(new FileReader("test")));
    }

    @Test
    public void testGetBufferedReader1() {
        assertEquals(null,vcfSnpInfo.getBufferedReader(null));
    }

    @Test
    public void testGetBufferedReader2() throws NullPointerException {
        thrown.expect(NullPointerException.class);
        vcfSnpInfo.getBufferedReader("test");
    }

    @Test
    public void testOpen() {
        vcfSnpInfo.open();
        vcfSnpInfo.close();
    }*/

}
