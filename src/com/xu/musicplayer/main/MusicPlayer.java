package com.xu.musicplayer.main;

import com.xu.musicplayer.config.Reading;
import com.xu.musicplayer.config.SongChoiceWindow;
import com.xu.musicplayer.entity.PlayerEntity;
import com.xu.musicplayer.lyric.LoadLocalLyric;
import com.xu.musicplayer.modle.Controller;
import com.xu.musicplayer.modle.ControllerServer;
import com.xu.musicplayer.player.Player;
import com.xu.musicplayer.player.XMusic;
import com.xu.musicplayer.system.Constant;
import com.xu.musicplayer.tray.MusicPlayerTray;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

import org.eclipse.swt.widgets.*;
import org.eclipse.wb.swt.SWTResourceManager;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

import javax.imageio.ImageIO;

public class MusicPlayer {

	public static boolean playing = true;// 播放按钮
	public static List<Color> COLORS = new ArrayList<Color>();
	private static Player player = new XMusic();

	protected Shell shell;
	private Display display;
	private Tray tray;// 播放器托盘

	Composite top;
	private Table lists;
	private Table lyrics;

	private Label text;
	private Label text_1;
	private Label label_3;
	private ControllerServer server = new ControllerServer(); // 歌词及频谱
	private ProgressBar progressBar; // 进度条
	private Composite foot; // 频谱面板

	private boolean click = false;//界面移动
	private int clickX, clickY;//界面移动
	private boolean choise = true;// 双击播放

	private static int merchant = 0;
	private static int remainder = 0;
	private static String format = "";

