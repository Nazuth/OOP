package WT_Day1;

public class Application {
	private String name;
	
	
	public Application(String name) {
		this.name = name;
	}
	
	
	public void print() {
		if (name != null) {
			System.out.println("Printing application name..");
			System.out.println("Application name: " + name);

		}
	}
	
	public static void main(String[] args) {
		Application app = new Application("NewApp");
		app.print();
	}
}
