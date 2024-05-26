

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeSet;


public class GLA{

	static Map<String,String> regEx=new HashMap<String,String>();
	static ArrayList<String> stanja = new ArrayList<>();
	static ArrayList<Akcija> akcije = new ArrayList<>();
	static String ulaz="";


	public static void main(String[] args) throws Exception {	
		generiraj();
		String leksickiAnalizator="";
		leksickiAnalizator+=""
				+ "import java.io.File;\r\n"
				+ "import java.io.FileNotFoundException;\r\n"
				+ "import java.util.ArrayList;\r\n"
				+ "import java.util.Arrays;\r\n"
				+ "import java.util.HashMap;\r\n"
				+ "import java.util.Map;\r\n"
				+ "import java.util.PriorityQueue;\r\n"
				+ "import java.util.Queue;\r\n"
				+ "import java.util.Scanner;\r\n"
				+ "import java.util.Set;\r\n"
				+ "import java.util.TreeSet;\r\n"
				+ "\r\n"
				+ "\r\n"
				+ "class Akcija {\r\n"
				+ "	public String stanje = null;\r\n"
				+ "	public String regex = null;\r\n"
				+ "	public String leksickaJedinka = null;\r\n"
				+ "	public String novoStanje = null;\r\n"
				+ "	public boolean noviRed = false;\r\n"
				+ "	public int vratiSe=-1;\r\n"
				+ "	public Automat a = new Automat();\r\n"
				+ "	public Akcija() {\r\n"
				+ "		\r\n"
				+ "	}\r\n"
				+ "	public Akcija(String stanje, String regex, String leksickaJedinka, String novoStanje, boolean noviRed,\r\n"
				+ "			int vratiSe) {\r\n"
				+ "		super();\r\n"
				+ "		this.stanje = stanje;\r\n"
				+ "		this.regex = regex;\r\n"
				+ "		this.leksickaJedinka = leksickaJedinka;\r\n"
				+ "		this.novoStanje = novoStanje;\r\n"
				+ "		this.noviRed = noviRed;\r\n"
				+ "		this.vratiSe = vratiSe;\r\n"
				+ "		Pair stanja = Automat.pretvori(regex, a);\r\n"
				+ "		a.pocetnoStanje=stanja.lijevoStanje;\r\n"
				+ "		a.pocetnoStanje=stanja.lijevoStanje;\r\n"
				+ "		a.trenutnaStanja=a.epsilonOkolina(a.pocetnoStanje);\r\n"
				+ "		a.skupPrihvatljivihStanja.add(stanja.desnoStanje);\r\n"
				+ "\r\n"
				+ "	}\r\n"
				+ "	public void initAutomat() {\r\n"
				+ "		Pair stanja = Automat.pretvori(regex, a);\r\n"
				+ "		a.pocetnoStanje=stanja.lijevoStanje;\r\n"
				+ "		a.pocetnoStanje=stanja.lijevoStanje;\r\n"
				+ "		a.trenutnaStanja=a.epsilonOkolina(a.pocetnoStanje);\r\n"
				+ "		a.skupPrihvatljivihStanja.add(stanja.desnoStanje);\r\n"
				+ "		\r\n"
				+ "	}\r\n"
				+ "	public String getStanje() {\r\n"
				+ "		return stanje;\r\n"
				+ "	}\r\n"
				+ "	public void setStanje(String stanje) {\r\n"
				+ "		this.stanje = stanje;\r\n"
				+ "	}\r\n"
				+ "	public String getRegex() {\r\n"
				+ "		return regex;\r\n"
				+ "	}\r\n"
				+ "	public void setRegex(String regex) {\r\n"
				+ "		this.regex = regex;\r\n"
				+ "	}\r\n"
				+ "	public String getLeksickaJedinka() {\r\n"
				+ "		return leksickaJedinka;\r\n"
				+ "	}\r\n"
				+ "	public void setLeksickaJedinka(String leksickaJedinka) {\r\n"
				+ "		this.leksickaJedinka = leksickaJedinka;\r\n"
				+ "	}\r\n"
				+ "	public String getNovoStanje() {\r\n"
				+ "		return novoStanje;\r\n"
				+ "	}\r\n"
				+ "	public void setNovoStanje(String novoStanje) {\r\n"
				+ "		this.novoStanje = novoStanje;\r\n"
				+ "	}\r\n"
				+ "	public boolean isNoviRed() {\r\n"
				+ "		return noviRed;\r\n"
				+ "	}\r\n"
				+ "	public void setNoviRed(boolean noviRed) {\r\n"
				+ "		this.noviRed = noviRed;\r\n"
				+ "	}\r\n"
				+ "	public int getVratiSe() {\r\n"
				+ "		return vratiSe;\r\n"
				+ "	}\r\n"
				+ "	public void setVratiSe(int vratiSe) {\r\n"
				+ "		this.vratiSe = vratiSe;\r\n"
				+ "	}\r\n"
				+ "\r\n"
				+ "	@Override\r\n"
				+ "	public String toString() {\r\n"
				+ "		return \"Akcija [stanje=\" + stanje + \", regex=\" + regex + \", leksickaJedinka=\" + leksickaJedinka\r\n"
				+ "				+ \", novoStanje=\" + novoStanje + \", noviRed=\" + noviRed + \", vratiSe=\" + vratiSe + \"]\";\r\n"
				+ "	}\r\n"
				+ "	\r\n"
				+ "}\r\n"
				+ "\r\n"
				+ "\r\n"
				+ "class Automat {\r\n"
				+ "	String pocetnoStanje;\r\n"
				+ "	Map<String,Set<String>> mapaPrijelaza;\r\n"
				+ "	TreeSet<String> skupPrihvatljivihStanja;\r\n"
				+ "	TreeSet<String> trenutnaStanja;\r\n"
				+ "	int brojStanja=0;\r\n"
				+ "	\r\n"
				+ "	public Automat() {\r\n"
				+ "		super();\r\n"
				+ "		mapaPrijelaza = new HashMap<String,Set<String>>();\r\n"
				+ "		skupPrihvatljivihStanja = new TreeSet<String>();\r\n"
				+ "		trenutnaStanja= new TreeSet<String>();\r\n"
				+ "		pocetnoStanje=null;\r\n"
				+ "	}\r\n"
				+ "	\r\n"
				+ "	int ispitaj() {\r\n"
				+ "		Set<String> prihv = new TreeSet<>(this.skupPrihvatljivihStanja);\r\n"
				+ "		prihv.retainAll(this.trenutnaStanja);\r\n"
				+ "		if(prihv.size()>0) {\r\n"
				+ "			return 1;\r\n"
				+ "		}else if(trenutnaStanja.size()==0) {\r\n"
				+ "			return -1;\r\n"
				+ "		}else {\r\n"
				+ "			return 0;\r\n"
				+ "		}\r\n"
				+ "	}\r\n"
				+ "	public void reset() {\r\n"
				+ "		trenutnaStanja=epsilonOkolina(pocetnoStanje);\r\n"
				+ "	}\r\n"
				+ "	private static int novoStanje(Automat automat) {\r\n"
				+ "		automat.brojStanja++;\r\n"
				+ "		return automat.brojStanja-1;\r\n"
				+ "	}\r\n"
				+ "	\r\n"
				+ "	private static boolean jeOperator(String izraz,int i) {\r\n"
				+ "		int br=0;\r\n"
				+ "		while(i-1>=0 && izraz.charAt(i-1)=='\\\\') {\r\n"
				+ "			br++;\r\n"
				+ "			i--;\r\n"
				+ "		}\r\n"
				+ "		return br%2==0;\r\n"
				+ "	}\r\n"
				+ "	\r\n"
				+ "	static Pair pretvori(String izraz, Automat automat) {\r\n"
				+ "		ArrayList<String> izbori = new ArrayList<>();\r\n"
				+ "		int zadnjiNeGrupirani=0;\r\n"
				+ "		int brZagrada = 0;\r\n"
				+ "		for(int i=0;i<izraz.length();i++) {\r\n"
				+ "			if(izraz.charAt(i)=='(' && jeOperator(izraz, i)) {\r\n"
				+ "				brZagrada++;\r\n"
				+ "			}else if(izraz.charAt(i)==')' && jeOperator(izraz, i)) {\r\n"
				+ "				brZagrada--;\r\n"
				+ "			}else if(brZagrada==0 && izraz.charAt(i)=='|' && jeOperator(izraz, i)) {\r\n"
				+ "				izbori.add(izraz.substring(zadnjiNeGrupirani,i));\r\n"
				+ "				zadnjiNeGrupirani=i+1;\r\n"
				+ "			}\r\n"
				+ "		}\r\n"
				+ "		\r\n"
				+ "		if(izbori.size()>0) {\r\n"
				+ "			izbori.add(izraz.substring(zadnjiNeGrupirani));\r\n"
				+ "		}\r\n"
				+ "		int lijevoStanje = novoStanje(automat);\r\n"
				+ "		int desnoStanje = novoStanje(automat);\r\n"
				+ "		if(izbori.size()>0) {\r\n"
				+ "			for(String izbor:izbori) {\r\n"
				+ "				Pair privremeno = pretvori(izbor, automat);\r\n"
				+ "				dodajEpsilonPrijelaz(automat,lijevoStanje+\"\",privremeno.lijevoStanje);\r\n"
				+ "				dodajEpsilonPrijelaz(automat,privremeno.desnoStanje+\"\",desnoStanje+\"\");\r\n"
				+ "			}\r\n"
				+ "		}else {\r\n"
				+ "			boolean prefiksirano = false;\r\n"
				+ "			int zadnjeStanje = lijevoStanje;\r\n"
				+ "			for(int i=0;i<izraz.length();i++) {\r\n"
				+ "				int a = 0,b = 0;\r\n"
				+ "				if(prefiksirano) {\r\n"
				+ "					//slucaj 1\r\n"
				+ "					prefiksirano=false;\r\n"
				+ "					char prijelazniZnak;\r\n"
				+ "					if(izraz.charAt(i)=='t') {\r\n"
				+ "						prijelazniZnak='\\t'; //jedan znak, kao u C-u\r\n"
				+ "					}else if(izraz.charAt(i)=='n') {\r\n"
				+ "						prijelazniZnak='\\n'; //jedan znak, kao u C-u\r\n"
				+ "					}else if(izraz.charAt(i)=='_') {\r\n"
				+ "						prijelazniZnak=' ';//obican razmak\r\n"
				+ "					}else {\r\n"
				+ "						prijelazniZnak=izraz.charAt(i);\r\n"
				+ "					}\r\n"
				+ "					a=novoStanje(automat);\r\n"
				+ "					b=novoStanje(automat);\r\n"
				+ "					dodajPrijelaz(automat, a+\"\", b+\"\", prijelazniZnak);\r\n"
				+ "				}else {\r\n"
				+ "					//slucaj 2\r\n"
				+ "					if(izraz.charAt(i)=='\\\\') { //jedan znak \\, kao u C-u\r\n"
				+ "						prefiksirano=true;\r\n"
				+ "						continue;\r\n"
				+ "					}\r\n"
				+ "					if(izraz.charAt(i)!='(') {\r\n"
				+ "						//slucaj 2a\r\n"
				+ "						a=novoStanje(automat);\r\n"
				+ "						b=novoStanje(automat);\r\n"
				+ "						if(izraz.charAt(i)=='$') {\r\n"
				+ "							dodajEpsilonPrijelaz(automat, a+\"\", b+\"\");\r\n"
				+ "						}else {\r\n"
				+ "							dodajPrijelaz(automat, a+\"\", b+\"\", izraz.charAt(i));\r\n"
				+ "						}\r\n"
				+ "					}else {\r\n"
				+ "						//slucaj 2b\r\n"
				+ "						int j= odgovarajucaZagrada(izraz,i);\r\n"
				+ "						Pair privremeno = pretvori(izraz.substring(i+1,j), automat);\r\n"
				+ "						a = Integer.parseInt(privremeno.lijevoStanje);\r\n"
				+ "						b = Integer.parseInt(privremeno.desnoStanje);\r\n"
				+ "						i=j;\r\n"
				+ "					}\r\n"
				+ "				}\r\n"
				+ "				\r\n"
				+ "				//provjera ponavljanja\r\n"
				+ "				if(i+1<izraz.length() && izraz.charAt(i+1)=='*') {\r\n"
				+ "					int x=a;\r\n"
				+ "					int y=b;\r\n"
				+ "					a=novoStanje(automat);\r\n"
				+ "					b=novoStanje(automat);\r\n"
				+ "					dodajEpsilonPrijelaz(automat, a+\"\", x+\"\");\r\n"
				+ "					dodajEpsilonPrijelaz(automat, y+\"\", b+\"\");\r\n"
				+ "					dodajEpsilonPrijelaz(automat, a+\"\", b+\"\");\r\n"
				+ "					dodajEpsilonPrijelaz(automat, y+\"\", x+\"\");\r\n"
				+ "					i++;\r\n"
				+ "				}\r\n"
				+ "				\r\n"
				+ "				//povezivanje s prethodnim podizrazom\r\n"
				+ "				dodajEpsilonPrijelaz(automat, zadnjeStanje+\"\", a+\"\");\r\n"
				+ "				zadnjeStanje=b;\r\n"
				+ "			}\r\n"
				+ "			dodajEpsilonPrijelaz(automat, zadnjeStanje+\"\", desnoStanje+\"\");\r\n"
				+ "		}\r\n"
				+ "		Pair par = new Pair();\r\n"
				+ "		par.lijevoStanje=lijevoStanje+\"\";\r\n"
				+ "		par.desnoStanje=desnoStanje+\"\";\r\n"
				+ "		return par;	\r\n"
				+ "	}\r\n"
				+ "\r\n"
				+ "	private static int odgovarajucaZagrada(String izraz, int i) {\r\n"
				+ "		int br=0;\r\n"
				+ "		for(int k=i+1;k<izraz.length();k++) {\r\n"
				+ "			if(izraz.charAt(k)==')' && br==0 && izraz.charAt(k-1)!='\\\\') {\r\n"
				+ "				return k;\r\n"
				+ "			}\r\n"
				+ "			if(izraz.charAt(k)=='(' && izraz.charAt(k-1)!='\\\\')\r\n"
				+ "				br++;\r\n"
				+ "			if(izraz.charAt(k)==')' && izraz.charAt(k-1)!='\\\\')\r\n"
				+ "				br--;\r\n"
				+ "		}\r\n"
				+ "		return -1;\r\n"
				+ "	}\r\n"
				+ "\r\n"
				+ "	private static void dodajPrijelaz(Automat automat, String lijevoStanje, String desnoStanje, char prijelazniZnak) {\r\n"
				+ "		Set<String> stanja = new TreeSet<>();\r\n"
				+ "		if(!(automat.mapaPrijelaza.get(lijevoStanje+\";\"+prijelazniZnak)==null)) {\r\n"
				+ "			stanja = automat.mapaPrijelaza.get(lijevoStanje+\";\"+prijelazniZnak);\r\n"
				+ "		}\r\n"
				+ "		stanja.add(desnoStanje);\r\n"
				+ "		automat.mapaPrijelaza.put(lijevoStanje+\";\"+prijelazniZnak, stanja);\r\n"
				+ "	}\r\n"
				+ "\r\n"
				+ "	private static void dodajEpsilonPrijelaz(Automat automat, String lijevoStanje, String desnoStanje) {\r\n"
				+ "		Set<String> stanja = new TreeSet<>();\r\n"
				+ "		if(!(automat.mapaPrijelaza.get(lijevoStanje+\";\")==null)) {\r\n"
				+ "			stanja = automat.mapaPrijelaza.get(lijevoStanje+\";\");\r\n"
				+ "		}\r\n"
				+ "		stanja.add(desnoStanje);\r\n"
				+ "		automat.mapaPrijelaza.put(lijevoStanje+\";\", stanja);\r\n"
				+ "	}\r\n"
				+ "	\r\n"
				+ "	public TreeSet<String> prijelaz(String simbol){\r\n"
				+ "		trenutnaStanja = epsilonOkolina(trenutnaStanja);\r\n"
				+ "		/*\r\n"
				+ "		System.out.println(\"Prijelaz:\");\r\n"
				+ "		System.out.println(\"\\t\"+trenutnaStanja+\", \"+simbol);\r\n"
				+ "		*/\r\n"
				+ "		TreeSet<String> skupNovihStanja = new TreeSet<String>();\r\n"
				+ "		for(String stanje:trenutnaStanja)\r\n"
				+ "		{\r\n"
				+ "			if(mapaPrijelaza.get(stanje+\";\"+simbol)==null)\r\n"
				+ "				continue;\r\n"
				+ "			\r\n"
				+ "			for(String novoStanje:mapaPrijelaza.get(stanje+\";\"+simbol)) {\r\n"
				+ "				skupNovihStanja.add(novoStanje);\r\n"
				+ "			}\r\n"
				+ "		}\r\n"
				+ "		trenutnaStanja = skupNovihStanja;\r\n"
				+ "		trenutnaStanja = epsilonOkolina(trenutnaStanja);\r\n"
				+ "		return trenutnaStanja;\r\n"
				+ "	}\r\n"
				+ "	\r\n"
				+ "	public TreeSet<String> epsilonOkolina(TreeSet<String> stanja) {\r\n"
				+ "		TreeSet<String> epsilonOkolina = new TreeSet<String>(stanja);\r\n"
				+ "		\r\n"
				+ "			for(String stanje : stanja) {\r\n"
				+ "				TreeSet<String> epsilonOkolinaStanja = epsilonOkolina(stanje);\r\n"
				+ "				epsilonOkolina.addAll(epsilonOkolinaStanja);\r\n"
				+ "			}\r\n"
				+ "		return epsilonOkolina;\r\n"
				+ "	}\r\n"
				+ "\r\n"
				+ "	public TreeSet<String> epsilonOkolina(String stanje) {\r\n"
				+ "		TreeSet<String> epsilonOkolina = new TreeSet<String>();\r\n"
				+ "		epsilonOkolina.add(stanje);\r\n"
				+ "		Queue<String> q = new PriorityQueue<String>();\r\n"
				+ "		q.add(stanje);\r\n"
				+ "		while(!q.isEmpty()) {\r\n"
				+ "			String trenutnoStanje = q.remove();\r\n"
				+ "			Set<String> susjednaStanja = mapaPrijelaza.get(trenutnoStanje+\";\");\r\n"
				+ "			if(susjednaStanja==null)\r\n"
				+ "				continue;\r\n"
				+ "			for(String susjednoStanje:susjednaStanja) {\r\n"
				+ "				if(!epsilonOkolina.contains(susjednoStanje)) {					\r\n"
				+ "					epsilonOkolina.add(susjednoStanje);\r\n"
				+ "					q.add(susjednoStanje);\r\n"
				+ "				}\r\n"
				+ "			}\r\n"
				+ "		}\r\n"
				+ "		return epsilonOkolina;\r\n"
				+ "	}\r\n"
				+ "\r\n"
				+ "	@Override\r\n"
				+ "	public String toString() {\r\n"
				+ "		return \"Automat [brojStanja=\" + brojStanja + \", pocetnoStanje=\" + pocetnoStanje + \", mapaPrijelaza=\"\r\n"
				+ "				+ mapaPrijelaza + \", skupPrihvatljivihStanja=\" + skupPrihvatljivihStanja + \"]\";\r\n"
				+ "	}\r\n"
				+ "	\r\n"
				+ "	\r\n"
				+ "}\r\n"
				+ "\r\n"
				+ "\r\n"
				+ "class Pair {\r\n"
				+ "	public String lijevoStanje;\r\n"
				+ "	public String desnoStanje;\r\n"
				+ "	public Pair() {\r\n"
				+ "		super();\r\n"
				+ "	}\r\n"
				+ "}\r\n"
				+ "\r\n"
				+ "\r\n"
				+ "public class LA {\r\n"
				+ "	static ArrayList<String> stanja = new ArrayList<>();\r\n"
				+ "	static ArrayList<Akcija> akcije = new ArrayList<>();\r\n"
				+ "	static String trenutnoStanjeAnalizatora;\r\n"
				+ "	\r\n"
				+ "	static int pocetak=0,zavrsetak=0,posljednji=0;\r\n"
				+ "	static Akcija zadnjaAkcija=null;\r\n"
				+ "	static int brojacReda=1;\r\n"
				+ "	static Set<String> R = new TreeSet<>();\r\n"
				+ "	static String ulaz=\"\";\r\n"
				+ "\r\n"
				+ "	public static void main(String[] args) throws Exception {\r\n"
				+ "		//File f = new File(\"C:\\\\Users\\\\meglic\\\\pokazni_zi\\\\generatorAnalizatora\\\\src\\\\generatorAnalizatora\\\\program.txt\");\r\n"
				+ "		Scanner sc = new Scanner(System.in);\r\n"
				+ "		while(sc.hasNextLine()) {\r\n"
				+ "			ulaz+=sc.nextLine()+\"\\n\";\r\n"
				+ "		}\r\n"
				+ "";
		
		for(String stanje:stanja) {
			leksickiAnalizator+="\tstanja.add(\""+stanje+"\");\n";
		}
		
		for(Akcija akcija:akcije) {
			String novoStanje;
			String leksickaJedinka;
			if(akcija.novoStanje==null) {
				novoStanje="null";
			}else {
				novoStanje="\""+akcija.novoStanje+"\"";
			}
			if(akcija.leksickaJedinka==null) {
				leksickaJedinka=null;
			}else {
				leksickaJedinka="\""+akcija.leksickaJedinka+"\"";
			}
			
			akcija.regex=akcija.regex.replace("\\", "\\\\");
			akcija.regex=akcija.regex.replace("\"", "\\\"");
			leksickiAnalizator+="\takcije.add(new Akcija(\""+akcija.stanje+"\",\""+akcija.regex+"\", "+leksickaJedinka+","+novoStanje+", "+akcija.isNoviRed()+", "+akcija.vratiSe+"));\n";
		}
		
		leksickiAnalizator+="\r\n"
				+ "		trenutnoStanjeAnalizatora=stanja.get(0);\r\n"
				+ "		\r\n"
				+ "		while(pocetak<ulaz.length()) {\r\n"
				+ "			String znak = ulaz.charAt(zavrsetak)+\"\";\r\n"
				+ "			for(Akcija akcija:akcije) {\r\n"
				+ "				if(akcija.stanje.equals(trenutnoStanjeAnalizatora)) {\r\n"
				+ "					akcija.a.prijelaz(znak);\r\n"
				+ "				}\r\n"
				+ "			}\r\n"
				+ "			boolean prazno=true;\r\n"
				+ "			for(Akcija akcija:akcije) {\r\n"
				+ "				if(!akcija.stanje.equals(trenutnoStanjeAnalizatora))\r\n"
				+ "					continue;\r\n"
				+ "				if(akcija.a.ispitaj()==1) {\r\n"
				+ "					zadnjaAkcija=akcija;\r\n"
				+ "					posljednji=zavrsetak;\r\n"
				+ "					prazno=false;\r\n"
				+ "					break;\r\n"
				+ "				}else if(akcija.a.ispitaj()==0) {\r\n"
				+ "					prazno=false;\r\n"
				+ "				}\r\n"
				+ "			}\r\n"
				+ "			if(!prazno) {\r\n"
				+ "				zavrsetak++;\r\n"
				+ "			}else {\r\n"
				+ "				if(zadnjaAkcija==null) {\r\n"
				+ "					System.err.println(ulaz.charAt(pocetak));\r\n"
				+ "					pocetak++;\r\n"
				+ "					zavrsetak=pocetak;\r\n"
				+ "					for(Akcija akcija:akcije) {\r\n"
				+ "						akcija.a.reset();\r\n"
				+ "					}\r\n"
				+ "				}else {\r\n"
				+ "					izvrsiAkciju(zadnjaAkcija);\r\n"
				+ "					for(Akcija akcija:akcije) {\r\n"
				+ "						akcija.a.reset();\r\n"
				+ "					}\r\n"
				+ "				}\r\n"
				+ "			}\r\n"
				+ "			\r\n"
				+ "			if(zavrsetak==ulaz.length() && pocetak!=ulaz.length()) {\r\n"
				+ "				if(zadnjaAkcija==null) {\r\n"
				+ "					System.err.println(ulaz.charAt(pocetak));\r\n"
				+ "					pocetak++;\r\n"
				+ "					zavrsetak=pocetak;\r\n"
				+ "					for(Akcija akcija:akcije) {\r\n"
				+ "						akcija.a.reset();\r\n"
				+ "					}\r\n"
				+ "				}else {\r\n"
				+ "					izvrsiAkciju(zadnjaAkcija);\r\n"
				+ "					for(Akcija akcija:akcije) {\r\n"
				+ "						akcija.a.reset();\r\n"
				+ "					}\r\n"
				+ "				}\r\n"
				+ "			}\r\n"
				+ "		}\r\n"
				+ "	}\r\n"
				+ "	private static void izvrsiAkciju(Akcija akcija) {\r\n"
				+ "		String niz=\"\";\r\n"
				+ "		if(akcija.vratiSe!=-1) {\r\n"
				+ "			niz=ulaz.substring(pocetak,pocetak+akcija.vratiSe);\r\n"
				+ "			pocetak=pocetak+akcija.vratiSe;\r\n"
				+ "			posljednji=pocetak;\r\n"
				+ "			zavrsetak=pocetak;\r\n"
				+ "		}else {\r\n"
				+ "			niz=ulaz.substring(pocetak,posljednji+1);\r\n"
				+ "			pocetak=zavrsetak;\r\n"
				+ "			posljednji=pocetak;\r\n"
				+ "		}\r\n"
				+ "		\r\n"
				+ "		if(akcija.leksickaJedinka!=null) {\r\n"
				+ "			System.out.println(akcija.leksickaJedinka+\" \"+brojacReda+\" \"+niz);\r\n"
				+ "		}\r\n"
				+ "		if(akcija.noviRed) {\r\n"
				+ "			brojacReda++;\r\n"
				+ "		}\r\n"
				+ "		if(akcija.novoStanje!=null) {\r\n"
				+ "			trenutnoStanjeAnalizatora=akcija.novoStanje;\r\n"
				+ "		}\r\n"
				+ "		zadnjaAkcija=null;\r\n"
				+ "	}\r\n"
				+ "\r\n"
				+ "}";
		/*
		FileWriter fileWriter = new FileWriter(".\\analizator\\LA.java");
		BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
		PrintWriter printWriter = new PrintWriter(bufferedWriter);
		printWriter.print(leksickiAnalizator);
		
		printWriter.close();
		bufferedWriter.close();
		fileWriter.close();
		
		*/
		File outPut = new File("./analizator/LA.java");
		outPut.createNewFile();
		FileWriter fileWriter = new FileWriter(outPut);
		fileWriter.write(leksickiAnalizator);
		
		fileWriter.close();
		System.out.println("completed!");
	}

	
	public static void generiraj() {
		try {
			//File myFile = new File("C:\\Users\\meglic\\pokazni_zi\\generatorAnalizatora\\src\\generatorAnalizatora\\ulaz.txt");
			Scanner myScn = new Scanner(System.in);
		
			while(myScn.hasNextLine()) {
				String data=myScn.nextLine();
			if(data.charAt(0)=='{') {
				parseExpression(data);
			}
			
			else if(data.contains("%X")) {
				stanjaParse(data);
			}
			else if(data.contains("%L")) {
				continue;
			}
			else if(data.charAt(0)=='<') {
				int cnt=0;
				Akcija akcija=new Akcija();
				for(String key:regEx.keySet()) {
					if(data.contains(key)) {
						data=data.replace(key, '('+regEx.get(key)+')');
					}
				}
				String line[]=data.substring(1).split(">",2);
				//System.out.println(data);
				akcija.setStanje(line[0]);
				
				line[1]=line[1].replace("\\_", " ");
				
				akcija.setRegex(line[1]);
				data=myScn.nextLine();
				while(true) {
					data=myScn.nextLine();
					if(data.contains("}")) {
						//System.out.println(akcija);
						akcija.initAutomat();
						//System.out.println(akcija.a);
						akcije.add(akcija);
						
						break;
					}
					if(cnt==0) {
						
						if(data.equals("-")) {
							
							data=null;
						}
						
						akcija.setLeksickaJedinka(data);
						cnt++;
					}else {
						if(data.contains("NOVI_REDAK")) {
							akcija.setNoviRed(true);
						}
						if(data.contains("UDJI_U_STANJE")) {
							data=data.split(" ")[1];
							akcija.setNovoStanje(data);
						}
						if(data.contains("VRATI_SE")) {
							data=data.split(" ")[1];
							akcija.setVratiSe(Integer.parseInt(data));
						}
					}
				}
			}
			 
			}
			myScn.close();		
			}catch(Exception e) {
				System.out.println("Nema ulaza");
			}
		}
	
