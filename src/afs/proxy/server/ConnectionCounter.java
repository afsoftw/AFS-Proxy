package afs.proxy.server;

import java.util.*;
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
/*
	public static synchronized String getNextConnectionId (String nodeId)
	{
		String time = Long.toHexString (new Date ().getTime()).toUpperCase ();
		if ((time.length () & 1) != 0) time = "0" + time;
		String connectionBase = nodeId + time;

		Integer id = counterMap.get (connectionBase);
		if (id != null)
		{
			id++;
			counterMap.replace (connectionBase, id);
		}
		else
		{
			id = new Integer (1);
			counterMap.put (connectionBase, id);
		}

		String connectionId = Integer.toHexString (id).toUpperCase ();
		if ((connectionId.length () & 1) != 0) connectionId = "0" + connectionId;
		connectionId = connectionBase + connectionId;
		return connectionId;
	}
*/

}