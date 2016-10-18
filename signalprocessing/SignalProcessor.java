package signalprocessing;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import model.AbstractSignal;
import model.Noise;
import model.Signal;

/**
 * <p>Titolo:SignalProcessor</p>
 * <p>Descrizione: Classe che contiene Funzioni utili da applicare sul segnale.</p>
 * @author Pietro Coronas
 **/

public class SignalProcessor {

	
	/**
	 * Metodo per ordinare una mappa in base alla chiave.
	 * In questo caso è utilizzato su una mappa che ha come chiave l'SNR e come valore la % di Detection Relativa.
	 * @param signalmapToOrder mappa con chiave SNR e valore la relativa % di detection 
	 * @return la mappa ordinata in base all'SNR
	 **/

	public static ArrayList<Double> orderSignal(HashMap<Double, Double> signalmapToOrder) {

		ArrayList<Double> snr = new ArrayList<Double>();
		for (Double key : signalmapToOrder.keySet()) {
			snr.add(key);
		}
		Collections.sort(snr);

		ArrayList<Double> Edetection = new ArrayList<Double>();
		for (Double key : snr) {
			Edetection.add(signalmapToOrder.get(key));
		}
		return Edetection;
	}
	
	/** 
	 * Metodo per il calcolo dell'energia del segnale 
	 * 
	 * @param s Segnale su cui calcolare l'energia
	 * @return energia del segnale **/

	public static double computeEnergy(AbstractSignal s) {
		double p = 0.0;
		for (int i = 0; i <  s.getLenght()-1; i++) {
			p=p+ (Math.pow(Math.abs(s.getSamplesRe().get(i)), 2))+(Math.pow(Math.abs(s.getSamplesIm().get(i)), 2));
			//p = p + Math.abs(Math.pow(s.getSamplesRe().get(i), 2)) + Math.abs((Math.pow(s.getSamplesIm().get(i), 2)));
		}
		return (p / s.getLenght());
	}

	/** 
	 * Metodo per la generazione dei momenti del secondo e quarto ordine.
	 * 
	 * @param s Il segnale
	 * @param length Lunghezza del segnale 
	 * @param energy Energia del segnale
	 * @param attempts Numero di prove su cui effettuare la simulazione
	 * @param inf Estremo inferiore di SNR su cui effettuare la simulazione
	 * @param sup Estremo superiore di SNR su cui effettuare la simulazione 
	 * @return Una lista di Momenti**/

	public static ArrayList<Moment> computeMoment(Signal s, int length, double energy,int attempts, int inf,
			int sup) {
		ArrayList<Moment> Moments = new ArrayList<Moment>();
		for (double i = inf; i < sup; i++) {
			Moment m = new Moment(s, attempts,i,length,energy);
			Moments.add(m);
		}
		return Moments;
	}

	/**
	 *  Metodo per il calcolo dell'energia dei momenti 
	 * 
	 * @param Moment Array di oggetti momento
	 * @return Energia **/

	public static ArrayList<ArrayList<Double>> computeMomentEnergy(ArrayList<Moment> Moment) {
		ArrayList<ArrayList<Double>> energy = new ArrayList<ArrayList<Double>>();
		for (int i = 0; i < Moment.size(); i++) {
			energy.add(Moment.get(i).computeSecondOrderMoment());
		}
		return energy;
	}

	/** 
	 * Metodo per la generazione degli parametri Pr. I parametri Pr sono particolari valori
	 * ottenuti a partire dai momenti del secondo e quarto ordine utilizzati per il calcolo della
	 * detection nel metodo proposto. 
	 * 
	 * @param Moment Lista di Oggetti Momento su cui calcolare Pr
	 * @return una lista di parametri Pr **/

