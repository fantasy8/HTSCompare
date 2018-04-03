package com.fengchao.bioinfo.htstools.tabparser;

import com.fengchao.bioinfo.htstools.domain.BedRecord;

import java.io.BufferedReader;
import java.io.IOException;

/**
 * GFF3 file DAO
 * 
 * @author Fengchao
 * 
 */
public class TabParserGff3Impl implements ITabParser {

	private BufferedReader in;

	public TabParserGff3Impl() {

		System.out.println("No input Bed files!");
	}

	public TabParserGff3Impl(BufferedReader infile) {
		in = infile;
	}

	public BedRecord getNextRecord() throws IOException {
		String line;
		BedRecord bedrecord = new BedRecord();

		if ((line = in.readLine()) != null) {
			String tokens[] = line.split(",");
			String chrom = tokens[0];
			int start = Integer.parseInt(tokens[3]);
			int end = Integer.parseInt(tokens[4]);
			String name = tokens[8];
			int score = 0;
			String strand = tokens[6];
			String extra1 = tokens[2];

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
