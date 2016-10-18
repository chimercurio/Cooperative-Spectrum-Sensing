package agents;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import signalprocessing.SignalProcessor;
import detection.Detector;

public class FusionCenter {
	
	
		//Mappa snr->lista di utenti che dichiarano la presenza del PU di cardinalità pari ad ogni prova
		HashMap<Double,ArrayList<ArrayList<String>>>  snrToPresenceUsers;
		//Mappa snr->lista di utenti che dichiarano l'assenza del PU di cardinalità pari ad ogni prova
		HashMap<Double,ArrayList<ArrayList<String>>> snrToAbsenceUsers;
		//Mappa idUtente->reputazione
		HashMap<String,Double> usersReliabilities;
		//Mappa idUtente->array di informazioni (lista,hitsConsecutivi,Errori)
		HashMap<String,Reputation> usersToInfo;
		//Valori per il passaggio tra liste
		int K,L,M,N;
		int Nb,Na;
		//Lista utenti Esclusi
		ArrayList<String> excludedUsers;
		HashMap<String,Integer> excludedUsersStamp;

		ArrayList<String> reliableState;
		ArrayList<String> pendingState;
		ArrayList<String> discardedState;
		HashMap<String,Integer> discardedStateStamp;




		public FusionCenter(){
			snrToPresenceUsers= new HashMap<Double,ArrayList<ArrayList<String>>>();
			snrToAbsenceUsers= new HashMap<Double,ArrayList<ArrayList<String>>>();
			usersReliabilities= new HashMap<String,Double>();
			excludedUsers= new ArrayList<String>();	
			usersToInfo= new 	HashMap<String,Reputation>();
			reliableState= new ArrayList<String> ();
			pendingState= new ArrayList<String> ();
			discardedState= new ArrayList<String> ();
			discardedStateStamp=new HashMap<String,Integer>();
			excludedUsersStamp=new HashMap<String,Integer>();
			this.Na=1;
			this.Nb=9;

		}


		/**
		 * Questo metodo prende in input una mappa, contenente per ogni utente secondario (chiave) una lista di decisioni binarie calcolate
		 * per ogni SNR su un numero di prove P. Per ogni SNR prende le decisioni degli utenti secondari, li inserisce in un vettore e richiama il metodo
		 * di detection secondo la tecnica AND. Ritornerà la percentuale di Detection da parte Fusion Center
		 * @param inf Estremo inferiore dell'SNR
		 * @param sup Estremo superiore di SNR
		 * @param userToBinaryDecision Mappa che ha come chiave il nome dell'uente primario. Come valore ha una lista di liste: per ogni SNR ha una lista
		 * di lunghezza pari al numero di prove contenente la decisione binaria sulla presenza o assenza dell'utente primario da parte dell'utente secondario
		 * @return La percentuale di Detection da parte del Fusion Center dopo il metodo di fusione AND**/

		public  ArrayList<Double> andDecision(int inf,int sup,HashMap<String,ArrayList<ArrayList<Integer>>> userToBinaryDecision){
			ArrayList<Double> EnergyDetection = new  ArrayList<Double>();

			for(int i=0;i<(sup-inf);i++){
				ArrayList<ArrayList<Integer>> decisions=new ArrayList<ArrayList<Integer>>();
				for(String SU: userToBinaryDecision.keySet()){
					ArrayList<Integer>snrDecisionVector= userToBinaryDecision.get(SU).get(i);
					decisions.add(snrDecisionVector);}
				EnergyDetection.add(Detector.andFusionDetection(decisions));
			}
			return EnergyDetection;
		}


		/**
		 * Questo metodo prende in input una mappa, contenente per ogni utente secondario (chiave) una lista di decisioni binarie calcolate
		 * per ogni SNR su un numero di prove P. Per ogni SNR prende le decisioni degli utenti secondari, li inserisce in un vettore e richiama il metodo
		 * di detection secondo la tecnica OR. Ritornerà la percentuale di Detection da parte Fusion Center
		 * @param inf Estremo inferiore dell'SNR
		 * @param sup Estremo superiore di SNR
		 * @param userToBinaryDecision Mappa che ha come chiave il nome dell'uente primario. Come valore ha una lista di liste: per ogni SNR ha una lista
		 * di lunghezza pari al numero di prove contenente la decisione binaria sulla presenza o assenza dell'utente primario da parte dell'utente secondario
		 * @return La percentuale di Detection da parte del Fusion Center dopo il metodo di fusione OR**/

		public  ArrayList<Double> orDecision(int inf,int sup,HashMap<String,ArrayList<ArrayList<Integer>>> userToBinaryDecision){
			ArrayList<Double> EnergyDetection = new  ArrayList<Double>();

			for(int i=0;i<(sup-inf);i++){
				ArrayList<ArrayList<Integer>> decisions=new ArrayList<ArrayList<Integer>>();
				for(String SU: userToBinaryDecision.keySet()){
					ArrayList<Integer>snrDecisionVector= userToBinaryDecision.get(SU).get(i);
					decisions.add(snrDecisionVector);}

				EnergyDetection.add(Detector.orFusionDetection(decisions));
			}
			return EnergyDetection;
		}


		/**
		 * Questo metodo prende in input una mappa, contenente per ogni utente secondario (chiave) una lista di decisioni binarie calcolate
		 * per ogni SNR su un numero di prove P. Per ogni SNR prende le decisioni degli utenti secondari, li inserisce in un vettore e richiama il metodo
		 * di detection secondo la tecnica MAJORITY. Ritornerà la percentuale di Detection da parte Fusion Center
		 * @param inf Estremo inferiore dell'SNR
		 * @param sup Estremo superiore di SNR
		 * @param userToBinaryDecision Mappa che ha come chiave il nome dell'uente primario. Come valore ha una lista di liste: per ogni SNR ha una lista
		 * di lunghezza pari al numero di prove contenente la decisione binaria sulla presenza o assenza dell'utente primario da parte dell'utente secondario
		 * @return La percentuale di Detection da parte del Fusion Center dopo il metodo di fusione MAJORITY**/

		public  ArrayList<Double> majorityDecision(int inf,int sup,HashMap<String,ArrayList<ArrayList<Integer>>> userToBinaryDecision){

			ArrayList<Double> EnergyDetection = new  ArrayList<Double>();

			for(int i=0;i<(sup-inf);i++){
				ArrayList<ArrayList<Integer>> decisions=new ArrayList<ArrayList<Integer>>();
				for(String SU: userToBinaryDecision.keySet()){
					ArrayList<Integer>snrDecisionVector= userToBinaryDecision.get(SU).get(i);
					decisions.add(snrDecisionVector);}

				EnergyDetection.add(Detector.majorityFusionDetection(decisions));
			}
			return EnergyDetection;
		}


		/** Questo metodo prende in input, oltre agli estremi snr,  una Mappa che ha come chiave il nome dell'utente secondario. Come valore ha una lista di liste: per ogni SNR ha una lista
		 * di lunghezza pari al numero di prove contenente la decisione binaria sulla presenza o assenza dell'utente primario .
		 * Ritorna la % di Dectection calcolata utilizzando un metodo basato sulla divisione in liste degli utenti che tiene conto delle volte che sbagliano e delle volte
		 * che concordano con la decisione globale in modo consecutivo
		 * @param inf Estremo inferiore SNR
		 * @param sup Estremo superiore SNR 
		 * @param userToBinaryDecision Mappa che ha come chiave il nome dell'utente secondario. Come valore ha una lista di liste: per ogni SNR ha una lista
		 * di lunghezza pari al numero di prove contenente la decisione binaria sulla presenza o assenza dell'utente primario da parte dell'utente secondario
		 * @param attempts Numero di prove
		 * @param K numero di hits consecutivi necessario per il passaggio da lista grigia lista bianca
		 * @param L numero di hits consecutivi necessario per il passaggio da lista nera lista grigia
		 * @param M numero di errori necessario per il passaggio da lista bianca lista grigia
		 * @param N numero di errori necessario per il passaggio da lista grigia lista nera
		 * @param typeMSU Tipo di MSU presente nella comunicazione. Parametro necessario per il salvatagigo del file di testo
		 * @return  Ritorna la % di Dectection calcolata utilizzando un metodo basato sulla divisione in liste degli utenti che tiene conto delle volte che sbagliano e delle volte
		 * che concordano con la decisione globale in modo consecutivo.
		 * @throws IOException 
		 */

