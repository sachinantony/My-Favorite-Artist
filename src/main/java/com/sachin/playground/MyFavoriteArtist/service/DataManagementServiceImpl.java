package com.sachin.playground.MyFavoriteArtist.service;

import com.fasterxml.jackson.annotation.JsonAlias;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import com.sachin.playground.MyFavoriteArtist.utils.StringLookup;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class DataManagementServiceImpl implements DataManagementService{

    private JSONObject responseJSON;

    @Autowired
    Environment env;


    public JSONObject getArtistInfo(String artistName){
        responseJSON = new JSONObject();
        UriComponentsBuilder builder = UriComponentsBuilder
                .fromUriString(StringLookup.ARTIST_INFO_URL)
                // Add query parameter
                .queryParam("artist", artistName)
                .queryParam("api_key", env.getProperty("lastfm.api.key"))
                .queryParam("format", "json");
        RestTemplate restTemplate = new RestTemplate();
        JSONObject artistInfo = restTemplate.getForObject(builder.toUriString(), JSONObject.class);
        if(artistInfo != null && artistInfo.get("artist") != null){
            Map<?,?> artistMap = (Map<?,?>)artistInfo.get("artist");
            Map<?,?> imageObj =(Map<?,?>)((List)artistMap.get("image")).get(5);
            responseJSON.put("name",artistMap.get("name"));
            responseJSON.put("image-url",imageObj.get("#text"));
            responseJSON.put("bio",((Map<Object,Object>)artistMap.get("bio")).get("summary"));
        }
        return responseJSON;
    }
}
