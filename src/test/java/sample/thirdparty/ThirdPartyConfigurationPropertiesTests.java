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

}
