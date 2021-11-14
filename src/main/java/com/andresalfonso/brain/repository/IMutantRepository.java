package com.andresalfonso.brain.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.andresalfonso.brain.model.DnaSubject;

public interface IMutantRepository extends JpaRepository<DnaSubject, Long> {

}
