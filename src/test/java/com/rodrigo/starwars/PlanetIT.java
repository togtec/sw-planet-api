package com.rodrigo.starwars;

import static com.rodrigo.starwars.common.PlanetConstants.PLANET;
import static com.rodrigo.starwars.common.PlanetConstants.TATOOINE;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.rodrigo.starwars.domain.Planet;


@ActiveProfiles("it")
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@Sql(scripts = {"/import_planets.sql"}, executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = {"/remove_planets.sql"}, executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
public class PlanetIT {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private WebTestClient webTestClient;

    @SuppressWarnings("null")
    @Test
    public void createPlanet_ReturnsCreated() {
        ResponseEntity<Planet> sut = restTemplate.postForEntity("/planets", PLANET, Planet.class);

        assertThat(sut.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(sut.getBody().getId()).isNotNull();
        assertThat(sut.getBody().getName()).isEqualTo(PLANET.getName());
        assertThat(sut.getBody().getClimate()).isEqualTo(PLANET.getClimate());
        assertThat(sut.getBody().getTerrain()).isEqualTo(PLANET.getTerrain());
    }    
    @SuppressWarnings("null")
    @Test
    public void createPlanet_ReturnsCreated2() {
      Planet sut = webTestClient.post().uri("/planets").bodyValue(PLANET)
        .exchange().expectStatus().isCreated().expectBody(Planet.class)
        .returnResult().getResponseBody();
     
      assertThat(sut.getId()).isNotNull();
      assertThat(sut.getName()).isEqualTo(PLANET.getName());
      assertThat(sut.getClimate()).isEqualTo(PLANET.getClimate());
      assertThat(sut.getTerrain()).isEqualTo(PLANET.getTerrain());
    }



    @Test
    public void getPlanetById_ReturnsPlanet() {
        ResponseEntity<Planet> sut = restTemplate.getForEntity("/planets/1", Planet.class);

        assertThat(sut.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(sut.getBody()).isEqualTo(TATOOINE);
    }
    @Test
    public void getPlanetById_ReturnsPlanet2() {
        Planet sut = webTestClient.get().uri("/planets/1")
            .exchange().expectStatus().isOk().expectBody(Planet.class)                       
            .returnResult().getResponseBody();
            
            assertThat(sut).isEqualTo(TATOOINE);
    }


    
    @Test
    public void getPlanetByName_ReturnsPlanet() {
        ResponseEntity<Planet> sut = restTemplate.getForEntity("/planets/name/" + TATOOINE.getName(), Planet.class);

        assertThat(sut.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(sut.getBody()).isEqualTo(TATOOINE);
    }
    @Test
    public void getPlanetByName_ReturnsPlanet2() {
        Planet sut = webTestClient.get().uri("/planets/name/" + TATOOINE.getName())
            .exchange().expectStatus().isOk().expectBody(Planet.class)
            .returnResult().getResponseBody();

            assertThat(sut).isEqualTo(TATOOINE);
    }



    @SuppressWarnings("null")
    @Test
    public void listPlanets_ReturnsAllPlanets() {
        ResponseEntity<Planet[]> sut = restTemplate.getForEntity("/planets", Planet[].class);

        assertThat(sut.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(sut.getBody()).hasSize(4);
        assertThat(sut.getBody()[0]).isEqualTo(TATOOINE);
    }
    @SuppressWarnings("null")
    @Test
    public void listPlanets_ReturnsAllPlanets2() {
        Planet[] sut = webTestClient.get().uri("/planets")
            .exchange().expectStatus().isOk().expectBody(Planet[].class)
            .returnResult().getResponseBody();

            assertThat(sut).hasSize(4);
            assertThat(sut[0]).isEqualTo(TATOOINE);
    }


    
    @SuppressWarnings("null")
    @Test
    public void listPlanets_ByClimate_ReturnsPlanets() {
        ResponseEntity<Planet[]> sut = restTemplate.getForEntity("/planets?climate=" + TATOOINE.getClimate(), Planet[].class);

        assertThat(sut.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(sut.getBody()).hasSize(1);
        assertThat(sut.getBody()[0]).isEqualTo(TATOOINE);
    }
    @SuppressWarnings("null")
    @Test
    public void listPlanets_ByClimate_ReturnsPlanets2() {
        Planet[] sut = webTestClient.get().uri("/planets?climate=" + TATOOINE.getClimate())
            .exchange().expectStatus().isOk().expectBody(Planet[].class)
            .returnResult().getResponseBody();

            assertThat(sut).hasSize(1);
            assertThat(sut[0]).isEqualTo(TATOOINE);
    }



    @SuppressWarnings("null")
    @Test
    public void listPlanets_ByTerrain_ReturnsPlanets() {
        ResponseEntity<Planet[]> sut = restTemplate.getForEntity("/planets?terrain=" + TATOOINE.getTerrain(), Planet[].class);

        assertThat(sut.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(sut.getBody()).hasSize(1);
        assertThat(sut.getBody()[0]).isEqualTo(TATOOINE);
    }
    @SuppressWarnings("null")
    @Test
    public void listPlanets_ByTerrain_ReturnsPlanets2() {
        Planet[] sut = webTestClient.get().uri("/planets?terrain=" + TATOOINE.getTerrain())
            .exchange().expectStatus().isOk().expectBody(Planet[].class)
            .returnResult().getResponseBody();

            assertThat(sut).hasSize(1);
            assertThat(sut[0]).isEqualTo(TATOOINE);
    }



    @Test
    public void removePlanet_ReturnsNoContent() {
        ResponseEntity<Void> sut = restTemplate.exchange("/planets/" + TATOOINE.getId(), HttpMethod.DELETE, null, Void.class);

        assertThat(sut.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }
    @Test
    public void removePlanet_ReturnsNoContent2() {
        webTestClient.delete().uri("/planets/" + TATOOINE.getId())
        .exchange().expectStatus().isNoContent();
    }

}
