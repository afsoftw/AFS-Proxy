package afs.proxy.server;

import java.net.Socket;

class Globals
{
	private static Integer proxyPort;
	private static Integer tcpPort;

	public static synchronized void setProxyPort (Integer port)
	{
		proxyPort = port;
	}

	public static synchronized Integer getProxyPort ()
	{
		return proxyPort;
	}

	public static synchronized void setTcpPort (Integer port)
	{
		tcpPort = port;
	}

	public static synchronized Integer getTcpPort ()
	{
		return tcpPort;
	}
}