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
		boolean lastNotEmpty = false;
		int sleepTime = 100;
		int i = 0;

		while (1 == 1)
		{
			if (PackageQueue.sendPackage () == 1)
			{
				if (lastNotEmpty)
				{
					sleepTime = decSleepTime(sleepTime);
				}

				lastNotEmpty = true;
			}
			else 
			{
				if (!lastNotEmpty)
				{
					i++;

					if (i >= 100)
					{
						sleepTime = incSleepTime(sleepTime);
						i = 0;
					}
				}
				else i = 0;

				lastNotEmpty = false;
			}

			if (sleepTime > 0)
				try
				{
					this.thread.sleep (sleepTime);
				}
				catch (InterruptedException e) {}
		}
	}

	private int decSleepTime(int time)
	{
		switch(time)
		{
			case (100):
				return 50;

			case (50):
				return 25;

			case (25):
				return 12;

			case (12):
				return 6;

			case (6):
				return 3;

			case (3):
				return 1;

			case (1):
				return 0;

			case (0):
				return 0;				
		}

		return 100;
	}

	private int incSleepTime(int time)
	{
		switch(time)
		{
			case (0):
				return 1;

			case (1):
				return 3;

			case (3):
				return 6;

			case (6):
				return 12;

			case (12):
				return 25;

			case (25):
				return 50;

			case (50):
				return 100;

			case (100):
				return 100;
		}

		return 100;
	}
}