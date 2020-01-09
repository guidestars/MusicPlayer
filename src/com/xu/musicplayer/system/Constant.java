package com.xu.musicplayer.system;

import java.awt.*;
import java.io.File;
import java.util.LinkedList;

public class Constant {

    public static final String SYSTEM_USER_HOME = System.getProperties().getProperty("user.home");

    public static final String SYSTEM_USER_NAME = System.getProperties().getProperty("user.name");

    public static final String MUSIC_PLAYER_SONG_LISTS_NAME = "MusicPlayer.song";
    public static final String MUSIC_PLAYER_SONG_LISTS_PATH = SYSTEM_USER_HOME + File.separator + ".MusicPlayer" + File.separator + MUSIC_PLAYER_SONG_LISTS_NAME;

    public static final String MUSIC_PLAYER_DOWNLOAD_PATH = SYSTEM_USER_HOME + File.separator + ".MusicPlayer" + File.separator + "download" + File.separator;

    public static final String MUSIC_PLAYER_LOG = "Log.log";

    public static final String SPLIT = "<-->";

    public static LinkedList<String> PLAY_LIST = new LinkedList<String>();
    public static LinkedList<String> PLAY_TEMP_LIST = new LinkedList<String>();

    public static LinkedList<String> PLAY_LYRIC = new LinkedList<String>();

    public static volatile boolean HAVE_LYRIC = false;
    
    public static volatile boolean START_LYRIC = true;
    public static volatile boolean START_SPECTRUM = true;

    public static int PLAY_INDEX = 0;
    
    public static int SONG_LENGTH = 0;
    
    public static String PLAYING_SONG = "";

    public static boolean PLAY_STATE = true;

    public static volatile Color SPECTRUM_BACKGROUND_COLOR = Color.WHITE;

    public static volatile Color SPECTRUM_COLOR = Color.PINK;

    public static volatile int SPECTRUM_WIDTH = 0;

    public static volatile int SPECTRUM_HEIGHT = 0;

    public static volatile int SPECTRUM_NUMBER = 0;

    public static volatile int SPECTRUM_SPLIT = 5;

    public static volatile int DOWNLOAD_CORE_POOL_SIZE = 10;

    public static volatile int DOWNLOAD_MAX_POOL_SIZE = 15;

    public static volatile long DOWNLOAD_FILE_SIZE_PER_THREAD = 10 * 1024 * 1024;//每个线程下载10M

}
