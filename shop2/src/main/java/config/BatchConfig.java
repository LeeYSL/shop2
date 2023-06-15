package config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

import controller.AjaxController;
import util.CountScheduler;

/*
 * Scheduling : 정기적인 실행을 하도록 설정
 * 
 */

@Configuration
@EnableScheduling  //Scheduling : 스케줄링 시작
public class BatchConfig {
	@Bean
	public CountScheduler countScheduler() {
		return new CountScheduler(); //Scheduling 클래스의 설정 대로 자동 실행
	}

	
}


