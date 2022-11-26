package afs.proxy.server;

import java.net.Socket;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashMap;
import afs.proxy.common.Util;
import java.io.IOException;

class ProxyClientReadThread implements Runnable
{
	public Thread thread;

	private Socket proxyClientSocket;

	private boolean isStopped = false;
	
	private final int bufSize = 65535;

	ProxyClientReadThread (Socket proxyClientSocket)
	{
		this.proxyClientSocket = proxyClientSocket;
		this.thread = new Thread (this);
		this.thread.start();
	}

	public void run ()
	{
		OutputStream tcpClientOutputStream = null;
		InputStream proxyClientInputStream = null;
		BufferedReader proxyClientBufferedReader = null;

		try
		{
			proxyClientInputStream = this.proxyClientSocket.getInputStream();
			proxyClientBufferedReader = new BufferedReader (new InputStreamReader (proxyClientInputStream));
		}
		catch (IOException e) { 
			this.setIsStopped ();
			return; }

		int len = 0;
		byte[] buf = null;
		HashMap<String, String> mapIn = new HashMap<String, String> ();
		Integer connectionId = 0;

		while (len != -1)
		{
			if (len > 0)
			{
				try
				{
					tcpClientOutputStream = ConnectionCounter.getConnectionSocket (connectionId).getOutputStream ();
				}
				catch (IOException e) 
				{
					this.setIsStopped ();
					return;
				}

				DataPackage dataPackage = new DataPackage (tcpClientOutputStream);
				dataPackage.setByteData (buf, len);
				dataPackage.type = 1;
				dataPackage.setConnectionId (connectionId);
				ProxyClientPackageQueue.addPackage (Integer.toString (connectionId), dataPackage);
				//System.out.println ("<- " + len);
			}

			String jsonString = null;

			try
			{
				jsonString = proxyClientBufferedReader.readLine ();
			}
			catch (IOException e) 
			{
				this.setIsStopped ();
				return;
			}

			if (jsonString == null)
			{
				this.setIsStopped ();
				return;
			}
			else
			{		
				Util.jsonParse (jsonString, mapIn, null);
				len = Integer.parseInt (mapIn.get ("length"));
				connectionId = Integer.parseInt (mapIn.get ("conid"));
				String data = mapIn.get ("data");
				buf = Util.fromBase58 (data);
			}
		}
	}

	private synchronized void setIsStopped ()
	{
		this.isStopped = true;
	}

	public synchronized boolean getIsStopped ()
	{
		return this.isStopped;	
	}
}