package sample.annotated;

import org.junit.jupiter.api.Test;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.context.properties.ImportAsConfigurationPropertiesBean;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.context.annotation.Configuration;

import static org.assertj.core.api.Assertions.assertThat;

class AnnotatedConfigurationPropertiesTests {

	private final ApplicationContextRunner contextRunner = new ApplicationContextRunner();

	@Test
	void annotatedConfigurationPropertiesRegisteredWithEnableConfigurationProperties() {
		this.contextRunner.withUserConfiguration(EnableAnnotated1PropertiesConfiguration.class)
				.withPropertyValues("test.1.name=test").run((context) -> {
					Annotated1Properties properties = context.getBean(Annotated1Properties.class);
					assertThat(properties.getName()).isEqualTo("test");
					assertThat(properties.getCounter()).isEqualTo(42);
				});
	}

	@Test
	void annotatedConfigurationPropertiesRegisteredWithImportAsConfigurationPropertiesAndNoPrefix() {
		this.contextRunner.withUserConfiguration(ImportAnnotated1PropertiesConfiguration.class)
				.withPropertyValues("test.1.name=test").run((context) -> {
					Annotated1Properties properties = context.getBean(Annotated1Properties.class);
					assertThat(properties.getName()).isEqualTo("test");
					assertThat(properties.getCounter()).isEqualTo(42);
				});
	}

	// Prefix ignored in import annotation if set in the base class?
	@Test
	void annotatedConfigurationPropertiesRegisteredWithImportAsConfigurationPropertiesAndSpecificPrefix() {
		this.contextRunner.withUserConfiguration(ImportAnnotated1PropertiesPrefixOverrideConfiguration.class)
				.withPropertyValues("test.1.name=test").withPropertyValues("override.1.name=override")
				.run((context) -> {
					Annotated1Properties properties = context.getBean(Annotated1Properties.class);
					assertThat(properties.getName()).isEqualTo("test");
					assertThat(properties.getCounter()).isEqualTo(42);
				});
	}

	@Test
	void annotatedConfigurationPropertiesRegisteredWithTwoConstructorsAndEnableConfigurationProperties() {
		this.contextRunner.withUserConfiguration(EnableAnnotated2PropertiesConfiguration.class)
				.withPropertyValues("test.2.name=test").run((context) -> {
					Annotated2Properties properties = context.getBean(Annotated2Properties.class);
					assertThat(properties.getName()).isEqualTo("test");
					assertThat(properties.getCounter()).isEqualTo(42);
				});
	}

	@Test
	void annotatedConfigurationPropertiesWithMissingConstructorBindingAndEnableConfigurationProperties() {
		this.contextRunner.withUserConfiguration(EnableAnnotated3PropertiesConfiguration.class)
				.withPropertyValues("test.3.name=test").run((context) -> {
					assertThat(context).hasFailed();
					assertThat(context).getFailure()
							.hasMessageContaining("No qualifying bean of type 'java.lang.String' available");
				});
	}

	@Test
	void annotatedConfigurationPropertiesWithMissingConstructorBindingAndImportAsConfigurationProperties() {
		this.contextRunner.withUserConfiguration(ImportAnnotated3PropertiesConfiguration.class)
				.withPropertyValues("test.3.name=test").run((context) -> {
					assertThat(context).hasNotFailed();
					Annotated3Properties properties = context.getBean(Annotated3Properties.class);
					assertThat(properties.getName()).isEqualTo("test");
					assertThat(properties.getCounter()).isEqualTo(42);
				});
	}

	@Configuration(proxyBeanMethods = false)
	@EnableConfigurationProperties(Annotated1Properties.class)
	static class EnableAnnotated1PropertiesConfiguration {

	}

	@Configuration(proxyBeanMethods = false)
	@ImportAsConfigurationPropertiesBean(Annotated1Properties.class)
	static class ImportAnnotated1PropertiesConfiguration {

	}

	@Configuration(proxyBeanMethods = false)
	@ImportAsConfigurationPropertiesBean(value = Annotated1Properties.class, prefix = "override.1")
	static class ImportAnnotated1PropertiesPrefixOverrideConfiguration {

	}

	@Configuration(proxyBeanMethods = false)
	@EnableConfigurationProperties(Annotated2Properties.class)
	static class EnableAnnotated2PropertiesConfiguration {

	}

	@Configuration(proxyBeanMethods = false)
	@EnableConfigurationProperties(Annotated3Properties.class)
	static class EnableAnnotated3PropertiesConfiguration {

	}

	@Configuration(proxyBeanMethods = false)
	@ImportAsConfigurationPropertiesBean(Annotated3Properties.class)
	static class ImportAnnotated3PropertiesConfiguration {

	}

}
