package be.rd.controller;

import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import be.rd.web.form.Message;

@Controller
@RequestMapping("/security")
public class SecurityController
{
	final Logger log = LoggerFactory.getLogger(ContactController.class);
	
	@Autowired
	private MessageSource messageSource;
	
	@RequestMapping("/loginfail")
	public String loginFail(Model uiModel, Locale locale) {
		log.info("Login failed detected");
		uiModel.addAttribute(
				"message",
				new Message("error", messageSource.getMessage(
						"message_login_fail", new Object[] {}, locale)));
		return "contacts/list";
	}
}