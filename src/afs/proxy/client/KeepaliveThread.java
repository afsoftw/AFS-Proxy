package afs.proxy.client;

import java.net.Socket;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.IOException;

class KeepaliveThread implements Runnable
{
	public Thread thread;

	KeepaliveThread  ()
	{
		this.thread = new Thread (this);
		this.thread.start();
	}

	public void run ()
	{
		OutputStream proxyOutputStream = null;
		PrintWriter proxyPrintWriter = null;

		while (true)
		{
			if (Globals.getIsProxyConnected ())
			{			
				Socket proxySocket = Globals.getProxySocket ();

				try
				{
					proxyOutputStream = proxySocket.getOutputStream();
					proxyPrintWriter = new PrintWriter (proxyOutputStream, true);
				}
				catch (IOException e)
				{
					return;
				}

				String jsonString = "{\"conid\":\"0\",\"length\":\"0\",\"data\":\"\"}";
				//System.out.println (jsonString);
				proxyPrintWriter.println (jsonString);
			}

			try
			{
				this.thread.sleep (60000);
			}
			catch (InterruptedException e1) {}
		}
	}
}