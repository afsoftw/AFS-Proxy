package afs.proxy.client;

import java.net.Socket;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import afs.proxy.common.*;
import java.io.IOException;

class ProxyToTcpMovingThread implements Runnable
{
	public Thread thread;

	private Socket proxySocket;
	private Socket tcpSocket;
	
	private final int buf_size = 65535;

	ProxyToTcpMovingThread  (Socket proxySocket, Socket tcpSocket)
	{
		this.proxySocket = proxySocket;
		this.tcpSocket = tcpSocket;
		this.thread = new Thread (this);
		this.thread.start();
	}

	public void run ()
	{
		InputStream proxyInputStream = null;
		BufferedReader proxyBufferedReader = null;
		OutputStream tcpOutputStream = null;

		try
		{
			proxyInputStream = this.proxySocket.getInputStream();
			proxyBufferedReader = new BufferedReader (new InputStreamReader (proxyInputStream));
			tcpOutputStream = this.tcpSocket.getOutputStream();
		}
		catch (IOException e)
		{
			return;
		}

		int len = 0;
		//byte[] buf = new byte[buf_size];
		byte[] buf = null;

		try
		{
			while (len != -1)
			{
				if (len > 0) 
				{
					tcpOutputStream.write (buf, 0, len);
					//System.out.println ("-> " + len);
				}
				//len = proxyInputStream.read (buf, 0, buf_size);
				String data = proxyBufferedReader.readLine ();
				buf = Util.fromBase58 (data);
				len = buf.length;
			}
		}
		catch (IOException e) 
		{
			return;
		}
	}
}