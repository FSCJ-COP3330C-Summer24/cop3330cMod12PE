// User.java
// D. Singletary
// 10/24/23
// user classes

package edu.fscj.cop3330c.birthday;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Locale;

// user class
public class User implements Serializable {
    private static Integer idStatic = 0;
    private Integer id; // primary key
    private String fName;
    private String lName;
    private String email;
    private ZonedDateTime birthday;

    public User() { }

    public User(Integer id, String fName, String lName, String email,
                ZonedDateTime birthday) {
        if (id == 0) {
            idStatic++;
            this.id = idStatic;
        } else {
            this.id = id;
        }
        this.fName = fName;
        this.lName = lName;
        this.email = email;
        this.birthday = birthday;
    }

    public Integer getId() { return id; }
    public String getFName() { return fName; }
    public String getLName() { return lName; }

    public String getName() {
        return fName + " " + lName;
    }

    public String getEmail() { return email; }

    public ZonedDateTime getBirthday() {
        return birthday;
    }

    @Override
    public String toString() {
        return this.getName() + "," + this.birthday;
    }
}

class UserWithLocale extends User {
    private User user;
    private Locale locale;

    // overload to allow previously instantiated user
    public UserWithLocale(User user, Locale locale) {
        this.user = user;
        this.locale = locale;
    }

    // overload to allow previously instantiated user
    public UserWithLocale(Integer id, String fName, String lName, String email,
                          ZonedDateTime birthday, Locale locale) {
        this.user = new User(id, fName, lName, email, birthday);
        this.locale = locale;
    }

    public Locale getLocale() { return locale; }

    // overrides
    @Override
    public Integer getId() {
        return user.getId();
    }
    @Override
    public String getFName() {
        return user.getFName();
    }
    @Override
    public String getLName() {
        return user.getLName();
    }
    @Override
    public String getName() {
        return user.getName();
    }
    @Override
    public String getEmail() { return user.getEmail(); }
    @Override
    public ZonedDateTime getBirthday() { return user.getBirthday(); }
    @Override
    public String toString() {
        return this.user + "," + this.locale;
    }
}


