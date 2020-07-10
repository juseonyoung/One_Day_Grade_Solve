package com.biz.grade.exec;

import java.util.Scanner;

import com.biz.grade.config.DBContract;
import com.biz.grade.config.Lines;
import com.biz.grade.service.ScoreService;
import com.biz.grade.service.ScoreServiceImplV1;
import com.biz.grade.service.StudentService;
import com.biz.grade.service.StudentServiceImplV1;

public class GradeEx_01 {

	public static void main(String[] args) {

		Scanner scan = new Scanner(System.in);
		StudentService stService = new StudentServiceImplV1();
		stService.loadStudnet(); // 여기다 만들어놓고 정보 불러들이기
		
		ScoreService scService = new ScoreServiceImplV1();
		scService.loadScore();

		while (true) {
			System.out.println(Lines.dLine);
			System.out.println("빛고을 대학 학사관리 시스템 V1");
			System.out.println(Lines.dLine);
			System.out.println("1. 학생정보 등록");
			System.out.println("2. 학생정보 출력");
			System.out.println("3. 성적 등록");
			System.out.println("4. 성적 일람표 출력");
			System.out.println(Lines.sLine);
			System.out.println("QUIT. 업무종료");
			System.out.println(Lines.dLine);
			System.out.print("");

			String stMenu = scan.nextLine();
			if (stMenu.equals("QUIT")) {
				break;
			}

			int intMenu = 0;
			try {
				intMenu = Integer.valueOf(stMenu);
			} catch (Exception e) {
				System.out.println("메뉴는 숫자로만 선택할 수 있음!");
				continue;
			}

			if (intMenu == DBContract.MENU.학생정보등록) {
				while (true) {
					if (!stService.inputStudent()) {
						break;
					}
				}
			} else if (intMenu ==  DBContract.MENU.학생리스트출력) { // 아까 1을 선택했으면 1번 무시해라(메뉴선택 2번이 나옴)

				stService.studentList();
			} else if (intMenu == DBContract.MENU.성적등록) {
				
				// 1. inputScore()를 호출하여 코드를 수행하고
				// 2. inputScore()가 true를 return 하면 계속 반복 한다 
				// 3. inputScore()가 false를 return하면 반복을 중단한다.
				// 위 코드를 이렇게 세련되게...
				while(scService.inputScore());
				
				// 성적입력을 종료(중단) 할 경우 총점과 평균계산 method 호출
				scService.calcSum();
				scService.calcAvg();
				
				
			} else if (intMenu == DBContract.MENU.성적일람표) {
				scService.calcSum();
				scService.calcAvg();
				scService.scoreList();
			}

		}
		// while문 끝나고 !!!!
		System.out.println("업무종료! 퇴근~~");

	}
}