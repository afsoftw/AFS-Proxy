package afs.proxy.client;

public class ProxyClient
{
	public static void main (String args[])
	{
		String proxyAddress = "127.0.0.1";
		int proxyPort = 10100;

		String tcpAddress = "127.0.0.1";
		int tcpPort = 0;

		int argsLen = args.length;

		boolean keepalive = false;

		for (int i = 0; i < argsLen; i++)
		{
			if (args[i].equals ("-h") || args[i].equals ("--help"))
			{
				System.out.println ("  -h,  --help           Print this help");
				System.out.println ("  -a,  --proxy-address  Specify address of proxy-server (default: 127.0.0.1)");
				System.out.println ("  -p,  --proxy-port     Specify port of proxy-server (default: 10100)");
				System.out.println ("  -A,  --tcp-address    Specify address of tcp service (default: 127.0.0.1)");
				System.out.println ("  -P,  --tcp-port       Specify port of tcp service (default: 0)");
				System.out.println ("  -k,  --keepalive      Send keepalive packets");
				return;
			}
			else if (args[i].equals ("-a") || args[i].equals ("--proxy-address"))
			{
				if (i < argsLen - 1) proxyAddress = args[i+1];
			}
			else if (args[i].equals ("-p") || args[i].equals ("--proxy-port"))
			{
				if (i < argsLen - 1) proxyPort = Integer.parseInt (args[i+1]);
			}
			else if (args[i].equals ("-A") || args[i].equals ("--tcp-address"))
			{
				if (i < argsLen - 1) tcpAddress = args[i+1];
			}
			else if (args[i].equals ("-P") || args[i].equals ("--tcp-port"))
			{
				if (i < argsLen - 1) tcpPort = Integer.parseInt (args[i+1]);
			}
			else if (args[i].equals ("-k") || args[i].equals ("--keepalive"))
			{
				keepalive = true;
			}
		}

		if (tcpPort < 1 || tcpPort > 65565)
		{
			System.out.println ("Wrong tcp port " + tcpPort);
			return;
		}

		Globals.setProxyAddress (proxyAddress);
		Globals.setProxyPort (proxyPort);
		Globals.setTcpAddress (tcpAddress);
		Globals.setTcpPort (tcpPort);
		Globals.setKeepalive (keepalive);

		ProxyConnectingThread proxyConnectingThread  = new ProxyConnectingThread ();

		if (keepalive)
		{
			KeepaliveThread keepaliveThread = new KeepaliveThread ();
		}
	}
}