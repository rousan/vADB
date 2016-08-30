package in.byter.vadb.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GraphicsConfiguration;
import java.awt.HeadlessException;
import java.awt.Point;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Vector;
import java.util.regex.Pattern;

import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.LookAndFeel;
import javax.swing.UIManager;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.sun.corba.se.impl.oa.poa.POACurrent;

import in.byter.vadb.run.Run;
import in.byter.vadb.utils.Utils;

public class LauncherFrame extends JFrame {

	public boolean FRAME_IS_MAXIMUM = true;
	public int WRONG_FRAME_POS_X_Y_VAL = -2222;
	public Point FRAME_PREVIOUS_POINT = new Point(WRONG_FRAME_POS_X_Y_VAL, WRONG_FRAME_POS_X_Y_VAL);

	public JLabel minimize_frame_lbl = new JLabel("-");
	public JLabel close_frame_lbl = new JLabel("x");
	public JLabel small_frame_lbl = new JLabel("--");

	public JLabel device_tab_lbl = new JLabel("Device");
	public JLabel run_tab_lbl = new JLabel("Run");
	public JLabel logs_tab_lbl = new JLabel("Logs");
	public JLabel abort_tab_lbl = new JLabel("Abort");
	public JLabel settings_tab_lbl = new JLabel("Settings");
	public JLabel help_tab_lbl = new JLabel("Help & About");

	public JCheckBox always_on_top_check_box = new JCheckBox();
	public JLabel check_update_btn = new JLabel("Check");
	public JLabel update_found_lbl = new JLabel("New version found");
	public JLabel updated_app_version_name_lbl = new JLabel("Version  :  vADB-2.0.1");
	public JLabel updated_app_size_lbl = new JLabel("Size  :  299KB");

	public JPanel content_holder = new JPanel(new BorderLayout());
	public Box run_tab_ui_holder = Box.createVerticalBox();
	public Box device_tab_ui_holder = Box.createVerticalBox();
	public Box logs_tab_ui_holder = Box.createVerticalBox();
	public Box help_tab_ui_holder = Box.createVerticalBox();
	public Box settings_tab_ui_holder = Box.createVerticalBox();
	public Box abort_tab_ui_holder = Box.createVerticalBox();

	public JTextField ip_input = new JTextField(50);
	public JTextField port_input = new JTextField(50);
	public JTextField file_input = new JTextField(42);
	public JTextField logs_filter_input = new JTextField(47);

	public JLabel run_btn = new JLabel("Run");
	public JLabel save_device_info_btn = new JLabel("Save");
	public JLabel file_chooser_btn = new JLabel("...");
	public JLabel get_logs_btn = new JLabel("Get Logs");
	public JLabel clear_logs_btn = new JLabel("Clear Logs");
	public JLabel abort_btn = new JLabel("Abort");
	public JLabel clear_outs_btn = new JLabel("Clear");

	public Box small_frame_content_holder = Box.createHorizontalBox();
	public JLabel small_frame_run_btn = new JLabel("Run");
	public JLabel small_frame_get_logs_btn = new JLabel("Logs");

	public JTextArea logs_viewer_area = new JTextArea(20, 20);

	public LauncherFrame() throws HeadlessException {
	}

	public LauncherFrame(GraphicsConfiguration gc) {
		super(gc);
		init();
	}

	public LauncherFrame(String title) throws HeadlessException {
		super(title);
		init();
	}

	public LauncherFrame(String title, GraphicsConfiguration gc) {
		super(title, gc);
		init();
	}

