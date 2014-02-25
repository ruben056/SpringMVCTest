package be.rd.controller;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;
import javax.validation.Valid;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import be.rd.beans.Contact;
import be.rd.service.IContactService;
import be.rd.web.form.ContactGrid;
import be.rd.web.form.Message;
import be.rd.web.util.UrlUtils;

import com.google.common.collect.Lists;

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
	@PreAuthorize("isAuthenticated()")
	@RequestMapping(value = "/{id}", params = "form", method = RequestMethod.GET)
	public String editForm(@PathVariable Long id, Model uiModel) {

		log.info("retrieving updateform ... ");

		Contact c = contactService.findById(id);
		uiModel.addAttribute("contact", c);

		return "contacts/edit";
	}

	@PreAuthorize("isAuthenticated()")
	@RequestMapping(value = "/{id}", params = "form", method = RequestMethod.POST)
	public String update(@Valid Contact contact, BindingResult bindingResult,
			Model uiModel, HttpServletRequest httpSR, RedirectAttributes ra,
			Locale locale,
			@RequestParam(value="file", required=false) Part file) {

		log.info("updating contact " + contact.getId() + " ...");

		if (bindingResult.hasErrors()) {
			uiModel.addAttribute(
					"message",
					new Message("error", messageSource.getMessage(
							"contact_save_fail", new Object[] {}, locale)));
			uiModel.addAttribute("contact", contact);
			return "contacts/edit";
		}

		// Process upload file
		if (file != null) {
			
			if(file.getSize() == 0)
			{
				// do not delete the picture if not changed...
				contact.setPhoto(contactService.findById(contact.getId()).getPhoto());
			}
			else
			{
				log.info("File name: " + file.getName());
				log.info("File size: " + file.getSize());
				log.info("File content type: " + file.getContentType());
				byte[] fileContent = null;
				try {
					InputStream inputStream = file.getInputStream();
					if (inputStream == null)
						log.info("File inputstream is null");
					fileContent = IOUtils.toByteArray(inputStream);
					contact.setPhoto(fileContent);
				} catch (IOException ex) {
					log.error("Error saving uploaded file");
				}
				contact.setPhoto(fileContent);				
			}			
		}

		uiModel.asMap().clear();
		ra.addFlashAttribute(
				"message",
				new Message("success", messageSource.getMessage(
						"contact_save_success", new Object[] {}, locale)));
		contactService.save(contact);
		return "redirect:/contacts/"
				+ UrlUtils.encodeUrlPathSegment(contact.getId().toString(),
						httpSR);

	}

	// creating contact
	@PreAuthorize("isAuthenticated()")
	@RequestMapping(params = "form", method = RequestMethod.GET)
	public String editForm(Model uiModel) {

		log.info("retrieving updateform ... ");

		uiModel.addAttribute("contact", new Contact());

		return "contacts/edit";
	}

	@PreAuthorize("isAuthenticated()")
	@RequestMapping(params = "form", method = RequestMethod.POST)
	public String create(@Valid Contact contact, BindingResult bindingResult,
			Model uiModel, HttpServletRequest httpSR, RedirectAttributes ra,
			Locale locale,
			@RequestParam(value="file", required=false) Part file) {

		log.info("insert contact " + contact.getId() + " ...");

		// Process upload file
		if (file != null) {
			log.info("File name: " + file.getName());
			log.info("File size: " + file.getSize());
			log.info("File content type: " + file.getContentType());
			byte[] fileContent = null;
			try {
				InputStream inputStream = file.getInputStream();
				if (inputStream == null)
					log.info("File inputstream is null");
				fileContent = IOUtils.toByteArray(inputStream);
				contact.setPhoto(fileContent);
			} catch (IOException ex) {
				log.error("Error saving uploaded file");
			}
			contact.setPhoto(fileContent);
		}

		if (bindingResult.hasErrors()) {
			uiModel.addAttribute(
					"message",
					new Message("error", messageSource.getMessage(
							"contact_save_fail", new Object[] {}, locale)));
			uiModel.addAttribute("contact", contact);
			return "contacts/edit";
		}

		uiModel.asMap().clear();
		ra.addFlashAttribute(
				"message",
				new Message("success", messageSource.getMessage(
						"contact_save_success", new Object[] {}, locale)));
		contactService.save(contact);
		return "redirect:/contacts/"
				+ UrlUtils.encodeUrlPathSegment(contact.getId().toString(),
						httpSR);

	}

	/**
	 * retrieve paginated grid
	 */
	@RequestMapping(value = "/listgrid", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public ContactGrid listGrid(
			@RequestParam(value = "page", required = false) Integer page,
			@RequestParam(value = "rows", required = false) Integer rows,
			@RequestParam(value = "sidx", required = false) String sortBy,
			@RequestParam(value = "sord", required = false) String order) {
		log.info("Listing contacts for grid with page: {}, rows: {}", page,
				rows);
		log.info("Listing contacts for grid with sort: {}, order: {}",
				sortBy, order);
		// Process order by
		Sort sort = null;
		String orderBy = sortBy;
		if (orderBy != null && orderBy.equals("birthDateString"))
			orderBy = "birthDate";
		if (orderBy != null && order != null) {
			if (order.equals("desc")) {
				sort = new Sort(Sort.Direction.DESC, orderBy);

			} else
				sort = new Sort(Sort.Direction.ASC, orderBy);
		}
		// Constructs page request for current page
		// Note: page number for Spring Data JPA starts with 0, while jqGrid
		// starts with 1
		PageRequest pageRequest = null;
		if (sort != null) {
			pageRequest = new PageRequest(page - 1, rows, sort);
		} else {
			pageRequest = new PageRequest(page - 1, rows);
		}
		Page<Contact> contactPage = contactService.findAllByPage(pageRequest);
		// Construct the grid data that will return as JSON data
		ContactGrid contactGrid = new ContactGrid();
		contactGrid.setCurrentPage(contactPage.getNumber() + 1);
		contactGrid.setTotalPages(contactPage.getTotalPages());
		contactGrid.setTotalRecords(contactPage.getTotalElements());
		contactGrid.setContactData(Lists.newArrayList(contactPage.iterator()));
		return contactGrid;
	}

	@RequestMapping(value = "/photo/{id}", method = RequestMethod.GET)
	@ResponseBody
	public byte[] downloadPhoto(@PathVariable Long id) {

		Contact contact = contactService.findById(id);

		if (contact.getPhoto() != null) {
			log.info("Downloading photo for id: {} with size: {}",
					contact.getId(), contact.getPhoto().length);
		}

		return contact.getPhoto();
	}


}