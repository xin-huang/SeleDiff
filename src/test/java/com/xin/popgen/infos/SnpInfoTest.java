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
package com.xin.popgen.infos;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class SnpInfoTest {

    private final SnpInfo snpInfo = new SnpInfo("examples/data/example.snp");
    private final SnpInfo gzSnpInfo = new SnpInfo("examples/compressed_data/example.snp.gz");


    @Test
    public void testGet() {
        // test uncompressed data
        assertEquals("rs13303118\tT\tG", snpInfo.get());
        snpInfo.close();

        // test compressed data
        assertEquals("rs13303118\tT\tG", gzSnpInfo.get());
        gzSnpInfo.close();
    }

}
