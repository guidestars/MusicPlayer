package com.xu.musicplayer.thread;

import java.io.IOException;
import java.net.URL;

import javax.media.ControllerEvent;
import javax.media.ControllerListener;
import javax.media.EndOfMediaEvent;
import javax.media.Manager;
import javax.media.NoPlayerException;
import javax.media.Player;
import javax.media.PrefetchCompleteEvent;
import javax.media.RealizeCompleteEvent;
import javax.media.StopEvent;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

public class MediaThread implements ControllerListener {

	private Logger logger=LogManager.getLogger(MediaThread.class);

	private URL url;
	private Player player;
	
	public MediaThread(){
		
	}
	
	public void play(URL url){
		this.url=url;
		try {
			player=Manager.createPlayer(url);
			player.addControllerListener(this);
		} catch (NoPlayerException e) {
			e.printStackTrace();
			logger.error(e.getMessage());
		} catch (IOException e) {
			e.printStackTrace();
			logger.error(e.getMessage());
		}
	}
	
	public void controllerUpdate(ControllerEvent event) {
		if (event instanceof RealizeCompleteEvent) {//实现完成事件
			logger.info("实现完成事件");
			player.prefetch();
			if(player.getState()==300){//表示播放器实例已经销毁
				logger.info("播放器实例已经销毁");
				try {
					player=Manager.createPlayer(url);
				} catch (NoPlayerException | IOException e) {
					e.printStackTrace();
				}
			}else if(player.getState()==500){
				logger.info("播放器开始播放");
				player.close();
			}
		} else if (event instanceof EndOfMediaEvent) {//歌曲播放结束事件
			logger.info("歌曲播放结束事件");
			player.close();
		} else if (event instanceof StopEvent) {//歌曲播放停止事件
			logger.info("歌曲播放停止事件");
		} else if (event instanceof PrefetchCompleteEvent) {//预取完成事件
			logger.info("预取完成事件");
			player.start();
		}
	}

}
