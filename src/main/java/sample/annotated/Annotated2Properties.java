package sample.annotated;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.boot.context.properties.bind.DefaultValue;

@ConfigurationProperties("test.2")
public class Annotated2Properties {

	private final String name;

	private final int counter;

	public Annotated2Properties(String name) {
		this(name, 42);
	}

	@ConstructorBinding
	public Annotated2Properties(String name, @DefaultValue("42") int counter) {
		this.name = name;
		this.counter = counter;
	}

	public String getName() {
		return this.name;
	}

	public int getCounter() {
		return this.counter;
	}

}
