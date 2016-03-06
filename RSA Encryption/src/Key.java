
public class Key {
	
	private HugeInt exp;
	private HugeInt n;
	private boolean pub;
	private static final String primeFile = "primeList.rsc";
	
	public Key(String keyName){
		
	}
	
	public HugeInt getN(){
		return n;
	}
	
	public HugeInt getExp(){
		return exp;
	}
	
	public boolean isPublic(){
		return pub;
	}
	
	public static void GenKeys(String pubKey, String priKey){
		
	}

	public static void GenKeys(HugeInt p, String pubKey, String priKey){

	}
	
	public static void GenKeys(HugeInt p, HugeInt q, String pubKey, String priKey){
		
	}
	
	private HugeInt chooseRand(){
		
	}
	
	private void writeKey(String keyName, Key k){
		
	}
	
}
