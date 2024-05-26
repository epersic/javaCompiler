import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Scanner;
import java.util.Set;
import java.util.Stack;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.stream.Collectors;

public class GeneratorSintaksnog {
	static Set<String> zavrsniZnakovi = new TreeSet<>();
	static Set<String> nezavrsniZnakovi = new TreeSet<>();
	static Set<String> sviZnakovi = new TreeSet<>();
	static ArrayList<Produkcija> produkcije = new ArrayList<>();
	static Set<String> sinkronizacijskiZnakovi = new TreeSet<>();
	static ArrayList<Stavka> stavke = new ArrayList<>();
	
	static Set<String> prazniZnakovi = new TreeSet<>();
	static Map<String, Set<String>> zapocinje = new HashMap<>();
	
	static Map<String, Set<String>> odlukaStanja = new HashMap<>();
	static Map<String, Integer> redukcijaStanja = new HashMap<>();
	
	static Map<String,String[]> akcija = new HashMap<>();
	
	static ArrayList<Token>tokens= new ArrayList<>();
	static Stack<Token> mainStack=new Stack<>();
	static Token root;
	
	
	public static void main(String[] args) throws FileNotFoundException {
		parseInput();
		generirajStavke();
		nadjiPrazneZnakove();
		izracunajZapocinje();

		System.out.println("Prazni znakovi:");
		System.out.println(prazniZnakovi);
		System.out.println("--------------------------------\n");
		System.out.println("Zapocinje:");
		System.out.println(zapocinje);
		System.out.println("---------------------------------\n");
		
		
		Automat epsilonNKA = generirajEpsilonNka();
		
		Automat DKA = generirajDKA(epsilonNKA);
		
		generirajTablicuAkcija(DKA);
		
		readFile();
		
		automat();
		
		ispisiStablo(root,0);
	}
	
	static void ispisiStablo(Token root,int dubina) {
		for(int i=0;i<dubina; i++) {
			System.out.print(" ");
		}
		if(root.getNumOfRow()==0) 
			System.out.println(root.getToken());
		else
			System.out.println(root.getToken()+" "+root.getNumOfRow()+" "+root.getCommand());
		
		for(int i = root.getDjeca().size()-1; i>=0; i--) {
			ispisiStablo(root.getDjeca().get(i), dubina+1);
		}
		return;
	}

	public static void readFile() throws FileNotFoundException {
		Scanner sc=new Scanner(new File("G:\\My Drive\\Mape\\Faks\\5. semestar\\ppjLab2\\src\\langdefs\\simplePpjLang_manji.in"));
		while(sc.hasNextLine()) {
			String inSplit[]=sc.nextLine().split(" ",3);
			Token lexOut= new Token(inSplit[0],Integer.parseInt(inSplit[1]),inSplit[2]);
			tokens.add(lexOut);
			//System.out.println(inSplit[0]);
		}
		Token zavrsni=new Token("&", 0 , "&");
		tokens.add(zavrsni);
	}
	
	public static boolean automat() {
		mainStack.push(new Token(0));
		Token lastToken = null;
		int cnt=0;
		while(akcija.get(tokens.get(cnt).getToken())[mainStack.peek().getNum()]!="Prihvati") {
			String tokenName=tokens.get(cnt).getToken();
			
			if(akcija.get(tokenName)[mainStack.peek().getNum()]==null) {
				return false;
			}
			String[] splitAkcija= akcija.get(tokenName)[mainStack.peek().getNum()].split(";");
			
			if(splitAkcija[0].equals("P")) {
				mainStack.push(tokens.get(cnt));
				mainStack.push(new Token(Integer.parseInt(splitAkcija[1])));
				cnt++;
			}else if(splitAkcija[0].equals("S")) {
				mainStack.push(new Token(Integer.parseInt(splitAkcija[1])));
				
			}else if(splitAkcija[0].equals("R")) { 
				Token noviToken= new Token(produkcije.get(Integer.parseInt(splitAkcija[1])).getLijevaStrana(),0,"");
				//Token[] djecaMalena= new Token[produkcije.get(Integer.parseInt(splitAkcija[1])).getDesnaStrana().size()*2];
				ArrayList<Token> djecaMalena = new ArrayList<>();
				if(!produkcije.get(Integer.parseInt(splitAkcija[1])).getDesnaStrana().get(0).equals("$")) {
					for(int i=0;i<produkcije.get(Integer.parseInt(splitAkcija[1])).getDesnaStrana().size()*2;i++) {
						if(!mainStack.peek().isNumber()) {
						djecaMalena.add(mainStack.peek());
						mainStack.pop().setRoditelj(noviToken);
						}else {
							mainStack.pop();
						}						
					}
				}else {
					djecaMalena.add(new Token("$", 0, "$"));
				}
				
				if(akcija.get(noviToken.getToken())[mainStack.peek().getNum()].equals("Prihvati")) {
					break;
				}
				String newState=akcija.get(noviToken.getToken())[mainStack.peek().getNum()].split(";")[1];
				
				noviToken.setDjeca(djecaMalena);
				lastToken=noviToken;
				mainStack.push(noviToken);

				mainStack.push(new Token(Integer.parseInt(newState)));
			}
		}
		root=lastToken;
		return true;
	}
	
