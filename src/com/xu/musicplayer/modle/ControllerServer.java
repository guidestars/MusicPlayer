package com.xu.musicplayer.modle;

import com.xu.musicplayer.entity.PlayerEntity;

/**
 * Java MusocPlayer 被观察者
 * @Author: hyacinth
 * @ClassName: LyricyServer   
 * @Description: TODO    
 * @Date: 2019年12月26日 下午8:03:42   
 * @Copyright: hyacinth
 */
public class ControllerServer implements Observed {
	
	@Override
	public void start_lyric_player(Observer observer,PlayerEntity entity) {
		observer.start(entity);
	}

	@Override
	public void end_lyric_player(Observer observer) {
		observer.end();
	}

	@Override
	public void stop_lyric_player(Observer observer) {
		observer.stop();
	}

	@Override
	public void start_spectrum(Observer observer, PlayerEntity entity) {
		observer.start(entity);
	}

	@Override
	public void end_spectrum(Observer observer) {
		observer.end();
	}
	
}

