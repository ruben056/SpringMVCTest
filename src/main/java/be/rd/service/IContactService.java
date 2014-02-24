package be.rd.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import be.rd.beans.Contact;

public interface IContactService {

	public List<Contact> findAll();
	public Contact findById(Long id);
	public Contact save(Contact contact);
	public Page<Contact> findAllByPage(Pageable pageable);

}
