package com.xu.musicplayer.modle;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.wb.swt.SWTResourceManager;

import com.xu.musicplayer.entity.PlayerEntity;
import com.xu.musicplayer.lyric.LyricyNotify;
import com.xu.musicplayer.lyric.LyricyThread;
import com.xu.musicplayer.main.MusicPlayer;
import com.xu.musicplayer.system.Constant;

/**
 * 观察者
 * @author hyacinth
 * @date 2019年10月10日12:00:09
 *
 */
public class LyricPlayer implements Observer {

	private static LyricyThread thread = null;
	private int merchant = 0;
	private int remainder = 0;
	private String format = "";

	@Override
	public void start(PlayerEntity entity) {

		int length = Integer.parseInt(MusicPlayer.PLAYING_SONG.split(Constant.SPLIT)[3]);
		
		PlayerEntity.getBar().setMaximum(length);
		PlayerEntity.getBar().setSelection(0);
		
		if (thread != null) {
			//TODO:
		} else {
			thread = new LyricyThread(new LyricyNotify() {
				@Override
				public void lyric(double lrc,double pro) {
					Display.getDefault().asyncExec(new Runnable() {
						@Override
						public void run() {
							TableItem[] items = PlayerEntity.getTable().getItems();
							for (int i = 0; i < items.length; i++) {
								if (i == lrc) {
									items[i].setBackground(SWTResourceManager.getColor(SWT.COLOR_GRAY));//将选中的行的颜色变为蓝色
								} else {
									items[i].setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));//将选中的行的颜色变为蓝色
								} 
							}
							PlayerEntity.getBar().setSelection((int)pro);
							PlayerEntity.getText().setText(format_time((int)pro));
						}
					});
				}
			});
			thread.setDaemon(true);
			thread.start();
		}

	}

	/**
	 * 时间转换
	 * @param time
	 * @return
	 */
	private String format_time(int time){
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

	@SuppressWarnings("deprecation")
	@Override
	public void end() {
		if (thread != null) {
			thread.stop();
			thread = null;
		}
	}

	@Override
	public void stop() {
		if (thread != null) {
			synchronized (this) {
				try {
					thread.wait(1);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

}

