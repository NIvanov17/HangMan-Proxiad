package com.example.config;

import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.ws.config.annotation.EnableWs;
import org.springframework.ws.transport.http.MessageDispatcherServlet;
import org.springframework.ws.wsdl.wsdl11.DefaultWsdl11Definition;
import org.springframework.xml.xsd.SimpleXsdSchema;
import org.springframework.xml.xsd.XsdSchema;

@EnableWs
@Configuration
public class WebServiceConfig {

	private final ApplicationContext applicationContext;

	public WebServiceConfig(ApplicationContext applicationContext) {
		this.applicationContext = applicationContext;
	}

	@Bean
	ServletRegistrationBean<MessageDispatcherServlet> messageDispatcherServlet() {
		MessageDispatcherServlet servlet = new MessageDispatcherServlet();
		servlet.setApplicationContext(applicationContext);
		servlet.setTransformWsdlLocations(true);
		return new ServletRegistrationBean<>(servlet, "/ws/*");
	}
	
    @Bean(name = "players")
    public DefaultWsdl11Definition defaultWsdl11Definition(XsdSchema playersSchema) {
        DefaultWsdl11Definition wsdl11Definition = new DefaultWsdl11Definition();
        wsdl11Definition.setPortTypeName("PlayersPort");
        wsdl11Definition.setLocationUri("/ws/players/games");
        wsdl11Definition.setTargetNamespace("http://www.example.org/players");
        wsdl11Definition.setSchema(playersSchema);
        return wsdl11Definition;
    }

	@Bean(name = "playersXsdSchema")
	public XsdSchema playersSchema() {

		return new SimpleXsdSchema(new ClassPathResource("/xsdl/players.xsd"));
	}
	
}
