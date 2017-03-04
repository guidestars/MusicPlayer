package com.xu.musicplayer.thread;

import java.util.List;

import com.xu.musicplayer.bean.PlayerBean;
import com.xu.musicplayer.bean.PlayerNotify;
import com.xu.musicplayer.main.MusicPlayer;

public class PlayerThread implements Runnable {


	private PlayerNotify playerNotify;

	private int autoProBarTime=0;   //进度条的时间
	private int autoLyricTime=0;    //歌词的时间
	private int autoShowTime=0;     //文本框显示的歌曲播放的时间

	private List<String> LYRICLISTS;
	private boolean HAVELYRIC;
	private int time;

	private boolean isFinished=false;
	private String     returnIndex="";
	private String  returnTime="";
	private int     returnLong=0;

	public PlayerThread(int time,List<String> LYRICLISTS,boolean HAVELYRIC,PlayerNotify playerNotify){
		this.playerNotify=playerNotify;
		this.LYRICLISTS=LYRICLISTS;
		this.HAVELYRIC=HAVELYRIC;
		this.time=time;
	}

	public void run() {
		for(int i=0;i<=time;i++){

			autoProBarTime++; //进度条
			autoLyricTime++;  //歌词的时间
			autoShowTime++;   //文本框显示的歌曲播放的时间

			if(HAVELYRIC){//如果该歌曲有歌词
				for(int j=0,len=LYRICLISTS.size();j<len;j++){
					if(getTime(autoLyricTime).equals(LYRICLISTS.get(j).substring(0, 5))){
						returnIndex=j+"";//LYRICLISTS.get(j).substring(5);
						break;
					}
				}
			}else{
				if(new MusicPlayer().lyric()!=null && "".equals(new MusicPlayer().lyric())){
					LYRICLISTS=new MusicPlayer().lyric();
					HAVELYRIC=true;
				}
			}

			returnLong=(int)((autoProBarTime+0.00)/time*100);//返回进度条的长度
			
			if(autoShowTime%60<10){
				returnTime=autoShowTime/60+" : 0"+autoShowTime%60+" / "+time/60+" : "+time%60;//在文本框中显示剩余时间
			}else{
				returnTime=autoShowTime/60+" : "+autoShowTime%60+" / "+time/60+" : "+time%60;//在文本框中显示剩余时间
			}
			if(i>=(time)){
				isFinished=true;
			}

			PlayerBean playerBean=new PlayerBean(returnIndex, returnTime, returnLong, isFinished);

			if(playerNotify!=null){
				this.playerNotify.PlayerResult(playerBean);
			}
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
	public String getTime(int time){//返回时间类型  01:10
		String temp="";
		if(time%2<10){
			temp=time/60+":0"+time%2;
		}else{
			temp=time/60+":"+time%2;
		}
		if(time<60){
			if(time<10){
				temp="00:0"+time;
			}else{
				temp="00:"+time;
			}
		}else{
			if(time/60>=10){
				if(time%60<10){
					temp="0"+time/60+":0"+time%60;
				}else{
					temp=+time/60+":"+time%60;
				}
			}else{
				if(time%60<10){
					temp="0"+time/60+":0"+time%60;
				}else{
					temp="0"+time/60+":"+time%60;
				}
			}
		}
		return temp;
	}

}