		public  ArrayList<Double> ListBasedDecision(String path,int inf,int sup,HashMap<String,ArrayList<ArrayList<Integer>>> userToBinaryDecision,int attempts,
				int K,int L,int M,int N,String typeMSU) throws IOException{
			this.K=K; 
			this.L=L;
			this.M=M;
			this.N=N;
			FileWriter w=new FileWriter(path+"Proposed_User_excluded"+"_"+typeMSU+".txt");
			BufferedWriter b=new BufferedWriter(w);
			HashMap<Double,Double> listBasedDetection=new HashMap<Double,Double>();
			createSnrToUsers(inf,sup,userToBinaryDecision,attempts);
			for(Double snr: this.snrToPresenceUsers.keySet()){
				b.write(" ");
				b.write("------------------- SNR="+snr+" -------------------------"+" \n");
				inizializeValue(userToBinaryDecision);
				ArrayList<Integer> globalDecisions= new ArrayList<Integer>();
				for(int attempt=0;attempt<this.snrToPresenceUsers.get(snr).size();attempt++){
					b.write("-------------------SNR="+snr+" Prova="+attempt+"-------------------------"+" \n");
					int whiteNumber=0;
					int blackNumber=0;
					int greyNumber=0;
					for(String SU: this.usersToInfo.keySet()){
						if(this.usersToInfo.get(SU).getFlag()==0){whiteNumber++;}
						if(this.usersToInfo.get(SU).getFlag()==1){greyNumber++;}
						if(this.usersToInfo.get(SU).getFlag()==2){blackNumber++;}

					}

					HashMap<String,Integer> binaryDecisionsWhite=computeUserToDecisionWhite(this.snrToPresenceUsers.get(snr).get(attempt),
							this.snrToAbsenceUsers.get(snr).get(attempt));
					HashMap<String,Integer> binaryDecisionsGrey=computeUserToDecisionGrey(this.snrToPresenceUsers.get(snr).get(attempt),
							this.snrToAbsenceUsers.get(snr).get(attempt));

					Integer globalDecision=computeGlobalDecision(binaryDecisionsWhite,binaryDecisionsGrey);
					b.write("Global Decision: "+globalDecision+" |");
					b.write(" Presence: "+ this.snrToPresenceUsers.get(snr).get(attempt).size()+"| ");
					b.write(" Absence: "+ this.snrToAbsenceUsers.get(snr).get(attempt).size()+"| ");
					b.write(" White list: "+ whiteNumber+"| ");
					b.write(" Grey list: "+ greyNumber+"| ");
					b.write(" Black list: "+ blackNumber+"| ");
					b.write(" \n");

					globalDecisions.add(globalDecision);
					updateValue(globalDecision,this.snrToPresenceUsers.get(snr).get(attempt),
							this.snrToAbsenceUsers.get(snr).get(attempt));

				}
				double detection=Detector.reputationBasedDetection(globalDecisions);
				listBasedDetection.put(snr,detection);
			}
			b.close();
			return SignalProcessor.orderSignal(listBasedDetection);
		}

		public  ArrayList<Double> ListBasedDecisionOptimazed(String path,String pathUE,int inf,int sup,HashMap<String,ArrayList<ArrayList<Integer>>> userToBinaryDecision,int attempts,
				int K,int L,int M,int N,String typeMSU) throws IOException{
			this.K=K; 
			this.L=L;
			this.M=M;
			this.N=N;
			//FileWriter w=new FileWriter(path+"ProposedOptimazed_User_Exluded"+"_"+typeMSU+".txt");
			FileWriter w2=new FileWriter(pathUE+"ProposedOptimazed_User_Exluded"+"_"+typeMSU+".txt");
			//BufferedWriter b=new BufferedWriter(w);
			BufferedWriter b2=new BufferedWriter(w2);
			HashMap<Double,Double> listBasedDetection=new HashMap<Double,Double>();
			createSnrToUsers(inf,sup,userToBinaryDecision,attempts);
			for(Double snr: this.snrToPresenceUsers.keySet()){
				//b.write(" ");
				//b.write("------------------- SNR="+snr+" -------------------------"+" \n");
				inizializeValue(userToBinaryDecision);
				ArrayList<Integer> globalDecisions= new ArrayList<Integer>();
				for(int attempt=0;attempt<this.snrToPresenceUsers.get(snr).size();attempt++){
					//b.write("-------------------SNR="+snr+" Prova="+attempt+"-------------------------"+" \n");
					/**int whiteNumber=0;
					int blackNumber=0;
					int greyNumber=0;
					for(String SU: this.usersToInfo.keySet()){
						if(this.usersToInfo.get(SU).getFlag()==0){whiteNumber++;}
						if(this.usersToInfo.get(SU).getFlag()==1){greyNumber++;}
						if(this.usersToInfo.get(SU).getFlag()==2){blackNumber++;}
					}**/

					HashMap<String,Integer> binaryDecisionsWhite=computeUserToDecisionWhite(this.snrToPresenceUsers.get(snr).get(attempt),
							this.snrToAbsenceUsers.get(snr).get(attempt));
					HashMap<String,Integer> binaryDecisionsGrey=computeUserToDecisionGrey(this.snrToPresenceUsers.get(snr).get(attempt),
							this.snrToAbsenceUsers.get(snr).get(attempt));

					Integer globalDecision=computeGlobalDecisionOptimazed(binaryDecisionsWhite,binaryDecisionsGrey);
					/**b.write("Global Decision: "+globalDecision+" |");
					b.write(" Presence: "+ this.snrToPresenceUsers.get(snr).get(attempt).size()+"| ");
					b.write(" Absence: "+ this.snrToAbsenceUsers.get(snr).get(attempt).size()+"| ");
					b.write(" White list: "+ whiteNumber+"| ");
					b.write(" Grey list: "+ greyNumber+"| ");
					b.write(" Black list: "+ blackNumber+"| ");
					b.write(" \n");**/

					globalDecisions.add(globalDecision);
					updateValue(globalDecision,this.snrToPresenceUsers.get(snr).get(attempt),
							this.snrToAbsenceUsers.get(snr).get(attempt));

				}
				b2.write("---------------------- "+snr+" ----------------------------------------"+"\n");
				ArrayList<String> SUADD= new ArrayList<String>();
				b2.write("Black List User: "+"\n");
				for(String SU: this.usersToInfo.keySet()){
					if(this.usersToInfo.get(SU).getFlag()==2 & !SUADD.contains(SU)){b2.write(SU+"\n"); SUADD.add(SU);}}
				b2.write("\n");
				b2.write("Grey List User: "+"\n");
				for(String SU: this.usersToInfo.keySet()){
					if(this.usersToInfo.get(SU).getFlag()==1& !SUADD.contains(SU)){b2.write(SU+"\n");SUADD.add(SU);};}
				b2.write("\n");
				b2.write("White List User: "+"\n");
				for(String SU: this.usersToInfo.keySet()){
					if(this.usersToInfo.get(SU).getFlag()==0& !SUADD.contains(SU)){b2.write(SU+"\n");SUADD.add(SU);}}
				

				b2.write("\n");
				
				double detection=Detector.reputationBasedDetection(globalDecisions);
				listBasedDetection.put(snr,detection);
			}
			//b.close();
			b2.close();
			return SignalProcessor.orderSignal(listBasedDetection);
		}


		/** QUesto metodo inizializza la mappa userToInfo, inserendo come chiave l'identificativo dell'utente secondario, e come valore un 
		 * array di 3 elementi contenente gli hits consecutivi, gli errori e la lista di appartenenza. Tutti questo valori vengono 
		 * inizialmente posti a 0 (0 hits,0 errori e lista bianca)
		 * @param userToBinaryDecision Mappa che ha come chiave il nome dell'uente secondario. Come valore ha una lista di liste: per ogni SNR ha una lista
		 * di lunghezza pari al numero di prove contenente la decisione binaria sulla presenza o assenza dell'utente primario.
		 * **/

		public void inizializeValue(HashMap<String,ArrayList<ArrayList<Integer>>> userToBinaryDecision){
			this.usersToInfo.clear();

			for(String SU: userToBinaryDecision.keySet()){
				this.usersToInfo.put(SU, new Reputation());		 
			}
		}

		/** Questo metodo ritorna una mappa contenente come chiave l'utente secondario appartenente alla lista bianca e come valore
		 * la decisione binaria relativa alla presenza o assenza dell'utente primario
		 * @param presenceUsers lista di utenti che affermano la presenza dell'utente primario
		 * @param absenceUsers Lista di utenti che affermano l'assenza dell'utente primario
		 * @return Una mappa contenente come chiave l'utente secondario appartenente alla lista bianca e come valore la decisione binaria
		 * relativa alla presenza o assenza dell'utente primario
		 */

