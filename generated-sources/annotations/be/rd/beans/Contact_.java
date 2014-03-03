package be.rd.beans;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import org.joda.time.DateTime;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Contact.class)
public abstract class Contact_ {

	public static volatile SingularAttribute<Contact, Long> id;
	public static volatile SingularAttribute<Contact, String> lastName;
	public static volatile SingularAttribute<Contact, String> description;
	public static volatile SingularAttribute<Contact, DateTime> birthDate;
	public static volatile SingularAttribute<Contact, String> firstName;
	public static volatile SingularAttribute<Contact, byte[]> photo;
	public static volatile SingularAttribute<Contact, Integer> version;

}

