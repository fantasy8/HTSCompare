package com.fengchao.bioinfo.htstools.logic;
import com.fengchao.bioinfo.htstools.domain.BedRecord;
import htsjdk.samtools.SAMFileReader;
import htsjdk.samtools.SAMRecord;
import htsjdk.samtools.util.CloseableIterator;

import java.util.ArrayList;
import java.util.Iterator;



/**
 * @author Fengchao
 * An utility class to query the SAM Record. 
 */
public class QuerySAM {

	/**
	 * @param bedrcd  a BedRecord to provide the coordinates for query
	 * @param samrdr  input SAM file for query
	 * @param offs  offset bp for the start region. - means upstream and + means downstream
	 * @param offe  offset bp for the end region
	 * @param tpr_flag  three prime reads selection
	 * @param strand_flag   strand specific selection
	 * @return
	 */
	public static ArrayList<SAMRecord> querySam(BedRecord bedrcd, SAMFileReader samrdr, int offs, int offe, boolean tpr_flag, boolean strand_flag){
		CloseableIterator<SAMRecord> iter = null;
		ArrayList<SAMRecord> samrlist = new ArrayList<SAMRecord>();
		SAMRecord samrtemp = null;
		int newstart=0;
		int newend=0;
		//		record.changeStartEnd(offs, offe); disabled due to calculation of distance 

		//change offset for downstream and upstream query
//		try {
//			bedrcd.printall();
//		} catch (IllegalArgumentException | IllegalAccessException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		if(bedrcd.getStrand().equals("+")){
			newstart=bedrcd.getStart()-offs;
			newend=bedrcd.getStart()+offs;
		}else if (bedrcd.getStrand().equals("-")){ 
			newstart=bedrcd.getEnd()-offs;
			newend=bedrcd.getEnd()+offs;
		}//this section is really tricky


		iter = samrdr.query(bedrcd.getChrom(), newstart, newend, false); //If true, each SAMRecord returned is will have its alignment completely contained in the interval of interest. If false, the alignment of the returned SAMRecords need only overlap the interval of interest.
		if (iter.hasNext()) {
			while (iter.hasNext()) {
				samrlist.add(iter.next());
			}
			iter.close();

//			if (tpr_flag == true) {
//				Iterator<SAMRecord> it = samrlist.iterator();
//				while (it.hasNext()) {
//					if (it.next().getFirstOfPairFlag() == true) {
//						it.remove();
//					}
//				}
//			}

			if(strand_flag==true){
				Iterator<SAMRecord> it = samrlist.iterator();
				if(bedrcd.getStrand()=="-"){
					while(it.hasNext()){
						if(it.next().getReadNegativeStrandFlag()==false){
							it.remove();
						}
					}
				} else if(bedrcd.getStrand()=="+"){
					while(it.hasNext()){
						if(it.next().getReadNegativeStrandFlag()==true){
							it.remove();
						}
					}
				}
			}

		}
		iter.close();
		return samrlist;
	}

}
