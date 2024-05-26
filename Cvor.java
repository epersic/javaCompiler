

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Cvor {
	public String znak = null;
	public Cvor roditelj = null;
	public List<Cvor> djeca = new ArrayList<>();
	
	public TablicaZnakova tablicaZnakova;
	public Map<String, String> svojstva = new HashMap<>();
	
	public Cvor(String Znak, Cvor roditelj) {
		this.znak=Znak;
		this.roditelj=roditelj;
	}

	public Cvor(String Znak) {
		this.znak=Znak;
	}

	@Override
	public String toString() {
		return "Cvor [Znak=" + znak + "]";
	}
	
	
}
