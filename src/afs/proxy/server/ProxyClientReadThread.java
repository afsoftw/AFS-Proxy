package afs.proxy.server;

import java.net.*;
import java.io.*;
import java.util.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import afs.proxy.common.*;

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
		BufferedReader proxyClientBufferedReader = null;

		try
		{
			proxyClientInputStream = this.proxyClientSocket.getInputStream();
			proxyClientBufferedReader = new BufferedReader (new InputStreamReader (proxyClientInputStream));
			tcpClientOutputStream = this.tcpClientSocket.getOutputStream();
		}
		catch (IOException e) {}

		int len = 0;
		//byte[] bufIn = new byte[bufSize];
		byte[] buf = null;
		HashMap<String, String> mapIn = new HashMap<String, String> ();

		try
		{
			while (len != -1)
			{
				if (len > 0)
				{
					//byte[] bufOut = new byte[len];
					//System.arraycopy (bufIn, 0, bufOut, 0, len);
					DataPackage dataPackage = new DataPackage (tcpClientOutputStream);
					dataPackage.setByteData (buf, len);
					dataPackage.type = 0;
					PackageQueue.addPackage ("1", dataPackage);
					//System.out.println ("<- " + len);
				}
				//len = proxyClientInputStream.read (bufIn, 0, bufSize);
				
				String jsonString = proxyClientBufferedReader.readLine ();
				if (jsonString == null)
				{
					buf = null;	
					len = 0;
				}
				else
				{
					Util.jsonParse (jsonString, mapIn, null);
					len = Integer.parseInt (mapIn.get ("length"));
					String data = mapIn.get ("data");
					buf = Util.fromBase58 (data);
					//len = buf.length;
				}
			}
		}
		catch (IOException e) {}
	}
}