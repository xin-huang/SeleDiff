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

/**
 * Class {@code InfoReader} construct a BufferedReader
 * from a given file name.
 *
 * @author Xin Huang {@code <xin.huang07@gmail.com>}
 */
public final class InfoReader implements Info {

	// a String stores a file name
	private final String fileName;

	/**
	 * Constructor of {@code InfoReader}.
	 *
	 * @param fileName a file name
	 */
	public InfoReader(String fileName) {
		this.fileName = fileName;
	}

	/**
	 * Returns a BufferedReader from the given file name.
	 *
	 * @return a BufferedReader corresponding to the given file name
	 */
	public BufferedReader getBufferedReader() {
		return getBufferedReader(fileName);
	}

	@Override
	public void parseLine(String line) {}

}
