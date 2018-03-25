package com.xin.popgen.infos;

import java.io.IOException;

public final class VcfSnpInfo extends SnpInfo {



    /**
     * Constructor of {@code SnpInfo}.
     *
     * @param snpFileName the name of an EIGENSTRAT SNP file
     */
    public VcfSnpInfo(String snpFileName) {
        super(snpFileName);
    }

    @Override
    /**
     * Returns the information of a SNP.
     * @return the SNP ID, the reference allele, and the alternative allele
     */
    public String get() {
        String info = null;
        try {
            String line = br.readLine();
            if (line != null) {
                int end = 0;
                for (int i = 0; i < 2; i++) {
                    end = line.indexOf("\t", end+1);
                }
                int start = end + 1;
                for (int i = 0; i < 3; i++) {
                    end = line.indexOf("\t", end+1);
                }
                info = line.substring(start, end);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return info;
    }

    @Override
    /**
     * Open the file storing SNP information.
     */
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
        if (line.startsWith("#"))
            skip++;
        else
            snpNum++;
    }
}
