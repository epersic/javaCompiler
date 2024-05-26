import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;

public class Stanje {
	public Stavka stavka;
	public Set<String> odluka = new TreeSet<>();
	
	public Stanje() { }
	
	public Stanje(Stavka stavka, Set<String> odluka) {
		super();
		this.stavka = stavka;
		this.odluka = odluka;
	}

	public Stavka getStavka() {
		return stavka;
	}

	public void setStavka(Stavka stavka) {
		this.stavka = stavka;
	}

	public Set<String> getOdluka() {
		return odluka;
	}

	public void setOdluka(Set<String> odluka) {
		this.odluka = odluka;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Stanje other = (Stanje) obj;
		return odluka.equals(other.odluka) && stavka.equals(other.stavka);
	}

	@Override
	public String toString() {
		return "Stanje [stavka=" + stavka + ", odluka=" + odluka + "]\n";
	}
	
	
}
