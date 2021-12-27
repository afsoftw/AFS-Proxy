package afs.proxy.server;

import java.net.*;
import java.io.*;
import java.util.*;

class PackageSendThread implements Runnable
{
	public Thread thread;

	PackageSendThread ()
	{
		this.thread = new Thread (this);
		this.thread.start();
	}

	public void run ()
	{
		while (1==1) 
			if (PackageQueue.sendPackage () == 0) 
			{
				/*
				try
				{
					this.thread.sleep (100);
				}
				catch (InterruptedException e) {}
				*/
			}
	}
}