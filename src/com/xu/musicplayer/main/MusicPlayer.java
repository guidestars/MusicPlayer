package com.xu.musicplayer.main;

import java.awt.Toolkit;
import java.util.LinkedList;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.wb.swt.SWTResourceManager;

import com.xu.musicplayer.config.Reading;
import com.xu.musicplayer.config.SongChoiceWindow;
import com.xu.musicplayer.entity.PlayerEntity;
import com.xu.musicplayer.lyric.LoadLyric;
import com.xu.musicplayer.modle.LyricyServer;
import com.xu.musicplayer.modle.LyricPlayer;
import com.xu.musicplayer.player.Player;
import com.xu.musicplayer.player.XMusic;
import com.xu.musicplayer.system.Constant;
import com.xu.musicplayer.tray.MusicPlayerTray;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.events.MouseTrackAdapter;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Tray;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.ProgressBar;

public class MusicPlayer {

	protected Shell shell;
	private Display display;
	private boolean click = false;
	private int clickX,clickY;
	private boolean choise = true;
	private static Player player = new XMusic();
	private Tray tray;//
	private Table table;
	private Table table_1;
	private Label text;
	private Label text_1;
	private boolean playing = true;
	private Label label_3 ;
	private LyricyServer server = new LyricyServer(); // 歌词
	private ProgressBar progressBar; // 进度条
	private Composite composite_3;

	private int length;

	public static String PLAYING_SONG = ""; //正在播放歌曲

