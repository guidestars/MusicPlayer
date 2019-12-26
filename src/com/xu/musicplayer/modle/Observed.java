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

	public void start_lyric_player(Observer observer,PlayerEntity entity);

	public void start_spectrum(Observer observer,PlayerEntity entity);

	public void end_spectrum(Observer observer);

	public void end_lyric_player(Observer observer);

	public void stop_lyric_player(Observer observer);

}

