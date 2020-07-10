package com.biz.grade.service;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.biz.grade.config.DBContract;
import com.biz.grade.config.Lines;
import com.biz.grade.domain.StudentVO;

public class StudentServiceImplV1 implements StudentService {

	private List<StudentVO> studentList;
	private Scanner scan;
	private String fileName; // 우리가 읽어야 할 파일

	public StudentServiceImplV1() {
		studentList = new ArrayList<>();
		scan = new Scanner(System.in);
		fileName = "src/com/biz/grade/exec/data/student.txt"; // 이렇게만 하면 준비가 안된 파일! 여기에 담아서 학번:이름:형식으로 만들겠다
	}

	// studentList를 외부에서 가져다 쓸 수 있도록 통로를 만들었음
	public List<StudentVO> getStudentList() {
		return studentList;

	}

	public StudentVO getStudent(String st_num) {

		// 1. studentVO를 null로 클리어, null값 studentVO에 할당
		StudentVO studentVO = null;
		
		// 2. studentList를 순서대로 뒤지면서 매개변수로 받은 st_num가 학생정보에서 나타나는지 검사
		for (StudentVO stVO : studentList) {
			
			if (stVO.getNum().equals(st_num)) { // stVO.getnum해서 받은거랑 매개변수로 받은 st_num이 같으면 studentvo에 넣어라
				// 없으면? 학번이 일치하는 사람 없으면 if문 거치지 않고 for문 빠져나가 return studentVO
				// studentVO는 위에서 null이라고 선언했으므로 학생정보가 없다는 뜻이다

				studentVO = stVO;
				break;

			}
			// 만약 studentList에서 해당학번을 못찾으면 반복문은 끝까지 진행할 것
		}
		// 만약 중간에 if, break를 만나고 for문이 중단된 상태라면 
		// studentVO에는 stVO가 담겨있을 것이고 for문이 반복진행한다는 것은 studentVO에는 null값이 담겨 있을 것
		return studentVO; // if문 안거친 것!! 리턴 타입이 null이면 학생정보 없는 것
	}

	@Override
	public void loadStudnet() {

		FileReader fileReader = null;
		BufferedReader buffer = null;

		try {
			fileReader = new FileReader(this.fileName);// 필드변수에 있는 거 가져다 쓸 때 이렇게! 마우스 올려서 서라운드 캐치 해주기
			buffer = new BufferedReader(fileReader);
			String reader = "";
			while (true) {
				reader = buffer.readLine(); // 마우스 올려서 예외조항 만들어 주기
				if (reader == null) {
					break;
				}

				String[] students = reader.split(":");
				// 학생정보를 VO에담기

				StudentVO studentVO = new StudentVO();
				studentVO.setNum(students[DBContract.STUDENT.ST_NUM]);
				studentVO.setName(students[DBContract.STUDENT.ST_NAME]);
				studentVO.setDept(students[DBContract.STUDENT.ST_DEPT]);
				studentVO.setGrade(Integer.valueOf(students[DBContract.STUDENT.ST_GRADE]));
				studentVO.setTel(students[DBContract.STUDENT.ST_TEL]);

				// 다 만들고 저장!
				studentList.add(studentVO);

			}
		} catch (FileNotFoundException e) {
			System.out.println("학생정보 파일 열기 오류!");

		} catch (IOException e) {
			System.out.println("학생정보 파일 읽기 오류!");
		}

	}