	public static ArrayList<ArrayList<Double>> computePr(ArrayList<Moment> Moment) {
		ArrayList<ArrayList<Double>> PrResult = new ArrayList<ArrayList<Double>>();
		for (int i = 0; i < Moment.size(); i++) {
			ArrayList<Double> m = Moment.get(i).computeSecondOrderMoment();
			ArrayList<Double> q = Moment.get(i).computeFourthOrderMoment();
			ArrayList<Double> prTemp=new ArrayList<Double>();
			for (int j = 0; j< m.size(); j++) {
				prTemp.add((double) ((2 * (Math.pow(m.get(j), 2)) - q.get(j))));
			}
			PrResult.add(prTemp);

		}
		return PrResult;
	}


	/**
	 * Metodo per la generazione del vettore di energie medie necessario per il calcolo dell'energy Detector.
	 * Per ogni prova genero il rumore e lo sommo al segnale.Divido il segnale in M blocchi di N campioni ciascuno e per ogni
	 * blocco calcolo l'energia. Faccio la media sommando le energie dei blocchi e dividendo per M.
	 * @param s Il segnale
	 * @param length Lunghezza del segnale 
	 * @param energy Energia del segnale
	 * @param attempts Numero di prove su cui effettuare la simulazione
	 * @param inf Estremo inferiore di SNR su cui effettuare la simulazione
	 * @param sup Estremo superiore di SNR su cui effettuare la simulazione 
	 * @param block Numero di blocchi M in cui dividere il segnale
	 * @return Una lista di liste contenente per ogni SNR, una lista di energie medie di cardinalità pari al numero di prove**/

	public static ArrayList<ArrayList<Double>> computeMediumEnergy(Signal s, int length, double energy, int attempts, int inf,
			int sup,int block) {

		ArrayList<ArrayList<Double>> MediumEnergy = new ArrayList<ArrayList<Double>>();
		//Prendo l'intervallo Snr
		for (double snr = inf; snr < sup; snr++) {
			ArrayList<Double> MediumEnergyTemp = new ArrayList<Double>();
			//Per ogni prova
			for (int j = 0; j < attempts; j++) {
				//Genero il rumore.
				Noise noise = new Noise(snr,length, energy);

				double avg = 0;
				int samples = (length/block);
				int startIndex=0;
				for(int i=0;i<length;i++){
					if(i%samples==0 & i!=0){

						Signal signal = new Signal(samples);
						if(s!=null){
							ArrayList<Double> samplesRe=MathFunctions.SumVector(noise.splitNoise(startIndex,i-1).getSamplesRe(), s.splitSignal(startIndex,i-1).getSamplesRe());
							ArrayList<Double> samplesIm=MathFunctions.SumVector(noise.splitNoise(startIndex,i-1).getSamplesIm(), s.splitSignal(startIndex,i-1).getSamplesIm());
							signal.setSamplesRe(samplesRe);
							signal.setSamplesIm(samplesIm);
							startIndex=i;
							avg=avg+computeEnergy(signal);}
						else{
							ArrayList<Double> samplesRe=noise.splitNoise(startIndex,i-1).getSamplesRe();
							ArrayList<Double> samplesIm=noise.splitNoise(startIndex,i-1).getSamplesIm();
							signal.setSamplesRe(samplesRe);
							signal.setSamplesIm(samplesIm);
							startIndex=i;
							avg=avg+computeEnergy(signal);}

					}
					if(i==length-1){
						MediumEnergyTemp.add(avg/block);}	
				}

			}
			MediumEnergy.add(MediumEnergyTemp);
		}
		return MediumEnergy;
	}

	/**Metodo per il calcolo dei vettori di Energia in un intervallo SNR nelle ipotesi di segnale+rumore e di solo rumore.
	 * Questo metodo calcola i valori necessari per l'energy Detector tradizionale, in quanto effettua il
	 * calcolo dell'energya diretto sui segnali senza operazioni intermedie
	 * @param s Il segnale
	 * @param length Lunghezza del segnale 
	 * @param energy Energia del segnale
	 * @param attempts Numero di prove su cui effettuare la simulazione
	 * @param inf Estremo inferiore di SNR su cui effettuare la simulazione
	 * @param sup Estremo superiore di SNR su cui effettuare la simulazione 
	 * @return Una lista di liste contenente per ogni SNR, una lista di energie di cardinalità pari al numero di prove**/