	private static void parseExpression(String data) {
		String line[] = data.split(" ");
		String regName=line[0];
		String reg=line[1];
		
		for(String key:regEx.keySet()) {
			if(reg.contains(key)) {
				
				reg=reg.replace(key, '('+regEx.get(key)+')');
			}
		}
		
		regEx.put(regName, reg);
		
	}
	
	private static void stanjaParse(String data) {
		data=data.replace("%X", "").strip();
		stanja = new ArrayList<String>(Arrays.asList(data.split(" ")));
	}
}

class Akcija {
	public String stanje = null;
	public String regex = null;
	public String leksickaJedinka = null;
	public String novoStanje = null;
	public boolean noviRed = false;
	public int vratiSe=-1;
	public Automat a = new Automat();
	public Akcija() {
		
	}
	public Akcija(String stanje, String regex, String leksickaJedinka, String novoStanje, boolean noviRed,
			int vratiSe) {
		super();
		this.stanje = stanje;
		this.regex = regex;
		this.leksickaJedinka = leksickaJedinka;
		this.novoStanje = novoStanje;
		this.noviRed = noviRed;
		this.vratiSe = vratiSe;
		Pair stanja = Automat.pretvori(regex, a);
		a.pocetnoStanje=stanja.lijevoStanje;
		a.pocetnoStanje=stanja.lijevoStanje;
		a.trenutnaStanja=a.epsilonOkolina(a.pocetnoStanje);
		a.skupPrihvatljivihStanja.add(stanja.desnoStanje);

	}
	public void initAutomat() {
		Pair stanja = Automat.pretvori(regex, a);
		a.pocetnoStanje=stanja.lijevoStanje;
		a.pocetnoStanje=stanja.lijevoStanje;
		a.trenutnaStanja=a.epsilonOkolina(a.pocetnoStanje);
		a.skupPrihvatljivihStanja.add(stanja.desnoStanje);
		
	}
	public String getStanje() {
		return stanje;
	}
	public void setStanje(String stanje) {
		this.stanje = stanje;
	}
	public String getRegex() {
		return regex;
	}
	public void setRegex(String regex) {
		this.regex = regex;
	}
	public String getLeksickaJedinka() {
		return leksickaJedinka;
	}
	public void setLeksickaJedinka(String leksickaJedinka) {
		this.leksickaJedinka = leksickaJedinka;
	}
	public String getNovoStanje() {
		return novoStanje;
	}
	public void setNovoStanje(String novoStanje) {
		this.novoStanje = novoStanje;
	}
	public boolean isNoviRed() {
		return noviRed;
	}
	public void setNoviRed(boolean noviRed) {
		this.noviRed = noviRed;
	}
	public int getVratiSe() {
		return vratiSe;
	}
	public void setVratiSe(int vratiSe) {
		this.vratiSe = vratiSe;
	}

