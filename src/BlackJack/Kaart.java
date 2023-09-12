package BlackJack;

public class Kaart {
	private String type;
	private String value;
	private boolean isVisible = true;
	
	public Kaart(String type, String value) {
		this.type = type;
		this.value = value;
	}
	

	public String getType() {
		return this.type;
	}
	
	public String getValue() {
		return this.value;
	}
	
	public int getPlayValue() {
		try {
			int value = Integer.parseInt(getValue());
			return value;
		} catch (Exception e) {
			int x = isAce() ? 11 : 10;
			return x;
		}
	}
	
	public boolean isAce() {
		return getValue().equals("A");
	}
	
	public boolean isVisible() {
		return isVisible;
	}
	
	public void setVisible(boolean visible) {
		this.isVisible = visible;
	}
	
	public String toString() {
		return getType() + " " + getValue();
	}
}
