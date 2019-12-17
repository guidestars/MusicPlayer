package com.xu.musicplayer.download;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Label;
import org.eclipse.wb.swt.SWTResourceManager;

import com.xu.musicplayer.threadpool.ThreadPoolManager;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

public class DownLoad {

	protected Shell shell;
	private Text text;

	private static long length;
	private long total_length;

	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			DownLoad window = new DownLoad();
			window.open();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Open the window.
	 */
	public void open() {
		Display display = Display.getDefault();
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
		shell = new Shell();
		shell.setSize(581, 323);
		shell.setText("迅雷下载");

		Label label = new Label(shell, SWT.NONE);
		label.setFont(SWTResourceManager.getFont("Microsoft YaHei UI", 12, SWT.NORMAL));
		label.setBounds(35, 38, 46, 23);
		label.setText("地址");

		text = new Text(shell, SWT.BORDER);
		text.setText("http://down.360safe.com/yunpan/360wangpan_setup_6.6.0.1307.exe");
		text.setBounds(97, 38, 423, 23);

		Label label_1 = new Label(shell, SWT.NONE);
		label_1.setText("线程");
		label_1.setFont(SWTResourceManager.getFont("Microsoft YaHei UI", 12, SWT.NORMAL));
		label_1.setBounds(35, 86, 46, 23);

		final Combo combo = new Combo(shell, SWT.READ_ONLY);
		combo.setItems(new String[] {"5", "10", "15", "20"});
		combo.setBounds(98, 86, 104, 25);

		Button btnNewButton = new Button(shell, SWT.NONE);
		btnNewButton.setBounds(440, 86, 80, 27);
		btnNewButton.setText("开    始");

		final ProgressBar progressBar = new ProgressBar(shell, SWT.NONE);
		progressBar.setForeground(SWTResourceManager.getColor(SWT.COLOR_GREEN));
		progressBar.setBounds(35, 190, 485, 17);

		//开始下载
		btnNewButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				String url=text.getText().trim();
				String threadNumber=combo.getText().trim();
				if(threadNumber==null || "".equalsIgnoreCase(threadNumber)){
					threadNumber="5";
				}

				ThreadPoolManager tpm=new ThreadPoolManager(5);
				try {
					total_length=new DownLoadManager().getLength(new URL(url));
				} catch (MalformedURLException e2) {
					e2.printStackTrace();
				} catch (IOException e2) {
					e2.printStackTrace();
				}
				System.out.println("-----"+total_length);
				try {
					new DownLoadManager(new URL(url),Integer.parseInt(threadNumber),tpm,new Notify() {
						public synchronized void notifyResult(Object object) {
							long size=Long.parseLong(object.toString());
							length+=size;
							System.out.println("已下载:"+length);
							Display.getDefault().asyncExec(new Runnable() {
								public void run() {
									progressBar.setSelection( (int)(length/(double)total_length*100) );
								}
							} );
						}
					}).startDownLoad();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});

	}
}