		public HashMap<String,Integer> computeUserToDecisionWhite(ArrayList<String> presenceUsers,ArrayList<String> absenceUsers){
			HashMap<String,Integer> binaryDecisionsWhite= new HashMap<String,Integer>();
			for(String SU:presenceUsers){
				int flag=this.usersToInfo.get(SU).getFlag();
				if(flag==0){
					binaryDecisionsWhite.put(SU, 1);
				}}
			for(String SU2:absenceUsers){
				int flag=this.usersToInfo.get(SU2).getFlag();
				if(flag==0){
					binaryDecisionsWhite.put(SU2,0);
				}
			}

			return binaryDecisionsWhite;
		}

		/** Questo metodo ritorna una mappa contenente come chiave l'utente secondario appartenente alla lista grigia e come valore
		 * la decisione binaria relativa alla presenza o assenza dell'utente primario
		 * @param presenceUsers lista di utenti che affermano la presenza dell'utente primario
		 * @param absenceUsers Lista di utenti che affermano l'assenza dell'utente primario
		 * @return Una mappa contenente come chiave l'utente secondario appartenente alla lista grigia e come valore la decisione binaria
		 * relativa alla presenza o assenza dell'utente primario
		 */

		public HashMap<String,Integer> computeUserToDecisionGrey(ArrayList<String> presenceUsers,ArrayList<String> absenceUsers){
			HashMap<String,Integer> binaryDecisionsWhite= new HashMap<String,Integer>();
			for(String SU:presenceUsers){
				int flag=this.usersToInfo.get(SU).getFlag();
				if(flag==1){
					binaryDecisionsWhite.put(SU, 1);
				}}
			for(String SU2:absenceUsers){
				int flag=this.usersToInfo.get(SU2).getFlag();
				if(flag==1){
					binaryDecisionsWhite.put(SU2,0);
				}
			}

			return binaryDecisionsWhite;
		}


		/** Questo metodo aggiorna la mappa usersToInfo. In particolare aggiorna gli hits consecutivi, gli errori e si occupa del cambio di lista
		 * degli utenti in base ai valori KLMN.
		 * Se un utente ha un numero di hits consecutivi maggiore o uguale a K e si trova in lista grigia va in lista bianca
		 * Se un utente ha un numero di hits consecutivi maggiore o uguale a L e si trova in lista nera va in lista grigia
		 * Se un utente ha un numero di errori maggiore o uguale a M e si trova in lista bianca va in lista grigia
		 * Se un utente ha un numero di errori maggiore o uguale a N e si trova in lista grigia va in lista nera
		 * @param globalDecision decisione globale
		 * @param presenceSU lista di utenti che affermano la presenza dell'utente primario
		 * @param absenceSU lista di utenti che affermano l'assenza dell'utente primario
		 */

		public void updateValue(int globalDecision,ArrayList<String>  presenceSU,ArrayList<String>  absenceSU){
			if(globalDecision==1){
				for(int i=0;i<presenceSU.size();i++){
					Reputation reputation=this.usersToInfo.get(presenceSU.get(i));
					//Incremento gli hits consecutivi
					reputation.incrementConsecutiveHits();
					//se gli hits superano K e si trova in lista grigia
					if(reputation.getConsecutiveHits()>=this.K & reputation.getFlag()==1){
						reputation.setFlag(0);


					}
					//se gli hits superano L e si trova in lista nera
					else if(reputation.getConsecutiveHits()>=this.L & reputation.getFlag()==2){
						//passa in lista grigia
						reputation.setFlag(1);


					}
				}
				for(int j=0;j<absenceSU.size();j++){
					Reputation reputation=this.usersToInfo.get(absenceSU.get(j));
					//incremento gli errori
					reputation.incrementErrorCount();



					//se gli ERRORI superano M e si trova in lista bianca
					if(reputation.getErrorCount()>=this.M & reputation.getFlag()==0){
						//passa in lista grigia
						reputation.setFlag(1);


					}
					//se gli errori superano L e si trova in lista grigia
					else if(reputation.getErrorCount()>=this.N & reputation.getFlag()==1){
						reputation.setFlag(2);;


					}

				}
			}
			else if(globalDecision==0){
				for(int i=0;i<presenceSU.size();i++){
					Reputation reputation=this.usersToInfo.get(presenceSU.get(i));
					//incremento gli errori
					reputation.incrementErrorCount();



					//se gli ERRORI superano M e si trova in lista bianca
					if(reputation.getErrorCount()>=this.M & reputation.getFlag()==0){
						reputation.setFlag(1);

					}
					//se gli errori superano L e si trova in lista grigia
					else if(reputation.getErrorCount()>=this.N & reputation.getFlag()==1){
						reputation.setFlag(2);

					}
				}

				for(int j=0;j<absenceSU.size();j++){

					Reputation reputation=this.usersToInfo.get(absenceSU.get(j));
					//Incremento gli hits consecutivi
					reputation.incrementConsecutiveHits();

					//se gli hits superano K e si trova in lista grigia
					if(reputation.getConsecutiveHits()>=this.K & reputation.getFlag()==1){
						reputation.setFlag(0);;

					}
					//se gli hits superano L e si trova in lista nera
					else if(reputation.getConsecutiveHits()>=this.L & reputation.getFlag()==2){
						reputation.setFlag(1);


					}
				}
			}
		}



		/** Questo metodo prende in input le decisioni binarie degli utenti presenti in lista grigia e lista bianca e ritorna
		 * una decisione globale sulla presenza o assenza dell'utente primario.
		 * @param binaryDecisionsWhite Mappa contenente le decisioni binarie degli utenti appartenenti alla lista bianca
		 * @param binaryDecisionsGrey Mappa contenente le decisioni binarie degli utenti appartenenti alla lsita grigia
		 * @return la decisione globale
		 */

		public int computeGlobalDecision(HashMap<String,Integer> binaryDecisionsWhite,HashMap<String,Integer> binaryDecisionsGrey){
			ArrayList<Integer> binaryDecisionAllList=new ArrayList<Integer>();
			if(binaryDecisionsWhite.size()!=0){

				binaryDecisionAllList.addAll(binaryDecisionsWhite.values());}
			if(binaryDecisionsGrey.size()!=0){
				ArrayList<Integer> greyDecisions= new ArrayList<Integer>();
				for(String SU: binaryDecisionsGrey.keySet() ){
					greyDecisions.add( binaryDecisionsGrey.get(SU));
				}

				binaryDecisionAllList.addAll(SignalProcessor.getGreyListDecision(greyDecisions));
			}
			//binaryDecisionAllList.addAll(binaryDecisionsGrey.values());}
			return Detector.majorityFusionRule(binaryDecisionAllList);
		}

		public int computeGlobalDecisionOptimazed(HashMap<String,Integer> binaryDecisionsWhite,HashMap<String,Integer> binaryDecisionsGrey){
			ArrayList<Integer> binaryDecisionAllList=new ArrayList<Integer>();
			if(binaryDecisionsWhite.size()!=0){
				for(String SU: binaryDecisionsWhite.keySet()){
					if(SU.contains("Node")){
						binaryDecisionAllList.add(binaryDecisionsWhite.get(SU));
						binaryDecisionAllList.add(binaryDecisionsWhite.get(SU));
						//binaryDecisionAllList.add(binaryDecisionsWhite.get(SU));

					}
				}
				binaryDecisionAllList.addAll(binaryDecisionsWhite.values());
				binaryDecisionAllList.addAll(binaryDecisionsWhite.values());}
			if(binaryDecisionsGrey.size()!=0){
				//	ArrayList<Integer> greyDecisions= new ArrayList<Integer>();
				//	for(String SU: binaryDecisionsGrey.keySet() ){
				//		greyDecisions.add( binaryDecisionsGrey.get(SU));
				//	}

				//binaryDecisionAllList.addAll(Utils.getGreyListDecision(greyDecisions));
				//}
				binaryDecisionAllList.addAll(binaryDecisionsGrey.values());}
			return Detector.majorityFusionRule(binaryDecisionAllList);
		}


