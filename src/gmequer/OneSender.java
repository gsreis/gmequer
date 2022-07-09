package gmequer;

import com.ibm.msg.client.jms.JmsConnectionFactory;
import com.ibm.msg.client.jms.JmsFactoryFactory;
import com.ibm.msg.client.wmq.WMQConstants;
import java.util.Properties;
import javax.jms.Connection;
import javax.jms.DeliveryMode;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;

public class OneSender extends Thread implements OneThreadMQ{

	private String hostName = null;
	private String hostPort = null;
	private String channelName = null;
	private String queueManagerName = null;
	private String queueName = null;
	private String userName = null;
	private String userPassword = null;
	private JmsFactoryFactory factory;
	private JmsConnectionFactory jmsFactory;
	private Connection connection;
	private Session session;
	private Queue destination;
	private String messageSizing;
	private String pattern;
	private TextMessage textMessage;
	private MessageProducer producer;
	private boolean running = true;
	private boolean persistent = true;
	private long sleepTime;
	private Statistic stats = null;
	private boolean connected = false;

	public OneSender(Properties p, Statistic stats) {
		this.stats = stats;
		hostName = p.getProperty("hostName").trim();
		hostPort = p.getProperty("hostPort").trim();
		channelName = p.getProperty("channelName").trim();
		queueManagerName = p.getProperty("queueManagerName").trim();
		queueName = p.getProperty("queueName").trim();
		userName = p.getProperty("userName").trim();
		userPassword = p.getProperty("userPassword").trim();
		messageSizing = p.getProperty("messageSizing").trim();
		pattern = p.getProperty("messagePattern").trim();
		persistent = Boolean.parseBoolean(p.getProperty("persistent").trim());
		sleepTime = Long.parseLong(p.getProperty("sleepTime").trim());
		
		try {
			factory = JmsFactoryFactory.getInstance(WMQConstants.WMQ_PROVIDER);
			jmsFactory = factory.createConnectionFactory();
			jmsFactory.setStringProperty(WMQConstants.WMQ_HOST_NAME, hostName);
			jmsFactory.setStringProperty(WMQConstants.WMQ_PORT, hostPort);
			jmsFactory.setStringProperty(WMQConstants.WMQ_CHANNEL, channelName);
			jmsFactory.setIntProperty(WMQConstants.WMQ_CONNECTION_MODE, WMQConstants.WMQ_CM_CLIENT);
			jmsFactory.setStringProperty(WMQConstants.WMQ_QUEUE_MANAGER, queueManagerName);
			connection = jmsFactory.createConnection(userName, userPassword);
			session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			destination = session.createQueue(queueName);
			producer = session.createProducer(destination);
			
			if (persistent)
			    producer.setDeliveryMode(DeliveryMode.PERSISTENT);
			else
			    producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
			
			connection.start();
			connected = true;
			this.start();
		} catch (JMSException e) {
			stats.addError((Exception)e);
		} catch (Exception e) { 
			stats.addError(e);
		}

	}

	public boolean getConnected() {
		return connected;
	}
	
	public void run() {
		while (running) {
			if (!connected)
				continue;
			try {
				String message = createMessage(messageSizing, pattern);
				textMessage = session.createTextMessage(message);
				producer.send(textMessage);
				stats.incrementSend();
			    try { Thread.sleep(sleepTime); } catch(Exception e ) {}
			} catch (Exception e) {
				stats.addError(e);
			}
		}
	}

	private String createMessage(String messageSizing, String pattern) {
		int size = Integer.parseInt(messageSizing);
		int patlen = pattern.length();
		int numrep = size / patlen;
		String result = "";
		for (int i = 0; i < numrep; i++) {
			result += pattern;
		}
		return result;
	}
	
	public void terminate()
	{
		try {
			running = false;
			producer.close();
			session.close();
			connection.close();
			connected = false;
		} catch (Exception e) {
		}
		
	}

	@Override
	public void printStats() {
		System.out.println(jmsFactory.toString());
		
	}

}