package afs.proxy.server;

import java.net.Socket;
import java.net.ServerSocket;
import java.io.IOException;

class TcpClientListeningThread implements Runnable
{
	public Thread thread;

	private ServerSocket serverSocket;
	private Socket proxyClientSocket;
	private boolean isForcedStop = false;
	private int port;

	TcpClientListeningThread (int port, Socket proxyClientSocket)
	{
		this.port = port;
		this.proxyClientSocket = proxyClientSocket;
		this.thread = new Thread (this);
		this.thread.start();
	}

	public void run ()
	{
		try
		{
			this.serverSocket = new ServerSocket (port);
		}
		catch (IOException e)
		{
			System.out.println ("TCP server socket error");
			return;
		}

		Socket tcpClientSocket = null;

		while (!this.getIsForcedStop ())
		{
			try
			{
				tcpClientSocket = this.serverSocket.accept ();
			}
			catch (IOException e)
			{
				if (!this.getIsForcedStop ())
				{
					System.out.println ("TCP client socket error");
				}
				return;
			}

			if (this.getIsForcedStop()) return;

			Integer connectionId = ConnectionCounter.getNewConnectionId (tcpClientSocket);
			TcpClientReadThread TcpClientReadThread = new TcpClientReadThread (tcpClientSocket, this.proxyClientSocket, connectionId);
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

	public void stop ()
	{
		this.setIsForcedStop ();		

		try
		{
			this.serverSocket.close ();
		}
		catch (IOException e) {}

		ConnectionCounter.clear ();

		try
		{
			this.thread.sleep (1000);
		}
		catch (InterruptedException e) {}

		TcpClientPackageQueue.clear ();
	}
}