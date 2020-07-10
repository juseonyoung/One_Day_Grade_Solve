package com.biz.grade.service;

import com.biz.grade.domain.ScoreVO;

public interface ScoreService {

	public void loadScore();
	public boolean inputScore();
	
	public void saveScoreVO(ScoreVO scoreVO); //매개변수로 scoreVO 설정
	
	public void calcSum();
	public void calcAvg();
	
	public void saveScore();
	public void scoreList();
	
}
