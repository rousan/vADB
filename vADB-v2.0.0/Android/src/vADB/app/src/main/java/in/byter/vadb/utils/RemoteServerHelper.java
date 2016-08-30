package in.byter.vadb.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilderFactory;

/**
 * Created by Ariyan Khan on 19-04-2016.
 */
public class RemoteServerHelper {

    public static String RS_HOST = "byter.in";//https://members.000webhost.com/panel
    public static String RS_PROTOCOL = "http";
    public static int RS_PORT = 80;

    public static URL getAddressOfAppUpdateHandler() {
        try {
            return new URL(RS_PROTOCOL + "://" + RS_HOST + ":" + RS_PORT + "/applications/vADB/" + AppInfo.APP_VERSION_CODE + "/handlers/app_update_check_handler.php");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    public static URL getAddressOfUserFeedbackSubmitHandler() {
        try {
            return new URL(RS_PROTOCOL + "://" + RS_HOST + ":" + RS_PORT + "/applications/vADB/" + AppInfo.APP_VERSION_CODE + "/handlers/submit_user_feedbacks_handler.php");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }



    public static URL getAddressOfDesktopAppDownloadLink() {
        try {
            return new URL(RS_PROTOCOL + "://" + RS_HOST + ":" + RS_PORT + "/applications/vADB/web/index.php");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean isNetworkOrInternetConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        if(networkInfo == null) {
            return false;
        } else {
            return networkInfo.isConnected();
        }
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

        } catch (Exception e) {
            e.printStackTrace();
            outputs.put("STATUS", "0");
            outputs.put("REASON", "Server Internal Error Occurred or request timed out");
            outputs.put("OUTPUTS", "");
        }
        return outputs;
    }


    //don't run in UI thread
    public static HashMap<String, String> checkAppUpdate() {
        HashMap<String, String> outs = new HashMap<>();
        try {

            HashMap<String, String> outs_server = processingURL(getAddressOfAppUpdateHandler(), "POST", "");
            if (outs_server.get("STATUS").equals("1")) {
                String outs_xml = outs_server.get("OUTPUTS").trim();
                if (outs_xml.isEmpty()) {
                    outs.put("STATUS", "0");
                    outs.put("REASON", "Internal Problem Occurred");
                    return outs;
                }
                outs_xml = "<?xml version=\"1.0\" encoding=\"utf-8\"?> " + "<ROOT> " + outs_xml + " </ROOT>";
                Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new InputSource(new StringReader(outs_xml)));
                Element root = doc.getDocumentElement();
                Element OUTPUTS_ELEMENT = null;
                NodeList nl = root.getChildNodes();
                for (int i=0; i<nl.getLength(); i++) {
                    Node node = nl.item(i);
                    if (node.getNodeType() == Node.ELEMENT_NODE) {
                        Element node_E = (Element)node;
                        if (node_E.getTagName().toLowerCase().equals("OUTPUTS".toLowerCase())) {
                            OUTPUTS_ELEMENT = node_E;
                            break;
                        }
                    }
                }
                if (OUTPUTS_ELEMENT == null)
                    throw new Exception("Outputs Element is not found in XML");
                String status = OUTPUTS_ELEMENT.getElementsByTagName("STATUS").item(0).getTextContent().trim().toLowerCase();
                if (status.equals("1")) {
                    outs.put("STATUS", "1");
                    outs.put("REASON", "");
                    outs.put("RESTRICTION", OUTPUTS_ELEMENT.getElementsByTagName("RESTRICTION").item(0).getTextContent());
                    outs.put("VERSIONCODE", OUTPUTS_ELEMENT.getElementsByTagName("VERSIONCODE").item(0).getTextContent());
                    outs.put("VERSIONNAME", OUTPUTS_ELEMENT.getElementsByTagName("VERSIONNAME").item(0).getTextContent());
                    outs.put("SIZE", OUTPUTS_ELEMENT.getElementsByTagName("SIZE").item(0).getTextContent());
                    outs.put("DOWNLOADLINK", OUTPUTS_ELEMENT.getElementsByTagName("DOWNLOADLINK").item(0).getTextContent());
                    outs.put("EXTRADATAHTMLURL", OUTPUTS_ELEMENT.getElementsByTagName("EXTRADATAHTMLURL").item(0).getTextContent());
                    outs.put("DESCRIPTION", OUTPUTS_ELEMENT.getElementsByTagName("DESCRIPTION").item(0).getTextContent());
                    return outs;
                } else {
                    outs.put("STATUS", "0");
                    outs.put("REASON", OUTPUTS_ELEMENT.getElementsByTagName("REASON").item(0).getTextContent());
                    return outs;
                }
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


    //don't run in UI thread
    public static HashMap<String, String> submitUserFeedback(String email, String messages) {
        HashMap<String, String> outs = new HashMap<>();
        try {

            String data = "email=" + URLEncoder.encode(email, "utf-8") + "&messages=" +  URLEncoder.encode(messages, "utf-8");
            HashMap<String, String> outs_server = processingURL(getAddressOfAppUpdateHandler(), "POST", data);
            if (outs_server.get("STATUS").equals("1")) {
                String outs_xml = outs_server.get("OUTPUTS").trim();
                if (outs_xml.isEmpty()) {
                    outs.put("STATUS", "0");
                    outs.put("REASON", "Internal Problem Occurred");
                    return outs;
                }
                outs_xml = "<?xml version=\"1.0\" encoding=\"utf-8\"?> " + "<ROOT> " + outs_xml + " </ROOT>";
                Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new InputSource(new StringReader(outs_xml)));
                Element root = doc.getDocumentElement();
                Element OUTPUTS_ELEMENT = null;
                NodeList nl = root.getChildNodes();
                for (int i=0; i<nl.getLength(); i++) {
                    Node node = nl.item(i);
                    if (node.getNodeType() == Node.ELEMENT_NODE) {
                        Element node_E = (Element)node;
                        if (node_E.getTagName().toLowerCase().equals("OUTPUTS".toLowerCase())) {
                            OUTPUTS_ELEMENT = node_E;
                            break;
                        }
                    }
                }
                if (OUTPUTS_ELEMENT == null)
                    throw new Exception("Outputs Element is not found in XML");
                String status = OUTPUTS_ELEMENT.getElementsByTagName("STATUS").item(0).getTextContent().trim().toLowerCase();
                if (status.equals("1")) {
                    outs.put("STATUS", "1");
                    outs.put("REASON", "");
                    return outs;
                } else {
                    outs.put("STATUS", "0");
                    outs.put("REASON", OUTPUTS_ELEMENT.getElementsByTagName("REASON").item(0).getTextContent());
                    return outs;
                }
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




