	@Override
	public String toString() {
		return "Akcija [stanje=" + stanje + ", regex=" + regex + ", leksickaJedinka=" + leksickaJedinka
				+ ", novoStanje=" + novoStanje + ", noviRed=" + noviRed + ", vratiSe=" + vratiSe + "]";
	}
	
}


class Automat {
	String pocetnoStanje;
	Map<String,Set<String>> mapaPrijelaza;
	TreeSet<String> skupPrihvatljivihStanja;
	TreeSet<String> trenutnaStanja;
	int brojStanja=0;
	
	public Automat() {
		super();
		mapaPrijelaza = new HashMap<String,Set<String>>();
		skupPrihvatljivihStanja = new TreeSet<String>();
		trenutnaStanja= new TreeSet<String>();
		pocetnoStanje=null;
	}
	
	int ispitaj() {
		Set<String> prihv = new TreeSet<>(this.skupPrihvatljivihStanja);
		prihv.retainAll(this.trenutnaStanja);
		if(prihv.size()>0) {
			return 1;
		}else if(trenutnaStanja.size()==0) {
			return -1;
		}else {
			return 0;
		}
	}
	public void reset() {
		trenutnaStanja=epsilonOkolina(pocetnoStanje);
	}
	private static int novoStanje(Automat automat) {
		automat.brojStanja++;
		return automat.brojStanja-1;
	}
	
