package com.xu.musicplayer.lyric;

import com.xu.musicplayer.player.XMusic;
import com.xu.musicplayer.system.Constant;

public class LyricyThread extends Thread {

	private LyricyNotify notify;
	private long time = 0;
	private int index = 0;
	private int length = 0;
	private String secounds = "";

	private long merchant = 0;
	private long remainder = 0;
	private String format = "";
	private boolean add = true;

	public LyricyThread(LyricyNotify notify) {
		this.notify = notify;
	}

	@Override
	public void run() {
		length = Constant.PLAY_LYRIC.size();		
		while (XMusic.isPlaying() && index <= length) {
			for (int i = 0, len = Constant.PLAY_LYRIC.size(); i < len; i++) {
				secounds = Constant.PLAY_LYRIC.get(i).split(Constant.SPLIT)[0];
				if (secounds.startsWith("0")) {
					secounds = secounds.substring(0,secounds.lastIndexOf("."));
					if (secounds.equalsIgnoreCase(format_time(time))) {
						index = i;
						add = false;
					} else {
						if (add) {
							index ++;
						}
					}
				}
			}
			this.notify.lyric(index,time);
			time++;
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 时间转换
	 * @param time
	 * @return
	 */
	private String format_time(long time){
		merchant = time/60;
		remainder = time%60;
		if(time<10){
			format="00:0"+time;
		} else if(time<60){
			format="00:"+time;
		} else {
			if(merchant<10 && remainder<10){
				format="0"+merchant+":0"+remainder;
			}else if(merchant<10 && remainder<60){
				format="0"+merchant+":"+remainder;
			} else if(merchant>=10 && remainder<10) {
				format=merchant+":0"+remainder;
			} else if(merchant>=10 && remainder<60) {
				format=merchant+":0"+remainder;
			}
		}
		return format;
	}

}
