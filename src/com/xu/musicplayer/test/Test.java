package com.xu.musicplayer.test;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.media.ControllerEvent;
import javax.media.ControllerListener;
import javax.media.EndOfMediaEvent;
import javax.media.Manager;
import javax.media.MediaLocator;
import javax.media.NoPlayerException;
import javax.media.Player;
import javax.media.PrefetchCompleteEvent;
import javax.media.RealizeCompleteEvent;
import javax.media.StopEvent;

public class Test {
//
//
//	private Player player;
//	private File file;
//
//	public static void main(String[] args) throws Exception{
//		new Test().play();
//	}
//
//	public void play() throws NoPlayerException, MalformedURLException, IOException{
//
//		file=new File("G:\\KuGou\\刘德华 - 开心的马骝 - 2007中国巡回演唱会上海站.mp3");
//
//		if(file.exists()){ 
//
//			//			URL url=new URL("G:\\KuGou\\刘德华 - 开心的马骝 - 2007中国巡回演唱会上海站.mp3");
//			//MediaLocator locator = new MediaLocator("G:\\KuGou\\刘德华 - 开心的马骝 - 2007中国巡回演唱会上海站.mp3");
//
//			player=Manager.createPlayer(file.toURL());
//			player.addControllerListener(this);
//			player.prefetch();
//
//		}
//	}
//
//
//	@Override
//	public void controllerUpdate(ControllerEvent event) {
//
//		if (event instanceof RealizeCompleteEvent) {//实现完成事件
//			System.out.println("RealizeCompleteEvent");
//			
//			if(player.getState()==300){//表示播放器实例已经销毁
//				try {
//					player=Manager.createPlayer(file.toURL());
//				} catch (NoPlayerException | IOException e) {
//					e.printStackTrace();
//				}
//			}else if(player.getState()==400){//表示播放器暂停
//				
//			}
////			player.start();
//
//		} else if (event instanceof EndOfMediaEvent) {//歌曲播放结束事件
//			player.close();
//			System.out.println("EndOfMediaEvent");
//		} else if (event instanceof StopEvent) {//歌曲播放停止事件
//			System.out.println("StopEvent");
//		} else if (event instanceof PrefetchCompleteEvent) {
//			System.out.println("PrefetchCompleteEvent");//预取完成事件
//			
//			player.start();
//			
//		}
//
//
//
//	}
}
