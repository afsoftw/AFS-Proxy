package afs.proxy.server;

import java.net.*;
import java.io.*;
import java.util.*;
import afs.proxy.common.*;

class TcpClientReadThread implements Runnable
{
	public Thread thread;

	private Socket tcpClientSocket;
	private Socket proxyClientSocket;

	private final int buf_size = 65535;

	TcpClientReadThread (Socket tcpClientSocket, Socket proxyClientSocket)
	{
		this.tcpClientSocket = tcpClientSocket;
		this.proxyClientSocket = proxyClientSocket;
		this.thread = new Thread (this);
		this.thread.start();
	}

	public void run ()
	{
		InputStream tcpClientInputStream = null;
		OutputStream proxyClientOutputStream = null;

		try
		{
			tcpClientInputStream = this.tcpClientSocket.getInputStream();
			proxyClientOutputStream = proxyClientSocket.getOutputStream();
		}
		catch (IOException e) {}

		int len = 0;
		byte[] bufIn = new byte[buf_size];

		try
		{
			while (len != -1)
			{
				if (len > 0) 
				{
					DataPackage dataPackage = new DataPackage (proxyClientOutputStream);
					byte[] bufOut = new byte[len];
					System.arraycopy (bufIn, 0, bufOut, 0, len);
					dataPackage.setStringData (Util.toBase58 (bufOut));
					dataPackage.type = 1;
					PackageQueue.addPackage ("1", dataPackage);
					//System.out.println ("-> " + len);
				}
				len = tcpClientInputStream.read (bufIn, 0, buf_size);
			}
		}
		catch (IOException e) {}	
	}
}