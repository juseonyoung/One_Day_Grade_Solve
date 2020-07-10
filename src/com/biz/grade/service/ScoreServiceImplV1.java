package com.biz.grade.service;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import com.biz.grade.config.DBContract;
import com.biz.grade.config.Lines;
import com.biz.grade.domain.ScoreVO;
import com.biz.grade.domain.StudentVO;

public class ScoreServiceImplV1 implements ScoreService {

	private List<StudentVO> studentList;
	private List<ScoreVO> scoreList;
	private Scanner scan;
	private String fileName;

	private String[] strSubjects;
	private Integer[] intScores;
	private int[] totalSum;
	private int[] totalAvg;

	StudentService stService;

	public ScoreServiceImplV1() {

		scoreList = new ArrayList<>();
		scan = new Scanner(System.in);
		fileName = "src/com/biz/grade/exec/data/Score.txt";

		// 과목명을 문자열 배열로 선언하고, 과목명 문자열 배열 개수만큼 점수를 담을 intScores배열을 선언
		strSubjects = new String[] { "국어", "영어", "수학", "음악" };
		intScores = new Integer[strSubjects.length];
		totalSum = new int[strSubjects.length];
		totalAvg = new int[strSubjects.length];

		stService = new StudentServiceImplV1(); // 여기부터 나오는 코드로 인해 스코어와 스튜던트 정보 연동
		stService.loadStudnet();

		// studentservice로 부터 studentlist를 추출하여 사용할 준비를 마친 코드 load밑에..
		studentList = stService.getStudentList();

	}

	@Override

