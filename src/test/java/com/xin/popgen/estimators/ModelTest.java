/*
    Copyright 2018 Xin Huang

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
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
