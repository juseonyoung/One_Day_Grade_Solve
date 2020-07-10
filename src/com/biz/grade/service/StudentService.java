package com.biz.grade.service;

import java.util.List;

import com.biz.grade.domain.StudentVO;

/*
 * 1. 파일을 읽어서 List에 담는 메서드
 * 2. 학생정보 입력받아 List에 담는 메서드
 * 3. 리스트에 담긴 학생정보 파일에 저장 메서드
 * 
 */
public interface StudentService {

	public void loadStudnet(); //파일 읽어서 리스트에 담기
	public boolean inputStudent(); //정보 입력받아 리스트에 담기
	public void saveStudent(); //리스트에 담은것 파일에 저장
	public void studentList(); // 리스트에 담은 파일 출력
	
	public StudentVO getStudent(String st_num);
	
	public List<StudentVO> getStudentList(); // List<StudentVO> 타입으로 설정된 변수를 return 하겠다 
	
	
}
