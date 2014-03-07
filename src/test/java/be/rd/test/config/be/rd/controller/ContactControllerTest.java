package be.rd.test.config.be.rd.controller;

import be.rd.beans.Contact;
import be.rd.controller.ContactController;
import be.rd.service.IContactService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.context.MessageSource;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributesModelMap;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by ruben on 3/4/14.
 */
public class ContactControllerTest extends AbstractControllerTest {

    private MessageSource messageSource;
    private final List<Contact> contacts = new ArrayList<Contact>();
    private Contact newContact;

    private IContactService contactService;

    @Before
    public void setup()
    {
        initFindContactsResult();
        initCreateContactResult();
        mockContactService();


    }

    private void initFindContactsResult()
    {
        // Initialize list of contacts for mocked ContactService
        Contact contact = new Contact();
        contact.setId(1l);
        contact.setFirstName("Clarence");
        contact.setLastName("Ho");
        contacts.add(contact);

        contact = new Contact();
        contact.setId(2l);
        contact.setFirstName("Jackie");
        contact.setLastName("Chan");
        contacts.add(contact);
    }

    private void initCreateContactResult()
    {
        newContact = new Contact();
        newContact.setId(1l);
        newContact.setFirstName("Clarence");
        newContact.setLastName("Ho");
    }

    private void mockContactService()
    {
        contactService = Mockito.mock(IContactService.class);
        Mockito.when(contactService.findAll()).thenReturn(contacts);

        Mockito.when(contactService.save(newContact)).thenAnswer(new Answer<Object>() {
            public Contact answer(InvocationOnMock invocation) throws Throwable {
                contacts.add(newContact);
                return newContact;
            }
        });
        messageSource = Mockito.mock(MessageSource.class);
        Mockito.when(messageSource.getMessage("contact_save_success", new Object[]{},
                Locale.ENGLISH)).thenReturn("success");
    }

    @Test
    public void testList()
    {
        // set the mocked contactservice
        ContactController ctrl = new ContactController();
        ReflectionTestUtils.setField(ctrl, "contactService", contactService);

        ExtendedModelMap uiModel = new ExtendedModelMap();
        String result = ctrl.list(uiModel);

        org.junit.Assert.assertNotNull(result);
        org.junit.Assert.assertEquals(result, "contacts/list");
        List<Contact> modelContacts = (List<Contact>) uiModel.get("contacts");
        org.junit.Assert.assertEquals(2, modelContacts.size());

    }

    @Test
    public void testCreate()
    {
        ContactController contactController = new ContactController();
        ReflectionTestUtils.setField(contactController, "contactService",
                contactService);
        ReflectionTestUtils.setField(contactController, "messageSource",
                messageSource);
        BindingResult bindingResult = new BeanPropertyBindingResult(
                newContact, "contact");
        ExtendedModelMap uiModel = new ExtendedModelMap();
        HttpServletRequest httpServletRequest = new MockHttpServletRequest();
        RedirectAttributes redirectAttributes =
                new RedirectAttributesModelMap();
        Locale locale = Locale.ENGLISH;
        String result = contactController.create(newContact, bindingResult,
                uiModel, httpServletRequest, redirectAttributes, locale, null);
        org.junit.Assert.assertNotNull(result);
        org.junit.Assert.assertEquals("redirect:/contacts/1", result);
        org.junit.Assert.assertEquals(3, contacts.size());
    }


}
