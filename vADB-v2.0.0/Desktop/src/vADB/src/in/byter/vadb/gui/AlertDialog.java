package in.byter.vadb.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.FlowLayout;
import java.awt.Point;
import java.awt.Window;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import in.byter.vadb.utils.Utils;

public class AlertDialog extends JDialog {
	
	public JLabel okay_btn = new JLabel("OK");
	public JLabel cancel_btn = new JLabel("Cancel");
	public JLabel dispose_btn = new JLabel("x");
	private String txt = "";
	public Color MIDDLE_PANEL_COLOR = new Color(0x693e69);
	public Color BOTTOM_PANEL_COLOR = new Color(0x3c2d3c);

	public AlertDialog(String txt, Window parent, boolean modal, String title) {
		super(parent);
		this.setModal(modal);
		this.setTitle(title);
		this.txt = txt;
		init();
	}
	
	public void init() {
		try {
			
			Container content_pane = getContentPane();
			DragHandler dragHandler = new DragHandler();
			content_pane.addMouseListener(dragHandler);
			content_pane.addMouseMotionListener(dragHandler);
			content_pane.setLayout(new BorderLayout());
			
			JPanel pnl_top = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
			JPanel pnl_middle = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
			JPanel pnl_bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 0));
			
			dispose_btn.setBorder(new CompoundBorder(new LineBorder(MIDDLE_PANEL_COLOR, 1, true),
					new EmptyBorder(3, 10, 3, 10)));
			dispose_btn.setForeground(Utils.THEME_PARAGRAPH_COLOR);
			dispose_btn.setFont(Utils.getParagraphFont(10));
			dispose_btn.addMouseListener(new MouseAdapter() {
				public void mouseEntered(MouseEvent evt) {
					dispose_btn.setBorder(new CompoundBorder(new LineBorder(Utils.HOVER_BORDER_COLOR, 1, true),
							new EmptyBorder(3, 10, 3, 10)));
				}

				public void mouseExited(MouseEvent evt) {
					dispose_btn.setBorder(
							new CompoundBorder(new LineBorder(MIDDLE_PANEL_COLOR, 1), new EmptyBorder(3, 10, 3, 10)));
				}

				public void mouseClicked(MouseEvent evt) {
					dispose_btn.requestFocus();
					setVisible(false);
					dispose();
				}
			});
			pnl_top.add(dispose_btn);
			Utils.addEnterKeyListenerForClickEvent(dispose_btn);
			
			pnl_middle.setBorder(new EmptyBorder(10, 50, 20, 50));
			JLabel txt_holder = new JLabel(txt);
			txt_holder.setForeground(Utils.THEME_PARAGRAPH_COLOR);
			txt_holder.setFont(Utils.getParagraphFont(12));
			pnl_middle.add(txt_holder);
			
