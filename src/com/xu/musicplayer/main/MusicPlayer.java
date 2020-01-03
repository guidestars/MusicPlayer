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
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

@SuppressWarnings(value = "all")
public class MusicPlayer {

    public static boolean playing = true;// 播放按钮
    public static String PLAYING_SONG = "";// 正在播放歌曲
    public static List<Color> COLORS = new ArrayList<Color>();
    private static Player player = new XMusic();
    private static int resize = 0;
    protected Shell shell;
    private Display display;
    private Tray tray;// 播放器托盘
    private Table table;
    private Table table_1;
    private Label text;
    private Label text_1;
    private Label label_3;
    private ControllerServer server = new ControllerServer(); // 歌词及频谱
    private ProgressBar progressBar; // 进度条
    private Composite composite_3; // 频谱面板
    private boolean click = false;//界面移动
    private int clickX, clickY;//界面移动
    private boolean choise = true;// 双击播放
    private int length;// 播放时长

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
        shell.setSize(890, 486);
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

        Composite composite_1 = new Composite(sashForm, SWT.NONE);
        composite_1.setBackgroundMode(SWT.INHERIT_FORCE);

        Label label = new Label(composite_1, SWT.NONE);
        label.setImage(SWTResourceManager.getImage(MusicPlayer.class, "/com/xu/musicplayer/image/exit-1.png"));
        label.setBounds(845, 10, 32, 32);

        Label label_1 = new Label(composite_1, SWT.NONE);
        label_1.setImage(SWTResourceManager.getImage(MusicPlayer.class, "/com/xu/musicplayer/image/mini-1.png"));
        label_1.setBounds(798, 10, 32, 32);

