package agents;

public class Reputation {
	
	int consecutiveHits;
	int errorCount;
	int flag;


	/** Costruttore del metodo reputazione. Inizializza consecutiveHits, ErrorCount e flag a zero
	 * 
	 */

	public Reputation(){
		this.consecutiveHits=0;
		this.errorCount=0;
		this.flag=0;
	}


	/** Metodo che ritorna i consecutive Hits dell'oggetto Reputation. I consecutive HITS rappresentano
	 * le volte consecutive che un utente concorda con la decisione globale del fusion center riguardo 
	 * la presenza o assenza dell'utente primario
	 * @return I consecutive HITS dell'oggetto reputation
	 */

	public int getConsecutiveHits() {
		return consecutiveHits;
	}


	/** Imposta i consecutive HITS al parametro specificato.I consecutive HITS rappresentano
	 * le volte consecutive che un utente concorda con la decisione globale del fusion center riguardo 
	 * la presenza o assenza dell'utente primario
	 * @param consecutiveHits Parametro a cui impostare i consecutive HITS
	 */

	public void setConsecutiveHits(int consecutiveHits) {
		this.consecutiveHits = consecutiveHits;
	}

	/** Ritorna l'error count dell'oggetto reputazione. L'error count rappresenta le volte che un utente
	 * non concorda con la decisione globale del fusion center circa la presenza o assenza dell'utente primario.
	 * @return Error Count dell'oggetto reputazionee
	 */

	public int getErrorCount() {
		return errorCount;
	}


	/** Imposta l'error count.L'error count rappresenta le volte che un utente
	 * non concorda con la decisione globale del fusion center circa la presenza o assenza dell'utente primario.
	 * @param errorCount Parametro a cui settare l'error count
	 */

	public void setErrorCount(int errorCount) {
		this.errorCount = errorCount;
	}


	/** Ritorna il flag dell'oggetto reputazione. Il flag rappresenta la lista di appartenenza di un determinato utente
	 * @return il flag dell'oggetto reputazione
	 */

	public int getFlag() {
		return flag;
	}


	/** Setta il flag dell'oggetto reputation al parametro specificato. Il flag rappresenta la lista di appartenenza di un determinato utente.
	 * Ad ogni cambio di flag, e quindi di lista, vengono azzerati sia gli error count che i consecutive hits
	 * @param flag Parametro a cui settare il flag
	 */

	public void setFlag(int flag) {
		this.consecutiveHits=0;
		this.errorCount=0;
		this.flag = flag;
	}


	/** Incrementa di uno il parametro consecutive HITS dell'oggetto reputation
	 * 
	 */

	public void incrementConsecutiveHits(){
		this.consecutiveHits++;
		if(this.errorCount>0){
			this.errorCount--;
		}
	}

	/** Incrementa di uno il parametro error count dell'oggetto reputation
	 * 
	 */

	public void incrementErrorCount(){
		this.consecutiveHits=0;
		this.errorCount++;

	}





}
	


