package edu.marquette.biology.andersonlab.tabparser;

import java.io.BufferedReader;
import java.io.IOException;

import edu.marquette.biology.andersonlab.domain.BedRecord;

/**
 * GFF3 file DAO
 * 
 * @author Fengchao
 * 
 */
public class Gff3impl implements tabParser {

	BufferedReader in;

	public Gff3impl() {

		System.out.println("No input Bed files!");
	}

	public Gff3impl(BufferedReader infile) {
		in = infile;
	}

	public BedRecord getNextRecord() throws IOException {
		String line;
		BedRecord bedrecord = new BedRecord();

		if ((line = in.readLine()) != null) {
			String tokens[] = line.split("[\t]");
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

	public void changeCoordinate(int s, int e) {

	}

}
