import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

public class Automat {
	public Map<String, Set<String> > prijelaz = new TreeMap<>();
	ArrayList<Stanje> stanjaAutomata = new ArrayList<>();
	
	public Automat () { }
	
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
			Set<String> susjednaStanja = prijelaz.get(trenutnoStanje+";$");
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
	
	public TreeSet<String> prijelaz(TreeSet<String> trenutnaStanja, String znak){
		trenutnaStanja = epsilonOkolina(trenutnaStanja);
		
		TreeSet<String> skupNovihStanja = new TreeSet<String>();
		for(String stanje:trenutnaStanja)
		{
			if(prijelaz.get(stanje+";"+znak)==null)
				continue;
			
			for(String novoStanje:prijelaz.get(stanje+";"+znak)) {
				skupNovihStanja.add(novoStanje);
			}
		}
		trenutnaStanja = skupNovihStanja;
		trenutnaStanja = epsilonOkolina(trenutnaStanja);
		return trenutnaStanja;
	}

}
