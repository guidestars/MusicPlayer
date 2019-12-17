package com.xu.musicplayer.modle;

import com.xu.musicplayer.entity.PlayerEntity;

/**
 * 被观察者
 * @author hyacinth
 * @date 2019年10月10日12:00:09
 *
 */
public class LyricyServer implements Observed {
	
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
	
}

