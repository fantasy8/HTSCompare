import com.fengchao.bioinfo.htstools.domain.BedRecord;
import com.fengchao.bioinfo.htstools.logic.QuerySAM;
import htsjdk.samtools.SAMFileReader;
import htsjdk.samtools.SAMRecord;
import htsjdk.samtools.SAMRecordIterator;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;



public class QuerySamTest {

	@Test
	public void test() throws IOException, IllegalArgumentException, IllegalAccessException {
		//		gff3 parser test
		//		File gff3file = new File("I:/commons/mm10alignment_2/mmu.gff3");
		//		FileReader gff3flrdr = new FileReader(gff3file);
		//		BufferedReader gff3bf = new BufferedReader(gff3flrdr);
		//		TabParserGff3Impl gff3input = new TabParserGff3Impl(gff3bf);
		//		BedRecord bed = gff3input.getNextRecord();
		//		bed.printall();
		ArrayList<SAMRecord> samarrl= new ArrayList<SAMRecord>();
		BedRecord bed = new BedRecord();
		bed.setChrom("chr9");
		bed.setStart(122669886);
		bed.setEnd(122672448);
		bed.setStrand("+");
		SAMRecordIterator samit = null;
		File samfile = new File("C:/Users/Fengchao/Desktop/IASearch/mtr42_sorted_F_2.bam");
		SAMFileReader samfilereader = new SAMFileReader(samfile);

		samarrl = QuerySAM.querySam(bed, samfilereader, 0, 0, true, true);

		System.out.println(samarrl.size());

	}

}
