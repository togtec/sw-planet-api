package com.rodrigo.starwars.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.rodrigo.starwars.domain.Planet;
import com.rodrigo.starwars.service.PlanetService;

@RestController
public class PlanetController {
  private final PlanetService planetService;

  public PlanetController(PlanetService planetService) {
    this.planetService = planetService;
  }

  @PostMapping("/planets")
  public ResponseEntity<Planet> create(@RequestBody @Valid Planet planet) {
    Planet planetCreated = planetService.create(planet);
    return ResponseEntity.status(HttpStatus.CREATED).body(planetCreated);
  }

  @GetMapping("/planets/{id}")
  public ResponseEntity<Planet> getById(@PathVariable Long id) {
    return planetService.get(id).map(planet -> ResponseEntity.ok(planet))
        .orElseGet(() -> ResponseEntity.notFound().build());
  }

  @GetMapping("/planets/name/{name}")
  public ResponseEntity<Planet> getByName(@PathVariable String name) {
    return planetService.getByName(name).map(planet -> ResponseEntity.ok(planet))
        .orElseGet(() -> ResponseEntity.notFound().build());
  }

  @GetMapping("/planets")
  public ResponseEntity<List<Planet>> list(@RequestParam(required = false) String climate,
      @RequestParam(required = false) String terrain) {
    List<Planet> planets = planetService.list(climate, terrain);
    return ResponseEntity.ok(planets);
  }

  @DeleteMapping("/planets/{id}")
  public ResponseEntity<Void> remove(@PathVariable Long id) {
    planetService.remove(id);
    return ResponseEntity.noContent().build();
  }
  
}
