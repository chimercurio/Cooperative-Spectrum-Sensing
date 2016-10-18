package signalprocessing;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import agents.MaliciousSecondaryUser;
import agents.TrustedNode;
import agents.TrustedSecondaryUser;
import model.Signal;


/**
 * <p>Titolo: Utils</p>
 * <p>Descrizione: Classe che contiene funzioni utili necessarie alla generazione rapidaW di simulazioni. </p>
 * @author Pietro Coronas**/

public class Utils {	

	/**
	 * Metodo per generare una lista di utenti secondari fidati
	 * @param number Numero di utenti secondari fidati da generare
	 * @param s Segnale su cui effettuare la Detection
	 * @param SignalLength lunghezza del segnale
	 * @param energy Energia del segnale
	 * @param attempts Numero di prove su cui viene effettuata la simulazione
	 * @param inf Estremo inferiore di SNR su cui è stata effettuata la simulazione
	 * @param sup Estremo superiore di SNR su cui è stata effettuata la simulazione
	 * @param block Blocchi in cui dividere il segnale per l'energy Detector
	 * @return una lista di utenti secondari fidati
	 * **/

	public static ArrayList<TrustedSecondaryUser> createTrustedSecondaryUsers(int number,Signal s,
			int SignalLength, double energy, int attempts, int inf, int sup,int block){

		ArrayList<TrustedSecondaryUser> TrustedSecondaryUsers = new ArrayList<TrustedSecondaryUser>();
		for(int i=0;i<number;i++){
			TrustedSecondaryUser TSU=new TrustedSecondaryUser();
			TSU.listenChannel(s, SignalLength, energy, attempts, inf, sup, block);
			TrustedSecondaryUsers.add(TSU);
		}

		return TrustedSecondaryUsers;

	}
	
	/**
	 * Metodo per generare una lista di trusted Node
	 * @param number Numero di trusted node da generare
	 * @param s Segnale su cui effettuare la Detection
	 * @param SignalLength lunghezza del segnale
	 * @param energy Energia del segnale
	 * @param attempts Numero di prove su cui viene effettuata la simulazione
	 * @param inf Estremo inferiore di SNR su cui è stata effettuata la simulazione
	 * @param sup Estremo superiore di SNR su cui è stata effettuata la simulazione
	 * @param block Blocchi in cui dividere il segnale per l'energy Detector
	 * @return una lista di utenti secondari fidati
	 * **/
	
	public static ArrayList<TrustedNode> createTrustedNode(int number,Signal s,
			int SignalLength, double energy, int attempts, int inf, int sup,int block){

		ArrayList<TrustedNode> TrustedNode = new ArrayList<TrustedNode>();
		for(int i=0;i<number;i++){
			TrustedNode TSU=new TrustedNode();
			TSU.listenChannel(s, SignalLength, energy, attempts, inf, sup, block);
			TrustedNode.add(TSU);
		}

		return TrustedNode;

	}

	/**
	 * Metodo per generare una lista di utenti secondari malevoli
	 * @param number Numero di utenti secondari fidati da generare
	 * @param s Segnale su cui effettuare la Detection
	 * @param SignalLength lunghezza del segnale
	 * @param energy Energia del segnale
	 * @param attempts Numero di prove su cui viene effettuata la simulazione
	 * @param inf Estremo inferiore di SNR su cui è stata effettuata la simulazione
	 * @param sup Estremo superiore di SNR su cui è stata effettuata la simulazione
	 * @param block Blocchi in cui dividere il segnale per l'energy Detector
	 * @return una lista di utenti secondari malevoli
	 * **/

	public static ArrayList<MaliciousSecondaryUser> createMaliciousSecondaryUsers(int number,Signal s,
			int SignalLength, double energy, int attempts, int inf, int sup,int block){

		ArrayList<MaliciousSecondaryUser> MaliciousSecondaryUsers = new ArrayList<MaliciousSecondaryUser>();
		for(int i=0;i<number;i++){
			MaliciousSecondaryUser MSU=new MaliciousSecondaryUser();
			MSU.listenChannel(s, SignalLength, energy, attempts, inf, sup, block);
			MaliciousSecondaryUsers.add(MSU);
		}

		return MaliciousSecondaryUsers;

	}

