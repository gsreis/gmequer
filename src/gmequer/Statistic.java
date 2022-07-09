package gmequer;

import java.util.Iterator;
import java.util.Vector;

public class Statistic {
    private long countSend = 0;
    private long countReceive = 0;
    private long seconds = 0;
    private long tpsSend =0;
    private long tpsReceive = 0;
    long time = System.currentTimeMillis();
    private Vector<Exception> errors = new Vector<Exception>();
    
    public Statistic(long seconds) {
    	this.seconds = seconds;
    }
    public synchronized void  incrementSend() { 
    	countSend++;
    }
    public synchronized void  incrementReceive() { 
    	countReceive++;
    }
    
    public void addError(Exception e) {
    	errors.add(e);
    }

	public void printErrMessage() {
		for (Iterator iterator = errors.iterator(); iterator.hasNext();) {
			Exception exception = (Exception) iterator.next();
			exception.printStackTrace(System.out);
		}
		errors.clear();
	}
    /*
     * Print statistics at time specified (seconds)
     */
    public void print() {
    	if (((System.currentTimeMillis() - time) > (seconds * 1000)))
    	{
    		time = System.currentTimeMillis();
    		String msgEnviados = "PAC Enviados   ";
    		String msgRecebidos = "PAC Recebidos  ";
    		String temp = "                  ".substring(0, 15-(""+countSend).length());
    		String msgCountSend =  (""+countSend) + temp ;
    		temp = "                  ".substring(0, 15-(""+countReceive).length());
    		String msgCountReceiv = (""+countReceive) + temp;
    		
    		System.out.println("--------------------------------");
    		System.out.println("|" + msgEnviados + "|" + msgRecebidos + "|");
    		System.out.println("|" + msgCountSend + "|" + msgCountReceiv + "|");
    		
    		String msgEnviadosStr = "TPS Send       ";
    		String msgRecebidosStr = "TPS Receive    ";
    		System.out.println("|-------------------------------|");
    		System.out.println("|" + msgEnviadosStr + "|" +  msgRecebidosStr + "|");
    		System.out.println("|-------------------------------|");

    		tpsSend = countSend / seconds;
    		tpsReceive = countReceive / seconds;
    		countSend = 0;
    		countReceive = 0; 
    		temp = "                  ".substring(0, 15-(""+tpsSend).length());
    		String tpsCountSend = (""+tpsSend) + temp;
    		temp = "                  ".substring(0, 15-(""+tpsReceive).length());
    		String tpsCountReceive =  (""+tpsReceive) + temp;
    		System.out.println("|" + tpsCountSend + "|" + tpsCountReceive + "|");
    		System.out.println("--------------------------------");
    		System.out.println("|Errors   " + errors.size() + "     |");
    		System.out.println("|-------------------------------|");    		
    		System.out.println("");
    	}
    }
}
