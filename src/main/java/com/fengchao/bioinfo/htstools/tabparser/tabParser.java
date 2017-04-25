package edu.marquette.biology.andersonlab.tabparser;

import java.io.IOException;

import edu.marquette.biology.andersonlab.domain.BedRecord;

/** interrface for tab-delimited text file
 * @author Fengchao
 *
 */
public interface tabParser {

	BedRecord getNextRecord() throws IOException;

	void changeCoordinate(int s, int e);

}