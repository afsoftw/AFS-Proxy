package afs.proxy.server;

import java.net.Socket;
import java.io.InputStream;
import java.io.OutputStream;
import afs.proxy.common.Util;
import java.io.IOException;

class TcpClientReadThread implements Runnable
{
	public Thread thread;

	private Socket tcpClientSocket;
	private Socket proxyClientSocket;
	private Integer connectionId;

	private final int buf_size = 65535;

	TcpClientReadThread (Socket tcpClientSocket, Socket proxyClientSocket, Integer connectionId)
	{
		this.tcpClientSocket = tcpClientSocket;
		this.proxyClientSocket = proxyClientSocket;
		this.connectionId = connectionId;
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

		DataPackage dataPackage = new DataPackage (proxyClientOutputStream);
		dataPackage.setConnectionId (this.connectionId);
		dataPackage.type = 0;
		TcpClientPackageQueue.addPackage (Integer.toString (this.connectionId), dataPackage);
		System.out.println ("Init new connection");

		try
		{
			while (len != -1)
			{
				if (len > 0) 
				{
					dataPackage = new DataPackage (proxyClientOutputStream);
					byte[] bufOut = new byte[len];
					System.arraycopy (bufIn, 0, bufOut, 0, len);
					dataPackage.setStringData (Util.toBase58 (bufOut));
					dataPackage.setByteDataLen (len);
					dataPackage.setConnectionId (this.connectionId);
					dataPackage.type = 2;
					TcpClientPackageQueue.addPackage (Integer.toString (this.connectionId), dataPackage);
					//System.out.println ("-> " + len);
				}
				len = tcpClientInputStream.read (bufIn, 0, buf_size);
			}
		}
		catch (IOException e) {}	
	}
}