package com.fengchao.bioinfo.htstools.domain;

/**
 * @author Fengchao
 * POJO for BED records
 *
 */
public class BedRecord {

    private String chrom;
    private int start;
    private int end;
    private String name;
    private int score;
    private String strand;
    private String extra1;
    private String extra2;

    /**
     * Initialize a BED Record
     */
    public BedRecord(){
        chrom = "chr?";
        start = 0;
        end = 0;
        name = "unknown";
        score = 0;
        strand = "unknown";
        extra1= "empty";
        extra2= "empty";
    }

    public String getChrom() {
        return chrom;
    }

    public void setChrom(String chrom) {
        this.chrom = chrom;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getEnd() {
        return end;
    }

    public void setEnd(int end) {
        this.end = end;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getStrand() {
        return strand;
    }

    public void setStrand(String strand) {
        this.strand = strand;
    }

    public String getExtra1() {
        return extra1;
    }

    public void setExtra1(String extra1) {
        this.extra1 = extra1;
    }

    public String getExtra2() {
        return extra2;
    }

    public void setExtra2(String extra2) {
        this.extra2 = extra2;
    }
}
