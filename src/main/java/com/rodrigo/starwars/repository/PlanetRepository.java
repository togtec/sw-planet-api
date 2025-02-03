package com.rodrigo.starwars.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Example;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.QueryByExampleExecutor;

import com.rodrigo.starwars.domain.Planet;

public interface PlanetRepository extends CrudRepository<Planet, Long>, QueryByExampleExecutor<Planet> {
	Optional<Planet> findByName(String name);

	@Override
	<S extends Planet> List<S> findAll(Example<S> example);	
}
