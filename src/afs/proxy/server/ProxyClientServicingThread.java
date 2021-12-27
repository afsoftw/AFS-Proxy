package afs.proxy.server;

import java.net.*;
import java.io.*;
import java.util.*;

class ProxyClientServicingThread implements Runnable
{
	public Thread thread;

	private Socket proxyClientSocket;

	ProxyClientServicingThread (Socket proxyClientSocket)
	{
		this.proxyClientSocket = proxyClientSocket;
		this.thread = new Thread (this);
		this.thread.start();
	}

	public void run ()
	{
		TcpClientListeningThread tcpClientListeningThread = new TcpClientListeningThread (10101, this.proxyClientSocket);
		ProxyClientReadThread proxyClientReadThread = new ProxyClientReadThread (this.proxyClientSocket);
	}
}