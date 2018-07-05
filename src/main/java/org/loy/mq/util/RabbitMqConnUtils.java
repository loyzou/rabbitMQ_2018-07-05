package org.loy.mq.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeoutException;

import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.util.SerializationUtils;

import com.rabbitmq.client.Address;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class RabbitMqConnUtils implements java.io.Serializable {
	private static RabbitMqConnUtils rabbitMqConnUtils;
	private static final long serialVersionUID = 4994133105915907111L;

	public RabbitMqConnUtils(){
		
	}
	
	public static RabbitMqConnUtils getInstance(){
		if(rabbitMqConnUtils == null){
			synchronized (RabbitMqConnUtils.class) {
				if(rabbitMqConnUtils == null){
					rabbitMqConnUtils = new RabbitMqConnUtils();
				}
			}
		}
		return rabbitMqConnUtils;
	}
	
	/***
	 * ��ȡrabbitMQ���Ӷ���
	 * @return
	 * @throws TimeoutException 
	 * @throws IOException 
	 */
	public static Connection getConsRabbitConnection() throws IOException, TimeoutException{
		return RabbitMqConnUtils.createConnectFactory().newConnection(getAddresses());
	}

	/***
	 * ��ȡ���ӵĵ�ַ
	 * @return
	 */
	private static List<Address> getAddresses() {
		String[] hosts = AppProperties.getProperties("rabbit.host").split(",");
		String[] posts = AppProperties.getProperties("rabbit.port").split(",");
		List<Address> addrList = new ArrayList<Address>();
		
		for(int i=0; i < hosts.length; i++){
			Address addr = new Address(hosts[i], Integer.parseInt(posts[i]));
			addrList.add(addr);
		}
		return addrList;
	}

	/***
	 * ���������ӹ�����
	 * @return
	 */
	public static ConnectionFactory createConnectFactory(){
		ConnectionFactory connectionFactory = new ConnectionFactory();
		connectionFactory.setUsername(AppProperties.getProperties("username"));
		connectionFactory.setPassword(AppProperties.getProperties("password"));
		connectionFactory.setConnectionTimeout(10000);
		connectionFactory.setAutomaticRecoveryEnabled(true);
		connectionFactory.setNetworkRecoveryInterval(10000);
		connectionFactory.setRequestedHeartbeat(5000); // ������������
		
		// �������ӵĲ���
		return  connectionFactory;

	}
	
	/**
	 *���͵㵽��������Ϣ 
	 * @param msgMap
	 * @throws TimeoutException 
	 * @throws IOException 
	 */
	public void sendDirectMsg(String queueName, Map<String,Object> msgMap) throws IOException, TimeoutException{
		Channel channel = getConsRabbitConnection().createChannel();
		//����
		channel.exchangeDeclare("sufowExchange", "direct", true);
		// ��������
		channel.queueDeclare(queueName, true, false, false, null);
		// ���а�
		channel.queueBind(queueName, "sufowExchange", "dfRoot_sufowQueue");
		
		// ���÷���
		channel.basicPublish("sufowExchange", "dfRoot_sufowQueue" , null, SerializationUtils.serialize(msgMap));
		
		// �ر�ͨ��
		channel.close();
	}

	@SuppressWarnings("resource")
	public static void main(String[] args) {
		// ������Ŀ����
		new ClassPathXmlApplicationContext("/spring/spring*.xml");
		//createConnectFactory();
		getAddresses();
	}

}
