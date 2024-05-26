import java.util.ArrayList;
import java.util.Arrays;

public class Token {
    private  String token;
    private  int numOfRow;
    private String command;
    private ArrayList<Token> djeca = new ArrayList<>();
    private Token roditelj;
    private boolean isNumber=false;
    private int num;

    public String getToken() {
        return token;
    }
    public boolean isNumber() {
        return isNumber;
    }
    public void setNumber(boolean isNumber) {
        this.isNumber = isNumber;
    }
    public void setToken(String token) {
        this.token = token;
    }
    public int getNumOfRow() {
        return numOfRow;
    }
    public ArrayList<Token> getDjeca() {
        return djeca;
    }
    public void setDjeca(ArrayList<Token> djeca) {
        this.djeca = djeca;
    }
    public Token getRoditelj() {
        return roditelj;
    }
    public void setRoditelj(Token roditelj) {
        this.roditelj = roditelj;
    }
    public int getNum() {
        return num;
    }
    public void setNum(int num) {
        this.num = num;
    }
    public void setNumOfRow(int numOfRow) {
        this.numOfRow = numOfRow;
    }
    public String getCommand() {
        return command;
    }
    public void setCommand(String command) {
        this.command = command;
    }

    public Token(String token,int numOfRow,String command) {
        this.command=command;
        this.token=token;
        this.numOfRow=numOfRow;
    }
    public Token(int number) {
        this.isNumber=true;
        this.num=number;
    }
    
	@Override
	public String toString() {
		return "Token [token=" + token + ", numOfRow=" + numOfRow + ", command=" + command + 
				", roditelj=" + roditelj + ", isNumber=" + isNumber + ", num=" + num + "]";
	}
	
}