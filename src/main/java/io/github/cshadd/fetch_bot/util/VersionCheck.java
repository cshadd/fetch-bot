package io.github.cshadd.fetch_bot.util;
import io.github.cshadd.fetch_bot.FetchBot;
import java.io.InputStream;
import java.io.IOException;
import java.net.URL;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.json.JSONException;
import org.json.JSONArray;
import org.json.JSONObject;

// Main
public final class VersionCheck
implements FetchBot {
    // Private Constant Instance/Property Fields
    private static final int API_OBJECT = 0;
    private static final String API_URL = "https://api.github.com/repos/cshadd/fetch-bot/releases";
    private static final String API_VERSION_TAG = "tag_name";

    // Private Constructors
    private VersionCheck() { }

    // Public Static Final Methods
    public static final void checkVersionMatch(String version) {
        JSONArray api = null;
        JSONObject apiData = null;
        String currentVersion = "";
        try {
            final InputStream in = new URL(API_URL).openStream();
            api = new JSONArray(IOUtils.toString(in, "UTF-8"));
            apiData = api.getJSONObject(API_OBJECT);
            currentVersion = apiData.getString(API_VERSION_TAG);
            if (!version.equals(currentVersion)) {
                Logger.warn("VersionCheck - [Version] mismatch (this: " + version + "; current: " + currentVersion + "), this version might be outdated!");
            }
        }
        catch (IOException e) {
            Logger.fatalError(e, "There was an issue with IO!");
        }
        catch (JSONException e) {
            Logger.fatalError(e, "There was an issue with JSON!");
        }
        catch (Exception e) {
            Logger.fatalError(e, "There was an unknown issue!");
        }
        finally { }
    }
}