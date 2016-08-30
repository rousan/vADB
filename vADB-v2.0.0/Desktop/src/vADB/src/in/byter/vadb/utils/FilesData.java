package in.byter.vadb.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;

public class FilesData {
	
	public static String FOLDER_ROOT_DIR = "vADBJVFGF8GIWGYEFYGJVGFVHG";
	public static String FILE_IP_HOLDER = "ip.cache";
	public static String FILE_PORT_HOLDER = "port.cache";
	public static String FILE_APK_FILE_PATH_HOLDER = "apk_file_path.cache";
	public static String FILE_LOGS_FILTER_HOLDER = "logs_filter.cache";
	
	public static File getDataRoot() {
		File f = new File("");//current dir
		try {
			
			String tmp_dir_path = System.getProperty("java.io.tmpdir");
			if(tmp_dir_path != null) {
				f = new File(tmp_dir_path.trim() + File.separator + FOLDER_ROOT_DIR);
			}
			
		} catch(Exception e) {
			e.printStackTrace();
		}
		return f;
	}
	
	public static void setup() {
		try {

			File folder_root = getDataRoot();
			if (!folder_root.isDirectory()) {
				folder_root.mkdirs();
			}

			File ip_cache = new File(folder_root, FILE_IP_HOLDER);
			File port_cache = new File(folder_root, FILE_PORT_HOLDER);
			File apk_file_path_cache = new File(folder_root, FILE_APK_FILE_PATH_HOLDER);
			File logs_filter_cache = new File(folder_root, FILE_LOGS_FILTER_HOLDER);

			if (!ip_cache.isFile()) {
				ip_cache.createNewFile();
			}
			if (!port_cache.isFile()) {
				port_cache.createNewFile();
			}
			if (!apk_file_path_cache.isFile()) {
				apk_file_path_cache.createNewFile();
			}
			if (!logs_filter_cache.isFile()) {
				logs_filter_cache.createNewFile();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void writeFile(File file, String str, boolean append) {
        try {

            FileOutputStream fos = new FileOutputStream(file, append);
            fos.write(str.getBytes("UTF-8"));
            fos.flush();
            fos.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String readFile(File file) {
        String contents = "";
        try {

            FileInputStream fis = new FileInputStream(file);
            BufferedReader bfr = new BufferedReader(new InputStreamReader(fis, "UTF-8"));
            String line = null;
            while((line = bfr.readLine()) != null) {
                contents += line;
            }
            bfr.close();

        } catch (Exception e) {
           e.printStackTrace();
        }
        return contents;
    }
 
	
}





