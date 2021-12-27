package afs.proxy.server;

import java.io.*;
import java.util.*;

class PackageQueue
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
		}
		else if (dataPackage.type == 1)
		{
			try
			{
				dataPackage.getOutputStream().write (dataPackage.getByteData(), 0, dataPackage.getByteDataLen());
			}
			catch (IOException e) {}	
		}
		else if (dataPackage.type == 2)
		{
			PrintWriter printWriter = new PrintWriter (dataPackage.getOutputStream(), true);
			String jsonString = "{\"conid\":\"" + Integer.toString(dataPackage.getConnectionId())
					+ "\",\"length\":\"" + Integer.toString(dataPackage.getByteDataLen()) 
					+ "\",\"data\":\"" 
					+ dataPackage.getStringData() + "\"}";
			printWriter.println (jsonString);
		}

		packageMap.remove (pair.getKey ());

		return 1;
		
		/*
		SortedMap<String, DataPackage> packages = packageMap.subMap (connectionId + "-0001", connectionId + "-9999");
		Iterator iterator = packages.entrySet ().iterator ();

		while (iterator.hasNext ())
		{
			Map.Entry pair = (Map.Entry) iterator.next ();
			DataPackage dataPackage = (DataPackage) pair.getValue ();
			packageMap.remove (pair.getKey ());
			packages.remove (pair.getKey ());
			try
			{
				dataPackage.getOutputStream().write (dataPackage.getBuffer(), 0, dataPackage.getLen());
			}
			catch (IOException e) {}
		}
		*/
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