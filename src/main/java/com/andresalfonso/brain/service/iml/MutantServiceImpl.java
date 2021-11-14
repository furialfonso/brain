package com.andresalfonso.brain.service.iml;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.andresalfonso.brain.dto.Statistics;
import com.andresalfonso.brain.model.DnaSubject;
import com.andresalfonso.brain.repository.IMutantRepository;
import com.andresalfonso.brain.service.IMutantService;

@Service
public class MutantServiceImpl implements IMutantService {
	private static final Logger LOGGER = LoggerFactory.getLogger(MutantServiceImpl.class);

	@Autowired
	private IMutantRepository mutantRepository;

	@Override
	public ResponseEntity<String> searchMutant(List<String> dna) {
		LOGGER.info("dna:{}", dna);

		boolean isValidDna = dna.stream().allMatch(d -> {
			if (dna.size() != d.length()) {
				return false;
			}
			return true;
		});

		if (Boolean.FALSE.equals(isValidDna)) {
			return new ResponseEntity<>("The number of rows is different from the number of columns",
					HttpStatus.INTERNAL_SERVER_ERROR);
		}

		char[][] matriz = new char[dna.size()][dna.size()];
		for (int i = 0; i < dna.size(); i++) {
			for (int j = 0; j < dna.size(); j++) {
				matriz[i][j] = dna.get(i).toCharArray()[j];
			}
		}

		List<String> combinations = new ArrayList<String>();
		for (int i = 0; i < matriz.length; i++) {
			StringBuilder column = new StringBuilder();
			StringBuilder row = new StringBuilder();
			for (int j = 0; j < matriz.length; j++) {
				column.append(matriz[i][j]);
				row.append(matriz[j][i]);
			}
			combinations.add(column.toString());
			combinations.add(row.toString());
		}

		Integer height = matriz.length;
		Integer width = matriz.length;

		for (Integer diagonal = 1 - width; diagonal <= height - 1; diagonal++) {
			StringBuilder text = new StringBuilder();
			for (Integer vertical = Math.max(0, diagonal), horizontal = -Math.min(0, diagonal); vertical < height
					&& horizontal < width; vertical++, horizontal++) {
				text.append(matriz[vertical][horizontal]);
			}
			if (text.toString().length() >= 4) {
				combinations.add(text.toString());
			}
		}

		return isMutant(combinations, dna);
	}

	private ResponseEntity<String> isMutant(List<String> combinations, List<String> dna) {
		Pattern pat = Pattern.compile(".*AAAA.*|.*CCCC.*|.*TTTT.*|.*GGGG.*");
		int count = 0;
		for (String c : combinations) {
			Matcher mat = pat.matcher(c);
			if (mat.matches()) {
				count++;
			}
		}

		DnaSubject dnaSubject = new DnaSubject();
		dnaSubject.setDna(dna.toString());

		if (count > 1) {
			dnaSubject.setMutantIndicator(true);
			mutantRepository.save(dnaSubject);
			return new ResponseEntity<>("True", HttpStatus.OK);
		}

		dnaSubject.setMutantIndicator(false);
		mutantRepository.save(dnaSubject);
		return new ResponseEntity<>("False", HttpStatus.FORBIDDEN);
	}

	@Override
	public ResponseEntity<Statistics> stats() {
		float countMutant = 0;
		float countHuman = 0;
		List<DnaSubject> dnaSubjects = mutantRepository.findAll();
		for (DnaSubject dnaSubject : dnaSubjects) {
			if (Boolean.TRUE.equals(dnaSubject.getMutantIndicator())) {
				countMutant++;
			} else {
				countHuman++;
			}
		}

		Statistics statistics = new Statistics();
		statistics.setCount_human_dna(countHuman);
		statistics.setCount_mutant_dna(countMutant);
		float result = 0;
		if (countHuman != 0) {
			result = countMutant / countHuman;
		}

		statistics.setRatio(result);

		return new ResponseEntity<>(statistics, HttpStatus.OK);
	}

}