	private static void generirajTablicuAkcija(Automat DKA) {
		//generiranje tablice akcija
		for(String znak:sviZnakovi) {
			String[] akcije = new String[redukcijaStanja.size()];
			for(String stanje: redukcijaStanja.keySet()) {
				if(DKA.prijelaz.get(stanje+";"+znak)==null)
					continue;
				
				
				String novoStanje  = DKA.prijelaz.get(stanje+";"+znak).toString();
				novoStanje = novoStanje.substring(1, novoStanje.length()-1);
				
				if(zavrsniZnakovi.contains(znak))
					akcije[Integer.parseInt(stanje)]="P;"+novoStanje;
				else 
					akcije[Integer.parseInt(stanje)]="S;"+novoStanje;
			}
			if(znak=="0")
				akcije[0]="Prihvati";
			akcija.put(znak, akcije);
		}
		
		for(int i=0;i<redukcijaStanja.size();i++) {
			String stanje = i+"";
			if(redukcijaStanja.get(stanje)==-1)
				continue;
			int maxPoz = redukcijaStanja.get(stanje);
			Set<String> odluka = odlukaStanja.get(stanje);
			for(String znak : odluka) {
				if(akcija.get(znak)==null) {
					String[] a = new String[redukcijaStanja.size()];
					a[i] = "R;"+maxPoz;
					akcija.put(znak, a);
				}else if(akcija.get(znak)[i]==null) {
					String[] a = akcija.get(znak);
					a[i]="R;"+maxPoz;
					akcija.put(znak, a);
				}
			}
		}
	}
	
	private static Automat generirajDKA(Automat epsilonNKA) {
		
		Automat DKA = new Automat();

		//generiramo NKA iz epsilonNKA
		for(int stanje=0; stanje<epsilonNKA.stanjaAutomata.size(); stanje++) {
			for(String znak:sviZnakovi) {
				TreeSet<String> stanja = new TreeSet<>();
				stanja.add(stanje+"");
				
				Set<String> novaStanja = epsilonNKA.prijelaz(stanja, znak);
				if(!novaStanja.isEmpty()) {
					DKA.prijelaz.put(stanje+";"+znak, novaStanja);					
				}
			}
		}
		
		
		
		Queue<String> q = new LinkedList<>();
		q.addAll(DKA.prijelaz.keySet());
		while(!q.isEmpty()) {
			String trenutniPrijelaz = q.remove();
			Set<String> novaStanja = DKA.prijelaz.get(trenutniPrijelaz);
			
			if(novaStanja.size()==1)
				continue;
			
			for(String znak:sviZnakovi) {
				Set<String> prijelazi = new TreeSet<>();
				for(String stanje:novaStanja) {
					if(DKA.prijelaz.get(stanje+";"+znak)!=null)
						prijelazi.addAll(DKA.prijelaz.get(stanje+";"+znak));
				}
				if(!prijelazi.isEmpty())
					DKA.prijelaz.put(novaStanja.toString()+";"+znak, prijelazi);
			}
		}
		
		
		Set<String> stanjaAutomata = new TreeSet<>();
		for(String prijelaz:DKA.prijelaz.keySet()) {
			String stanje = prijelaz.split(";")[0];
			if(!stanje.contains("["))
				stanje = "["+stanje+"]";
			stanjaAutomata.add(stanje);
			
			String novoStanje = DKA.prijelaz.get(prijelaz).toString();
			if(!novoStanje.contains("["))
				novoStanje = "["+novoStanje+"]";
			stanjaAutomata.add(novoStanje);
		}
		
		ArrayList<String> preimenovanje = new ArrayList<>();
		preimenovanje.addAll(stanjaAutomata);
		
		Map<String, Set<Stanje>> stavkaOdluka = new HashMap<>();
		for(int i=0; i<preimenovanje.size(); i++) {
			String[] stanja = preimenovanje.get(i).substring(1,preimenovanje.get(i).length()-1).split(",");
			Set<Stanje> so = new HashSet<>(); 
			for(String stanjeIndex:stanja) {
				Stanje stanje = epsilonNKA.stanjaAutomata.get(Integer.parseInt(stanjeIndex.trim()));
				so.add(stanje);
			}
			stavkaOdluka.put(i+"",so);
		}
		
		Map<String, Set<String> > noviPrijelaz = new HashMap<>();
		for(String prijelaz : DKA.prijelaz.keySet()) {
			String stanje = prijelaz.split(";")[0];
			String znak = prijelaz.split(";")[1];
			Set<String> novoStanje = new TreeSet<>(); 
			String s = preimenovanje.indexOf(DKA.prijelaz.get(prijelaz).toString())+"";
			novoStanje.add(s);
			
			String stanjeUglate = stanje.contains("[")?stanje:"["+stanje+"]"; 
			String preimenovanoStanje = preimenovanje.indexOf(stanjeUglate)+"";
			noviPrijelaz.put(preimenovanoStanje+";" + znak, novoStanje);
		}
		DKA.prijelaz=noviPrijelaz;
		
		
		//racunanje redukcije Stanja
		for(int i=0; i<preimenovanje.size(); i++) {
			
			Set<Stanje> stavkeOdluke = stavkaOdluka.get(i+"");
			int maxPoz=-1;
			Set<String> maxPozOdluka = null;
			for(Stanje trenStavkaOdluka:stavkeOdluke) {
				if(!trenStavkaOdluka.stavka.isPotpuna())
					continue;
				Produkcija produkcijaStanja = trenStavkaOdluka.stavka.getProdukcija();
				
				if(produkcije.indexOf(produkcijaStanja)<maxPoz || maxPoz==-1) {
					maxPoz=produkcije.indexOf(produkcijaStanja);
					maxPozOdluka=trenStavkaOdluka.getOdluka();
				}
			}
			odlukaStanja.put(i+"", maxPozOdluka);
			redukcijaStanja.put(i+"", maxPoz);
			
		}

		return DKA;
	}
	
