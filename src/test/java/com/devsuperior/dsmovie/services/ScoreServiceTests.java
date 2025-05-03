package com.devsuperior.dsmovie.services;

import static org.mockito.ArgumentMatchers.any;

import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.devsuperior.dsmovie.dto.MovieDTO;
import com.devsuperior.dsmovie.dto.ScoreDTO;
import com.devsuperior.dsmovie.entities.MovieEntity;
import com.devsuperior.dsmovie.entities.ScoreEntity;
import com.devsuperior.dsmovie.entities.UserEntity;
import com.devsuperior.dsmovie.repositories.MovieRepository;
import com.devsuperior.dsmovie.repositories.ScoreRepository;
import com.devsuperior.dsmovie.services.exceptions.ResourceNotFoundException;
import com.devsuperior.dsmovie.tests.MovieFactory;
import com.devsuperior.dsmovie.tests.ScoreFactory;
import com.devsuperior.dsmovie.tests.UserFactory;

@ExtendWith(SpringExtension.class)
public class ScoreServiceTests {
	
	@InjectMocks
	private ScoreService service;
	
	private Long existingId, nonExistingId;
	private MovieEntity movie;
	private UserEntity user;
	private ScoreEntity score;
	private ScoreDTO scoreDTO,nonExistingScoreDTO;
	
	@Mock
	private ScoreRepository repository;
	
	@Mock
	private MovieRepository movieRepository;
	
	@Mock
	private UserService userService;
	
	@BeforeEach
	void setup() throws Exception {
		existingId = 1L;
		nonExistingId = 1000L;
		movie = MovieFactory.createMovieEntity();
		user = UserFactory.createUserEntity();
		score = ScoreFactory.createScoreEntity();
		scoreDTO = new ScoreDTO(score);
		nonExistingScoreDTO = new ScoreDTO(nonExistingId,4.0);
		Mockito.when(repository.saveAndFlush(any())).thenReturn(score);
		Mockito.when(movieRepository.save(any())).thenReturn(movie);
		Mockito.when(movieRepository.findById(existingId)).thenReturn(Optional.of(movie));
		Mockito.when(movieRepository.findById(nonExistingId)).thenReturn(Optional.empty());
	}
	
	@Test
	public void saveScoreShouldReturnMovieDTO() {
		Mockito.when(userService.authenticated()).thenReturn(user);
		MovieDTO result = service.saveScore(scoreDTO);
		Assertions.assertNotNull(result);
		Assertions.assertEquals(result.getTitle(), movie.getTitle());
	}
	
	@Test
	public void saveScoreShouldThrowResourceNotFoundExceptionWhenNonExistingMovieId() {
		Mockito.when(userService.authenticated()).thenReturn(user);
		
		Assertions.assertThrows(ResourceNotFoundException.class,()->{
			service.saveScore(nonExistingScoreDTO);
		});
	}
	
}
