package agents;
import java.util.ArrayList;

import detection.Detector;
import signalprocessing.SignalProcessor;

public class MaliciousSecondaryUser extends SecondaryUser {
	
	/**Questo metodo ritorna, per ogni valore di SNR , una lista di decisioni lunga quanto il numero di prove contenente l'assenza dell'utente
	 * primario per ogni misurazione. E' utilizzato per la creazione del vettore di decisione dell'utente malevolo
	 * @return Una lista di liste contenente per ogni SNR, una lista decisioni binarie che affermanol'assenza dell'utente primario di cardinalità pari al numero di prove
	 * @throws Exception **/

	public ArrayList<ArrayList<Integer>> computeAbsenceBinaryDecisionVector() throws Exception{
		ArrayList<ArrayList<Integer>> decisions= new  ArrayList<ArrayList<Integer>>();
		for(int i=inf;i<sup;i++){
			ArrayList<Integer> snrDecisions= new  ArrayList<Integer>();			
			for(int j=0;j<attempts;j++){
				snrDecisions.add(0);
			}
			decisions.add(snrDecisions);


		}
		return decisions;	
	}


	/**Questo metodo ritorna, per ogni valore di SNR , una lista di decisioni lunga quanto il numero di prove contenente la presenza dell'utente
	 * primario per ogni misurazione. E' utilizzato per la creazione del vettore di decisione dell'utente malevolo
	 * @return Una lista di liste contenente per ogni SNR, una lista decisioni binarie che affermano la presenza dell'utente primario di cardinalità pari al numero di prove
	 * @throws Exception **/

	public ArrayList<ArrayList<Integer>> computePresenceBinaryDecisionVector() throws Exception{
		ArrayList<ArrayList<Integer>> decisions= new  ArrayList<ArrayList<Integer>>();
		for(int i=inf;i<sup;i++){
			ArrayList<Integer> snrDecisions= new  ArrayList<Integer>();			
			for(int j=0;j<attempts;j++){
				snrDecisions.add(1);
			}
			decisions.add(snrDecisions);


		}
		return decisions;	
	}


	/**Questo metodo ritorna, per ogni valore di SNR , una lista di decisioni lunga quanto il numero di prove contenente la presenza (1) o
	 * l'assenza(0) dell'utente primario. Inquesto caso rappresenta un vettore di decisioni prodotto da un utente malevolo che riporta l'ooposto
	 * dell'energy detector: 0 se è presente l'utente, 1 se è assente.
	 * @param pfa Probabilità di falso allarme
	 * @return Una lista di liste contenente per ogni SNR, una lista decisioni binarie Opporte sulla presenza o assenza dell'utente primario di cardinalità pari al numero di prove
	 * @throws Exception **/

	public ArrayList<ArrayList<Integer>> computeOppositeBinaryDecisionVector(double pfa) throws Exception{
		ArrayList<ArrayList<Integer>> decisions= new  ArrayList<ArrayList<Integer>>();

		ArrayList<ArrayList<Double>> VectorSignalEnergy;
		if(s!=null){
			VectorSignalEnergy=SignalProcessor.computeVectorsEnergy(s, length, energy, attempts, inf, sup);
		}	
		else{VectorSignalEnergy=SignalProcessor.computeVectorsEnergy(null, length, energy, attempts, inf, sup);}
		int snr=inf;
		for (int i = 0; i < VectorSignalEnergy.size(); i++) {
			//System.out.println(inf-1);
			ArrayList<Integer> snrDecisions = Detector.oppositeBinaryDetector(
					SignalProcessor.getEnergyDetectorThreshold(pfa, snr), VectorSignalEnergy.get(i));
			decisions.add(snrDecisions);
			snr++;
		}

		return decisions;

	}


	/**Questo metodo ritorna, per ogni valore di SNR , una lista di decisioni lunga quanto il numero di prove contenente la presenza (1) o
	 * l'assenza(0) dell'utente primario. Inquesto caso rappresenta un vettore di decisioni prodotto da un utente malevolo che riporta, una volta
	 * ogni 10,20,30 o 50 prove, l'opposto dell'energy Detector.
	 * @param pfa Probabilità di falso allarme
	 * @return Una lista di liste contenente per ogni SNR, una lista decisioni binarie errate in maniera casuale sulla presenza o assenza dell'utente primario di cardinalità pari al numero di prove
	 * @throws Exception **/

