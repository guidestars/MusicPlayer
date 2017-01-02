package com.xu.musicplayer.download;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import com.xu.threadpool.ThreadPoolManager;

public class Test {
	
	private static long length;

	public static void main(String[] args) throws MalformedURLException {
		//http://download.phoenixos.com/os/PhoenixOSInstaller-v1.1.2-226.exe
		//http://down.360safe.com/yunpan/360wangpan_setup_6.6.0.1307.exe
		URL url=new URL("http://down.360safe.com/yunpan/360wangpan_setup_6.6.0.1307.exe");
		ThreadPoolManager tpm=new ThreadPoolManager(10);
		try {
			new DownLoadManager(url,10,tpm,new MyNotify() {
				public synchronized void notifyResult(Object object) {
					long size=Long.parseLong(object.toString());
					length+=size;
					System.out.println("已下载:"+length);
					//16,519,168
				}
			}).startDownLoad();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/*public static void main(String[] args) {
		int a=21;
		int b=223;
		System.out.println((a+0.00)/b);
	}*/
}
