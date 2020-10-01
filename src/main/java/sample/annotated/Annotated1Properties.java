package sample.annotated;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.boot.context.properties.bind.DefaultValue;

@ConfigurationProperties("test.1")
@ConstructorBinding
public class Annotated1Properties {

	private final String name;

	private final int counter;

	public Annotated1Properties(String name, @DefaultValue("42") int counter) {
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