	private static boolean jeOperator(String izraz,int i) {
		int br=0;
		while(i-1>=0 && izraz.charAt(i-1)=='\\') {
			br++;
			i--;
		}
		return br%2==0;
	}
	
	static Pair pretvori(String izraz, Automat automat) {
		ArrayList<String> izbori = new ArrayList<>();
		int zadnjiNeGrupirani=0;
		int brZagrada = 0;
		for(int i=0;i<izraz.length();i++) {
			if(izraz.charAt(i)=='(' && jeOperator(izraz, i)) {
				brZagrada++;
			}else if(izraz.charAt(i)==')' && jeOperator(izraz, i)) {
				brZagrada--;
			}else if(brZagrada==0 && izraz.charAt(i)=='|' && jeOperator(izraz, i)) {
				izbori.add(izraz.substring(zadnjiNeGrupirani,i));
				zadnjiNeGrupirani=i+1;
			}
		}
		
		if(izbori.size()>0) {
			izbori.add(izraz.substring(zadnjiNeGrupirani));
		}
		int lijevoStanje = novoStanje(automat);
		int desnoStanje = novoStanje(automat);
		if(izbori.size()>0) {
			for(String izbor:izbori) {
				Pair privremeno = pretvori(izbor, automat);
				dodajEpsilonPrijelaz(automat,lijevoStanje+"",privremeno.lijevoStanje);
				dodajEpsilonPrijelaz(automat,privremeno.desnoStanje+"",desnoStanje+"");
			}
		}else {
			boolean prefiksirano = false;
			int zadnjeStanje = lijevoStanje;
			for(int i=0;i<izraz.length();i++) {
				int a = 0,b = 0;
				if(prefiksirano) {
					//slucaj 1
					prefiksirano=false;
					char prijelazniZnak;
					if(izraz.charAt(i)=='t') {
						prijelazniZnak='\t'; //jedan znak, kao u C-u
					}else if(izraz.charAt(i)=='n') {
						prijelazniZnak='\n'; //jedan znak, kao u C-u
					}else if(izraz.charAt(i)=='_') {
						prijelazniZnak=' ';//obican razmak
					}else {
						prijelazniZnak=izraz.charAt(i);
					}
					a=novoStanje(automat);
					b=novoStanje(automat);
					dodajPrijelaz(automat, a+"", b+"", prijelazniZnak);
				}else {
					//slucaj 2
					if(izraz.charAt(i)=='\\') { //jedan znak \, kao u C-u
						prefiksirano=true;
						continue;
					}
					if(izraz.charAt(i)!='(') {
						//slucaj 2a
						a=novoStanje(automat);
						b=novoStanje(automat);
						if(izraz.charAt(i)=='$') {
							dodajEpsilonPrijelaz(automat, a+"", b+"");
						}else {
							dodajPrijelaz(automat, a+"", b+"", izraz.charAt(i));
						}
					}else {
						//slucaj 2b
						int j= odgovarajucaZagrada(izraz,i);
						Pair privremeno = pretvori(izraz.substring(i+1,j), automat);
						a = Integer.parseInt(privremeno.lijevoStanje);
						b = Integer.parseInt(privremeno.desnoStanje);
						i=j;
					}
				}
				
				//provjera ponavljanja
				if(i+1<izraz.length() && izraz.charAt(i+1)=='*') {
					int x=a;
					int y=b;
					a=novoStanje(automat);
					b=novoStanje(automat);
					dodajEpsilonPrijelaz(automat, a+"", x+"");
					dodajEpsilonPrijelaz(automat, y+"", b+"");
					dodajEpsilonPrijelaz(automat, a+"", b+"");
					dodajEpsilonPrijelaz(automat, y+"", x+"");
					i++;
				}
				
				//povezivanje s prethodnim podizrazom
				dodajEpsilonPrijelaz(automat, zadnjeStanje+"", a+"");
				zadnjeStanje=b;
			}
			dodajEpsilonPrijelaz(automat, zadnjeStanje+"", desnoStanje+"");
		}
		Pair par = new Pair();
		par.lijevoStanje=lijevoStanje+"";
		par.desnoStanje=desnoStanje+"";
		return par;	
	}