		/** Questo metodo prende in input, oltre agli estremi snr,  una Mappa che ha come chiave il nome dell'utente secondario. Come valore ha una lista di liste: per ogni SNR ha una lista
		 * di lunghezza pari al numero di prove contenente la decisione binaria sulla presenza o assenza dell'utente primario. A differenza del metodo normale, questo prevede anche i Trusted Node
		 * Ritorna la % di Dectection calcolata utilizzando un metodo basato sulla divisione in liste degli utenti che tiene conto delle volte che sbagliano e delle volte
		 * che concordano con la decisione globale in modo consecutivo
		 * @param inf Estremo inferiore SNR
		 * @param sup Estremo superiore SNR 
		 * @param userToBinaryDecision Mappa che ha come chiave il nome dell'utente secondario. Come valore ha una lista di liste: per ogni SNR ha una lista
		 * di lunghezza pari al numero di prove contenente la decisione binaria sulla presenza o assenza dell'utente primario da parte dell'utente secondario
		 * @param trustedNodeToBinaryDecision Mappa che ha come chiave il nome del trusted node. Come valore ha una lista di liste: per ogni SNR ha una lista
		 * di lunghezza pari al numero di prove contenente la decisione binaria sulla presenza o assenza dell'utente primario da parte dell'utente secondario
		 * @param attempts Numero di prove
		 * @param K numero di hits consecutivi necessario per il passaggio da lista grigia lista bianca
		 * @param L numero di hits consecutivi necessario per il passaggio da lista nera lista grigia
		 * @param M numero di errori necessario per il passaggio da lista bianca lista grigia
		 * @param N numero di errori necessario per il passaggio da lista grigia lista nera
		 * @param typeMSU Tipo di MSU presente nella comunicazione. Parametro necessario per il salvatagigo del file di testo
		 * @return  Ritorna la % di Dectection calcolata utilizzando un metodo basato sulla divisione in liste degli utenti che tiene conto delle volte che sbagliano e delle volte
		 * che concordano con la decisione globale in modo consecutivo.
		 * @throws IOException 
		 */

		public  ArrayList<Double> ListBasedWithTrustedNodeDecision(String path,int inf,int sup,HashMap<String,ArrayList<ArrayList<Integer>>> userToBinaryDecision,HashMap<String,ArrayList<ArrayList<Integer>>> trustedNodeToBinaryDecision,int attempts,
				int K,int L,int M,int N,String typeMSU) throws IOException{
			this.K=K; 
			this.L=L;
			this.M=M;
			this.N=N;
			//FileWriter w=new FileWriter(path+"Proposed_User_excluded"+"TN_"+typeMSU+".txt");
			//BufferedWriter b=new BufferedWriter(w);
			HashMap<Double,Double> listBasedDetection=new HashMap<Double,Double>();
			createSnrToUsers(inf,sup,userToBinaryDecision,trustedNodeToBinaryDecision,attempts);
			for(Double snr: this.snrToPresenceUsers.keySet()){
				//b.write(" ");
				//b.write("------------------- SNR="+snr+" -------------------------"+" \n");
				inizializeValueTN(userToBinaryDecision,trustedNodeToBinaryDecision);
				ArrayList<Integer> globalDecisions= new ArrayList<Integer>();
				for(int attempt=0;attempt<this.snrToPresenceUsers.get(snr).size();attempt++){
					//b.write("-------------------SNR="+snr+" Prova="+attempt+"-------------------------"+" \n");
				/**	int whiteNumber=0;
					int blackNumber=0;
					int greyNumber=0;
					for(String SU: this.usersToInfo.keySet()){
						if(this.usersToInfo.get(SU).getFlag()==0){whiteNumber++;}
						if(this.usersToInfo.get(SU).getFlag()==5){whiteNumber++;}
						if(this.usersToInfo.get(SU).getFlag()==1){greyNumber++;}
						if(this.usersToInfo.get(SU).getFlag()==2){blackNumber++;}
					}**/

					HashMap<String,Integer> binaryDecisionsWhite=computeUserToDecisionWhiteTN(this.snrToPresenceUsers.get(snr).get(attempt),
							this.snrToAbsenceUsers.get(snr).get(attempt));
					HashMap<String,Integer> binaryDecisionsGrey=computeUserToDecisionGrey(this.snrToPresenceUsers.get(snr).get(attempt),
							this.snrToAbsenceUsers.get(snr).get(attempt));

					Integer globalDecision=computeGlobalDecision(binaryDecisionsWhite,binaryDecisionsGrey);
					/**b.write("Global Decision: "+globalDecision+" |");
					b.write(" Presence: "+ this.snrToPresenceUsers.get(snr).get(attempt).size()+"| ");
					b.write(" Absence: "+ this.snrToAbsenceUsers.get(snr).get(attempt).size()+"| ");
					b.write(" White list: "+ whiteNumber+"| ");
					b.write(" Grey list: "+ greyNumber+"| ");
					b.write(" Black list: "+ blackNumber+"| ");
					b.write(" \n");**/

					globalDecisions.add(globalDecision);
					updateValueTN(globalDecision,this.snrToPresenceUsers.get(snr).get(attempt),
							this.snrToAbsenceUsers.get(snr).get(attempt));

				}
				double detection=Detector.reputationBasedDetection(globalDecisions);
				listBasedDetection.put(snr,detection);
			}
			//b.close();
			return SignalProcessor.orderSignal(listBasedDetection);
		}

		public  ArrayList<Double> ListBasedWithTrustedNodeDecisionOptimazed(String path,String pathUE,int inf,int sup,HashMap<String,ArrayList<ArrayList<Integer>>> userToBinaryDecision,HashMap<String,ArrayList<ArrayList<Integer>>> trustedNodeToBinaryDecision,int attempts,
				int K,int L,int M,int N,String typeMSU) throws IOException{
			this.K=K; 
			this.L=L;
			this.M=M;
			this.N=N;
		//	FileWriter w=new FileWriter(path+"ProposedOptimized_User_Excluded_"+"TN_"+typeMSU+".txt");
			FileWriter w2=new FileWriter(pathUE+"ProposedOptimized_User_Excluded_"+"TN_"+typeMSU+".txt");
		//	BufferedWriter b=new BufferedWriter(w);
			BufferedWriter b2=new BufferedWriter(w2);
			HashMap<Double,Double> listBasedDetection=new HashMap<Double,Double>();
			createSnrToUsers(inf,sup,userToBinaryDecision,trustedNodeToBinaryDecision,attempts);
			for(Double snr: this.snrToPresenceUsers.keySet()){
				//b.write(" ");
				//b.write("------------------- SNR="+snr+" -------------------------"+" \n");
				inizializeValueTN(userToBinaryDecision,trustedNodeToBinaryDecision);
				ArrayList<Integer> globalDecisions= new ArrayList<Integer>();
				for(int attempt=0;attempt<this.snrToPresenceUsers.get(snr).size();attempt++){
					//b.write("-------------------SNR="+snr+" Prova="+attempt+"-------------------------"+" \n");
					/**int whiteNumber=0;
					int blackNumber=0;
					int greyNumber=0;
					for(String SU: this.usersToInfo.keySet()){
						if(this.usersToInfo.get(SU).getFlag()==0){whiteNumber++;}
						if(this.usersToInfo.get(SU).getFlag()==5){whiteNumber++;}
						if(this.usersToInfo.get(SU).getFlag()==1){greyNumber++;}
						if(this.usersToInfo.get(SU).getFlag()==2){blackNumber++;}
					}**/

					HashMap<String,Integer> binaryDecisionsWhite=computeUserToDecisionWhiteTN(this.snrToPresenceUsers.get(snr).get(attempt),
							this.snrToAbsenceUsers.get(snr).get(attempt));
					HashMap<String,Integer> binaryDecisionsGrey=computeUserToDecisionGrey(this.snrToPresenceUsers.get(snr).get(attempt),
							this.snrToAbsenceUsers.get(snr).get(attempt));

					Integer globalDecision=computeGlobalDecisionOptimazed(binaryDecisionsWhite,binaryDecisionsGrey);
					/**b.write("Global Decision: "+globalDecision+" |");
					b.write(" Presence: "+ this.snrToPresenceUsers.get(snr).get(attempt).size()+"| ");
					b.write(" Absence: "+ this.snrToAbsenceUsers.get(snr).get(attempt).size()+"| ");
					b.write(" White list: "+ whiteNumber+"| ");
					b.write(" Grey list: "+ greyNumber+"| ");
					b.write(" Black list: "+ blackNumber+"| ");
					b.write(" \n");**/

					globalDecisions.add(globalDecision);
					updateValueTN(globalDecision,this.snrToPresenceUsers.get(snr).get(attempt),
							this.snrToAbsenceUsers.get(snr).get(attempt));

				}
				b2.write("---------------------- "+snr+" ----------------------------------------"+"\n");
				ArrayList<String> SUADD= new ArrayList<String>();
				b2.write("Black List User: "+"\n");
				for(String SU: this.usersToInfo.keySet()){
					if(this.usersToInfo.get(SU).getFlag()==2 & !SUADD.contains(SU)){b2.write(SU+"\n");SUADD.add(SU);}}
				b2.write("\n");
				b2.write("Grey List User: "+"\n");
				for(String SU: this.usersToInfo.keySet()){
					if(this.usersToInfo.get(SU).getFlag()==1 & !SUADD.contains(SU) ){b2.write(SU+"\n");SUADD.add(SU);;}}
				b2.write("\n");			
				b2.write("White List User: "+"\n");
				for(String SU: this.usersToInfo.keySet()){
					if((this.usersToInfo.get(SU).getFlag()==0 | this.usersToInfo.get(SU).getFlag()==5)& !SUADD.contains(SU)){b2.write(SU+"\n");SUADD.add(SU);;};}
				b2.write("\n");
				double detection=Detector.reputationBasedDetection(globalDecisions);
				listBasedDetection.put(snr,detection);
			}
			//b.close();
			b2.close();
			return SignalProcessor.orderSignal(listBasedDetection);
		}

