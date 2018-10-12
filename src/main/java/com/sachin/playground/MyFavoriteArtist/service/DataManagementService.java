package com.sachin.playground.MyFavoriteArtist.service;

import org.json.simple.JSONObject;

public interface DataManagementService {
    JSONObject getArtistInfo(String artistName) throws Exception;
}
