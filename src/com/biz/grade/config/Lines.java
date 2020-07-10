package com.biz.grade.config;

public class Lines {

	public static String dLine=""; //여긴 파이널 안씀
	public static String sLine="";
	
	static { //static 클래스
		
		for(int i=0; i< 100 ; i++) {
			
			dLine += "=";
			sLine += "-";
			
		}
	}
}
