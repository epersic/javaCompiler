
import java.util.ArrayList;

public class TablicaZnakova {
	public ArrayList<String> znak = new ArrayList<>();
	public ArrayList<String> tip = new ArrayList<>();
	public ArrayList<Boolean> l_izraz = new ArrayList<>();
	public ArrayList<Boolean> def = new ArrayList<>();
	public TablicaZnakova ugnjezdujuca = null;
	public ArrayList<TablicaZnakova> ugnjezdena = new ArrayList<>();

	public TablicaZnakova(TablicaZnakova ugnjezdujuca) {
		this.ugnjezdujuca = ugnjezdujuca;
		if(ugnjezdujuca!=null) ugnjezdujuca.ugnjezdena.add(this);
	}

	public void add(String znak, String tip, boolean l_izraz, boolean def) {
		this.znak.add(znak);
		this.tip.add(tip);
		this.l_izraz.add(l_izraz);
		this.def.add(def);
	}

	public String getZnak(int index) {
		return znak.get(index);
	}

	public String getTip(int index) {
		return tip.get(index);
	}

	public boolean getLizraz(int index) {
		return l_izraz.get(index);
	}

	public boolean getDef(int index) {
		return def.get(index);
	}

	public boolean exists(String idn) {
		int index = znak.indexOf(idn);
		if(index==-1 && ugnjezdujuca==null)
			return false;

		if(index==-1)
			return ugnjezdujuca.exists(idn);

		return true;
	}

	public int existsDefFunction(String idn) {

		for(int i=0;i<znak.size();i++) {
			if(getZnak(i).equals(idn) && getTip(i).contains("->") && getDef(i)==true) {
				return i;
			}
		}
		if(ugnjezdujuca==null) return -1;
		return ugnjezdujuca.existsDefFunction(idn);
	}

	public String getTip(String znak) {
		int index = this.znak.indexOf(znak);

		if(index==-1 && ugnjezdujuca==null)
			return null;

		if(index==-1)
			return ugnjezdujuca.getTip(znak);

		return getTip(index);
	}

	public Boolean getLizraz(String znak) {
		int index = this.znak.indexOf(znak);

		if(index==-1 && ugnjezdujuca==null)
			return null;

		if(index==-1)
			return ugnjezdujuca.getLizraz(znak);

		return getLizraz(index);
	}

	public Boolean getDef(String znak) {
		int index = this.znak.indexOf(znak);

		if(index==-1 && ugnjezdujuca==null)
			return null;

		if(index==-1)
			return ugnjezdujuca.getDef(znak);

		return getDef(index);
	}

	public boolean postojiNeDefinirana(TablicaZnakova globalna) {
		for(int i=0;i<znak.size();i++){
			if(!getTip(i).contains("->")) continue;
			boolean found = false;
			for(int j=0;j<globalna.znak.size();j++){
				if(globalna.getZnak(j).equals(getZnak(i)) && globalna.getDef(j) && globalna.getTip(j).equals(getTip(i))){
					found = true;
					break;
				}
			}
			if(!found) return true;
		}

		if(ugnjezdena.isEmpty()) return false;
		for(TablicaZnakova ugnj : ugnjezdena){
			if(ugnj.postojiNeDefinirana(globalna)) return true;
		}
		return false;
	}
}
