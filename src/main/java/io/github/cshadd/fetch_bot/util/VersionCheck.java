package io.github.cshadd.fetch_bot.util;
import io.github.cshadd.fetch_bot.FetchBot;
import java.io.InputStream;
import java.io.IOException;
import java.net.URL;
import org.apache.commons.io.IOUtils;
import org.json.JSONException;
import org.json.JSONArray;
import org.json.JSONObject;

// Main
public class VersionCheck
implements FetchBot {
    // Private Constant Instance/Property Fields
    private static final int API_OBJECT = 0;
    private static final  String API_URL = "https://api.github.com/repos/cshadd/fetch-bot/releases";
    private static final  String API_VERSION_TAG = "tag_name";

    // Private Constructors
    private VersionCheck() { }

    // Public Static Methods
    public static String getCurrentVersion()
    throws VersionCheckException {
        String returnData = "v0.0.0";
        JSONArray api = null;
        JSONObject apiData = null;
        try {
            InputStream in = new URL(API_URL).openStream();
            api = new JSONArray(IOUtils.toString(in, "UTF-8"));
            apiData = api.getJSONObject(API_OBJECT);
            returnData = apiData.getString(API_VERSION_TAG);
        }
        catch (IOException e) {
            throw new VersionCheckException("There was an issue with IO!", e);
        }
        catch (JSONException e) {
            throw new VersionCheckException("There was an issue with JSON!", e);
        }
        catch (Exception e) {
            throw new VersionCheckException("There was an unknown issue!", e);
        }
        finally { }
        return returnData;
    }
}
