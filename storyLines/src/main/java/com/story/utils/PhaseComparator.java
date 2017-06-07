package com.story.utils;

import java.util.Comparator;

import com.story.model.Phase;

public class PhaseComparator implements Comparator<Phase>{

	@Override
	public int compare(Phase phase_1, Phase phase_2) {
		return phase_1.getCreatedDate() < phase_2.getCreatedDate() ? -1 : 1;
	}

}
