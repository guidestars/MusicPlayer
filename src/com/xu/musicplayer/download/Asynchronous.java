package com.xu.musicplayer.download;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class Asynchronous {

public static void main(String[] args) throws InterruptedException {
		
		ThreadPoolExecutor executor = new ThreadPoolExecutor(5, 10, 10, TimeUnit.MILLISECONDS,new ArrayBlockingQueue<Runnable>(5),Executors.defaultThreadFactory(),new ThreadPoolExecutor.AbortPolicy());

		for(int i=0;i<15;i++){
			MyTask myTask = new MyTask(i);
			executor.execute(myTask);
		}
		
		while(!executor.isTerminated()) {
			Thread.sleep(1000);
			System.out.println("线程池中任务数目："+executor.getPoolSize()+"，队列中等待执行的任务数目："+executor.getQueue().size()+"，已执行完成的任务数目："+executor.getCompletedTaskCount());
		}
		
		executor.shutdown();
		
	}
}


class MyTask implements Runnable {
	private int taskNum;

	public MyTask(int num) {
		this.taskNum = num;
	}

	public void run() {
		System.out.println("正在执行task "+taskNum);
		try {
			Thread.sleep(taskNum*1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("task "+taskNum+"执行完毕");
	}
}