	public void init() {
		try {
			minimize_frame_lbl.setForeground(Color.WHITE);
			close_frame_lbl.setForeground(Color.white);
			small_frame_lbl.setForeground(Color.white);
			minimize_frame_lbl.setFont(Utils.getParagraphFont(10));
			close_frame_lbl.setFont(Utils.getParagraphFont(10));
			small_frame_lbl.setFont(Utils.getParagraphFont(10));
			// minimize_frame_lbl.setCursor(new Cursor(Cursor.HAND_CURSOR));
			// close_frame_lbl.setCursor(new Cursor(Cursor.HAND_CURSOR));
			// small_frame_lbl.setCursor(new Cursor(Cursor.HAND_CURSOR));
			minimize_frame_lbl.setBorder(new EmptyBorder(3, 10, 3, 10));
			close_frame_lbl.setBorder(new EmptyBorder(3, 10, 3, 10));
			small_frame_lbl.setBorder(new EmptyBorder(3, 10, 3, 10));
			minimize_frame_lbl.setToolTipText("Minimize");
			small_frame_lbl.setToolTipText("Small Frame");
			close_frame_lbl.setToolTipText("CLose Frame");
			JPanel minimize_holder = new JPanel(new BorderLayout());
			JPanel close_holder = new JPanel(new BorderLayout());
			JPanel small_holder = new JPanel(new BorderLayout());
			minimize_holder.setBackground(null);
			close_holder.setBackground(null);
			small_holder.setBackground(null);
			minimize_holder.add(minimize_frame_lbl);
			close_holder.add(close_frame_lbl);
			small_holder.add(small_frame_lbl);
			minimize_holder.setBorder(new LineBorder(Utils.TOP_PANEL_BG_COLOR, 1));
			close_holder.setBorder(new LineBorder(Utils.TOP_PANEL_BG_COLOR, 1));
			small_holder.setBorder(new LineBorder(Utils.TOP_PANEL_BG_COLOR, 1));
			close_frame_lbl.setFocusable(true);
			minimize_frame_lbl.setFocusable(true);
			small_frame_lbl.setFocusable(true);

			minimize_frame_lbl.addMouseListener(new MouseAdapter() {
				public void mouseEntered(MouseEvent evt) {
					minimize_holder.setBorder(new LineBorder(Utils.HOVER_BORDER_COLOR, 1));
				}

				public void mouseExited(MouseEvent evt) {
					minimize_holder.setBorder(new LineBorder(Utils.TOP_PANEL_BG_COLOR, 1));
				}

				public void mouseClicked(MouseEvent evt) {
					setState(JFrame.ICONIFIED);
				}
			});
			minimize_frame_lbl.addFocusListener(new FocusListener() {

				@Override
				public void focusLost(FocusEvent arg0) {
					minimize_holder.setBorder(new LineBorder(Utils.TOP_PANEL_BG_COLOR, 1));
				}

				@Override
				public void focusGained(FocusEvent arg0) {
					minimize_holder.setBorder(new LineBorder(Utils.HOVER_BORDER_COLOR, 1));
				}
			});
			Utils.addEnterKeyListenerForClickEvent(minimize_frame_lbl);

			close_frame_lbl.addMouseListener(new MouseAdapter() {
				public void mouseEntered(MouseEvent evt) {
					close_holder.setBorder(new LineBorder(Utils.HOVER_BORDER_COLOR, 1));
				}

				public void mouseExited(MouseEvent evt) {
					close_holder.setBorder(new LineBorder(Utils.TOP_PANEL_BG_COLOR, 1));
				}

				public void mouseClicked(MouseEvent evt) {
					int res = AlertDialog.showConfirmDialog(LauncherFrame.this,
							"Do you want to exit, all data transactions will be cancelled ?");
					if (res == 1) {
						Run.callbackBeforeAppClosing();
						System.exit(0);
					}
				}
			});
			close_frame_lbl.addFocusListener(new FocusListener() {

				@Override
				public void focusLost(FocusEvent arg0) {
					close_holder.setBorder(new LineBorder(Utils.TOP_PANEL_BG_COLOR, 1));
				}

				@Override
				public void focusGained(FocusEvent arg0) {
					close_holder.setBorder(new LineBorder(Utils.HOVER_BORDER_COLOR, 1));
				}
			});
			Utils.addEnterKeyListenerForClickEvent(close_frame_lbl);

			small_frame_lbl.addMouseListener(new MouseAdapter() {
				public void mouseEntered(MouseEvent evt) {
					small_holder.setBorder(new LineBorder(Utils.HOVER_BORDER_COLOR, 1));
				}

				public void mouseExited(MouseEvent evt) {
					small_holder.setBorder(new LineBorder(Utils.TOP_PANEL_BG_COLOR, 1));
				}

				public void mouseClicked(MouseEvent evt) {
					//
				}
			});
			small_frame_lbl.addFocusListener(new FocusListener() {

				@Override
				public void focusLost(FocusEvent arg0) {
					small_holder.setBorder(new LineBorder(Utils.TOP_PANEL_BG_COLOR, 1));
				}

				@Override
				public void focusGained(FocusEvent arg0) {
					small_holder.setBorder(new LineBorder(Utils.HOVER_BORDER_COLOR, 1));
				}
			});

			Container content_pane = this.getContentPane();
			JPanel root = new JPanel(new BorderLayout());
			JPanel top_panel = new JPanel(new BorderLayout());
			JPanel main_panel = new JPanel(new BorderLayout());

			top_panel.setBorder(new EmptyBorder(5, 5, 5, 5));
			JPanel pnl1 = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
			JPanel pnl2 = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
			pnl1.setBackground(null);
			pnl1.setBorder(new EmptyBorder(3, 7, 3, 7));
			try {
				JLabel icon_holder = new JLabel();
				icon_holder.setIcon(new ImageIcon(
						getClass().getClassLoader().getResource("in/byter/vadb/assets/app_icon_24_24.png")));
				icon_holder.setPreferredSize(new Dimension(20, 20));
				pnl1.add(icon_holder);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			JLabel app_title = new JLabel("vADB");
			app_title.setBorder(new EmptyBorder(0, 5, 0, 10));
			app_title.setForeground(Utils.THEME_PARAGRAPH_COLOR);
			app_title.setFont(Utils.getAppTitleFont(15));
			pnl1.add(app_title);
			pnl2.setBackground(null);
			pnl2.add(small_holder);
			pnl2.add(minimize_holder);
			pnl2.add(close_holder);
			top_panel.add(pnl1, BorderLayout.CENTER);
			top_panel.add(pnl2, BorderLayout.EAST);

			JPanel tabs_holder = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
			tabs_holder.setBackground(Utils.TOP_PANEL_BG_COLOR);
			device_tab_lbl.setBackground(Utils.THEME_BG_COLOR);
			device_tab_lbl.setOpaque(true);
			device_tab_lbl.setForeground(Utils.THEME_PARAGRAPH_COLOR);
			device_tab_lbl.setFont(Utils.getParagraphFont(12));
			device_tab_lbl.setBorder(new EmptyBorder(10, 20, 10, 20));
			run_tab_lbl.setBackground(Utils.THEME_BG_COLOR);
			run_tab_lbl.setOpaque(true);
			run_tab_lbl.setForeground(Utils.THEME_PARAGRAPH_COLOR);
			run_tab_lbl.setFont(Utils.getParagraphFont(12));
			run_tab_lbl.setBorder(new EmptyBorder(10, 20, 10, 20));
			logs_tab_lbl.setBackground(null);
			logs_tab_lbl.setOpaque(true);
			logs_tab_lbl.setForeground(Utils.THEME_PARAGRAPH_COLOR);
			logs_tab_lbl.setFont(Utils.getParagraphFont(12));
			logs_tab_lbl.setBorder(new EmptyBorder(10, 20, 10, 20));
			abort_tab_lbl.setBackground(null);
			abort_tab_lbl.setOpaque(true);
			abort_tab_lbl.setForeground(Utils.THEME_PARAGRAPH_COLOR);
			abort_tab_lbl.setFont(Utils.getParagraphFont(12));
			abort_tab_lbl.setBorder(new EmptyBorder(10, 20, 10, 20));
			settings_tab_lbl.setBackground(null);
			settings_tab_lbl.setOpaque(true);
			settings_tab_lbl.setForeground(Utils.THEME_PARAGRAPH_COLOR);
			settings_tab_lbl.setFont(Utils.getParagraphFont(12));
			settings_tab_lbl.setBorder(new EmptyBorder(10, 20, 10, 20));
			help_tab_lbl.setBackground(null);
			help_tab_lbl.setOpaque(true);
			help_tab_lbl.setForeground(Utils.THEME_PARAGRAPH_COLOR);
			help_tab_lbl.setFont(Utils.getParagraphFont(12));
			help_tab_lbl.setBorder(new EmptyBorder(10, 20, 10, 20));
			tabs_holder.add(device_tab_lbl);
			tabs_holder.add(run_tab_lbl);
			tabs_holder.add(logs_tab_lbl);
			tabs_holder.add(abort_tab_lbl);
			tabs_holder.add(settings_tab_lbl);
			tabs_holder.add(help_tab_lbl);

			device_tab_ui_holder.setBorder(new EmptyBorder(30, 30, 30, 30));
			Box box0_device = Box.createHorizontalBox();
			Box box1_device = Box.createHorizontalBox();
			Box box2_device = Box.createHorizontalBox();
			Box box3_device = Box.createHorizontalBox();

			JLabel desc_devvice = new JLabel("Enter Android Device Info   :");
			desc_devvice.setForeground(Utils.THEME_PARAGRAPH_COLOR);
			desc_devvice.setFont(Utils.getParagraphFont(13));
			box0_device.add(desc_devvice);
			box0_device.add(Box.createGlue());

			JLabel ip_lbl = new JLabel("IP           :");
			ip_lbl.setForeground(Utils.THEME_PARAGRAPH_COLOR);
			ip_lbl.setFont(Utils.getParagraphFont(11));
			box1_device.add(Box.createHorizontalStrut(10));
			box1_device.add(ip_lbl);
			box1_device.add(Box.createHorizontalStrut(11));
			box1_device.add(ip_input);
			ip_input.setBackground(Utils.INPUTS_BG_COLOR);
			ip_input.setForeground(Utils.INPUTS_TXT_COLOR);
			ip_input.setFont(Utils.getParagraphFont(Utils.INPUTS_FONT_SIZE));

			JLabel port_lbl = new JLabel("Port       :");
			port_lbl.setForeground(Utils.THEME_PARAGRAPH_COLOR);
			port_lbl.setFont(Utils.getParagraphFont(11));
			box2_device.add(Box.createHorizontalStrut(10));
			box2_device.add(port_lbl);
			box2_device.add(Box.createHorizontalStrut(9));
			box2_device.add(port_input);
			port_input.setBackground(Utils.INPUTS_BG_COLOR);
			port_input.setForeground(Utils.INPUTS_TXT_COLOR);
			port_input.setFont(Utils.getParagraphFont(Utils.INPUTS_FONT_SIZE));

			ip_input.setBorder(
					new CompoundBorder(new LineBorder(Utils.THEME_BG_COLOR, 1), new EmptyBorder(5, 5, 5, 5)));
			port_input.setBorder(
					new CompoundBorder(new LineBorder(Utils.THEME_BG_COLOR, 1), new EmptyBorder(5, 5, 5, 5)));

			ip_input.addFocusListener(new FocusListener() {

				@Override
				public void focusLost(FocusEvent arg0) {
					ip_input.setBorder(
							new CompoundBorder(new LineBorder(Utils.THEME_BG_COLOR, 1), new EmptyBorder(5, 5, 5, 5)));
				}

				@Override
				public void focusGained(FocusEvent arg0) {
					ip_input.setBorder(new CompoundBorder(new LineBorder(Utils.HOVER_BORDER_COLOR, 1),
							new EmptyBorder(5, 5, 5, 5)));
				}
			});

			port_input.addFocusListener(new FocusListener() {

				@Override
				public void focusLost(FocusEvent arg0) {
					port_input.setBorder(
							new CompoundBorder(new LineBorder(Utils.THEME_BG_COLOR, 1), new EmptyBorder(5, 5, 5, 5)));
				}

				@Override
				public void focusGained(FocusEvent arg0) {
					port_input.setBorder(new CompoundBorder(new LineBorder(Utils.HOVER_BORDER_COLOR, 1),
							new EmptyBorder(5, 5, 5, 5)));
				}
			});

			save_device_info_btn.setForeground(Utils.THEME_PARAGRAPH_COLOR);
			save_device_info_btn.setFont(Utils.getParagraphFont(12));
			save_device_info_btn.setBorder(
					new CompoundBorder(new LineBorder(new Color(0x6d556b), 1), new EmptyBorder(5, 20, 5, 20)));
			box3_device.add(Box.createGlue());
			box3_device.add(save_device_info_btn);
			save_device_info_btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
			save_device_info_btn.addMouseListener(new MouseAdapter() {
				public void mouseEntered(MouseEvent evt) {
					save_device_info_btn.setBorder(new CompoundBorder(new LineBorder(Utils.HOVER_BORDER_COLOR, 1, true),
							new EmptyBorder(5, 20, 5, 20)));
				}

				public void mouseExited(MouseEvent evt) {
					save_device_info_btn.setBorder(
							new CompoundBorder(new LineBorder(new Color(0x6d556b), 1), new EmptyBorder(5, 20, 5, 20)));
				}

				public void mouseClicked(MouseEvent evt) {
					save_device_info_btn.requestFocus();
				}
			});
			save_device_info_btn.setFocusable(true);
			save_device_info_btn.addFocusListener(new FocusListener() {

				@Override
				public void focusLost(FocusEvent arg0) {
					save_device_info_btn.setBorder(
							new CompoundBorder(new LineBorder(new Color(0x6d556b), 1), new EmptyBorder(5, 20, 5, 20)));
				}

				@Override
				public void focusGained(FocusEvent arg0) {
					save_device_info_btn.setBorder(new CompoundBorder(new LineBorder(Utils.HOVER_BORDER_COLOR, 1, true),
							new EmptyBorder(5, 20, 5, 20)));
				}
			});

			device_tab_ui_holder.add(box0_device);
			device_tab_ui_holder.add(Box.createVerticalStrut(15));
			device_tab_ui_holder.add(box1_device);
			device_tab_ui_holder.add(Box.createVerticalStrut(5));
			device_tab_ui_holder.add(box2_device);
			device_tab_ui_holder.add(Box.createVerticalStrut(10));
			device_tab_ui_holder.add(box3_device);
			device_tab_ui_holder.setBackground(null);

			run_tab_ui_holder.setBorder(new EmptyBorder(30, 30, 30, 30));
			Box box0_run = Box.createHorizontalBox();
			Box box1_run = Box.createHorizontalBox();
			Box box2_run = Box.createHorizontalBox();

			JLabel desc_run = new JLabel("Enter Apk File path to run   :");
			desc_run.setForeground(Utils.THEME_PARAGRAPH_COLOR);
			desc_run.setFont(Utils.getParagraphFont(13));
			box0_run.add(desc_run);
			box0_run.add(Box.createGlue());

			file_chooser_btn.setForeground(Utils.THEME_PARAGRAPH_COLOR);
			file_chooser_btn.setFont(Utils.getParagraphFont(12));
			file_chooser_btn.setBorder(
					new CompoundBorder(new LineBorder(new Color(0x6d556b), 1), new EmptyBorder(4, 10, 4, 10)));
			file_chooser_btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
			file_chooser_btn.addMouseListener(new MouseAdapter() {
				public void mouseEntered(MouseEvent evt) {
					file_chooser_btn.setBorder(new CompoundBorder(new LineBorder(Utils.HOVER_BORDER_COLOR, 1, true),
							new EmptyBorder(4, 10, 4, 10)));
				}

				public void mouseExited(MouseEvent evt) {
					file_chooser_btn.setBorder(
							new CompoundBorder(new LineBorder(new Color(0x6d556b), 1), new EmptyBorder(4, 10, 4, 10)));
				}

				public void mouseClicked(MouseEvent evt) {
					file_chooser_btn.requestFocus();
					try {

						JFileChooser jfc = new JFileChooser(System.getProperty("user.home"));
						jfc.setDialogTitle("Apk File Chooser");
						jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);
						FileNameExtensionFilter fnef = new FileNameExtensionFilter("Apk File", "apk");
						for (FileFilter ff : jfc.getChoosableFileFilters()) {
							jfc.removeChoosableFileFilter(ff);
						}
						jfc.addChoosableFileFilter(fnef);
						jfc.setFileFilter(fnef);
						int result = jfc.showOpenDialog(LauncherFrame.this);
						String outs = "";
						if (result == JFileChooser.APPROVE_OPTION) {
							outs = jfc.getSelectedFile().getAbsolutePath();
						} else {
							outs = "";
						}

						if (!outs.trim().isEmpty()) {
							file_input.setText(outs.trim());
						}

					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
			Utils.addEnterKeyListenerForClickEvent(file_chooser_btn);
			file_chooser_btn.setFocusable(true);
			file_chooser_btn.addFocusListener(new FocusListener() {

				@Override
				public void focusLost(FocusEvent arg0) {
					file_chooser_btn.setBorder(
							new CompoundBorder(new LineBorder(new Color(0x6d556b), 1), new EmptyBorder(4, 10, 4, 10)));
				}

				@Override
				public void focusGained(FocusEvent arg0) {
					file_chooser_btn.setBorder(new CompoundBorder(new LineBorder(Utils.HOVER_BORDER_COLOR, 1, true),
							new EmptyBorder(4, 10, 4, 10)));
				}
			});
			JLabel file_lbl = new JLabel("APK File Path     :");
			file_lbl.setForeground(Utils.THEME_PARAGRAPH_COLOR);
			file_lbl.setFont(Utils.getParagraphFont(11));
			box1_run.add(Box.createHorizontalStrut(10));
			box1_run.add(file_lbl);
			box1_run.add(Box.createHorizontalStrut(15));
			box1_run.add(file_input);
			box1_run.add(Box.createHorizontalStrut(5));
			box1_run.add(file_chooser_btn);
			file_input.setBackground(Utils.INPUTS_BG_COLOR);
			file_input.setForeground(Utils.INPUTS_TXT_COLOR);
			file_input.setFont(Utils.getParagraphFont(Utils.INPUTS_FONT_SIZE));
			file_input.setBorder(
					new CompoundBorder(new LineBorder(Utils.THEME_BG_COLOR, 1), new EmptyBorder(5, 5, 5, 5)));

			file_input.addFocusListener(new FocusListener() {

				@Override
				public void focusLost(FocusEvent arg0) {
					file_input.setBorder(
							new CompoundBorder(new LineBorder(Utils.THEME_BG_COLOR, 1), new EmptyBorder(5, 5, 5, 5)));
				}

				@Override
				public void focusGained(FocusEvent arg0) {
					file_input.setBorder(new CompoundBorder(new LineBorder(Utils.HOVER_BORDER_COLOR, 1),
							new EmptyBorder(5, 5, 5, 5)));
				}
			});

			run_btn.setForeground(Utils.THEME_PARAGRAPH_COLOR);
			run_btn.setFont(Utils.getParagraphFont(12));
			run_btn.setBorder(
					new CompoundBorder(new LineBorder(new Color(0x6d556b), 1), new EmptyBorder(5, 20, 5, 20)));
			box2_run.add(Box.createGlue());
			box2_run.add(run_btn);
			run_btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
			run_btn.addMouseListener(new MouseAdapter() {
				public void mouseEntered(MouseEvent evt) {
					run_btn.setBorder(new CompoundBorder(new LineBorder(Utils.HOVER_BORDER_COLOR, 1, true),
							new EmptyBorder(5, 20, 5, 20)));
				}

				public void mouseExited(MouseEvent evt) {
					run_btn.setBorder(
							new CompoundBorder(new LineBorder(new Color(0x6d556b), 1), new EmptyBorder(5, 20, 5, 20)));
				}

				public void mouseClicked(MouseEvent evt) {
					run_btn.requestFocus();
				}
			});
			run_btn.setFocusable(true);
			run_btn.addFocusListener(new FocusListener() {

				@Override
				public void focusLost(FocusEvent arg0) {
					run_btn.setBorder(
							new CompoundBorder(new LineBorder(new Color(0x6d556b), 1), new EmptyBorder(5, 20, 5, 20)));
				}

				@Override
				public void focusGained(FocusEvent arg0) {
					run_btn.setBorder(new CompoundBorder(new LineBorder(Utils.HOVER_BORDER_COLOR, 1, true),
							new EmptyBorder(5, 20, 5, 20)));
				}
			});

			run_tab_ui_holder.add(box0_run);
			run_tab_ui_holder.add(Box.createVerticalStrut(15));
			run_tab_ui_holder.add(box1_run);
			run_tab_ui_holder.add(Box.createVerticalStrut(15));
			run_tab_ui_holder.add(box2_run);

			logs_tab_ui_holder.setBorder(new EmptyBorder(30, 30, 30, 30));
			Box box0_logs = Box.createHorizontalBox();
			Box box1_logs = Box.createHorizontalBox();
			Box box2_logs = Box.createHorizontalBox();
			Box box3_logs = Box.createHorizontalBox();
			Box box4_logs = Box.createHorizontalBox();

			JLabel desc_logs = new JLabel("Enter app name or app package name   :");
			desc_logs.setForeground(Utils.THEME_PARAGRAPH_COLOR);
			desc_logs.setFont(Utils.getParagraphFont(13));
			box0_logs.add(desc_logs);
			box0_logs.add(Box.createGlue());

			JLabel logs_filter_lbl = new JLabel("Logs Filter    :");
			logs_filter_lbl.setForeground(Utils.THEME_PARAGRAPH_COLOR);
			logs_filter_lbl.setFont(Utils.getParagraphFont(11));
			box1_logs.add(Box.createHorizontalStrut(10));
			box1_logs.add(logs_filter_lbl);
			box1_logs.add(Box.createHorizontalStrut(15));
			box1_logs.add(logs_filter_input);
			logs_filter_input.setBackground(Utils.INPUTS_BG_COLOR);
			logs_filter_input.setForeground(Utils.INPUTS_TXT_COLOR);
			logs_filter_input.setFont(Utils.getParagraphFont(Utils.INPUTS_FONT_SIZE));
			logs_filter_input.setBorder(
					new CompoundBorder(new LineBorder(Utils.THEME_BG_COLOR, 1), new EmptyBorder(5, 5, 5, 5)));

			logs_filter_input.addFocusListener(new FocusListener() {

				@Override
				public void focusLost(FocusEvent arg0) {
					logs_filter_input.setBorder(
							new CompoundBorder(new LineBorder(Utils.THEME_BG_COLOR, 1), new EmptyBorder(5, 5, 5, 5)));
				}

				@Override
				public void focusGained(FocusEvent arg0) {
					logs_filter_input.setBorder(new CompoundBorder(new LineBorder(Utils.HOVER_BORDER_COLOR, 1),
							new EmptyBorder(5, 5, 5, 5)));
				}
			});

			get_logs_btn.setForeground(Utils.THEME_PARAGRAPH_COLOR);
			get_logs_btn.setFont(Utils.getParagraphFont(12));
			get_logs_btn.setBorder(
					new CompoundBorder(new LineBorder(new Color(0x6d556b), 1), new EmptyBorder(5, 20, 5, 20)));
			get_logs_btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
			get_logs_btn.addMouseListener(new MouseAdapter() {
				public void mouseEntered(MouseEvent evt) {
					get_logs_btn.setBorder(new CompoundBorder(new LineBorder(Utils.HOVER_BORDER_COLOR, 1, true),
							new EmptyBorder(5, 20, 5, 20)));
				}

				public void mouseExited(MouseEvent evt) {
					get_logs_btn.setBorder(
							new CompoundBorder(new LineBorder(new Color(0x6d556b), 1), new EmptyBorder(5, 20, 5, 20)));
				}

				public void mouseClicked(MouseEvent evt) {
					get_logs_btn.requestFocus();
				}
			});
			get_logs_btn.setFocusable(true);
			get_logs_btn.addFocusListener(new FocusListener() {

				@Override
				public void focusLost(FocusEvent arg0) {
					get_logs_btn.setBorder(
							new CompoundBorder(new LineBorder(new Color(0x6d556b), 1), new EmptyBorder(5, 20, 5, 20)));
				}

				@Override
				public void focusGained(FocusEvent arg0) {
					get_logs_btn.setBorder(new CompoundBorder(new LineBorder(Utils.HOVER_BORDER_COLOR, 1, true),
							new EmptyBorder(5, 20, 5, 20)));
				}
			});

			clear_logs_btn.setForeground(Utils.THEME_PARAGRAPH_COLOR);
			clear_logs_btn.setFont(Utils.getParagraphFont(12));
			clear_logs_btn.setBorder(
					new CompoundBorder(new LineBorder(new Color(0x6d556b), 1), new EmptyBorder(5, 20, 5, 20)));
			clear_logs_btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
			clear_logs_btn.addMouseListener(new MouseAdapter() {
				public void mouseEntered(MouseEvent evt) {
					clear_logs_btn.setBorder(new CompoundBorder(new LineBorder(Utils.HOVER_BORDER_COLOR, 1, true),
							new EmptyBorder(5, 20, 5, 20)));
				}

				public void mouseExited(MouseEvent evt) {
					clear_logs_btn.setBorder(
							new CompoundBorder(new LineBorder(new Color(0x6d556b), 1), new EmptyBorder(5, 20, 5, 20)));
				}

				public void mouseClicked(MouseEvent evt) {
					clear_logs_btn.requestFocus();
				}
			});
			clear_logs_btn.setFocusable(true);
			clear_logs_btn.addFocusListener(new FocusListener() {

				@Override
				public void focusLost(FocusEvent arg0) {
					clear_logs_btn.setBorder(
							new CompoundBorder(new LineBorder(new Color(0x6d556b), 1), new EmptyBorder(5, 20, 5, 20)));
				}

				@Override
				public void focusGained(FocusEvent arg0) {
					clear_logs_btn.setBorder(new CompoundBorder(new LineBorder(Utils.HOVER_BORDER_COLOR, 1, true),
							new EmptyBorder(5, 20, 5, 20)));
				}
			});
			box2_logs.add(Box.createGlue());
			box2_logs.add(clear_logs_btn);
			box2_logs.add(Box.createHorizontalStrut(10));
			box2_logs.add(get_logs_btn);

			logs_viewer_area.setCursor(new Cursor(Cursor.TEXT_CURSOR));
			logs_viewer_area.setBackground(new Color(0, 0, 0, 255));
			logs_viewer_area.setForeground(Utils.THEME_PARAGRAPH_COLOR);
			logs_viewer_area.setFont(Utils.getParagraphFont(Utils.INPUTS_FONT_SIZE));
			logs_viewer_area.setBorder(
					new CompoundBorder(new LineBorder(Utils.THEME_BG_COLOR, 1), new EmptyBorder(10, 10, 10, 10)));
			logs_viewer_area.setEditable(false);
			LookAndFeel laf = UIManager.getLookAndFeel();
			try {
				for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
					if ("Metal".equals(info.getName())) {
						javax.swing.UIManager.setLookAndFeel(info.getClassName());
						break;
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			JScrollPane area_srcl_pn = new JScrollPane(logs_viewer_area);
			UIManager.setLookAndFeel(laf);
			area_srcl_pn.setBackground(new Color(0, 0, 0, 255));
			area_srcl_pn.setForeground(Utils.THEME_PARAGRAPH_COLOR);
			area_srcl_pn.setFont(Utils.getParagraphFont(10));
			area_srcl_pn.setBorder(null);
			area_srcl_pn.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
			area_srcl_pn.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
			box3_logs.add(area_srcl_pn);

			clear_outs_btn.setForeground(Utils.THEME_PARAGRAPH_COLOR);
			clear_outs_btn.setFont(Utils.getParagraphFont(12));
			clear_outs_btn.setBorder(
					new CompoundBorder(new LineBorder(new Color(0x6d556b), 1), new EmptyBorder(5, 20, 5, 20)));
			clear_outs_btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
			clear_outs_btn.addMouseListener(new MouseAdapter() {
				public void mouseEntered(MouseEvent evt) {
					clear_outs_btn.setBorder(new CompoundBorder(new LineBorder(Utils.HOVER_BORDER_COLOR, 1, true),
							new EmptyBorder(5, 20, 5, 20)));
				}

				public void mouseExited(MouseEvent evt) {
					clear_outs_btn.setBorder(
							new CompoundBorder(new LineBorder(new Color(0x6d556b), 1), new EmptyBorder(5, 20, 5, 20)));
				}

				public void mouseClicked(MouseEvent evt) {
					clear_outs_btn.requestFocus();
					logs_viewer_area.setText("");
				}
			});
			clear_outs_btn.setFocusable(true);
			Utils.addEnterKeyListenerForClickEvent(clear_outs_btn);
			clear_outs_btn.setFocusable(true);
			clear_outs_btn.addFocusListener(new FocusListener() {

				@Override
				public void focusLost(FocusEvent arg0) {
					clear_outs_btn.setBorder(
							new CompoundBorder(new LineBorder(new Color(0x6d556b), 1), new EmptyBorder(5, 20, 5, 20)));
				}

				@Override
				public void focusGained(FocusEvent arg0) {
					clear_outs_btn.setBorder(new CompoundBorder(new LineBorder(Utils.HOVER_BORDER_COLOR, 1, true),
							new EmptyBorder(5, 20, 5, 20)));
				}
			});
			box4_logs.add(Box.createGlue());
			box4_logs.add(clear_outs_btn);

			logs_tab_ui_holder.add(box0_logs);
			logs_tab_ui_holder.add(Box.createVerticalStrut(15));
			logs_tab_ui_holder.add(box1_logs);
			logs_tab_ui_holder.add(Box.createVerticalStrut(15));
			logs_tab_ui_holder.add(box2_logs);
			logs_tab_ui_holder.add(Box.createVerticalStrut(15));
			logs_tab_ui_holder.add(box3_logs);
			logs_tab_ui_holder.add(Box.createVerticalStrut(2));
			logs_tab_ui_holder.add(box4_logs);

			abort_tab_ui_holder.setBorder(new EmptyBorder(30, 30, 30, 30));
			Box box0_abort = Box.createHorizontalBox();
			Box box1_abort = Box.createHorizontalBox();
			Box box2_abort = Box.createHorizontalBox();

			JLabel desc_abort = new JLabel("Abort all current transactions    : ");
			desc_abort.setForeground(Utils.THEME_PARAGRAPH_COLOR);
			desc_abort.setFont(Utils.getParagraphFont(13));
			box0_abort.add(desc_abort);
			box0_abort.add(Box.createGlue());

			JLabel warning_lbl = new JLabel("Warning");
			warning_lbl.setForeground(Utils.THEME_HEADINGS_COLOR);
			warning_lbl.setFont(Utils.getHeadingsFont(12));
			JLabel warning_desc = new JLabel(
					":   If you abort all socket connections your all transaction will be closed! So be sure you are ready.");
			warning_desc.setForeground(Utils.THEME_PARAGRAPH_COLOR);
			warning_desc.setFont(Utils.getParagraphFont(12));
			box1_abort.add(Box.createHorizontalStrut(0));
			box1_abort.add(warning_lbl);
			box1_abort.add(Box.createHorizontalStrut(10));
			box1_abort.add(warning_desc);

			abort_btn.setForeground(Utils.THEME_PARAGRAPH_COLOR);
			abort_btn.setFont(Utils.getParagraphFont(12));
			abort_btn.setBorder(
					new CompoundBorder(new LineBorder(new Color(0x6d556b), 1), new EmptyBorder(5, 40, 5, 40)));
			abort_btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
			abort_btn.addMouseListener(new MouseAdapter() {
				public void mouseEntered(MouseEvent evt) {
					abort_btn.setBorder(new CompoundBorder(new LineBorder(Utils.HOVER_BORDER_COLOR, 1, true),
							new EmptyBorder(5, 40, 5, 40)));
				}

				public void mouseExited(MouseEvent evt) {
					abort_btn.setBorder(
							new CompoundBorder(new LineBorder(new Color(0x6d556b), 1), new EmptyBorder(5, 40, 5, 40)));
				}

				public void mouseClicked(MouseEvent evt) {
					abort_btn.requestFocus();
				}
			});
			abort_btn.setFocusable(true);
			abort_btn.addFocusListener(new FocusListener() {

				@Override
				public void focusLost(FocusEvent arg0) {
					abort_btn.setBorder(
							new CompoundBorder(new LineBorder(new Color(0x6d556b), 1), new EmptyBorder(5, 40, 5, 40)));
				}

				@Override
				public void focusGained(FocusEvent arg0) {
					abort_btn.setBorder(new CompoundBorder(new LineBorder(Utils.HOVER_BORDER_COLOR, 1, true),
							new EmptyBorder(5, 40, 5, 40)));
				}
			});
			// box2_abort.setPreferredSize(new Dimension(590, 80));
			box2_abort.add(abort_btn);

			abort_tab_ui_holder.add(box0_abort);
			abort_tab_ui_holder.add(Box.createVerticalStrut(10));
			abort_tab_ui_holder.add(box1_abort);
			abort_tab_ui_holder.add(Box.createVerticalStrut(20));
			abort_tab_ui_holder.add(box2_abort);

			settings_tab_ui_holder.setBorder(new EmptyBorder(30, 30, 30, 30));
			Box box1 = Box.createHorizontalBox();
			Box box2 = Box.createVerticalBox();
			Box box3 = Box.createHorizontalBox();
			Box box4 = Box.createHorizontalBox();
			Box box5 = Box.createHorizontalBox();
			Box box6 = Box.createHorizontalBox();
			Box box7 = Box.createVerticalBox();
			Box box8 = Box.createHorizontalBox();
			Box box9 = Box.createHorizontalBox();
			Box box10 = Box.createHorizontalBox();
			Box box11 = Box.createHorizontalBox();

			JLabel always_on_top_title = new JLabel("Always On Top");
			always_on_top_title.setForeground(Utils.THEME_PARAGRAPH_COLOR);
			always_on_top_title.setFont(Utils.getParagraphFont(13));
			box3.add(always_on_top_title);
			box3.add(Box.createGlue());

			JLabel always_on_top_Short_note = new JLabel(
					"set the frame will be always on top or not. check for always on top and uncheck for reverse   :");
			always_on_top_Short_note.setForeground(Utils.SETTINGS_SHORT_NOTE_TXT_COLOR);
			always_on_top_Short_note.setFont(Utils.getParagraphFont(12));
			box4.add(always_on_top_Short_note);
			box4.add(Box.createHorizontalStrut(10));
			box4.add(always_on_top_check_box);
			box4.add(Box.createGlue());

			JLabel check_update_title = new JLabel("Check Update");
			check_update_title.setForeground(Utils.THEME_PARAGRAPH_COLOR);
			check_update_title.setFont(Utils.getParagraphFont(13));
			box5.add(check_update_title);
			box5.add(Box.createGlue());

			check_update_btn.setForeground(Utils.THEME_PARAGRAPH_COLOR);
			check_update_btn.setFont(Utils.getParagraphFont(11));
			check_update_btn.setBorder(
					new CompoundBorder(new LineBorder(new Color(0x6d556b), 1), new EmptyBorder(4, 10, 4, 10)));
			check_update_btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
			check_update_btn.addMouseListener(new MouseAdapter() {
				public void mouseEntered(MouseEvent evt) {
					check_update_btn.setBorder(new CompoundBorder(new LineBorder(Utils.HOVER_BORDER_COLOR, 1, true),
							new EmptyBorder(4, 10, 4, 10)));
				}

				public void mouseExited(MouseEvent evt) {
					check_update_btn.setBorder(
							new CompoundBorder(new LineBorder(new Color(0x6d556b), 1), new EmptyBorder(4, 10, 4, 10)));
				}

				public void mouseClicked(MouseEvent evt) {
					check_update_btn.requestFocus();
				}
			});
			check_update_btn.setFocusable(true);
			check_update_btn.addFocusListener(new FocusListener() {

				@Override
				public void focusLost(FocusEvent arg0) {
					check_update_btn.setBorder(
							new CompoundBorder(new LineBorder(new Color(0x6d556b), 1), new EmptyBorder(4, 10, 4, 10)));
				}

				@Override
				public void focusGained(FocusEvent arg0) {
					check_update_btn.setBorder(new CompoundBorder(new LineBorder(Utils.HOVER_BORDER_COLOR, 1, true),
							new EmptyBorder(4, 10, 4, 10)));
				}
			});
			JLabel check_update_Short_note = new JLabel(
					"Check update for this desktop app, to get good performance always stay updated");
			check_update_Short_note.setForeground(Utils.SETTINGS_SHORT_NOTE_TXT_COLOR);
			check_update_Short_note.setFont(Utils.getParagraphFont(12));
			box6.add(check_update_Short_note);
			box6.add(Box.createHorizontalStrut(10));
			box6.add(check_update_btn);
			box6.add(Box.createGlue());

			update_found_lbl.setForeground(Utils.THEME_HEADINGS_COLOR);
			update_found_lbl.setFont(Utils.getParagraphFont(12));
			box8.add(update_found_lbl);
			box8.add(Box.createGlue());

			updated_app_version_name_lbl.setForeground(Utils.SETTINGS_SHORT_NOTE_TXT_COLOR);
			updated_app_version_name_lbl.setFont(Utils.getParagraphFont(12));
			box9.add(updated_app_version_name_lbl);
			box9.add(Box.createGlue());

			updated_app_size_lbl.setForeground(Utils.SETTINGS_SHORT_NOTE_TXT_COLOR);
			updated_app_size_lbl.setFont(Utils.getParagraphFont(12));
			box10.add(updated_app_size_lbl);
			box10.add(Box.createGlue());

			box7.add(box8);
			box7.add(box9);
			box7.add(box10);
			box7.setBorder(new EmptyBorder(0, 10, 10, 10));

			box11.add(Box.createHorizontalStrut(40));
			box11.add(box7);
			box11.add(Box.createGlue());

			box2.add(box3);
			box2.add(box4);
			box2.add(Box.createVerticalStrut(20));
			box2.add(box5);
			box2.add(box6);
			box2.add(Box.createVerticalStrut(0));
			box2.add(box11);

			box1.add(box2);
			box1.add(Box.createHorizontalStrut(38));
			settings_tab_ui_holder.add(box1);

			help_tab_ui_holder.setBorder(new EmptyBorder(30, 30, 30, 30));
			help_tab_ui_holder.setFocusable(true);
			Box box0_help = Box.createHorizontalBox();
			Box box1_help = Box.createHorizontalBox();
			Box box2_help = Box.createHorizontalBox();
			Box box3_help = Box.createHorizontalBox();
			Box box4_help = Box.createHorizontalBox();
			Box box5_help = Box.createHorizontalBox();
			Box box6_help = Box.createHorizontalBox();
			Box box7_help = Box.createHorizontalBox();
			Box box8_help = Box.createVerticalBox();
			Box box9_help = Box.createVerticalBox();
			Box box10_help = Box.createHorizontalBox();

			JLabel help_icon = new JLabel(new ImageIcon(
					getClass().getClassLoader().getResource("in/byter/vadb/assets/ic_help_white_24px.png")));
			box0_help.add(help_icon);

			JLabel help_text = new JLabel(
					"If you face any problem or if you have any query, then please contact with us.");
			help_text.setForeground(Utils.THEME_PARAGRAPH_COLOR);
			help_text.setFont(Utils.getParagraphFont(12));
			box1_help.add(help_text);

			JLabel email_lbl1 = new JLabel("Email   :   ");
			JTextField email_lbl2 = new JTextField(20);
			email_lbl2.setText("ariyankhan46@gmail.com");
			email_lbl2.setBorder(new EmptyBorder(0, 5, 0, 5));
			email_lbl2.setBackground(null);
			email_lbl2.setEditable(false);
			email_lbl2.setCursor(new Cursor(Cursor.HAND_CURSOR));
			email_lbl1.setForeground(Utils.SETTINGS_SHORT_NOTE_TXT_COLOR);
			email_lbl1.setFont(Utils.getParagraphFont(11));
			email_lbl2.setForeground(Utils.SETTINGS_SHORT_NOTE_TXT_COLOR);
			email_lbl2.setFont(Utils.getParagraphFont(11));
			box2_help.add(email_lbl1);
			box2_help.add(email_lbl2);
			box2_help.add(Box.createGlue());
			email_lbl2.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent evt) {
					try {
						String uri = String.format("mailto:?subject=%s&body=%s&to=%s",
								URLEncoder.encode("Help", "UTF-8"),
								URLEncoder.encode("Hi, Ariyan", "UTF-8").replaceAll(Pattern.quote("+"), "%20"),
								URLEncoder.encode("ariyankhan46@gmail.com", "UTF-8"));
						Utils.mail(new URI(uri));
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});

			JLabel website_lbl1 = new JLabel("Website   :   ");
			JTextField website_lbl2 = new JTextField(20);
			website_lbl2.setText("www.byter.in");
			website_lbl2.setBorder(new EmptyBorder(0, 5, 0, 5));
			website_lbl2.setCursor(new Cursor(Cursor.HAND_CURSOR));
			website_lbl2.setBackground(null);
			website_lbl2.setEditable(false);
			website_lbl1.setForeground(Utils.SETTINGS_SHORT_NOTE_TXT_COLOR);
			website_lbl1.setFont(Utils.getParagraphFont(12));
			website_lbl2.setForeground(new Color(0xff5486d9));
			website_lbl2.setFont(Utils.getParagraphFont(12));
			box3_help.add(website_lbl1);
			box3_help.add(website_lbl2);
			box3_help.add(Box.createGlue());
			website_lbl2.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent evt) {
					try {
						Utils.browseUrl(new URL("http://byter.in"));
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});

			JLabel profile_link_lbl1 = new JLabel("Facebook   :   ");
			JTextField profile_link_lbl2 = new JTextField(20);
			profile_link_lbl2.setText("www.facebook.com/fakeyounger");
			profile_link_lbl2.setBorder(new EmptyBorder(0, 5, 0, 5));
			profile_link_lbl2.setCursor(new Cursor(Cursor.HAND_CURSOR));
			profile_link_lbl2.setBackground(null);
			profile_link_lbl2.setEditable(false);
			profile_link_lbl1.setForeground(Utils.SETTINGS_SHORT_NOTE_TXT_COLOR);
			profile_link_lbl1.setFont(Utils.getParagraphFont(12));
			profile_link_lbl2.setForeground(new Color(0xff5486d9));
			profile_link_lbl2.setFont(Utils.getParagraphFont(12));
			box4_help.add(profile_link_lbl1);
			box4_help.add(profile_link_lbl2);
			box4_help.add(Box.createGlue());
			profile_link_lbl2.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent evt) {
					try {
						Utils.browseUrl(new URL("https://facebook.com/fakeyounger"));
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});

			JLabel credits_lbl1 = new JLabel("Credits  :   ");
			JTextField credits_lbl2 = new JTextField(16);
			credits_lbl2.setText("Ariyan Khan");
			credits_lbl2.setBorder(new EmptyBorder(0, 5, 0, 5));
			credits_lbl2.setBackground(null);
			credits_lbl2.setEditable(false);
			credits_lbl1.setForeground(Utils.SETTINGS_SHORT_NOTE_TXT_COLOR);
			credits_lbl1.setFont(Utils.getParagraphFont(12));
			credits_lbl2.setForeground(Utils.SETTINGS_SHORT_NOTE_TXT_COLOR);
			credits_lbl2.setFont(Utils.getParagraphFont(12));
			box5_help.add(credits_lbl1);
			box5_help.add(credits_lbl2);
			box5_help.add(Box.createGlue());

			JLabel app_version_lbl1 = new JLabel("Version  :   ");
			JTextField app_version_lbl2 = new JTextField(16);
			app_version_lbl2.setText("v2.0.0");
			app_version_lbl2.setBorder(new EmptyBorder(0, 5, 0, 5));
			app_version_lbl2.setBackground(null);
			app_version_lbl2.setEditable(false);
			app_version_lbl1.setForeground(Utils.SETTINGS_SHORT_NOTE_TXT_COLOR);
			app_version_lbl1.setFont(Utils.getParagraphFont(12));
			app_version_lbl2.setForeground(Utils.SETTINGS_SHORT_NOTE_TXT_COLOR);
			app_version_lbl2.setFont(Utils.getParagraphFont(12));
			box6_help.add(app_version_lbl1);
			box6_help.add(app_version_lbl2);
			box6_help.add(Box.createGlue());

			JLabel last_moified_lbl1 = new JLabel("Last Modified  :   ");
			JTextField last_moified_lbl2 = new JTextField(16);
			last_moified_lbl2.setText("12th April, 2016");
			last_moified_lbl2.setBorder(new EmptyBorder(0, 5, 0, 5));
			last_moified_lbl2.setBackground(null);
			last_moified_lbl2.setEditable(false);
			last_moified_lbl1.setForeground(Utils.SETTINGS_SHORT_NOTE_TXT_COLOR);
			last_moified_lbl1.setFont(Utils.getParagraphFont(12));
			last_moified_lbl2.setForeground(Utils.SETTINGS_SHORT_NOTE_TXT_COLOR);
			last_moified_lbl2.setFont(Utils.getParagraphFont(12));
			box7_help.add(last_moified_lbl1);
			box7_help.add(last_moified_lbl2);
			box7_help.add(Box.createGlue());

			box8_help.add(box2_help);
			box8_help.add(box3_help);
			box8_help.add(box4_help);

			box9_help.add(box5_help);
			box9_help.add(box6_help);
			box9_help.add(box7_help);

			box10_help.add(box8_help);
			box10_help.add(Box.createGlue());
			box10_help.add(box9_help);

			help_tab_ui_holder.add(box0_help);
			help_tab_ui_holder.add(Box.createVerticalStrut(5));
			help_tab_ui_holder.add(box1_help);
			help_tab_ui_holder.add(Box.createVerticalStrut(30));
			help_tab_ui_holder.add(box10_help);

			small_frame_content_holder.setBorder(new EmptyBorder(15, 20, 15, 20));
			small_frame_run_btn.setForeground(Utils.THEME_PARAGRAPH_COLOR);
			small_frame_run_btn.setFont(Utils.getParagraphFont(12));
			small_frame_run_btn.setBorder(
					new CompoundBorder(new LineBorder(new Color(0x6d556b), 1), new EmptyBorder(5, 20, 5, 20)));
			small_frame_run_btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
			small_frame_run_btn.addMouseListener(new MouseAdapter() {
				public void mouseEntered(MouseEvent evt) {
					small_frame_run_btn.setBorder(new CompoundBorder(new LineBorder(Utils.HOVER_BORDER_COLOR, 1, true),
							new EmptyBorder(5, 20, 5, 20)));
				}

				public void mouseExited(MouseEvent evt) {
					small_frame_run_btn.setBorder(
							new CompoundBorder(new LineBorder(new Color(0x6d556b), 1), new EmptyBorder(5, 20, 5, 20)));
				}

				public void mouseClicked(MouseEvent evt) {
					small_frame_run_btn.requestFocus();
				}
			});
			small_frame_run_btn.setFocusable(true);
			small_frame_run_btn.addFocusListener(new FocusListener() {

				@Override
				public void focusLost(FocusEvent arg0) {
					small_frame_run_btn.setBorder(
							new CompoundBorder(new LineBorder(new Color(0x6d556b), 1), new EmptyBorder(5, 20, 5, 20)));
				}

				@Override
				public void focusGained(FocusEvent arg0) {
					small_frame_run_btn.setBorder(new CompoundBorder(new LineBorder(Utils.HOVER_BORDER_COLOR, 1, true),
							new EmptyBorder(5, 20, 5, 20)));
				}
			});
			small_frame_get_logs_btn.setForeground(Utils.THEME_PARAGRAPH_COLOR);
			small_frame_get_logs_btn.setFont(Utils.getParagraphFont(12));
			small_frame_get_logs_btn.setBorder(
					new CompoundBorder(new LineBorder(new Color(0x6d556b), 1), new EmptyBorder(5, 20, 5, 20)));
			small_frame_get_logs_btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
			small_frame_get_logs_btn.addMouseListener(new MouseAdapter() {
				public void mouseEntered(MouseEvent evt) {
					small_frame_get_logs_btn.setBorder(new CompoundBorder(
							new LineBorder(Utils.HOVER_BORDER_COLOR, 1, true), new EmptyBorder(5, 20, 5, 20)));
				}

				public void mouseExited(MouseEvent evt) {
					small_frame_get_logs_btn.setBorder(
							new CompoundBorder(new LineBorder(new Color(0x6d556b), 1), new EmptyBorder(5, 20, 5, 20)));
				}

				public void mouseClicked(MouseEvent evt) {
					small_frame_get_logs_btn.requestFocus();
					try {

						if (!small_frame_get_logs_btn.isEnabled()) {
							AlertDialog.showAlertDialog(LauncherFrame.this, "Now one transaction is running!");
							return;
						}

						Vector<Component> tabs = new Vector<>();
						tabs.addElement(device_tab_lbl);
						tabs.addElement(run_tab_lbl);
						tabs.addElement(logs_tab_lbl);
						tabs.addElement(help_tab_lbl);
						tabs.addElement(abort_tab_lbl);
						tabs.addElement(settings_tab_lbl);

						for (Component tab : tabs) {
							tab.setBackground(null);
						}
						logs_tab_lbl.setBackground(Utils.THEME_BG_COLOR);
						content_holder.removeAll();
						content_holder.add(logs_tab_ui_holder);

						main_panel.removeAll();
						main_panel.add(tabs_holder, BorderLayout.NORTH);
						main_panel.add(content_holder, BorderLayout.CENTER);
						pack();
						setLocationRelativeTo(null);
						FRAME_IS_MAXIMUM = true;// important

						Run.logsBtnCallback(small_frame_get_logs_btn);// this
																		// the
																		// last
																		// instruction

					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
			small_frame_get_logs_btn.setFocusable(true);
			small_frame_get_logs_btn.addFocusListener(new FocusListener() {

				@Override
				public void focusLost(FocusEvent arg0) {
					small_frame_get_logs_btn.setBorder(
							new CompoundBorder(new LineBorder(new Color(0x6d556b), 1), new EmptyBorder(5, 20, 5, 20)));
				}

				@Override
				public void focusGained(FocusEvent arg0) {
					small_frame_get_logs_btn.setBorder(new CompoundBorder(
							new LineBorder(Utils.HOVER_BORDER_COLOR, 1, true), new EmptyBorder(5, 20, 5, 20)));
				}
			});
			small_frame_content_holder.add(small_frame_run_btn);
			small_frame_content_holder.add(Box.createHorizontalStrut(15));
			small_frame_content_holder.add(small_frame_get_logs_btn);

			Vector<Component> tabs = new Vector<>();
			tabs.addElement(device_tab_lbl);
			tabs.addElement(run_tab_lbl);
			tabs.addElement(logs_tab_lbl);
			tabs.addElement(help_tab_lbl);
			tabs.addElement(abort_tab_lbl);
			tabs.addElement(settings_tab_lbl);

			Vector<Component> tab_uis = new Vector<>();
			tab_uis.addElement(device_tab_ui_holder);
			tab_uis.addElement(run_tab_ui_holder);
			tab_uis.addElement(logs_tab_ui_holder);
			tab_uis.addElement(help_tab_ui_holder);
			tab_uis.addElement(abort_tab_ui_holder);
			tab_uis.addElement(settings_tab_ui_holder);

			for (Component tab : tabs) {
				tab.setBackground(null);
			}
			tabs.get(0).setBackground(Utils.THEME_BG_COLOR);
			content_holder.removeAll();
			content_holder.add(tab_uis.get(0));

			for (int i = 0; i < tabs.size(); i++) {
				Component tab_ui = tab_uis.get(i);
				Component tb = tabs.get(i);
				tb.addMouseListener(new MouseAdapter() {
					public void mouseClicked(MouseEvent evt) {
						for (Component tab : tabs) {
							tab.setBackground(null);
						}
						tb.setBackground(Utils.THEME_BG_COLOR);
						content_holder.removeAll();
						content_holder.add(tab_ui);
						pack();
					}
				});
			}

			device_tab_lbl.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent evt) {
					ip_input.requestFocus();
				}
			});

			run_tab_lbl.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent evt) {
					file_input.requestFocus();
				}
			});

			logs_tab_lbl.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent evt) {
					logs_filter_input.requestFocus();
					LauncherFrame.this.setLocationRelativeTo(null);
				}
			});
			
