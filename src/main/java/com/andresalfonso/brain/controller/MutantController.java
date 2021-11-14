package com.andresalfonso.brain.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.andresalfonso.brain.dto.Human;
import com.andresalfonso.brain.dto.Statistics;
import com.andresalfonso.brain.service.IMutantService;

@RestController
@RequestMapping(path = "/api")
public class MutantController {

	@Autowired
	private IMutantService mutantService;

	@PostMapping(value = "/mutant")
	public ResponseEntity<String> isMutant(@Valid @RequestBody Human human, final BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			return new ResponseEntity<>(bindingResult.getFieldErrors().get(0).getDefaultMessage(),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return mutantService.searchMutant(human.getDna());
	}

	@GetMapping(value = "/stats")
	public ResponseEntity<Statistics> stats() {
		return mutantService.stats();
	}
}