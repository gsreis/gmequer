package gmequer;

import com.ibm.disthub2.impl.formats.OldEnvelop.payload.error;
import com.ibm.msg.client.jms.JmsConnectionFactory;
import com.ibm.msg.client.jms.JmsFactoryFactory;
import com.ibm.msg.client.wmq.WMQConstants;

import java.util.Iterator;
import java.util.Properties;
import java.util.Random;
import java.util.Vector;

import javax.jms.Connection;
import javax.jms.DeliveryMode;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;

public class OneReceiver extends Thread implements OneThreadMQ{

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
	private MessageConsumer consumer;
	private boolean running = true;
	private long sleepTime;
	Statistic stats = null;
	private boolean connected = false;

	public OneReceiver(Properties p, Statistic stats) {
		this.stats = stats;
		hostName = p.getProperty("hostName").trim();
		hostPort = p.getProperty("hostPort").trim();
		channelName = p.getProperty("channelName").trim();
		queueManagerName = p.getProperty("queueManagerName").trim();
		queueName = p.getProperty("queueName").trim();
		userName = p.getProperty("userName").trim();
		userPassword = p.getProperty("userPassword").trim();
		p.getProperty("messageSizing").trim();
		p.getProperty("messagePattern").trim();
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
			consumer = session.createConsumer(destination);
			
			connection.start();
			connected = true;
			this.start();
		} catch (JMSException e) {
			stats.addError((Exception)e);
		} catch (Exception e) { 
			stats.addError(e);
		}
		
	}

	public void run() {
		while (running) {
			if (!connected)
				continue;
			try {
				Message m = consumer.receive();
				stats.incrementReceive();
			    try { Thread.sleep(sleepTime); } catch(Exception e ) {}
			} catch (Exception e) {
				stats.addError(e);
			}
		}
	}

	public boolean getConnected() {
		return connected;
	}
	
	public void terminate()
	{
		try {
			consumer.close();
			session.close();
			connection.close();
			connected = false;
			running = false;
		} catch (Exception e) {
		}
	}

	@Override
	public void printStats() {
		System.out.println(jmsFactory.toString());
		
	}

}