		/** QUesto metodo inizializza la mappa userToInfo, inserendo come chiave l'identificativo dell'utente secondario, e come valore un 
		 * array di 3 elementi contenente gli hits consecutivi, gli errori e la lista di appartenenza. Tutti questo valori vengono 
		 * inizialmente posti a 0 (0 hits,0 errori e lista bianca). Tiene conto anche dei trusted Node, settandone i flag a 5
		 * @param userToBinaryDecision Mappa che ha come chiave il nome dell'uente secondario. Come valore ha una lista di liste: per ogni SNR ha una lista
		 * di lunghezza pari al numero di prove contenente la decisione binaria sulla presenza o assenza dell'utente primario.
		 * **/

		public void inizializeValueTN(HashMap<String,ArrayList<ArrayList<Integer>>> userToBinaryDecision,
				HashMap<String,ArrayList<ArrayList<Integer>>> trustedNodeToBinaryDecision){
			this.usersToInfo.clear();

			for(String SU: userToBinaryDecision.keySet()){
				this.usersToInfo.put(SU, new Reputation());		 
			}
			for(String TN: trustedNodeToBinaryDecision.keySet()){
				Reputation reputationTN=new Reputation();
				reputationTN.setFlag(5);
				this.usersToInfo.put(TN, reputationTN);		 
			}
		}

		/** Questo metodo ritorna una mappa contenente come chiave l'utente secondario appartenente alla lista bianca e come valore
		 * la decisione binaria relativa alla presenza o assenza dell'utente primario. Tiene conto anche dei Trusted Node
		 * @param presenceUsers lista di utenti che affermano la presenza dell'utente primario
		 * @param absenceUsers Lista di utenti che affermano l'assenza dell'utente primario
		 * @return Una mappa contenente come chiave l'utente secondario appartenente alla lista bianca e come valore la decisione binaria
		 * relativa alla presenza o assenza dell'utente primario
		 */

		public HashMap<String,Integer> computeUserToDecisionWhiteTN(ArrayList<String> presenceUsers,ArrayList<String> absenceUsers){
			HashMap<String,Integer> binaryDecisionsWhite= new HashMap<String,Integer>();
			for(String SU:presenceUsers){
				int flag=this.usersToInfo.get(SU).getFlag();
				if(flag==0 || flag==5){
					binaryDecisionsWhite.put(SU, 1);
				}}
			for(String SU2:absenceUsers){
				int flag=this.usersToInfo.get(SU2).getFlag();
				if(flag==0 || flag==5){
					binaryDecisionsWhite.put(SU2,0);
				}
			}

			return binaryDecisionsWhite;
		}



		/** Questo metodo aggiorna la mappa usersToInfo. In particolare aggiorna gli hits consecutivi, gli errori e si occupa del cambio di lista
		 * degli utenti in base ai valori KLMN. Tiene conto anche dei Trusted Node, che lascia in lista bianca indipendentemente da errorCount e ConsecutiveHITS
		 * Se un utente ha un numero di hits consecutivi maggiore o uguale a K e si trova in lista grigia va in lista bianca
		 * Se un utente ha un numero di hits consecutivi maggiore o uguale a L e si trova in lista nera va in lista grigia
		 * Se un utente ha un numero di errori maggiore o uguale a M e si trova in lista bianca va in lista grigia
		 * Se un utente ha un numero di errori maggiore o uguale a N e si trova in lista grigia va in lista nera
		 * @param globalDecision decisione globale
		 * @param presenceSU lista di utenti che affermano la presenza dell'utente primario
		 * @param absenceSU lista di utenti che affermano l'assenza dell'utente primario
		 */

		public void updateValueTN(int globalDecision,ArrayList<String>  presenceSU,ArrayList<String>  absenceSU){
			if(globalDecision==1){
				for(int i=0;i<presenceSU.size();i++){
					Reputation reputation=this.usersToInfo.get(presenceSU.get(i));
					if(reputation.getFlag()!=5){
						//Incremento gli hits consecutivi
						reputation.incrementConsecutiveHits();
						//se gli hits superano K e si trova in lista grigia
						if(reputation.getConsecutiveHits()>=this.K & reputation.getFlag()==1){
							reputation.setFlag(0);


						}
						//se gli hits superano L e si trova in lista nera
						else if(reputation.getConsecutiveHits()>=this.L & reputation.getFlag()==2){
							//passa in lista grigia
							reputation.setFlag(1);


						}
					}}
				for(int j=0;j<absenceSU.size();j++){
					Reputation reputation=this.usersToInfo.get(absenceSU.get(j));
					//incremento gli errori
					if(reputation.getFlag()!=5){
						reputation.incrementErrorCount();



						//se gli ERRORI superano M e si trova in lista bianca
						if(reputation.getErrorCount()>=this.M & reputation.getFlag()==0){
							//passa in lista grigia
							reputation.setFlag(1);


						}
						//se gli errori superano L e si trova in lista grigia
						else if(reputation.getErrorCount()>=this.N & reputation.getFlag()==1){
							reputation.setFlag(2);;


						}

					}
				}}
			else if(globalDecision==0){
				for(int i=0;i<presenceSU.size();i++){
					Reputation reputation=this.usersToInfo.get(presenceSU.get(i));
					//incremento gli errori
					if(reputation.getFlag()!=5){
						reputation.incrementErrorCount();



						//se gli ERRORI superano M e si trova in lista bianca
						if(reputation.getErrorCount()>=this.M & reputation.getFlag()==0){
							reputation.setFlag(1);

						}
						//se gli errori superano L e si trova in lista grigia
						else if(reputation.getErrorCount()>=this.N & reputation.getFlag()==1){
							reputation.setFlag(2);

						}
					}}

				for(int j=0;j<absenceSU.size();j++){

					Reputation reputation=this.usersToInfo.get(absenceSU.get(j));
					//Incremento gli hits consecutivi
					if(reputation.getFlag()!=5){
						reputation.incrementConsecutiveHits();

						//se gli hits superano K e si trova in lista grigia
						if(reputation.getConsecutiveHits()>=this.K & reputation.getFlag()==1){
							reputation.setFlag(0);;

						}
						//se gli hits superano L e si trova in lista nera
						else if(reputation.getConsecutiveHits()>=this.L & reputation.getFlag()==2){
							reputation.setFlag(1);


						}
					}}
			}
		}





		/** Questo metodo prende in input, oltre agli estremi snr,  una Mappa che ha come chiave il nome dell'utente secondario e Come valore  una lista di liste: per ogni SNR ha una lista
		 * di lunghezza pari al numero di prove contenente la decisione binaria sulla presenza o assenza dell'utente primario.
		 * Ritorna la % di Dectection calcolata utilizzando un meccanismo di reputazione per gli utenti secondari.
		 * @param inf Estremo inferiore SNR
		 * @param sup Estremo superiore SNR 
		 * @param userToBinaryDecision Mappa che ha come chiave il nome dell'uente primario. Come valore ha una lista di liste: per ogni SNR ha una lista
		 * di lunghezza pari al numero di prove contenente la decisione binaria sulla presenza o assenza dell'utente primario da parte dell'utente secondario
		 * @param attempts Numero di prove
		 * @return  Ritorna la % di Dectection calcolata utilizzando un meccanismo di reputazione per gli utenti secondari.
		 * @throws IOException 
		 */

