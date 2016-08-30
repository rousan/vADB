package in.byter.vadb.utils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;

public class RemoteServerHelper {
	
	 public static String RS_HOST = "byter.in";//https://members.000webhost.com/panel
	 public static String RS_PROTOCOL = "http";
	 public static int RS_PORT = 80;
	
	public static URL getAddressOfAppUpdaterHandler() {
        try {
            return new URL(RS_PROTOCOL + "://" + RS_HOST + ":" + RS_PORT + "/applications/vADB/desktop_version/handlers/latest_app_info.xml");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
	
	public static HashMap<String, String> processingURL(URL url, String req_type, String data) {
        HashMap<String, String> outputs = new HashMap<String, String>();
        try {
            HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
            httpURLConnection.setRequestMethod(req_type.trim().toUpperCase());
            httpURLConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            httpURLConnection.setRequestProperty("Content-Length", "" + Integer.toString(data.getBytes("UTF-8").length));
            httpURLConnection.setRequestProperty("Connection", "keep-alive");
            httpURLConnection.setDoInput(true);
            httpURLConnection.setDoOutput(true);

            OutputStream os = httpURLConnection.getOutputStream();
            os.write(data.getBytes("UTF-8"));
            os.flush();
            try {
                os.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

            InputStream is = httpURLConnection.getInputStream();
            BufferedReader bfr = new BufferedReader(new InputStreamReader(is));
            String outs = "";//important
            String line_data = "";
            while ((line_data=bfr.readLine()) != null) {
                outs += line_data + "\n";
            }
            try {
                is.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

            outputs.put("STATUS", "1");
            outputs.put("REASON", "");
            outputs.put("OUTPUTS", outs);

        } catch(UnknownHostException e) {
        	e.printStackTrace();
            outputs.put("STATUS", "0");
            outputs.put("REASON", "No internet connection available");
            outputs.put("OUTPUTS", "");
        } catch (Exception e) {
            e.printStackTrace();
            outputs.put("STATUS", "0");
            outputs.put("REASON", "Server Internal Error Occurred or request timed out");
            outputs.put("OUTPUTS", "");
        }
        return outputs;
    }
	
	public static HashMap<String, String> checkAppUpdate() {
		HashMap<String, String> outs = new HashMap<>();
		try {

			HashMap<String, String> outs_server = processingURL(getAddressOfAppUpdaterHandler(), "POST", "");
			if (outs_server.get("STATUS").equals("1")) {
				String outs_xml = outs_server.get("OUTPUTS").trim();
				if (outs_xml.isEmpty()) {
					outs.put("STATUS", "0");
					outs.put("REASON", "Server Internal Error Occurred or request timed out");
					return outs;
				}
				outs_xml = "<?xml version=\"1.0\" encoding=\"utf-8\"?> " + "<ROOT> " + outs_xml + " </ROOT>";
				Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder()
						.parse(new InputSource(new StringReader(outs_xml)));
				outs.put("STATUS", "1");
				outs.put("REASON", "");
				outs.put("VERSIONCODE", doc.getElementsByTagName("VERSIONCODE").item(0).getTextContent());
				outs.put("VERSIONNAME", doc.getElementsByTagName("VERSIONNAME").item(0).getTextContent());
				outs.put("SIZE", doc.getElementsByTagName("SIZE").item(0).getTextContent());
				outs.put("DOWNLOADLINK", doc.getElementsByTagName("DOWNLOADLINK").item(0).getTextContent());
				return outs;
			} else {
				outs.put("STATUS", "0");
				outs.put("REASON", outs_server.get("REASON"));
			}

		} catch (Exception e) {
			e.printStackTrace();
			outs.put("STATUS", "0");
			outs.put("REASON", "Server Internal Error Occurred or request timed out");
		}
		return outs;
	}
	
}






















