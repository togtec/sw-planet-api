package com.rodrigo.starwars.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;

import com.rodrigo.starwars.jacoco.ExcludeFromJacocoGeneratedReport;


@Entity
@Table(name = "planets")
public class Planet implements Serializable {
  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotEmpty
  @Column(nullable = false, unique = true)
  private String name;

  @NotEmpty
  @Column(nullable = false)
  private String climate;

  @NotEmpty
  @Column(nullable = false)
  private String terrain;

  public Planet() {
    super();
  }

  public Planet(String climate, String terrain) {
    this.climate = climate;
    this.terrain = terrain;
  }

  public Planet(String name, String climate, String terrain) {
    this.name = name;
    this.climate = climate;
    this.terrain = terrain;
  }

  public Planet(Long id, @NotEmpty String name, @NotEmpty String climate, @NotEmpty String terrain) {
    this.id = id;
    this.name = name;
    this.climate = climate;
    this.terrain = terrain;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getClimate() {
    return climate;
  }

  public void setClimate(String climate) {
    this.climate = climate;
  }

  public String getTerrain() {
    return terrain;
  }

  public void setTerrain(String terrain) {
    this.terrain = terrain;
  }

  @ExcludeFromJacocoGeneratedReport
  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((name == null) ? 0 : name.hashCode());
    return result;
  }

  @ExcludeFromJacocoGeneratedReport
  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    Planet other = (Planet) obj;
    if (name == null) {
      if (other.name != null)
        return false;
    } else if (!name.equals(other.name))
      return false;
    return true;
  }

  @Override
  public String toString() {
    return "Planet [id=" + id + ", name=" + name + ", climate=" + climate + ", terrain=" + terrain + "]";
  }

}
