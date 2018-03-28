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
package com.xin.popgen.estimators;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ModelTest {

    @Test
    public void testCalLogOdds() {
        // rs1800407 YRI-CEU
        assertEquals(-3.863523, Model.calLogOdds(290, 0, 207, 17), 0.000001);
        // rs1800407 YRI-CHS
        assertEquals(-1.680897, Model.calLogOdds(290, 0, 486, 4), 0.000001);
        // rs1800407 CEU-CHS
        assertEquals(2.182626, Model.calLogOdds(207, 17, 486, 4), 0.000001);
    }

    @Test
    public void testCalVarLogOdds() {
        // rs1800407 YRI-CEU
        assertEquals(2.067103, Model.calVarLogOdds(290, 0, 207, 17), 0.000001);
        // rs1800407 YRI-CHS
        assertEquals(2.227728, Model.calVarLogOdds(290, 0, 486, 4), 0.000001);
        // rs1800407 CEU-CHS
        assertEquals(0.287934, Model.calVarLogOdds(207, 17, 486, 4), 0.000001);
    }

    @Test
    public void testCalVarOmega() {
        // rs1800407 YRI-CEU
        assertEquals(30.7390, Model.calVarOmega(290, 0, 207, 17), 0.0001);
        // rs1800407 YRI-CHS
        assertEquals(3.9819, Model.calVarOmega(290, 0, 486, 4), 0.0001);
        // rs1800407 CEU-CHS
        assertEquals(10.1820, Model.calVarOmega(207, 17, 486, 4), 0.0001);
    }

}
