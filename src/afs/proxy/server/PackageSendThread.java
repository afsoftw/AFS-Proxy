package afs.proxy.server;

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
		int i = 0;

		while (1 == 1)
			if (PackageQueue.sendPackage () == 0)
			{
				i++;
				if (i >= 100000000)
				{
					try
					{
						this.thread.sleep (100);
					}
					catch (InterruptedException e) {}
				}
			}
			else i = 0;
	}
}