package detection;

import java.util.ArrayList;
import signalprocessing.SignalProcessor;

public class Detector {
	
	/**
	 * Metodo proposto per il calcolo della detection che è stato
	 * argomento di tesi triennale. Invece di effettuare il calcolo della soglia e il successivo confronto
	 * sull'energia calcolata a partire dai Momenti del secondo e quarto ordine, questo metodo effettua lo stesso
	 * procedimento utilizzando i parametri Pr invece dei momenti.
	 * 
	 * @param threshold Soglia utilizzata per la Detection
	 * @param pr Parametro Pr calcolato a partire dai momenti
	 * @return La percentuale di Detection calcolata ad uno specifico SNR
	 * @see  SignalProcessor#computePr
	 **/

	public static double proposedMethodDetection(double threshold, ArrayList<Double> pr) {
		double cont = 0;
		for (int i = 0; i < pr.size(); i++) {
			if (pr.get(i) >= threshold) {
				cont++;
			}
		}
		return (double) 100 / (double) (pr.size() / cont);
	}

	/**
	 * Metodo per l'Energy Detector . Prende in input la soglia e un array di
	 * energia. Per ogni valore dell'array che supera la soglia incrementa un
	 * contatore. Successivamente riporta la % di Detection per un dato SNR. 
	 * 
	 * @param threshold Soglia utilizzata per la Detection
	 * @param energy Array di energia. Nello specifico è un vettore di oggetti momento, contenente momenti del 
	 * secondo e quarto ordine
	 * @return Percentuale di Detection per un dato SNR
	 **/

	public static double energyDetection(double threshold, ArrayList<Double> energy) {
		double cont = 0;
		for (int i = 0; i < energy.size(); i++) {
			if (energy.get(i) > threshold) {
				cont++;
			}
		}
		return (double) 100 / (double) (energy.size() / cont);
	}


	/**
	 * Metodo per la creazione di un array di decisioni . Prende in input la soglia e un array di
	 * energia. Per ogni valore dell'array che supera la soglia inserisce 1 (utente primario trovato), altrimenti 0. 
	 *@param threshold Soglia utilizzata per la Detection
	 * @param energy Array di energia. Nello specifico è un vettore di oggetti momento, contenente momenti del 
	 * secondo e quarto ordine
	 * @return Un array di decisioni binarie di cardinalità pari al numero di prove, per uno stesso snr
	 **/

	public static ArrayList<Integer> binaryDetector(double threshold, ArrayList<Double> energy) {
		ArrayList<Integer> snrDecisions= new ArrayList<Integer>();
		for (int i = 0; i < energy.size(); i++) {
			if (energy.get(i) > threshold) {
				snrDecisions.add(1);
			}
			else{snrDecisions.add(0);}
		}
		return snrDecisions;
	}

	/**
	 * Metodo per la creazione di un array di decisioni da parte di un dispositivo malevolo . Prende in input la soglia e un array di
	 * energia. Per ogni valore dell'array che supera la soglia inserisce 0 (utente primario non trovato), altrimenti 1.
	 * Riporta l'opposto dell'energy detector. 
	 *@param threshold Soglia utilizzata per la Detection
	 * @param energy Array di energia. Nello specifico è un vettore di oggetti momento, contenente momenti del 
	 * secondo e quarto ordine
	 * @return Un array di decisioni binarie di cardinalità pari al numero di prove, per uno stesso snr
	 **/

	public static ArrayList<Integer> oppositeBinaryDetector(double threshold, ArrayList<Double> energy) {
		ArrayList<Integer> snrDecisions= new ArrayList<Integer>();
		for (int i = 0; i < energy.size(); i++) {
			if (energy.get(i) > threshold) {
				snrDecisions.add(0);
			}
			else{snrDecisions.add(1);}
		}
		return snrDecisions;
	}

	/**
	 * Metodo per la creazione di un array di decisioni da parte di un dispositivo malevolo . Prende in input la soglia e un array di
	 * energia. In modo randomico, riporta l'opposto dell'energy detector. Può sbagliare una volta ogni 10 prove, una volta
	 * ogni 30 prove,una volta ogni 50 prove o due volte ogni 10 prove
	 *@param threshold Soglia utilizzata per la Detection
	 * @param energy Array di energia. Nello specifico è un vettore di oggetti momento, contenente momenti del 
	 * secondo e quarto ordine
	 * @return Un array di decisioni binarie di cardinalità pari al numero di prove, per uno stesso snr
	 **/

