package afs.proxy.server;

import java.net.Socket;

class TempTcpSocket
{
	private static Socket static_socket;

	public static synchronized void setSocket (Socket socket)
	{
		System.out.println("setSocket()");
		static_socket = socket;
	}

	public static synchronized Socket getSocket ()
	{
		System.out.println("getSocket()");
		return static_socket;	
	}
}