	public static ArrayList<ArrayList<Double>> computeVectorsEnergy(Signal s, int length, double energy, int attempts, int inf,
			int sup){
		ArrayList<ArrayList<Double>> EnergyVector = new ArrayList<ArrayList<Double>>();
		for (double snr = inf; snr < sup; snr++) {
			ArrayList<Double> EnergyVectorTemp = new ArrayList<Double>();
			for (int j = 0; j < attempts; j++) {
				Noise noise = new Noise(snr,length, energy);
				Signal Resultsignal = new Signal(length);
				ArrayList<Double> samplesRe;
				ArrayList<Double> samplesIm;
				if(s!=null){
					samplesRe=MathFunctions.SumVector(noise.getSamplesRe(),s.getSamplesRe());
					samplesIm=MathFunctions.SumVector(noise.getSamplesIm(),s.getSamplesIm());
				}
				else{
					samplesRe=noise.getSamplesRe();
					samplesIm=noise.getSamplesIm();
				}
				Resultsignal.setSamplesRe(samplesRe);
				Resultsignal.setSamplesIm(samplesIm);

				EnergyVectorTemp.add(SignalProcessor.computeEnergy(Resultsignal));
			}

			EnergyVector.add(EnergyVectorTemp);
		}
		return EnergyVector;}

	/**Metodo per il calcolo del vettore di Energia ad uno specifico SNR nelle ipotesi di segnale+rumore e di solo rumore.
	 * Questo metodo calcola i valori necessari per l'energy Detector tradizionale, in quanto effettua il
	 * calcolo dell'energya diretto sui segnali senza operazioni intermedie
	 * @param s Il segnale
	 * @param length Lunghezza del segnale 
	 * @param energy Energia del segnale
	 * @param attempts Numero di prove su cui effettuare la simulazione
	 * @param snr Snr a cui effettuare la simulazione
	 * @return Una lista  contenente le energie calcolate allo specifico SNR, di cardinalità pari al numero di prove**/

	public static ArrayList<Double> computeVectorEnergy(Signal s, int length, double energy, int attempts, int snr){

		ArrayList<Double> EnergyVector = new ArrayList<Double>();
		for (int j = 0; j < attempts; j++) {
			Noise noise = new Noise(snr,length, energy);
			Signal Resultsignal = new Signal(length);
			ArrayList<Double> samplesRe;
			ArrayList<Double> samplesIm;
			if(s!=null){
				samplesRe=MathFunctions.SumVector(noise.getSamplesRe(),s.getSamplesRe());
				samplesIm=MathFunctions.SumVector(noise.getSamplesIm(),s.getSamplesIm());
			}
			else{
				samplesRe=noise.getSamplesRe();
				samplesIm=noise.getSamplesIm();
			}
			Resultsignal.setSamplesRe(samplesRe);
			Resultsignal.setSamplesIm(samplesIm);

			EnergyVector.add(SignalProcessor.computeEnergy(Resultsignal));
		}


		return EnergyVector;}

	/**
	 * Metodo per il calcolo della soglia necessaria per la Detection del metodo
	 * proposto.
	 * 
	 * @param Pfa Probabilità di falso allarme
	 * @param pr Lista di Double contenente i valori del parametro Pr
	 * calcolato nell'ipotesi in cui il segnale primario è assente (solo rumore)
	 * @return La soglia
	 * @throws Exception L'argomento della funzione InvErf deve essere compreso tra -1 e 1
	 **/

	public static double computeProposedThreshold(double Pfa, ArrayList<Double> pr) throws Exception {
		double M = MathFunctions.Mean(pr);
		double V = MathFunctions.Variance(pr);

		double implThreshold = M + Math.sqrt(2 * V) * MathFunctions.InvErf(1 - 2 * Pfa);
		return implThreshold;
	}

