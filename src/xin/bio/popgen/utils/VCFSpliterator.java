/*
  Copyright (C) 2017 Xin Huang

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
package xin.bio.popgen.utils;

import java.io.BufferedReader;
import java.util.function.Consumer;

/**
 * 
 * @author Xin Huang {@code <huangxin@picb.ac.cn>}
 *
 */
public class VCFSpliterator extends FixedBatchSpliteratorBase<String> {
	
	private final BufferedReader br;
	
	VCFSpliterator(BufferedReader br, int batchSize) {
		super(IMMUTABLE | ORDERED | NONNULL, batchSize);
		this.br = br;
	}
	
	public VCFSpliterator(BufferedReader br) {
		this(br, 128);
	}

	@Override
	public boolean tryAdvance(Consumer<? super String> action) {
		if (action == null)
			throw new NullPointerException();
		try {
			final String line = br.readLine();
			if (line == null) return false;
			action.accept(line);
			return true;
		} catch (RuntimeException e) {
			throw e;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	@Override
	public void forEachRemaining(Consumer<? super String> action) {
		if (action == null)
			throw new NullPointerException();
		try {
			for (String line; (line = br.readLine()) != null; ) action.accept(line);
		} catch (RuntimeException e) {
			throw e;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