	public static ArrayList<Integer> intelligentOppositeMaliciousBinaryDetector(double threshold, ArrayList<Double> energy) {
		ArrayList<Integer> snrDecisions= new ArrayList<Integer>();
		double random= Math.random()*10;
		//Sbaglia circa 33 volte ogni 100 prove
		if(random>0 & random<3.5){
			for (int i = 0; i < energy.size(); i++) {
				if(i%3!=0){
					if(energy.get(i)>threshold){snrDecisions.add(1);}
					else{snrDecisions.add(0);}
				}
				else{
					if(energy.get(i)>threshold){snrDecisions.add(0);}
					else{snrDecisions.add(1);} 
				}
			} 
		}
		//sbaglia 20 volte ogni 100 prove circa
		if(random>3.5 & random<7.1){
			for (int i = 0; i < energy.size(); i++) {
				if(i%5!=0){
					if(energy.get(i)>threshold){snrDecisions.add(1);}
					else{snrDecisions.add(0);}
				}
				else{
					if(energy.get(i)>threshold){snrDecisions.add(0);}
					else{snrDecisions.add(1);}  
				} 
			}
		}
		//sbaglia 16 volte ogni 100 prove circa
		else{
			for (int i = 0; i < energy.size(); i++) {
				if(i%6!=0){
					if(energy.get(i)>threshold){snrDecisions.add(1);}
					else{snrDecisions.add(0);}
				}
				else{
					if(energy.get(i)>threshold){snrDecisions.add(0);}
					else{snrDecisions.add(1);} 
				}
			}

		}


		return snrDecisions;
	}
	
	/**
	 * Metodo per la creazione di un array di decisioni da parte di un dispositivo malevolo . Prende in input la soglia e un array di
	 * energia. In modo randomico, riporta l'assenza dell'utente primario. Può sbagliare una volta ogni 10 prove, una volta
	 * ogni 30 prove,una volta ogni 50 prove o due volte ogni 10 prove
	 *@param threshold Soglia utilizzata per la Detection
	 * @param energy Array di energia. Nello specifico è un vettore di oggetti momento, contenente momenti del 
	 * secondo e quarto ordine
	 * @return Un array di decisioni binarie di cardinalità pari al numero di prove, per uno stesso snr
	 **/

	public static ArrayList<Integer> intelligentAbsenceMaliciousBinaryDetector(double threshold, ArrayList<Double> energy) {
		ArrayList<Integer> snrDecisions= new ArrayList<Integer>();
		double random= Math.random()*10;
		//Sbaglia circa 33 volte ogni 100 prove
		if(random>0 & random<3.5){
			for (int i = 0; i < energy.size(); i++) {
				if(i%3!=0){
					if(energy.get(i)>threshold){snrDecisions.add(1);}
					else{snrDecisions.add(0);}
				}
				else{
					snrDecisions.add(0); 
				}
			} 
		}
		//sbaglia 20 volte ogni 100 prove circa
		if(random>3.5 & random<7.1){
			for (int i = 0; i < energy.size(); i++) {
				if(i%5!=0){
					if(energy.get(i)>threshold){snrDecisions.add(1);}
					else{snrDecisions.add(0);}
				}
				else{
					snrDecisions.add(0); 
				} 
			}
		}
		//sbaglia 16 volte ogni 100 prove circa
		else{
			for (int i = 0; i < energy.size(); i++) {
				if(i%6!=0){
					if(energy.get(i)>threshold){snrDecisions.add(1);}
					else{snrDecisions.add(0);}
				}
				else{
					snrDecisions.add(0);
				}
			}

		}


		return snrDecisions;
	}
	
