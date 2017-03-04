package com.xu.threadpool;

import java.util.Vector;

public class ThreadPoolManager {

	private Vector<SimpleThread> vector;
	private int initialSize=10;
	private int i;

	public void setInitialSize(int initialSize) {
		this.initialSize = initialSize;
	}

	/**
	 * 初始化线程池
	 * @param initialSize
	 */
	public ThreadPoolManager(int initialSize){
		this.initialSize = initialSize;
		vector=new Vector<SimpleThread>();
		for(int i=0;i<initialSize;i++){
			SimpleThread sthread=new SimpleThread(i+1);
			vector.add(sthread);			
			sthread.start();//启动线程
		}
	}
	
	public ThreadPoolManager(){
		
	}
	
	/**
	 * 关闭一些线程
	 */
	public void shutdown(){
		if(vector.size()>10){
			System.out.println("========================线程池过大 "+vector.size()+"系统正在回收线程=====================");
			flag:for(int j=vector.size();j>vector.size()-5;j--){
				SimpleThread newsthread=vector.get(j-1);
				if(newsthread.isRunning()==false){
					vector.remove(j-1);
					System.out.println("线程: "+newsthread.getThreadNumber()+" 已被回收");
					i--;
				}else{
					break flag;
				}
			}
			System.out.println("======================================================");
		}
	}

	/**
	 * 调度线程的方法
	 * @param task
	 */
	public void process(Runnable task){
		//循环所有的 vector 找到一个 runningFlag 为 false 的线程
		//找到之后 调用这个线程的 setTask（task）方法
		//调用setRunning（true）方法
		for(i=0;i<vector.size();i++){
			SimpleThread sthread=vector.get(i);
			if(sthread.isRunning()==false){
				System.out.println("线程: "+sthread.getThreadNumber()+" 开始运行");
				sthread.setTask(task);
				sthread.setRunning(true);
				return;
			}			
		}
		System.out.println("======================================================");
		System.out.println("当前线程池大小为:"+this.initialSize+" 已经使用完,自动扩容程序开始运行");
		System.out.println("======================================================");
		if(i==vector.size()){
			int temp=i+10;
			this.initialSize=temp;
			for(;i<temp;i++){
				SimpleThread thread=new SimpleThread(i+1);
				vector.add(thread);				
				thread.start();//启动线程
			}
			this.process(task);
		}
		shutdown();
	}

}
