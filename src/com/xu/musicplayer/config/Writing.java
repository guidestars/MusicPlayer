package com.xu.musicplayer.config;

import com.xu.musicplayer.system.Constant;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;

public class Writing {

    public static void main(String[] args) {
        Writing writing = new Writing();
        List<String> list = new ArrayList<String>();
        list.add("F:\\KuGou\\丸子呦 - 广寒宫.mp3<-->Y");
        writing.write(list);
        for (String l : Constant.MUSIC_PLAYER_SONGS_LIST) {
            System.out.println(l);
        }
    }


    public void write(List<String> lists) {
        File file = new File(Constant.MUSIC_PLAYER_SONG_LISTS_FULL_PATH);
        HashSet<String> songs = new HashSet<String>();
        if (file.exists()) {
            songs = new Reading().read();
            String content;
            String[] splits;
            for (String list : lists) {
                splits = list.split(Constant.MUSIC_PLAYER_SYSTEM_SPLIT);
                content = splits[0];
                try {
                    content += Constant.MUSIC_PLAYER_SYSTEM_SPLIT + get_song_name(splits[0]);
                    content += Constant.MUSIC_PLAYER_SYSTEM_SPLIT + get_song_name(splits[0]);
                    content += Constant.MUSIC_PLAYER_SYSTEM_SPLIT + get_song_Length(splits[0]);
                    content += Constant.MUSIC_PLAYER_SYSTEM_SPLIT + splits[1];
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
                songs.add(content);
            }
        } else {
            try {
                new File(Constant.MUSIC_PLAYER_SONG_LISTS_PATH).mkdirs();
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            String content;
            String[] splits;
            for (String list : lists) {
                splits = list.split(Constant.MUSIC_PLAYER_SYSTEM_SPLIT);
                content = splits[0];
                try {
                    content += Constant.MUSIC_PLAYER_SYSTEM_SPLIT + get_song_name(splits[0]);
                    content += Constant.MUSIC_PLAYER_SYSTEM_SPLIT + get_song_name(splits[0]);
                    content += Constant.MUSIC_PLAYER_SYSTEM_SPLIT + get_song_Length(splits[0]);
                    content += Constant.MUSIC_PLAYER_SYSTEM_SPLIT + splits[1];
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
                songs.add(content);
            }
        }
        FileWriter FWriter = null;
        BufferedWriter BWriter;
        try {
            FWriter = new FileWriter(new File(Constant.MUSIC_PLAYER_SONG_LISTS_FULL_PATH));
            BWriter = new BufferedWriter(FWriter);
            Constant.MUSIC_PLAYER_SONGS_LIST.clear();
            for (String song : songs) {
                BWriter.write(song);
                Constant.MUSIC_PLAYER_SONGS_LIST.add(song);
                BWriter.newLine();
            }
            BWriter.flush();
            BWriter.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (FWriter != null) {
                    FWriter.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private String get_song_name(String song) {
        song = song.replace("/", "\\");
        return song.substring(song.lastIndexOf("\\") + 1, song.lastIndexOf("."));
    }

    private int get_song_Length(String path) {
        File file = new File(path);
        AudioFile mp3 = null;
        try {
            mp3 = AudioFileIO.read(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Objects.requireNonNull(mp3).getAudioHeader().getTrackLength();
    }

}