	private static Automat generirajEpsilonNka() {
		Automat epsilonNKA = new Automat();
		
		// stanje 0 je pocetna stavka koja odgovara generiranoj stavci: 0 -> S, S - pocetni nezavrsni znak gramatike
		Set<String> o = new TreeSet<>();
		o.add("&"); //znak kraja ulaza - "&"
		
		Stanje pocetno = new Stanje(stavke.get(0),o);
		epsilonNKA.stanjaAutomata.add(pocetno);
		
		Set<Integer> visited = new HashSet<>();
		Queue<Stanje> q = new LinkedList<>();
		q.add(pocetno);
		visited.add(epsilonNKA.stanjaAutomata.indexOf(pocetno));
		
		while(!q.isEmpty()) {
			Stanje trenutnoStanje = q.remove();
			int trenutnoStanjeIndex = epsilonNKA.stanjaAutomata.indexOf(trenutnoStanje);
			
			if(trenutnoStanje.stavka.isPotpuna())
				continue;
			
			Stavka novaStavka = new Stavka();
			if(trenutnoStanje.stavka.getPozicija()==trenutnoStanje.stavka.getProdukcija().getDesnaStrana().size()-1)
				novaStavka.setPotpuna(true);				
			else
				novaStavka.setPotpuna(false);
			
			novaStavka.setPozicija(trenutnoStanje.stavka.getPozicija()+1);
			novaStavka.setProdukcija(trenutnoStanje.stavka.getProdukcija());
			Set<String> od = trenutnoStanje.getOdluka();
			
			Stanje novoStanje = new Stanje(novaStavka, od);
			if(!epsilonNKA.stanjaAutomata.contains(novoStanje)) epsilonNKA.stanjaAutomata.add(novoStanje);
			int novoStanjeIndex = epsilonNKA.stanjaAutomata.indexOf(novoStanje);
			
			String znakPrijelaza = trenutnoStanje.stavka.desnoOdTocke().get(0);
			Set<String> skupPrijelaz = epsilonNKA.prijelaz.get(trenutnoStanjeIndex+";"+znakPrijelaza)!=null?epsilonNKA.prijelaz.get(trenutnoStanjeIndex+";"+znakPrijelaza):new TreeSet<>();
			skupPrijelaz.add(novoStanjeIndex+"");
			epsilonNKA.prijelaz.put(trenutnoStanjeIndex+";"+znakPrijelaza, skupPrijelaz);
			
			if(!visited.contains(novoStanjeIndex)) {
				q.add(novoStanje);
				visited.add(novoStanjeIndex);				
			}
			
			if(!nezavrsniZnakovi.contains(znakPrijelaza))
				continue;
			
			for(Stavka stavka:stavke) {
				if(!stavka.getProdukcija().getLijevaStrana().equals(znakPrijelaza) || !(stavka.getPozicija()==0))
					continue;
				
				ArrayList<String> beta = new ArrayList<>();
				ArrayList<String> desnaStrana = trenutnoStanje.stavka.getProdukcija().getDesnaStrana();
				int pozicija = trenutnoStanje.stavka.getPozicija()+1;
				for(;pozicija<desnaStrana.size();pozicija++) {
					beta.add(desnaStrana.get(pozicija));
				}
				
				Set<String> zapocinje = Zapocinje(beta);
				
				if(prazanNiz(beta)) {
					zapocinje.addAll(trenutnoStanje.getOdluka());
				}
				
				Stanje novoStanje2 = new Stanje();
				
				novoStanje2.setStavka(stavka);
				novoStanje2.setOdluka(zapocinje);
				if(!epsilonNKA.stanjaAutomata.contains(novoStanje2)) epsilonNKA.stanjaAutomata.add(novoStanje2);					
				
				int novoStanje2Index=epsilonNKA.stanjaAutomata.indexOf(novoStanje2);
				
				Set<String> ss = epsilonNKA.prijelaz.get(trenutnoStanjeIndex+";$")!=null?epsilonNKA.prijelaz.get(trenutnoStanjeIndex+";$"):new TreeSet<>();
				ss.add(novoStanje2Index+"");
				epsilonNKA.prijelaz.put(trenutnoStanjeIndex+";$", ss);
				
				if(!visited.contains(novoStanje2Index)) {
					q.add(novoStanje2);
					visited.add(novoStanje2Index);					
				}
			}
		}
		return epsilonNKA;
	}
	
