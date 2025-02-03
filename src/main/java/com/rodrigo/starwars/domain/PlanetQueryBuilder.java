package com.rodrigo.starwars.domain;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;

public class PlanetQueryBuilder {
	private PlanetQueryBuilder() {}

	public static Example<Planet> makeQuery(Planet planet) {
		ExampleMatcher exampleMatcher = ExampleMatcher.matchingAll()
		.withIgnoreCase()
		.withIgnoreNullValues()
		.withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);
		return Example.of(planet, exampleMatcher);
	}

}