	/**
	 * Metodo per il calcolo della soglia necessaria per l'energy Detector.
	 * 
	 * @param Pfa Probabilità di falso allarme
	 * @param energy Momento calcolato nell'ipotesi in cui il segnale primario è assente (solo rumore)
	 * @return La soglia necessaria per l'energy Detector
	 * @throws Exception L'argomento della funzione InvErf deve essere compreso tra -1 e 1
	 **/

	public static double computeEnergyDetectorThreshold(double Pfa, ArrayList<Double> energy) throws Exception {

		double M = MathFunctions.Mean(energy);
		double V = MathFunctions.Variance(energy);

		double edThreshold = M + Math.sqrt(2 * V) * MathFunctions.InvErf(1 - 2 * Pfa);
		return edThreshold;
	}

	/** Metodo che riporta la soglia dell'energy Detector in base alla probabilità di falso allarme e all'SNR. La soglia viene presa da un
	 * file di testo generato precedentemente.
	 * @param Pfa probabilità di falso allarme
	 * @param snr SNR specifico a cui prendere la soglia
	 * @return Una soglia
	 * @see Utils#generateThreshold
	 * @throws Exception
	 */

	public static double getEnergyDetectorThreshold(double Pfa, int snr) throws Exception {

		FileReader f=new FileReader("threshold"+Pfa+".txt");
		BufferedReader reader=new BufferedReader(f);
		String s=null;
		boolean find=false;
		String line = reader.readLine();
		while(line!=null & find==false) {
			if((line.substring(0,String.valueOf(snr).length())).equals(String.valueOf(snr)) & (line.charAt(String.valueOf(snr).length())==' ')){
				s=line;
				find=true;
			}
			line = reader.readLine();
		}

		s=s.substring(String.valueOf(snr).length(),s.length());

		reader.close();
		return Double.valueOf(s);
	}

	/** Dato un array di decisioni binarie, il metodo riporta 1 se la media dell'array supera 0.5, 0 altrimenti
	 * @param binaryDecisions un array di Decisioni binarie
	 * @return 1 se la media dell'array supera 0.5,0 altrimenti.
	 */

	public static int getMediumDecision(ArrayList<Integer> binaryDecisions){
		if(binaryDecisions.size()==1){return binaryDecisions.get(0);}
		else{
			int tot=0;
			for(int i=0;i<binaryDecisions.size();i++){
				tot=tot+binaryDecisions.get(i);
			}
			double mediumValue=tot/binaryDecisions.size();
			if(mediumValue>=0.5){return 1;}
			else return 0;
		}}
	
	
	
	/**Dato un array di decisioni binarie, il metodo riporta una una lista di decisioni binarie dando peso 0.5 a ciascun utente.
	 * Occorrono due utenti concordi su una decisione per riportare la presenza o l'assenza dell'utente primario
	 
	 * @param binaryDecisions un array di Decisioni binarie
	 * @return una una lista di decisioni binarie dando peso 0.5 a ciascun utente.
	 */
	public static ArrayList<Integer> getGreyListDecision(ArrayList<Integer> binaryDecisions){
		ArrayList<Integer> result= new ArrayList<Integer>();
		int presence=0;
		int absence=0;
		if(binaryDecisions.size()==1){result.add(binaryDecisions.get(0));}
		else{
			for(int i=0;i<binaryDecisions.size();i++){
				if(binaryDecisions.get(i)==0){ absence++;}
				else if (binaryDecisions.get(i)==1){ presence++;}
			}
			
			if(absence%2==0 && presence%2==0){
				for(int i=0;i<(absence/2);i++){
					result.add(0);
				}
				for(int i=0;i<(presence/2);i++){
					result.add(1);
				}
			}
			else if(absence%2!=0 && presence%2==0){
				for(int i=0;i<((absence-1)/2);i++){
					result.add(0);
				}
				for(int i=0;i<(presence/2);i++){
					result.add(1);
				}
			}
			else if(absence%2==0 && presence%2!=0){
				for(int i=0;i<(absence/2);i++){
					result.add(0);
				}
				for(int i=0;i<((presence-1)/2);i++){
					result.add(1);
				}
			}
			
			else if(absence%2!=0 && presence%2!=0){
				for(int i=0;i<((absence-1)/2);i++){
					result.add(0);
				}
				for(int i=0;i<((presence-1)/2);i++){
					result.add(1);
				}
				result.add(0);
			}
			
		}
		
		return result;
	}
	
