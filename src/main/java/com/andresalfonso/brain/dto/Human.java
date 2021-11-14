package com.andresalfonso.brain.dto;

import java.util.List;

import javax.validation.constraints.Pattern;

import lombok.Data;

@Data
public class Human {
	private List<@Pattern(regexp = "[atcgATCG]+$", message = "nitrogenous base not allowed") String> dna;
}