		public  ArrayList<Double> reputationBasedDecision(String path,int inf,int sup,HashMap<String,ArrayList<ArrayList<Integer>>> userToBinaryDecision,int attempts,	String typeMSU) throws IOException{
			HashMap<Double,Double> reputationBasedDetection=new HashMap<Double,Double>();
			//Inizializzo le mappe SnrToPresenceUser e SnrToAbsenceUser. Queste mappe, per ogni snr, hanno una lista di utenti che affermano
			//la presenza o l'assenza dell'utente primario di cardinalità pari al numero di prove
			createSnrToUsers(inf,sup,userToBinaryDecision,attempts);
			//Per ogni SNR
			FileWriter w=new FileWriter(path+"Conventional_user_exluded"+"_"+typeMSU+".txt");
			BufferedWriter b=new BufferedWriter(w);
			for(Double snr: this.snrToPresenceUsers.keySet()){
				this.excludedUsers.clear();
				this.excludedUsersStamp.clear();
				//Inizializzo la reputazione a 5 ad ogni cambio di SNR
				inizializeReliabilities(userToBinaryDecision);
				ArrayList<Integer> globalDecisions= new ArrayList<Integer>();
				for(int attempt=0;attempt<this.snrToPresenceUsers.get(snr).size();attempt++){
					//Per ogni prova mi creo una mappa UTENTE->DECISIONE dei soli utenti che partecipano alla comunicazione, e quindi
					//con reputazione >=1
					HashMap<String,Integer> binaryDecisions=computeUserToDecision(this.snrToPresenceUsers.get(snr).get(attempt),
							this.snrToAbsenceUsers.get(snr).get(attempt));
					//Mi calcolo la decisione globale
					Integer globalDecision=computeGlobalDecision(binaryDecisions);
					//Aggiungo alla decisione globale
					globalDecisions.add(globalDecision);
					//aggiorno la reputazione
					updateReliabilities(globalDecision,this.snrToPresenceUsers.get(snr).get(attempt),
							this.snrToAbsenceUsers.get(snr).get(attempt),snr,attempt);




				}
				b.write("------------------- SNR="+snr+" -------------------------"+" \n");
				b.write("User Excluded: "+this.excludedUsers.size()+" \n");	
				for(String key: this.excludedUsersStamp.keySet()){
					b.write("User: " + key+ " Prova: " + this.excludedUsersStamp.get(key)+" \n");
				}

				//Per ogni snr, aggiungo alla mappa l'array di decisioni globali di cardinalità pari al numero di prove
				double detection=Detector.reputationBasedDetection(globalDecisions);
				reputationBasedDetection.put(snr,detection);
			}

			b.close();
			return SignalProcessor.orderSignal(reputationBasedDetection);


		}



		/**Questo metodo Inizializza le mappe SnrToPresenceUser e SnrToAbsenceUser. Queste mappe, per ogni snr, hanno una lista di utenti che affermano
		la presenza o l'assenza dell'utente primario di cardinalità pari al numero di prove
		 * @param inf Estremo SNR inferiore
		 * @param sup Estremo SNR superiore
		 * @param userToBinaryDecision Mappa che ha come chiave il nome dell'utente secondario. Come valore ha una lista di liste: per ogni SNR ha una lista
		 * di lunghezza pari al numero di prove contenente la decisione binaria sulla presenza o assenza dell'utente primario da parte dell'utente secondario
		 * @param attempts Numero di prove
		 */

		public void createSnrToUsers(int inf,int sup,HashMap<String,ArrayList<ArrayList<Integer>>> userToBinaryDecision,int attempts){
			//Per ogni SNR
			for(int i=0;i<(sup-inf);i++){
				ArrayList<ArrayList<String>> listOfPresenceUser =new ArrayList<ArrayList<String>>();	
				ArrayList<ArrayList<String>> listOfAbsenceUser=new ArrayList<ArrayList<String>>();	
				//Per ogni prova
				for(int j=0;j<attempts;j++){
					ArrayList<String> presenceUser =new ArrayList<String>();	
					ArrayList<String> nonPresenceUser=new ArrayList<String>();	
					//Per ogni utente
					for(String SU: userToBinaryDecision.keySet()){
						if(userToBinaryDecision.get(SU).get(i).get(j)==0){
							nonPresenceUser.add(SU);}
						else{
							presenceUser.add(SU);}
					}
					listOfPresenceUser.add(presenceUser);
					listOfAbsenceUser.add(nonPresenceUser);
				}

				this.snrToPresenceUsers.put((double)inf+i,listOfPresenceUser );
				this.snrToAbsenceUsers.put((double)inf+i, listOfAbsenceUser);


			}
		}


		/** Inizializza la reputazione di tutti gli utenti presenti a 5
		 * @param userToBinaryDecision Mappa che ha come chiave il nome dell'utente secondario. Come valore ha una lista di liste: per ogni SNR ha una lista
		 * di lunghezza pari al numero di prove contenente la decisione binaria sulla presenza o assenza dell'utente primario da parte dell'utente secondario
		 */

		public void inizializeReliabilities(HashMap<String,ArrayList<ArrayList<Integer>>> userToBinaryDecision){
			this.usersReliabilities.clear();
			for(String SU: userToBinaryDecision.keySet()){
				this.usersReliabilities.put(SU, 5.0);
			}

		}


		/**Questo metodo ritorna una mappa contenente come chiave il nome dell'utente e come valore la decisione relativa alla
		 * presenza o assenza dell'utente primario. Considera solamente gli utenti che partecipano alla conversazione, ovvero
		 * utenti con reputazione maggiore o uguale ad 1
		 * @param presenceUsers Lista di utenti che affermano la presenza dell'utente primario
		 * @param absenceUsers Lista di utenti che affermano l'assenza dell'utente primario
		 * @return Una mappa utente decisione
		 **/

		public HashMap<String,Integer> computeUserToDecision(ArrayList<String> presenceUsers,ArrayList<String> absenceUsers){
			HashMap<String,Integer> binaryDecisions= new HashMap<String,Integer>();
			for(int SU=0;SU<presenceUsers.size();SU++){
				double reliabilities = usersReliabilities.get(presenceUsers.get(SU));
				if(reliabilities>=1){
					binaryDecisions.put(presenceUsers.get(SU), 1);
				}}
			for(int SU2=0;SU2<absenceUsers.size();SU2++){
				double reliabilities = usersReliabilities.get(absenceUsers.get(SU2));
				if(reliabilities>=1){
					binaryDecisions.put(absenceUsers.get(SU2),0);
				}
			}

			return binaryDecisions;
		}



		/** Questo metodo calcola la decisione globale prendendo in input una mappa utente decisione . Il calcolo della decisione
		 * globale si basa su un peso associato ad ogni utente che tiene conto della sua reputazione, dell'utente con la massima reputazione
		 * e delle reputazione di tutti gli altri utenti.
		 * @param binaryDecisions Mappa utente Decisione
		 * @return La decisione globale basa sul peso di ogni utente
		 */

		public int computeGlobalDecision(HashMap<String,Integer> binaryDecisions){
			int globalDecision;
			double cont=0;
			//Calcolo la massima reputazione
			double maxReputation= getMaxReliability(binaryDecisions.keySet());
			//sommatoria w'(j), la somma dei pesi parziali di tutti gli utenti che partecipano alla comunicazione
			double totalPartialWeight= getPartialTotalWeigth(binaryDecisions.keySet(),maxReputation);

			//System.out.println(this.threshold);
			//Per ogni utente	
			for(String SU: binaryDecisions.keySet()){
				//Calcolo il peso*decisione locale e lo sommo al precedente
				//System.out.println(SU+" Reputation: "+usersReliabilities.get(SU)+" Decision: "+binaryDecisions.get(SU));
				cont=cont+( binaryDecisions.get(SU)*computeWeight(SU,totalPartialWeight,maxReputation));
			}
			// System.out.println("--------------------");
			if(cont>=0.5){globalDecision=1;}
			else{globalDecision=0;}
			return globalDecision;
		}


		/**Metodo per il calcolo della soglia nel metodo basato su reputazione
		 * @param binaryDecisions Mappa contenente per ogni utente, la decisione relativa alla presenza o assenza dell'utente primario
		 * ad un dato SNR
		 * @param maxReputation La massima reputazione 
		 * @param totalPartialWeight La somma dei pesi parziali
		 * @return Ritorma la soglia
		 */