	/**
	 * Prendendo in input una lista di utenti secondari fidati e la probabilità di falso allarma, questo metodo riporta
	 * una mappa avente come chiave l'identificatore dell'utente secondario e come valore una lista di decisioni binarie
	 * sulla presenza o assenza dell'utente primario per ogni valore di SNR
	 * @param TrustedSecondaryUsers Lista di utenti fidati
	 * @param pfa Probabilità di falso allarme
	 * @return una mappa avente come chiave l'identificatore dell'utente secondario e come valore una lista di decisioni binarie
	 * sulla presenza o assenza dell'utente primario per ogni valore di SNR
	 * @throws Exception 
	 * **/

	public static HashMap<String,ArrayList<ArrayList<Integer>>> genereteBinaryDecisionVectors(
			ArrayList<TrustedSecondaryUser> TrustedSecondaryUsers,double pfa) throws Exception {
		HashMap<String,ArrayList<ArrayList<Integer>>> userToBinaryDecision=new HashMap<String,ArrayList<ArrayList<Integer>>>();

		for(int i=0;i<TrustedSecondaryUsers.size();i++){
			userToBinaryDecision.put("TrustedSecondaryUser"+i, TrustedSecondaryUsers.get(i).computeBinaryDecisionVector(pfa));	
		}
		return userToBinaryDecision;
	}
	
	/**
	 * Prendendo in input una lista di trusted Node e la probabilità di falso allarma, questo metodo riporta
	 * una mappa avente come chiave l'identificatore del trusted Node e come valore una lista di decisioni binarie
	 * sulla presenza o assenza dell'utente primario per ogni valore di SNR
	 * @param TrustedNode Lista di trusted Node
	 * @param pfa Probabilità di falso allarme
	 * @return una mappa avente come chiave l'identificatore del trusted node e come valore una lista di decisioni binarie
	 * sulla presenza o assenza dell'utente primario per ogni valore di SNR
	 * @throws Exception 
	 * **/

	public static HashMap<String,ArrayList<ArrayList<Integer>>> genereteTrustedNodeBinaryDecisionVectors(
			ArrayList<TrustedNode> TrustedNode,double pfa) throws Exception {
		HashMap<String,ArrayList<ArrayList<Integer>>> trustedNodeToBinaryDecision=new HashMap<String,ArrayList<ArrayList<Integer>>>();

		for(int i=0;i<TrustedNode.size();i++){
			trustedNodeToBinaryDecision.put("TrustedNode_"+i, TrustedNode.get(i).computeBinaryDecisionVector(pfa));	
		}
		return trustedNodeToBinaryDecision;
	}


	/**
	 * Prendendo in input una lista di utenti malevoli, questo metodo riporta
	 * una mappa avente come chiave l'identificatore dell'utente secondario e come valore una lista di decisioni binarie
	 * contenente solo l'assenza dell'utente primario per ogni valore di SNR
	 * @param MaliciousSecondaryUsers lista di utenti malevoli
	 * @return una mappa avente come chiave l'identificatore dell'utente secondario e come valore una lista di decisioni binarie
	 * contenente solo l'assenza dell'utente primario per ogni valore di SNR
	 * @throws Exception 
	 * **/

	public static HashMap<String,ArrayList<ArrayList<Integer>>> genereteAbsenceBinaryDecisionVectors(
			ArrayList<MaliciousSecondaryUser> MaliciousSecondaryUsers) throws Exception {
		HashMap<String,ArrayList<ArrayList<Integer>>> userToBinaryDecision=new HashMap<String,ArrayList<ArrayList<Integer>>>();

		for(int i=0;i<MaliciousSecondaryUsers.size();i++){
			userToBinaryDecision.put("MaliciousAbsenceSecondaryUser"+i, MaliciousSecondaryUsers.get(i).computeAbsenceBinaryDecisionVector());	
		}
		return userToBinaryDecision;
	}

	/**
	 * Prendendo in input una lista di utenti malevoli, questo metodo riporta
	 * una mappa avente come chiave l'identificatore dell'utente secondario e come valore una lista di decisioni binarie
	 * contenente solo la presenza dell'utente primario per ogni valore di SNR
	 * @param MaliciousSecondaryUsers lista di utenti malevoli
	 * @return una mappa avente come chiave l'identificatore dell'utente secondario e come valore una lista di decisioni binarie
	 * contenente solo la presenza dell'utente primario per ogni valore di SNR
	 * @throws Exception 
	 * **/

