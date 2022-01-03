package afs.proxy.client;

import java.net.Socket;

class Globals
{
	private static String proxyAddress;
	private static Integer proxyPort;
	private static String tcpAddress;
	private static Integer tcpPort;
	private static Socket proxySocket;
	private static boolean isProxyConnected = false;

	public static synchronized void setProxyAddress (String address)
	{
		proxyAddress = address;
	}

	public static synchronized String getProxyAddress ()
	{
		return proxyAddress;
	}

	public static synchronized void setProxyPort (Integer port)
	{
		proxyPort = port;
	}

	public static synchronized Integer getProxyPort ()
	{
		return proxyPort;
	}

	public static synchronized void setTcpAddress (String address)
	{
		tcpAddress = address;
	}

	public static synchronized String getTcpAddress ()
	{
		return tcpAddress;
	}

	public static synchronized void setTcpPort (Integer port)
	{
		tcpPort = port;
	}

	public static synchronized Integer getTcpPort ()
	{
		return tcpPort;
	}

	public static synchronized void setProxySocket (Socket socket)
	{
		proxySocket = socket;
	}

	public static synchronized Socket getProxySocket ()
	{
		return proxySocket;
	}

	public static synchronized void setIsProxyConnected (boolean isConnected)
	{
		isProxyConnected = isConnected;
	}

	public static synchronized boolean getIsProxyConnected ()
	{
		return isProxyConnected;
	}
}