package sample.thirdparty;

public class ThirdParty2Properties {

	private final String name;

	private final int counter;

	public ThirdParty2Properties(String name) {
		this(name, null);
	}

	public ThirdParty2Properties(String name, Integer counter) {
		this.name = name;
		this.counter = (counter != null) ? counter : 42;
	}

	public String getName() {
		return this.name;
	}

	public int getCounter() {
		return this.counter;
	}

}