	/**
	 * Metodo per la creazione di un array di decisioni da parte di un dispositivo malevolo . Prende in input la soglia e un array di
	 * energia. In modo randomico, riporta la presenza dell'utente primario. Può sbagliare una volta ogni 10 prove, una volta
	 * ogni 30 prove,una volta ogni 50 prove o due volte ogni 10 prove
	 *@param threshold Soglia utilizzata per la Detection
	 * @param energy Array di energia. Nello specifico è un vettore di oggetti momento, contenente momenti del 
	 * secondo e quarto ordine
	 * @return Un array di decisioni binarie di cardinalità pari al numero di prove, per uno stesso snr
	 **/

	public static ArrayList<Integer> intelligentPresenceMaliciousBinaryDetector(double threshold, ArrayList<Double> energy) {
		ArrayList<Integer> snrDecisions= new ArrayList<Integer>();
		double random= Math.random()*10;
		//Sbaglia circa 33 volte ogni 100 prove
		if(random>0 & random<3.5){
			for (int i = 0; i < energy.size(); i++) {
				if(i%3!=0){
					if(energy.get(i)>threshold){snrDecisions.add(1);}
					else{snrDecisions.add(0);}
				}
				else{
					snrDecisions.add(1);
				}
			} 
		}
		//sbaglia 20 volte ogni 100 prove circa
		if(random>3.5 & random<7.1){
			for (int i = 0; i < energy.size(); i++) {
				if(i%5!=0){
					if(energy.get(i)>threshold){snrDecisions.add(1);}
					else{snrDecisions.add(0);}
				}
				else{
					snrDecisions.add(1); 
				} 
			}
		}
		//sbaglia 16 volte ogni 100 prove circa
		else{
			for (int i = 0; i < energy.size(); i++) {
				if(i%6!=0){
					if(energy.get(i)>threshold){snrDecisions.add(1);}
					else{snrDecisions.add(0);}
				}
				else{
					snrDecisions.add(1); 
				}
			}

		}


		return snrDecisions;
	}

	/**
	 * Metodo la generazione della detection da parte Del fusion center secondo la tecnica AND.
	 * 
	 * @param decisionsVector Vettori di decisione degli utenti ad uno stesso SNR
	 * @return Percentuale di Detection per un dato SNR
	 **/

	public static  double andFusionDetection(ArrayList<ArrayList<Integer>> decisionsVector){
		int cont=0;
		ArrayList<Integer> Finaldecisions=new ArrayList<Integer>();
		for(int i=0;i<decisionsVector.get(0).size();i++){
			ArrayList<Integer> decisions=new ArrayList<Integer>();
			for(int j=0;j<decisionsVector.size();j++){		
				decisions.add(decisionsVector.get(j).get(i));

			}
			Finaldecisions.add(andFusionRule(decisions));
			//if(andFusionRule(decisions)==1){cont++;

			//}

		}
		for(int h=0;h<Finaldecisions.size();h++){
			if(Finaldecisions.get(h)==1){cont++;}
		}

		if(cont==0){return 0.0;}
		else{
			//System.out.println("Size: "+ (decisionsVector.get(0).size()) + " Cont: "+ cont);
			return (double) 100 / ((double) ((decisionsVector.get(0).size())  / (double) cont));}

	}

	/**
	 * Metodo la generazione della detection da parte Del fusion center secondo la tecnica Or.
	 * 
	 * @param decisionsVector Vettori di decisione degli utenti ad uno stesso SNR
	 * @return Percentuale di Detection per un dato SNR
	 **/

	public static  double orFusionDetection(ArrayList<ArrayList<Integer>> decisionsVector){
		int cont=0;
		for(int i=0;i<decisionsVector.get(0).size();i++){
			ArrayList<Integer> decisions=new ArrayList<Integer>();
			for(int j=0;j<decisionsVector.size();j++){

				decisions.add(decisionsVector.get(j).get(i));

			}
			if(orFusionRule(decisions)==1){cont++;

			}

		}
		if(cont==0){return 0.0;}
		else{
			return (double) 100 / ((double) ((decisionsVector.get(0).size())  / (double) cont));}

	}

	/**
	 * Metodo la generazione della detection da parte Del fusion center secondo la tecnica Majority.
	 * 
	 * @param decisionsVector Vettori di decisione degli utenti ad uno stesso SNR
	 * @return Percentuale di Detection per un dato SNR
	 **/

