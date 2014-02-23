package be.rd.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import be.rd.beans.Contact;
import be.rd.service.IContactService;

@Controller
@RequestMapping("/contacts")
public class ContactController {

	final Logger log = LoggerFactory.getLogger(ContactController.class);
	
	@Autowired
	private IContactService contactService;
	
	@RequestMapping(method = RequestMethod.GET)
	public String list(Model uiModel){
		log.info("listing contacts ...");
		
		List<Contact> allContacts = contactService.findAll();
		uiModel.addAttribute("contacts", allContacts);
		
		return "contacts/list";
	}
	
}