			LauncherFrame.this.addComponentListener(new ComponentAdapter() {
				@Override
				public void componentMoved(ComponentEvent arg0) {
					try {
						if(!FRAME_IS_MAXIMUM) {
							FRAME_PREVIOUS_POINT = getLocation();
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});

			small_frame_lbl.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent evt) {
					try {

						if (FRAME_IS_MAXIMUM) {
							main_panel.removeAll();
							main_panel.add(small_frame_content_holder);
							pack();
							if(FRAME_PREVIOUS_POINT.x == WRONG_FRAME_POS_X_Y_VAL && FRAME_PREVIOUS_POINT.y == WRONG_FRAME_POS_X_Y_VAL) {
								setLocation(0,
										(int) (getToolkit().getScreenSize().getHeight() - (getSize().getHeight())) - 0);
								FRAME_PREVIOUS_POINT = getLocation();
							} else {
								setLocation(FRAME_PREVIOUS_POINT);
							}
							
							FRAME_IS_MAXIMUM = false;
						} else {
							main_panel.removeAll();
							main_panel.add(tabs_holder, BorderLayout.NORTH);
							main_panel.add(content_holder, BorderLayout.CENTER);
							pack();
							setLocationRelativeTo(null);
							FRAME_IS_MAXIMUM = true;
						}

					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
			Utils.addEnterKeyListenerForClickEvent(small_frame_lbl);

			content_holder.setBackground(null);
			main_panel.add(tabs_holder, BorderLayout.NORTH);
			main_panel.add(content_holder, BorderLayout.CENTER);
			root.add(top_panel, BorderLayout.NORTH);
			root.add(main_panel, BorderLayout.CENTER);
			content_pane.setLayout(new BorderLayout());
			content_pane.add(root);
			DragHandler dragHandler = new DragHandler();// same object
			content_pane.addMouseListener(dragHandler);
			content_pane.addMouseMotionListener(dragHandler);
			top_panel.setBackground(Utils.TOP_PANEL_BG_COLOR);
			main_panel.setBackground(null);
			root.setBackground(null);
			content_pane.setBackground(Utils.THEME_BG_COLOR);
			this.setUndecorated(true);
			this.setSize(500, 300);
			this.setLocationRelativeTo(null);
			this.setAlwaysOnTop(true);
			this.setResizable(false);
			pack();
			try {
				URL icon_url = getClass().getClassLoader().getResource("in/byter/vadb/assets/app_icon_48_48.png");
				setIconImage(getToolkit().getImage(icon_url));
			} catch (Exception e) {
				e.printStackTrace();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private class DragHandler extends MouseAdapter {

		private int FRAME_DRAG_X_POS_TRACKER = 0;
		private int FRAME_DRAG_Y_POS_TRACKER = 0;

		public void mousePressed(MouseEvent evt) {
			try {
				FRAME_DRAG_X_POS_TRACKER = evt.getXOnScreen();
				FRAME_DRAG_Y_POS_TRACKER = evt.getYOnScreen();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		public void mouseDragged(MouseEvent evt) {
			try {

				int dx = evt.getXOnScreen() - FRAME_DRAG_X_POS_TRACKER;
				int dy = evt.getYOnScreen() - FRAME_DRAG_Y_POS_TRACKER;

				FRAME_DRAG_X_POS_TRACKER = evt.getXOnScreen();
				FRAME_DRAG_Y_POS_TRACKER = evt.getYOnScreen();

				Point prev_pos = getLocationOnScreen();
				Point next_pos = new Point(prev_pos.x + dx, prev_pos.y + dy);

				setLocation(next_pos);

			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	private void test(JComponent comp, Color color) {
		try {
			comp.setBackground(color);
			comp.setOpaque(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