	/**Dato un array di decisioni binarie, il metodo riporta una una lista di decisioni binarie dando peso 0.33 a ciascun utente.
	
	 * @param binaryDecisions un array di Decisioni binarie
	 * @return una una lista di decisioni binarie dando peso 0.33 a ciascun utente.
	 */
	public static ArrayList<Integer> getOneThirdGreyListDecision(ArrayList<Integer> binaryDecisions){
		ArrayList<Integer> result= new ArrayList<Integer>();
		int presence=0;
		int absence=0;
		if(binaryDecisions.size()==1){result.add(binaryDecisions.get(0));}
		else{
			for(int i=0;i<binaryDecisions.size();i++){
				if(binaryDecisions.get(i)==0){ absence++;}
				else if (binaryDecisions.get(i)==1){ presence++;}
			}
			
			if(absence%3==0 && presence%3==0){
				for(int i=0;i<(absence/3);i++){
					result.add(0);
				}
				for(int i=0;i<(presence/3);i++){
					result.add(1);
				}
			}
			else if(absence%3!=0 && presence%3==0){
				
			  for(int i=0;i<((int)((absence)/3));i++){
						result.add(0);
					}
				for(int i=0;i<(presence/3);i++){
					result.add(1);
				}
			}
			else if(absence%3==0 && presence%3!=0){
				for(int i=0;i<(absence/3);i++){
					result.add(0);}
				
				for(int i=0;i<((int)((presence)/3));i++){
						result.add(1);
						
				}
			}
			
			else if(absence%3!=0 && presence%3!=0){
				
					for(int i=0;i<((int)((absence)/3));i++){
						result.add(0);
					}
					
					for(int i=0;i<((int)((presence)/3));i++){
						result.add(1);
					
					}
							}
		}
		//System.out.println("absence "+absence);
		//System.out.println("presence"+presence);
		//System.out.println(result.size());
		return result;
	}
	
	
	/**Dato un array di decisioni binarie, il metodo riporta una una lista di decisioni binarie dando peso 0.25 a ciascun utente.
	
	 * @param binaryDecisions un array di Decisioni binarie
	 * @return una una lista di decisioni binarie dando peso 0.25 a ciascun utente.
	 */
	public static ArrayList<Integer> getOneFourthGreyListDecision(ArrayList<Integer> binaryDecisions){
		ArrayList<Integer> result= new ArrayList<Integer>();
		int presence=0;
		int absence=0;
		if(binaryDecisions.size()==1){result.add(binaryDecisions.get(0));}
		else{
			for(int i=0;i<binaryDecisions.size();i++){
				if(binaryDecisions.get(i)==0){ absence++;}
				else if (binaryDecisions.get(i)==1){ presence++;}
			}
			
			if(absence%4==0 && presence%4==0){
				for(int i=0;i<(absence/4);i++){
					result.add(0);
				}
				for(int i=0;i<(presence/4);i++){
					result.add(1);
				}
			}
			else if(absence%4!=0 && presence%4==0){
				
			  for(int i=0;i<((int)((absence)/4));i++){
						result.add(0);
					}
				for(int i=0;i<(presence/4);i++){
					result.add(1);
				}
			}
			else if(absence%4==0 && presence%4!=0){
				for(int i=0;i<(absence/4);i++){
					result.add(0);}
				
				for(int i=0;i<((int)((presence)/4));i++){
						result.add(1);
						
				}
			}
			
			else if(absence%4!=0 && presence%4!=0){
				
					for(int i=0;i<((int)((absence)/4));i++){
						result.add(0);
					}
					
					for(int i=0;i<((int)((presence)/4));i++){
						result.add(1);
					
					}
				}
		}
		
		return result;
	}
	
