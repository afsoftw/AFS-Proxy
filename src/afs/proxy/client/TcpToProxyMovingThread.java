package afs.proxy.client;

import java.net.Socket;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import afs.proxy.common.*;
import java.io.IOException;

class TcpToProxyMovingThread implements Runnable
{
	public Thread thread;

	private Socket tcpSocket;
	private Socket proxySocket;

	private final int buf_size = 65535;

	TcpToProxyMovingThread  (Socket tcpSocket, Socket proxySocket)
	{
		this.tcpSocket = tcpSocket;
		this.proxySocket = proxySocket;
		this.thread = new Thread (this);
		this.thread.start();
	}

	public void run ()
	{
		InputStream tcpInputStream = null;
		OutputStream proxyOutputStream = null;
		PrintWriter proxyPrintWriter = null;

		try
		{
			tcpInputStream = this.tcpSocket.getInputStream();
			proxyOutputStream = this.proxySocket.getOutputStream();
			proxyPrintWriter = new PrintWriter (proxyOutputStream, true);
		}
		catch (IOException e)
		{
			return;
		}

		int len = 0;
		byte[] bufIn = new byte[buf_size];
		byte[] bufOut = null;

		try
		{
			while (len != -1)
			{
				if (len > 0)
				{
					bufOut = new byte[len];
					System.arraycopy (bufIn, 0, bufOut, 0, len);
					String jsonString = "{\"length\":\"" + Integer.toString(len) + "\",\"data\":\"" 
					+ Util.toBase58 (bufOut) + "\"}";
					proxyPrintWriter.println (jsonString);
					//System.out.println ("<- " + len);
				}
				len = tcpInputStream.read (bufIn, 0, buf_size);
			}
		}
		catch (IOException e) 
		{
			return;
		}
	}
}