package kr.hs.dgsw.dbook.dbook.Configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.hs.dgsw.dbook.dbook.Json.HtmlCharacterEscapes;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.server.WebServer;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
@RequiredArgsConstructor
public class MyWebAppConfiguration extends WebMvcConfigurerAdapter implements WebMvcConfigurer{
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

        /*
        *  ex )  프로젝트 저장 경로 : D/dev/{PROJECT} 면
        *        path = "/dev"
        *        location = "/D:/dev"
        * */

        String path = "/dev";
        String location = "D:/dev";

        registry.addResourceHandler(path+ "/WebServer/upload/**").addResourceLocations("file:/"+ location +"v/WebServer/upload/");
    }

    private final ObjectMapper objectMapper;

    @Bean
    public MappingJackson2HttpMessageConverter jsonEscapeConverter() {
        ObjectMapper copy = objectMapper.copy();
        copy.getFactory().setCharacterEscapes(new HtmlCharacterEscapes());
        return new MappingJackson2HttpMessageConverter(copy);
    }


}
