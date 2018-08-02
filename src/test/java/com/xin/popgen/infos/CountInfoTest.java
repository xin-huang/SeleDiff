package com.xin.popgen.infos;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class CountInfoTest {

    private final CountInfo countInfo = new CountInfo("examples/data/example.count");

    @Test
    public void testGetPopInfo() {
        PopInfo popInfo = countInfo.getPopInfo();
        assertEquals(3, popInfo.getPopNum());
    }

    @Test
    public void testCountAlleles() {
        // the first SNP
        int[][] alleleCounts = countInfo.countAlleles();
        // YRI
        assertEquals(33, alleleCounts[0][0]);
        assertEquals(257, alleleCounts[0][1]);
        // CEU
        assertEquals(140, alleleCounts[1][0]);
        assertEquals(84, alleleCounts[1][1]);
        // CHS
        assertEquals(360, alleleCounts[2][0]);
        assertEquals(130, alleleCounts[2][1]);

        assertEquals("1\t908247\trs13303118\tT\tG", countInfo.getSnpInfo());

        countInfo.close();
    }

}
