package afs.proxy.server;

import java.net.Socket;
import java.net.ServerSocket;
import java.io.IOException;

class ProxyClientListeningThread implements Runnable
{
	public Thread thread;

	private ServerSocket serverSocket;
	private boolean stopFlag;
	private int port;

	ProxyClientListeningThread (int port)
	{
		this.port = port;
		this.setStopFlag (false);
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
			System.out.println ("error");
			return;
		}

		while (!this.getStopFlag ())
		{	
			Socket proxyClientSocket = null;
			try
			{
				proxyClientSocket = this.serverSocket.accept ();
			}
			catch (IOException e)
			{
				if (!this.getStopFlag ())
				{
					System.out.println ("error");
				}
				return;
			}

			if (this.getStopFlag ()) return;
/*
			Socket remoteSocket = null;

			try
			{
				remoteSocket = new Socket ("127.0.0.1", 22);
			}
			catch (IOException e) {}
*/
			ProxyClientServicingThread proxyClientServicingThread = new ProxyClientServicingThread (proxyClientSocket);
			//RemoteSocketReadThread remoteSocketReadThread = new RemoteSocketReadThread (localSocket, remoteSocket);
//			this.servicingThreadList.add (servicingThread);
/*
			ListIterator<ServicingThread> iterator = this.servicingThreadList.listIterator ();
			while (iterator.hasNext ())
			{
				servicingThread = iterator.next ();
				if (!servicingThread.thread.isAlive ()) iterator.remove ();
			}
*/
		}
	}

	public synchronized void setStopFlag (boolean value)
	{
		this.stopFlag = value;
	}

	public synchronized boolean getStopFlag ()
	{
		return this.stopFlag;	
	}

	public void onStop ()
	{
		System.out.println ("");
		System.out.print ("Shutting down...");
		this.setStopFlag (true);

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

/*
		ServicingThread servicingThread;
		int i = 0;
		while (this.servicingThreadList.size () != 0)
		{
			servicingThread = this.servicingThreadList.get (i);
			if (!servicingThread.thread.isAlive ())
			{
				this.servicingThreadList.remove (i);
				if (i >= this.servicingThreadList.size ()) i = 0;
				//System.out.println (i);
			}
			else
			{
				if (i >= this.servicingThreadList.size () - 1) i = 0;
				else i++;
			}
		}
*/
		System.out.println (" done");
	}	
}