package com.rodrigo.starwars.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import com.rodrigo.starwars.domain.Planet;
import com.rodrigo.starwars.domain.PlanetQueryBuilder;
import com.rodrigo.starwars.repository.PlanetRepository;

@Service
public class PlanetService {
	private final PlanetRepository planetRepository;

	public PlanetService(PlanetRepository planetRepository) {
		this.planetRepository = planetRepository;
	}

	public Planet create(Planet planet) {
		return planetRepository.save(planet);
	}

	public Optional<Planet> get(Long id) {
		return planetRepository.findById(id);
	}

	public Optional<Planet> getByName(String name) {
		return planetRepository.findByName(name);
	}

	public List<Planet> list(String climate, String terrain) {
		Example<Planet> query = PlanetQueryBuilder.makeQuery(new Planet(climate, terrain));
		return planetRepository.findAll(query);
	}

	public void remove(Long id) {
		planetRepository.deleteById(id);
	}

}
