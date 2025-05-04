package com.devsuperior.dsmovie.tests;

import com.devsuperior.dsmovie.dto.MovieDTO;
import com.devsuperior.dsmovie.entities.MovieEntity;
import com.devsuperior.dsmovie.entities.ScoreEntity;
import com.devsuperior.dsmovie.entities.UserEntity;

public class MovieFactory {
	
	public static MovieEntity createMovieEntity() {
		MovieEntity movie = new MovieEntity(1L, "Test Movie", 0.0, 0, "https://www.themoviedb.org/t/p/w533_and_h300_bestv2/jBJWaqoSCiARWtfV0GlqHrcdidd.jpg");
		
		UserEntity user = UserFactory.createUserEntity();
		ScoreEntity score = new ScoreEntity();
		score.setMovie(movie);
		score.setUser(user);
		score.setValue(3.5);
		movie.getScores().add(score);
		return movie;
	}
	
	public static MovieDTO createMovieDTO() {
		MovieEntity movie = createMovieEntity();
		return new MovieDTO(movie);
	}
}
