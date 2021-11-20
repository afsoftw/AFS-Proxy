package afs.proxy.client;

import java.net.Socket;
import java.io.InputStream;
import java.io.OutputStream;
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

		try
		{
			tcpInputStream = this.tcpSocket.getInputStream();
			proxyOutputStream = this.proxySocket.getOutputStream();
		}
		catch (IOException e)
		{
			return;
		}

		int len = 0;
		byte[] buf = new byte[buf_size];

		try
		{
			while (len != -1)
			{
				if (len > 0) 
				{
					proxyOutputStream.write (buf, 0, len);
					System.out.println ("<- " + len);
				}
				len = tcpInputStream.read (buf, 0, buf_size);
			}
		}
		catch (IOException e) 
		{
			return;
		}
	}
}