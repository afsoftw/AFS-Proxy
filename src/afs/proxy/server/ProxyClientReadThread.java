package afs.proxy.server;

import java.net.*;
import java.io.*;
import java.util.*;

class ProxyClientReadThread implements Runnable
{
	public Thread thread;

	private Socket proxyClientSocket;
	private Socket tcpClientSocket;
	
	private final int bufSize = 65535;

	ProxyClientReadThread (Socket proxyClientSocket)
	{
		this.proxyClientSocket = proxyClientSocket;
		this.tcpClientSocket = TempTcpSocket.getSocket();
		this.thread = new Thread (this);
		this.thread.start();
	}

	public void run ()
	{
		OutputStream tcpClientOutputStream = null;
		InputStream proxyClientInputStream = null;

		try
		{
			proxyClientInputStream = this.proxyClientSocket.getInputStream();
			tcpClientOutputStream = this.tcpClientSocket.getOutputStream();
		}
		catch (IOException e) {}

		int len = 0;
		byte[] bufIn = new byte[bufSize];

		try
		{
			while (len != -1)
			{
				if (len > 0) 
				{
					byte[] bufOut = new byte[len];
					System.arraycopy (bufIn, 0, bufOut, 0, len);
					DataPackage dataPackage = new DataPackage (tcpClientOutputStream, bufOut, len);
					PackageQueue.addPackage ("1", dataPackage);
					System.out.println ("<- " + len);
				}
				len = proxyClientInputStream.read (bufIn, 0, bufSize);
			}
		}
		catch (IOException e) {}
	}
}