	public void loadScore() { // student에서 만든거 복붙
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

				String[] scores = reader.split(":");
				// 학생정보를 VO에담기

				ScoreVO scoreVO = new ScoreVO();
				scoreVO.setNum(scores[DBContract.SCORE.SC_NUM]);
				scoreVO.setKor(Integer.valueOf(scores[DBContract.SCORE.SC_KOR]));
				scoreVO.setEng(Integer.valueOf(scores[DBContract.SCORE.SC_ENG]));
				scoreVO.setMath(Integer.valueOf(scores[DBContract.SCORE.SC_MATH]));
				scoreVO.setMusic(Integer.valueOf(scores[DBContract.SCORE.SC_MUSIC]));

				// 다 만들고 저장!
				scoreList.add(scoreVO);

			}
		} catch (FileNotFoundException e) {
			System.out.println("학생정보 파일 열기 오류!");

		} catch (IOException e) {
			System.out.println("학생정보 파일 읽기 오류!");
		}

	}

	// return type을 int(primitive)가 아닌 Integer(Wrapper Class)로 설정했음
	// Wrapper class의 사용법

	private Integer scoreCheck(String sc_score) { // sc_score(매개변수)로 전달받은 값을 검사한느 코드
		// 1. END 문자열 받으면 -1을 리턴하고
		// 2. 숫자로 바꿀 수 없는 문자열, 점수범위를 벗어나는 값이면 null을 리턴
		// 3. 정상적이면 문자열을 정수로 바꾸어 리턴

		// 만약 END 입력했으면 -1을 리턴해라
		if (sc_score.equals("END")) {
			return -1; // return타입이 integer여서 return false 하면 오류남

		}

		// int intScore =null; 오류남
		// primitive int 형 변수는 null값으로 클리어, 초기화 할수 없다.
		// Integer intScore =null; 정상적인 코드!
		// 왜냐-> Wrapper 클래스 Integer형 변수는 null값으로 clear가 가능
		Integer intScore = null; // 객체를 clear시킴. 아무것도 아니다
		try {
			intScore = Integer.valueOf(sc_score);
		} catch (Exception e) {
			System.out.println("점수는 숫자만 가능!");
			System.out.println("입력한 문자열:" + sc_score); // 문자열로 입력하지 마라
			return null;
		}

		if (intScore < 0 || intScore > 100) {
			System.out.println("점수는 100까지만 가능");
			System.out.println("다시 입력해 주세요");
			return null;
		}

		return intScore;
	}

	@Override
	public boolean inputScore() {
		ScoreVO scoreVO = new ScoreVO();

		System.out.println("학번 (END:종료)>>");
		String st_num = scan.nextLine();
		if (st_num.equals("END")) {
			return false;

		}

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
		st_num = String.format("%05d", intNum);

		// 학생정보에서 학번이 등록되어 있는지 확인하는 부분!!
		for (ScoreVO sVO : scoreList) {
			if (sVO.getNum().equals(st_num)) {
				System.out.println(st_num + "학생의 성적이 이미 등록되어 있습니다");
				return true;
			}
		}

		/*
		 * for(StudentVO stVO : studentList) { //학생정보 검사
		 * if(stVO.getNum().equals(st_num)) { System.out.println(st_num +
		 * "학생정보가 리스트에 없음!"); System.out.println("성적을 입력할 수 없습니다"); return true; } }
		 */
		StudentVO retVO = stService.getStudent(st_num);
		if (retVO == null) {
			System.out.println(st_num + "학생정보가 학적부에 없음");
			System.out.println("성적 입력할 수 없음");
			return true;
		}

		scoreVO.setNum(st_num); // 내가 입력한 학번이 저장

		for (int i = 0; i < strSubjects.length; i++) {

			System.out.printf("%s점수(END:종료)", strSubjects[i]); // 국영수음 점수를 담는
			String sc_score = scan.nextLine();
			// intScore -1, null, 숫자 값이 intScore에 담겨진다
			Integer intScore = this.scoreCheck(sc_score); // check 한테 sc_score를 매개변수로 전달 오류나면 null값 알려주고
			if (intScore == null) {
				i--; //
				continue;
				// -1이면 종료해라
			} else if (intScore < 0) { // 입력 중 오류나면 -1을 빼서 다시 i=0으로 !
				return false;
				// 입력 중 오류가 났다면(문자열, 범위오류) for() 반복문의 i값을
				// -1하여 감소시켜 주고 다시 for문을 시작하도록 한다.
				// 국어점수에서 이러한 일이 발생한다면 정상으로 입력할 때까지 국어점수 입력하게 함
				// for문 다시..
			}
			intScores[i] = intScore; // 정상코드면 점수배열에 그 값을 집어넣어

		}

		scoreVO.setKor(intScores[0]);
		scoreVO.setEng(intScores[1]);
		scoreVO.setMath(intScores[2]);
		scoreVO.setMusic(intScores[3]);

		scoreList.add(scoreVO);
		this.saveScoreVO(scoreVO); // 1명의 데이터를 추가저장하는 코드(입력할 때마다 1명씩) filewriter가 이런 기능을 함
		return false;
	}

	@Override
	public void saveScore() {
		// 필요가 없어짐..
	}

	public void scoreList() {
		
		// 과목별 평균과 총점을 계산할 배열을 0으로 클리어하기
		Arrays.fill(totalSum, 0);
		Arrays.fill(totalAvg, 0);
		
		
		System.out.println(Lines.dLine);
		System.out.println("성적 일람표");
		System.out.println(Lines.dLine);
		System.out.println("학번\t|이름\t|국어\t|영어\t|수학\t|음악\t|총점\t|평균\t|"); // |는 구분표시
		System.out.println(Lines.sLine);

		for (ScoreVO sVO : scoreList) { // 스튜던드 리스트만큼 돌리기 위해 sVO 여기서 처음 만듦! 내맘대로 객체 생성
			System.out.printf("%s\t|", sVO.getNum());

			StudentVO retVO = stService.getStudent(sVO.getNum());
			String st_name = "[없음]";
			if (retVO != null) {
				st_name = retVO.getName();
			}
			System.out.printf("%s\t|", st_name);
			System.out.printf("%d\t|", sVO.getKor());
			System.out.printf("%d\t|", sVO.getEng());
			System.out.printf("%d\t|", sVO.getMath());
			System.out.printf("%d\t|", sVO.getMusic());
			System.out.printf("%d\t|", sVO.getSum());
			System.out.printf("%5.2f\t|\n", sVO.getAvg()); // 전체 5자리중 소수점 둘째자리까지 표현하겠다
			
			totalSum[0] += sVO.getKor();
			totalSum[1] += sVO.getEng();
			totalSum[2] += sVO.getMath();
			totalSum[3] += sVO.getMusic();
			
		}
		System.out.println(Lines.sLine);
		System.out.print("과목총점:\t|");
		
		int sumAndSum=0;
		for(int sum : totalSum) {
			System.out.printf("%s\t|",sum);
			sumAndSum += sum;
		}
		System.out.printf("%s\t|",sumAndSum);
		
		System.out.print("과목평균:\t|");
		float avgAndAvg=0f;
		for(int sum : totalSum) {
			float avg = (float)sum / totalSum.length;
			System.out.printf("%5.2f\t|\n",avg);
			
		}
		System.out.printf("\t%5.2f\t|\n",avgAndAvg/totalSum.length);
		System.out.println();
		
		System.out.println(Lines.dLine);

	}

	@Override
	public void saveScoreVO(ScoreVO scoreVO) {
		FileWriter fileWriter = null;
		PrintWriter pWriter = null;

		try {
			fileWriter = new FileWriter(this.fileName, true); // true넣는순간!

			pWriter = new PrintWriter(fileWriter);
			pWriter.printf("%s:", scoreVO.getNum());
			pWriter.printf("%d:", scoreVO.getKor());
			pWriter.printf("%d:", scoreVO.getEng());
			pWriter.printf("%d:", scoreVO.getMath());
			pWriter.printf("%d:", scoreVO.getMusic());
			pWriter.printf("%d:", scoreVO.getSum());
			pWriter.printf("%d:\n", scoreVO.getAvg());
			pWriter.flush();
			pWriter.close();

		} catch (Exception e) {
			// TODO: handle exception
		}

	}

	@Override
	public void calcSum() {

		for (ScoreVO scoreVO : scoreList) {
			int sum = scoreVO.getKor();
			sum += scoreVO.getEng();
			sum += scoreVO.getMath();
			sum += scoreVO.getMusic();

			scoreVO.setSum(sum);
		}

	}

	@Override
	public void calcAvg() {
		// TODO Auto-generated method stub
		for (ScoreVO scoreVO : scoreList) {
			int sum = scoreVO.getSum();
			float avg = (float) sum / 4;
			scoreVO.setAvg(avg);
		}
	}

}
