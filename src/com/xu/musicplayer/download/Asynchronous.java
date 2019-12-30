package com.xu.musicplayer.download;

import java.io.BufferedInputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.xu.musicplayer.system.Constant;


public class Asynchronous {

	private long length = 0;

	public static void main(String[] args) throws InterruptedException {
		new Asynchronous().download(new DownloadNotify() {
			@Override
			public void result(Object object) {
				
			}
		},"http://down.360safe.com/yunpan/360wangpan_setup_6.6.0.1307.exe", "a");
	}

	public void download(DownloadNotify notify,String url) {
		String name =url.substring(url.lastIndexOf("/")+1, url.length());
		HttpURLConnection connection = null;
		try {
			connection = (HttpURLConnection) new URL(url).openConnection();
			connection.setRequestMethod("HEAD");
			connection.connect();
			length = connection.getContentLengthLong();

			RandomAccessFile raf=new RandomAccessFile(Constant.MUSIC_PLAYER_DOWNLOAD_PATH, "rw");
			raf.setLength(length);
			raf.close();
			connection.disconnect();

			task(notify,url,Constant.MUSIC_PLAYER_DOWNLOAD_PATH + name,length);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void download(DownloadNotify notify,String url,String name) {
		name +=url.substring(url.lastIndexOf("."), url.length());
		HttpURLConnection connection = null;
		try {
			connection = (HttpURLConnection) new URL(url).openConnection();
			connection.setRequestMethod("HEAD");
			connection.connect();
			length = connection.getContentLengthLong();

			RandomAccessFile raf=new RandomAccessFile(Constant.MUSIC_PLAYER_DOWNLOAD_PATH, "rw");
			raf.setLength(length);
			raf.close();
			connection.disconnect();

			task(notify,url,Constant.MUSIC_PLAYER_DOWNLOAD_PATH + name,length);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void task(DownloadNotify notify,String url,String path,long length) {
		ThreadPoolExecutor executor = new ThreadPoolExecutor(Constant.DOWNLOAD_CORE_POOL_SIZE, Constant.DOWNLOAD_MAX_POOL_SIZE, 10, TimeUnit.MILLISECONDS,new ArrayBlockingQueue<Runnable>(5),Executors.defaultThreadFactory(),new ThreadPoolExecutor.AbortPolicy());
		if (length <= 10*1024*1024) {
			executor.execute(new DownLoadTask(notify, url, path, 0, length));
		} else {
			for (long i = 0,len = length/(10*1024*1024); i < len; i++) {
				if (i == len-1) {
					executor.execute(new DownLoadTask(notify, url, path, i*(10*1024*1024),length-i*(10*1024*1024)));
				} else {
					executor.execute(new DownLoadTask(notify, url, path, i*(10*1024*1024),(i+1)*(10*1024*1024)-1));
				}
			}			
		}
		executor.shutdown();
	}

}


class DownLoadTask implements Runnable {

	private DownloadNotify notify;
	private String url;
	private String path;
	private  long start;
	private  long end;

	public DownLoadTask(DownloadNotify notify,String url,String path,long start,long end) {
		this.notify = notify;
		this.url = url;
		this.path = path;
		this.start = start;
		this.end = end;
	}

	public void run() {
		BufferedInputStream inputStream=null;
		RandomAccessFile access=null;
		HttpURLConnection connection = null;
		try {
			access=new RandomAccessFile(path, "rw");
			connection = (HttpURLConnection) new URL(url).openConnection();
			connection.setRequestMethod("GET");
			connection.setRequestProperty("Range", "bytes="+this.start+"-"+this.end);
			access.seek(this.start);
			inputStream=new BufferedInputStream(connection.getInputStream());
			byte[] bt=new byte[10*1024];
			int length=0;
			while((length=inputStream.read(bt, 0, bt.length))!=-1){
				access.write(bt, 0, length);
				if (notify != null) {
					synchronized (this) {
						this.notify.result(length);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(inputStream!=null){
				try {
					inputStream.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			if(access!=null){
				try {
					access.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			if (connection != null) {
				connection.disconnect();				
			}
		}

	}
}