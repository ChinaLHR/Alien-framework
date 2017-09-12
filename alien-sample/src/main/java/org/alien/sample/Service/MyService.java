package org.alien.sample.Service;

import org.alien.framework.ioc.annotation.Service;
import org.alien.sample.entity.Person;

@Service
public class MyService {

  public Person getPerson(){
      Person person = new Person();
      person.setName("lhr");
      person.setAddress("广东潮州");
      person.setPhone("6666777778888");
     return person;
  }

}
