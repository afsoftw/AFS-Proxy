package afs.proxy.server;

import java.io.*;

class DataPackage
{
	private OutputStream stream;
	private byte[] buf;
	private int len;

	DataPackage (OutputStream stream, byte[] buf, int len)
	{
		this.stream = stream;
		this.buf = buf;
		this.len = len;
	}

	public OutputStream getOutputStream ()
	{
		return this.stream;
	}

	public byte[] getBuffer ()
	{
		return this.buf;
	}

	public int getLen ()
	{
		return this.len;
	}
}