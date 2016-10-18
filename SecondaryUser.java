package agents;
import java.util.ArrayList;
import java.util.HashMap;

import detection.Detector;
import signalprocessing.Moment;
import signalprocessing.SignalProcessor;
import model.Signal;

public abstract class SecondaryUser {
	
	Signal s;
	int attempts,length,inf,sup,block;
	double energy;

	/**
	 * Questo metodo inizializza i valori che saranno usati nei diversi tipi di
	 * detection.
	 * 
	 * @param s Segnale su cui effettuare la Detection
	 * @param signalLength lunghezza del segnale
	 * @param energy Energia del segnale
	 * @param attempts Numero di prove su cui viene effettuata la simulazione
	 * @param inf Estremo inferiore di SNR su cui è stata effettuata la simulazione
	 * @param sup Estremo superiore di SNR su cui è stata effettuata la simulazione
	 * @param block Blocchi in cui dividere il segnale per l'energy Detector
	 * @see SignalProcessor#computeMoment
	 * @see SignalProcessor#computeEnergy
	 * @see SignalProcessor#computePr
	 * @see SignalProcessor#computeMediumEnergy
	 * 
	 **/

	public void listenChannel(Signal s,int signalLength, double energy, int attempts, int inf, int sup,int block){
		this.s=s;
		this.length=signalLength;
		this.energy=energy;
		this.attempts=attempts;
		this.inf=inf;
		this.sup=sup;
		this.block=block;




	}
	/**
	 * Metodo per lo Spectrum Senging dell'energy Detector. Il confronto viene fatto tra la soglia e un vettore di energie ottenuto
	 * dividendo il segnale in M blocchi da N campioni e facendo la media delle energie.
	 * 
	 * @param pfa Probabilità di falso allarme
	 * @return Array con le percentuali di detection ordinate per SNR
	 * @throws Exception Pfa deve essere scelto in modo che 1-2pfa sia compreso tra -1 e 1
	 **/

	public ArrayList<Double> spectrumSensingBlockEnergyDetector(Double pfa) throws Exception{
		HashMap<Double, Double> EnergyDetection = new HashMap<Double, Double>();
		ArrayList<ArrayList<Double>> MediumSignalEnergy;
		if(s!=null){
			MediumSignalEnergy=SignalProcessor.computeMediumEnergy(s, length, energy, attempts, inf, sup, block);
		}
		else{MediumSignalEnergy=SignalProcessor.computeMediumEnergy(null, length, energy, attempts, inf, sup, block);}
		int snr=inf-1;
		for (int i = 0; i < MediumSignalEnergy.size(); i++) {
			Double ED = Detector.energyDetection(
					SignalProcessor.getEnergyDetectorThreshold(pfa, snr), MediumSignalEnergy.get(i));
			EnergyDetection.put((double)snr++, ED);
		}

		return SignalProcessor.orderSignal(EnergyDetection);
	}

	/**
	 * Metodo per lo Spectrum Senging dell'energy Detector effettuato senza dividere il segnale in blocchi,
	 * calcolando l'energia a partire dai momenti del secondo e quarto ordine. 
	 * 
	 * @param pfa Probabilità di falso allarme
	 * @return Array con le percentuali di detection ordinate per SNR
	 * @throws Exception  Pfa deve essere scelto in modo che 1-2pfa sia compreso tra -1 e 1
	 **/

	public ArrayList<Double> spectrumSensingMomentEnergyDetector(double pfa) throws Exception {
		ArrayList<Moment> MomentsSignal = SignalProcessor.computeMoment(s, length, energy, attempts, inf, sup);
		ArrayList<Moment> MomentsNoise =SignalProcessor.computeMoment(null, length, energy, attempts, inf, sup);

		HashMap<Double, Double> EnergyDetection = new HashMap<Double, Double>();
		ArrayList<ArrayList<Double>> MomentNoiseEnergy = SignalProcessor.computeMomentEnergy(MomentsNoise);
		ArrayList<ArrayList<Double>> MomentSignalEnergy;
		if(s!=null){MomentSignalEnergy = SignalProcessor.computeMomentEnergy(MomentsSignal);}
		else{MomentSignalEnergy=SignalProcessor.computeMomentEnergy(MomentsNoise);}

		for (int i = 0; i < MomentSignalEnergy.size(); i++) {
			Double ED = Detector.energyDetection(
					SignalProcessor.computeEnergyDetectorThreshold(pfa, MomentNoiseEnergy.get(i)), MomentSignalEnergy.get(i));
			EnergyDetection.put(MomentsSignal.get(i).getSnr(), ED);
		}

		return SignalProcessor.orderSignal(EnergyDetection);
	}

