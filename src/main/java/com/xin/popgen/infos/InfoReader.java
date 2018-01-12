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

public class InfoReader implements Info {
	
	private final String fileName;
	
	public InfoReader(String fileName) {
		this.fileName = fileName;
	}

	@Override
	public void parseLine(String line) {}
	
	public BufferedReader getBufferedReader() {
		return getBufferedReader(fileName);
	}

}
