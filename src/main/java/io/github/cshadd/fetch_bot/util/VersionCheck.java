/*
 * MIT License
 * 
 * Copyright (c) 2018 Christian Shadd
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 * 
 * https://cshadd.github.io/fetch-bot/
 */
package io.github.cshadd.fetch_bot.util;

import io.github.cshadd.fetch_bot.Component;
import io.github.cshadd.fetch_bot.FetchBot;
import io.github.cshadd.fetch_bot.References;

import java.io.InputStream;
import java.io.IOException;
import java.net.URL;
import org.apache.commons.io.IOUtils;
import org.json.JSONException;
import org.json.JSONArray;
import org.json.JSONObject;

// Main

/**
 * The Class VersionCheck. Version Check checks for an update on the API.
 * 
 * @author Christian Shadd
 * @author Maria Verna Aquino
 * @author Thanh Vu
 * @author Joseph Damian
 * @author Giovanni Orozco
 * @since 1.0.0
 */
@Component("Util")
public class VersionCheck implements FetchBot {
    // Private Constant Instance/Property Fields
    
    /**
     * The Constant API_OBJECT_ID.
     */
    private static final int API_OBJECT_ID = 0;
    
    /**
     * The Constant API_URL.
     */
    private static final String API_URL = "https://api.github.com/repos/cshadd/fetch-bot/releases";
    
    /**
     * The Constant API_VERSION_TAG.
     */
    private static final String API_VERSION_TAG = "tag_name";
    
    // Private Constructors
    
    /**
     * Instantiates a new Version Check.
     */
    private VersionCheck() {
    }
    
    // Public Static Methods
    
    /**
     * Gets the current version.
     *
     * @return the current version
     * @throws VersionCheckException
     *             if the version check failed
     */
    public static String getCurrentVersion() throws VersionCheckException {
        String returnData = "v0.0.0";
        JSONArray api = null;
        JSONObject apiData = null;
        try {
            try (final InputStream in = new URL(API_URL).openStream()) {
                api = new JSONArray(IOUtils.toString(in, "UTF-8"));
                apiData = api.getJSONObject(API_OBJECT_ID);
                returnData = apiData.getString(API_VERSION_TAG);
            } finally {
                /* */ }
        } catch (IOException e) {
            throw new VersionCheckException("Could not read " + API_URL + ".",
                            e);
        } catch (JSONException e) {
            throw new VersionCheckException("Could not parse JSON in " + API_URL
                            + ".", e);
        } catch (Exception e) {
            throw new VersionCheckException("Unknown issue.", e);
        } finally {
            /* */ }
        return returnData;
    }
    
    /**
     * Verifies that the version is not mismatched to the current version of the
     * API.
     *
     * @param version
     *            the version
     * @return true, if successful
     * @throws VersionCheckException
     *             if the version check failed
     */
    public static boolean verify(String version) throws VersionCheckException {
        if (References.CUSTOM_PROGRAM) { return (getCurrentVersion().equals(
                        version)); }
        return true;
    }
}
