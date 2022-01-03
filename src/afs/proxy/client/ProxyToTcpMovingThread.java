package afs.proxy.client;

import java.net.Socket;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashMap;
import afs.proxy.common.Util;
import java.io.IOException;

class ProxyToTcpMovingThread implements Runnable
{
	public Thread thread;
	
	private final int buf_size = 65535;

	ProxyToTcpMovingThread  ()
	{
		this.thread = new Thread (this);
		this.thread.start();
	}

	public void run ()
	{
		InputStream proxyInputStream = null;
		BufferedReader proxyBufferedReader = null;
		OutputStream tcpOutputStream = null;
		Socket proxySocket = Globals.getProxySocket ();

		try
		{
			proxyInputStream = proxySocket.getInputStream();
			proxyBufferedReader = new BufferedReader (new InputStreamReader (proxyInputStream));
			//tcpOutputStream = this.tcpSocket.getOutputStream();
		}
		catch (IOException e)
		{
			Globals.setIsProxyConnected (false);
			return;
		}

		int len = 0;
		int id = 0;
		byte[] buf = null;
		HashMap<String, String> mapIn = new HashMap<String, String> ();

		try
		{
			while (len != -1)
			{
				if (len > 0) 
				{
					tcpOutputStream = TcpConnection.getOutputStream (id);
					tcpOutputStream.write (buf, 0, len);
					//System.out.println ("-> " + len);
				}
				String jsonData = proxyBufferedReader.readLine ();
				if (jsonData == null)
				{
					Globals.setIsProxyConnected (false);
					return;
				}
				Util.jsonParse (jsonData, mapIn, null);
				String lenStr = mapIn.get ("length");
				if (lenStr != null) len = Integer.parseInt (lenStr);
				else len = 0;
				if (len > 0)
				{
					id = Integer.parseInt (mapIn.get ("conid"));
					String data = mapIn.get ("data");
					buf = Util.fromBase58 (data);
				}
				else
				{
					Boolean init = Boolean.parseBoolean (mapIn.get ("init"));
					if (init) 
					{
						id = Integer.parseInt (mapIn.get ("conid"));
						TcpConnection.create (id);
					}
				}
			}
			Globals.setIsProxyConnected (false);
		}
		catch (IOException e) 
		{
			Globals.setIsProxyConnected (false);
			return;
		}
	}
}