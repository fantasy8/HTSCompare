package com.fengchao.bioinfo.htstools;
import com.fengchao.bioinfo.htstools.domain.BedRecord;
import com.fengchao.bioinfo.htstools.logic.QuerySAM;
import com.fengchao.bioinfo.htstools.tabparser.TabParserGff3Impl;
import com.fengchao.bioinfo.htstools.tabparser.ITabParser;
import htsjdk.samtools.SAMFileReader;
import htsjdk.samtools.SAMRecord;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;



/**	main program for intersecting the BAM data.
 * @author Fengchao
 * 
 */
public class MiRComp {

	public static void main(String[] args) {
		// fixed for testing purpose
		//File resultfile = new File("C:/Users/Fengchao/Desktop/IASearch/ctrl2_20141130_02.txt");
		File resultfile = new File("C:/lab/mtr42_sorted.txt");
		File bedfile = new File("D:/CloudDrives/Dropbox/IASearch/refseq.gtf");
		//File samfile = new File("D:/CloudDrives/Dropbox/IASearch/mtr42_sorted_F_2_20141201.bam");
		File samfile = new File("C:/lab/mtr42_sorted.bam");

		try {
			miRCompare(samfile, bedfile, resultfile);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
		}
	}

	/** 
	 * @param samfl SAM/BAM input
	 * @param bedfl BED input
	 * @param resultfl output result file
	 * @throws Exception
	 */
	public static void miRCompare(File samfl, File bedfl, File resultfl)
			throws Exception {

		SAMFileReader samfilereader = new SAMFileReader(samfl);
		// need to integrate this part into parser
		FileReader in = new FileReader(bedfl);
		BufferedReader bedbf = new BufferedReader(in);
		ITabParser gffinput = new TabParserGff3Impl(bedbf);

		FileWriter resultoutput = new FileWriter(resultfl);

		Hashtable<String, ArrayList<Double>> resulttbl = new Hashtable<String, ArrayList<Double>>();

		Hashtable<String, ArrayList<String>> readstbl = new Hashtable<String, ArrayList<String>>();

		BedRecord bedrecord = null;
		BedRecord record = new BedRecord();

		// ArrayList<SAMRecord> arrayl = new ArrayList<SAMRecord>();

		while ((bedrecord = gffinput.getNextRecord()) != null) {

//			 System.out.println(bedrecord.getStart());
//			 System.out.println(bedrecord.getChrom());
//			 System.out.println(bedrecord.getName());

			ArrayList<SAMRecord> arrayl = QuerySAM.querySam(bedrecord, samfilereader,
					10, 0, true, true);
			double distance = 0;
			if (arrayl.size()>0) {
				for (SAMRecord samrecord : arrayl) {
					distance += calcDistance(samrecord, bedrecord);
					// construct read-miRNA table
					if (readstbl.containsKey(samrecord.getReadName())) {
						readstbl.get(samrecord.getReadName()).add(
								bedrecord.getName());
					} else {
						ArrayList<String> namearrltemp = new ArrayList<String>();
						namearrltemp.add(bedrecord.getName());
						readstbl.put(samrecord.getReadName(), namearrltemp);
					}
				}
			}
			//System.out.println(distance);

			if (arrayl.size() != 0) {
				distance = distance / arrayl.size();
			}
			//prepare result table
			ArrayList<Double> data = new ArrayList<Double>();
			data.add((double) arrayl.size());
			data.add(distance);
			resulttbl.put(bedrecord.getName(), data);
		}
		// calculate adjusted miRNA count
		Iterator<ArrayList<String>> readsit = readstbl.values()
				.iterator();
		while (readsit.hasNext()) {
			ArrayList<String> namearry = readsit.next();
			Iterator<String> mirit = namearry.iterator();
			mirit.next();
			while (mirit.hasNext()) {
				String miname = mirit.next();
				double counttemp = resulttbl.get(miname).get(0);
				counttemp = counttemp - 1;
				resulttbl.get(miname).set(0, counttemp);
			}
		}
		//System.out.print(bedrecord.getName());
		// System.out.print(" ");
		// System.out.println(arrayl.size());
		// System.out.print(" ");
		// System.out.println(distance);
		// System.out.println("--------------");

		Iterator<String> resultit = resulttbl.keySet().iterator();
		while (resultit.hasNext()) {
			String namekey = resultit.next();
			StringBuilder sb = new StringBuilder();
			sb.append(namekey);
			sb.append("\t");
			sb.append(resulttbl.get(namekey).get(0));
			sb.append("\t");
			sb.append(resulttbl.get(namekey).get(1));
			sb.append("\r\n");
			resultoutput.write(sb.toString());

		}

		// Test code
		// record.setChrom("chrX");
		// record.setStart(53053755);
		// record.setEnd(53054349);
		// record.setStrand("-");
		// ArrayList<SAMRecord> arrayl= QuerySAM.querySam(record, saminput, 0,
		// 0,true,true);
		// for(SAMRecord samrecord:arrayl){
		// System.out.println(samrecord.getReadName());
		// }
		// System.out.print(arrayl.size());
		// System.out.print(arrayl.get(1).getFirstOfPairFlag());
		// System.out.print(arrayl.get(1).getReadName()); 
		resultoutput.close();
		bedbf.close();
		in.close();
	}

	/**
	 * calculate the average distance of each read to the drosha clevage site
	 * 
	 * @param samr
	 * @param bedr
	 * @return
	 */
	public static int calcDistance(SAMRecord samr, BedRecord bedr) {
		int dist = 0;
		if (bedr.getStrand().equals("+")) {
			dist = samr.getAlignmentEnd() - bedr.getStart();
		}
		if (bedr.getStrand().equals("-")) {
			dist = bedr.getEnd() - samr.getAlignmentStart();
		}
		return dist;
	}

}
