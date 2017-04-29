package com.fengchao.bioinfo.htstools.tabparser;

import com.fengchao.bioinfo.htstools.domain.BedRecord;

import java.io.IOException;


/** Interface for tab-delimited text file reader
 *  @author Fengchao
 *
 */
public interface ITabParser {

	BedRecord getNextRecord() throws IOException;

	void changeCoordinate(int s, int e);

}