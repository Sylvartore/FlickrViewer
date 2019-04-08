package ass2.bcit.ca;

import android.util.Log;

import com.google.gson.Gson;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class HttpHandler {
    private static final String TAG = HttpHandler.class.getSimpleName();

    public HttpHandler() {
    }

    public List<Item> makeServiceCall(String reqUrl) {
        List<Item> response = new ArrayList<>();
        try {
            URL url = new URL(reqUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            // read the response
            InputStream in = new BufferedInputStream(conn.getInputStream());
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            StringBuilder sb = new StringBuilder();
            String line;
            Gson parser = new Gson();
            try {
                for (int c = 0, i = 0; (line = reader.readLine()) != null; i++) {
                    if (i <= 6) continue;
                    if (c == 0) {
                        sb = new StringBuilder();
                        sb.append(line).append("\n");
                        c++;
                    } else if (line.contains("media")) {
                        line = line.replace("{\"m\":", "");
                        line = line.replace("}", "");
                        sb.append(line).append("\n");
                        c++;
                    } else if (c == 10) {
                        if (line.charAt(line.length() - 1) == ',')
                            line = line.substring(0, line.length() - 1);
                        sb.append(line);
                        Item item = parser.fromJson(sb.toString(), Item.class);
                        response.add(item);
                        c = 0;
                    } else {
                        sb.append(line).append("\n");
                        c++;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (MalformedURLException e) {
            Log.e(TAG, "MalformedURLException: " + e.getMessage());
        } catch (ProtocolException e) {
            Log.e(TAG, "ProtocolException: " + e.getMessage());
        } catch (IOException e) {
            Log.e(TAG, "IOException: " + e.getMessage());
        } catch (Exception e) {
            Log.e(TAG, "Exception: " + e.getMessage());
        }
        return response;
    }
}
