package com.andresalfonso.brain.service;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.andresalfonso.brain.dto.Statistics;

public interface IMutantService {

	ResponseEntity<String> searchMutant(List<String> dna);

	ResponseEntity<Statistics> stats();

}
