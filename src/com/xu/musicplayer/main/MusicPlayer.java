package com.xu.musicplayer.main;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.Shell;

import java.awt.Toolkit;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.media.Manager;
import javax.media.Player;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;

import com.sun.media.util.MediaThread;
import com.xu.musicplayer.bean.PlayerBean;
import com.xu.musicplayer.bean.PlayerNotify;
import com.xu.musicplayer.lyric.Analysis;
import com.xu.musicplayer.thread.PlayerThread;
import com.xu.musicplayer.trayutil.TrayUtil;

import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tray;
import org.eclipse.wb.swt.SWTResourceManager;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.events.MouseTrackAdapter;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.TouchListener;
import org.eclipse.swt.events.TouchEvent;

public class MusicPlayer {

	protected Shell shell;
	private Display display;

	private Text   text;
	private Text   text_1;
	private Table  table;
	private Table  table_1;
	private Label  label_3 ;

	private static boolean HAVELYRIC=false;//是否拥有歌词
	private static int     MUSICLENGTH=0; //歌曲总时间

	private Player player=null;//播放器

	private List<String>    LYRICLISTS=new ArrayList<String>();  //歌词

	private HashSet<String> PLAYLISTS=new HashSet<String>();   //歌曲库

	private boolean click=true;

	private String PLAYREALPATH="";

	private boolean shellmove=false;
	private int clickX=0;
	private int clickY=0;

	private Toolkit tool=Toolkit.getDefaultToolkit();

	private Logger logger=LogManager.getLogger(MusicPlayer.class);

	private Thread playThread=new Thread();

	private ProgressBar progressBar;

	private Tray tray;

	private MediaThread mThread=new MediaThread();
	private Table table_2;

	private SashForm sashForm_1 ;

	private List<String> netList=null;

	private boolean STARTPLAY=false;

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
		shell.setImage(SWTResourceManager.getImage(MusicPlayer.class, "/com/xu/musicplayer/image/music.png"));
		shell.setSize(new Point(1000, 645));
		shell.setSize(889, 485);
		shell.setText("音乐");
		shell.setLocation((display.getClientArea().width - shell.getSize().x)/2, 
				(display.getClientArea().height - shell.getSize().y)/2);
		shell.setLayout(new FillLayout(SWT.HORIZONTAL));	

		// 托盘引入
		tray = display.getSystemTray();
		TrayUtil trayutil = new com.xu.musicplayer.trayutil.TrayUtil(shell, tray);
		trayutil.tray();

		SashForm sashForm = new SashForm(shell, SWT.NONE);
		sashForm.setOrientation(SWT.VERTICAL);

		Composite composite = new Composite(sashForm, SWT.NONE);

		final Label label = new Label(composite, SWT.NONE);
		label.setImage(SWTResourceManager.getImage(MusicPlayer.class, "/com/xu/musicplayer/image/minus_1.png"));
		label.setBounds(817, 0, 32, 32);

		final Label label_1 = new Label(composite, SWT.NONE);
		label_1.setImage(SWTResourceManager.getImage(MusicPlayer.class, "/com/xu/musicplayer/image/delete_1.png"));
		label_1.setBounds(855, 0, 32, 32);

		Composite composite_1 = new Composite(sashForm, SWT.NONE);
		composite_1.setLayout(new FillLayout(SWT.HORIZONTAL));

		sashForm_1 = new SashForm(composite_1, SWT.NONE);

		Composite composite_3 = new Composite(sashForm_1, SWT.NONE);
		composite_3.setLayout(new FillLayout(SWT.HORIZONTAL));

		table = new Table(composite_3, SWT.FULL_SELECTION);
		table.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		table.setFont(SWTResourceManager.getFont("Microsoft YaHei UI", 10, SWT.NORMAL));
		table.setHeaderVisible(true);
		table.setLinesVisible(true);

		TableColumn tableColumn = new TableColumn(table, SWT.NONE);
		tableColumn.setWidth(39);
		tableColumn.setText("编号");

		TableColumn tableColumn_1 = new TableColumn(table, SWT.NONE);
		tableColumn_1.setWidth(330);
		tableColumn_1.setText("名称");

		Composite composite_4 = new Composite(sashForm_1, SWT.NONE);
		composite_4.setLayout(new FillLayout(SWT.HORIZONTAL));

