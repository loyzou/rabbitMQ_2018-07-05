package org.loy.mq.recevice;

import java.util.Map;

import org.loy.mq.consumer.LoyMqConsumer;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

public class LoyMqReceviced implements ApplicationListener<ContextRefreshedEvent> {
	private String queueName; // �������ơ�
	public String getQueueName() {
		return queueName;
	}
	public void setQueueName(String queueName) {
		this.queueName = queueName;
	}
	
	public LoyMqReceviced (){
		
	}
	
	/***
	 * ���ն˴�������
	 * @param msgMap
	 */
	public static void onMessage(Map<String,Object> msgMap){
		System.out.println("���Ѷ˴�������:" + msgMap);
	}

	public void onApplicationEvent(ContextRefreshedEvent event) {
		
		initMQ(queueName);
		
		System.out.println("��ʼ�����ķ���");
	}
	
	/***
	 * ����MQ
	 * @param queueName
	 */
	private void initMQ(String queueName) {
		new LoyMqConsumer().init(queueName,"sufowConsumer");
	}

}
