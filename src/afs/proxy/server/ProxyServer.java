package afs.proxy.server;

public class ProxyServer
{
	public static void main (String args[]) throws InterruptedException
	{
		int proxyPort = 10100;
		int tcpPort = 10101;
		int argsLen = args.length;

		for (int i = 0; i < argsLen; i++)
		{
			if (args[i].equals ("-h") || args[i].equals ("--help"))
			{
				System.out.println ("  -h,  --help           Print this help");
				System.out.println ("  -p,  --proxy-port     Specify port to accept proxy-client (default: 10100)");
				System.out.println ("  -P,  --tcp-port       Specify port to accept tcp-clients (default: 10101)");
				return;
			}
			else if (args[i].equals ("-p") || args[i].equals ("--proxy-port"))
			{
				if (i < argsLen - 1) proxyPort = Integer.parseInt (args[i+1]);
			}
			else if (args[i].equals ("-P") || args[i].equals ("--tcp-port"))
			{
				if (i < argsLen - 1) tcpPort = Integer.parseInt (args[i+1]);
			}
		}
/*
		if (tcpPort < 1 || tcpPort > 65565)
		{
			System.out.println ("Wrong tcp port " + tcpPort);
			return;
		}
*/
		Globals.setProxyPort (proxyPort);
		Globals.setTcpPort (tcpPort);

		PackageSendThread packageSendThread = new PackageSendThread ();

		ProxyClientListeningThread proxyClientListeningThread = new ProxyClientListeningThread (proxyPort);
		Runtime.getRuntime().addShutdownHook(new Thread() {public void run () {proxyClientListeningThread.onStop ();}});
		proxyClientListeningThread.thread.join ();
	}
}