	public static HashMap<String,ArrayList<ArrayList<Integer>>> generetePresenceBinaryDecisionVectors(
			ArrayList<MaliciousSecondaryUser> MaliciousSecondaryUsers) throws Exception {
		HashMap<String,ArrayList<ArrayList<Integer>>> userToBinaryDecision=new HashMap<String,ArrayList<ArrayList<Integer>>>();

		for(int i=0;i<MaliciousSecondaryUsers.size();i++){
			userToBinaryDecision.put("MaliciousPresenceSecondaryUser"+i, MaliciousSecondaryUsers.get(i).computePresenceBinaryDecisionVector());	
		}
		return userToBinaryDecision;
	}

	/**
	 * Prendendo in input una lista di utenti malevol e la probabilità di falso allarme, questo metodo riporta
	 * una mappa avente come chiave l'identificatore dell'utente secondario e come valore una lista di decisioni binarie
	 * contenente l'opposto del risultato dell'energy detector (1 se l'utente è assente e 0 se è presente) per ogni valore di SNR
	 * @param MaliciousSecondaryUsers lista di utenti malevoli
	 * @param pfa Probabilità di falso allarme
	 * @return una mappa avente come chiave l'identificatore dell'utente secondario e come valore una lista di decisioni binarie
	 * contenente l'opposto del risultato dell'energy detector (1 se l'utente è assente e 0 se è presente) per ogni valore di SNR
	 * @throws Exception 
	 * **/

	public static HashMap<String,ArrayList<ArrayList<Integer>>> genereteOppositeBinaryDecisionVectors(
			ArrayList<MaliciousSecondaryUser> MaliciousSecondaryUsers,double pfa) throws Exception {
		HashMap<String,ArrayList<ArrayList<Integer>>> userToBinaryDecision=new HashMap<String,ArrayList<ArrayList<Integer>>>();

		for(int i=0;i<MaliciousSecondaryUsers.size();i++){
			userToBinaryDecision.put("MaliciousOppositeSecondaryUser"+i, MaliciousSecondaryUsers.get(i).computeOppositeBinaryDecisionVector(pfa));	
		}
		return userToBinaryDecision;
	}	

	/**
	 * Prendendo in input una lista di utenti malevol e la probabilità di falso allarme, questo metodo riporta
	 * una mappa avente come chiave l'identificatore dell'utente secondario e come valore una lista di decisioni binarie
	 * contenente la deicisione errata, un numero di volte random, sulla presenza o assenza dell'utente primario
	 * @param MaliciousSecondaryUsers lista di utenti malevoli
	 * @param pfa Probabilità di falso allarme
	 * @return una mappa avente come chiave l'identificatore dell'utente secondario e come valore una lista di decisioni binarie
	 * contenente l'opposto del risultato dell'energy detector (1 se l'utente è assente e 0 se è presente) per ogni valore di SNR
	 * @throws Exception 
	 * **/

	public static HashMap<String,ArrayList<ArrayList<Integer>>> genereteIntelligentOppositeMaliciousBinaryDecisionVectors(
			ArrayList<MaliciousSecondaryUser> MaliciousSecondaryUsers,double pfa) throws Exception {
		HashMap<String,ArrayList<ArrayList<Integer>>> userToBinaryDecision=new HashMap<String,ArrayList<ArrayList<Integer>>>();

		for(int i=0;i<MaliciousSecondaryUsers.size();i++){
			userToBinaryDecision.put("MaliciousIntelligentOppositeSecondaryUser"+i, MaliciousSecondaryUsers.get(i).computeIntelligentOppositeBinaryDecisionVector(pfa));	
		}
		return userToBinaryDecision;
	}
	
	/**
	 * Prendendo in input una lista di utenti malevol e la probabilità di falso allarme, questo metodo riporta
	 * una mappa avente come chiave l'identificatore dell'utente secondario e come valore una lista di decisioni binarie
	 * contenente la decisione errata (in questo caso l'assenza del PU), un numero di volte random, sulla presenza o assenza dell'utente primario
	 * @param MaliciousSecondaryUsers lista di utenti malevoli
	 * @param pfa Probabilità di falso allarme
	 * @return una mappa avente come chiave l'identificatore dell'utente secondario e come valore una lista di decisioni binarie
	 * contenente l'opposto del risultato dell'energy detector (1 se l'utente è assente e 0 se è presente) per ogni valore di SNR
	 * @throws Exception 
	 * **/

