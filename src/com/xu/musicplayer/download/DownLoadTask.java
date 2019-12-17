package com.xu.musicplayer.download;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;

import com.xu.musicplayer.threadpool.ThreadPoolManager;

@SuppressWarnings(value = "all")
public class DownLoadTask implements Runnable{

	private File path;
	
	private URL url;
	
	private Notify notify;
	
	private int index;
	
	private long size;
	private long start;
	private long end;

	private ThreadPoolManager tpm;

	/**
	 * 构造方法
	 * @param url
	 * @param abstructFileSavePath
	 * @param fileSizePerThread
	 * @param i
	 * @param myNotify
	 */
	public DownLoadTask(URL url, File abstructFileSavePath, long fileSizePerThread, int i, Notify myNotify) {
		this.url=url;
		this.path=abstructFileSavePath;
		this.index=i;
		this.notify=myNotify;
		this.size=fileSizePerThread;
		
		this.start=fileSizePerThread*index;
		this.end=(index+1)*fileSizePerThread-1;
	}
	
	/**
	 * 构造方法
	 * @param url
	 * @param abstructFileSavePath
	 * @param fileSizePerThread
	 * @param i
	 * @param tpm
	 * @param myNotify
	 */
	public DownLoadTask(URL url, File abstructFileSavePath, long fileSizePerThread, int i,ThreadPoolManager tpm, Notify myNotify) {
		this.url=url;
		this.path=abstructFileSavePath;
		this.index=i;
		this.notify=myNotify;
		this.size=fileSizePerThread;
		
		this.start=index*fileSizePerThread;
		this.end=(index+1)*fileSizePerThread-1;
		this.tpm=tpm;
	}

	/**
	 * 构造方法
	 * 
	 * 
	 * @param url
	 * @param abstructFileSavePath
	 * @param totalLength
	 * @param myNotify
	 */
	public DownLoadTask(URL url, File abstructFileSavePath, long totalLength, Notify myNotify) {
		this.url=url;
		this.path=abstructFileSavePath;
		this.notify=myNotify;
		
		this.start=0;
		this.end=totalLength;
		
	}

	public void run() {
		BufferedInputStream inputStream=null;
		RandomAccessFile accessFile=null;
		
		try {
			accessFile=new RandomAccessFile(path, "rw");//随机文件流
			HttpURLConnection connection=(HttpURLConnection) url.openConnection();
			connection.setRequestMethod("GET");//设置http的获取方法
			connection.setRequestProperty("Range", "bytes="+this.start+"-"+this.end);//设定获取文件的位置
			accessFile.seek(this.start);//自定文件写入的位置
			
			System.out.println("bytes="+this.start+"-"+this.end);
			
			inputStream=new BufferedInputStream(connection.getInputStream());//缓冲写入流
			byte[] bt=new byte[10*1024];
			int length=0;
			while((length=inputStream.read(bt, 0, bt.length))!=-1){
				accessFile.write(bt, 0, length);
				if(this.notify!=null){
					synchronized (this) {
						this.notify.notifyResult(length);
					}
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (ProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			if(inputStream!=null){
				try {
					inputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if(accessFile!=null){
				try {
					accessFile.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
	}

}
