package org.loy.mq.sender;

import java.util.HashMap;
import java.util.Map;

import org.loy.mq.util.RabbitMqConnUtils;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class LoyMqSender {
	private String queueName;//队列名称

	public String getQueueName() {
		return queueName;
	}

	public void setQueueName(String queueName) {
		this.queueName = queueName;
	}
	
	public LoyMqSender(){
		
	}
	
	/***
	 * 发送队列消息
	 * @param messageMap
	 */
	public void SendMessage(Map<String,Object> messageMap){
		this.sendInfo(messageMap);
	}
	
	/***
	 * mq发送消息
	 * @param sendMessage
	 */
	public void sendInfo(final Map<String,Object> sendMessage){
		try{
			RabbitMqConnUtils.getInstance().sendDirectMsg("sufowQueue", sendMessage);
			System.out.println("send data success");
		}catch(Exception e){
			System.out.println("mq sendInfo error:" + e);
		}
	}
	
	
	@SuppressWarnings("resource")
	public static void main(String[] args) throws InterruptedException {
		new ClassPathXmlApplicationContext(new String[] {"/spring/spring-mq-send.xml","/spring/spring.xml"});
		// 测试发送的数据
		Map<String,Object> sendMap = new HashMap<String, Object>();
		sendMap.put("data", "aaaaa");
		
		while(true){
			Thread.sleep(10000);
			new LoyMqSender().sendInfo(sendMap);
		}
	}
	
}