	public static HashMap<String,ArrayList<ArrayList<Integer>>> genereteIntelligentAbsenceMaliciousBinaryDecisionVectors(
			ArrayList<MaliciousSecondaryUser> MaliciousSecondaryUsers,double pfa) throws Exception {
		HashMap<String,ArrayList<ArrayList<Integer>>> userToBinaryDecision=new HashMap<String,ArrayList<ArrayList<Integer>>>();

		for(int i=0;i<MaliciousSecondaryUsers.size();i++){
			userToBinaryDecision.put("MaliciousIntelligentAbsenceSecondaryUser"+i, MaliciousSecondaryUsers.get(i).computeIntelligentAbsenceBinaryDecisionVector(pfa));	
		}
		return userToBinaryDecision;
	}
	
	/**
	 * Prendendo in input una lista di utenti malevol e la probabilità di falso allarme, questo metodo riporta
	 * una mappa avente come chiave l'identificatore dell'utente secondario e come valore una lista di decisioni binarie
	 * contenente la decisione errata (in questo caso la presenza del PU), un numero di volte random, sulla presenza o assenza dell'utente primario
	 * @param MaliciousSecondaryUsers lista di utenti malevoli
	 * @param pfa Probabilità di falso allarme
	 * @return una mappa avente come chiave l'identificatore dell'utente secondario e come valore una lista di decisioni binarie
	 * contenente l'opposto del risultato dell'energy detector (1 se l'utente è assente e 0 se è presente) per ogni valore di SNR
	 * @throws Exception 
	 * **/

	public static HashMap<String,ArrayList<ArrayList<Integer>>> genereteIntelligentPresenceMaliciousBinaryDecisionVectors(
			ArrayList<MaliciousSecondaryUser> MaliciousSecondaryUsers,double pfa) throws Exception {
		HashMap<String,ArrayList<ArrayList<Integer>>> userToBinaryDecision=new HashMap<String,ArrayList<ArrayList<Integer>>>();

		for(int i=0;i<MaliciousSecondaryUsers.size();i++){
			userToBinaryDecision.put("MaliciousIntelligentPresenceSecondaryUser"+i, MaliciousSecondaryUsers.get(i).computeIntelligentPresenceBinaryDecisionVector(pfa));	
		}
		return userToBinaryDecision;
	}

	/** Metodo per la creazione di un file di testo contenente le soglie per l'energy detector. 
	 * @param length Lunghezza del rumore su cui calcolare le soglie
	 * @param energy Energia a cui generale il rumore
	 * @param attempts Numero di tentativi
	 * @param inf Estremo inferiore di SNR
	 * @param sup Estremo superiore di SNR
	 * @param pfa Probabilità di falso allarme
	 * @throws Exception
	 */

	public static void generateThreshold(int length,double energy,int attempts,int inf,int sup,double pfa) throws Exception{
		FileWriter w=new FileWriter("thresholds.txt");
		BufferedWriter b=new BufferedWriter(w);
		ArrayList<ArrayList<Double>> VectorNoiseEnergy=SignalProcessor.computeVectorsEnergy(null, length, energy, attempts, inf, sup);	
		int snr=inf;
		b.write(" ");
		b.write(String.valueOf(pfa+" "));     
		b.write("\n");
		for (int i = 0; i < VectorNoiseEnergy.size(); i++) {
			b.write(snr+" "+ SignalProcessor.computeEnergyDetectorThreshold(pfa, VectorNoiseEnergy.get(i)));
			b.write("\n");
			snr++;
		}
		b.close();
	}

	

	
	public static void generateMDTText(String title, HashMap<String, ArrayList<Double>> detection ,int inf,int sup,String path) throws IOException{
		FileWriter w=new FileWriter(path+".txt");
		BufferedWriter b=new BufferedWriter(w);
	for (String graphName : detection.keySet()) {
	
	b.write(graphName+" \n");
	for(int i=0;i<detection.get(graphName).size();i++){
		b.write("SNR: " +inf+i+" MDT: "+SignalProcessor.computeMDT(detection.get(graphName).get(i))+" \n ");

	}
	}	
	b.close();
	}
	
	public static void generateMDTRatioText(String title, HashMap<String, ArrayList<Double>> detection ,int inf,int sup,String path) throws IOException{
		FileWriter w=new FileWriter(path+".txt");
		BufferedWriter b=new BufferedWriter(w);
		for (String graphName : detection.keySet()) {
		
		b.write(graphName+" \n");
		for(int i=0;i<detection.get(graphName).size();i++){
			b.write("SNR: " +inf+i+" MDT Ratio: "+detection.get(graphName).get(i)+" \n ");

		}
		}	
		b.close();
		}



}
