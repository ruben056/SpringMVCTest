package be.rd.repo;

import org.springframework.data.repository.CrudRepository;

import be.rd.beans.Contact;

public interface IContactRepository extends CrudRepository<Contact, Long> {

}
