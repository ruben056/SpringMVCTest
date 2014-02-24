package be.rd.repo;

import org.springframework.data.repository.PagingAndSortingRepository;

import be.rd.beans.Contact;

public interface IContactRepository extends PagingAndSortingRepository<Contact, Long> {

}
