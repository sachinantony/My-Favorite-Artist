package com.sachin.playground.MyFavoriteArtist.controller;

import com.sachin.playground.MyFavoriteArtist.service.DataManagementService;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

@RestController
public class ArtistController {

    @Autowired
    DataManagementService dataManagementService;

    @GetMapping(value = "/fav_artist", params = {"artist_name"})
    public ResponseEntity<?> getFavoriteArtistInfo(@RequestParam("artist_name") String artistName) throws Exception{

        JSONObject res = dataManagementService.getArtistInfo(artistName);
        return new ResponseEntity<>(res, HttpStatus.OK);
    }
}
