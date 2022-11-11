package afs.proxy.server;

import java.io.PrintWriter;
import java.util.Map;
import java.util.TreeMap;
import java.util.HashMap;
import java.io.IOException;

class TcpClientPackageQueue
{
	private static TreeMap<String, DataPackage> packageMap = new TreeMap<String, DataPackage> ();
	private static HashMap<String, Integer> counterMap = new HashMap<String, Integer> ();

	public static synchronized void addPackage (String connectionId, DataPackage dataPackage)
	{
		packageMap.put (connectionId + "-" + getNextPackageId (connectionId), dataPackage);
	}

	public static synchronized int sendPackage ()
	{
		Map.Entry pair = packageMap.firstEntry();
		if (pair == null) return 0;
		DataPackage dataPackage = (DataPackage) pair.getValue ();

		if (dataPackage.type == 0)
		{
			PrintWriter printWriter = new PrintWriter (dataPackage.getOutputStream(), true);
			String jsonString = "{\"init\":\"true\",\"conid\":\"" 
					+ Integer.toString(dataPackage.getConnectionId()) +"\"}";
			printWriter.println (jsonString);

			//System.out.println ("0 >> " + Integer.toString(dataPackage.getByteDataLen()));
		}
		else if (dataPackage.type == 2)
		{
			PrintWriter printWriter = new PrintWriter (dataPackage.getOutputStream(), true);
			String jsonString = "{\"conid\":\"" + Integer.toString(dataPackage.getConnectionId())
					+ "\",\"length\":\"" + Integer.toString(dataPackage.getByteDataLen()) 
					+ "\",\"data\":\"" 
					+ dataPackage.getStringData() + "\"}";
			printWriter.println (jsonString);

			//System.out.println ("2 >> " + Integer.toString(dataPackage.getByteDataLen()));
		}

		packageMap.remove (pair.getKey ());

		return 1;
	}

	private static String getNextPackageId (String connectionId)
	{
		Integer id = counterMap.get (connectionId);
		if (id != null && id <= 9999)
		{
			id++;
			counterMap.replace (connectionId, id);
		}
		else
		{
			id = new Integer (1);
			counterMap.put (connectionId, id);
		}

		String packageId = Integer.toString (id);
		switch (packageId.length ())
		{
			case 1:
				packageId = "000" + packageId;
				break;
			case 2:
				packageId = "00" + packageId;
				break;
			case 3:
				packageId = "0" + packageId;
				break;
		}

		return packageId;
	}
}