		table_1 = new Table(composite_4, SWT.FULL_SELECTION);
		table_1.setToolTipText("歌词");
		table_1.setSortDirection(SWT.UP);
		table_1.setFont(SWTResourceManager.getFont("Microsoft YaHei UI", 10, SWT.NORMAL));
		table_1.setHeaderVisible(true);
		table_1.setLinesVisible(true);

		TableColumn tableColumn_3 = new TableColumn(table_1, SWT.CENTER);
		tableColumn_3.setWidth(42);
		tableColumn_3.setText("序号");

		TableColumn tableColumn_2 = new TableColumn(table_1, SWT.CENTER);
		tableColumn_2.setWidth(650);
		tableColumn_2.setText("歌词");

		Composite composite_5 = new Composite(sashForm_1, SWT.NONE);
		composite_5.setLayout(new FillLayout(SWT.HORIZONTAL));

		table_2 = new Table(composite_5, SWT.FULL_SELECTION);
		table_2.setHeaderVisible(true);
		table_2.setLinesVisible(true);

		TableColumn tableColumn_4 = new TableColumn(table_2, SWT.CENTER);
		tableColumn_4.setWidth(42);
		tableColumn_4.setText("序号");

		TableColumn tableColumn_5 = new TableColumn(table_2, SWT.NONE);
		tableColumn_5.setWidth(145);
		tableColumn_5.setText("歌词名称");
		//sashForm_1.setWeights(new int[] {186, 521, 174});
		sashForm_1.setWeights(new int[] {186, 693, 0});

		Composite composite_2 = new Composite(sashForm, SWT.NONE);

		Label label_2 = new Label(composite_2, SWT.NONE);
		label_2.setImage(SWTResourceManager.getImage(MusicPlayer.class, "/com/xu/musicplayer/image/prev.png"));
		label_2.setBounds(22, 30, 24, 24);

		label_3 = new Label(composite_2, SWT.NONE);
		label_3.setImage(SWTResourceManager.getImage(MusicPlayer.class, "/com/xu/musicplayer/image/stop.png"));
		label_3.setBounds(73, 30, 24, 24);

		Label label_4 = new Label(composite_2, SWT.NONE);
		label_4.setImage(SWTResourceManager.getImage(MusicPlayer.class, "/com/xu/musicplayer/image/next.png"));
		label_4.setBounds(120, 30, 24, 24);

		progressBar = new ProgressBar(composite_2, SWT.NONE);
		progressBar.setBounds(217, 50, 517, 15);

		text = new Text(composite_2, SWT.READ_ONLY);
		text.setFont(SWTResourceManager.getFont("Microsoft YaHei UI", 12, SWT.NORMAL));
		text.setEnabled(false);
		text.setBounds(217, 20, 301, 23);

		text_1 = new Text(composite_2, SWT.READ_ONLY);
		text_1.setFont(SWTResourceManager.getFont("Microsoft YaHei UI", 12, SWT.NORMAL));
		text_1.setEnabled(false);
		text_1.setBounds(610, 21, 123, 23);

		sashForm.setWeights(new int[] {50, 342, 85});

		//获取本地歌曲库
		getMusicPlayerFile();
		//getRegistry();

		//退出
		label_1.addMouseListener(new MouseAdapter() {
			public void mouseDown(MouseEvent e) {
				label_1.setImage(SWTResourceManager.getImage(MusicPlayer.class, "/com/xu/musicplayer/image/delete_2.png"));
			}
			public void mouseUp(MouseEvent e) {
				label_1.setImage(SWTResourceManager.getImage(MusicPlayer.class, "/com/xu/musicplayer/image/delete_1.png"));
				player.close();
				tray.dispose();	
				shell.dispose();			
			}
		});

		label_1.addMouseTrackListener(new MouseTrackAdapter() {
			public void mouseExit(MouseEvent e) {
				label_1.setImage(SWTResourceManager.getImage(MusicPlayer.class, "/com/xu/musicplayer/image/delete_1.png"));
			}
			public void mouseHover(MouseEvent e) {
				label_1.setImage(SWTResourceManager.getImage(MusicPlayer.class, "/com/xu/musicplayer/image/delete_2.png"));
				label_1.setToolTipText("退出");
			}
		});


