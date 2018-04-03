package com.fengchao.bioinfo.htstools.logic;
import com.fengchao.bioinfo.htstools.domain.BedRecord;
import htsjdk.samtools.SAMFileReader;
import htsjdk.samtools.SAMRecord;
import htsjdk.samtools.util.CloseableIterator;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


/**
 * @author Fengchao
 * An utility class to query the SAM Record. 
 */
public class QuerySAMUtil {

  /**
   * @param refRecord  a BedRecord to provide the coordinates for query
   * @param seqFileReader  input SAM file for query
   * @param upstreamOffset  offset bp for the start region. - means upstream and + means downstream
   * @param downstreamOffset  offset bp for the end region
   * @param tpr_flag  three prime reads selection
   * @param strandFlag   strand specific selection
   * @return matchedReads a list of matched reads
   */
  public static List<SAMRecord> querySam(BedRecord refRecord, SAMFileReader seqFileReader, int upstreamOffset, int downstreamOffset, boolean tpr_flag, boolean strandFlag) {

    List<SAMRecord> matchedReads = new ArrayList<SAMRecord>();
    SAMRecord samrtemp = null;
    int adjustedStartOffset=0;
    int adjustedEndOffset=0;

    //Transform start and end offset for querying the actual genome sequence file.
    //This section is very tricky. Do not change if not necessary.
    if (refRecord.getStrand().equals("+")) {
      adjustedStartOffset = refRecord.getStart() - upstreamOffset;
      adjustedEndOffset = refRecord.getStart() + downstreamOffset;
    } else if (refRecord.getStrand().equals("-")) {
      adjustedStartOffset = refRecord.getEnd() - upstreamOffset;
      adjustedEndOffset = refRecord.getEnd() + upstreamOffset;
    }

    CloseableIterator<SAMRecord> it = seqFileReader.query(refRecord.getChrom(), adjustedStartOffset, adjustedEndOffset, false);
    //If true, each SAMRecord returned is will have its alignment completely contained in the interval of interest.
    //If false, the alignment of the returned SAMRecords need only overlap the interval of interest.

    while (it.hasNext()) {
      if(strandFlag && ("-".equals(refRecord.getStrand()) && it.next().getReadNegativeStrandFlag() == false)
          || ("+".equals(refRecord.getStrand()) && it.next().getReadNegativeStrandFlag() == true)){

      } else {
        matchedReads.add(it.next());
      }
    }
    it.close();

//			if (tpr_flag == true) {
//				Iterator<SAMRecord> it = matchedReads.iterator();
//				while (it.hasNext()) {
//					if (it.next().getFirstOfPairFlag() == true) {
//						it.remove();
//					}
//				}
//			}
    return matchedReads;
  }

}
