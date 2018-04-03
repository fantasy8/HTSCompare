package com.fengchao.bioinfo.htstools;

import com.fengchao.bioinfo.htstools.domain.BedRecord;
import com.fengchao.bioinfo.htstools.logic.QuerySAMUtil;
import com.fengchao.bioinfo.htstools.tabparser.ITabParser;
import com.fengchao.bioinfo.htstools.tabparser.TabParserGff3Impl;
import htsjdk.samtools.SAMFileReader;
import htsjdk.samtools.SAMRecord;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.*;


/**
 * Main program for intersecting the BAM data.
 * @author Fengchao
 *
 * Specs for input files
 * Reference file: The genome coordiates of the gene of interest. It should use the original offset range rather than
 * the adjusted one. This program will do the adjustment according to the strand and the provided up/down-stream
 * offsets.
 *
 */
public class HtsReadsCounter {

  private static boolean adjustDoubleCountedReads = false;

  public static void main(String[] args) {
    if(args.length != 4) {
      throw new IllegalArgumentException("Must have 3 input paths: result file, genome reference file, sequence file, adjust double counted reads.");
    }

    File resultFile = new File(args[0]);
    File genomeRefFile = new File(args[1]);
    File seqFile = new File(args[2]);
    adjustDoubleCountedReads = Boolean.parseBoolean(args[3]);

    try {
      startComparison(seqFile, genomeRefFile, resultFile);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * Generate the abundance file
   * @param resultFile SAM/BAM input
   * @param genomeRefFile BED input
   * @param seqFile output result file
   * @throws Exception exception
   */
  private static void startComparison(File resultFile, File genomeRefFile, File seqFile) throws Exception {

    SAMFileReader samfilereader = new SAMFileReader(resultFile);
    FileReader genomeFileReader = new FileReader(genomeRefFile);
    BufferedReader bufferedGenomeFileReader = new BufferedReader(genomeFileReader);
    ITabParser referenceFile = new TabParserGff3Impl(bufferedGenomeFileReader);
    FileWriter resultFileWriter = new FileWriter(seqFile);
    Map<String, ArrayList<Double>> resultMap = new HashMap<String, ArrayList<Double>>();
    //Key: reads' name, value: reference entry's name
    HashMap<String, ArrayList<String>> readsMap = new HashMap<String, ArrayList<String>>();

    BedRecord refEntry;
    while ((refEntry = referenceFile.getNextRecord()) != null) {
      List<SAMRecord> matchedReads = QuerySAMUtil.querySam(refEntry, samfilereader,
          10, 0, true, true);
      double distance = 0;
      if (matchedReads.size() > 0) {
        for (SAMRecord sequenceRead : matchedReads) {
          distance += calcDistance(sequenceRead, refEntry);
          // construct read-miRNA table
          if (readsMap.containsKey(sequenceRead.getReadName())) {
            readsMap.get(sequenceRead.getReadName()).add(
                refEntry.getName());
          } else {
            ArrayList<String> refEntryArray = new ArrayList<String>();
            refEntryArray.add(refEntry.getName());
            readsMap.put(sequenceRead.getReadName(), refEntryArray);
          }
        }
      }

      if (matchedReads.size() != 0) {
        distance = distance / matchedReads.size();
      }
      //prepare result table
      ArrayList<Double> resultColumns = new ArrayList<Double>();
      //First column is the number of matched reads
      resultColumns.add((double) matchedReads.size());
      //Second column is the distance
      resultColumns.add(distance);
      resultMap.put(refEntry.getName(), resultColumns);
    }


    //Adjust double counted reads
    if(adjustDoubleCountedReads) {
      Iterator<ArrayList<String>> it = readsMap.values().iterator();
      while (it.hasNext()) {
        ArrayList<String> refEntryList = it.next();
        Iterator<String> refEntryIterator = refEntryList.iterator();
        refEntryIterator.next();
        while (refEntryIterator.hasNext()) {
          String entryName = refEntryIterator.next();
          double counttemp = resultMap.get(entryName).get(0);
          counttemp = counttemp - 1;
          resultMap.get(entryName).set(0, counttemp);
        }
      }
    }

    Iterator<String> resultIterator = resultMap.keySet().iterator();
    while (resultIterator.hasNext()) {
      String nameKey = resultIterator.next();
      StringBuilder sb = new StringBuilder();
      sb.append(nameKey);
      sb.append("\t");
      sb.append(resultMap.get(nameKey).get(0));
      sb.append("\t");
      sb.append(resultMap.get(nameKey).get(1));
      sb.append("\r\n");
      resultFileWriter.write(sb.toString());
    }

    // Test code
    // record.setChrom("chrX");
    // record.setStart(53053755);
    // record.setEnd(53054349);
    // record.setStrand("-");
    // ArrayList<SAMRecord> arrayl= QuerySAMUtil.querySam(record, saminput, 0,
    // 0,true,true);
    // for(SAMRecord samrecord:arrayl){
    // System.out.println(samrecord.getReadName());
    // }
    // System.out.print(arrayl.size());
    // System.out.print(arrayl.get(1).getFirstOfPairFlag());
    // System.out.print(arrayl.get(1).getReadName());
    resultFileWriter.close();
    bufferedGenomeFileReader.close();
    genomeFileReader.close();
  }

  /**
   * calculate the average distance of each read to the drosha clevage site
   *
   * @param samr
   * @param bedr
   * @return
   */
  private static int calcDistance(SAMRecord samr, BedRecord bedr) {
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
