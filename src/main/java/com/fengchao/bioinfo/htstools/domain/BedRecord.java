package com.fengchao.bioinfo.htstools.domain;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author Fengchao
 * Container for a Bed record
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

	//failed because not all methods are accessers, should use getDeclaredMethods() 
	//	public void printall() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException{
	//		Class clazz = this.getClass();
	//		Method[] methods = clazz.getDeclaredMethods();
	//		for(Method method:methods){
	//			System.out.println(method.invoke(this));
	//		}
	//	}

	/**	print all properties of a BED reord
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	public void printall() throws IllegalArgumentException, IllegalAccessException{
		Class clazz =this.getClass();
		Field[] fields = clazz.getDeclaredFields();
		StringBuilder out = new StringBuilder();
		for (Field field:fields){
			out.append(field.get(this).toString());
			out.append(" ");
		}
		System.out.print(out);
		System.out.println();
	}
	/** change the start and end of a BED record
	 * @param offsets	the start coordinate
	 * @param offsete	the end coordinate
	 */
	public void changeStartEnd(int offsets,int offsete){
		if (strand=="+"){
			start=start+offsets;
			end=end+offsete;
		} else{
			start=start-offsets;
			end=end-offsete;
		}

		if (start<=0) start=0;
		if (end<=0) end = 0;
	}




}
