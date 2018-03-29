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

import java.io.BufferedReader;
import java.io.IOException;

/**
 * Class {@code VcfSnpInfo} is used for counting how many SNPs in VCF
 * and how many lines to skip.
 *
 * @author Xin Huang {@code <xin.huang07@gmail.com>}
 */
public final class VcfSnpInfo extends SnpInfo {

    /**
     * Constructor of {@code VcfSnpInfo}.
     *
     * @param snpFileName the name of a VCF file
     */
    public VcfSnpInfo(String snpFileName) {
        super(snpFileName);
        try (BufferedReader br = getBufferedReader(snpFileName)) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.startsWith("#")) skip++;
                else break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void open() {
        this.br = getBufferedReader(snpFileName);
        try {
            for (int i = 0; i < skip; i++)
                br.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void parseLine(String line) {
    }
}
