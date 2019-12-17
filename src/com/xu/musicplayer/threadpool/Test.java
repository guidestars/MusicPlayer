package com.xu.musicplayer.threadpool;

import java.util.Date;

public class Test {
	
	public static void main(String[] args) {
		
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