	private static boolean prazanNiz(ArrayList<String> niz) {
		boolean rez = true;
		for(String znak:niz) {
			if(!prazniZnakovi.contains(znak)) {
				rez=false;
				break;
			}
		}
		return rez;
	}

	private static Set<String> Zapocinje(ArrayList<String> niz){
		if(niz==null) 
			return null;
		
		Set<String> skupZapocinje = new TreeSet<>();
		for(String znak:niz) {
			Set<String> zap;
			if(zapocinje.get(znak)==null) {
				zap = new TreeSet<>();
			}else {
				zap = zapocinje.get(znak);
			}
			skupZapocinje.addAll(zap);
			if(!prazniZnakovi.contains(znak))
				break;
		}
		return skupZapocinje;
	}

	private static void izracunajZapocinje() {
		Map<ArrayList<String>, Boolean> zapocinjeZnakom = new HashMap<>();
		for(Produkcija prod:produkcije) {
			String lijevaStrana = prod.getLijevaStrana();
			int i=0;
			while(true) {
				String desniZnak = prod.getDesnaStrana().get(i);
				if(desniZnak.equals("$")) {
					break;
				}
				ArrayList<String> l = new ArrayList<>();
				l.add(lijevaStrana);
				l.add(desniZnak);
				zapocinjeZnakom.put(l, true);
				if(prazniZnakovi.contains(desniZnak)&&i!=prod.getDesnaStrana().size()-1) {
					i++;
				}else {
					break;
				}
			}
		}
		
		Set<String> znakovi = new TreeSet<>();
		for(ArrayList<String> par:zapocinjeZnakom.keySet()) {
			znakovi.addAll(par);
		}
		
		while(true) {
			boolean nastavi=false;
			
			for(String a:znakovi) {
				for(String b:znakovi) {
					ArrayList<String> ab = new ArrayList<>();
					ab.add(a);
					ab.add(b);
					
					if(zapocinjeZnakom.get(ab)!=null)
						continue;
					
					if(a.equals(b)) {
						zapocinjeZnakom.put(ab, true);
						nastavi=true;
						continue;
					}
					
					for(String c:znakovi) {
						ArrayList<String> ac = new ArrayList<>();
						ac.add(a);
						ac.add(c);
						ArrayList<String> cb = new ArrayList<>();
						cb.add(c);
						cb.add(b);
						
						if(zapocinjeZnakom.get(ac)==null || zapocinjeZnakom.get(cb)==null)
							continue;
						
						zapocinjeZnakom.put(ab, true);
						nastavi=true;
						break;
					}
				}
			}
			if(!nastavi)
				break;
		}
		
		for(String nezavrsniZnak: nezavrsniZnakovi) {
			for(String zavrsni: zavrsniZnakovi) {
				ArrayList<String> l = new ArrayList<>();
				l.add(nezavrsniZnak);
				l.add(zavrsni);
				if(zapocinjeZnakom.get(l)!=null) {
					Set<String> sz;
					if(zapocinje.get(nezavrsniZnak)==null)
						sz = new TreeSet<>();
					else
						sz = zapocinje.get(nezavrsniZnak);
					sz.add(zavrsni);
					zapocinje.put(nezavrsniZnak, sz);
				}
			}
		}
		
		for(String zavrsni:zavrsniZnakovi) {
			Set<String> s = new TreeSet<>();
			s.add(zavrsni);
			zapocinje.put(zavrsni, s);
		}
	}
	
