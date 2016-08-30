package in.byter.vadb.utils;

import java.awt.Color;
import java.awt.Component;
import java.awt.Desktop;
import java.awt.Font;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.PointerInfo;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;

public class Utils {
	
	public static Color THEME_BG_COLOR = new Color(0x221222);
	public static Color THEME_PARAGRAPH_COLOR = new Color(0xe0d4df);
	public static Color THEME_HEADINGS_COLOR = new Color(0xe52569);
	public static Color TOP_PANEL_BG_COLOR = new Color(0x3c233c);
	public static Color HOVER_BORDER_COLOR = new Color(0x4b6580);
	public static Color INPUTS_BG_COLOR = new Color(0x190c19);
	public static Color INPUTS_TXT_COLOR = new Color(0xc6bdc5);
	public static Color SETTINGS_SHORT_NOTE_TXT_COLOR = new Color(0x998F98);
	public static int INPUTS_FONT_SIZE = 11;
	
	public static int APP_VERSION_CODE = 20162;
	
	public static int MIN_SERVER_PORT = 0;
    public static int MAX_SEREVR_PORT = 65535;
    public static int SOCKET_DEFAULT_PORT = 1247;
	
	public static Font getParagraphFont(int size) {
		Font font = new Font("DIALOG", Font.PLAIN, size);
		try {
			
			InputStream font_data = Utils.class.getClassLoader().getResourceAsStream("in/byter/vadb/assets/fonts/OpenSans-Regular.ttf");
			Font font_copy = Font.createFont(Font.TRUETYPE_FONT, font_data);
			if(font_copy == null) {
				return new Font("DIALOG", Font.PLAIN, size);
			}
			
			font = font_copy.deriveFont(Font.PLAIN, (float)size);
			
		} catch (Exception e) {
			e.printStackTrace();
			return new Font("DIALOG", Font.PLAIN, size);
		}
		return font;
	}
	
	public static Font getAppLoadingFont(int size) {
		Font font = new Font("DIALOG", Font.PLAIN, size);
		try {
			
			InputStream font_data = Utils.class.getClassLoader().getResourceAsStream("in/byter/vadb/assets/fonts/Venera300.ttf");
			Font font_copy = Font.createFont(Font.TRUETYPE_FONT, font_data);
			if(font_copy == null) {
				return new Font("DIALOG", Font.PLAIN, size);
			}
			
			font = font_copy.deriveFont(Font.PLAIN, (float)size);
			
		} catch (Exception e) {
			e.printStackTrace();
			return new Font("DIALOG", Font.PLAIN, size);
		}
		return font;
	}
	
	public static Font getAppTitleFont(int size) {
		Font font = new Font("DIALOG", Font.PLAIN, size);
		try {
			
			InputStream font_data = Utils.class.getClassLoader().getResourceAsStream("in/byter/vadb/assets/fonts/Philosopher-Regular.ttf");
			Font font_copy = Font.createFont(Font.TRUETYPE_FONT, font_data);
			if(font_copy == null) {
				return new Font("DIALOG", Font.PLAIN, size);
			}
			
			font = font_copy.deriveFont(Font.PLAIN, (float)size);
			
		} catch (Exception e) {
			e.printStackTrace();
			return new Font("DIALOG", Font.PLAIN, size);
		}
		return font;
	}
	
	public static Font getHeadingsFont(int size) {
		Font font = new Font("DIALOG", Font.BOLD, size);
		try {
			
			InputStream font_data = Utils.class.getClassLoader().getResourceAsStream("in/byter/vadb/assets/fonts/OpenSans-Bold.ttf");
			Font font_copy = Font.createFont(Font.TRUETYPE_FONT, font_data);
			if(font_copy == null) {
				return new Font("DIALOG", Font.BOLD, size);
			}
			
			font = font_copy.deriveFont(Font.PLAIN, (float)size);
			
		} catch (Exception e) {
			e.printStackTrace();
			return new Font("DIALOG", Font.BOLD, size);
		}
		return font;
	}
	
	
	public static void browseUrl(URL url) {
		try {
			if(Desktop.isDesktopSupported()) {
				Desktop.getDesktop().browse(url.toURI());
			} else {
				String os = System.getProperty("os.name").toLowerCase();
				if(os.indexOf("win") >= 0) {
					Runtime.getRuntime().exec("rundll32 url.dll, FileProtocolHandler " + url.toString());
				} else if(os.indexOf("mac") >= 0) {
					Runtime.getRuntime().exec("open " + url.toString());
				} else if(os.indexOf("nix") >= 0 || os.indexOf("nux") >= 0) {
					String[] browsers = new String[] {"epiphany", "firefox", "mozilla", "konqueror", "netscape", "opera", "links", "lynx"};
					StringBuffer cmd = new StringBuffer();
					for(int i=0; i<browsers.length; i++) {
						cmd.append((i==0)?"":"||" + browsers[i] + "\"" + url.toString() + "\"");
					}
					Runtime.getRuntime().exec(new String[] {"sh", "-c", cmd.toString()});
				} else {
					System.out.println("The url can not be opened!");
				}
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void mail(URI uri) {
		try {
			if(Desktop.isDesktopSupported()) {
				Desktop.getDesktop().mail(uri);
			} else {
				System.out.println("The mail program can not be opened!");
			}	
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void addEnterKeyListenerForClickEvent(Component comp) {
		try {
			comp.addKeyListener(new KeyAdapter() {
				@Override
				public void keyPressed(KeyEvent arg0) {
					try {
						if(arg0.getKeyCode() == KeyEvent.VK_ENTER && arg0.getModifiers() == 0) {
							doClick(comp);
						}
					} catch(Exception e) {
						e.printStackTrace();
					}
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void doClick(Component comp) {
		try {
			for(MouseListener ml : comp.getMouseListeners()) {
				PointerInfo pi = MouseInfo.getPointerInfo();
				Point p = pi.getLocation();
				MouseEvent evt = new MouseEvent(comp, MouseEvent.MOUSE_CLICKED, System.currentTimeMillis(), 0, p.x, p.y, 1, false);
				if(ml == null) {
					continue;
				}
				ml.mouseClicked(evt);
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static boolean checkIPV4(String ip) {
		boolean outs = false;
		try {
			if (ip == null) {
				return false;
			}

			ip = ip.trim();

			if (!ip.isEmpty() && ip.matches("\\d+\\.\\d+\\.\\d+\\.\\d+")) {
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return outs;
	}
	
	public static boolean checkPort(String port) {
		boolean outs = false;
		try {
			if(port == null) {
				return false;
			}
			
			port = port.trim();
			
			if(port.isEmpty())
				return false;
			
			if(!port.matches("\\d+")) {
				return false;
			}
			
			try {
				int port_int = Integer.parseInt(port);
				if(port_int < MIN_SERVER_PORT || port_int > MAX_SEREVR_PORT) {
					return false;
				} else {
					return true;
				}
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		return outs;
	}
	
}





















