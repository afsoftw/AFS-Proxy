package afs.proxy.server;

import java.io.*;

class DataPackage
{
	private OutputStream stream;
	private byte[] byteData;	
	private String stringData;
	private int byteDataLen;
	
	public int type;

	DataPackage (OutputStream stream)
	{
		this.stream = stream;
	}

	public OutputStream getOutputStream ()
	{
		return this.stream;
	}

	public void setByteData (byte[] data, int len)
	{
		this.byteData = new byte[len];
		System.arraycopy (data, 0, this.byteData, 0, len);
		this.byteDataLen = len;
	}

	public byte[] getByteData ()
	{
		return this.byteData;
	}

	public void setStringData (String data)
	{
		this.stringData = data;
	}

	public String getStringData ()
	{
		return this.stringData;
	}

	public void setByteDataLen (int len)
	{
		this.byteDataLen = len;
	}

	public int getByteDataLen ()
	{
		return this.byteDataLen;
	}
}