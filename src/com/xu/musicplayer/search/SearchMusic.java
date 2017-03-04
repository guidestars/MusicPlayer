package com.xu.musicplayer.search;

import java.io.File;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.wb.swt.SWTResourceManager;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

public class SearchMusic {

	protected Shell shell;
	private Display display;
	private Text text_1;

	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			SearchMusic window = new SearchMusic();
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
		shell = new Shell(SWT.MIN);
		shell.setBackground(SWTResourceManager.getColor(SWT.COLOR_CYAN));
		shell.setImage(SWTResourceManager.getImage(SearchMusic.class, "/com/xu/musicplayer/image/music.png"));
		shell.setSize(691, 438);
		shell.setText("扫 描 歌 曲 文 件");
		shell.setLocation((display.getClientArea().width - shell.getSize().x)/2, 
				(display.getClientArea().height - shell.getSize().y)/2);

		Button btnNewButton = new Button(shell, SWT.NONE);
		btnNewButton.setBounds(10, 361, 130, 27);
		btnNewButton.setText("全   盘   扫   描");

		Button button = new Button(shell, SWT.NONE);
		button.setText("完   成   扫   描");
		button.setBounds(545, 361, 130, 27);

		text_1 = new Text(shell, SWT.BORDER | SWT.READ_ONLY | SWT.H_SCROLL | SWT.V_SCROLL | SWT.CANCEL | SWT.MULTI);
		text_1.setBackground(SWTResourceManager.getColor(SWT.COLOR_CYAN));
		text_1.setFont(SWTResourceManager.getFont("Microsoft YaHei UI", 12, SWT.NORMAL));
		text_1.setBounds(0, 0, 685, 325);


		//全   盘   扫   描
		btnNewButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				search(new File("G://"));
			}
		});

		//完   成   扫   描
		button.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				System.exit(0);
				System.out.println("kkk");
			}
		});

	}

	public void search(File root){
		File[] files=root.listFiles();
		if(files!=null && files.length>0){
			for(final File file:files){
				if(file.isDirectory()){
					search(file.getAbsoluteFile());
				}else{					
					if(file.toString().toString().endsWith("mp3")){
						text_1.append(file.toString());
						text_1.append("\r\n");
					}	
				}
			}
		}else{
			return;
		}
	}
	
}
