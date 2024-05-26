import java.util.ArrayList;
import java.util.Objects;
import java.util.Set;

public class Stavka {
	private Produkcija produkcija;
	private int pozicija;
	private boolean potpuna;
	

	public ArrayList<String> desnoOdTocke(){
		ArrayList<String> desnoOdTocke = new ArrayList<>();
		ArrayList<String> desnaStrana = produkcija.getDesnaStrana();
		for(int i=pozicija;i<desnaStrana.size();i++) {
			desnoOdTocke.add(desnaStrana.get(i));
		}
		return desnoOdTocke;
		
	}
	
	
	public Stavka () { }

	public Stavka(Produkcija produkcija, int pozicija, Set<String> odluka, boolean potpuna) {
		super();
		this.produkcija = produkcija;
		this.pozicija = pozicija;
		this.potpuna = potpuna;
	}

	public Produkcija getProdukcija() {
		return produkcija;
	}

	public void setProdukcija(Produkcija produkcija) {
		this.produkcija = produkcija;
	}

	public int getPozicija() {
		return pozicija;
	}

	public void setPozicija(int pozicija) {
		this.pozicija = pozicija;
	}

	public boolean isPotpuna() {
		return potpuna;
	}

	public void setPotpuna(boolean potpuna) {
		this.potpuna = potpuna;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		
		Stavka other = (Stavka) obj;
		
		return potpuna == other.potpuna && pozicija == other.pozicija
				&& produkcija.equals(other.produkcija);
	}

	@Override
	public String toString() {
		return "Stavka [produkcija=" + produkcija + ", pozicija=" + pozicija + ", potpuna="+ potpuna;
	}


	
	
}
