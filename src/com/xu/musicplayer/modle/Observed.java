package com.xu.musicplayer.modle;

import com.xu.musicplayer.entity.PlayerEntity;

/**
 * 抽象 被观察者 接口
 * @author hyacinth
 * @date 2019年10月10日12:00:09
 * 
 */
public interface Observed {

	public void start_lyric_player(Observer observer,PlayerEntity entity);

	public void start_spectrum(Observer observer,PlayerEntity entity);

	public void end_spectrum(Observer observer);

	public void end_lyric_player(Observer observer);

	public void stop_lyric_player(Observer observer);

}