	@Override
	public boolean inputStudent() {
		StudentVO studentVO = new StudentVO();

		System.out.print("학번 (END:종료)>>"); // end쓰면 불린으로 바꿔줌
		// 변수명 명명규칙
		// Camel case : 두 단어 이상 사용할 때 단어 첫글자 대문자
		// snake case : 두 단어 이상 사용할 때 단어 사이를 언더스코어_로 지정

		String st_num = scan.nextLine(); // 학번을 문자열로 입력할 변수 st_num 선언 DB와 연동할 때는 변수이름 snake case로 씀
		if (st_num.equals("END")) {
			return false;
		}

		// 학번은 String인데 숫자로 입력해야 하니까 인티져 해줌
		int intNum = 0;
		try {
			intNum = Integer.valueOf(st_num);
		} catch (Exception e) {
			System.out.println("학번은 숫자만 가능!");
			System.out.println("입력한 문자열:" + st_num); // 문자열로 입력하지 마라
			return true;
		}

		if (intNum < 1 || intNum > 99999) {
			System.out.println("학번은 1~99999까지만 가능");
			System.out.println("다시 입력해 주세요");
			return true;
		}

		// 00001 형식으로 만들어라(마지막에)
		st_num = String.format("%05d", intNum); // intNum은 내가 숫자로 입력하는 값

		for (StudentVO sVO : studentList) {
			if (sVO.getNum().equals(st_num)) {
				System.out.println(st_num + "학생정보가 이미 등록되어 있습니다");
				return true;
			}
		}

		studentVO.setNum(st_num); // 내가 입력한 학번이 저장

		System.out.println("이름(END:종료) >>");
		String st_name = scan.nextLine();
		if (st_name.equals("END")) {
			return false;
		}
		studentVO.setName(st_name);

		System.out.println("학과(END:종료) >>");
		String st_dept = scan.nextLine();
		if (st_dept.equals("END")) {
			return false;
		}
		studentVO.setDept(st_dept);

		// 학년
		System.out.print("학년 (END:종료)>>"); // end쓰면 불린으로 바꿔줌
		String st_grade = scan.nextLine(); // 학번을 문자열로 입력할 변수 st_num 선언 DB와 연동할 때는 변수이름 snake case로 씀
		if (st_grade.equals("END")) {
			return false;
		}

		// 학년은 String인데 숫자로 입력해야 하니까 인티져 해줌
		int intGrade = 0;
		try {
			intGrade = Integer.valueOf(st_grade);
		} catch (Exception e) {
			System.out.println("학년은 숫자만 가능!");
			System.out.println("입력한 문자열:" + st_grade); // 문자열로 입력하지 마라
			return true;
		}

		if (intGrade < 0 || intGrade > 5) {
			System.out.println("학년은 1~4까지만 가능");
			System.out.println("다시 입력해 주세요");
			return true;
		}
		studentVO.setGrade(intGrade);

		System.out.println("전화번호: 010-000-0000 형식으로 입력." + "(END:종료) >>");
		String st_tel = scan.nextLine();
		if (st_tel.equals("END")) {
			return false;
		}
		studentVO.setTel(st_tel);

		studentList.add(studentVO); // add해줘야 최종 set
		this.saveStudent(); // 파일에 저장하기
		return true;
	}

	@Override
	public void saveStudent() {

		// 파일기록 하는데 보조적인 역할
		FileWriter fileWriter = null; // printstream이랑 비슷..
		PrintWriter pWriter = null;

		try {
			fileWriter = new FileWriter(this.fileName); // 서라운드
			pWriter = new PrintWriter(fileWriter);

			// 내부의 Writer buffer에 값을 기록
			for (StudentVO svo : studentList) {
				pWriter.printf("%s:", svo.getNum());
				pWriter.printf("%s:", svo.getName());
				pWriter.printf("%s:", svo.getDept());
				pWriter.printf("%d:", svo.getGrade()); // grade는 숫자
				pWriter.printf("%s:", svo.getTel());
			} // system.out을 pWriter로 바꿈

			pWriter.flush(); // Writer buffer에 기록된 값을 파일에 저장
			pWriter.close(); // 이걸 해야 저장이 됨

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public void studentList() {

		System.out.println(Lines.dLine);
		System.out.println("학생 명부 리스트");
		System.out.println(Lines.dLine);
		System.out.println("학번\t|이름\t|학과\t|학년\t|전화번호\t|"); // |는 구분표시
		System.out.println(Lines.sLine);

		for (StudentVO sVO : studentList) { // 스튜던드 리스트만큼 돌리기 위해 sVO 여기서 처음 만듦! 내맘대로 객체 생성
			System.out.printf("%s\t|", sVO.getNum());
			System.out.printf("%s\t|", sVO.getName());
			System.out.printf("%s\t|", sVO.getDept());
			System.out.printf("%d\t|", sVO.getGrade());
			System.out.printf("%s\t|\n", sVO.getTel());

		}
		System.out.println(Lines.dLine);

	}

}
