package com.biz.grade.exec;

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;

public class PrintWriterEx_01 {

	public static void main(String[] args) {

		String fileName = "src/com/biz/grade/exec/data/test1.txt";
		FileWriter fileWriter =null; //printwriter는 filewriter와 연동 filewriter로 일단 파일 오픈
		PrintWriter pWriter = null;

		try {
			// printwriter는 보통 단독으로 사용하지 않고 filewriter로 파일을 오픈 한 후
			// printwriter에 연결하여 사용한다
			// FileWriter로 파일을 open할 때 두번째 매개변수로 true를 주입하면 파일을 append mode로 연다
			// append mode로 오픈이 되면 기존에 저장된 내용을 삭제하지 않고 계속해서 문자열을 추가하는 상태로 변경된다.
			// 그래서 printwriter 씀
			fileWriter = new FileWriter(fileName,true); //true 쓰면 append mode로 한개씩 추가하는 기능이 됨
			pWriter = new PrintWriter(fileWriter); // try-catch
			
			Date date = new Date();
			pWriter.println("대한민국만세" + date.toString());
			
			// printwriter는 값을 저장하면 일단 임시 buffer에 보관이 된다
			// flush() method를 호출하여 buffer에 담긴 데이터를 파일로 보낸 후 close()해주어야 한다.
			pWriter.flush();
			pWriter.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 

	}
}
