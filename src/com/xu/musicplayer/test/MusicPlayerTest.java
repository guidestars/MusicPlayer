package com.xu.musicplayer.test;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.Shell;

import org.eclipse.swt.widgets.Button;
import java.awt.Toolkit;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

import javax.media.Manager;
import javax.media.Player;
import javax.swing.JFileChooser;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;

import com.xu.musicplayer.main.MusicPlayer;
import com.xu.musicplayer.search.SearchMusic;
import com.xu.musicplayer.trayutil.TrayUtil;
import com.xu.roboto.entity.MusicList;

import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tray;
import org.eclipse.wb.swt.SWTResourceManager;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.events.MouseTrackAdapter;

public class MusicPlayerTest {

	protected Shell shell;
	private Display display;

	//	private AudioStream audio;
	private Player player;
	private Button button ;
	private Table table;
	private Tray tray;
	private String playPath;

	static int totalPlayTime;

	private Text text;
	private Thread playProgress;
	private ProgressBar progressBar ;
	private Text text_1;
	private int clickX,clickY;
	private Boolean click=false;
	private Label label_5 ;
	private int judeg=0;
	private StringBuilder BufferPlayList;
	private Text text_2;
	private Text text_3;
	private int lyricTimes=0;
	private String [] lyrics=new String[200];

	protected static boolean haslyric=false;

	private static int playListLength=0;
	private Table table_1;
	private String[] playlist;
	private GC gc;

	private List<MusicList> musicLists = null;

	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			MusicPlayerTest window = new MusicPlayerTest();
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
	 * @wbp.parser.entryPoint
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

		Composite composite = new Composite(shell, SWT.NO_RADIO_GROUP);
		composite.setLayout(new FormLayout());


		button = new Button(composite, SWT.NONE);
		button.setImage(SWTResourceManager.getImage(MusicPlayer.class, "/com/xu/musicplayer/image/addMusic.png"));
		FormData fd_button = new FormData();
		fd_button.right = new FormAttachment(100, -803);
		fd_button.left = new FormAttachment(0);
		button.setLayoutData(fd_button);
		button.setText(" 添  加  ");
		FormData fd_composite_2 = new FormData();
		fd_composite_2.left = new FormAttachment(0, 105);
		fd_composite_2.bottom = new FormAttachment(100, -10);
		fd_composite_2.top = new FormAttachment(0, 382);
		fd_composite_2.right = new FormAttachment(100, -123);

