package afs.proxy.server;

import java.util.HashMap;
import java.net.Socket;
import java.util.Set;
import java.util.Map.Entry;
import java.io.IOException;

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

	public static synchronized void clear ()
	{
		lastId = 0;

		Set<Entry<Integer, Socket>> set = socketMap.entrySet ();
		for (Entry<Integer, Socket> element : set)
		{
			Socket socket = element.getValue ();

			try
			{
				socket.close ();
			}
			catch (IOException e) {}
		}

		socketMap.clear ();
	}
}