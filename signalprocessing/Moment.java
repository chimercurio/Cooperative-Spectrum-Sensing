package signalprocessing;

import java.util.ArrayList;

import model.Noise;
import model.Signal;

/** <p>Titolo:Moment</p>
 * <p>Descrizione: Classe relativa ai momenti del secondo e quarto ordine </p>
 * @author Pietro Coronas
 * **/

public class Moment {
	//SNR di riferimento
	public double snr;
	//numero di prove
	public int attempts;
	//lughezza del segnale. Mi serve in quanto quando passo il segnale nullo (solo rumore) non posso fare s.getLength
	public int length;
	//energia del segnale. lo setto come parametro cos' evito di ricalcolarlo ad ogni prova
	public double energy;
	public ArrayList<Double> samplesRe;
	public ArrayList<Double> samplesIm;


	/**
	 * Costruttore dell'oggetto Momento. Ogni oggetto momento è composto da 4 attributi: I momenti del secondo ordine,
	 * i Momenti del quarto ordine, l'energia del segnale e l'SNR su cui viene effettuato il calcolo dei momenti
	 * 
	 * @param s Il segnale
	 * @param attempts Numero di prove su cui effettuare la simulazione
	 * @param energy Energia del segnale
	 * @param snr L'SNR di riferimento
	 * @param length Lunghezza del segnale
	 **/

	public Moment(Signal s,int attempts,double snr,int length,double energy) {
		// Inizializza i parametri
		//energia del segnale
		this.energy=energy;
		this.length=length;
		this.snr = snr;
		this.attempts=attempts;
		if (s != null) {

			samplesRe = s.getSamplesRe();
			samplesIm = s.getSamplesIm();
		}
		//else {
		//samplesRe = null;
		//samplesIm = null;

		//}
	}



	/** Metodo per il calcolo dei momenti del secondo  ordine
	 * @return Una lista di momenti del secondo ordine di cardinalità pari al numero di prove
	 */

	public ArrayList<Double> computeSecondOrderMoment(){
		ArrayList<Double>	secondOrder = new ArrayList<Double>();

		for (int i = 0; i < attempts; i++) {
			double j = 0.0;

			Noise noise = new Noise(snr, length, energy);

			Signal signal = new Signal(noise.getLenght());

			signal.setSamplesRe(MathFunctions.SumVector(noise.getSamplesRe(), samplesRe));
			signal.setSamplesIm(MathFunctions.SumVector(noise.getSamplesIm(), samplesIm));

			for (int k = 1; k < noise.getLenght() - 1; k++) {

				j = (j + Math.pow(signal.getSamplesRe().get(k), 2) + Math.pow(signal.getSamplesIm().get(k), 2));
			}

			secondOrder.add(i, j * (1 / (double) signal.getLenght()));
		}
		return secondOrder;
	}


	/** Metodo per il calcolo dei momenti del se quarto ordine
	 * @return Una lista di momenti del quarto ordine di cardinalità pari al numero di prove
	 */

	public ArrayList<Double> computeFourthOrderMoment(){
		ArrayList<Double>	fourthOrder = new ArrayList<Double>();

		for (int i = 0; i < attempts; i++) {
			double h = 0.0;

			Noise noise = new Noise(snr, length, energy);

			Signal signal = new Signal(noise.getLenght());

			signal.setSamplesRe(MathFunctions.SumVector(noise.getSamplesRe(), samplesRe));
			signal.setSamplesIm(MathFunctions.SumVector(noise.getSamplesIm(), samplesIm));

			for (int k = 1; k < noise.getLenght() - 1; k++) {

				h = (h + Math.pow(signal.getSamplesRe().get(k), 4) + Math.pow(signal.getSamplesIm().get(k), 4));
			}

			fourthOrder.add(i, h * (1 / (double) signal.getLenght()));
		}
		return fourthOrder;
	}



	/**  Metodo che ritorna l'snr a cui sono stati calcolati i momenti del secondo e quarto ordine
	 * @return Ritorna l'snr a cui sono stati calcolati i momenti del secondo e quarto ordine
	 */

	public double getSnr() {
		return snr;
	}


}