		table = new Table(composite, SWT.FULL_SELECTION);
		FormData fd_table = new FormData();
		fd_table.top = new FormAttachment(button, 6);
		fd_table.left = new FormAttachment(0);
		fd_table.right = new FormAttachment(100, -689);
		table.setLayoutData(fd_table);
		table.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_LIGHT_SHADOW));

		table.setHeaderVisible(true);
		table.setLinesVisible(true);

		TableColumn tableColumn_2 = new TableColumn(table, SWT.NONE);
		tableColumn_2.setWidth(37);
		tableColumn_2.setText("编号");

		TableColumn tableColumn_1 = new TableColumn(table, SWT.NONE);
		tableColumn_1.setWidth(244);
		tableColumn_1.setText("默 认 列 表");

		Composite composite_1 = new Composite(composite, SWT.NONE);
		fd_button.top = new FormAttachment(composite_1, 6);
		composite_1.setBackgroundMode(SWT.INHERIT_DEFAULT);
		composite_1.setBackground(SWTResourceManager.getColor(SWT.COLOR_CYAN));
		FormData fd_composite_1 = new FormData();
		fd_composite_1.top = new FormAttachment(0);
		fd_composite_1.left = new FormAttachment(0);
		fd_composite_1.right = new FormAttachment(0, 887);
		fd_composite_1.bottom = new FormAttachment(0, 56);
		composite_1.setLayoutData(fd_composite_1);

		Label label = new Label(composite_1,SWT.NONE);
		label.setImage(SWTResourceManager.getImage(MusicPlayer.class, "/com/xu/musicplayer/image/hand.png"));
		label.setBounds(0, 0, 56, 56);		
		gc=new GC(label);
		gc.fillOval(0,0,56,56);
		gc.setBackground(SWTResourceManager.getColor(new RGB(11, 22, 22)));
		gc.dispose();

		final Label label_1 = new Label(composite_1, SWT.NONE);

		label_1.setImage(SWTResourceManager.getImage(MusicPlayer.class, "/com/xu/musicplayer/image/minus_1.png"));
		label_1.setBounds(817, 0, 32, 32);

		final Label label_2 = new Label(composite_1, SWT.NONE);

		label_2.setImage(SWTResourceManager.getImage(MusicPlayer.class, "/com/xu/musicplayer/image/delete_1.png"));
		label_2.setBounds(855, 0, 32, 32);

		Label lblNewLabel = new Label(composite_1, SWT.NONE);
		lblNewLabel.setBounds(72, 20, 61, 17);
		lblNewLabel.setText("月色深蓝");

		Composite composite_3 = new Composite(composite, SWT.NONE);
		fd_table.bottom = new FormAttachment(composite_3, -6);
		composite_3.setBackgroundMode(SWT.INHERIT_DEFAULT);
		composite_3.setBackground(SWTResourceManager.getColor(SWT.COLOR_GRAY));
		FormData fd_composite_3 = new FormData();
		fd_composite_3.top = new FormAttachment(0, 409);
		fd_composite_3.bottom = new FormAttachment(100);
		fd_composite_3.right = new FormAttachment(composite_1, 0, SWT.RIGHT);

		text_2 = new Text(composite_1, SWT.NONE);
		text_2.setForeground(SWTResourceManager.getColor(SWT.COLOR_CYAN));
		text_2.setFont(SWTResourceManager.getFont("Microsoft YaHei UI", 16, SWT.NORMAL));
		text_2.setBounds(281, 12, 336, 32);
		GC gc=new GC(text_2);
		gc.setBackground(SWTResourceManager.getColor(new RGB(255, 0, 0)));
		gc.fillOval(10, 10, 10, 10);
		gc.dispose();

		Label label_6 = new Label(composite_1, SWT.NONE);
		label_6.setImage(SWTResourceManager.getImage(MusicPlayer.class, "/com/xu/musicplayer/image/find.png"));
		label_6.setBounds(640, 12, 32, 32);
		fd_composite_3.left = new FormAttachment(0);
		composite_3.setLayoutData(fd_composite_3);

		Label label_3 = new Label(composite_3, SWT.NONE);

		label_3.setImage(SWTResourceManager.getImage(MusicPlayer.class, "/com/xu/musicplayer/image/prev.png"));
		label_3.setBounds(24, 27, 24, 24);

		Composite composite_2 = new Composite(composite_3, SWT.NONE);
		composite_2.setLocation(225, 48);
		composite_2.setSize(452, 10);
		composite_2.setLayout(new FillLayout(SWT.HORIZONTAL));

		progressBar = new ProgressBar(composite_2, SWT.NONE);
		progressBar.setBounds(0, 0, 655, 27);//设置进度条的位置

		Label label_4 = new Label(composite_3, SWT.NONE);

		label_4.setImage(SWTResourceManager.getImage(MusicPlayer.class, "/com/xu/musicplayer/image/next.png"));
		label_4.setBounds(132, 27, 24, 24);


		text_1 = new Text(composite_3, SWT.READ_ONLY);
		text_1.setBackground(SWTResourceManager.getColor(SWT.COLOR_GRAY));
		text_1.setBounds(225, 10, 287, 27);
		text_1.setForeground(SWTResourceManager.getColor(SWT.COLOR_GREEN));
		text_1.setFont(SWTResourceManager.getFont("Microsoft YaHei UI", 12, SWT.NORMAL));
		text_1.setEnabled(false);

		text = new Text(composite_3, SWT.READ_ONLY | SWT.CENTER);
		text.setBackground(SWTResourceManager.getColor(SWT.COLOR_GRAY));
		text.setBounds(541, 10, 136, 27);
		text.setForeground(SWTResourceManager.getColor(SWT.COLOR_GREEN));
		text.setFont(SWTResourceManager.getFont("Microsoft YaHei UI", 12, SWT.NORMAL));
		text.setEnabled(false);


		label_5 = new Label(composite_3, SWT.NONE);
		label_5.setImage(SWTResourceManager.getImage(MusicPlayer.class, "/com/xu/musicplayer/image/stop.png"));
		label_5.setBounds(79, 27, 24, 24);

		Button button_1 = new Button(composite, SWT.NONE);
		button_1.setText("扫  描");
		button_1.setImage(SWTResourceManager.getImage(MusicPlayer.class, "/com/xu/musicplayer/image/search.png"));
		FormData fd_button_1 = new FormData();
		fd_button_1.left = new FormAttachment(table, -84);
		fd_button_1.bottom = new FormAttachment(button, 0, SWT.BOTTOM);
		fd_button_1.right = new FormAttachment(table, 0, SWT.RIGHT);
		button_1.setLayoutData(fd_button_1);

		text_3 = new Text(composite, SWT.READ_ONLY | SWT.CENTER);
		text_3.setFont(SWTResourceManager.getFont("Microsoft YaHei UI", 16, SWT.NORMAL));
		text_3.setForeground(SWTResourceManager.getColor(SWT.COLOR_RED));
		FormData fd_text_3 = new FormData();
		fd_text_3.right = new FormAttachment(table, 629, SWT.RIGHT);
		fd_text_3.top = new FormAttachment(composite_1, 313);
		fd_text_3.bottom = new FormAttachment(composite_3, -6);
		fd_text_3.left = new FormAttachment(table, 6);
		text_3.setLayoutData(fd_text_3);

		table_1 = new Table(composite, SWT.FULL_SELECTION);
		FormData fd_table_1 = new FormData();
		fd_table_1.bottom = new FormAttachment(text_3, -6);
		fd_table_1.right = new FormAttachment(text_3, 0, SWT.RIGHT);
		fd_table_1.top = new FormAttachment(table, 0, SWT.TOP);
		fd_table_1.left = new FormAttachment(table, 6);
		table_1.setLayoutData(fd_table_1);
		table_1.setHeaderVisible(true);
		table_1.setLinesVisible(true);

		TableColumn tableColumn = new TableColumn(table_1, SWT.CENTER);
		tableColumn.setWidth(621);
		tableColumn.setText("                                                                        歌词");


		button_1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				new SearchMusic().open();//寻找播放文件的界面
			}
		});
		
		getRegistry();
		
		//playProgress.setDaemon(true);
		getMusicPlayerFile();//读取本地播放文件
		playProgress=new Thread(new PlayProgress(display));

		//选择菜单中的音乐
		table.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				TableItem[ ] items=table.getSelection();
				playPath=items[0].getText(1);
				for(String lyric:playlist){
					if(lyric.substring(0,lyric.lastIndexOf(".")).equals(playPath)){
						playPath=lyric;
					}
				}
			}
		});

		//导入本地播放文件
		button.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				inputMusicPlayerFile();
			}
		});


		//退出
		label_2.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				label_2.setImage(SWTResourceManager.getImage(MusicPlayer.class, "/com/xu/musicplayer/image/delete_2.png"));
			}
			@Override
			public void mouseUp(MouseEvent e) {
				label_2.setImage(SWTResourceManager.getImage(MusicPlayer.class, "/com/xu/musicplayer/image/delete_1.png"));
				tray.dispose();
				distory();
			}
		});

		label_2.addMouseTrackListener(new MouseTrackAdapter() {
			@Override
			public void mouseExit(MouseEvent e) {
				label_2.setImage(SWTResourceManager.getImage(MusicPlayer.class, "/com/xu/musicplayer/image/delete_1.png"));
			}
			@Override
			public void mouseHover(MouseEvent e) {
				label_2.setImage(SWTResourceManager.getImage(MusicPlayer.class, "/com/xu/musicplayer/image/delete_2.png"));
				label_2.setToolTipText("退出");
			}
		});


		//缩小
		label_1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				label_1.setImage(SWTResourceManager.getImage(MusicPlayer.class, "/com/xu/musicplayer/image/minus_2.png"));
			}
			@Override
			public void mouseUp(MouseEvent e) {
				label_1.setImage(SWTResourceManager.getImage(MusicPlayer.class, "/com/xu/musicplayer/image/minus_1.png"));
				shell.setMinimized(true);	
			}
		});

		label_1.addMouseTrackListener(new MouseTrackAdapter() {
			@Override
			public void mouseExit(MouseEvent e) {				
				label_1.setImage(SWTResourceManager.getImage(MusicPlayer.class, "/com/xu/musicplayer/image/minus_1.png"));
			}
			@Override
			public void mouseHover(MouseEvent e) {
				label_1.setImage(SWTResourceManager.getImage(MusicPlayer.class, "/com/xu/musicplayer/image/minus_2.png"));
				label_1.setToolTipText("最小化");
			}
		});

		progressBar.addMouseListener(new MouseAdapter() {
			public void mouseDown(MouseEvent e){
			}
		});

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

		//播放按钮
		label_5.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
			}
			@Override
			public void mouseUp(MouseEvent e) {
				judeg++;
				if(judeg%2!=0){//单击音乐开始
					if(playPath==null || "".equals(playPath)){
						Toolkit toolkit=Toolkit.getDefaultToolkit();
						toolkit.beep();
						MessageDialog.openError(shell, "错误提示", "请选择播放文件");
					}else{
						label_5.setImage(SWTResourceManager.getImage(MusicPlayer.class, "/com/xu/musicplayer/image/start.png"));
						musicPlayerStart(playPath);
					}
				}else{//单击音乐停止
					label_5.setImage(SWTResourceManager.getImage(MusicPlayer.class, "/com/xu/musicplayer/image/stop.png"));
					playProgress.stop();
					progressBar.setSelection(0);
					player.close();
					text.setText("");
				}
				if(judeg==4){
					judeg=0;
				}
			}
		});

		//上一曲
		label_3.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				if(text_1.getText().trim()==null||"".equals(text_1.getText().trim())){
					String [] musicList=BufferPlayList.toString().split("<-->");					
					int playLists=new Random().nextInt(musicList.length)+1;
					if(!musicList[playLists].endsWith(".lrc")){
						playPath=musicList[playLists];
					}else{
						if(playLists==musicList.length){
							playPath=musicList[playLists-1];
						}else if(playLists==0){
							playPath=musicList[playLists+1];
						}else{
							playPath=musicList[playLists+1];
						}
					}
					label_5.setImage(SWTResourceManager.getImage(MusicPlayer.class, "/com/xu/musicplayer/image/start.png"));
					musicPlayerStart(playPath.trim());//开始音乐
					judeg=1;
				}else{
					//结束现在所有
					endAll();					
					String [] musicList=BufferPlayList.toString().split("<-->");					
					int playLists=new Random().nextInt(musicList.length)+1;
					if(!musicList[playLists].endsWith(".lrc")){
						playPath=musicList[playLists];
					}else{
						if(playLists==musicList.length){
							playPath=musicList[playLists-1];
						}else if(playLists==0){
							playPath=musicList[playLists+1];
						}else{
							playPath=musicList[playLists+1];
						}
					}
					label_5.setImage(SWTResourceManager.getImage(MusicPlayer.class, "/com/xu/musicplayer/image/start.png"));
					musicPlayerStart(playPath.trim());//开始音乐
					judeg=1;
				}
			}
		});

		//下一曲
		label_4.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				if(text_1.getText().trim()==null||"".equals(text_1.getText().trim())){
					int playLists=new Random().nextInt(playlist.length);
					if(!playlist[playLists].endsWith(".lrc")){
						playPath=playlist[playLists];
					}else{
						if(playLists==playlist.length){
							playPath=playlist[playLists-1];
						}else if(playLists==0){
							playPath=playlist[playLists+1];
						}else{
							playPath=playlist[playLists+1];
						}
					}
					label_5.setImage(SWTResourceManager.getImage(MusicPlayer.class, "/com/xu/musicplayer/image/start.png"));
					musicPlayerStart(playPath.trim());//开始音乐
					judeg=1;

				}else{
					//结束现在所有
					endAll();						
					int playLists=new Random().nextInt(playlist.length);
					if(!playlist[playLists].endsWith(".lrc")){
						playPath=playlist[playLists];
					}else{
						if(playLists==playlist.length){
							playPath=playlist[playLists-1];
						}else if(playLists==0){
							playPath=playlist[playLists+1];
						}else{
							playPath=playlist[playLists+1];
						}
					}
					label_5.setImage(SWTResourceManager.getImage(MusicPlayer.class, "/com/xu/musicplayer/image/start.png"));
					musicPlayerStart(playPath.trim());//开始音乐
					judeg=1;
				}
			}
		});

		//下载歌词
		table_1.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(haslyric==false){
					//int index=table_1.getItemCount();
					//String songid=musicLists.get(index).getSongid();
				}

			}
		});

	}

	public void endAll(){
		//结束现在所有
		playProgress.stop();
		player.close();
		progressBar.setSelection(0);
	}

	/**
	 * 网上查找歌词
	 * @param playPaths
	 */
	public void findLyric(String playPaths){
		//		String musicname=playPath.substring(playPath.lastIndexOf("\\")+1, playPath.lastIndexOf("."));
		//
		//		try {
		//			musicLists = Analysis.getMusicList(musicname);
		//		} catch (IOException e) {
		//			e.printStackTrace();
		//		}
		//		if(haslyric==false){
		//			table_1.removeAll();
		//			TableItem item;
		//			if(musicLists!=null){
		//				for(MusicList musicList:musicLists){
		//					item=new TableItem(table_1,SWT.NONE);
		//					item.setText("歌曲名字:"+musicList.getSongname()+"\t"+"演唱者:"+musicList.getArtistname());
		//				}
		//			}
		//		}
	}

	/**
	 * 显示歌词
	 * @param playPaths
	 */
	private void lyric(String playPaths){

		TableItem tableItem=null;
		table_1.removeAll();

		if(playPaths.endsWith("wav")||playPaths.endsWith("mp3")){//对传进来的歌曲进行截取
			playPaths=playPaths.substring(0, playPaths.lastIndexOf("."));
		}
		for(String lyric:playlist){//为歌词匹配路径
			if(lyric.endsWith("lrc") || lyric.endsWith("LRC")){
				if(lyric.trim().substring(0, lyric.lastIndexOf(".")).equals(playPaths)){
					playPaths=lyric;
					haslyric=true;
					break;
				}else{
					haslyric=false;
				}
			}
		}
		if(haslyric){
			String m;//分钟
			String s;//秒钟
			int index=0;
			try {
				File file=new File(playPaths);
				if(file.exists()){
					FileReader reader=new FileReader(file);
					BufferedReader bufferedReader=new BufferedReader(reader);
					String text;
					while((text=bufferedReader.readLine())!= null){
						String time=text.substring(1,text.lastIndexOf("]"));        //歌曲时间
						String lyric=text.substring(text.lastIndexOf("]")+1).trim();//歌词
						if(time.startsWith("0")){
							m=time.substring(1, 2);
							if(time.substring(3).startsWith("0")){
								s="0"+time.substring(4,5);
							}else{
								s=time.substring(3,5);
							}
							lyrics[index]=m+":"+s+"--"+lyric;
							index++;
						}
					}
					reader.close();
					bufferedReader.close();
				}else{
					haslyric=false;
				}
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			for(String lyric:lyrics){
				if(lyric!=null && lyric.length()>0){
					tableItem=new TableItem(table_1, SWT.NONE);
					tableItem.setText(lyric.substring(6));
				}
			}
		}else{
			text_3.setText("感谢使用JAVA音乐播放器");
		}
	}

	/**
	 * 自动播放
	 */
	public void autoPlay(){
		int playLists=new Random().nextInt(playlist.length);
		if(!playlist[playLists].endsWith(".lrc")){
			playPath=playlist[playLists];
		}else{
			if(playLists==playlist.length){
				playPath=playlist[playLists-1];
			}else if(playLists==0){
				playPath=playlist[playLists+1];
			}else{
				playPath=playlist[playLists+1];
			}
		}
		musicPlayerStart(playPath);
	}


	/**
	 * 改变列表的颜色
	 * @param Music (改变选中歌曲的颜色)
	 */

	private void updateList(String Music){
		if(Music.toLowerCase().endsWith("wav")||Music.endsWith("mp3")){//对传进来的歌曲进行截取
			Music=Music.substring(Music.lastIndexOf("\\")+1, Music.lastIndexOf("."));
		}
		TableItem[] tableItem=table.getItems();
		for(int i=0;i<tableItem.length;i++){
			if(tableItem[i].getText(1).equals(Music)){
				tableItem[i].setBackground(SWTResourceManager.getColor(SWT.COLOR_BLUE));
			}else{
				tableItem[i].setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
			}
		}	
	}

	/**
	 * 开始播放音乐
	 * @param playPath
	 * @author Administrator
	 */
	public void musicPlayerStart(String playPath){//开始播放音乐
		if(!playPath.endsWith("lrc") && !playPath.endsWith("wav")){//为进来的歌曲名字不以 .wav 结尾的歌曲找到源歌曲路径
			for(String lyric:playlist){
				if(lyric.toLowerCase().endsWith("wav") || lyric.endsWith("mp3")){
					if(lyric.substring(lyric.lastIndexOf("\\")+1, lyric.lastIndexOf(".")).equals(playPath)){
						playPath=lyric;
					}
				}
			}
		}
		updateList(playPath);//改变歌曲颜色
		lyric(playPath);     //查看是否有歌词
		//findLyric(playPath);

		File file=new File(playPath);
		if(file.exists()){
			if(!"".equals(playPath) && playPath!=null){
				if(playPath.trim().substring(playPath.lastIndexOf(".")+1).equalsIgnoreCase("wav")){
					try {
						player=Manager.createPlayer(new File(playPath).toURL());
						player.start();
						text_1.setText(playPath.substring(playPath.lastIndexOf("\\")+1, playPath.lastIndexOf(".")));

						AudioFile mp3=AudioFileIO.read(new File(playPath));//获取播放流
						totalPlayTime=mp3.getAudioHeader().getTrackLength();//获取播放时间

						progressBar.setMaximum(100);//设置进度条的最大长度
						progressBar.setSelection(100);
						progressBar.setMinimum(0);//设置进度的条最小程度				

						if(playProgress.getState().toString().equalsIgnoreCase("new")){
							playProgress.start();
						}else if(playProgress.getState().toString().equalsIgnoreCase("runable")){

						}else if(playProgress.getState().toString().equalsIgnoreCase("TERMINATED")){//已销毁
							playProgress=new Thread(new PlayProgress(display));
							playProgress.start();
						}else{
							playProgress.start();
						}					

					} catch (Exception e1) {
						e1.printStackTrace();
					}
				}else{
					for(int i=0;i<playlist.length;i++){
						String music=playlist[i].trim().substring(playlist[i].lastIndexOf("\\")+1, playlist[i].lastIndexOf("."));
						if(playPath.equalsIgnoreCase(music)){
							playPath=playlist[i].trim();
						}
					}
					try {		
						setRegistry(playPath);
						player=Manager.createPlayer(new File(playPath).toURL());
						player.start();
						text_1.setText(playPath.substring(playPath.lastIndexOf("\\")+1, playPath.lastIndexOf(".")));
						AudioFile mp3=AudioFileIO.read(new File(playPath));//获取播放流
						totalPlayTime=mp3.getAudioHeader().getTrackLength();        //获取播放时间

						progressBar.setMaximum(100);//设置进度条的最大长度
						progressBar.setMinimum(0);//设置进度的条最小程度

						if(playProgress.getState().toString().equalsIgnoreCase("NEW")){
							playProgress.start();
						}else if(playProgress.getState().toString().equalsIgnoreCase("runable")){

						}else if(playProgress.getState().toString().equalsIgnoreCase("TERMINATED")){//已销毁
							playProgress=new Thread(new PlayProgress(display));
							playProgress.start();
						}else{
							playProgress.start();
						}						

					} catch (Exception e1) {
						e1.printStackTrace();
					}
				}
			}else{
				Toolkit toolkit=Toolkit.getDefaultToolkit();
				toolkit.beep();
				MessageDialog.openError(shell, "错误提示", "请选择播放文件");
			}
		}else{
			Toolkit toolkit=Toolkit.getDefaultToolkit();
			toolkit.beep();
			MessageDialog.openError(shell, "错误提示", "播放文件不存在");
		}
	}

	/**
	 * 导入本地的播放文件(本地歌曲目录不存在，需要从新导入)
	 * @author Administrator
	 * 
	 */
	public void inputMusicPlayerFile(){//导入本地的播放文件(本地歌曲目录不存在，需要从新导入)
		JFileChooser choise=new JFileChooser(new File("G:"));//设置默认路径
		choise.setFileSelectionMode(JFileChooser.FILES_ONLY);//只选择文件而不选择文件夹
		choise.setMultiSelectionEnabled(true);               //可以多选文件
		choise.showOpenDialog(choise);                       //显示窗口
		File[] files=choise.getSelectedFiles();              //选中的文件

		try {
			File file=new File("E:\\musicList.txt");//读取文件
			/*	if(file.exists()){
				file.delete();
			}else{
				file.createNewFile();
				//file.setReadOnly();
			}*/
			table.removeAll();//去除列表中的全部歌曲
			FileWriter writer=new FileWriter(file);//写出流
			BufferedWriter bufferedWriter=new BufferedWriter(writer);//Buffere流
			TableItem item=null;
			BufferPlayList=new StringBuilder();
			table.removeAll();
			int index=1;
			for(File f:files){
				if(f.isFile()){ 
					bufferedWriter.write(f.toString());//将播放文件地址写入文件中
					bufferedWriter.newLine();//换行
					bufferedWriter.flush();//刷新
					if(f.toString().toLowerCase().endsWith("wav")||f.toString().endsWith("mp3")){//只有音乐文件才能添加到播放列表中
						item=new TableItem(table, SWT.NONE);//新建一个列表的行
						item.setText(new String[ ]{""+index,f.toString().substring(f.toString().lastIndexOf("\\")+1,f.toString().lastIndexOf("."))});
						index++;
					}
					BufferPlayList.append(f.toString()+"<-->");
					playlist=new String[BufferPlayList.length()];
					playlist=BufferPlayList.toString().split("<-->");
				}					
			}
			bufferedWriter.close();//结束写入流
			writer.close();//结束写入流
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	/**
	 * 获取本地的播放文件(本地歌曲目录已存在)
	 * @author Administrator
	 * 
	 */
	public void getMusicPlayerFile(){//获取本地的播放文件(本地歌曲目录已存在)
		TableItem[ ] items=table.getSelection();
		if(items.length<=0){
			File file=new File("E:\\musicList.txt");
			if(!file.exists()){
				Toolkit toolkit=Toolkit.getDefaultToolkit();
				toolkit.beep();
				MessageDialog.openError(shell, "错误提示", "丢失歌曲文件目录索引，请重新添加文件");
				inputMusicPlayerFile();//如果文件不存在就从新导入文件
			}else{
				FileReader reader;
				BufferPlayList=new StringBuilder();
				try {
					reader = new FileReader(file);
					BufferedReader bufferedReader=new BufferedReader(reader);
					String txt="";
					TableItem item;
					int index=1;
					while((txt=bufferedReader.readLine())!=null){
						if(txt.toLowerCase().endsWith("wav")||txt.endsWith("mp3")){
							item=new TableItem(table, SWT.NONE);
							item.setText(new String[ ]{""+index,txt.substring(txt.toString().lastIndexOf("\\")+1,txt.toString().lastIndexOf("."))});
							index++;							
						}
						BufferPlayList.append(txt+"<-->");
						playlist=new String[BufferPlayList.length()];
						playlist=BufferPlayList.toString().split("<-->");
					}
					bufferedReader.close();
					reader.close();
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 音乐时间、进度条 线程
	 * @author Administrator
	 *
	 */

	class PlayProgress implements Runnable{
		//private int totalPlayTime;
		private Display display;
		private int autoAddLyricTime=0;      //歌词的时间
		private int autoAddShowTime=0;       //文本框显示的歌曲播放的时间
		private int autoAddprogressBarTime=0;//进度条的时间
		private String tempLyric="";

		public PlayProgress(Display display){		
			this.display=display;	
		}

		public void run() {
			autoAddShowTime=totalPlayTime;
			for(int i=0;i<totalPlayTime;i++){
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				display.getDefault().asyncExec(new Runnable() {//这个是同步主线程的资源而不发生异常
					public void run() {
						autoAddprogressBarTime++;//进度条的时间
						autoAddLyricTime++;      //歌词的时间
						autoAddShowTime--;       //文本框显示的歌曲播放的时间

						TableItem [] items=table_1.getItems();
						if(haslyric){//如果这首歌曲拥有歌词
							tempLyric=getTime(autoAddLyricTime);
							for(String lyric:lyrics){
								if(lyric!=null && lyric.length()>0){
									if(tempLyric.trim().equals(lyric.substring(0,4))){
										text_3.setText(lyric.substring(6));
										for(int i=0;i<items.length;i++){
											if(items[i].getText(0).equals(lyric.substring(6))){
												items[i].setBackground(SWTResourceManager.getColor(SWT.COLOR_BLUE));
											}else{
												items[i].setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
											}
										}
									}
								}
							}	
						}


						text.setText((autoAddShowTime)/60+" : "+(autoAddShowTime)%60+"/"+(totalPlayTime)/60+" : "+(totalPlayTime)%60);//在文本框中显示剩余时间
						progressBar.setSelection((int)((autoAddprogressBarTime+0.00)/totalPlayTime*100));//进度条递增
						if(autoAddShowTime==0){//如果歌曲播放时间为0时
							progressBar.setSelection(0);
							text.setText("00:00/00:00");
							player.stop();
							autoPlay();
						}
					}
				});
			}
		}
	}

	/**
	 * 时间转换
	 * @param time
	 * @return
	 */
	public String getTime(int time){
		String temp="";
		if(time%2<10){
			temp=time/60+":0"+time%2;
		}else{
			temp=time/60+":"+time%2;
		}
		if(time<60){
			if(time<10){
				temp="0:0"+time;
			}else{
				temp="0:"+time;
			}
		}else{
			if(time%60<10){
				temp=time/60+":0"+time%60;
			}else{
				temp=time/60+":"+time%60;
			}
		}
		return temp;
	}

	/**
	 * 销毁会进程
	 * @author Administrator
	 * 
	 */
	public void distory(){//销毁会进程
		Timer timer=new Timer();
		timer.schedule(new Time(), 0, 1*1000);
	}
	/**
	 * 系统退出延时器
	 * @author Administrator
	 *
	 */
	class Time extends TimerTask{//系统退出延时器
		Toolkit toolkit = Toolkit.getDefaultToolkit();
		int time=1;
		public void run() {
			if(time>0){
				//toolkit.beep();
				time--;
			}else{				
				System.exit(0);
			}
		}
	}


	private void setRegistry(String path) throws BackingStoreException{
		Preferences preferences=Preferences.userNodeForPackage(MusicPlayerTest.class);
		if(preferences.get("MusicPlayer", null)==null){
			preferences.put("MusicPlayer", path);
		}else if(preferences.get("MusicPlayer", null).equals(path)){
			
		}else{
			preferences.remove("MusicPlayer");			
		}
		preferences.flush();
	}
	
	private void getRegistry(){
		Preferences preferences=Preferences.userNodeForPackage(MusicPlayerTest.class);
		if(preferences.get("MusicPlayer", null)!=null){
			String mu=preferences.get("MusicPlayer", null).toString();
//			System.out.println(mu);
			//musicPlayerStart(mu);
		}
	}

}

