package com.fengchao.bioinfo.htstools.tabparser;

import com.fengchao.bioinfo.htstools.domain.BedRecord;

import java.io.BufferedReader;
import java.io.IOException;

/**
 * Created by zhang on 2017/5/3.
 */
public class TabParserBedImpl implements ITabParser {

    private BufferedReader in;
    private final String SEPERATOR = " ";

    private TabParserBedImpl() {

    }

    public TabParserBedImpl(BufferedReader infile) {
        in = infile;
    }

    public BedRecord getNextRecord() throws IOException {
        String line;
        BedRecord bedrecord = new BedRecord();

        if ((line = in.readLine()) != null) {
            String tokens[] = line.split(SEPERATOR);
            String chrom = tokens[0];
            int start = Integer.parseInt(tokens[1]);
            int end = Integer.parseInt(tokens[2]);
            String name = tokens[3];
            int score = 0;
            String strand = tokens[5];
            String extra1 = tokens[0];

            bedrecord.setChrom(chrom);
            bedrecord.setStart(start);
            bedrecord.setEnd(end);
            bedrecord.setName(name);
            bedrecord.setScore(score);
            bedrecord.setStrand(strand);
            bedrecord.setExtra1(extra1);
            return bedrecord;
        } else{
            return null;
        }

    }
}