	private static int odgovarajucaZagrada(String izraz, int i) {
		int br=0;
		for(int k=i+1;k<izraz.length();k++) {
			if(izraz.charAt(k)==')' && br==0 && izraz.charAt(k-1)!='\\') {
				return k;
			}
			if(izraz.charAt(k)=='(' && izraz.charAt(k-1)!='\\')
				br++;
			if(izraz.charAt(k)==')' && izraz.charAt(k-1)!='\\')
				br--;
		}
		return -1;
	}

	private static void dodajPrijelaz(Automat automat, String lijevoStanje, String desnoStanje, char prijelazniZnak) {
		Set<String> stanja = new TreeSet<>();
		if(!(automat.mapaPrijelaza.get(lijevoStanje+";"+prijelazniZnak)==null)) {
			stanja = automat.mapaPrijelaza.get(lijevoStanje+";"+prijelazniZnak);
		}
		stanja.add(desnoStanje);
		automat.mapaPrijelaza.put(lijevoStanje+";"+prijelazniZnak, stanja);
	}

	private static void dodajEpsilonPrijelaz(Automat automat, String lijevoStanje, String desnoStanje) {
		Set<String> stanja = new TreeSet<>();
		if(!(automat.mapaPrijelaza.get(lijevoStanje+";")==null)) {
			stanja = automat.mapaPrijelaza.get(lijevoStanje+";");
		}
		stanja.add(desnoStanje);
		automat.mapaPrijelaza.put(lijevoStanje+";", stanja);
	}
	
