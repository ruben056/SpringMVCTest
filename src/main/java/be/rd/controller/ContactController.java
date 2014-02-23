package be.rd.controller;

import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import be.rd.beans.Contact;
import be.rd.service.IContactService;
import be.rd.web.form.Message;
import be.rd.web.util.UrlUtils;

@Controller
@RequestMapping("/contacts")
public class ContactController {

	final Logger log = LoggerFactory.getLogger(ContactController.class);

	@Autowired
	private IContactService contactService;
	@Autowired
	private MessageSource messageSource;

	@RequestMapping(method = RequestMethod.GET)
	public String list(Model uiModel) {
		log.info("listing contacts ...");

		List<Contact> allContacts = contactService.findAll();
		uiModel.addAttribute("contacts", allContacts);

		return "contacts/list";
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public String show(@PathVariable Long id, Model uiModel) {

		log.info("get contact " + id + " ...");

		Contact contact = contactService.findById(id);
		uiModel.addAttribute("contact", contact);

		return "contacts/show";
	}

	// updating contact
	@RequestMapping(value = "/{id}", params = "form", method = RequestMethod.GET)
	public String editForm(@PathVariable Long id, Model uiModel) {

		log.info("retrieving updateform ... ");

		Contact c = contactService.findById(id);
		uiModel.addAttribute("contact", c);

		return "contacts/edit";
	}
	@RequestMapping(value = "/{id}", params = "form", method = RequestMethod.POST)
	public String update(@Valid Contact contact, BindingResult bindingResult,
			Model uiModel, HttpServletRequest httpSR, RedirectAttributes ra,
			Locale locale) {

		log.info("updating contact " + contact.getId() + " ...");

		if (bindingResult.hasErrors()) {
			uiModel.addAttribute(
					"message",
					new Message("error", messageSource.getMessage(
							"contact_save_fail", new Object[] {}, locale)));
			uiModel.addAttribute("contact", contact);
			return "contacts/edit";
		}
		
		uiModel.asMap().clear();
		ra.addFlashAttribute("message", new Message("success",
				messageSource.getMessage("contact_save_success", new Object[]{}, locale)));
		contactService.save(contact);
		return "redirect:/contacts/" + UrlUtils.encodeUrlPathSegment(
				contact.getId().toString(),httpSR);

	}
	
	// creating contact
	@RequestMapping(params = "form", method = RequestMethod.GET)
	public String editForm(Model uiModel) {

		log.info("retrieving updateform ... ");

		uiModel.addAttribute("contact", new Contact());

		return "contacts/edit";
	}
	@RequestMapping(params = "form", method = RequestMethod.POST)
	public String create(@Valid Contact contact, BindingResult bindingResult,
			Model uiModel, HttpServletRequest httpSR, RedirectAttributes ra,
			Locale locale) {

		log.info("insert contact " + contact.getId() + " ...");

		if (bindingResult.hasErrors()) {
			uiModel.addAttribute(
					"message",
					new Message("error", messageSource.getMessage(
							"contact_save_fail", new Object[] {}, locale)));
			uiModel.addAttribute("contact", contact);
			return "contacts/edit";
		}
		
		uiModel.asMap().clear();
		ra.addFlashAttribute("message", new Message("success",
				messageSource.getMessage("contact_save_success", new Object[]{}, locale)));
		contactService.save(contact);
		return "redirect:/contacts/" + UrlUtils.encodeUrlPathSegment(
				contact.getId().toString(),httpSR);

	}
}