package br.com.deckmarket.config;

import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Deck Market API")
                        .version("1.0.0")
                        .description("API backend do Projeto Deck Market: marketplace de card games")
                        .contact(new Contact()
                                .name("Bruno Ciccio - Desenvolvedor Full Stack")
                                .email("dev.bruno.ciccio@gmail.com"))
                        .license(new License().name("Apache 2.0").url("https://www.apache.org/licenses/LICENSE-2.0")));
    }

    @Bean
    public GroupedOpenApi publicApi() {
        return GroupedOpenApi.builder()
                .group("public")
                .pathsToMatch(
                        "/cards/**", 
                        "/carrinhos/**", 
                        "/categorias/**", 
                        "/pagamentos/**", 
                        "/pedidos/**", 
                        "/usuarios/**"
                )
                .build();
    }

}
