package sample.annotated;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.boot.context.properties.bind.DefaultValue;

@ConfigurationProperties("test.3")
public class Annotated3Properties {

	private final String name;

	private final int counter;

	public Annotated3Properties(String name, @DefaultValue("42") int counter) {
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
