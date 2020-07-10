package com.biz.grade.exec;

import java.io.FileNotFoundException;
import java.io.PrintStream;

public class PrintStreamEx_01 {

	public static void main(String[] args) {

		String fileName = "src/main/com/biz/grade/exec/data/test.txt";

		PrintStream pStream = null;

		try {
			// PrintStream() 으로 파일을 기록하기 위해 open
			// 기존 파일이 삭제되고 새로 생성된다
			pStream = new PrintStream(fileName); // try-catch
			pStream.println("대한민국만세");
			pStream.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
