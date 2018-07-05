package org.loy.mq.consumer;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeoutException;

import org.loy.mq.recevice.LoyMqReceviced;
import org.loy.mq.util.RabbitMqConnUtils;
import org.springframework.util.SerializationUtils;

import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.Envelope;
import com.rabbitmq.client.ShutdownSignalException;
public class LoyMqConsumer implements  Consumer{
	private String queueName; // ��������
	private String consumerType;// ����������
	private volatile static Connection queueConnect; //���Ӷ���
	private Channel channel;// ����ͨ��
	private static boolean runFlag = true;// 

	public LoyMqConsumer(){
		runFlag = true;
	}
	
	public String getQueueName() {
		return queueName;
	}

	public void setQueueName(String queueName) {
		this.queueName = queueName;
	}

	public String getConsumerType() {
		return consumerType;
	}

	public void setConsumerType(String consumerType) {
		this.consumerType = consumerType;
	}

	public void handleConsumeOk(String consumerTag) {

	}

	public void handleCancelOk(String consumerTag) {

	}

	public void handleCancel(String consumerTag) throws IOException {

	}

	/***
	 * ��������
	 */
	public void handleDelivery(String consumerTag, Envelope arg1,
			BasicProperties arg2, byte[] arg3) throws IOException {
		try{
			if(!runFlag){
				channel.basicCancel(consumerTag);
				return;
			}
			@SuppressWarnings("unchecked")
			Map<String,Object> messageInfo = (Map<String,Object>)SerializationUtils.deserialize(arg3);
			if(runFlag && messageInfo != null){
				System.out.println(messageInfo);
				LoyMqReceviced.onMessage(messageInfo);
				channel.basicAck(arg1.getDeliveryTag(), false);
			}
			
		}catch(Exception e){
			System.out.println("��ȡ�����쳣:" + e);
		}
	}
	
    /***
	 * �ͷ���Դ
	 */
	public static void reaseResource() {
		try {		
			if (queueConnect != null) {
				queueConnect.close();
				queueConnect = null;
				runFlag = false;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}		
	
	}

	public void handleShutdownSignal(String consumerTag, ShutdownSignalException cause) {
		if(this.channel != null && runFlag){
			if(!this.channel.isOpen()){
				try {
					init(queueName,consumerType);
				} catch (Exception e) {
					e.printStackTrace();
				} 
			}
		}
	}
	

	public void handleRecoverOk(String consumerTag) {

	}

	public static Connection getConnect() throws IOException, TimeoutException {
		// ������ȡ����
		if(queueConnect == null){
			synchronized (LoyMqConsumer.class) {
				if(queueConnect == null){
					queueConnect = RabbitMqConnUtils.getConsRabbitConnection();
				}
			}
		}
		// δ����״̬��������
		if(!queueConnect.isOpen()){
			queueConnect = RabbitMqConnUtils.getConsRabbitConnection();
		}
		
		return queueConnect;
	}

	/***
	 * ��ʼ������MQ
	 * @param queueName
	 * @param consumerType
	 */
	public void init(String queueName ,String consumerType){
		this.queueName = queueName;
		this.consumerType = consumerType;
		try{
			channel = getConnect().createChannel();
			// ��������
			channel.queueDeclare(queueName, true, false, false, null);
			// ����ͬһʱ���������ת������
			channel.basicQos(1);
			// ָ������
			channel.basicConsume(queueName, true, this);
			
			System.out.println("sufow mq start success!!!");
			
		}catch(Exception e){
			System.err.println("connect is error:" + e);
		}

	}
	

}
