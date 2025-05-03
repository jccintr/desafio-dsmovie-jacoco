package com.devsuperior.dsmovie.services;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.devsuperior.dsmovie.entities.UserEntity;
import com.devsuperior.dsmovie.projections.UserDetailsProjection;
import com.devsuperior.dsmovie.repositories.UserRepository;
import com.devsuperior.dsmovie.tests.UserDetailsFactory;
import com.devsuperior.dsmovie.tests.UserFactory;
import com.devsuperior.dsmovie.utils.CustomUserUtil;

@ExtendWith(SpringExtension.class)
@ContextConfiguration
public class UserServiceTests {

	@InjectMocks
	private UserService service;
	
	private String existingUserName,nonExistingUserName;
	private UserEntity user;
	
	private List<UserDetailsProjection> userDetails;
	
	@Mock
	private UserRepository repository;
	
	@Mock
	private CustomUserUtil customUserUtil;
	
	@BeforeEach
	void setup() throws Exception {
		existingUserName = "maria@gmail.com";
		nonExistingUserName = "user@gmail.com";
		user = UserFactory.createUserEntity();
		userDetails = UserDetailsFactory.createCustomClientUser(existingUserName);
		Mockito.when(repository.searchUserAndRolesByUsername(existingUserName)).thenReturn(userDetails);
		Mockito.when(repository.findByUsername(existingUserName)).thenReturn(Optional.of(user));
		Mockito.when(repository.findByUsername(nonExistingUserName)).thenReturn(Optional.empty());
	}

	@Test
	public void authenticatedShouldReturnUserEntityWhenUserExists() {
		Mockito.when(customUserUtil.getLoggedUsername()).thenReturn(existingUserName);
		UserEntity result = service.authenticated();
		Assertions.assertNotNull(result);
		Assertions.assertEquals(result.getUsername(),existingUserName);
	}

	@Test
	public void authenticatedShouldThrowUsernameNotFoundExceptionWhenUserDoesNotExists() {
		Mockito.doThrow(ClassCastException.class).when(customUserUtil).getLoggedUsername();
		Assertions.assertThrows(UsernameNotFoundException.class,()->{
			service.authenticated();
		});
	}

	@Test
	public void loadUserByUsernameShouldReturnUserDetailsWhenUserExists() {
		UserDetails result = service.loadUserByUsername(existingUserName);
		Assertions.assertNotNull(result);
		Assertions.assertEquals(result.getUsername(),existingUserName);
	}

	@Test
	public void loadUserByUsernameShouldThrowUsernameNotFoundExceptionWhenUserDoesNotExists() {
		Assertions.assertThrows(UsernameNotFoundException.class,()->{
			service.loadUserByUsername(nonExistingUserName);
		});
	}


	
}
