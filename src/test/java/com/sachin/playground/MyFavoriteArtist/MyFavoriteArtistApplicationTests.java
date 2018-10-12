package com.sachin.playground.MyFavoriteArtist;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static io.restassured.RestAssured.get;
import static org.hamcrest.CoreMatchers.equalTo;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MyFavoriteArtistApplicationTests {

	@Test
	public void inValidArtistTest() {
		get("/fav_artist?artist_name=Siadsdsdsdsdsdsd").then().statusCode(200).assertThat()
				.body("message", equalTo("No artist found"));
	}

	@Test
	public void topSongTest() {
		get("/fav_artist?artist_name=Sia").then().statusCode(200).assertThat()
				.body("top_geo_song", equalTo("Cheap Thrills"));
	}
}
