package com.xu.musicplayer.threadpool;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class Test {
	public static void main(String[] args) {
		//		ThreadPoolManager manager=new ThreadPoolManager(5);
		//		manager.process(new TimeTest());
		//		manager.process(new TimeTest());
		//		manager.process(new TimeTest());
		//		manager.process(new TimeTest());
		//		manager.process(new TimeTest());
		//		manager.process(new TimeTest());


	}




}
class TimeTest implements Runnable{

	public void run() {
		while(true){
			System.out.println(new Date());
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}