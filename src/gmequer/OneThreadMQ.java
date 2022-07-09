package gmequer;

public interface OneThreadMQ {
	public void run();
	public void start();
	public void terminate();
	public boolean getConnected();
	public void printStats();
}
