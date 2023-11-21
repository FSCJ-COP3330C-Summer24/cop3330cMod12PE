// BirthdayGreeter.java
// D. Singletary
// 1/29/23
// interface for sending birthday greetings

package edu.fscj.cop3330c.birthday;

public interface BirthdayGreeter {
    // build a birthday card
    public String buildCard(String msg);
    // send a birthday card
    public void sendCard(User u);
}
