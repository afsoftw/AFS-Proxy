package afs.proxy.server;

import java.net.*;
import java.io.*;
import java.util.*;

class TcpClientListeningThread implements Runnable
{
	public Thread thread;

	private ServerSocket serverSocket;
	private Socket proxyClientSocket;
	private boolean stopFlag;
	private int port;
	//private ArrayList<ServicingThread> servicingThreadList;

	TcpClientListeningThread (int port, Socket proxyClientSocket)
	{
		this.port = port;
		this.proxyClientSocket = proxyClientSocket;
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
			Socket tcpClientSocket = null;
			try
			{
				tcpClientSocket = this.serverSocket.accept ();
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

			TempTcpSocket.setSocket (tcpClientSocket);
			TcpClientReadThread TcpClientReadThread = new TcpClientReadThread (tcpClientSocket, this.proxyClientSocket);
			// вернуть обратно в ProxyClientServicingThread 
			ProxyClientReadThread proxyClientReadThread = new ProxyClientReadThread (this.proxyClientSocket);

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
/*
		System.out.println ("");
		System.out.print ("  Shutting down the validation node. Waiting for threads...");
		this.setStopFlag (true);

		try
		{
			Thread.sleep (1000);
		}
		catch (InterruptedException e) {}

		try
		{
			this.serverSocket.close ();
		}
		catch (IOException e) {}

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
		System.out.println (" done");
		*/
	}	
}