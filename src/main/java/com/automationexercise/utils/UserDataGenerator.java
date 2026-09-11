package com.automationexercise.utils;

import com.automationexercise.models.ContactUsMessage;
import com.automationexercise.models.User;
import net.datafaker.Faker;

public class UserDataGenerator {
    private static final Faker faker = new Faker();

    public static ContactUsMessage generateRandomContactUsMessage() {
        String name = faker.name().fullName();
        String email = faker.internet().username() + System.currentTimeMillis() + "@" + faker.internet().domainName();
        String subject = faker.lorem().sentence(3);
        String message = faker.lorem().paragraph(2);

        return ContactUsMessage.builder()
                .name(name)
                .email(email)
                .subject(subject)
                .message(message)
                .build();
    }

    public static User generateRandomUser() {
        String firstName = faker.name().firstName();
        String lastName = faker.name().lastName();
        String username = firstName + " " + lastName;
        String email = faker.internet().emailAddress();
        String password = faker.internet().password(8, 12, true, true, true);
        
        // Randomly pick title
        String title = faker.bool().bool() ? "Mr" : "Mrs";
        
        // Random Date of Birth
        String birthDay = String.valueOf(faker.number().numberBetween(1, 28));
        
        String[] months = {
            "January", "February", "March", "April", "May", "June",
            "July", "August", "September", "October", "November", "December"
        };
        String birthMonth = months[faker.number().numberBetween(0, 11)];
        String birthYear = String.valueOf(faker.number().numberBetween(1970, 2010));
        
        boolean newsletter = faker.bool().bool();
        boolean optionCheckbox = faker.bool().bool();
        
        String company = faker.company().name();
        String address1 = faker.address().streetAddress();
        String address2 = faker.address().secondaryAddress();
        
        // Countries in DOM: India, United States, Canada, Australia, Israel, New Zealand, Singapore
        String[] countries = {"India", "United States", "Canada", "Australia", "New Zealand", "Singapore"};
        String country = countries[faker.number().numberBetween(0, countries.length - 1)];
        
        String state = faker.address().state();
        String city = faker.address().city();
        String zipcode = faker.address().zipCode();
        String mobileNumber = faker.phoneNumber().cellPhone();

        return User.builder()
                .username(username)
                .email(email)
                .password(password)
                .title(title)
                .birthDay(birthDay)
                .birthMonth(birthMonth)
                .birthYear(birthYear)
                .newsletter(newsletter)
                .optin(optionCheckbox)
                .firstName(firstName)
                .lastName(lastName)
                .company(company)
                .address1(address1)
                .address2(address2)
                .country(country)
                .state(state)
                .city(city)
                .zipcode(zipcode)
                .mobileNumber(mobileNumber)
                .build();
    }
}
