package in.byter.vadb.run;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URL;
import java.util.HashMap;
import java.util.Random;
import java.util.Vector;

import javax.swing.JLabel;
import javax.swing.SwingUtilities;
import javax.swing.UnsupportedLookAndFeelException;

import in.byter.vadb.gui.AlertDialog;
import in.byter.vadb.gui.AppLoadingFrame;
import in.byter.vadb.gui.LauncherFrame;
import in.byter.vadb.utils.DataTransactionSignals;
import in.byter.vadb.utils.FilesData;
import in.byter.vadb.utils.RemoteServerHelper;
import in.byter.vadb.utils.Utils;

public class Run {
	
	static {

		try {
			for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
				if ("Nimbus".equals(info.getName())) {
					javax.swing.UIManager.setLookAndFeel(info.getClassName());
					break;
				}
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}

	}

	public static LauncherFrame main_frame;
	public static AppLoadingFrame app_loading_frame;
	public static Vector<Socket> socket_store = new Vector<>();
	public static int socket_store_max_length = 30;
	public static String app_update_download_link_holder = null;
	
	public static void main(String[] args) {
		try {          
			app_loading_frame = new AppLoadingFrame();
			app_loading_frame.setVisible(true);
			FilesData.setup();
			main_frame = new LauncherFrame("vADB");
			callbackAfterAppLaunching();
			
			Runnable save_Device_info_btn_callback = new Runnable() {
				
				@Override
				public void run() {
					try {
						String ip = main_frame.ip_input.getText().trim();
						String port = main_frame.port_input.getText().trim();
						if(!Utils.checkIPV4(ip)) {
							AlertDialog.showAlertDialog(main_frame,  "Target Device IP is invalid, provide IPV4 only!");
							return;
						}
						if(!Utils.checkPort(port)) {
							AlertDialog.showAlertDialog(main_frame, "Invalid port, Port must be in range : " + Utils.MIN_SERVER_PORT + "-" + Utils.MAX_SEREVR_PORT);
							return;
						}
						FilesData.setup();
						FilesData.writeFile(new File(FilesData.getDataRoot(), FilesData.FILE_IP_HOLDER), ip.trim(), false);
						FilesData.writeFile(new File(FilesData.getDataRoot(), FilesData.FILE_PORT_HOLDER), port.trim(), false);
						AlertDialog.showAlertDialog(main_frame, "Data has been saved successfully!");
					} catch(Exception e) {
						e.printStackTrace();
						AlertDialog.showAlertDialog(main_frame, e.getMessage());
					}
				}
			};
			main_frame.save_device_info_btn.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent evt) {
					save_Device_info_btn_callback.run();
				}
			});
			Utils.addEnterKeyListenerForClickEvent(main_frame.save_device_info_btn);
			
			
			Runnable run_btn_callback = new Runnable() {
				
				@Override
				public void run() {
					//if JLABEL is not enabled then it also receives the click event
					//so it is must to check.
					if(!main_frame.run_btn.isEnabled()) {
						AlertDialog.showAlertDialog(main_frame, "Now one transaction is running!");
						return;
					}
					runBtnCallback(main_frame.run_btn);//this is last instruction
				}
			};
			main_frame.run_btn.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent evt) {
					run_btn_callback.run();
				}
			});
			Utils.addEnterKeyListenerForClickEvent(main_frame.run_btn);
			
			Runnable small_run_btn_callback = new Runnable() {
				
				@Override
				public void run() {
					//if JLABEL is not enabled then it also receives the click event
					//so it is must to check.
					if(!main_frame.small_frame_run_btn.isEnabled()) {
						AlertDialog.showAlertDialog(main_frame, "Now one transaction is running!");
						return;
					}
					runBtnCallback(main_frame.small_frame_run_btn);//this is last instruction
				}
			};
			main_frame.small_frame_run_btn.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent evt) {
					small_run_btn_callback.run();
				}
			});
			Utils.addEnterKeyListenerForClickEvent(main_frame.small_frame_run_btn);
			
			Runnable get_logs_btn_callback = new Runnable() {
				
				@Override
				public void run() {
					//if JLABEL is not enabled then it also receives the click event
					//so it is must to check.
					if(!main_frame.get_logs_btn.isEnabled()) {
						AlertDialog.showAlertDialog(main_frame, "Now one transaction is running!");
						return;
					}
					logsBtnCallback(main_frame.get_logs_btn);//this is last instruction
				}
			};
			main_frame.get_logs_btn.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent evt) {
					get_logs_btn_callback.run();
				}
			});
			Utils.addEnterKeyListenerForClickEvent(main_frame.get_logs_btn);
			
			Runnable clear_logs_btn_callback = new Runnable() {
				
				@Override
				public void run() {
					//if JLABEL is not enabled then it also receives the click event
					//so it is must to check.
					if(!main_frame.clear_logs_btn.isEnabled()) {
						AlertDialog.showAlertDialog(main_frame, "Now one transaction is running!");
						return;
					}
					int outs = AlertDialog.showConfirmDialog(main_frame, "If you clear logs then you would not be access them, continue ?");
					if(outs == 1)
						clearLogsCallback(main_frame.clear_logs_btn);//it is last instruction
					else {
						return;
					}
				}
			};
			main_frame.clear_logs_btn.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent evt) {
					clear_logs_btn_callback.run();
				}
			});
			Utils.addEnterKeyListenerForClickEvent(main_frame.clear_logs_btn);
			
			Runnable abort_btn_callback = new Runnable() {
				
				@Override
				public void run() {
					//if JLABEL is not enabled then it also receives the click event
					//so it is must to check.
					if(!main_frame.abort_btn.isEnabled()) {
						AlertDialog.showAlertDialog(main_frame, "Now one transaction is running!");
						return;
					}
					abortTransactionsCallback(main_frame.abort_btn);//it is last instruction
				}
			};
			main_frame.abort_btn.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent evt) {
					abort_btn_callback.run();
				}
			});
			Utils.addEnterKeyListenerForClickEvent(main_frame.abort_btn);
			
			
			Runnable check_update_btn_callback = new Runnable() {
				
				@Override
				public void run() {
					//if JLABEL is not enabled then it also receives the click event
					//so it is must to check.
					if(!main_frame.check_update_btn.isEnabled()) {
						AlertDialog.showAlertDialog(main_frame, "Now one transaction is running!");
						return;
					}
					checkUpdateCallback(main_frame.check_update_btn);//it is last instruction
				}
			};
			main_frame.update_found_lbl.setVisible(false);
			main_frame.updated_app_version_name_lbl.setVisible(false);
			main_frame.updated_app_size_lbl.setVisible(false);
			main_frame.check_update_btn.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent evt) {
					check_update_btn_callback.run();
				}
			});
			Utils.addEnterKeyListenerForClickEvent(main_frame.check_update_btn);
			
			Runnable always_on_top_check_box_callback = new Runnable() {
				
				@Override
				public void run() {
					try {
						boolean selected = main_frame.always_on_top_check_box.isSelected();
						main_frame.setAlwaysOnTop(selected);
						main_frame.pack();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			};
			main_frame.always_on_top_check_box.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent arg0) {
					always_on_top_check_box_callback.run();
				}
			});
			main_frame.always_on_top_check_box.addKeyListener(new KeyAdapter() {
				@Override
				public void keyPressed(KeyEvent arg0) {
					try {
						if(arg0.getKeyCode() == KeyEvent.VK_ENTER && arg0.getModifiers() == 0) {
							if(main_frame.always_on_top_check_box.isSelected()) {
								main_frame.always_on_top_check_box.setSelected(false);
							} else {
								main_frame.always_on_top_check_box.setSelected(true);
							}
							always_on_top_check_box_callback.run();
						}
					} catch(Exception e) {
						e.printStackTrace();
					}
				}
			});
			
			main_frame.always_on_top_check_box.setSelected(true);
			main_frame.setAlwaysOnTop(main_frame.always_on_top_check_box.isSelected());
			main_frame.logs_viewer_area.setText("");
			appLoadingCompletedCallback();
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void checkUpdateCallback(JLabel btn) {
		try {
			
			if(main_frame.check_update_btn.getText().trim().equals("Update Now")) {
				try {
					
					if(app_update_download_link_holder != null) {
						Utils.browseUrl(new URL(app_update_download_link_holder));
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				return;
			}
			
			main_frame.check_update_btn.setEnabled(false);
			main_frame.check_update_btn.setText("Checking");
			main_frame.pack();
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					try {
						
						HashMap<String, String> outs_server = RemoteServerHelper.checkAppUpdate();
						String status = outs_server.get("STATUS").trim();
						if(status.equals("1")) {
							try {
								final String version_name = outs_server.get("VERSIONNAME");
								final String size = outs_server.get("SIZE");
								int version_code = Integer.parseInt(outs_server.get("VERSIONCODE"));
								String download_url = outs_server.get("DOWNLOADLINK");
								if(version_code > Utils.APP_VERSION_CODE) {
									app_update_download_link_holder = download_url;
									SwingUtilities.invokeLater(new Runnable() {
										
										@Override
										public void run() {
											main_frame.update_found_lbl.setVisible(true);
											main_frame.updated_app_version_name_lbl.setVisible(true);
											main_frame.updated_app_size_lbl.setVisible(true);
											main_frame.updated_app_version_name_lbl.setText("Version  :  " + version_name);
											main_frame.updated_app_size_lbl.setText("Size  :  " + size);
											main_frame.pack();
										}
									});
									exitCallbackForCheckUpdate();
									SwingUtilities.invokeLater(new Runnable() {
										
										@Override
										public void run() {
											main_frame.check_update_btn.setText("Update Now");
											main_frame.pack();
										}
									});
									return;
								} else {
									exitCallbackForCheckUpdate();
									AlertDialog.showAlertDialog(main_frame, "No updates found");
									return;
								}
								
							} catch (Exception e) {
								exitCallbackForCheckUpdate();
								AlertDialog.showAlertDialog(main_frame, "Server Internal Error Occurred or request timed out");
								return;
							}
						} else {
							exitCallbackForCheckUpdate();
							AlertDialog.showAlertDialog(main_frame, outs_server.get("REASON"));
							return;
						}
						
					} catch (Exception e) {
						exitCallbackForCheckUpdate();
						AlertDialog.showAlertDialog(main_frame, "Server Internal Error Occurred or request timed out");
						e.printStackTrace();
						return;
					}
				}
			}).start();
			
		} catch(Exception e) {
			exitCallbackForCheckUpdate();
			AlertDialog.showAlertDialog(main_frame, "Server Internal Error Occurred or request timed out");
			e.printStackTrace();
		}
	}
	
	public static void exitCallbackForCheckUpdate() {
		try {
			
			SwingUtilities.invokeLater(new Runnable() {
				
				@Override
				public void run() {
					main_frame.check_update_btn.setEnabled(true);
					main_frame.check_update_btn.setText("Check");
					main_frame.pack();
				}
			});
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void abortTransactionsCallback(JLabel abort_btn) {
		try {
			
			int outs = AlertDialog.showConfirmDialog(main_frame, "Are you sure you want to abort all transactions?");
			
			if(outs == 1) {
				abort_btn.setEnabled(false);
				abort_btn.setText("Aborting");
				new Thread(new Runnable() {
					
					@Override
					public void run() {
						try {
							synchronized (socket_store) {
								try {
									for (int i = 0; i < socket_store.size(); i++) {
										Socket s = socket_store.elementAt(i);
										try {
											if (s != null) {
												s.close();
											}
										} catch (Exception e) {
											e.printStackTrace();
										}
									}
									socket_store.removeAllElements();
								} catch (Exception e) {
									e.printStackTrace();
								}
							}

							try {
								Thread.sleep(500);
							} catch (Exception e) {
								e.printStackTrace();
							}

							SwingUtilities.invokeLater(new Runnable() {

								@Override
								public void run() {
									abort_btn.setEnabled(true);
									abort_btn.setText("Abort");
									AlertDialog.showAlertDialog(main_frame, "Aborting successful");
								}
							});

						} catch (Exception e) {
							e.printStackTrace();
							SwingUtilities.invokeLater(new Runnable() {

								@Override
								public void run() {
									abort_btn.setEnabled(true);
									abort_btn.setText("Abort");
									AlertDialog.showAlertDialog(main_frame, "Aborting successful");
								}
							});
						}
					}
				}).start();
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void appLoadingCompletedCallback() {
		try {
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					try {
						
						int random_wait = new Random(System.currentTimeMillis()).nextInt(5000);
						if(random_wait < 2000) {
							random_wait = 2000;
						}
						Thread.sleep(random_wait);
						SwingUtilities.invokeLater(new Runnable() {
							
							@Override
							public void run() {
								app_loading_frame.setVisible(false);
								app_loading_frame.dispose();
								main_frame.setVisible(true);
							}
						});
						
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}).start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void runBtnCallback(JLabel run_btn) {
		try {
			
			String ip_str = main_frame.ip_input.getText().trim();
			String port_str = main_frame.port_input.getText().trim();
			String file_str = main_frame.file_input.getText().trim();
			
			if(!Utils.checkIPV4(ip_str)) {
				AlertDialog.showAlertDialog(main_frame, "Target Device IP is invalid, provide IPV4 only!");
				return;
			}
			
			if(!Utils.checkPort(port_str)) {
				AlertDialog.showAlertDialog(main_frame, "Invalid port, Port must be in range : " + Utils.MIN_SERVER_PORT + "-" + Utils.MAX_SEREVR_PORT);
				return;
			}
			
			if(file_str.isEmpty() || !new File(file_str).isFile()) {
				AlertDialog.showAlertDialog(main_frame, "The given apk path is not exist or not a file!");
				return;
			}
			
			main_frame.run_btn.setEnabled(false);
			main_frame.small_frame_run_btn.setEnabled(false);
			
			new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						
						InetAddress ia = null;
						try {
							ia = InetAddress.getByName(ip_str);//important
						} catch(Exception e) {
							e.printStackTrace();
							actionOnSocketCloseForRun(null, "Target Device IP is invalid, provide IPV4 only!");
							return;
						}
						if(ia == null) {
							actionOnSocketCloseForRun(null, "Target Device IP is invalid, provide IPV4 only!");
							return;
						}
						
						int port = Utils.SOCKET_DEFAULT_PORT;
						try {
							port = Integer.parseInt(port_str);
						} catch(Exception e) {
							e.printStackTrace();
							actionOnSocketCloseForRun(null, "Invalid port, Port must be in range : " + Utils.MIN_SERVER_PORT + "-" + Utils.MAX_SEREVR_PORT);
							return;
						}
						File target_apk_file = new File(file_str);
						if(!target_apk_file.isFile()) {
							actionOnSocketCloseForRun(null, "The given apk path is not exist or not a file!");
							return;
						}
						
						
						Socket s = null;
						try {
							SwingUtilities.invokeLater(new Runnable() {
								@Override
								public void run() {
									main_frame.run_btn.setText("Connecting");
									main_frame.small_frame_run_btn.setText("Connecting");
									main_frame.pack();
								}
							});
							s = new Socket(ia, port); 
							final Socket temp = s;
							try {
								new Thread(new Runnable() {
									
									@Override
									public void run() {
										try {
											synchronized (socket_store) {
												socket_store.addElement(temp);
											}
										} catch (Exception e2) {
											e2.printStackTrace();
										}
									}
								}).start();
							} catch (Exception e2) {
								e2.printStackTrace();
							}
							SwingUtilities.invokeLater(new Runnable() {
								
								@Override
								public void run() {
									main_frame.run_btn.setText("Connected");
									main_frame.small_frame_run_btn.setText("Connected");
									main_frame.pack();
								}
							});
							
							InputStream is = s.getInputStream();
							OutputStream os = s.getOutputStream();
							
							SwingUtilities.invokeLater(new Runnable() {
								
								@Override
								public void run() {
									main_frame.run_btn.setText("Transferred 0%");
									main_frame.small_frame_run_btn.setText("Transferred 0%");
									main_frame.pack();
								}
							});
							
							os.write(DataTransactionSignals.RUN_APP_SIGNAL);
							long app_size_bytes = target_apk_file.length();
							if(app_size_bytes <= 0) {
								throw new Exception();
							}
							os.write((app_size_bytes + "").getBytes("UTF-8").length);
							os.write((app_size_bytes + "").getBytes("UTF-8"));
							
							FileInputStream fis = null;
							try {
								fis = new FileInputStream(target_apk_file);
								byte[] buff = new byte[1024*1024];
								int r;
								long total_sent = 0;
								while((r=fis.read(buff)) != -1) {
									os.write(buff, 0, r);
									total_sent += r;
									long temp1 = total_sent;
									SwingUtilities.invokeLater(new Runnable() {
										
										@Override
										public void run() {
											main_frame.run_btn.setText("Transferred " + (temp1*100/app_size_bytes) + "%");
											main_frame.small_frame_run_btn.setText("Transferred " + (temp1*100/app_size_bytes) + "%");
											main_frame.pack();
										}
									});
								}
								
								try {
									if(fis != null) {
										fis.close();
									}
								} catch(Exception e1) {
									e1.printStackTrace();
								}
								
								is.read();
								actionOnSocketCloseForRun(s, "App run is successful");
								return;
								
							} catch(Exception e) {
								try {
									if(fis != null) {
										fis.close();
									}
								} catch(Exception e1) {
									e1.printStackTrace();
								}
								e.printStackTrace();
								actionOnSocketCloseForRun(s, e.getMessage());
								return;
							}
							
						} catch(Exception e) {
							e.printStackTrace();
							actionOnSocketCloseForRun(s, e.getMessage());
							return;
						}
						
					} catch(Exception e) {
						e.printStackTrace();
						actionOnSocketCloseForRun(null, e.getMessage());
						return;
					}
				}
			}).start();
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void actionOnSocketCloseForRun(Socket s, String alert) {
		try {
			
			try {
				if(s != null) {
					s.close();
				}
			} catch(Exception e) {
				e.printStackTrace();
			}
			
			SwingUtilities.invokeLater(new Runnable() {
				
				@Override
				public void run() {
					main_frame.run_btn.setEnabled(true);
					main_frame.small_frame_run_btn.setEnabled(true);
					main_frame.run_btn.setText("Run");
					main_frame.small_frame_run_btn.setText("Run");
					main_frame.pack();
				}
			});
			
			
			//at last
			try {
				if(alert != null && !alert.trim().isEmpty()) {
					AlertDialog.showAlertDialog(main_frame, alert.trim());
				}
			} catch(Exception e) {
				e.printStackTrace();
			}
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void logsBtnCallback(JLabel btn) {
		try {
			
			main_frame.logs_viewer_area.setText("");
			
			String ip_str = main_frame.ip_input.getText().trim();
			String port_str = main_frame.port_input.getText().trim();
			String filter = main_frame.logs_filter_input.getText().trim();
			
			if(!Utils.checkIPV4(ip_str)) {
				AlertDialog.showAlertDialog(main_frame, "Target Device IP is invalid, provide IPV4 only!");
				return;
			}
			
			if(!Utils.checkPort(port_str)) {
				AlertDialog.showAlertDialog(main_frame, "Invalid port, Port must be in range : " + Utils.MIN_SERVER_PORT + "-" + Utils.MAX_SEREVR_PORT);
				return;
			}
			
			if(filter.isEmpty()) {
				main_frame.logs_viewer_area.setText("No logs found for this filter!");
				return;
			}
			
			main_frame.get_logs_btn.setEnabled(false);
			main_frame.clear_logs_btn.setEnabled(false);
			main_frame.small_frame_get_logs_btn.setEnabled(false);
			
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					try { 
						
						InetAddress ia = null;
						try {
							ia = InetAddress.getByName(ip_str);//important
						} catch(Exception e) {
							e.printStackTrace();
							actionOnSocketCloseForGetLogs(null, "Target Device IP is invalid, provide IPV4 only!");
							return;
						}
						if(ia == null) {
							actionOnSocketCloseForGetLogs(null, "Target Device IP is invalid, provide IPV4 only!");
							return;
						}
						
						int port = Utils.SOCKET_DEFAULT_PORT;
						try {
							port = Integer.parseInt(port_str);
						} catch(Exception e) {
							e.printStackTrace();
							actionOnSocketCloseForGetLogs(null, "Invalid port, Port must be in range : " + Utils.MIN_SERVER_PORT + "-" + Utils.MAX_SEREVR_PORT);
							return;
						}
						
						Socket s = null;
						try {
							
							SwingUtilities.invokeLater(new Runnable() {
								public void run() {
									main_frame.get_logs_btn.setText("Connecting");
									main_frame.small_frame_get_logs_btn.setText("Connecting");
									main_frame.pack();
								}
							});
							s = new Socket(ia, port);
							final Socket temp = s;
							try {
								new Thread(new Runnable() {
									
									@Override
									public void run() {
										try {
											synchronized (socket_store) {
												socket_store.addElement(temp);
											}
										} catch (Exception e2) {
											e2.printStackTrace();
										}
									}
								}).start();
							} catch (Exception e2) {
								e2.printStackTrace();
							}
							SwingUtilities.invokeLater(new Runnable() {
								public void run() {
									main_frame.get_logs_btn.setText("Connected");
									main_frame.small_frame_get_logs_btn.setText("Connected");
									main_frame.pack();
								}
							});
							
							InputStream is = s.getInputStream();
							OutputStream os = s.getOutputStream();
							
							SwingUtilities.invokeLater(new Runnable() {
								public void run() {
									main_frame.get_logs_btn.setText("Transferring");
									main_frame.small_frame_get_logs_btn.setText("Transferring");
									main_frame.pack();
								}
							});
							
							os.write(DataTransactionSignals.GET_LOGS_SIGNAL);
							writeStringLineToSocket(s, os, filter, new SocketPasser() {
								
								@Override
								public void run(Socket s, String error) {
									actionOnSocketCloseForGetLogs(s, null);
								}
							});
							
							while(true) {
								int code = is.read();
								if(code < 0) {
									throw new Exception();
								}
								if(code == DataTransactionSignals.NEW_LINE_STRING_TRANSFERRING_SIGNAL) {
									
									final String line = readStringDataFromSocket(s, is, new SocketPasser() {
										
										@Override
										public void run(Socket s, String error) {
											actionOnSocketCloseForGetLogs(s, null);
										}
									});
									SwingUtilities.invokeLater(new Runnable() {
										
										@Override
										public void run() {
											main_frame.logs_viewer_area.append(line + "\n"); //here line = line + "\n" from server
										}
									});
									
								} else if(code == DataTransactionSignals.NEW_LINE_STRING_TRANSFERRING_FINISHED_SIGNAL) {
									break;
								} else {
									throw new Exception();
								}
							}
							
							os.write(DataTransactionSignals.TRANSACTION_COMPLETED_SIGNAL);
							Thread.sleep(200);
							actionOnSocketCloseForGetLogs(s, null);
							
						} catch(Exception e) {
							e.printStackTrace();
							actionOnSocketCloseForGetLogs(s, e.getMessage());
						}
						
					} catch(Exception e) {
						e.printStackTrace();
						actionOnSocketCloseForGetLogs(null, e.getMessage());
					}
				}
			}).start();
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	//it can read the empty string
	public static String readStringDataFromSocket(Socket s, InputStream is, SocketPasser actonOnClose) {
        String outs = "";
        try {

            int bytes_in_size_str = is.read();
            if (bytes_in_size_str <= 0)
                throw new Exception();
            byte[] size_bytes = new byte[bytes_in_size_str];
            for(int i=0; i<bytes_in_size_str; i++) {
                byte byt = (byte)is.read();
                if (byt < 0)
                    throw new Exception();
                size_bytes[i] = byt;
            }
            String size_str = new String (size_bytes, "UTF-8");
            if (size_str.trim().isEmpty())
                throw new Exception();
            int size_in_bytes = 0;
            try {
                size_in_bytes = Integer.parseInt(size_str.trim());
            } catch (Exception e) {
                e.printStackTrace();
                throw new Exception();
            }
            if (size_in_bytes < 0) //for empty string size_in_bytes = 0; 
                throw new Exception();

            byte[] bytes = new byte[size_in_bytes];
            int z = 0;
            try {
                int total_read = 0;
                byte[] buff = new byte[size_in_bytes + 1];
                while (total_read < size_in_bytes) {
                    int r = is.read(buff, 0, size_in_bytes - total_read);
                    if (r < 0) {
                        throw new Exception();
                    }
                    for(int i=0; i<r; i++) {
                    	bytes[z] = buff[i];
                    	z++;
                    }
                    total_read += r;
                }

            } catch (Exception e) {
            	e.printStackTrace();
                actonOnClose.run(s, e.getMessage());
            }

            outs = new String(bytes, "UTF-8");

        } catch (Exception e) {
            e.printStackTrace();
            actonOnClose.run(s, e.getMessage());
        }
        return outs;
    }

	//it can write empty string
    public static void writeStringLineToSocket(Socket s, OutputStream os, String str, SocketPasser actonOnClose) {
        try {

            if (str == null) 
            	 str = "";

            String size_str = str.getBytes("UTF-8").length + "";
            os.write(size_str.getBytes().length);
            os.write(size_str.getBytes());
            os.write(str.getBytes("UTF-8"));

        } catch (Exception e) {
            e.printStackTrace();
            actonOnClose.run(s, e.getMessage());
        }
    }
	
	public static void actionOnSocketCloseForGetLogs(Socket s, String alert) {
		try {

			try {
				if (s != null) {
					s.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

			SwingUtilities.invokeLater(new Runnable() {

				@Override
				public void run() {
					main_frame.get_logs_btn.setEnabled(true);
					main_frame.clear_logs_btn.setEnabled(true);
					main_frame.small_frame_get_logs_btn.setEnabled(true);
					main_frame.get_logs_btn.setText("Get Logs");
					main_frame.small_frame_get_logs_btn.setText("Logs");
					//main_frame.logs_viewer_area.setCaretPosition(0);
					main_frame.pack();
				}
			});

			// at last
			try {
				if (alert != null && !alert.trim().isEmpty()) {
					AlertDialog.showAlertDialog(main_frame, alert.trim());
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	

	public static void clearLogsCallback(JLabel action_btn) {
		try {
			
			String ip_str = main_frame.ip_input.getText().trim();
			String port_str = main_frame.port_input.getText().trim();
			String filter = main_frame.logs_filter_input.getText().trim();
			
			if(!Utils.checkIPV4(ip_str)) {
				AlertDialog.showAlertDialog(main_frame, "Target Device IP is invalid, provide IPV4 only!");
				return;
			}
			
			if(!Utils.checkPort(port_str)) {
				AlertDialog.showAlertDialog(main_frame, "Invalid port, Port must be in range : " + Utils.MIN_SERVER_PORT + "-" + Utils.MAX_SEREVR_PORT);
				return;
			}
			
			if(filter.isEmpty()) {
				AlertDialog.showAlertDialog(main_frame, "No logs found for this filter to clear!");
				return;
			}
			
			main_frame.get_logs_btn.setEnabled(false);
			main_frame.clear_logs_btn.setEnabled(false);
			main_frame.small_frame_get_logs_btn.setEnabled(false);
			
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					try {

						InetAddress ia = null;
						try {
							ia = InetAddress.getByName(ip_str);// important
						} catch (Exception e) {
							e.printStackTrace();
							actionOnSocketCloseForClearLogs(null, "Target Device IP is invalid, provide IPV4 only!");
							return;
						}
						if (ia == null) {
							actionOnSocketCloseForClearLogs(null, "Target Device IP is invalid, provide IPV4 only!");
							return;
						}

						int port = Utils.SOCKET_DEFAULT_PORT;
						try {
							port = Integer.parseInt(port_str);
						} catch (Exception e) {
							e.printStackTrace();
							actionOnSocketCloseForClearLogs(null, "Invalid port, Port must be in range : "
									+ Utils.MIN_SERVER_PORT + "-" + Utils.MAX_SEREVR_PORT);
							return;
						}

						Socket s = null;
						try {

							SwingUtilities.invokeLater(new Runnable() {

								@Override
								public void run() {
									main_frame.clear_logs_btn.setText("Connecting");
								}
							});

							s = new Socket(ia, port);
							final Socket temp = s;
							try {
								new Thread(new Runnable() {
									
									@Override
									public void run() {
										try {
											synchronized (socket_store) {
												socket_store.addElement(temp);
											}
										} catch (Exception e2) {
											e2.printStackTrace();
										}
									}
								}).start();
							} catch (Exception e2) {
								e2.printStackTrace();
							}
							SwingUtilities.invokeLater(new Runnable() {

								@Override
								public void run() {
									main_frame.clear_logs_btn.setText("Connected");
								}
							});
							
							InputStream is = s.getInputStream();
							OutputStream os = s.getOutputStream();
							
							SwingUtilities.invokeLater(new Runnable() {

								@Override
								public void run() {
									main_frame.clear_logs_btn.setText("Processing");
								}
							});
							
							os.write(DataTransactionSignals.CLEAR_LOGS_SIGNAL);
							writeStringLineToSocket(s, os, filter, new SocketPasser() {
								
								@Override
								public void run(Socket s, String error) {
									actionOnSocketCloseForClearLogs(s, null);
								}
							});
							
							is.read();
							actionOnSocketCloseForClearLogs(s, "The specified logs are cleared successfully!");
							
							
						} catch (Exception e) {
							e.printStackTrace();
							actionOnSocketCloseForClearLogs(s, e.getMessage());
						}

					} catch (Exception e) {
						e.printStackTrace();
						actionOnSocketCloseForClearLogs(null, e.getMessage());
					}
				}
			}).start();
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void actionOnSocketCloseForClearLogs(Socket s, String alert) {
		try {
			
		    try {
				if (s != null) {
					s.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					main_frame.get_logs_btn.setEnabled(true);
					main_frame.clear_logs_btn.setEnabled(true);
					main_frame.small_frame_get_logs_btn.setEnabled(true);
					main_frame.clear_logs_btn.setText("Clear Logs");
					main_frame.pack();
				}
			});

			// at last
			try {
				if (alert != null && !alert.trim().isEmpty()) {
					AlertDialog.showAlertDialog(main_frame, alert.trim());
				}
			} catch (Exception e) {
				e.printStackTrace();
			}			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public static void callbackBeforeAppClosing() {
		try {
			
			String ip = main_frame.ip_input.getText().trim();
			String port = main_frame.port_input.getText().trim();
			String apk_file_path = main_frame.file_input.getText().trim();
			String logs_filter = main_frame.logs_filter_input.getText().trim();
			
			FilesData.setup();
			FilesData.writeFile(new File(FilesData.getDataRoot(), FilesData.FILE_IP_HOLDER), ip.trim(), false);
			FilesData.writeFile(new File(FilesData.getDataRoot(), FilesData.FILE_PORT_HOLDER), port.trim(), false);
			FilesData.writeFile(new File(FilesData.getDataRoot(), FilesData.FILE_APK_FILE_PATH_HOLDER), apk_file_path.trim(), false);
			FilesData.writeFile(new File(FilesData.getDataRoot(), FilesData.FILE_LOGS_FILTER_HOLDER), logs_filter.trim(), false);
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void addSocketToSocketStore(Socket s) {
		try {
			
			if(socket_store.size() >= socket_store_max_length) {
				socket_store.removeElementAt(0);
				socket_store.add(s);
			} else {
				socket_store.add(s);
			}
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void callbackAfterAppLaunching() {
		try {
			FilesData.setup();
			
			String ip = FilesData.readFile(new File(FilesData.getDataRoot(), FilesData.FILE_IP_HOLDER)).trim();
			String port = FilesData.readFile(new File(FilesData.getDataRoot(), FilesData.FILE_PORT_HOLDER)).trim();
			String apk_file_path = FilesData.readFile(new File(FilesData.getDataRoot(), FilesData.FILE_APK_FILE_PATH_HOLDER)).trim();
			String logs_filter = FilesData.readFile(new File(FilesData.getDataRoot(), FilesData.FILE_LOGS_FILTER_HOLDER)).trim();
			
			main_frame.ip_input.setText(ip);
			main_frame.port_input.setText(port);
			main_frame.file_input.setText(apk_file_path);
			main_frame.logs_filter_input.setText(logs_filter);
			main_frame.pack();
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static interface SocketPasser {
		public void run(Socket s, String error);
	}
}


















