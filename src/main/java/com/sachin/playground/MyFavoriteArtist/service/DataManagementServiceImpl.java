package com.sachin.playground.MyFavoriteArtist.service;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import com.sachin.playground.MyFavoriteArtist.utils.StringLookup;
import java.util.List;
import java.util.Map;

@Service
public class DataManagementServiceImpl implements DataManagementService{

    private JSONObject responseJSON;
    private UriComponentsBuilder builder;

    @Autowired
    Environment env;


    public JSONObject getArtistInfo(String artistName) throws Exception{
        responseJSON = new JSONObject();
        artistName = artistName.replaceAll("\\s+","+");

        builder = UriComponentsBuilder
                .fromUriString(StringLookup.ARTIST_INFO_URL)
                .queryParam("artist", artistName).encode()
                .queryParam("api_key", env.getProperty("lastfm.api.key"));
        RestTemplate restTemplate = new RestTemplate();
        JSONObject artistInfo = restTemplate.getForObject(builder.toUriString(), JSONObject.class);

        if(artistInfo != null && artistInfo.get("artist") != null){
            Map<?,?> artistMap = (Map<?,?>)artistInfo.get("artist");
            artistName = (String)artistMap.get("name");
            Map<?,?> imageObj =(Map<?,?>)((List)artistMap.get("image")).get(5);
            responseJSON.put("name",artistMap.get("name"));
            responseJSON.put("image-url",imageObj.get("#text"));
            responseJSON.put("info",((Map<?,?>)artistMap.get("bio")).get("summary"));
            getTopSong(artistName);
        }
        else{
            responseJSON.put("message","No artist found");
        }
        return responseJSON;
    }

    private void getTopSong(String artistName) throws Exception{
        builder = UriComponentsBuilder
                .fromUriString(StringLookup.TOP_SONGS_URL)
                .queryParam("api_key", env.getProperty("lastfm.api.key"));
        RestTemplate restTemplate = new RestTemplate();
        JSONObject topSongs = restTemplate.getForObject(builder.toUriString(), JSONObject.class);

        List<?> tracks = (List<?>)((Map<?,?>)topSongs.get("tracks")).get("track");
        for(Object track : tracks){
            Map<?,?> trackMap = (Map<?,?>)track;
            if((((Map<?,?>)trackMap.get("artist")).get("name")).equals(artistName)){
                responseJSON.put("top_geo_song",trackMap.get("name"));
                responseJSON.put("top_song_lyrics",getSongLyrics((String)trackMap.get("name"),artistName));
                return;
            }
        }
        responseJSON.put("top_geo_song","No song in top 100 list");
    }

    private String getSongLyrics(String songName,String artistName) throws Exception{
        builder = UriComponentsBuilder
                .fromUriString(StringLookup.LYRICS_URL)
                .queryParam("q_track",songName)
                .queryParam("q_artist",artistName)
                .queryParam("apikey", env.getProperty("musixmatch.api.key"));
        RestTemplate restTemplate = new RestTemplate();
        JSONParser parser = new JSONParser();
        JSONObject apiRes = (JSONObject)parser.parse(restTemplate.getForObject(builder.toUriString(), String.class));

        return (String)((Map<?,?>)((Map<?,?>)((Map<?,?>)apiRes.get("message")).get("body")).get("lyrics")).get("lyrics_body");
    }
}