	public ArrayList<ArrayList<Integer>> computeIntelligentOppositeBinaryDecisionVector(double pfa) throws Exception{
		ArrayList<ArrayList<Integer>> decisions= new  ArrayList<ArrayList<Integer>>();
		ArrayList<ArrayList<Double>> VectorSignalEnergy;
		if(s!=null){
			VectorSignalEnergy=SignalProcessor.computeVectorsEnergy(s, length, energy, attempts, inf, sup);
		}	
		else{VectorSignalEnergy=SignalProcessor.computeVectorsEnergy(null, length, energy, attempts, inf, sup);}
		int snr=inf;
		for (int i = 0; i < VectorSignalEnergy.size(); i++) {
			//System.out.println(inf-1);
			ArrayList<Integer> snrDecisions = Detector.intelligentOppositeMaliciousBinaryDetector(
					SignalProcessor.getEnergyDetectorThreshold(pfa, snr), VectorSignalEnergy.get(i));
			decisions.add(snrDecisions);
			snr++;

		}

		return decisions;

	}

	/**Questo metodo ritorna, per ogni valore di SNR , una lista di decisioni lunga quanto il numero di prove contenente la presenza (1) o
	 * l'assenza(0) dell'utente primario. Inquesto caso rappresenta un vettore di decisioni prodotto da un utente malevolo che riporta, una volta
	 * ogni 10,20,30 o 50 prove, l'assenza del'utente primario.
	 * @param pfa Probabilità di falso allarme
	 * @return Una lista di liste contenente per ogni SNR, una lista decisioni binarie errate in maniera casuale sulla presenza o assenza dell'utente primario di cardinalità pari al numero di prove
	 * @throws Exception **/

	public ArrayList<ArrayList<Integer>> computeIntelligentAbsenceBinaryDecisionVector(double pfa) throws Exception{
		ArrayList<ArrayList<Integer>> decisions= new  ArrayList<ArrayList<Integer>>();
		ArrayList<ArrayList<Double>> VectorSignalEnergy;
		if(s!=null){
			VectorSignalEnergy=SignalProcessor.computeVectorsEnergy(s, length, energy, attempts, inf, sup);
		}	
		else{VectorSignalEnergy=SignalProcessor.computeVectorsEnergy(null, length, energy, attempts, inf, sup);}
		int snr=inf;
		for (int i = 0; i < VectorSignalEnergy.size(); i++) {
			//System.out.println(inf-1);
			ArrayList<Integer> snrDecisions = Detector.intelligentAbsenceMaliciousBinaryDetector(
					SignalProcessor.getEnergyDetectorThreshold(pfa, snr), VectorSignalEnergy.get(i));
			decisions.add(snrDecisions);
			snr++;

		}

		return decisions;

	}

	/**Questo metodo ritorna, per ogni valore di SNR , una lista di decisioni lunga quanto il numero di prove contenente la presenza (1) o
	 * l'assenza(0) dell'utente primario. Inquesto caso rappresenta un vettore di decisioni prodotto da un utente malevolo che riporta, una volta
	 * ogni 10,20,30 o 50 prove, la presenza del'utente primario.
	 * @param pfa Probabilità di falso allarme
	 * @return Una lista di liste contenente per ogni SNR, una lista decisioni binarie errate in maniera casuale sulla presenza o assenza dell'utente primario di cardinalità pari al numero di prove
	 * @throws Exception **/

	public ArrayList<ArrayList<Integer>> computeIntelligentPresenceBinaryDecisionVector(double pfa) throws Exception{
		ArrayList<ArrayList<Integer>> decisions= new  ArrayList<ArrayList<Integer>>();


		ArrayList<ArrayList<Double>> VectorSignalEnergy;
		if(s!=null){
			VectorSignalEnergy=SignalProcessor.computeVectorsEnergy(s, length, energy, attempts, inf, sup);
		}	
		else{VectorSignalEnergy=SignalProcessor.computeVectorsEnergy(null, length, energy, attempts, inf, sup);}
		int snr=inf;
		for (int i = 0; i < VectorSignalEnergy.size(); i++) {
			//System.out.println(inf-1);
			ArrayList<Integer> snrDecisions = Detector.intelligentPresenceMaliciousBinaryDetector(
					SignalProcessor.getEnergyDetectorThreshold(pfa, snr), VectorSignalEnergy.get(i));
			decisions.add(snrDecisions);
			snr++;

		}

		return decisions;

	}

}


