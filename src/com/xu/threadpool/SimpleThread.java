package com.xu.threadpool;

public class SimpleThread extends Thread {

	private int       threadNumber;   //线程编号
	private boolean   runningFlag;//线程状态
	private Runnable  task;      //任务
	private boolean   run=true;

	/**
	 * 任务是否结束
	 * @param stop
	 */
	public void stop(boolean stop){
		this.run=!stop;
	}

	/**
	 * 获取任务编号
	 * @return
	 */
	public int getThreadNumber(){
		return this.threadNumber;
	}

	/**
	 * 获取任务运行状态
	 * @return
	 */
	public boolean isRunning(){
		return this.runningFlag;
	}

	/**
	 * 启动线程
	 * @param runningFlag
	 */
	public synchronized void setRunning(boolean runningFlag){
		this.runningFlag=runningFlag;//资源锁，当前线程running后可以进入阻塞状态--》notify
		if(this.runningFlag){
			this.notifyAll();
		}
	}

	/**
	 * 得到正在执行的任务
	 * @return
	 */
	public Runnable getTask(){
		return this.task;
	}

	/**
	 * 设置任务
	 * @param task
	 */
	public void setTask(Runnable task){
		this.task=task;
	}
	/**
	 * 激活线程
	 * @param threadNumber
	 */
	public SimpleThread(int threadNumber){
		this.threadNumber=threadNumber;
		this.runningFlag=false;
		System.out.println("线程: "+threadNumber+" 已经激活、等待运行");
	}

	/**
	 * 开始运行
	 */
	public synchronized void run(){
		try {
			while(run){
				if(!runningFlag){
					this.wait();//当前的线程还不能启动，则让当前的线程进入wait()，这样不会耗费cpu
				}else{					
					System.out.println("线程: "+this.threadNumber+" 开始运行");
					if(this.task!=null){
						this.task.run();//线程要运行
					}
					Thread.sleep(1000); //当task任务运行完毕  则休息一下
					System.out.println("线程: "+threadNumber+" 重新进入等待状态");
					this.setRunning(false);
				}
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
