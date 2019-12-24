package com.xu.musicplayer.config;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.HashSet;

import com.xu.musicplayer.system.Constant;

public class Reading {

	public HashSet<String> read() {
		File file = new File(Constant.MUSIC_PLAYER_SONG_LISTS_PATH);
		if (file.exists() && file.isFile()) {
			HashSet<String> songs = new HashSet<String>();
			Constant.PLAY_LIST.clear();
			InputStreamReader FReader = null;
			BufferedReader BReader = null;
			String song = "";
			try(FileInputStream stream = new FileInputStream(file)) {
				FReader = new InputStreamReader(stream,"UTF-8");
				BReader = new BufferedReader(FReader);
				while ((song = BReader.readLine()) != null) {
					songs.add(song);
					Constant.PLAY_LIST.add(song);
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					if (FReader != null) {
						FReader.close();
					}
					if (BReader != null) {
						BReader.close();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			return songs;
		} else {
			return null;
		}

	}

	public boolean empty(String k) {
		if (k == null || k.length()<=0) {
			return true;
		} else {
			return false;
		}
	}

}
