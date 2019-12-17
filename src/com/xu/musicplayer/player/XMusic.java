package com.xu.musicplayer.player;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.SourceDataLine;

import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;

import javax.sound.sampled.AudioFormat.Encoding;

import com.xu.musicplayer.modle.LyricyServer;
import com.xu.musicplayer.modle.LyricPlayer;

import javazoom.spi.mpeg.sampled.file.MpegAudioFileReader;

public class XMusic implements Player {

	private static DataLine.Info info = null;
	private static AudioFormat format = null;
	private static SourceDataLine data = null;
	private static AudioInputStream stream = null;
	private static double length = 0.0;

	private static Thread thread = null;
	private static volatile boolean playing = false;

	@Override
	public void load(URL url) {
		try {
			end();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			stream = AudioSystem.getAudioInputStream(url);
			format = stream.getFormat();
			if (format.getEncoding().toString().contains("MPEG")) {//mp3
				MpegAudioFileReader mp = new MpegAudioFileReader();
				stream = mp.getAudioInputStream(url);
				format = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, format.getSampleRate(), 16, format.getChannels(), format.getChannels() * 2, format.getSampleRate(), false);
				stream = AudioSystem.getAudioInputStream(format, stream);
			} else if (format.getEncoding().toString().contains("FLAc")){
				format = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, format.getSampleRate(), 16, format.getChannels(), format.getChannels()*2, format.getSampleRate(), false);
				stream = AudioSystem.getAudioInputStream(format, stream);
			}
			info= new DataLine.Info(SourceDataLine.class, format, AudioSystem.NOT_SPECIFIED);
			data = (SourceDataLine) AudioSystem.getLine(info);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void load(File file) {
		try {
			end();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			stream = AudioSystem.getAudioInputStream(file);
			format = stream.getFormat();
			if (format.getEncoding().toString().contains("MPEG")) {//mp3
				MpegAudioFileReader mp = new MpegAudioFileReader();
				stream = mp.getAudioInputStream(file);
				format = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, format.getSampleRate(), 16, format.getChannels(), format.getChannels() * 2, format.getSampleRate(), false);
				stream = AudioSystem.getAudioInputStream(format, stream);
			} else if (format.getEncoding().toString().contains("FLAC")){
				format = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, format.getSampleRate(), 16, format.getChannels(), format.getChannels()*2, format.getSampleRate(), false);
				stream = AudioSystem.getAudioInputStream(format, stream);
			}
			info= new DataLine.Info(SourceDataLine.class, format, AudioSystem.NOT_SPECIFIED);
			data = (SourceDataLine) AudioSystem.getLine(info);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void load(String path) {
		try {
			end();
		} catch (IOException e) {
			e.printStackTrace();
		}
		File file = new File(path);
		load(file);
	}

	@Override
	public void load(InputStream streams) {
		try {
			end();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			stream = AudioSystem.getAudioInputStream(streams);
			format = stream.getFormat();
			if (format.getEncoding().toString().contains("MPEG")) {//mp3
				MpegAudioFileReader mp = new MpegAudioFileReader();
				stream = mp.getAudioInputStream(streams);
				format = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, format.getSampleRate(), 16, format.getChannels(), format.getChannels() * 2, format.getSampleRate(), false);
				stream = AudioSystem.getAudioInputStream(format, stream);
			} else if (format.getEncoding().toString().contains("FLAC")){
				format = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, format.getSampleRate(), 16, format.getChannels(), format.getChannels()*2, format.getSampleRate(), false);
				stream = AudioSystem.getAudioInputStream(format, stream);
			}
			info= new DataLine.Info(SourceDataLine.class, format, AudioSystem.NOT_SPECIFIED);
			data = (SourceDataLine) AudioSystem.getLine(info);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void load(Encoding encoding, AudioInputStream stream) {
		// TODO Auto-generated method stub
	}

	@Override
	public void load(AudioFormat format, AudioInputStream stream) {

	}

	@SuppressWarnings("deprecation")
	@Override
	public void end() throws IOException {
		if (data!=null) {
			data.stop();
			data.drain();
			stream.close();
			if (thread != null) {
				thread.stop();
				thread = null;
			}
		}
		playing = false;
	}

	@Override
	public void stop() {
		if (data!=null && data.isOpen()) {
			data.stop();
			synchronized (thread) {
				try {
					thread.wait(1);
				} catch (InterruptedException e) {
					e.printStackTrace();
				};
			}
			playing = false;
		}
	}

	@Override
	public void start() {
		if (thread != null) {
			synchronized (thread) {
				thread.notify();
				data.start();
				playing = true;
			}
		} else {
			playing = true;
			thread = new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						if (info!=null) {
							data.open(format);
							data.start();
							byte[] bt = new byte[1024];
							while (stream.read(bt) != -1 && playing) {
								data.write(bt, 0, bt.length);				
							}
							new LyricyServer().end_lyric_player(new LyricPlayer());
							end();
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
			thread.setDaemon(true);			
			thread.start();
		}

	}

	@Override
	public boolean isOpen() {
		return data.isOpen();
	}

	@Override
	public boolean isAlive() {
		if (data!=null && data.isOpen()) {
			return data.isActive();
		} else {
			return false;
		}
	}

	@Override
	public boolean isRuning() {
		if (data!=null && data.isOpen()) {
			return data.isRunning();
		} else {
			return false;
		}
	}

	@Override
	public String info() {
		if (stream!=null) {
			return stream.getFormat().toString();
		} else {
			return "";
		}
	}

	@Override
	public double length(String path) {
		File file=new File(path);
		AudioFile mp3 = null;
		try {
			mp3 = AudioFileIO.read(file);
		} catch (Exception e) {
			e.printStackTrace();
		}
		length = mp3.getAudioHeader().getTrackLength();
		return length;
	}

	public static boolean isPlaying() {
		return playing;
	}

	public static void setPlaying(boolean playing) {
		XMusic.playing = playing;
	}


}