	/**
	 * Launch the application.
	 *
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			MusicPlayer window = new MusicPlayer();
			window.open();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Open the window.
	 */
	public void open() {
		display = Display.getDefault();
		createContents();
		shell.open();
		shell.layout();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

	/**
	 * Create contents of the window.
	 */
	protected void createContents() {
		shell = new Shell(SWT.NONE);
		shell.setImage(SWTResourceManager.getImage(MusicPlayer.class, "/com/xu/musicplayer/image/main.png"));
		shell.setSize(new Point(1000, 645));
		shell.setSize(900, 486);
		shell.setText("MusicPlayer");
		shell.setLocation((display.getClientArea().width - shell.getSize().x) / 2,
				(display.getClientArea().height - shell.getSize().y) / 2);
		shell.setLayout(new FillLayout(SWT.HORIZONTAL));
		shell.setBackgroundMode(SWT.INHERIT_DEFAULT);

		// 托盘引入
		tray = display.getSystemTray();
		MusicPlayerTray trayutil = new MusicPlayerTray(shell, tray);
		trayutil.tray();

		Composite composite = new Composite(shell, SWT.NONE);
		composite.setBackgroundMode(SWT.INHERIT_FORCE);
		composite.setLayout(new FillLayout(SWT.HORIZONTAL));

		SashForm sashForm = new SashForm(composite, SWT.VERTICAL);

		top = new Composite(sashForm, SWT.NONE);
		top.setBackgroundMode(SWT.INHERIT_FORCE);

		Label label = new Label(top, SWT.NONE);
		label.setImage(SWTResourceManager.getImage(MusicPlayer.class, "/com/xu/musicplayer/image/exit-1.png"));
		label.setBounds(845, 10, 32, 32);

		Label label_1 = new Label(top, SWT.NONE);
		label_1.setImage(SWTResourceManager.getImage(MusicPlayer.class, "/com/xu/musicplayer/image/mini-1.png"));
		label_1.setBounds(798, 10, 32, 32);

		Combo combo = new Combo(top, SWT.NONE);
		combo.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				combo.clearSelection();
			}
		});
		combo.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent arg0) {
				//List<APISearchTipsEntity> songs = Search.search(combo.getText(),"API");
				//for (APISearchTipsEntity song:songs) {
				//	combo.add(song.getFilename());
				//}
				//combo.setListVisible(true);
				combo.clearSelection();
				for (int i = 0; i < Constant.MUSIC_PLAYER_SONGS_LIST.size(); i++) {
					if (Constant.MUSIC_PLAYER_SONGS_LIST.get(i).contains(combo.getText())) {
						combo.add(Constant.MUSIC_PLAYER_SONGS_LIST.get(i).split(Constant.MUSIC_PLAYER_SYSTEM_SPLIT)[1]);
					}
				}
				combo.setListVisible(true);
			}
		});
		combo.setBounds(283, 21, 330, 25);
		combo.setVisible(false);

		Composite center = new Composite(sashForm, SWT.NONE);
		center.setBackgroundMode(SWT.INHERIT_FORCE);
		center.setLayout(new FillLayout(SWT.HORIZONTAL));

		SashForm sashForm_1 = new SashForm(center, SWT.NONE);

		Composite composite_4 = new Composite(sashForm_1, SWT.NONE);
		composite_4.setBackgroundMode(SWT.INHERIT_FORCE);
		composite_4.setLayout(new FillLayout(SWT.HORIZONTAL));

		lists = new Table(composite_4, SWT.FULL_SELECTION);
		lists.setHeaderVisible(true);

		TableColumn tableColumn = new TableColumn(lists, SWT.NONE);
		tableColumn.setWidth(41);
		tableColumn.setText("序号");

		TableColumn tableColumn_1 = new TableColumn(lists, SWT.NONE);
		tableColumn_1.setWidth(117);
		tableColumn_1.setText("歌曲");

		Composite composite_5 = new Composite(sashForm_1, SWT.NONE);
		composite_5.setBackgroundMode(SWT.INHERIT_FORCE);
		composite_5.setLayout(new FillLayout(SWT.HORIZONTAL));

		lyrics = new Table(composite_5, SWT.NONE);

		TableColumn tableColumn_2 = new TableColumn(lyrics, SWT.CENTER);
		tableColumn_2.setText("歌词");

		TableColumn tableColumn_3 = new TableColumn(lyrics, SWT.CENTER);
		tableColumn_3.setWidth(738);
		tableColumn_3.setText("歌词");

		COLORS.add(Color.BLACK);
		COLORS.add(Color.BLUE);
		COLORS.add(Color.CYAN);
		COLORS.add(Color.DARK_GRAY);
		COLORS.add(Color.GRAY);
		COLORS.add(Color.GREEN);
		COLORS.add(Color.LIGHT_GRAY);
		COLORS.add(Color.MAGENTA);
		COLORS.add(Color.ORANGE);
		COLORS.add(Color.PINK);
		COLORS.add(Color.RED);
		COLORS.add(Color.WHITE);
		COLORS.add(Color.YELLOW);

		foot = new Composite(sashForm, SWT.NONE);
		foot.setBackgroundMode(SWT.INHERIT_FORCE);

		Label lblNewLabel = new Label(foot, SWT.NONE);
		lblNewLabel.setImage(SWTResourceManager.getImage(MusicPlayer.class, "/com/xu/musicplayer/image/lastsong-1.png"));
		lblNewLabel.setBounds(33, 18, 32, 32);

		Label label_2 = new Label(foot, SWT.NONE);
		label_2.setImage(SWTResourceManager.getImage(MusicPlayer.class, "/com/xu/musicplayer/image/nextsong-1.png"));
		label_2.setBounds(165, 18, 32, 32);

		label_3 = new Label(foot, SWT.NONE);
		label_3.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				playing = Constant.MUSIC_PLAYER_PLAYING_STATE;
				if (playing && !XMusic.isPlaying()) {
					//TODO:
					label_3.setImage(SWTResourceManager.getImage(MusicPlayer.class, "/com/xu/musicplayer/image/start.png"));
					playing = false;
				} else {
					//TODO:
					playing = true;
					label_3.setImage(SWTResourceManager.getImage(MusicPlayer.class, "/com/xu/musicplayer/image/stop.png"));
				}
			}
		});
		label_3.setImage(SWTResourceManager.getImage(MusicPlayer.class, "/com/xu/musicplayer/image/stop.png"));
		label_3.setBounds(98, 18, 32, 32);

		progressBar = new ProgressBar(foot, SWT.NONE);
		progressBar.setEnabled(false);
		progressBar.setBounds(238, 25, 610, 17);
		progressBar.setMaximum(100);//设置进度条的最大长度
		progressBar.setSelection(0);
		progressBar.setMinimum(0);//设置进度的条最小程度

		text = new Label(foot, SWT.NONE);
		text.setFont(SWTResourceManager.getFont("Consolas", 9, SWT.NORMAL));
		text.setEnabled(false);
		text.setBounds(238, 4, 73, 20);

		text_1 = new Label(foot, SWT.RIGHT);
		text_1.setFont(SWTResourceManager.getFont("Consolas", 9, SWT.NORMAL));
		text_1.setEnabled(false);
		text_1.setBounds(775, 4, 73, 20);

		sashForm.setWeights(new int[]{1, 5, 1});
		sashForm_1.setWeights(new int[]{156, 728});

		//界面移动
		top.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				click = true;
				clickX = e.x;
				clickY = e.y;
			}

			@Override
			public void mouseUp(MouseEvent e) {
				click = false;
			}
		});
		top.addMouseMoveListener(new MouseMoveListener() {
			public void mouseMove(MouseEvent arg0) {//当鼠标按下的时候执行这条语句
				if (click) {
					shell.setLocation(shell.getLocation().x - clickX + arg0.x, shell.getLocation().y - clickY + arg0.y);
				}
			}
		});

		// 缩小
		label_1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				label_1.setImage(SWTResourceManager.getImage(MusicPlayer.class, "/com/xu/musicplayer/image/mini-2.png"));
			}

			@Override
			public void mouseUp(MouseEvent e) {
				label_1.setImage(SWTResourceManager.getImage(MusicPlayer.class, "/com/xu/musicplayer/image/mini-1.png"));
				shell.setMinimized(true);
			}
		});
		label_1.addMouseTrackListener(new MouseTrackAdapter() {
			@Override
			public void mouseExit(MouseEvent e) {
				label_1.setImage(SWTResourceManager.getImage(MusicPlayer.class, "/com/xu/musicplayer/image/mini-1.png"));
			}

			@Override
			public void mouseHover(MouseEvent e) {
				label_1.setImage(SWTResourceManager.getImage(MusicPlayer.class, "/com/xu/musicplayer/image/mini-2.png"));
				label_1.setToolTipText("最小化");
			}
		});

		//退出
		label.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				label.setImage(SWTResourceManager.getImage(MusicPlayer.class, "/com/xu/musicplayer/image/exit-2.png"));
			}

			@Override
			public void mouseUp(MouseEvent e) {
				label.setImage(SWTResourceManager.getImage(MusicPlayer.class, "/com/xu/musicplayer/image/exit-1.png"));
				exitMusicPlayer();
			}
		});
		label.addMouseTrackListener(new MouseTrackAdapter() {
			@Override
			public void mouseExit(MouseEvent e) {
				label.setImage(SWTResourceManager.getImage(MusicPlayer.class, "/com/xu/musicplayer/image/exit-1.png"));
			}

			@Override
			public void mouseHover(MouseEvent e) {
				label.setImage(SWTResourceManager.getImage(MusicPlayer.class, "/com/xu/musicplayer/image/exit-2.png"));
				label.setToolTipText("退出");
			}
		});

		//双击播放
		lists.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDoubleClick(MouseEvent e) {
				choise = true;
			}
		});
		lists.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (choise) {
					TableItem[] items = lists.getSelection();
					int index = Integer.parseInt(items[0].getText(0).trim());
					changePlayingSong(index - 1, 1);//下一曲
				}
			}
		});

		//上一曲
		lblNewLabel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				lblNewLabel.setImage(SWTResourceManager.getImage(MusicPlayer.class, "/com/xu/musicplayer/image/lastsong-2.png"));
			}

			@Override
			public void mouseUp(MouseEvent e) {
				changePlayingSong(-1, 0);//上一曲
				lblNewLabel.setImage(SWTResourceManager.getImage(MusicPlayer.class, "/com/xu/musicplayer/image/lastsong-1.png"));
			}
		});

		//下一曲
		label_2.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				label_2.setImage(SWTResourceManager.getImage(MusicPlayer.class, "/com/xu/musicplayer/image/nextsong-2.png"));
			}

			@Override
			public void mouseUp(MouseEvent e) {
				changePlayingSong(-1, 1);//下一曲
				label_2.setImage(SWTResourceManager.getImage(MusicPlayer.class, "/com/xu/musicplayer/image/nextsong-1.png"));
			}
		});

		foot.addControlListener(new ControlAdapter() {
			@Override
			public void controlResized(ControlEvent e) {
				sashForm.setWeights(new int[]{1, 5, 1});
				sashForm_1.setWeights(new int[]{156, 728});
			}
		});

		foot.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDoubleClick(MouseEvent e) {
				Color color = COLORS.get(new Random().nextInt(COLORS.size()));
				if (color != Constant.SPECTRUM_BACKGROUND_COLOR) {
					Constant.SPECTRUM_FOREGROUND_COLOR = color;
				}
			}
		});

		sashForm.addControlListener(new ControlAdapter() {
			@Override
			public void controlResized(ControlEvent e) {
				sashForm.setWeights(new int[]{1, 5, 1});
				sashForm_1.setWeights(new int[]{156, 728});
			}
		});

		composite_4.addControlListener(new ControlAdapter() {
			@Override
			public void controlResized(ControlEvent e) {
				sashForm.setWeights(new int[]{1, 5, 1});
				sashForm_1.setWeights(new int[]{156, 728});
			}
		});

		composite_5.addControlListener(new ControlAdapter() {
			@Override
			public void controlResized(ControlEvent e) {
				sashForm.setWeights(new int[]{1, 5, 1});
				sashForm_1.setWeights(new int[]{156, 728});
			}
		});

		sashForm_1.addControlListener(new ControlAdapter() {
			@Override
			public void controlResized(ControlEvent e) {
				sashForm.setWeights(new int[]{1, 5, 1});
				sashForm_1.setWeights(new int[]{156, 728});
			}
		});

		initMusicPlayer(shell, lists);

	}

	/**
	 * Java MusicPlayer 初始化音乐播放器
	 *
	 * @param shell
	 * @param table
	 * @return void
	 * @Author: hyacinth
	 * @Title: initMusicPlayer
	 * @Description: TODO
	 * @date: 2019年12月26日 下午7:20:00
	 */
	public void initMusicPlayer(Shell shell, Table table) {
		SongChoiceWindow choice = new SongChoiceWindow();
		new Reading().read();
		if (Constant.MUSIC_PLAYER_SONGS_LIST == null || Constant.MUSIC_PLAYER_SONGS_LIST.size() <= 0) {
			Toolkit.getDefaultToolkit().beep();
			choice.open_choise_windows(shell);
		}
		updatePlayerSongLists(Constant.MUSIC_PLAYER_SONGS_LIST, table);
		readMusicPlayerPlayingSong();
	}

	/**
	 * Java MusicPlayer 更新播放歌曲列表
	 *
	 * @param lists
	 * @param table
	 * @return void
	 * @Author: hyacinth
	 * @Title: updatePlayerSongLists
	 * @Description: TODO
	 * @date: 2019年12月26日 下午7:20:33
	 */
	private void updatePlayerSongLists(LinkedList<String> lists, Table table) {
		table.removeAll();
		TableItem item;
		for (int i = 0; i < lists.size(); i++) {
			item = new TableItem(table, SWT.NONE);
			item.setText(new String[]{"" + (i + 1), lists.get(i).split(Constant.MUSIC_PLAYER_SYSTEM_SPLIT)[1]});
		}
	}

	/**
	 * Java MusicPlayer 更新播放歌曲列表
	 *
	 * @param lists
	 * @param table
	 * @return void
	 * @Author: hyacinth
	 * @Title: markSongsLists
	 * @Description: TODO
	 * @date: 2019年12月31日 下午8:20:33
	 */
	public void markSongsLists(Table table, int index) {
		TableItem[] items = table.getItems();
		for (int i = 0, len = items.length; i < len; i++) {
			if (index == i) {
				items[i].setBackground(SWTResourceManager.getColor(SWT.COLOR_GRAY));
			} else {
				items[i].setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
			}
		}
		table.setTopIndex(index);
	}

	/**
	 * Java MusicPlayer 改变播放歌曲
	 * <table border="1" cellpadding="10">
	 * <tr><td colspan="2" align="center">changePlayingSong</td></tr>
	 * <tr><th align="center">Mode 输入参数</th><th align="center">参数解释</th></tr>
	 * <tr><td align="left">0</td><td align="left">上一曲</td></tr>
	 * <tr><td align="left">1</td><td align="left">下一曲</td></tr>
	 *
	 * @param index 歌曲索引
	 * @param mode  切歌模式(上一曲/下一曲)
	 * @return void
	 * @Author: hyacinth
	 * @Title: changePlayingSong
	 * @Description: TODO
	 * @date: 2019年12月26日 下午7:33:36
	 */
	public void changePlayingSong(int index, int mode) {
		System.out.println(Constant.PLAYING_SONG_INDEX + "\t" + Constant.MUSIC_PLAYER_SONGS_LIST.get(Constant.PLAYING_SONG_INDEX));

		Constant.PLAYING_SONG_INDEX = index == -1 ? Constant.PLAYING_SONG_INDEX : index;
		Constant.PLAYING_SONG_NAME = Constant.MUSIC_PLAYER_SONGS_LIST.get(Constant.PLAYING_SONG_INDEX);

		if (Constant.MUSIC_PLAYER_SONGS_LIST.size() <= 0) {
			Toolkit.getDefaultToolkit().beep();
			MessageBox message = new MessageBox(shell,SWT.YES|SWT.ICON_WARNING|SWT.NO);
			message.setText("提示");
			message.setMessage("未发现歌曲，现在添加歌曲？");
			if (message.open() == SWT.YES) {
				initMusicPlayer(shell,lists);
			} else {
				Toolkit.getDefaultToolkit().beep();
				message = new MessageBox(shell,SWT.OK|SWT.ICON_ERROR);
				message.setText("提示");
				message.setMessage("你将不能播放歌曲。");
				message.open();
			}
		}

		player.load(Constant.PLAYING_SONG_NAME.split(Constant.MUSIC_PLAYER_SYSTEM_SPLIT)[0]);

		try {
			player.start();
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		Constant.MUSIC_PLAYER_PLAYING_STATE = true;
		updatePlayerSongListsColor(lists, Constant.PLAYING_SONG_INDEX);
		if (mode == 0) {//上一曲
			if (Constant.PLAYING_SONG_INDEX <= 0) {
				Constant.PLAYING_SONG_INDEX = Constant.MUSIC_PLAYER_SONGS_LIST.size();
			} else {
				Constant.PLAYING_SONG_INDEX--;
			}
		} else {//下一曲
			if (Constant.PLAYING_SONG_INDEX >= Constant.MUSIC_PLAYER_SONGS_LIST.size()) {
				Constant.PLAYING_SONG_INDEX = 0;
			} else {
				Constant.PLAYING_SONG_INDEX++;
			}
		}
	}

	/**
	 * Java MusicPlayer 改变选中歌曲的颜色
	 *
	 * @param table
	 * @param index
	 * @return void
	 * @Author: hyacinth
	 * @Title: updatePlayerSongListsColor
	 * @Description: TODO
	 * @date: 2019年12月26日 下午7:40:10
	 */
	private void updatePlayerSongListsColor(Table table, int index) {
		label_3.setImage(SWTResourceManager.getImage(MusicPlayer.class, "/com/xu/musicplayer/image/start.png"));

		Constant.PLAYING_SONG_HAVE_LYRIC = false;
		Constant.PLAYING_SONG_LENGTH = Integer.parseInt(Constant.PLAYING_SONG_NAME.split(Constant.MUSIC_PLAYER_SYSTEM_SPLIT)[3]);
		text.setText(format(Constant.PLAYING_SONG_LENGTH));

		TableItem[] items = table.getItems();
		for (int i = 0; i < items.length; i++) {
			if (index == i) {
				items[i].setBackground(SWTResourceManager.getColor(SWT.COLOR_GRAY));
			} else {
				items[i].setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
			}
		}
		if (Constant.MUSIC_PLAYER_SONGS_LIST.get(Constant.PLAYING_SONG_INDEX).split(Constant.MUSIC_PLAYER_SYSTEM_SPLIT)[4].equalsIgnoreCase("Y")) {
			Constant.PLAYING_SONG_HAVE_LYRIC = true;
			LoadLocalLyric lyric = new LoadLocalLyric();
			String path = Constant.MUSIC_PLAYER_SONGS_LIST.get(Constant.PLAYING_SONG_INDEX).split(Constant.MUSIC_PLAYER_SYSTEM_SPLIT)[0];
			path = path.substring(0, path.lastIndexOf(".")) + ".lrc";
			lyric.lyric(path);
			lyrics.removeAll();
			if (Constant.PLAYING_SONG_LYRIC != null && Constant.PLAYING_SONG_LYRIC.size() > 0) {
				TableItem item;
				for (int i = 0, len = Constant.PLAYING_SONG_LYRIC.size() + 8; i < len; i++) {
					item = new TableItem(lyrics, SWT.NONE);
					if (i < len - 8) {
						item.setText(new String[]{"", Constant.PLAYING_SONG_LYRIC.get(i).split(Constant.MUSIC_PLAYER_SYSTEM_SPLIT)[1]});
					}
				}
				PlayerEntity.setBar(progressBar);
				PlayerEntity.setText(text_1);
				PlayerEntity.setSong(Constant.PLAYING_SONG_NAME);
				PlayerEntity.setTable(lyrics);
			}
			PlayerEntity.setSpectrum(foot);
			server.endLyricPlayer(new Controller());
			server.startLyricPlayer(new Controller(), null);
		}
		setMusicPlayerPlayingSong(index + "");
		JVMinfo();
	}


	/**
	 * Java MusicPlayer 暂停播放(未实现)
	 *
	 * @return void
	 * @Author: hyacinth
	 * @Title: stopMusicPlayer
	 * @Description: TODO
	 * @date: 2019年12月26日 下午7:47:41
	 */
	public void stopMusicPlayer() {
		try {
			player.end();
		} catch (Exception e) {
			e.printStackTrace();
		}
		Constant.MUSIC_PLAYER_PLAYING_STATE = false;
		label_3.setImage(SWTResourceManager.getImage(MusicPlayer.class, "/com/xu/musicplayer/image/stop.png"));
		updatePlayerSongListsColor(lists, Constant.PLAYING_SONG_INDEX);
	}


	/**
	 * Java MusicPlayer 退出音乐播放器
	 *
	 * @return void
	 * @Author: hyacinth
	 * @Title: exitMusicPlayer
	 * @Description: TODO
	 * @date: 2019年12月26日 下午7:57:12
	 */
	private void exitMusicPlayer() {
		tray.dispose();
		System.exit(0);
		player.stop();
		shell.dispose();
	}


	/**
	 * Java MusicPlayer 将正在播放的歌曲存在注册表中
	 *
	 * @param index
	 * @return void
	 * @Author: hyacinth
	 * @Title: setMusicPlayerPlayingSong
	 * @Description: TODO
	 * @date: 2019年12月29日 下午2:57:30
	 */
	private void setMusicPlayerPlayingSong(String index) {
		Preferences preferences = Preferences.userNodeForPackage(MusicPlayer.class);
		if (preferences.get("MusicPlayer", null) == null) {
			preferences.put("MusicPlayer", index);
		} else {
			preferences.put("MusicPlayer", index);
		}
		try {
			preferences.flush();
		} catch (BackingStoreException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Java MusicPlayer 读取上次播放器退出前播放的歌曲
	 *
	 * @return void
	 * @Author: hyacinth
	 * @Title: readMusicPlayerPlayingSong
	 * @Description: TODO
	 * @date: 2019年12月29日 下午2:58:24
	 */
	private void readMusicPlayerPlayingSong() {
		Preferences preferences = Preferences.userNodeForPackage(MusicPlayer.class);
		String index = preferences.get("MusicPlayer", null);
		if (index != null) {
			//next_song(Integer.parseInt(index));
		}
	}

	public void JVMinfo() {
		long vmFree = 0;
		long vmUse = 0;
		long vmTotal = 0;
		long vmMax = 0;
		int byteToMb = 1024;
		Runtime rt = Runtime.getRuntime();
		vmTotal = rt.totalMemory() / byteToMb;
		vmFree = rt.freeMemory() / byteToMb;
		vmMax = rt.maxMemory() / byteToMb;
		vmUse = vmTotal - vmFree;
		System.out.println("JVM 已用内存为：" + vmUse + "\tKB");
		System.out.println("JVM 空闲内存为：" + vmFree + "\tKB");
		System.out.println("JVM 可用内存为：" + vmTotal + "\tKB");
		System.out.println("JVM 最大内存为：" + vmMax + "\tKB");
	}

	private static String format(int time) {
		merchant = time / 60;
		remainder = time % 60;
		if (time < 10) {
			format = "00:0" + time;
		} else if (time < 60) {
			format = "00:" + time;
		} else {
			if (merchant < 10 && remainder < 10) {
				format = "0" + merchant + ":0" + remainder;
			} else if (merchant < 10 && remainder < 60) {
				format = "0" + merchant + ":" + remainder;
			} else if (merchant >= 10 && remainder < 10) {
				format = merchant + ":0" + remainder;
			} else if (merchant >= 10 && remainder < 60) {
				format = merchant + ":0" + remainder;
			}
		}
		return format;
	}

	@SuppressWarnings("unused")
	private void changePanelImage(Color color) {
		BufferedImage image = new BufferedImage(5, 5, BufferedImage.TYPE_INT_RGB);
		Graphics2D graphics = image.createGraphics();
		//image = graphics.getDeviceConfiguration().createCompatibleImage(width, height, Transparency.TRANSLUCENT);
		//graphics.dispose();
		//graphics = image.createGraphics();
		graphics.setBackground(color);
		graphics.clearRect(0, 0, 5, 5);
		//graphics.setColor(Color.PINK);
		//graphics.setStroke(new BasicStroke(1f));

		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		try {
			ImageIO.write(image, "png", stream);
		} catch (IOException e) {
			e.printStackTrace();
		}
		InputStream inputStream = new ByteArrayInputStream(stream.toByteArray());
		if (inputStream != null) {
			top.setBackgroundImage(new Image(null, new ImageData(inputStream)));
			foot.setBackgroundImage(top.getBackgroundImage());
			//lists.setBackgroundImage(top.getBackgroundImage());
			//lyrics.setBackgroundImage(top.getBackgroundImage());
		}
		graphics.dispose();
	}

}
