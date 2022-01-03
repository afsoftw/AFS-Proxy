package afs.proxy.server;

import java.util.HashMap;
import java.net.Socket;

class ConnectionCounter
{
	private static Integer lastId = 0;
	private static HashMap<Integer, Socket> socketMap = new HashMap<Integer, Socket> ();

	public static synchronized Integer getNewConnectionId (Socket socket)
	{
		lastId += 1;
		socketMap.put (lastId, socket);
		return lastId;
	}

	public static synchronized Socket getConnectionSocket ( Integer id)
	{
		return socketMap.get (id);
	}
}