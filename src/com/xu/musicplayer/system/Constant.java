package com.xu.musicplayer.system;

import java.awt.Color;
import java.io.File;
import java.util.LinkedList;

public class Constant {
	
	public static final String SYSTEM_USER_HOME = System.getProperties().getProperty("user.home");
	
	public static final String SYSTEM_USER_NAME = System.getProperties().getProperty("user.name");

	public static final String MUSIC_PLAYER_SONG_LISTS_NAME = "MusicPlayer.song";
	public static final String MUSIC_PLAYER_SONG_LISTS_PATH = SYSTEM_USER_HOME+File.separator+".MusicPlayer"+File.separator+MUSIC_PLAYER_SONG_LISTS_NAME;
	
	public static final String MUSIC_PLAYER_LOG = "Log.log";
	
	public static final String SPLIT = "<-->";
	
	public static LinkedList<String> PLAY_LIST = new LinkedList<String>();
	public static LinkedList<String> PLAY_TEMP_LIST = new LinkedList<String>();
	
	public static LinkedList<String> PLAY_LYRIC = new LinkedList<String>();
	
	public static int PLAY_INDEX = 0;
	
	public static boolean PLAY_STATE = true;
	
	public static volatile Color SPECTRUM_BACKGROUND_COLOR = Color.WHITE;
	
	public static volatile Color SPECTRUM_COLOR = Color.PINK;
	
}
