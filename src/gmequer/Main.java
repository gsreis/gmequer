

package gmequer;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.Scanner;

public class Main {

	public static void main(String[] args) {
		Properties prop = new Properties();
		Statistic stats = null;
		
		InputStream reader = null;
		try {
			reader = new FileInputStream("gmequer.properties");
			prop.load(reader);
		} catch (Exception e1) {
			System.out.println("Erro na inicialização"); 
			System.exit(0);
		} 
		
		System.out.println("GMEQUER 1.0");
		System.out.println("hostName " + prop.getProperty("hostName").trim());
		System.out.println("hostPort " + prop.getProperty("hostPort").trim());
		System.out.println("channelName " + prop.getProperty("channelName").trim());	
		System.out.println("queueManagerName " + prop.getProperty("queueManagerName").trim());
		System.out.println("queueName " + prop.getProperty("queueName").trim());
		System.out.println("userName " + prop.getProperty("userName").trim());
		System.out.println("userPassword " + prop.getProperty("userPassword").trim());
		System.out.println("messageSizing " + prop.getProperty("messageSizing").trim());
		System.out.println("pattern " + prop.getProperty("messagePattern").trim());
		System.out.println("sleepTime " + Long.parseLong(prop.getProperty("sleepTime")));
		System.out.println("persistent " + Boolean.parseBoolean(prop.getProperty("persistent").trim()));		
		System.out.println("numThreadsReceiver " + prop.getProperty("numThreadsReceiver").trim());
		System.out.println("numThreadsSender " + prop.getProperty("numThreadsSender").trim());
		
		stats = new Statistic(Integer.parseInt(prop.getProperty("statisticTime").trim()));

		int numThreads = Integer.parseInt(prop.getProperty("numThreadsSender").trim()) + Integer.parseInt(prop.getProperty("numThreadsReceiver").trim());
		OneThreadMQ thr[] = new OneThreadMQ[numThreads];
		for (int i = 0; i < thr.length; i++) {
			if (i < Integer.parseInt(prop.getProperty("numThreadsSender").trim())) {
				thr[i] = new OneSender(prop, stats);
			} else {
				thr[i] = new OneReceiver(prop, stats);
			}
		}		
		
        System.out.println("Threads iniciadas"); 
        System.out.println("Producers " + Integer.parseInt(prop.getProperty("numThreadsSender").trim()) );
        System.out.println("Consumers " + Integer.parseInt(prop.getProperty("numThreadsReceiver").trim()) );
 
    	Scanner keyboard = new Scanner(System.in);
    	System.out.println("Digite uma das opções : exit, error ou stats ");

        while (true) {  
            try { Thread.sleep(Long.parseLong(prop.getProperty("statisticTime")) * 1000); } catch (Exception e) {}
        	try {
				if (System.in.available() != 0) {
				    String input = keyboard.nextLine();
				    if(input != null) {
				        if ("exit".equals(input)) {
				            System.out.println("Terminando");
				            for (int i = 0; i < thr.length; i++) {
				            	thr[i].terminate();
				            }
				            break;
				        } else if ("error".equals(input)) {
				            stats.printErrMessage();
				        }else if ("stats".equals(input)) {
				            for (int i = 0; i < thr.length; i++) {
				            		thr[i].printStats();
				            }                	
				        }
				    }        		
				}
			} catch (IOException e) {
			}
        	stats.print();
        } 

	}

}
