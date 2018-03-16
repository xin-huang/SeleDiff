/*
  Copyright (C) 2018 Xin Huang

  This file is part of SeleDiff.

  SeleDiff is free software: you can redistribute it and/or modify
  it under the terms of the GNU General Public License as published by
  the Free Software Foundation, either version 3 of the License, or
  (at your option) any later version

  SeleDiff is distributed in the hope that it will be useful,
  but WITHOUT ANY WARRANTY; without even the implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.

  You should have received a copy of the GNU General Public License
  along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.xin.popgen.infos;

import java.io.BufferedReader;

/**
 * Class {@code InfoReader} construct a BufferedReader
 * from a given file name.
 *
 * @author Xin Huang {@code <huangxin@picb.ac.cn>}
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
