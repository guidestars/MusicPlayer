package com.xu.musicplayer.entity;

import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Text;

public class PlayerEntity {
	
	private static Table table;
	private static Text text;
	private static ProgressBar bar;
	private static String song;
	public static Table getTable() {
		return table;
	}
	public static void setTable(Table table) {
		PlayerEntity.table = table;
	}
	public static Text getText() {
		return text;
	}
	public static void setText(Text text) {
		PlayerEntity.text = text;
	}
	public static ProgressBar getBar() {
		return bar;
	}
	public static void setBar(ProgressBar bar) {
		PlayerEntity.bar = bar;
	}
	public static String getSong() {
		return song;
	}
	public static void setSong(String song) {
		PlayerEntity.song = song;
	}
	
}
