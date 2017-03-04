package com.xu.musicplayer.test;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.media.ControllerEvent;
import javax.media.ControllerListener;
import javax.media.EndOfMediaEvent;
import javax.media.Manager;
import javax.media.MediaLocator;
import javax.media.NoPlayerException;
import javax.media.Player;
import javax.media.PrefetchCompleteEvent;
import javax.media.RealizeCompleteEvent;
import javax.media.StopEvent;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TableItem;

public class Test {

	public static void main(String[] args) {
		inputMusicPlayerFile();
	}
	

	/**
	 * 导入本地的播放文件(本地歌曲目录不存在，需要从新导入)
	 * @author Administrator
	 */
	private static void inputMusicPlayerFile(){
		FileDialog fileDialog=new FileDialog(new Shell(),SWT.OPEN|SWT.MULTI);
		//fileDialog.setFilterExtensions(new String[]{"*.mp3,*.lrc","*.wma","*.wav","*.wav","*.LRC"});
		fileDialog.setFilterNames(new String[]{"*.mp3,*.lrc","*.wma","*.wav","*.wav","*.LRC"});
		fileDialog.open();
		String[] playlist=fileDialog.getFileNames();
		TableItem tableItem=null;
		int index=1;
		for(int i=0;i<playlist.length;i++){
			System.out.println(playlist[i]);
		}

		
	}
	
}
