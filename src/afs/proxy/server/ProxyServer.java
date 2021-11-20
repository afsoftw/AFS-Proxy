package afs.proxy.server;

public class ProxyServer
{
	public static void main (String args[]) throws InterruptedException
	{
		PackageSendThread packageSendThread = new PackageSendThread ();

		ProxyClientListeningThread proxyClientListeningThread = new ProxyClientListeningThread (10100);
		Runtime.getRuntime().addShutdownHook(new Thread() {public void run () {proxyClientListeningThread.onStop ();}});
		proxyClientListeningThread.thread.join ();
	}
}