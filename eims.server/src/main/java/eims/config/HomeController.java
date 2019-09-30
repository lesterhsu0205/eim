package eims.config;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Home redirection to swagger api documentation 
 */
@Controller
public class HomeController {
	@RequestMapping(value = "/")
	public String index() {
		return "redirect:index.html#!/main/blank";
//		return "redirect:index.html";
	}
}

//public class HomeController {
//	@RequestMapping(value = "/")
//	public String index() {
//		System.out.println("swagger-ui.html");
//		return "redirect:swagger-ui.html";
//	}
//}