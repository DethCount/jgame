package count.jgame.controllers;


import javax.ws.rs.core.MediaType;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@Controller("index")
public class IndexController {
	
	class IndexModel {
		public final String title;
		
		IndexModel(String title) {
			this.title = title;
		}
		
		ModelAndView toModelAndView() {
			return new ModelAndView("index")
				.addObject("title", this.title);
		}
	}
	
	@GetMapping(path = "/", produces= {MediaType.TEXT_HTML})
	@ResponseBody
	public ModelAndView indexHtml() {
		return this.index().toModelAndView();
	}
	
	@GetMapping(path = "/", produces= {MediaType.APPLICATION_JSON})
	@ResponseBody
	public IndexModel index() {
		return new IndexModel("coucou !");
	}
}
