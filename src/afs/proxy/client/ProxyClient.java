package afs.proxy.client;

import java.net.Socket;
import java.io.IOException;

public class ProxyClient
{
	public static void main (String args[])
	{
		Socket tcpSocket = null;
		Socket proxySocket = null;

		try
		{
			tcpSocket = new Socket ("127.0.0.1", 22);
		}
		catch (IOException e) 
		{
			System.out.println ("Could not connect to TCP service");
			return;
		}

		try
		{
			proxySocket = new Socket ("127.0.0.1", 10100);
			//proxySocket = new Socket ("192.168.2.211", 10100);
		}
		catch (IOException e) 
		{
			System.out.println ("Could not connect to proxy-server");
			return;
		}

		ProxyToTcpMovingThread proxyToTcpMovingThread = new ProxyToTcpMovingThread (proxySocket, tcpSocket);
		TcpToProxyMovingThread tcpToProxyMovingThread = new TcpToProxyMovingThread (tcpSocket, proxySocket);
	}
}