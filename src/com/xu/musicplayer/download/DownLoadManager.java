package com.xu.musicplayer.download;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;

import com.xu.musicplayer.threadpool.ThreadPoolManager;

public class DownLoadManager {

	private String fileName;//文件的名字
	private File   abstructFileSavePath;//文件的绝对路径

	private URL url;                   //要下载的文件的地址

	private long fileSizePerThread;//每个线程要下载的长度
	private long totalLength;//整个文件的长度
	private int  threadSize=2;//线程的数量

	private Notify myNotify;
	private ThreadPoolManager tpm;

	/**
	 * 构造方法
	 */
	public DownLoadManager(){

	}

	/**
	 * 构造方法
	 * @param url
	 * @param threadSize
	 */
	public DownLoadManager(URL url,int threadSize){
		this.url=url;
		this.threadSize=threadSize;
	}

	/**
	 * 构造方法
	 * @param url
	 * @param threadSize
	 * @param myNotify
	 */
	public DownLoadManager(URL url,int threadSize,Notify myNotify){
		this.url=url;
		this.threadSize=threadSize;
		this.myNotify=myNotify;
	}

	/**
	 * 构造方法
	 * @param url
	 * @param threadSize
	 * @param tpm
	 * @param myNotify
	 */
	public DownLoadManager(URL url,int threadSize,ThreadPoolManager tpm,Notify myNotify){
		this.url=url;
		this.threadSize=threadSize;
		this.myNotify=myNotify;
		this.tpm=tpm;
	}

	/**
	 * 开始下载
	 * @throws IOException
	 */
	public void startDownLoad() throws IOException{
		getFileName(url);                 //获取文件名字
		getFileLength(url);               //获取文件的长度
		createEmptyFile(fileName);        //创建一个同样长度的空文件
		getFileSizePerThread(totalLength);//获取每个线程要下载的长度
		if(totalLength<=10*1024*1024){
			Thread thread=new Thread(new DownLoadTask(url,abstructFileSavePath,totalLength,this.myNotify));
			thread.start();
		}else{
			for(int i=0;i<threadSize;i++){
				if(this.tpm!=null){
					tpm.process(new DownLoadTask(url,abstructFileSavePath,fileSizePerThread,i,this.myNotify));
				}else{
					Thread thread=new Thread(new DownLoadTask(url,abstructFileSavePath,fileSizePerThread,i,this.myNotify));
					thread.start();
				}
			}
		}
	}

	/**
	 * 获取每个线程要下载的长度
	 * @param totalLength
	 * @return
	 */
	private long getFileSizePerThread(long totalLength){//获取每个线程要下载的长度
		this.fileSizePerThread=this.totalLength%threadSize==0?this.totalLength/threadSize:this.totalLength/threadSize+10;
		return fileSizePerThread;
	}

	/**
	 * 获取文件名字
	 * @param url
	 * @return
	 */

	private String getFileName(URL url){//获取文件的名字
		fileName=url.toString().substring(url.toString().lastIndexOf("/")+1);
		return fileName;
	}

	/**
	 * 获取文件的长度
	 * @param url
	 * @return
	 * @throws IOException
	 */
	private long getFileLength(URL url) throws IOException{//获取文件的长度
		HttpURLConnection connection=(HttpURLConnection) url.openConnection();
		connection.setRequestMethod("HEAD");//获取http的头部而不获取整个文件
		connection.connect();               //建立连接
		this.totalLength=connection.getContentLengthLong();//获取文件的长度（以字节为单位）
		connection.disconnect();
		System.out.println("总长度:"+totalLength);
		return totalLength;
	}

	public long getLength(URL url) throws IOException{
		return getFileLength(url);
	}

	/**
	 * 创建一个同样长度的空文件
	 * @param fileName
	 * @throws IOException
	 */
	private void createEmptyFile(String fileName) throws IOException{//建立一个空文件
		abstructFileSavePath=new File(System.getProperty("user.home")+File.separator+fileName);
		int index=1;
		while(abstructFileSavePath.exists()){
			abstructFileSavePath=new File(System.getProperty("user.home")+File.separator+fileName.substring(0,fileName.lastIndexOf("."))+"["+index+"]"+fileName.substring(fileName.lastIndexOf(".")));
			index++;
		}
		System.out.println(abstructFileSavePath);
		RandomAccessFile raf=new RandomAccessFile(abstructFileSavePath, "rw");
		raf.setLength(totalLength);
		raf.close();
	}

}
