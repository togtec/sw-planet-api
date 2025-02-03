package com.rodrigo.starwars.repository;

import static com.rodrigo.starwars.common.PlanetConstants.PLANET;
import static com.rodrigo.starwars.common.PlanetConstants.TATOOINE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import javax.validation.ConstraintViolationException;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Example;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import com.rodrigo.starwars.domain.Planet;
import com.rodrigo.starwars.domain.PlanetQueryBuilder;

@ActiveProfiles("test-automated")
@DataJpaTest
public class PlanetRepositoryTest {
	@Autowired
	private PlanetRepository planetRepository;

	@Autowired
	private TestEntityManager testEntityManager;

  @AfterEach
  public void afterEach() {
    PLANET.setId(null);
  }

	@Test
	public void createPlanet_WithValidData_ReturnsPlanet() {
		Planet planet = planetRepository.save(PLANET);
		
		Planet sut = testEntityManager.find(Planet.class, planet.getId());
    System.out.println(sut);

		assertThat(sut).isNotNull();
		assertThat(sut.getName()).isEqualTo(PLANET.getName());
		assertThat(sut.getClimate()).isEqualTo(PLANET.getClimate());
		assertThat(sut.getTerrain()).isEqualTo(PLANET.getTerrain());
	}

  @ParameterizedTest
  @MethodSource("providesInvalidPlanets")
  public void createPlanet_WithInvalidData_ThrowsException(Planet planet) {    
    assertThatThrownBy(() -> planetRepository.save(planet)).isInstanceOf(ConstraintViolationException.class);
  }
  private static Stream<Arguments> providesInvalidPlanets() {
    return Stream.of(
      Arguments.of(new Planet(null, "climate", "terrain")),
      Arguments.of(new Planet("name", null, "terrain")),
      Arguments.of(new Planet("name", "climate", null)),
      Arguments.of(new Planet(null, null, "terrain")),
      Arguments.of(new Planet(null, "climate", null)),
      Arguments.of(new Planet("name", null, null)),
      Arguments.of(new Planet(null, null, null)),
      Arguments.of(new Planet("", "climate", "terrain")),
      Arguments.of(new Planet("name", "", "terrain")),
      Arguments.of(new Planet("name", "climate", "")),
      Arguments.of(new Planet("", "", "terrain")),
      Arguments.of(new Planet("", "climate", "")),
      Arguments.of(new Planet("name", "", "")),
      Arguments.of(new Planet("", "", ""))
    );
  }

  @Test
  public void createPlanet_WithExistingName_ThrowsException() {
    Planet planet = testEntityManager.persistFlushFind(PLANET);
    testEntityManager.detach(planet);
    planet.setId(null);
    
    assertThatThrownBy(() -> planetRepository.save(planet)).isInstanceOf(RuntimeException.class);
  }

  @Test
  public void getPlanet_ByExistingId_ReturnsPlanet() {
    Planet planet = testEntityManager.persistFlushFind(PLANET);

    Optional<Planet> sut = planetRepository.findById(planet.getId());

    assertThat(sut).isNotEmpty();
    assertThat(sut.get()).isEqualTo(planet);
  }

  @Test
  public void getPlanet_ByNotExistingId_ReturnsNotFound() {
    Optional<Planet> sut = planetRepository.findById(99L);

    assertThat(sut).isEmpty();
  }

  @Test
  public void getPlanet_ByExistingName_ReturnsPlanet() {
    Planet planet = testEntityManager.persistFlushFind(PLANET);

    Optional<Planet> sut = planetRepository.findByName(PLANET.getName());

    assertThat(sut).isNotEmpty();
    assertThat(sut.get()).isEqualTo(planet);
  }

  @Test
  public void getPlanet_ByNotExistingName_ReturnsNotFound() {
    Optional<Planet> sut = planetRepository.findByName("name");
    
    assertThat(sut).isEmpty();
  }

  @Sql(scripts = "/import_planets.sql")
  @Test
  public void listPlanets_ReturnsFilteredPlanets() {
    Example<Planet> queryWithoutFilters = PlanetQueryBuilder.makeQuery(new Planet());
    Example<Planet> queryWithFilters = PlanetQueryBuilder.makeQuery(new Planet(TATOOINE.getClimate(), TATOOINE.getTerrain()));

    List<Planet> responseWithoutFilters = planetRepository.findAll(queryWithoutFilters);
    List<Planet> responseWithFilters = planetRepository.findAll(queryWithFilters);

    assertThat(responseWithoutFilters).isNotEmpty();
    assertThat(responseWithoutFilters).hasSize(4);

    assertThat(responseWithFilters).isNotEmpty();
    assertThat(responseWithFilters).hasSize(1);
    assertThat(responseWithFilters.get(0)).isEqualTo(TATOOINE);
  }

  @Test
  public void listPlanets_ReturnsNoPlanets() throws Exception {
    Example<Planet> query = PlanetQueryBuilder.makeQuery(new Planet("non-existent-climate", "none"));

    List<Planet> response = planetRepository.findAll(query);

    assertThat(response).isEmpty();
  }

  @Test
  public void removePlanet_WithExistingID_RemovesPlanetFromDatabase() {
    Planet planet = testEntityManager.persistFlushFind(PLANET);

    planetRepository.deleteById(planet.getId());

    Planet removedPlanet = testEntityManager.find(Planet.class, planet.getId());
    assertThat(removedPlanet).isNull();
  }

  @Test
  public void removePlanet_WithUnexistingID_ThrowsException() {
    assertThatThrownBy(() -> planetRepository.deleteById(99L)).isInstanceOf(EmptyResultDataAccessException.class);
  }

}

