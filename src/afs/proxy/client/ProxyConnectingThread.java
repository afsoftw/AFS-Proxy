package afs.proxy.client;

import java.net.Socket;
import java.io.IOException;

class ProxyConnectingThread implements Runnable
{
	public Thread thread;

	ProxyConnectingThread   ()
	{
		this.thread = new Thread (this);
		this.thread.start();
	}

	public void run ()
	{
		Socket proxySocket = null;
		int tryCount = 0;

		while (1 == 1)
		{
			if (!Globals.getIsProxyConnected())
			{
				if (++tryCount == 1) 
					System.out.print ("Connecting to proxy-server " + Globals.getProxyAddress() + " port " + Globals.getProxyPort() + " ... ");
				try
				{
					proxySocket = new Socket (Globals.getProxyAddress(), Globals.getProxyPort());
				}
				catch (IOException e) 
				{
					try
					{
						this.thread.sleep (5000);
					}
					catch (InterruptedException e1) {}
					continue;
				}

				System.out.println ("connected");
				Globals.setProxySocket (proxySocket);
				Globals.setIsProxyConnected (true);

				ProxyToTcpMovingThread proxyToTcpMovingThread = new ProxyToTcpMovingThread ();
				tryCount = 0;
			}
			else
			{
				try
				{
					this.thread.sleep (1000);
				}
				catch (InterruptedException e1) {}
			}
		}
	}
}