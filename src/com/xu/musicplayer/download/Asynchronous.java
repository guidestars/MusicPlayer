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
		ThreadPoolExecutor executor = new ThreadPoolExecutor(5, 10, 10, TimeUnit.MILLISECONDS,new ArrayBlockingQueue<Runnable>(5),Executors.defaultThreadFactory(),new ThreadPoolExecutor.AbortPolicy());
		for(int i=0;i<15;i++){
			DownLoadTask myTask = new DownLoadTask("","",1,1);
			executor.execute(myTask);
		}
		while(!executor.isTerminated()) {
			Thread.sleep(1000);
			System.out.println("线程池中任务数目："+executor.getPoolSize()+"，队列中等待执行的任务数目："+executor.getQueue().size()+"，已执行完成的任务数目："+executor.getCompletedTaskCount());
		}
		executor.shutdown();
	}

	public void download(String url,String name) {
		HttpURLConnection connection = null;
		try {
			connection = (HttpURLConnection) new URL(url).openConnection();
			connection.setRequestMethod("HEAD");
			connection.connect();
			length = connection.getContentLengthLong();

			RandomAccessFile raf=new RandomAccessFile(Constant.MUSIC_PLAYER_DOWNLOAD_PATH+name, "rw");
			raf.setLength(length);
			raf.close();
			
			task(url,Constant.MUSIC_PLAYER_DOWNLOAD_PATH+name,length);
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			connection.disconnect();
		}
	}
	
	public void task(String url,String path,long length) {
		ThreadPoolExecutor executor = new ThreadPoolExecutor(5, 10, 10, TimeUnit.MILLISECONDS,new ArrayBlockingQueue<Runnable>(5),Executors.defaultThreadFactory(),new ThreadPoolExecutor.AbortPolicy());
		if (length <= 10*1024*1024) {
			executor.execute(new DownLoadTask(url, path, 0, (int)length));
		} else {
			for (int i = 0,len = (int) (length/(10*1024*1024)); i < len; i++) {
				if (i == len-1) {
					
				} else {
					executor.execute(new DownLoadTask(url, path, i*(10*1024*1024),(i+1)*(10*1024*1024)));
				}
			}			
		}
		executor.shutdown();
	}

}


class DownLoadTask implements Runnable {

	private String url;
	private String path;
	private  long start;
	private  long end;

	public DownLoadTask(String url,String path,int start,int end) {
		this.url = url;
		this.path = path;
		this.start = start;
		this.end = end;
	}

	public void run() {
		BufferedInputStream inputStream=null;
		RandomAccessFile accessFile=null;

		try {
			accessFile=new RandomAccessFile(path, "rw");
			HttpURLConnection connection=(HttpURLConnection) new URL(url).openConnection();
			connection.setRequestMethod("GET");
			connection.setRequestProperty("Range", "bytes="+this.start+"-"+this.end);
			accessFile.seek(this.start);
			inputStream=new BufferedInputStream(connection.getInputStream());
			byte[] bt=new byte[10*1024];
			int length=0;
			while((length=inputStream.read(bt, 0, bt.length))!=-1){
				accessFile.write(bt, 0, length);
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
			if(accessFile!=null){
				try {
					accessFile.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

	}
}