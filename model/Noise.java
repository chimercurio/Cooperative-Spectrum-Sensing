package model;

import java.util.ArrayList;
import java.util.Random;

import signalprocessing.SignalProcessor;

/** 
 * <p>Titolo:Noise</p>
 * <p>Descrizione: Classe per la generazione del rumore </p>
 * @author Pietro Coronas
 * **/

public class Noise extends AbstractSignal {

	double variance;
	double snr;

	/**
	 * Costruttore dell'oggetto Rumore. Il rumore sarà gaussiano con varianza unitaria.
	 * L'oggetto è costituito da 2 vettori, relativi a parte reale e parte immaginaria.
	 * 
	 * @param snr L'SNR a cui generare il rumore
	 * @param noiseLenght Lunghezza del rumore
	 * @param energy Energia del rumore, in questo caso pari ad 1
	 **/

	public Noise(double snr, int noiseLenght, double energy) {
		Random sample;
		double normalizeSnr = Math.pow(10, (snr / 10));
		this.variance = (1 / normalizeSnr);
		this.snr=snr;
		lenght = noiseLenght;

		samplesRe = new ArrayList<Double>();
		for (int i = 0; i < lenght; i++) {
			sample = new Random();
			samplesRe.add(i, (sample.nextGaussian() * Math.sqrt(variance/2)));

		}
		samplesIm = new ArrayList<Double>();
		for (int i = 0; i < lenght; i++) {
			sample = new Random();
			samplesIm.add(i, (sample.nextGaussian() * Math.sqrt(variance/2)));
		}
	}

	/**
	 * Metodo per la Divisione di un Segnale. Dato un segnale (segnale o rumore), un indice di inizio
	 * e uno di fine, il metodo ritorna la porzione di segnale che va dall'indice di inizio all'indice di terminazione.
	 * @param start Indice di inizio della sottoporzione
	 * @param end Indice di terminazione della sottoporzione
	 * @return Sottoporzione del segnale passato come parametro
	 * **/

	public Noise splitNoise(int start,int end){
		Noise splittedNoise= new Noise(this.getSnr(),(end-start),SignalProcessor.computeEnergy(this));
		ArrayList<Double> samplesRea=new ArrayList<Double>();
		ArrayList<Double> samplesImm=new ArrayList<Double>();
		for(int i=start;i<end;i++){
			samplesRea.add(this.getSamplesRe().get(i));
			samplesImm.add(this.getSamplesIm().get(i));

		}
		splittedNoise.setSamplesRe(samplesRea);
		splittedNoise.setSamplesIm(samplesImm);
		return splittedNoise;
	}

	
	
	/** Metodo per ottenere la varianza dell' Oggetto rumore
	 * @return la varianza dell'oggetto rumore
	 */
	
	public double getVariance() {
		return variance;
	}

	
	/** Metodo per settare la varianza dell'oggetto rumore
	 * @param variance La varianza da impostare all'oggetto rumore
	 */
	
	public void setVariance(double variance) {
		this.variance = variance;
	}

	/** Metodo per ottenere l'SNR a cui è stato creato l'oggetto rumore
	 * @return l'snr a cui è stato creato l'oggetto rumore
	 */

	public double getSnr() {
		return snr;
	}

	/** Metod per settare l'SNR dell'oggetto rumore
	 * @param snr Snr a cui settare l'oggetto rumore.
	 */
	 
	public void setSnr(double snr) {
		this.snr = snr;
	}






}
