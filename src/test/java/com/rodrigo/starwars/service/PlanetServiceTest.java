package com.rodrigo.starwars.service;

import static com.rodrigo.starwars.common.PlanetConstants.INVALID_PLANET;
import static com.rodrigo.starwars.common.PlanetConstants.PLANET;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Example;

import com.rodrigo.starwars.domain.Planet;
import com.rodrigo.starwars.domain.PlanetQueryBuilder;
import com.rodrigo.starwars.repository.PlanetRepository;


@ExtendWith(MockitoExtension.class)
public class PlanetServiceTest {
	@InjectMocks
	private PlanetService planetService;

	@Mock
	private PlanetRepository planetRepository;



	@Test
	public void createPlanet_WithValidData_ReturnsPlanet() {
		when(planetRepository.save(PLANET)).thenReturn(PLANET); // Arrange
		
		Planet sut = planetService.create(PLANET); // Act - sut means system under test
		
		assertThat(sut).isEqualTo(PLANET); // Assert
	}

	@Test
	public void createPlanet_WithInvalidData_ThrowsException() {
		when(planetRepository.save(INVALID_PLANET)).thenThrow(RuntimeException.class); //Arrange
		
		assertThatThrownBy(() -> planetService.create(INVALID_PLANET)).isInstanceOf(RuntimeException.class); //Act, Assert
	}

	@Test
	public void getPlanet_ByExistingId_ReturnsPlanet() {
		when(planetRepository.findById(anyLong())).thenReturn(Optional.of(PLANET)); //Arrange

		Optional<Planet> sut = planetService.get(1L); //Act

		//Assert
		assertThat(sut).isNotEmpty(); 
		assertThat(sut.get()).isEqualTo(PLANET);
	}

	@Test
	public void getPlanet_ByNotExistingId_ReturnsEmpty() {
		when(planetRepository.findById(1L)).thenReturn(Optional.empty()); //Arrange

		Optional<Planet> sut = planetService.get(1L); //Act

		assertThat(sut).isEmpty(); //Assert
	}

	@Test
	public void getPlanet_ByExistingName_ReturnsPlanet() {
		when(planetRepository.findByName(PLANET.getName())).thenReturn(Optional.of(PLANET)); //Arrange

		Optional<Planet> sut = planetService.getByName(PLANET.getName()); //Act

		//Assert
		assertThat(sut).isNotEmpty();
		assertThat(sut.get()).isEqualTo(PLANET);
	}

	@Test
	public void getPlanet_ByNotExistingName_ReturnsEmpty() {
		final String name = "Not Existing Name";
		when(planetRepository.findByName(name)).thenReturn(Optional.empty());	//Arrange

		Optional<Planet> sut = planetService.getByName(name);	//Act

		assertThat(sut).isEmpty();	//Assert
	}

	@Test
	public void listPlanets_ReturnsAllPlanets() {
		//Arrange
		List<Planet> planets = new ArrayList<>() {
			{
				add(PLANET);
			}
		};
		Example<Planet> query = PlanetQueryBuilder.makeQuery(new Planet(PLANET.getClimate(), PLANET.getTerrain()));
		when(planetRepository.findAll(query)).thenReturn(planets);

		//Act
		List<Planet> sut = planetService.list(PLANET.getClimate(), PLANET.getTerrain());

		//Assert
		assertThat(sut).isNotEmpty();
		assertThat(sut).hasSize(1);
		assertThat(sut.get(0)).isEqualTo(PLANET);	
	}

	@Test
	public void listPlanets_ReturnsNoPlanets() {				
		when(planetRepository.findAll(any())).thenReturn(Collections.emptyList()); //Arrange
		
		List<Planet> sut = planetService.list(PLANET.getClimate(), PLANET.getTerrain()); //Act
		
		assertThat(sut).isEmpty(); //Assert
	}

	@Test
	public void removePlanet_WithExistingId_doesNotThrowAnyException() {
		assertThatCode(() -> planetService.remove(1L)).doesNotThrowAnyException();		
	}

	@Test
	public void removePlanet_WithNotExistingId_ThrowsException() {
		doThrow(new RuntimeException()).when(planetRepository).deleteById(99L);

		assertThatThrownBy(() -> planetService.remove(99L)).isInstanceOf(RuntimeException.class);
	}
	
}
