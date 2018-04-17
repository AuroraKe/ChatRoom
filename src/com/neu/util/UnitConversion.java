package com.neu.util;

import java.io.Serializable;
import java.text.DecimalFormat;

/**
 * Units conversion based on the CONVERSION_FACTOR you set.
 * Foe example, when the setting factor is 0.5, when the file 
 * size is 513 bytes, since 513 is divided by 1024 to more than 
 * 0.5, then the conversion is performed. When the size of file
 * is 511 bytes, since 511 is divided by 1024 to less than 0.5, 
 * then the conversion isn't performed. 
 * 
 * @author Petrichor
 */
public class UnitConversion implements Serializable{

	private static final long serialVersionUID = 2017100217050601L;

	/**
	 * The maximum bytes of the file to be transfered.
	 */
	static final long MAXIMUM_FILE_SIZE = 1l << 32; //aka 4GB
	
	/**
	 * The conversion factor is used when none is specified in constructor.
	 */
	static final float DEFAULT_CONVERSION_FACTOR = 0.5f;
	
	/**
	 * The conversion factor for the unit conversion. 
	 */
	final float conversionFactor;
	
	/**
	 * Constructs an UnitConversion with the specified conversion 
	 * factor.
	 * @param conversionFactor the conversion factor
	 */
	public UnitConversion(float conversionFactor){
		if(conversionFactor <= 0 || Float.isNaN(conversionFactor)){
			throw new IllegalArgumentException("Illegal conversion factor" +
					conversionFactor);
		}
		this.conversionFactor = conversionFactor;
	}
	
	/**
	 * Constructs an UnitConversion with the default conversion 
	 * factor (0.5) and the specified file size.
	 * 
	 * @param fileSize the file size
	 */
	public UnitConversion() {
		this(DEFAULT_CONVERSION_FACTOR);
	}
	
	/**
	 * Constructs an UnitConversion with the default conversion 
	 * factor (0.5) and the specified file size.
	 * 
	 * @param fileSize the file size
	 */
//	public UnitConversion(long fileSize) {
//		this(fileSize, DEFAULT_CONVERSION_FACTOR);
//	}
	
	
	public String conversion(long fileSize){
		if(fileSize <= 0 || fileSize > MAXIMUM_FILE_SIZE){
			throw new IllegalArgumentException("Can not send more than 4GB of files.");
		}
		StringBuffer buffer = new StringBuffer();
		DecimalFormat df = new DecimalFormat("0.00");
		double sizeKB = fileSize / 1024d; //单位转换成KB
		double sizeMB = sizeKB / 1024d; //单位转换成MB
		double sizeGB  = sizeMB / 1024d; //单位转换成GB
		if(sizeKB >= conversionFactor){
			if (sizeMB >= conversionFactor) {
				if (sizeGB >= conversionFactor) {
					buffer.append(df.format(sizeGB) + "GB");
				} else {
					buffer.append(df.format(sizeMB) + "MB");
				}
			} else {
				buffer.append(df.format(sizeKB) + "KB");
			}
		}else{
			buffer.append(fileSize + "B");
		}
		
		return buffer.toString();
	}
	
	
	public static void main(String[] args) {
		System.out.println(MAXIMUM_FILE_SIZE / 1024/1024/1024);
	}

}
