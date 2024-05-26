

import java.util.ArrayList;
import java.util.Objects;

public class Produkcija {
	private String lijevaStrana;
	private ArrayList<String> desnaStrana;
	
	public Produkcija() {}
	
	public Produkcija(String lijevaStrana, ArrayList<String> desnaStrana) {
		super();
		this.lijevaStrana = lijevaStrana;
		this.desnaStrana = desnaStrana;
	}
	
	
	public String getLijevaStrana() {
		return lijevaStrana;
	}
	public void setLijevaStrana(String lijevaStrana) {
		this.lijevaStrana = lijevaStrana;
	}
	public ArrayList<String> getDesnaStrana() {
		return desnaStrana;
	}
	public void setDesnaStrana(ArrayList<String> desnaStrana) {
		this.desnaStrana = desnaStrana;
	}

	@Override
	public int hashCode() {
		return Objects.hash(desnaStrana, lijevaStrana);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Produkcija other = (Produkcija) obj;
		return desnaStrana.equals(other.desnaStrana) && lijevaStrana.equals(other.lijevaStrana);
	}

	@Override
	public String toString() {
		return "Produkcija [lijevaStrana=" + lijevaStrana + ", desnaStrana=" + desnaStrana;
	}
	
	
}
