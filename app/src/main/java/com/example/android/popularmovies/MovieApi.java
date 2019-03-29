package com.example.android.popularmovies;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

class MovieApi {

    private static final String TAG = "MovieApi";
    // TODO: Replace API_KEY with your own key
    private static final String API_KEY="YOUR KEY HERE";
    private String base_url = "";
    private final String mSortCriterion;
    static final String POPULARITY_SORT="popularity.desc";
    static final String TOP_RATED_SORT="vote_average.desc";

    MovieApi(final String sortCriterion) {
        mSortCriterion = sortCriterion;
    }

    private void getConfiguration() {
        if(base_url.equals("")) {
            try {
                String url = Uri.parse("https://api.themoviedb.org/3/configuration")
                        .buildUpon()
                        .appendQueryParameter("api_key", API_KEY)
                        .build().toString();
                Log.i(TAG, "Requesting configuration information");

                String jsonString = getUrlString(url);
                JSONObject jsonObject = new JSONObject(jsonString);
                JSONObject jsonImage = jsonObject.getJSONObject("images");
                base_url = jsonImage.getString("secure_base_url");
            }  catch (IOException ioe) {
              Log.e(TAG, "Failed to fetch JSON: " + ioe);
            } catch (JSONException je) {
                Log.e(TAG, "JSON Exception: " + je);
            }
        }
    }
    @NonNull
    List<MovieItem> fetchItems() {
        getConfiguration();
        List<MovieItem> items = new ArrayList<>();
        try {
            String url = Uri.parse("https://api.themoviedb.org/3/discover/movie")
                    .buildUpon()
                    .appendQueryParameter("sort_by", mSortCriterion)
                    .appendQueryParameter("api_key", API_KEY)
                    .appendQueryParameter("include_adult", "false")
                    .appendQueryParameter("include_video", "false")
                    .appendQueryParameter("language", "en-US")
                    .build().toString();

            Log.i(TAG, "Issuing request for: " + url);

            String jsonString = getUrlString(url);
            JSONObject jsonBody = new JSONObject(jsonString);



            parseItems(items, jsonBody);

        } catch (IOException ioe) {
            Log.e(TAG, "Failed to fetch JSON: " + ioe);
        } catch (JSONException je) {
            Log.e(TAG, "JSON Exception: " + je);
        }
        return items;
    }

    private void parseItems(@NonNull List<MovieItem> items, JSONObject jsonBody)
            throws JSONException
    {
        JSONArray photoJsonArray = jsonBody.getJSONArray("results");
        for(int i=0; i<photoJsonArray.length(); i++) {
            JSONObject movieJsonObject = (JSONObject)photoJsonArray.get(i);
            MovieItem item = new MovieItem();
            item.setId(movieJsonObject.getString("id"));
            item.setTitle(movieJsonObject.getString("title"));
            if(! movieJsonObject.has("poster_path")) {
                continue;
            }
            String file_size = "w185";
            item.setUrl(base_url + file_size + movieJsonObject.getString("poster_path"));
            item.setBigUrl(base_url + "w500" + movieJsonObject.getString("poster_path"));
            item.setOverview(movieJsonObject.getString("overview"));
            item.setVoteAverage(movieJsonObject.getString("vote_average"));
            item.setReleaseDate(movieJsonObject.getString("release_date"));
            items.add(item);
        }
    }

    /**
     * getUrlBytes was originally taken from the PhotoGallery application
     * from the bignerdranch Android programming guide. The application
     * is licensed under the Apache 2.0 license. This method is used
     * without alteration.
     *
     * @param urlSpec URL to retrieve data from
     * @return byte array of returned data from the URL
     * @throws IOException I/O error
     */
    private byte[] getUrlBytes(String urlSpec) throws IOException{
        URL url = new URL(urlSpec);
        HttpURLConnection connection = (HttpURLConnection)url.openConnection();
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            InputStream in = connection.getInputStream();

            if(connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                throw new IOException(connection.getResponseMessage() + ": with " + urlSpec);
            }

            int bytesRead;
            byte[] buffer = new byte[1024];
            while((bytesRead = in.read(buffer)) > 0) {
                out.write(buffer, 0, bytesRead);
            }
            out.close();
            return out.toByteArray();
        } finally {
            connection.disconnect();
        }
    }

    /**
     * getUrlString was taken from the bignerdranch Android programming guide.
     * The application is licensed under the Apache 2.0 license. This method
     * is used here without alteration.
     *
     * @param urlSpec URL to retrieve data from
     * @return JSON data from the URL
     * @throws IOException I/O error
     */
    private String getUrlString(String urlSpec) throws IOException {
        return new String(getUrlBytes(urlSpec));
    }
}
