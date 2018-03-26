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

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class SnpInfoTest {

    private final SnpInfo snpInfo = new SnpInfo("examples/data/example.snp");
    private final SnpInfo gzSnpInfo = new SnpInfo("examples/compressed_data/example.snp.gz");

    @Test
    public void testGetSkipNum() {
        // test uncompressed data
        assertEquals(0, snpInfo.getSkipNum());

        // test compressed data
        assertEquals(0, gzSnpInfo.getSkipNum());
    }

    @Test
    public void testGetSnpNum() {
        // test uncompressed data
        assertEquals(20309, snpInfo.getSnpNum());

        // test compressed data
        assertEquals(20309, gzSnpInfo.getSnpNum());
    }

    @Test
    public void testGet() {
        // test uncompressed data
        snpInfo.open();
        assertEquals("rs13303118\tT\tG", snpInfo.get());
        snpInfo.close();

        // test compressed data
        gzSnpInfo.open();
        assertEquals("rs13303118\tT\tG", gzSnpInfo.get());
        gzSnpInfo.close();
    }

}