package it.vitalegi.cocorido;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import it.vitalegi.cocorido.util.ListUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@EnableWebMvc
@Configuration
@ComponentScan(basePackages = { "it.vitalegi.cocorido" })
public class MvcConfig implements WebMvcConfigurer {

	@Value("${cors.allowedOrigins}")
	List<String> corsAllowedOrigins;

	@Value("${cors.allowedMethods}")
	List<String> corsAllowedMethods;
	
	@Value("${frontend.resources.location}")
	String frontendResources;

	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**")//
				.allowedOrigins(ListUtil.toArray(String.class, corsAllowedOrigins))//
				.allowedMethods(ListUtil.toArray(String.class, corsAllowedMethods));
	}

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		log.info("Frontend resource folder: {}", frontendResources);
		registry.addResourceHandler("/**")//
				.addResourceLocations(frontendResources)//
				.setCachePeriod(60);
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addWebRequestInterceptor(new LoggerContextWebRequestInterceptor());
	}

}