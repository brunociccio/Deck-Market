package br.com.deckmarket;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@SpringBootApplication
@Controller
public class DeckmarketApplication {

	public static void main(String[] args) {
		SpringApplication.run(DeckmarketApplication.class, args);
	}

	@RequestMapping("/home")
    @ResponseBody
    public String home() {
        return "API Back-End do Projeto Deck Market: Marktplace de card games ";
    }

}
