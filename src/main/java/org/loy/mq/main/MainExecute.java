package org.loy.mq.main;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class MainExecute {
	
	public static ApplicationContext init(){
		// º”‘ÿœÓƒø≈‰÷√
		ApplicationContext context = new ClassPathXmlApplicationContext("/spring/spring*.xml");
		
		return context;
	}
	
	public static void main(String[] args) {
		System.out.println("************start mq begin***************");
		init();
		System.out.println("************start mq  end***************");
	}
}