			okay_btn.setForeground(Utils.THEME_PARAGRAPH_COLOR);
			okay_btn.setFont(Utils.getParagraphFont(12));
			okay_btn.setBorder(
					new CompoundBorder(new LineBorder(new Color(0x6d556b), 1), new EmptyBorder(5, 30, 5, 30)));
			okay_btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
			okay_btn.addMouseListener(new MouseAdapter() {
				public void mouseEntered(MouseEvent evt) {
					okay_btn.setBorder(new CompoundBorder(new LineBorder(Utils.HOVER_BORDER_COLOR, 1, true),
							new EmptyBorder(5, 30, 5, 30)));
				}

				public void mouseExited(MouseEvent evt) {
					okay_btn.setBorder(
							new CompoundBorder(new LineBorder(new Color(0x6d556b), 1), new EmptyBorder(5, 30, 5, 30)));
				}

				public void mouseClicked(MouseEvent evt) {
					okay_btn.requestFocus();
					setVisible(false);
					dispose();
				}
			});
			Utils.addEnterKeyListenerForClickEvent(okay_btn);
			okay_btn.setFocusable(true);
			okay_btn.addFocusListener(new FocusListener() {
				
				@Override
				public void focusLost(FocusEvent arg0) {
					okay_btn.setBorder(
							new CompoundBorder(new LineBorder(new Color(0x6d556b), 1), new EmptyBorder(5, 30, 5, 30)));
				}
				
				@Override
				public void focusGained(FocusEvent arg0) {
					okay_btn.setBorder(new CompoundBorder(new LineBorder(Utils.HOVER_BORDER_COLOR, 1, true),
							new EmptyBorder(5, 30, 5, 30)));
				}
			});
			cancel_btn.setForeground(Utils.THEME_PARAGRAPH_COLOR);
			cancel_btn.setFont(Utils.getParagraphFont(12));
			cancel_btn.setBorder(
					new CompoundBorder(new LineBorder(new Color(0x6d556b), 1), new EmptyBorder(5, 30, 5, 30)));
			cancel_btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
			cancel_btn.addMouseListener(new MouseAdapter() {
				public void mouseEntered(MouseEvent evt) {
					cancel_btn.setBorder(new CompoundBorder(new LineBorder(Utils.HOVER_BORDER_COLOR, 1, true),
							new EmptyBorder(5, 30, 5, 30)));
				}

				public void mouseExited(MouseEvent evt) {
					cancel_btn.setBorder(
							new CompoundBorder(new LineBorder(new Color(0x6d556b), 1), new EmptyBorder(5, 30, 5, 30)));
				}

				public void mouseClicked(MouseEvent evt) {
					cancel_btn.requestFocus();
					setVisible(false);
					dispose();
				}
			});
			Utils.addEnterKeyListenerForClickEvent(cancel_btn);
			cancel_btn.setFocusable(true);
			cancel_btn.addFocusListener(new FocusListener() {
				
				@Override
				public void focusLost(FocusEvent arg0) {
					cancel_btn.setBorder(
							new CompoundBorder(new LineBorder(new Color(0x6d556b), 1), new EmptyBorder(5, 30, 5, 30)));
				}
				
				@Override
				public void focusGained(FocusEvent arg0) {
					cancel_btn.setBorder(new CompoundBorder(new LineBorder(Utils.HOVER_BORDER_COLOR, 1, true),
							new EmptyBorder(5, 30, 5, 30)));
				}
			});
			pnl_bottom.setBorder(new EmptyBorder(15, 7, 15, 7));
			pnl_bottom.setBackground(BOTTOM_PANEL_COLOR);
			pnl_bottom.add(cancel_btn);
			pnl_bottom.add(okay_btn);
			try {
				
				MouseAdapter ma = new MouseAdapter() {
					public void mouseClicked(MouseEvent evt) {
						if(isVisible()) {
							System.out.println("lll");
						} else {
							System.out.println("lllk");
						}
					}
				};
				
				getParent().addMouseListener(ma);
				
			} catch(Exception e) {
				e.printStackTrace();
			}
			content_pane.add(pnl_top, BorderLayout.NORTH);
			content_pane.add(pnl_middle, BorderLayout.CENTER);
			content_pane.add(pnl_bottom, BorderLayout.SOUTH);
			pnl_top.setBackground(MIDDLE_PANEL_COLOR);
			pnl_middle.setBackground(MIDDLE_PANEL_COLOR);
			content_pane.setBackground(in.byter.vadb.utils.Utils.TOP_PANEL_BG_COLOR);
			this.setSize(200, 100);
			this.setUndecorated(true);
			this.setAlwaysOnTop(true);
			this.setResizable(false);
			pack();
			setLocationRelativeTo(getParent());//important, below of pack() call.
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public static void showAlertDialog(Window parent, String txt) {
		try {
			
			AlertDialog ad = new AlertDialog(txt, parent, true, "vADB");
			ad.cancel_btn.setVisible(false);
			ad.setVisible(true);
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static int showConfirmDialog(Window parent, String txt) {
		final HashMap<String, Integer> results = new HashMap<>();
		results.put("RESPONSE", 0);
		try {
			AlertDialog ad = new AlertDialog(txt, parent, true, "vADB");
			ad.okay_btn.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent evt) {
					results.put("RESPONSE", 1);
				}
			});
			ad.setVisible(true);
		} catch(Exception e) {
			e.printStackTrace();
		}
		return results.get("RESPONSE");
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
