package in.byter.vadb.gui;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagLayout;
import java.awt.HeadlessException;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import in.byter.vadb.utils.Utils;

public class AppLoadingFrame extends JDialog {

	public AppLoadingFrame() throws HeadlessException {
		super();
		init();
	}
	
	public void init() {
		try {

			Container content_pane = getContentPane();
			// DragHandler dragHandler = new DragHandler();
			// content_pane.addMouseListener(dragHandler);
			// content_pane.addMouseMotionListener(dragHandler);
			content_pane.setLayout(new BorderLayout());

			JPanel pnl_top = new JPanel(new GridBagLayout());
			JPanel pnl_bottom = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
			Box box1 = Box.createVerticalBox();
			Box box2 = Box.createHorizontalBox();
			Box box3 = Box.createHorizontalBox();

			try {
				JLabel icon_holder = new JLabel();
				icon_holder.setIcon(new ImageIcon(
						getClass().getClassLoader().getResource("in/byter/vadb/assets/app_icon_48_48.png")));
				icon_holder.setPreferredSize(new Dimension(50, 50));
				box2.add(icon_holder);
			} catch (Exception e1) {
				e1.printStackTrace();
			}

			JLabel app_title = new JLabel("vADB");
			app_title.setForeground(Utils.THEME_PARAGRAPH_COLOR);
			app_title.setBorder(new EmptyBorder(3, 0, 0, 0));
			app_title.setFont(Utils.getAppLoadingFont(20));
			box3.add(app_title);

			box1.add(box2);
			box1.add(box3);

			JPanel pb = new JPanel();
			pb.setBackground(Utils.TOP_PANEL_BG_COLOR);
			pb.setPreferredSize(new Dimension(270, 1));
			pnl_bottom.add(pb);
			pnl_bottom.setBorder(new EmptyBorder(0, 10, 30, 10));

			pnl_top.add(box1);
			content_pane.add(pnl_top, BorderLayout.CENTER);
			// content_pane.add(pnl_bottom, BorderLayout.SOUTH);

			this.setCursor(new Cursor(Cursor.WAIT_CURSOR));
			box2.setBackground(null);
			box3.setBackground(null);
			box1.setBackground(null);
			pnl_top.setBackground(null);
			pnl_bottom.setBackground(null);
			content_pane.setBackground(in.byter.vadb.utils.Utils.THEME_BG_COLOR);
			this.setSize(300, 200);
			this.setUndecorated(true);
			this.setLocationRelativeTo(null);
			this.setAlwaysOnTop(true);
			this.setResizable(false);

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

}





