		public double computeThreshold(HashMap<String,Integer> binaryDecisions,double maxReputation,double totalPartialWeight){
			double threshold=0;
			double minWeight=1;
			for(String SU: binaryDecisions.keySet()){
				double weight=computeWeight(SU,totalPartialWeight,maxReputation);
				threshold=threshold+weight;
				if(weight<minWeight){minWeight=weight;}

			}
			return (threshold/2)-minWeight;
		}



		/** Questo metodo calcola il peso di ogni utente utilizzando come parametri la sommatoria dei pesi parziali,
		 * la massima reputazione tra gli utenti e la reputazione dell'utente considerato
		 * @param SU L'utente secondario
		 * @param totalPartialWeight Sommatoria dei pesi parziali
		 * @param maxReputation La massima reputazione tra gli utenti
		 * @return Il peso dell'utente passato come parametro
		 */

		public double computeWeight(String SU,double totalPartialWeight,double maxReputation){
			double weight= (usersReliabilities.get(SU)/maxReputation)/totalPartialWeight;		
			return weight;

		}


		/** Questo metodo calcola la sommatoria dei pesi parziali. Il peso parziale si calcola facendo la somamtoria
		 * dei rapporti tra la reputazione dell'utente e la massima reputazione tra tutti gli utenti
		 * @param listSU Lista degli utenti secondari che partecipano alla comunicazione
		 * @param maxReputation Massima Reputazione 
		 * @return La sommatoria dei pesi parziali degli utenti che partecipano alla counicazione
		 */

		public double getPartialTotalWeigth(Set<String> listSU,double maxReputation){
			double  totalPartialWeigth=0.0;
			for(String su: listSU){
				totalPartialWeigth= totalPartialWeigth+(usersReliabilities.get(su)/maxReputation); 
			}
			return totalPartialWeigth;
		}


		/** Questo metodo ritorna la massima reputazione tra gli utenti che partecipano alla comunicazione
		 * @param listSU Lista di utenti che partecipano alla comunicazione
		 * @return La massima reputazione
		 */

		public double getMaxReliability(Set<String> listSU){
			double maxReliabilities=0.0;
			for(String su: listSU){
				if(usersReliabilities.get(su)>maxReliabilities){
					maxReliabilities=usersReliabilities.get(su);
				}
			}
			return maxReliabilities;
		}


		/** Questo metodo aggiorna la reputazione di ogni utente, sia di quelli che partecipano alla comunicazione sia di
		 * quelli che non partecipano. Per fare ciò, si basa sulla decisione locale e la confronta con quella globale.
		 * Se coincidono, la reputazione viene incrementata di 1, altrimenti decrementata.
		 * @param globalDecision Decisione globale
		 * @param presenceSU Lista di utenti che affermano la presenza dell'utente primario
		 * @param absenceSU Lista di utenti che affermano l'assenza dell'utente primario
		 * @param snr L'SNR a cui si sta effettuando l'aggiornamento della reputazione
		 */

		public void updateReliabilities(int globalDecision,ArrayList<String>  presenceSU,ArrayList<String>  absenceSU,double snr,int attempt){
			for(int i=0;i<presenceSU.size();i++){
				//Questo controllo fa si che una volta che un utente va sotto la soglia minima (1) non viene più considerato
				if(this.usersReliabilities.get(presenceSU.get(i))>=1){
					double newReliabilities=this.usersReliabilities.get(presenceSU.get(i))+ Math.pow(-1,(1+globalDecision));
					this.usersReliabilities.replace(presenceSU.get(i),newReliabilities);
				}
				else{
					if(!this.excludedUsers.contains(presenceSU.get(i))){
						excludedUsers.add(presenceSU.get(i));
						if(!excludedUsersStamp.containsKey(presenceSU.get(i)))
							this.excludedUsersStamp.put(presenceSU.get(i), attempt);}
				}

				//System.out.println
				//("Malicious user: "+ presenceSU.get(i)+" Snr: "+ snr + " Attempt: " + attempt);
			}

			for(int i=0;i<absenceSU.size();i++){
				//Questo controllo fa si che una volta che un utente va sotto la soglia minima (1) non viene più considerato
				if(this.usersReliabilities.get(absenceSU.get(i))>=1){
					double newReliabilities=this.usersReliabilities.get(absenceSU.get(i))+ Math.pow(-1,(0+globalDecision));
					this.usersReliabilities.replace(absenceSU.get(i),newReliabilities);}

				else{
					if(!this.excludedUsers.contains(absenceSU.get(i))){
						excludedUsers.add(absenceSU.get(i));
						if(!excludedUsersStamp.containsKey(absenceSU.get(i)))
							this.excludedUsersStamp.put(absenceSU.get(i), attempt);}
				}

			}
		}


		/** Questo metodo Ritorna la % di Dectection calcolata utilizzando un meccanismo di reputazione per gli utenti secondari con utilizzo di Trusted Node
		 * @param inf Estremo inferiore SNR
		 * @param sup Estremo superiore SNR 
		 * @param userToBinaryDecision Mappa che ha come chiave il nome dell'uente secondario. Come valore ha una lista di liste: per ogni SNR ha una lista
		 * di lunghezza pari al numero di prove contenente la decisione binaria sulla presenza o assenza dell'utente primario da parte dell'utente secondario
		 * @param trustedNodeToBinaryDecision Mappa che ha come chiave il nome del trusted Node. Come valore ha una lista di liste: per ogni SNR ha una lista
		 * di lunghezza pari al numero di prove contenente la decisione binaria sulla presenza o assenza dell'utente primario da parte dell'utente secondario
		 * @param attempts Numero di prove
		 * @return  Ritorna la % di Dectection calcolata utilizzando un meccanismo di reputazione per gli utenti secondari.
		 * @throws IOException 
		 */
		public  ArrayList<Double> reputationBasedWithTrustedNodeDecision(String path,int inf,int sup,HashMap<String,ArrayList<ArrayList<Integer>>> userToBinaryDecision,HashMap<String,ArrayList<ArrayList<Integer>>> trustedNodeToBinaryDecision,int attempts,	String typeMSU) throws IOException{
			HashMap<Double,Double> reputationBasedDetection=new HashMap<Double,Double>();
			//Inizializzo le mappe SnrToPresenceUser e SnrToAbsenceUser. Queste mappe, per ogni snr, hanno una lista di utenti che affermano
			//la presenza o l'assenza dell'utente primario di cardinalità pari al numero di prove
			createSnrToUsers(inf,sup,userToBinaryDecision,trustedNodeToBinaryDecision,attempts);
			//Per ogni SNR
			FileWriter w=new FileWriter(path+"ConventionalTN_user_exluded"+"_"+typeMSU+".txt");
			BufferedWriter b=new BufferedWriter(w);
			for(Double snr: this.snrToPresenceUsers.keySet()){
				this.discardedStateStamp.clear();

				//Inizializzo la reputazione a 5 ad ogni cambio di SNR
				inizializeReliabilitiesAndState(userToBinaryDecision,trustedNodeToBinaryDecision);
				ArrayList<Integer> globalDecisions= new ArrayList<Integer>();
				for(int attempt=0;attempt<this.snrToPresenceUsers.get(snr).size();attempt++){
					//Per ogni prova mi creo una mappa UTENTE->DECISIONE dei soli utenti che partecipano alla comunicazione, e quindi
					//con reputazione >=1

					HashMap<String,Integer> binaryDecisions=computeReliableUserToDecision(this.snrToPresenceUsers.get(snr).get(attempt),
							this.snrToAbsenceUsers.get(snr).get(attempt));
					//Mi calcolo la decisione globale
					Integer globalDecision=computeGlobalDecision(binaryDecisions);
					//Aggiungo alla decisione globale
					globalDecisions.add(globalDecision);
					//aggiorno la reputazione
					updateReliabilitiesAndState(globalDecision,this.snrToPresenceUsers.get(snr).get(attempt),
							this.snrToAbsenceUsers.get(snr).get(attempt),snr,attempt);
				}
				b.write("------------------- SNR="+snr+" -------------------------"+" \n");
				b.write("User Excluded: "+this.discardedState.size()+" \n");	
				for(String key: this.discardedStateStamp.keySet()){
					b.write("User :" + key+" prova: "+ this.discardedStateStamp.get(key)+" \n");	
				}
				b.write("\n");
				b.write("Reliable User: "+this.reliableState.size()+" \n");	
				for(int i=0;i<this.reliableState.size();i++){
					b.write(this.reliableState.get(i)+" \n");}
				b.write("\n");
				b.write("Pending User: "+this.pendingState.size()+" \n");	
				for(int i=0;i<this.pendingState.size();i++){
					b.write(this.pendingState.get(i)+" \n");}

				//Per ogni snr, aggiungo alla mappa l'array di decisioni globali di cardinalità pari al numero di prove
				double detection=Detector.reputationBasedDetection(globalDecisions);
				reputationBasedDetection.put(snr,detection);
			}

			b.close();
			return SignalProcessor.orderSignal(reputationBasedDetection);


		}

