package com.laioffer.onlineorder.hello;


import net.datafaker.Faker;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class HelloController {

    @GetMapping("/hello")
    public Person sayHello(@RequestParam(value = "name", required = false) String name) {
//        @RequestParam 一定要记得把 value 加上去
        if (name == null) {
            name = "Guest";
        }
        Faker faker = new Faker();
        String company = faker.company().name();
        String street = faker.address().streetAddress();
        String city = faker.address().city();
        String state = faker.address().state();
        String bookTitle = faker.book().title();
        String bookAuthor = faker.book().author();
        return new Person(name, company, new Address(street, city, state, null), new Book(bookTitle, bookAuthor));
    }
}