	/**
	 * Metodo per lo Spectrum sensing del metodo proposto. Il procedimento è simile a quello dell'energy Detector ma con
	 * la differenza che utilizza gli oggetti PR al posto dei momenti del secondo e quarto ordine.
	 * 
	 * @param pfa Probabilità di falso allarme
	 * @return Array con le percentuali di detection ordinate per SNR
	 * @throws Exception Pfa deve essere scelto in modo che 1-2pfa sia compreso tra -1 e 1
	 * 
	 **/

	public ArrayList<Double> spectrumSensingProposedDetector(Double pfa) throws Exception {
		ArrayList<Moment> MomentsSignal = SignalProcessor.computeMoment(s, length, energy, attempts, inf, sup);
		ArrayList<Moment> MomentsNoise =SignalProcessor.computeMoment(null, length, energy, attempts, inf, sup);

		HashMap<Double, Double> ProposedDetection = new HashMap<Double, Double>();
		ArrayList<ArrayList<Double>> PrNoise = SignalProcessor.computePr(MomentsNoise);
		ArrayList<ArrayList<Double>> PrSignal;
		if(s!=null){PrSignal = SignalProcessor.computePr(MomentsSignal);}
		else{PrSignal=SignalProcessor.computePr(MomentsNoise);}

		for (int i = 0; i < PrSignal.size(); i++) {
			Double PD = Detector.proposedMethodDetection(SignalProcessor.computeProposedThreshold(pfa, PrNoise.get(i)),
					PrSignal.get(i));
			ProposedDetection.put(MomentsSignal.get(i).getSnr(), PD);
		}
		return SignalProcessor.orderSignal(ProposedDetection);
	}


	/**Questo metodo rappresenta l'energy Detector tradizionale. Effettua il calcolo dell'energia del solo rumore su cui
	 * calcola la soglia. Successivamente calcola l'energia del segnale+rumore ed effettua il confronto con la soglia
	 * calcolata.
	 * @param pfa Probabilità di falso allarme
	 * @return Ritorna la percentuale di Detection calcolata sulle energie senza operazioni intermedie
	 * @throws Exception **/

	public ArrayList<Double> spectrumSensingTraditionalEnergyDetector(double pfa) throws Exception {

		HashMap<Double, Double> EnergyDetection = new HashMap<Double, Double>();

		ArrayList<ArrayList<Double>> VectorSignalEnergy;
		if(s!=null){
			VectorSignalEnergy=SignalProcessor.computeVectorsEnergy(s, length, energy, attempts, inf, sup);
		}	
		else{VectorSignalEnergy=SignalProcessor.computeVectorsEnergy(null, length, energy, attempts, inf, sup);}
		int snr=inf;
		for (int i = 0; i < VectorSignalEnergy.size(); i++) {
			Double ED = Detector.energyDetection(
					SignalProcessor.getEnergyDetectorThreshold(pfa,snr), VectorSignalEnergy.get(i));
			EnergyDetection.put((double)(inf++), ED);
			//snr++;
		}

		return SignalProcessor.orderSignal(EnergyDetection);
	}

	/**Questo metodo ritorna, per ogni valore di SNR , una lista di decisioni lunga quanto il numero di prove contenente la presenza (1) o
	 * l'assenza(0) dell'utente primario
	 * @param pfa Probabilità di falso allarme
	 * @return Una lista di liste contenente per ogni SNR, una lista decisioni binarie sulla presenza o assenza dell'utente primario di cardinalità pari al numero di prove
	 * @throws Exception **/

	public ArrayList<ArrayList<Integer>> computeBinaryDecisionVector(double pfa) throws Exception {
		ArrayList<ArrayList<Integer>> decisions= new  ArrayList<ArrayList<Integer>>();
		ArrayList<ArrayList<Double>> VectorSignalEnergy;

		if(s!=null){
			VectorSignalEnergy=SignalProcessor.computeVectorsEnergy(s, length, energy, attempts, inf, sup);
		}	
		else{VectorSignalEnergy=SignalProcessor.computeVectorsEnergy(null, length, energy, attempts, inf, sup);}
		int snr=inf;
		for (int i = 0; i < VectorSignalEnergy.size(); i++) {
			ArrayList<Integer> snrDecisions = Detector.binaryDetector(
					SignalProcessor.getEnergyDetectorThreshold(pfa,snr), VectorSignalEnergy.get(i));
			decisions.add(snrDecisions);
			snr++;

		}

		return decisions;
	}

}