	/**
	 * Launch the application.
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
		shell.setSize(889, 485);
		shell.setText("MusicPlayer");
		shell.setLocation((display.getClientArea().width - shell.getSize().x)/2, 
				(display.getClientArea().height - shell.getSize().y)/2);
		shell.setLayout(new FillLayout(SWT.HORIZONTAL));
		shell.setBackgroundMode(SWT.INHERIT_DEFAULT);

		// 托盘引入
		tray = display.getSystemTray();
		MusicPlayerTray trayutil = new MusicPlayerTray(shell, tray);
		trayutil.tray();

		Composite composite = new Composite(shell, SWT.NONE);
		composite.setLayout(new FillLayout(SWT.HORIZONTAL));

		SashForm sashForm = new SashForm(composite, SWT.VERTICAL);

		Composite composite_1 = new Composite(sashForm, SWT.NONE);

		Label label = new Label(composite_1, SWT.NONE);
		label.setImage(SWTResourceManager.getImage(MusicPlayer.class, "/com/xu/musicplayer/image/exit-1.png"));
		label.setBounds(845, 10, 32, 32);

		Label label_1 = new Label(composite_1, SWT.NONE);
		label_1.setImage(SWTResourceManager.getImage(MusicPlayer.class, "/com/xu/musicplayer/image/mini-1.png"));
		label_1.setBounds(798, 10, 32, 32);

		Composite composite_2 = new Composite(sashForm, SWT.NONE);
		composite_2.setLayout(new FillLayout(SWT.HORIZONTAL));

		SashForm sashForm_1 = new SashForm(composite_2, SWT.NONE);
		sashForm_1.setTouchEnabled(true);

		Composite composite_4 = new Composite(sashForm_1, SWT.NONE);
		composite_4.setLayout(new FillLayout(SWT.HORIZONTAL));

		table = new Table(composite_4, SWT.FULL_SELECTION);
		table.setHeaderVisible(true);
		table.setLinesVisible(true);

		TableColumn tableColumn = new TableColumn(table, SWT.NONE);
		tableColumn.setWidth(41);
		tableColumn.setText("序号");

		TableColumn tableColumn_1 = new TableColumn(table, SWT.NONE);
		tableColumn_1.setWidth(117);
		tableColumn_1.setText("歌曲");

		Composite composite_5 = new Composite(sashForm_1, SWT.NONE);
		composite_5.setLayout(new FillLayout(SWT.HORIZONTAL));

		table_1 = new Table(composite_5, SWT.FULL_SELECTION);
		table_1.setHeaderVisible(true);
		table_1.setLinesVisible(true);

		TableColumn tableColumn_2 = new TableColumn(table_1, SWT.CENTER);
		tableColumn_2.setText("歌词");

		TableColumn tableColumn_3 = new TableColumn(table_1, SWT.CENTER);
		tableColumn_3.setWidth(728);
		tableColumn_3.setText("歌词");

		composite_3 = new Composite(sashForm, SWT.NONE);


		Label lblNewLabel = new Label(composite_3, SWT.NONE);
		lblNewLabel.setImage(SWTResourceManager.getImage(MusicPlayer.class, "/com/xu/musicplayer/image/lastsong-1.png"));
		lblNewLabel.setBounds(33, 18, 32, 32);

		Label label_2 = new Label(composite_3, SWT.NONE);
		label_2.setImage(SWTResourceManager.getImage(MusicPlayer.class, "/com/xu/musicplayer/image/nextsong-1.png"));
		label_2.setBounds(165, 18, 32, 32);

		label_3 = new Label(composite_3, SWT.NONE);
		label_3.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				playing = Constant.PLAY_STATE;
				if (playing && !XMusic.isPlaying()) {
					start_player();
					label_3.setImage(SWTResourceManager.getImage(MusicPlayer.class, "/com/xu/musicplayer/image/start.png"));
					playing = false;
				} else {
					stop_player();
					playing = true;
					label_3.setImage(SWTResourceManager.getImage(MusicPlayer.class, "/com/xu/musicplayer/image/stop.png"));
				}
			}
		});
		label_3.setImage(SWTResourceManager.getImage(MusicPlayer.class, "/com/xu/musicplayer/image/stop.png"));
		label_3.setBounds(98, 18, 32, 32);

		progressBar = new ProgressBar(composite_3, SWT.NONE);
		progressBar.setEnabled(false);
		progressBar.setBounds(238, 25, 610, 17);
		progressBar.setMaximum(100);//设置进度条的最大长度
		progressBar.setSelection(0);
		progressBar.setMinimum(0);//设置进度的条最小程度		

		text = new Label(composite_3, SWT.NONE);
		text.setFont(SWTResourceManager.getFont("Consolas", 9, SWT.NORMAL));
		text.setEnabled(false);
		text.setBounds(238, 4, 73, 20);

		text_1 = new Label(composite_3, SWT.RIGHT);
		text_1.setFont(SWTResourceManager.getFont("Consolas", 9, SWT.NORMAL));
		text_1.setEnabled(false);
		text_1.setBounds(775, 4, 73, 20);

		sashForm.setWeights(new int[] {1, 5, 1});
		sashForm_1.setWeights(new int[] {156, 728});

		//界面移动
		composite_1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				click=true;
				clickX=e.x;
				clickY=e.y;
			}
			@Override
			public void mouseUp(MouseEvent e) {
				click=false;
			}
		});
		composite_1.addMouseMoveListener(new MouseMoveListener() {
			public void mouseMove(MouseEvent arg0) {//当鼠标按下的时候执行这条语句
				if(click){
					shell.setLocation(shell.getLocation().x-clickX+arg0.x, shell.getLocation().y-clickY+arg0.y);
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
				//tray.dispose();
				//distory();
				exit();
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
		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDoubleClick(MouseEvent e) {
				choise = true;
			}
		});
		table.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (choise) {
					TableItem[] items=table.getSelection();
					int index = Integer.parseInt(items[0].getText(0).trim());
					choise_song(index);
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
				last_song(-1);
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
				next_song(-1);
				label_2.setImage(SWTResourceManager.getImage(MusicPlayer.class, "/com/xu/musicplayer/image/nextsong-1.png"));
			}
		});

		init_music_player(shell,table);
	}

	public void init_music_player(Shell shell,Table table) {
		SongChoiceWindow choice = new SongChoiceWindow();
		new Reading().read();
		if (Constant.PLAY_LIST==null || Constant.PLAY_LIST.size()<=0) {
			Toolkit.getDefaultToolkit().beep();
			choice.open_choise_windows(shell);
		}
		update_list(Constant.PLAY_LIST,table);
		get_registry();
	}

	private void update_list(LinkedList<String> lists,Table table) {
		table.removeAll();
		TableItem item;
		for (int i = 0; i < lists.size(); i++) {
			item = new TableItem(table, SWT.NONE);
			item.setText(new String[]{""+(i+1),lists.get(i).split(Constant.SPLIT)[1]});
		}			
	}

	public void start_player() {
		PLAYING_SONG = Constant.PLAY_LIST.get(Constant.PLAY_INDEX);
		player.load(PLAYING_SONG.split(Constant.SPLIT)[0]);
		try {
			player.start();
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		Constant.PLAY_STATE = true;
		label_3.setImage(SWTResourceManager.getImage(MusicPlayer.class, "/com/xu/musicplayer/image/start.png"));
		update_list_color(table,Constant.PLAY_INDEX);
	}

	public void stop_player() {
		try {
			player.end();
		} catch (Exception e) {
			e.printStackTrace();
		}
		Constant.PLAY_STATE = false;
		label_3.setImage(SWTResourceManager.getImage(MusicPlayer.class, "/com/xu/musicplayer/image/stop.png"));
		update_list_color(table,Constant.PLAY_INDEX);
	}

	public void choise_song(int index) {
		Constant.PLAY_INDEX = index==-1?Constant.PLAY_INDEX:index-1;
		System.out.println(Constant.PLAY_INDEX+"\t"+index+"\t"+Constant.PLAY_LIST.get(Constant.PLAY_INDEX));
		PLAYING_SONG = Constant.PLAY_LIST.get(Constant.PLAY_INDEX);
		player.load(PLAYING_SONG.split(Constant.SPLIT)[0]);
		try {
			player.start();
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		Constant.PLAY_STATE = true;
		label_3.setImage(SWTResourceManager.getImage(MusicPlayer.class, "/com/xu/musicplayer/image/start.png"));
		update_list_color(table,index-1);
	}

	public void last_song(int index) {
		Constant.PLAY_INDEX = index==-1?Constant.PLAY_INDEX:index;
		System.out.println(Constant.PLAY_INDEX+"\t"+Constant.PLAY_LIST.get(Constant.PLAY_INDEX));
		PLAYING_SONG = Constant.PLAY_LIST.get(Constant.PLAY_INDEX);
		player.load(PLAYING_SONG.split(Constant.SPLIT)[0]);
		try {
			player.start();
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		Constant.PLAY_STATE = true;
		label_3.setImage(SWTResourceManager.getImage(MusicPlayer.class, "/com/xu/musicplayer/image/start.png"));
		update_list_color(table,Constant.PLAY_INDEX);
		if (Constant.PLAY_INDEX == 0) {
			Constant.PLAY_INDEX = Constant.PLAY_LIST.size();
		} else {
			Constant.PLAY_INDEX--;
		}
	}

	public void next_song(int index) {
		Constant.PLAY_INDEX = index==-1?Constant.PLAY_INDEX:index;
		System.out.println(Constant.PLAY_INDEX+"\t"+Constant.PLAY_LIST.get(Constant.PLAY_INDEX));
		PLAYING_SONG = Constant.PLAY_LIST.get(Constant.PLAY_INDEX);
		player.load(PLAYING_SONG.split(Constant.SPLIT)[0]);
		try {
			player.start();
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		Constant.PLAY_STATE = true;
		label_3.setImage(SWTResourceManager.getImage(MusicPlayer.class, "/com/xu/musicplayer/image/start.png"));
		update_list_color(table,Constant.PLAY_INDEX);
		if (Constant.PLAY_INDEX == Constant.PLAY_LIST.size()) {
			Constant.PLAY_INDEX = 0;
		} else {
			Constant.PLAY_INDEX++;
		}
	}



	private void update_list_color(Table table,int index) {
		length = Integer.parseInt(PLAYING_SONG.split(Constant.SPLIT)[3]);
		text.setText(((int)length/60+":"+length%60)+"");
		TableItem[] items = table.getItems();
		for (int i = 0; i < items.length; i++) {
			if (index == i) {
				items[i].setBackground(SWTResourceManager.getColor(SWT.COLOR_BLUE));//将选中的行的颜色变为蓝色
			} else {
				items[i].setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));//将选中的行的颜色变为蓝色
			}
		}
		if (Constant.PLAY_LIST.get(Constant.PLAY_INDEX).split(Constant.SPLIT)[4].equalsIgnoreCase("Y")) {
			LoadLyric lyric = new LoadLyric();
			String path = Constant.PLAY_LIST.get(Constant.PLAY_INDEX).split(Constant.SPLIT)[0];
			path = path.substring(0, path.lastIndexOf("."))+".lrc";
			lyric.lyric(path);
			table_1.removeAll();
			if (Constant.PLAY_LYRIC!=null && Constant.PLAY_LYRIC.size()>0) {
				TableItem item;
				for (String lyri:Constant.PLAY_LYRIC) {
					item = new TableItem(table_1, SWT.NONE);
					item.setText(new String[]{"",lyri.split(Constant.SPLIT)[1]});
				}
				PlayerEntity entity = new PlayerEntity();
				PlayerEntity.setBar(progressBar);
				PlayerEntity.setText(text_1);
				PlayerEntity.setSong(PLAYING_SONG);
				PlayerEntity.setTable(table_1);
				PlayerEntity.setSpectrum(composite_3);
				server.end_lyric_player(new LyricPlayer());
				server.start_lyric_player(new LyricPlayer(), entity);
			}
		}
		set_registry(index+"");
	}

	private void set_registry(String index) {
		Preferences preferences=Preferences.userNodeForPackage(MusicPlayer.class);
		if(preferences.get("MusicPlayer", null) == null){
			preferences.put("MusicPlayer", index);
		} else{
			preferences.put("MusicPlayer", index);
		}
		try {
			preferences.flush();
		} catch (BackingStoreException e) {
			e.printStackTrace();
		}
	}

	private void get_registry(){
		Preferences preferences=Preferences.userNodeForPackage(MusicPlayer.class);
		String index = preferences.get("MusicPlayer", null);
		if(index != null){
			//next_song(Integer.parseInt(index));
		}
	}

	private void exit() {
		player.stop();
		tray.dispose();
		shell.dispose();
		System.exit(0);
	}

}
