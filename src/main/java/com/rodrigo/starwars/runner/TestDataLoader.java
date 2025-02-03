package com.rodrigo.starwars.runner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import com.rodrigo.starwars.domain.Planet;
import com.rodrigo.starwars.repository.PlanetRepository;

@Component
@Profile("testmanual")
public class TestDataLoader implements CommandLineRunner {    
    private Logger logger = LoggerFactory.getLogger(TestDataLoader.class);
    private final PlanetRepository repository;

    public TestDataLoader(PlanetRepository repository) {
        this.repository = repository;
    }

    @Override
    public void run(String... args) throws Exception {
        Planet planet1 = new Planet("Tatooine", "arid", "desert");
        Planet planet2 = new Planet("Alderaan", "temperate", "grasslands, mountains");
        Planet planet3 = new Planet("Yavin IV", "temperate, tropical", "jungle, rainforests");

        repository.save(planet1);
        repository.save(planet2);
        repository.save(planet3);

        logger.info("Test data loaded into database!");
    }
    
}
