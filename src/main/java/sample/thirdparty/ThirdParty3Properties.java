package sample.thirdparty;

public class ThirdParty3Properties {

	private String name;

	private int counter = 42;

	public ThirdParty3Properties(String name) {
		this.name = name;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getCounter() {
		return this.counter;
	}

	public void setCounter(int counter) {
		this.counter = counter;
	}

}
