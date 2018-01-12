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

public class CountSnpNumInfo implements Info {
	
	private int snpNum = 0;
	
	public CountSnpNumInfo(String snpFileName) {
		readFile(getBufferedReader(snpFileName));
		System.out.println(snpNum + " variants are read from " + snpFileName);
	}

	@Override
	public void parseLine(String line) {
		snpNum++;
	}
	
	public int getSnpNum() {
		return snpNum;
	}

}