	public static  double majorityFusionDetection(ArrayList<ArrayList<Integer>> decisionsVector){
		int cont=0;
		for(int i=0;i<decisionsVector.get(0).size();i++){
			ArrayList<Integer> decisions=new ArrayList<Integer>();
			for(int j=0;j<decisionsVector.size();j++){

				decisions.add(decisionsVector.get(j).get(i));

			}
			if(majorityFusionRule(decisions)==1){cont++;
			}

		}
		if(cont==0){return 0.0;}
		else{
			return (double) 100 / ((double) ((decisionsVector.get(0).size())  / (double) cont));}

	}


	/** Semplice metodo che riporta, su un vettore di decisioni binarie, la % di Detection dell'utente primario
	 * @param decisionsVector Vettore di decisioni binarie
	 * @return La % di detection dell'utente primario
	 */

	public static double reputationBasedDetection(ArrayList<Integer> decisionsVector){
		int cont=0;
		for(int i=0;i<decisionsVector.size();i++){
			if(decisionsVector.get(i)==1){
				cont++;
			}
		}
		//System.out.println(decisionsVector.size()+"     "+(double) cont);
		return (double) 100 / ((double) ((decisionsVector.size())  / (double) cont));
	}

	/**Metodo di fusione con la tecnica and. Ritorna 1 (l'utente primario è presente) se tutte le decisioni
	 * di tutti i dispositivi sono uguali ad 1, 0 altrimenti
	 * @param decisions Un array contenente le decisioni binarie sulla presenza o assenza dell'utente primario per un dato SNR di
	 * ogni utente secondario. La tenica di fusione AND prevede che sia dichiarata la presenza dell'utente primario se tutti gli utenti secondari 
	 * ne verificano la presenza.
	 * Questo metodo verrà iterato per ogni SNR
	 * @return una decisione binaria risultato della fusione secondo la tecnica AND**/

	public static int andFusionRule(ArrayList<Integer> decisions){
		int fusionDecision=1;
		for(int i=0;i<decisions.size();i++){
			if(decisions.get(i)==0){
				fusionDecision=0;
			}
		}
		return fusionDecision;
	}


	/**Metodo di fusione con la tecnica or. Ritorna 1 (l'utente primario è presente) se almeno una delle decisioni
	 * dei dispositivi sono uguali ad 1, 0 altrimenti
	 *  @param decisions un array contenente le decisioni binarie sulla presenza o assenza dell'utente primario per un dato SNR di
	 * ogni utente secondario. La tecnica di fusione OR prevede che sia dichiarata la presenza dell'utente se almeno un utente secondario
	 * ne verifica la presenza
	 * Questo metodo verrà iterato per ogni SNR
	 * @return una decisione binaria risultato della fusione secondo la tecnica OR**/

	public static int orFusionRule(ArrayList<Integer> decisions){
		int fusionDecision=0;

		for(int i=0;i<decisions.size();i++){
			if(decisions.get(i)==1){
				fusionDecision=1;
			}
		}
		return fusionDecision;
	}


	/**Metodo di fusione con la tecnica or. Ritorna 1 (l'utente primario è presente) se almeno una delle decisioni
	 * dei dispositivi sono uguali ad 1, 0 altrimenti
	 *  @param decisions un array contenente le decisioni binarie sulla presenza o assenza dell'utente primario per un dato SNR di
	 * ogni utente secondario. La tecnica di fusione MAJORITY prevede che sia dichiarata la presenza dell'utente se la metà più uno degli
	 * utenti secondari ne verifica la presenza
	 * Questo metodo verrà iterato per ogni SNR
	 * @return una decisione binaria risultato della fusione secondo la tecnica MAJORITY**/

	public static int majorityFusionRule(ArrayList<Integer> decisions){
		int majority=(decisions.size()/2)+1;
		int contPresence=0;
		int fusionDecision = 0;
		for(int i=0;i<decisions.size();i++){
			if(decisions.get(i)==1){
				contPresence++;
			}
		}
		if(contPresence>=majority){
			fusionDecision=1;
		}


		return fusionDecision;}

}