	/**Dato un array di decisioni binarie, il metodo riporta una una lista di decisioni binarie dando peso 0.2 a ciascun utente.
	  
	 * @param binaryDecisions un array di Decisioni binarie
	 * @return una una lista di decisioni binarie dando peso 0.16 a ciascun utente.
	 */
	public static ArrayList<Integer> getOneSixthGreyListDecision(ArrayList<Integer> binaryDecisions){
		ArrayList<Integer> result= new ArrayList<Integer>();
		int presence=0;
		int absence=0;
		if(binaryDecisions.size()==1){result.add(binaryDecisions.get(0));}
		else{
			for(int i=0;i<binaryDecisions.size();i++){
				if(binaryDecisions.get(i)==0){ absence++;}
				else if (binaryDecisions.get(i)==1){ presence++;}
			}
			
			if(absence%6==0 && presence%6==0){
				for(int i=0;i<(absence/6);i++){
					result.add(0);
				}
				for(int i=0;i<(presence/6);i++){
					result.add(1);
				}
			}
			else if(absence%6!=0 && presence%6==0){
				
			  for(int i=0;i<((int)((absence)/6));i++){
						result.add(0);
					}
				for(int i=0;i<(presence/6);i++){
					result.add(1);
				}
			}
			else if(absence%6==0 && presence%6!=0){
				for(int i=0;i<(absence/6);i++){
					result.add(0);}
				
				for(int i=0;i<((int)((presence)/6));i++){
						result.add(1);
						
				}
			}
			
			else if(absence%6!=0 && presence%6!=0){
				
					for(int i=0;i<((int)((absence)/6));i++){
						result.add(0);
					}
					
					for(int i=0;i<((int)((presence)/6));i++){
						result.add(1);
					
					}
				}
		}
		
		return result;
	}	
	
	/**Dato un array di decisioni binarie, il metodo riporta una lista vuota.
	 * @param binaryDecisions un array di Decisioni binarie
	 * @return una lista vuota
	 */
	public static ArrayList<Integer> getNullGreyListDecision(ArrayList<Integer> binaryDecisions){
		ArrayList<Integer> result= new ArrayList<Integer>();
		
		return result;}
	
		
	/** Metodo per il calcolo del mean detection time
	 * @param detection Probabilità di detection
	 * @return tempo medio di detection
	 */
	
	public static double computeMDT(double detection){
		double pd,mtd=0.0;
		if(detection==0){
		pd= 0.000001;
		}
		else{
		pd=detection/100;}
		mtd=((2-pd)/(2*pd));
		return  mtd;


	}
	
	
	
	/** Metodo per il calcolo del rapporto tra i mean detection time di due metodi
	 * @param detectionDividend Vettore di detection del metodo dividendo
	 * @param detectionDivisor Vettore di detection del metodo divisore
	 * @return Vettore contenente il rapporto tra i mean detection time dei vettori delle detection
	 */
	public static ArrayList<Double> computeMDTRatio(ArrayList<Double> detectionDividend,ArrayList<Double> detectionDivisor){
		ArrayList<Double> mtdDividend= new ArrayList<Double>();
		ArrayList<Double> mtdDivisor= new ArrayList<Double>();
		ArrayList<Double> mtdRatio= new ArrayList<Double>();
		for(double pdDividen: detectionDividend){
		double pd,mtd=0.0;
		if(pdDividen==0){
		pd= 0.01;
		}
		else{
		pd=pdDividen/100;}
		mtd=((2-pd)/(2*pd));
		mtdDividend.add(mtd);}
		
		for(double pdDivisor: detectionDivisor){
			double pd,mtd=0.0;
			if(pdDivisor==0){
			pd= 0.01;
			}
			else{
			pd=pdDivisor/100;}
			mtd=((2-pd)/(2*pd));
			mtdDivisor.add(mtd);}

       for(int i=0;i<mtdDividend.size();i++){
    	   double res=mtdDividend.get(i)/mtdDivisor.get(i);
    	   mtdRatio.add(res);  
       }
		return mtdRatio;
	}

	



}
