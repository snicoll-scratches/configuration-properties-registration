package sample.thirdparty;

import org.junit.jupiter.api.Test;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.context.properties.ImportAsConfigurationPropertiesBean;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.context.annotation.Configuration;

import static org.assertj.core.api.Assertions.assertThat;

class ThirdPartyConfigurationPropertiesTests {

	private final ApplicationContextRunner contextRunner = new ApplicationContextRunner();

	// With this approach, default values have to be handled in code (-> no metadata)
	@Test
	void thirdPartyConfigurationPropertiesRegisteredWithImportAsConfigurationPropertiesAndNoPrefix() {
		this.contextRunner.withUserConfiguration(ImportThirdParty1PropertiesConfiguration.class)
				.withPropertyValues("name=test").run((context) -> {
					ThirdParty1Properties properties = context.getBean(ThirdParty1Properties.class);
					assertThat(properties.getName()).isEqualTo("test");
					assertThat(properties.getCounter()).isEqualTo(42);
				});
	}

	// The other annotation leads to a failure because the @ConfigurationProperties
	// annotation is expected
	@Test
	void thirdPartyConfigurationPropertiesRegisteredWithEnablesConfigurationPropertiesAndNoPrefix() {
		this.contextRunner.withUserConfiguration(EnableThirdParty1PropertiesPrefixConfiguration.class)
				.withPropertyValues("name=test").run((context) -> {
					assertThat(context).hasFailed();
					assertThat(context).getFailure()
							.hasMessageContaining("No ConfigurationProperties annotation found");
				});
	}

	@Test
	void thirdPartyConfigurationPropertiesRegisteredWithWithImportAsConfigurationPropertiesAndPrefix() {
		this.contextRunner.withUserConfiguration(ImportThirdParty1PropertiesPrefixConfiguration.class)
				.withPropertyValues("name=noise").withPropertyValues("test.1.name=test").run((context) -> {
					ThirdParty1Properties properties = context.getBean(ThirdParty1Properties.class);
					assertThat(properties.getName()).isEqualTo("test");
					assertThat(properties.getCounter()).isEqualTo(42);
				});
	}

	@Test
	void thirdPartyConfigurationPropertiesWithTwoConstructorsRegisteredWithImportAsConfigurationProperties() {
		this.contextRunner.withUserConfiguration(ImportThirdParty2PropertiesConfiguration.class)
				.withPropertyValues("name=test").run((context) -> {
					assertThat(context).hasFailed();
					assertThat(context).getFailure()
							.hasMessageContaining("Unable process @ImportAsConfigurationPropertiesBean annotations")
							.getCause().hasMessageContaining("Unable to deduce @ConfigurationProperties bind method");
				});
	}

	@Test
	void thirdPartyConfigurationPropertiesWithCommonPrefixRegisteredWithImportAsConfigurationProperties() {
		this.contextRunner.withUserConfiguration(ImportThirdParty1And1BisPropertiesConfiguration.class)
				.withPropertyValues("common.name=test").run((context) -> {
					ThirdParty1Properties properties = context.getBean(ThirdParty1Properties.class);
					assertThat(properties.getName()).isEqualTo("test");
					assertThat(properties.getCounter()).isEqualTo(42);
					ThirdParty1BisProperties anotherProperties = context.getBean(ThirdParty1BisProperties.class);
					assertThat(anotherProperties.getName()).isEqualTo("test");
					assertThat(anotherProperties.getCounter()).isEqualTo(42);

				});
	}

	// If a constructor is present, you can't opt-out of that
	@Test
	void thirdPartyConfigurationPropertiesWithImportAsConfigurationPropertiesCanOptInForJavaBeanConvention() {
		this.contextRunner.withUserConfiguration(ImportThirdParty3PropertiesConfiguration.class)
				.withPropertyValues("test.name=test", "test.counter=20").run((context) -> {
					ThirdParty3Properties properties = context.getBean(ThirdParty3Properties.class);
					assertThat(properties.getName()).isEqualTo("test");
					assertThat(properties.getCounter()).isEqualTo(42);
				});
	}

	@Configuration(proxyBeanMethods = false)
	@ImportAsConfigurationPropertiesBean(ThirdParty1Properties.class)
	static class ImportThirdParty1PropertiesConfiguration {

	}

	@Configuration(proxyBeanMethods = false)
	@EnableConfigurationProperties(ThirdParty1Properties.class)
	static class EnableThirdParty1PropertiesPrefixConfiguration {

	}

	@Configuration(proxyBeanMethods = false)
	@ImportAsConfigurationPropertiesBean(value = ThirdParty1Properties.class, prefix = "test.1")
	static class ImportThirdParty1PropertiesPrefixConfiguration {

	}

	@Configuration(proxyBeanMethods = false)
	@ImportAsConfigurationPropertiesBean(value = ThirdParty2Properties.class, prefix = "test.2")
	static class ImportThirdParty2PropertiesConfiguration {

	}

	@Configuration(proxyBeanMethods = false)
	@ImportAsConfigurationPropertiesBean(value = { ThirdParty1Properties.class, ThirdParty1BisProperties.class },
			prefix = "common")
	static class ImportThirdParty1And1BisPropertiesConfiguration {

	}

	@Configuration(proxyBeanMethods = false)
	@ImportAsConfigurationPropertiesBean(value = ThirdParty3Properties.class, prefix = "test")
	static class ImportThirdParty3PropertiesConfiguration {

	}

}
