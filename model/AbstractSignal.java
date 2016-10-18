package model;

import java.util.ArrayList;

/**
 * <p>Titolo: AbstractSignal</p>
 * <p>Descrizione: Classe astratta per i segnalipresenti nella comunicazione</p>
 * @author Pietro Coronas**/

public abstract class AbstractSignal {
	ArrayList<Double> samplesRe;
	ArrayList<Double> samplesIm;
	int lenght;


	public ArrayList<Double> getSamplesRe() {
		return samplesRe;
	}

	public void setSamplesRe(ArrayList<Double> samplesRe) {
		this.samplesRe = samplesRe;
	}

	public ArrayList<Double> getSamplesIm() {
		return samplesIm;
	}

	public void setSamplesIm(ArrayList<Double> samplesIm) {
		this.samplesIm = samplesIm;
	}

	public int getLenght() {
		return lenght;
	}

	public void setLenght(int signalLenght) {
		this.lenght = signalLenght;
	}



}