	public TreeSet<String> prijelaz(String simbol){
		trenutnaStanja = epsilonOkolina(trenutnaStanja);
		/*
		System.out.println("Prijelaz:");
		System.out.println("\t"+trenutnaStanja+", "+simbol);
		*/
		TreeSet<String> skupNovihStanja = new TreeSet<String>();
		for(String stanje:trenutnaStanja)
		{
			if(mapaPrijelaza.get(stanje+";"+simbol)==null)
				continue;
			
			for(String novoStanje:mapaPrijelaza.get(stanje+";"+simbol)) {
				skupNovihStanja.add(novoStanje);
			}
		}
		trenutnaStanja = skupNovihStanja;
		trenutnaStanja = epsilonOkolina(trenutnaStanja);
		return trenutnaStanja;
	}
	
	public TreeSet<String> epsilonOkolina(TreeSet<String> stanja) {
		TreeSet<String> epsilonOkolina = new TreeSet<String>(stanja);
		
			for(String stanje : stanja) {
				TreeSet<String> epsilonOkolinaStanja = epsilonOkolina(stanje);
				epsilonOkolina.addAll(epsilonOkolinaStanja);
			}
		return epsilonOkolina;
	}

	public TreeSet<String> epsilonOkolina(String stanje) {
		TreeSet<String> epsilonOkolina = new TreeSet<String>();
		epsilonOkolina.add(stanje);
		Queue<String> q = new PriorityQueue<String>();
		q.add(stanje);
		while(!q.isEmpty()) {
			String trenutnoStanje = q.remove();
			Set<String> susjednaStanja = mapaPrijelaza.get(trenutnoStanje+";");
			if(susjednaStanja==null)
				continue;
			for(String susjednoStanje:susjednaStanja) {
				if(!epsilonOkolina.contains(susjednoStanje)) {					
					epsilonOkolina.add(susjednoStanje);
					q.add(susjednoStanje);
				}
			}
		}
		return epsilonOkolina;
	}

	@Override
	public String toString() {
		return "Automat [brojStanja=" + brojStanja + ", pocetnoStanje=" + pocetnoStanje + ", mapaPrijelaza="
				+ mapaPrijelaza + ", skupPrihvatljivihStanja=" + skupPrihvatljivihStanja + "]";
	}
	
	
}


class Pair {
	public String lijevoStanje;
	public String desnoStanje;
	public Pair() {
		super();
	}
}


