package afs.proxy.server;

import java.net.Socket;
import java.net.ServerSocket;
import java.io.IOException;

class ProxyClientListeningThread implements Runnable
{
	public Thread thread;

	private ServerSocket serverSocket;
	private boolean isForcedStop = false;
	private int port;

	ProxyClientListeningThread (int port)
	{
		this.port = port;
		this.thread = new Thread (this);
		this.thread.start();
	}

	public void run ()
	{
		Socket proxyClientSocket = null;
		TcpClientListeningThread tcpClientListeningThread = null;
		ProxyClientReadThread proxyClientReadThread = null;

		while (!this.getIsForcedStop ())
		{	
			if (proxyClientSocket != null)
			{
				if (proxyClientReadThread.getIsStopped ())
				{
					try
					{
						proxyClientSocket.close();
					}
					catch (IOException e) {}
					
					proxyClientSocket = null;
					ProxyClientPackageQueue.clear ();

					if (tcpClientListeningThread != null)
					{
						tcpClientListeningThread.stop ();
					}
					tcpClientListeningThread = null;

					continue;
				}
				else
				{
					try
					{
						Thread.sleep (1000);
					}
					catch (InterruptedException e) {}	
				}
			}
			else
			{
				try
				{
					this.serverSocket = new ServerSocket (port, 1);
				}
				catch (IOException e)
				{
					System.out.println (" Server socket error");
					return;
				}

				try
				{
					proxyClientSocket = this.serverSocket.accept ();
					this.serverSocket.close ();
				}
				catch (IOException e)
				{
					if (!this.getIsForcedStop ())
					{
						System.out.println ("Proxy client socket error");
					}
					return;
				}

				if (this.getIsForcedStop ()) return;

				tcpClientListeningThread = new TcpClientListeningThread (Globals.getTcpPort (), proxyClientSocket);
				proxyClientReadThread = new ProxyClientReadThread (proxyClientSocket);
			}
		}
	}

	private synchronized void setIsForcedStop ()
	{
		this.isForcedStop = true;
	}

	private synchronized boolean getIsForcedStop ()
	{
		return this.isForcedStop;	
	}

	public void onStop ()
	{
		System.out.println ("");
		System.out.print ("Shutting down...");
		this.setIsForcedStop ();

		try
		{
			Thread.sleep (100);
		}
		catch (InterruptedException e) {}

		try
		{
			this.serverSocket.close ();
		}
		catch (IOException e) {}

		System.out.println (" done");
	}	
}