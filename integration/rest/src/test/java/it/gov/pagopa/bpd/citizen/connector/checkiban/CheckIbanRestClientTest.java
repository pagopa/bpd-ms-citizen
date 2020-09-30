package it.gov.pagopa.bpd.citizen.connector.checkiban;

import com.github.tomakehurst.wiremock.junit.WireMockClassRule;
import it.gov.pagopa.bpd.citizen.connector.checkiban.config.CheckIbanRestConnectorConfig;
import it.gov.pagopa.bpd.common.connector.BaseFeignRestClientTest;
import lombok.SneakyThrows;
import org.junit.ClassRule;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.support.TestPropertySourceUtils;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;


@TestPropertySource(
        locations = "classpath:config/BpdCitizenRestConnector.properties",
        properties = "spring.application.name=bpd-ms-citizen-integration-rest")
@ContextConfiguration(initializers = CheckIbanRestClientTest.RandomPortInitializer.class,
        classes = {CheckIbanRestConnectorImpl.class, CheckIbanRestConnectorConfig.class})
public class CheckIbanRestClientTest extends BaseFeignRestClientTest {

    @ClassRule
    public static WireMockClassRule wireMockRule = new WireMockClassRule(wireMockConfig()
            .dynamicPort()
            .usingFilesUnderClasspath("stubs/checkIban")
    );

    @Test
    public void checkIban_test() {
        String fiscalCode = "ABCDEF12L12A123K";
        String payOffInstr = "IT12A1234512345123456789012";
        final String actualResponse = restConnector.checkIban(payOffInstr, fiscalCode);

        assertNotNull(actualResponse);
        assertEquals("OK", actualResponse);
    }

    @Autowired
    private CheckIbanRestClient restClient;

    @Autowired
    private CheckIbanRestConnector restConnector;

    public static class RandomPortInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
        @SneakyThrows
        @Override
        public void initialize(ConfigurableApplicationContext applicationContext) {
            TestPropertySourceUtils
                    .addInlinedPropertiesToEnvironment(applicationContext,
                            String.format("rest-client.checkiban.base-url=http://%s:%d",
                                    wireMockRule.getOptions().bindAddress(),
                                    wireMockRule.port())
                    );
        }
    }
}