		//缩小
		label.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				label.setImage(SWTResourceManager.getImage(MusicPlayer.class, "/com/xu/musicplayer/image/minus_2.png"));
			}
			@Override
			public void mouseUp(MouseEvent e) {
				label.setImage(SWTResourceManager.getImage(MusicPlayer.class, "/com/xu/musicplayer/image/minus_1.png"));
				shell.setMinimized(true);	
			}
		});

		label.addMouseTrackListener(new MouseTrackAdapter() {
			public void mouseExit(MouseEvent e) {				
				label.setImage(SWTResourceManager.getImage(MusicPlayer.class, "/com/xu/musicplayer/image/minus_1.png"));
			}
			@Override
			public void mouseHover(MouseEvent e) {
				label.setImage(SWTResourceManager.getImage(MusicPlayer.class, "/com/xu/musicplayer/image/minus_2.png"));
				label.setToolTipText("最小化");
			}
		});

		//界面移动
		composite.addMouseListener(new MouseAdapter() {
			public void mouseDown(MouseEvent e) {
				shellmove=true;
				clickX=e.x;
				clickY=e.y;
			}
			public void mouseUp(MouseEvent e) {
				shellmove=false;
			}
		});

		composite.addMouseMoveListener(new MouseMoveListener() {
			public void mouseMove(MouseEvent arg0) {//当鼠标按下的时候执行这条语句
				if(shellmove){
					shell.setLocation(shell.getLocation().x-clickX+arg0.x, shell.getLocation().y-clickY+arg0.y);
				}
			}
		});

		//确认播放歌曲
		label_3.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent arg0) {
				if(click){//这里播放歌曲成功
					label_3.setImage(SWTResourceManager.getImage(Player.class, "/com/xu/musicplayer/image/start.png"));
				}else{//这里是暂停歌曲
					label_3.setImage(SWTResourceManager.getImage(Player.class, "/com/xu/musicplayer/image/stop.png"));
				}

				if(click){
					click=false;
				}else{
					click=true;
				}
			}
			public void mouseUp(MouseEvent arg0) {
				start(PLAYREALPATH);
			}
		});

		//获取歌曲路径
		table.addMouseListener(new MouseAdapter() {			
			TableItem[] tableItem=table.getItems();			
			public void mouseDown(MouseEvent e) {//选中歌曲
				for(int i=0;i<table.getItemCount();i++){
					if(i==table.getSelectionIndex()){//选中的歌曲
						tableItem[i].setBackground(SWTResourceManager.getColor(SWT.COLOR_LIST_SELECTION));
					}else{//非选中的歌曲
						tableItem[i].setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
					}
				}				
			}
			public void mouseUp(MouseEvent e) {//获取歌曲路径
				String name=tableItem[table.getSelectionIndex()].getText(1);
				text.setText(name);
				getPath(name);
			}
			public void mouseDoubleClick(MouseEvent e) {//播放歌曲
				label_3.setImage(SWTResourceManager.getImage(Player.class, "/com/xu/musicplayer/image/start.png"));
				click=true;
				String name=tableItem[table.getSelectionIndex()].getText(1);
				String path=getPath(name);
				text.setText(name);
				start(path);
			}
		});

		//上一曲
		label_2.addMouseListener(new MouseAdapter() {
			public void mouseUp(MouseEvent e) {
				String [] temp=new String[PLAYLISTS.size()];
				PLAYLISTS.toArray(temp);
				PLAYREALPATH=temp[new Random().nextInt(temp.length)];
				while(PLAYREALPATH.toLowerCase().endsWith(".lrc")){
					PLAYREALPATH=temp[new Random().nextInt(temp.length)];
				}
				modifyListColor(PLAYREALPATH);
				start(PLAYREALPATH);
				modifyImage();
			}
		});

		//下一曲
		label_4.addMouseListener(new MouseAdapter() {
			public void mouseUp(MouseEvent e) {
				String [] temp=new String[PLAYLISTS.size()];
				PLAYLISTS.toArray(temp);
				PLAYREALPATH=temp[new Random().nextInt(temp.length)];
				while(PLAYREALPATH.toLowerCase().endsWith(".lrc")){
					PLAYREALPATH=temp[new Random().nextInt(temp.length)];
				}
				modifyListColor(PLAYREALPATH);
				start(PLAYREALPATH);
				modifyImage();
			}
		});

		//选择歌词
		table_2.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				TableItem[] item=table_2.getSelection();
				if(item!=null){
					int index=Integer.parseInt(item[0].getText(0));
					index=index-1;
					String url=netList.get(index).substring(netList.get(index).toString().indexOf("$")+1);
					String lyric="";
					try {
						lyric=new Analysis().getLyric(url.substring(0, url.lastIndexOf("."))+"/"+netList.get(index).substring(0, netList.get(index).toString().indexOf("$"))+".lrc");
					} catch (IOException e1) {
						e1.printStackTrace();
					}
					lyric(lyric);
				}
			}
		});

		//自动播放
		text_1.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent arg0) {
				String time=text_1.getText().trim();
				if(time!=null && "".equals(time)){
					String [] judege=time.split("/");
					System.out.println(judege[0]+"----"+judege[1]);
					if(judege.length>=2){
						if(judege[0].equals(judege[1])){
							autoPlay();
						}
					}
				}
			}
		});
		
		//进度条
		progressBar.addTouchListener(new TouchListener() {
			public void touch(TouchEvent arg0) {
				System.out.println(arg0.time);
			}
		});
		
	}

	/**
	 * 自动播放
	 */
	public void autoPlay(){
		String [] temp=new String[PLAYLISTS.size()];
		PLAYLISTS.toArray(temp);
		PLAYREALPATH=temp[new Random().nextInt(temp.length)];
		while(PLAYREALPATH.toLowerCase().endsWith(".lrc")){
			PLAYREALPATH=temp[new Random().nextInt(temp.length)];
		}
		modifyListColor(PLAYREALPATH);
		start(PLAYREALPATH);
		modifyImage();
	}

	public List<String> lyric(){
		return LYRICLISTS;
	}
	/**
	 * 开始播放音乐
	 * @param play
	 */
	@SuppressWarnings("all")
	private void start(String play){
		File file=new File(play);
		if(!file.exists()){
			tool.beep();
			MessageDialog.openError(shell, "错误提示", "歌曲不存在或者目录不正确!");
		}else{
			lyric(file);
			setRegistry(play);
			if(!HAVELYRIC){
				sashForm_1.setWeights(new int[] {186, 521, 174});
				String name=play.substring(play.lastIndexOf(File.separator)+1, play.lastIndexOf("."));
//				try {
//					netList=new Analysis().getLyricList(name);
//				} catch (IOException e) {
//					e.printStackTrace();
//				}
//				if(netList!=null){
//					TableItem item=null;
//					table_2.removeAll();
//					for(int i=0;i<netList.size();i++){
//						item=new TableItem(table_2, SWT.NONE);
//						item.setText(new String []{(i+1)+"",netList.get(i).substring(0, netList.get(i).toString().indexOf("$"))});
//					}
//				}
			}else{
				sashForm_1.setWeights(new int[] {186, 693, 0});
			}
			try {
				AudioFile getLength=AudioFileIO.read(file);//获取播放流
				MUSICLENGTH=getLength.getAudioHeader().getTrackLength();//获取播放时间
				if(playThread.getState().toString().equals("RUNNABLE")){
					playThread.stop();
				}else{
					playThread.stop();
				}
				if(STARTPLAY){
					player.close();
				}
				playThread=new Thread(new PlayerThread(MUSICLENGTH, LYRICLISTS, HAVELYRIC, new PlayerNotify() {
					public void PlayerResult(Object object) {
						final PlayerBean bean=(PlayerBean) object;
						Display.getDefault().asyncExec(new Runnable() {
							public void run() {
								if(!bean.isFinished()){
									progressBar.setSelection(bean.getReturnLong());
									text_1.setText(bean.getReturnTime());
									if(HAVELYRIC){
										for(int i=0;i<table_1.getItemCount();i++){												
											String lyric=table_1.getItem(i).getText(0).trim();
											if(lyric.equals(bean.getReturnIndex())){//选中的歌曲
												table_1.getItem(i).setBackground(SWTResourceManager.getColor(SWT.COLOR_LIST_SELECTION));
											}else{//非选中的歌曲
												table_1.getItem(i).setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
											}
										}										
									}
								}else{
									//TODO: dosomething
								}
							}
						});
					}
				}));
				playThread.setDaemon(true);
				playThread.start();
				mThread.start();

				player=Manager.createPlayer(file.toURL());
				player.start();
				STARTPLAY=true;

			} catch (Exception e) {
				logger.error(e.getMessage());
				e.printStackTrace();
			} 
		}
	}

	/**
	 * 播放框的图片改变
	 */
	private void modifyImage(){
		label_3.setImage(SWTResourceManager.getImage(Player.class, "/com/xu/musicplayer/image/start.png"));
		click=true;
	}

	/**
	 * 改变歌曲目录的选着歌曲所在行颜色
	 * @param play
	 */
	private void modifyListColor(String play){
		String name=play.substring(play.lastIndexOf(File.separator)+1,play.lastIndexOf("."));		
		for(int i=0;i<table.getItemCount();i++){
			if(table.getItem(i).getText(1).equals(name)){//选中的歌曲
				table.getItem(i).setBackground(SWTResourceManager.getColor(SWT.COLOR_LIST_SELECTION));
			}else{//非选中的歌曲
				table.getItem(i).setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
			}
		}
	}

	/**
	 * 获取歌曲歌词
	 * @param play
	 */
	/**
	 * @param musicfile
	 */
	private void lyric(File musicfile){
		String play=musicfile.toString();
		LYRICLISTS.clear();
		String lyricpath="";
		for(String playlist:PLAYLISTS){
			if(playlist.toLowerCase().endsWith(".lrc")){
				if(playlist.subSequence(0, playlist.lastIndexOf(".")).equals(play.substring(0, play.lastIndexOf(".")))){
					lyricpath=playlist;
					break;
				}
			}
		}
		File file=new File(lyricpath);
		if(file.exists()){
			HAVELYRIC=true;
			FileReader fReader=null;
			BufferedReader bReader=null;
			try {
				fReader = new FileReader(file);
				bReader=new BufferedReader(fReader);
				String txt="";
				String reg="\\[(\\d{2}:\\d{2}\\.\\d{2})\\]";				
				while((txt=bReader.readLine())!=null){
					if(txt.contains("[ti:")) {       // 歌曲信息
						LYRICLISTS.add("歌曲信息: "+txt.substring(txt.lastIndexOf(":")+1,txt.length()-1));
					}else if (txt.contains("[ar:")) {// 歌手信息
						LYRICLISTS.add("歌手信息: "+txt.substring(txt.lastIndexOf(":")+1,txt.length()-1));
					}else if (txt.contains("[al:")) {// 专辑信息
						LYRICLISTS.add("专辑信息: "+txt.substring(txt.lastIndexOf(":")+1,txt.length()-1));
					}else if (txt.contains("[wl:")) {// 歌词作家
						LYRICLISTS.add("歌词作家: "+txt.substring(txt.lastIndexOf(":")+1,txt.length()-1));
					}else if (txt.contains("[wm:")) {// 歌曲作家
						LYRICLISTS.add("歌曲作家: "+txt.substring(txt.lastIndexOf(":")+1,txt.length()-1));
					}else if (txt.contains("[al:")) {// 歌词制作
						LYRICLISTS.add("歌词制作: "+txt.substring(txt.lastIndexOf(":")+1,txt.length()-1));
					}
					Pattern pattern=Pattern.compile(reg);
					Matcher matcher=pattern.matcher(txt);
					while(matcher.find()){
						LYRICLISTS.add(matcher.group(0).substring(1,6).trim()+txt.substring(txt.lastIndexOf("]")+1).trim());
					}
				}
				reg="^\\d+$";
				for(int i=0;i<LYRICLISTS.size();i++){
					if(Pattern.matches(reg,LYRICLISTS.get(i).substring(0, 2))){
						for(int j=0;j<LYRICLISTS.size();j++){
							if(Pattern.matches(reg,LYRICLISTS.get(j).substring(0, 2))){
								if(Integer.parseInt(LYRICLISTS.get(i).substring(0, 2))<Integer.parseInt(LYRICLISTS.get(j).substring(0, 2))){
									String temp=LYRICLISTS.get(i);
									LYRICLISTS.set(i, LYRICLISTS.get(j));
									LYRICLISTS.set(j, temp);
								}
								if(Integer.parseInt(LYRICLISTS.get(i).substring(0, 2))==Integer.parseInt(LYRICLISTS.get(j).substring(0, 2)) && Double.parseDouble(LYRICLISTS.get(i).substring(3,5))<Double.parseDouble(LYRICLISTS.get(j).substring(3,5))){
									String temp=LYRICLISTS.get(i);
									LYRICLISTS.set(i, LYRICLISTS.get(j));
									LYRICLISTS.set(j, temp);
								}
							}
						}
					}
				}			
				TableItem tableItem=null;
				table_1.removeAll();
				int index=1;
				for(String lyric:LYRICLISTS){
					tableItem=new TableItem(table_1, SWT.NONE);
					tableItem.setText(0, index+"");
					tableItem.setText(1,lyric.substring(5));
					index++;
				}

			} catch (Exception e) {
				logger.error(e.getMessage());
				e.printStackTrace();
			}finally {
				if(fReader!=null){
					try {
						fReader.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				if(bReader!=null){
					try {
						bReader.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}

		}else{
			tool.beep();
			HAVELYRIC=false;
		}
	}

	/**
	 * 获取歌曲歌词
	 * @param play
	 */
	private void lyric(String play){
		String[] text=play.split("\r\n");
		String reg="\\[(\\d{2}:\\d{2}\\.\\d{2})\\]";
		for(String txt:text){
			if(txt.contains("[ti:")) {       // 歌曲信息
				LYRICLISTS.add("歌曲信息: "+txt.substring(txt.lastIndexOf(":")+1,txt.length()-1));
			}else if (txt.contains("[ar:")) {// 歌手信息
				LYRICLISTS.add("歌手信息: "+txt.substring(txt.lastIndexOf(":")+1,txt.length()-1));
			}else if (txt.contains("[al:")) {// 专辑信息
				LYRICLISTS.add("专辑信息: "+txt.substring(txt.lastIndexOf(":")+1,txt.length()-1));
			}else if (txt.contains("[wl:")) {// 歌词作家
				LYRICLISTS.add("歌词作家: "+txt.substring(txt.lastIndexOf(":")+1,txt.length()-1));
			}else if (txt.contains("[wm:")) {// 歌曲作家
				LYRICLISTS.add("歌曲作家: "+txt.substring(txt.lastIndexOf(":")+1,txt.length()-1));
			}else if (txt.contains("[al:")) {// 歌词制作
				LYRICLISTS.add("歌词制作: "+txt.substring(txt.lastIndexOf(":")+1,txt.length()-1));
			}
			
			Pattern pattern=Pattern.compile(reg);
			Matcher matcher=pattern.matcher(txt);
			
			while(matcher.find()){
				LYRICLISTS.add(matcher.group(0).substring(1,6).trim()+txt.substring(txt.lastIndexOf("]")+1).trim());
			}
			
			reg="^\\d+$";
			for(int i=0;i<LYRICLISTS.size();i++){
				if(Pattern.matches(reg,LYRICLISTS.get(i).substring(0, 2))){
					for(int j=0;j<LYRICLISTS.size();j++){
						if(Pattern.matches(reg,LYRICLISTS.get(j).substring(0, 2))){
							if(Integer.parseInt(LYRICLISTS.get(i).substring(0, 2))<Integer.parseInt(LYRICLISTS.get(j).substring(0, 2))){
								String temp=LYRICLISTS.get(i);
								LYRICLISTS.set(i, LYRICLISTS.get(j));
								LYRICLISTS.set(j, temp);
							}
							if(Integer.parseInt(LYRICLISTS.get(i).substring(0, 2))==Integer.parseInt(LYRICLISTS.get(j).substring(0, 2)) && Double.parseDouble(LYRICLISTS.get(i).substring(3,5))<Double.parseDouble(LYRICLISTS.get(j).substring(3,5))){
								String temp=LYRICLISTS.get(i);
								LYRICLISTS.set(i, LYRICLISTS.get(j));
								LYRICLISTS.set(j, temp);
							}
						}
					}
				}
			}
			
		}
		TableItem tableItem=null;
		table_1.removeAll();
		int index=1;
		for(String lyric:LYRICLISTS){
			tableItem=new TableItem(table_1, SWT.NONE);
			tableItem.setText(0, index+"");
			tableItem.setText(1,lyric.substring(5));
			index++;
		}
		//		HAVELYRIC=true;
		lyric();
	}

	/**
	 * 获取歌曲的真是路径
	 * @param musicname
	 * @return 
	 */
	private String getPath(String musicname){
		String realpath="";
		for(String playlist:PLAYLISTS){
			if(playlist.toLowerCase().endsWith(".mp3")){
				if(playlist.subSequence(playlist.lastIndexOf(File.separator)+1,playlist.lastIndexOf(".")).equals(musicname)){
					PLAYREALPATH=playlist;
					realpath=playlist;
					break;
				}
			}
		}
		return realpath;
	}

	/**
	 * 导入本地的播放文件(本地歌曲目录不存在，需要从新导入)
	 * @author Administrator
	 */
	private void inputMusicPlayerFile(){
		FileDialog fileDialog=new FileDialog(shell,SWT.OPEN|SWT.MULTI);
		fileDialog.setFilterExtensions(new String[]{"*.mp3","*.wma","*.wav","*.wav",".lrc"});
		fileDialog.open();
		String[] playlist=fileDialog.getFileNames();
		table.removeAll();
		TableItem tableItem=null;
		int index=1;
		for(int i=0;i<playlist.length;i++){
			if(playlist[i]!=null && !playlist[i].equals("")){
				if(!playlist[i].toLowerCase().endsWith(".lrc")){
					tableItem=new TableItem(table, SWT.NONE);
					tableItem.setText(new String[]{index+"",playlist[i].substring(0, playlist[i].lastIndexOf("."))});
					index++;
				}
			}
			PLAYLISTS.add(fileDialog.getFilterPath()+File.separator+playlist[i]);
		}

		FileWriter FWriter=null;
		BufferedWriter BWriter=null;
		try {
			File file=new File("G:\\KuGou\\test.txt");
			FWriter=new FileWriter(file);
			BWriter=new BufferedWriter(FWriter);
			for(String plays:PLAYLISTS){
				BWriter.write(plays);
				BWriter.newLine();
			}
			BWriter.flush();
		} catch (IOException e) {
			logger.error(e.getMessage());
			e.printStackTrace();
		}finally {
			if(BWriter!=null){
				try {
					BWriter.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if(FWriter!=null){
				try {
					FWriter.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 获取本地的播放文件(本地歌曲目录已存在)
	 * @author Administrator
	 */
	private void getMusicPlayerFile(){//获取本地的播放文件(本地歌曲目录已存在)
		FileReader FReader=null;
		BufferedReader BReader=null;
		try {
			File file=new File("G:\\KuGou\\test.txt");
			if(!file.exists()){				
				tool.beep();
				MessageDialog.openError(shell, "错误提示", "缺失歌曲索引!");
				inputMusicPlayerFile();
			}else{
				FReader=new FileReader(file);
				BReader=new BufferedReader(FReader);
				String txt="";
				table.removeAll();
				TableItem tableItem=null;
				int index=1;
				while((txt=BReader.readLine())!=null){
					if(!txt.equals("") && !txt.toLowerCase().endsWith(".lrc")){
						tableItem=new TableItem(table, SWT.NONE);
						tableItem.setText(new String[]{index+"",txt.substring(txt.lastIndexOf(File.separator)+1, txt.lastIndexOf("."))});
						index++;
					}
					PLAYLISTS.add(txt);
				}
			}
		} catch (IOException e) {
			logger.error(e.getMessage());
			e.printStackTrace();
		}finally {
			if(FReader!=null){
				try {
					FReader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if(BReader!=null){
				try {
					BReader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private void setRegistry(String path) {
		Preferences preferences=Preferences.userNodeForPackage(MusicPlayer.class);
		if(preferences.get("MusicPlayer", null)==null){
			preferences.put("MusicPlayer", path);
		}else if(preferences.get("MusicPlayer", null).equals(path)){

		}else{
			preferences.remove("MusicPlayer");			
		}
		try {
			preferences.flush();
		} catch (BackingStoreException e) {
			logger.error(e.getMessage());
			e.printStackTrace();
		}
	}

	private void getRegistry(){
		Preferences preferences=Preferences.userNodeForPackage(MusicPlayer.class);
		if(preferences.get("MusicPlayer", null)!=null || !"".equals(preferences.get("MusicPlayer", null)) ){
			String mu=preferences.get("MusicPlayer", null).toString();
			start(mu);
			text.setText(mu.substring(mu.lastIndexOf(File.separator)+1, mu.lastIndexOf(".")));
			label_3.setImage(SWTResourceManager.getImage(Player.class, "/com/xu/musicplayer/image/start.png"));
			click=false;
		}
	}

}