	public static void printMap(Map<ArrayList<String>, Boolean> map) {
        for (Map.Entry<ArrayList<String>, Boolean> entry : map.entrySet()) {
            ArrayList<String> key = entry.getKey();
            Boolean value = entry.getValue();

            System.out.print("Key: ");
            System.out.println(key);
            System.out.println("Value: " + value);
        }
    }

	private static void nadjiPrazneZnakove() {
		while(true) {			
			Set<String> noviPrazniZnakovi = new TreeSet<>();
			for(Produkcija prod : produkcije) {
				
				ArrayList<String> desnaStrana = prod.getDesnaStrana();
				String lijevaStrana = prod.getLijevaStrana();
				if(prazniZnakovi.contains(lijevaStrana))
					continue;
				
				if(desnaStrana.get(0).equals("$")){
					noviPrazniZnakovi.add(lijevaStrana);
					continue;
				}
				
				boolean sviPrazni=true;
				for(String znak:desnaStrana) {
					if(!prazniZnakovi.contains(znak)) {
						sviPrazni=false;
						break;
					}
				}
				
				if(sviPrazni) 
					noviPrazniZnakovi.add(lijevaStrana);
			}
			if(noviPrazniZnakovi.size()==0)
				break;
			prazniZnakovi.addAll(noviPrazniZnakovi);
		}
	}

	private static void generirajStavke() {
		for(Produkcija prod : produkcije) {
			ArrayList<String> desnaStrana = prod.getDesnaStrana();
			if(desnaStrana.get(0).equals("$")) {
				stavke.add(new Stavka(prod, 0, null, true));
				continue;
			}
			for(int i=0;i<desnaStrana.size();i++) {
				stavke.add(new Stavka(prod, i, null,false));
			}
			stavke.add(new Stavka(prod, desnaStrana.size(), null, true));
		}
	}

	private static void parseInput() throws FileNotFoundException {
		File myFile = new File("G:\\My Drive\\Mape\\Faks\\5. semestar\\ppjLab2\\src\\langdefs\\simplePpjLang.san");
		Scanner myScn = new Scanner(myFile);
		
		String lijevaStrana = null;
		boolean pocetniNezavrsni=true;
		while(myScn.hasNextLine()) {
			String line = myScn.nextLine();
			
			if(line.contains("%V")) {
				String[] nezavrsni = line.substring(3).split(" ");
				nezavrsniZnakovi.addAll(Arrays.asList(nezavrsni));
				continue;
			}
			nezavrsniZnakovi.add("0");
			
			if(line.contains("%T")) {
				String[] zavrsni = line.substring(3).split(" ");
				zavrsniZnakovi.addAll(Arrays.asList(zavrsni));
				continue;
			}
			sviZnakovi.addAll(zavrsniZnakovi);
			sviZnakovi.addAll(nezavrsniZnakovi);
			
			if(line.contains("%Syn")) {
				String[] sinkronizacijski = line.substring(5).split(" ");
				sinkronizacijskiZnakovi.addAll(Arrays.asList(sinkronizacijski));
				continue;
			}
			
			if(line.charAt(0)!=' ') {
				lijevaStrana=line;
				if(pocetniNezavrsni) {
					pocetniNezavrsni=false;
					ArrayList<String> list = new ArrayList<>(Arrays.asList(line));				
					produkcije.add(new Produkcija("0", list));
				}
			}else {
				String desnaStrana = line.substring(1);
				String[] desnaStranaArr = desnaStrana.split(" ");
				ArrayList<String> desnaStranaList = new ArrayList<>(Arrays.asList(desnaStranaArr));
				produkcije.add(new Produkcija(lijevaStrana,desnaStranaList));
			}
		}
		
		myScn.close();
	}

}
