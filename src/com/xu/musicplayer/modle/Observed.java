package com.xu.musicplayer.modle;

import com.xu.musicplayer.entity.PlayerEntity;
/**
 * Java MusicPlayer 抽象 被观察者 接口
 * @Author: hyacinth
 * @ClassName: Observed   
 * @Description: TODO    
 * @Date: 2019年12月26日 下午8:02:38   
 * @Copyright: hyacinth
 */
public interface Observed {

	public void startLyricPlayer(Observer observer,PlayerEntity entity);

	public void startSpectrumPlayer(Observer observer,PlayerEntity entity);

	public void endLyricPlayer(Observer observer);

	public void endSpectrumPlayer(Observer observer);

	public void stopLyricPlayer(Observer observer);
	
	public void stopSpectrumPlayer(Observer observer);

}