		/**Questo metodo Inizializza le mappe SnrToPresenceUser e SnrToAbsenceUser. Queste mappe, per ogni snr, hanno una lista di utenti che affermano
		la presenza o l'assenza dell'utente primario di cardinalità pari al numero di prove. In questo modo sono compresi anche i trusted Node.
		 * @param inf Estremo SNR inferiore
		 * @param sup Estremo SNR superiore
		 * @param trustedNodeToBinaryDecision Mappa che ha come chiave il nome del trusted node. Come valore ha una lista di liste: per ogni SNR ha una lista
		 * di lunghezza pari al numero di prove contenente la decisione binaria sulla presenza o assenza dell'utente primario da parte dell'utente secondario
		 * @param userToBinaryDecision Mappa che ha come chiave il nome dell'utente secondario. Come valore ha una lista di liste: per ogni SNR ha una lista
		 * di lunghezza pari al numero di prove contenente la decisione binaria sulla presenza o assenza dell'utente primario da parte dell'utente secondario
		 * @param attempts Numero di prove
		 */

		public void createSnrToUsers(int inf,int sup,HashMap<String,ArrayList<ArrayList<Integer>>> userToBinaryDecision,HashMap<String,ArrayList<ArrayList<Integer>>> trustedNodeToBinaryDecision,int attempts){
			//Per ogni SNR
			for(int i=0;i<(sup-inf);i++){
				ArrayList<ArrayList<String>> listOfPresenceUser =new ArrayList<ArrayList<String>>();	
				ArrayList<ArrayList<String>> listOfAbsenceUser=new ArrayList<ArrayList<String>>();	
				//Per ogni prova
				for(int j=0;j<attempts;j++){
					ArrayList<String> presenceUser =new ArrayList<String>();	
					ArrayList<String> nonPresenceUser=new ArrayList<String>();	
					//Per ogni utente
					for(String SU: userToBinaryDecision.keySet()){
						if(userToBinaryDecision.get(SU).get(i).get(j)==0){
							nonPresenceUser.add(SU);}
						else{
							presenceUser.add(SU);}
					}

					for(String TN: trustedNodeToBinaryDecision.keySet()){
						if(trustedNodeToBinaryDecision.get(TN).get(i).get(j)==0){
							nonPresenceUser.add(TN);}
						else{
							presenceUser.add(TN);}
					}
					listOfPresenceUser.add(presenceUser);
					listOfAbsenceUser.add(nonPresenceUser);
				}

				this.snrToPresenceUsers.put((double)inf+i,listOfPresenceUser );
				this.snrToAbsenceUsers.put((double)inf+i, listOfAbsenceUser);


			}
		}

		/** Inizializza la reputazione di tutti gli utenti presenti a 5, quella dei trusted node a 13. Inoltre divide gli utenti in 2 liste
		 *  * @param trustedNodeToBinaryDecision Mappa che ha come chiave il nome del trusted node. Come valore ha una lista di liste: per ogni SNR ha una lista
		 * di lunghezza pari al numero di prove contenente la decisione binaria sulla presenza o assenza dell'utente primario da parte dell'utente secondario
		 * @param userToBinaryDecision Mappa che ha come chiave il nome dell'utente secondario. Come valore ha una lista di liste: per ogni SNR ha una lista
		 * di lunghezza pari al numero di prove contenente la decisione binaria sulla presenza o assenza dell'utente primario da parte dell'utente secondario
		 */

		public void inizializeReliabilitiesAndState(HashMap<String,ArrayList<ArrayList<Integer>>> userToBinaryDecision,HashMap<String,ArrayList<ArrayList<Integer>>> trustedNodeToBinaryDecision){
			this.usersReliabilities.clear();
			this.pendingState.clear();
			this.discardedState.clear();
			this.reliableState.clear();
			for(String SU: userToBinaryDecision.keySet()){
				this.usersReliabilities.put(SU, 5.0);
				this.pendingState.add(SU);
			}
			for(String TN: trustedNodeToBinaryDecision.keySet()){
				this.usersReliabilities.put(TN, 13.0);
				this.reliableState.add(TN);
			}

		}

		/**Questo metodo ritorna una mappa contenente come chiave il nome dell'utente e come valore la decisione relativa alla
		 * presenza o assenza dell'utente primario. Considera solamente gli utenti che partecipano alla conversazione, ovvero
		 * utenti contenuti della lista reliableState
		 * @param presenceUsers Lista di utenti che affermano la presenza dell'utente primario
		 * @param absenceUsers Lista di utenti che affermano l'assenza dell'utente primario
		 * @return Una mappa utente decisione
		 **/

		public HashMap<String,Integer> computeReliableUserToDecision(ArrayList<String> presenceUsers,ArrayList<String> absenceUsers){
			HashMap<String,Integer> binaryDecisions= new HashMap<String,Integer>();
			for(String SU:presenceUsers){

				if(this.reliableState.contains(SU)){
					binaryDecisions.put(SU, 1);
				}}
			for(String SU2:absenceUsers){

				if(this.reliableState.contains(SU2)){
					binaryDecisions.put(SU2,0);
				}
			}

			return binaryDecisions;
		}

		/** Questo metodo aggiorna la reputazione di ogni utente, esclusi quelli esclusi dalla comunicazione contenuti nella lista
		 * discardedState. Per fare ciò, si basa sulla decisione locale e la confronta con quella globale.
		 * Se coincidono, la reputazione viene incrementata di 1, altrimenti decrementata.
		 * @param globalDecision Decisione globale
		 * @param presenceSU Lista di utenti che affermano la presenza dell'utente primario
		 * @param absenceSU Lista di utenti che affermano l'assenza dell'utente primario
		 * @param snr L'SNR a cui si sta effettuando l'aggiornamento della reputazione
		 */

		public void updateReliabilitiesAndState(int globalDecision,ArrayList<String>  presenceSU,ArrayList<String>  absenceSU,double snr,int attempt){
			for(String SU: presenceSU){
				//Questo controllo fa si che una volta che un utente va sotto la soglia minima (1) non viene più considerato
				if(!this.discardedState.contains(SU)){
					double newReliabilities=this.usersReliabilities.get(SU)+ Math.pow(-1,(1+globalDecision));
					this.usersReliabilities.replace(SU,newReliabilities);
				}


			}

			for(String SU: absenceSU){
				//Questo controllo fa si che una volta che un utente va sotto la soglia minima (1) non viene più considerato
				if(!this.discardedState.contains(SU)){
					double newReliabilities=this.usersReliabilities.get(SU)+ Math.pow(-1,(0+globalDecision));
					this.usersReliabilities.replace(SU,newReliabilities);}



			}

			ArrayList<String> reliableStateTemp= new ArrayList<String>();
			reliableStateTemp.addAll(this.reliableState);
			for(String SU: reliableStateTemp){
				if(this.usersReliabilities.get(SU)<Nb){
					this.reliableState.remove(SU);
					this.pendingState.add(SU);
				}
				if(this.usersReliabilities.get(SU)<Na){
					this.reliableState.remove(SU);
					this.discardedState.add(SU);
					if(!this.discardedStateStamp.containsKey(SU))
						this.discardedStateStamp.put(SU, attempt);
				}

				ArrayList<String> pendingStateTemp= new ArrayList<String>();
				pendingStateTemp.addAll(this.pendingState);
				for(String SU2: pendingStateTemp){
					if(this.usersReliabilities.get(SU2)<Na){
						this.pendingState.remove(SU2);
						this.discardedState.add(SU2);
						if(!this.discardedStateStamp.containsKey(SU))
							this.discardedStateStamp.put(SU2, attempt);
					}	
					else if(this.usersReliabilities.get(SU2)>=Nb){
						this.pendingState.remove(SU2);
						this.reliableState.add(SU2);
					}


				}

			}



		}





	}