        Combo combo = new Combo(composite_1, SWT.NONE);
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
                for (int i = 0; i < Constant.PLAY_LIST.size(); i++) {
                    if (Constant.PLAY_LIST.get(i).contains(combo.getText())) {
                        combo.add(Constant.PLAY_LIST.get(i).split(Constant.SPLIT)[1]);
                    }
                }
                combo.setListVisible(true);
            }
        });
        combo.setBounds(283, 21, 330, 25);
        combo.setVisible(false);

        Composite composite_2 = new Composite(sashForm, SWT.NONE);
        composite_2.setBackgroundMode(SWT.INHERIT_FORCE);
        composite_2.setLayout(new FillLayout(SWT.HORIZONTAL));

        SashForm sashForm_1 = new SashForm(composite_2, SWT.NONE);

        Composite composite_4 = new Composite(sashForm_1, SWT.NONE);
        composite_4.setBackgroundMode(SWT.INHERIT_FORCE);
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
        composite_5.setBackgroundMode(SWT.INHERIT_FORCE);
        composite_5.setLayout(new FillLayout(SWT.HORIZONTAL));

        table_1 = new Table(composite_5, SWT.FULL_SELECTION);

        TableColumn tableColumn_2 = new TableColumn(table_1, SWT.CENTER);
        tableColumn_2.setText("歌词");

        TableColumn tableColumn_3 = new TableColumn(table_1, SWT.CENTER);
        tableColumn_3.setWidth(728);
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

        composite_3 = new Composite(sashForm, SWT.NONE);
        composite_3.setBackgroundMode(SWT.INHERIT_FORCE);

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

        sashForm.setWeights(new int[]{1, 5, 1});
        sashForm_1.setWeights(new int[]{156, 728});

        //界面移动
        composite_1.addMouseListener(new MouseAdapter() {
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
        composite_1.addMouseMoveListener(new MouseMoveListener() {
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
                //tray.dispose();
                //distory();
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
                    TableItem[] items = table.getSelection();
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

        composite_3.addControlListener(new ControlAdapter() {
            @Override
            public void controlResized(ControlEvent e) {
                if (resize > 0) {
                    Constant.SPECTRUM_HEIGHT = composite_3.getClientArea().height;
                    Constant.SPECTRUM_WIDTH = composite_3.getClientArea().width;
                    Constant.SPECTRUM_NUMBER = composite_3.getClientArea().width / 5;
                }
                sashForm.setWeights(new int[]{1, 5, 1});
                sashForm_1.setWeights(new int[]{156, 728});
                resize++;
            }
        });

        composite_3.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseDoubleClick(MouseEvent e) {
                Color color = COLORS.get(new Random().nextInt(COLORS.size()));
                if (color != Constant.SPECTRUM_BACKGROUND_COLOR) {
                    Constant.SPECTRUM_COLOR = color;
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

        initMusicPlayer(shell, table);
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
        if (Constant.PLAY_LIST == null || Constant.PLAY_LIST.size() <= 0) {
            Toolkit.getDefaultToolkit().beep();
            choice.open_choise_windows(shell);
        }
        updatePlayerSongLists(Constant.PLAY_LIST, table);
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
            item.setText(new String[]{"" + (i + 1), lists.get(i).split(Constant.SPLIT)[1]});
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
        Constant.PLAY_INDEX = index == -1 ? Constant.PLAY_INDEX : index;
        System.out.println(Constant.PLAY_INDEX + "\t" + Constant.PLAY_LIST.get(Constant.PLAY_INDEX));
        PLAYING_SONG = Constant.PLAY_LIST.get(Constant.PLAY_INDEX);
        player.load(PLAYING_SONG.split(Constant.SPLIT)[0]);
        try {
            player.start();
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        Constant.PLAY_STATE = true;
        updatePlayerSongListsColor(table, Constant.PLAY_INDEX);
        if (mode == 0) {//上一曲
            if (Constant.PLAY_INDEX <= 0) {
                Constant.PLAY_INDEX = Constant.PLAY_LIST.size();
            } else {
                Constant.PLAY_INDEX--;
            }
        } else {//下一曲
            if (Constant.PLAY_INDEX >= Constant.PLAY_LIST.size()) {
                Constant.PLAY_INDEX = 0;
            } else {
                Constant.PLAY_INDEX++;
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
        Constant.HAVE_LYRIC = false;
        label_3.setImage(SWTResourceManager.getImage(MusicPlayer.class, "/com/xu/musicplayer/image/start.png"));
        length = Integer.parseInt(PLAYING_SONG.split(Constant.SPLIT)[3]);
        text.setText(((int) length / 60 + ":" + length % 60) + "");
        TableItem[] items = table.getItems();
        for (int i = 0; i < items.length; i++) {
            if (index == i) {
                items[i].setBackground(SWTResourceManager.getColor(SWT.COLOR_GRAY));
            } else {
                items[i].setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
            }
        }
        if (Constant.PLAY_LIST.get(Constant.PLAY_INDEX).split(Constant.SPLIT)[4].equalsIgnoreCase("Y")) {
            Constant.HAVE_LYRIC = true;
            LoadLocalLyric lyric = new LoadLocalLyric();
            String path = Constant.PLAY_LIST.get(Constant.PLAY_INDEX).split(Constant.SPLIT)[0];
            path = path.substring(0, path.lastIndexOf(".")) + ".lrc";
            lyric.lyric(path);
            table_1.removeAll();
            if (Constant.PLAY_LYRIC != null && Constant.PLAY_LYRIC.size() > 0) {
                TableItem item;
                for (int i = 0, len = Constant.PLAY_LYRIC.size() + 8; i < len; i++) {
                    item = new TableItem(table_1, SWT.NONE);
                    if (i < len - 8) {
                        item.setText(new String[]{"", Constant.PLAY_LYRIC.get(i).split(Constant.SPLIT)[1]});
                    }
                }
                PlayerEntity.setBar(progressBar);
                PlayerEntity.setText(text_1);
                PlayerEntity.setSong(PLAYING_SONG);
                PlayerEntity.setTable(table_1);
            }
            PlayerEntity.setSpectrum(composite_3);
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
        Constant.PLAY_STATE = false;
        label_3.setImage(SWTResourceManager.getImage(MusicPlayer.class, "/com/xu/musicplayer/image/stop.png"));
        updatePlayerSongListsColor(table, Constant.PLAY_INDEX);
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
    
}
