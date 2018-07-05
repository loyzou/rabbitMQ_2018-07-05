package org.loy.mq.recevice;

import java.util.Map;

import org.loy.mq.consumer.LoyMqConsumer;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

public class LoyMqReceviced implements ApplicationListener<ContextRefreshedEvent> {
	private String queueName; // 队列名称、
	public String getQueueName() {
		return queueName;
	}
	public void setQueueName(String queueName) {
		this.queueName = queueName;
	}
	
	public LoyMqReceviced (){
		
	}
	
	/***
	 * 接收端处理数据
	 * @param msgMap
	 */
	public static void onMessage(Map<String,Object> msgMap){
		System.out.println("消费端处理数据:" + msgMap);
	}

	public void onApplicationEvent(ContextRefreshedEvent event) {
		
		initMQ(queueName);
		
		System.out.println("初始启动的方法");
	}
	
	/***
	 * 启动MQ
	 * @param queueName
	 */
	private void initMQ(String queueName) {
		new LoyMqConsumer().init(queueName,"sufowConsumer");
	}

}
