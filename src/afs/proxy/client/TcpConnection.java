package afs.proxy.client;

import java.util.HashMap;
import java.net.Socket;
import java.io.OutputStream;
import java.io.IOException;

class TcpConnection
{
	private static HashMap<Integer, Socket> socketMap = new HashMap<Integer, Socket> ();

	public static synchronized void create (int id)
	{
		Socket tcpSocket = null;

		try
		{
			tcpSocket = new Socket (Globals.getTcpAddress (), Globals.getTcpPort ());
		}
		catch (IOException e) 
		{
			System.out.println ("Could not connect to TCP service on " + Globals.getTcpAddress () 
				+ " port " + Globals.getTcpPort ());
			//error = true;
			return;
		}
/*
		if (error)
		{
			try 
			{
				proxySocket.close();
			}
			catch (IOException e) {};
			return;
		}
*/
		socketMap.put (id, tcpSocket);

		TcpToProxyMovingThread tcpToProxyMovingThread = new TcpToProxyMovingThread (id, tcpSocket);
	}

	public static synchronized OutputStream getOutputStream (Integer id)
	{
		Socket socket = socketMap.get (id);
		if (socket == null) return null;

		OutputStream outputStream = null;

		try
		{
			return socket.getOutputStream();
		}
		catch (IOException e) 
		{
			return null;
		}

	}
}