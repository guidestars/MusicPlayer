package com.xu.musicplayer.config;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;

import com.xu.musicplayer.system.Constant;

public class Writing {

	public static void main(String[] args) {
		Writing writing = new Writing();
		List<String> list = new ArrayList<String>();
		list.add("F:\\KuGou\\丸子呦 - 广寒宫.mp3<-->Y");
		writing.write(list);
		for (String l:Constant.PLAY_LIST) {
			System.out.println(l);
		}
	}

	
	public boolean write(List<String> lists) {
		File file = new File(Constant.MUSIC_PLAYER_SONG_LISTS_PATH);
		HashSet<String> songs = new HashSet<String>();
		if (file.exists()) {			
			songs = new Reading().read();
			String content = "";
			String[] splits = null;
			for (String list:lists) {
				splits = list.split(Constant.SPLIT);
				content = splits[0];
				try {
					content += Constant.SPLIT + get_song_name(splits[0]);
					content += Constant.SPLIT + get_song_name(splits[0]);
					content += Constant.SPLIT + get_song_Length(splits[0]);
					content += Constant.SPLIT + splits[1];
				} catch (Exception e1) {
					e1.printStackTrace();
				}
				songs.add(content);
			}
		} else {
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
			String content = "";
			String[] splits = null;
			for (String list:lists) {
				splits = list.split(Constant.SPLIT);
				content = splits[0];
				try {
					content += Constant.SPLIT + get_song_name(splits[0]);
					content += Constant.SPLIT + get_song_name(splits[0]);
					content += Constant.SPLIT + get_song_Length(splits[0]);
					content += Constant.SPLIT + splits[1];
				} catch (Exception e1) {
					e1.printStackTrace();
				}
				songs.add(content);
			}
		}
		FileWriter FWriter = null;
		BufferedWriter BWriter = null;
		try {
			FWriter = new FileWriter(Constant.MUSIC_PLAYER_SONG_LISTS_PATH);
			BWriter = new BufferedWriter(FWriter);
			Constant.PLAY_LIST.clear();
			for (String song:songs) {
				BWriter.write(song);
				Constant.PLAY_LIST.add(song);
				BWriter.newLine();
			}
			BWriter.flush();
			BWriter.close();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			try {
				if (FWriter != null) {
					FWriter.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return true;
	}

	private String get_song_name(String song) {
		song = song.replace("/", "\\");
		return song.substring(song.lastIndexOf("\\")+1, song.lastIndexOf("."));
	}

	private int get_song_Length(String path) {
		File file=new File(path);
		AudioFile mp3 = null;
		try {
			mp3 = AudioFileIO.read(file);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mp3.getAudioHeader().getTrackLength();
	}
	
}
