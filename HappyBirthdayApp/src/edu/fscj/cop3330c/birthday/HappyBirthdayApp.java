// HappyBirthdayApp.java
// D. Singletary
// 1/29/23
// wish multiple users a happy birthday

// D. Singletary
// 3/7/23
// Changed to thread-safe queue
// Instantiate the BirthdayCardProcessor object
// added test data for multi-threading tests

// D. Singletary
// 11/14/23
// Added user serialization
// Added logging feature

package edu.fscj.cop3330c.birthday;

import edu.fscj.cop3330c.dispatch.Dispatcher;

import java.io.*;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Locale;
import java.util.concurrent.ConcurrentLinkedQueue;

// main application class
public class HappyBirthdayApp implements BirthdayCardSender {

    private static final String USER_FILE = "user.dat";

    private ArrayList<UserWithLocale> birthdays = new ArrayList<>();
    // Use a thread-safe Queue<LinkedList> to act as message queue for the dispatcher
    private ConcurrentLinkedQueue<BirthdayCard> safeQueue = new ConcurrentLinkedQueue<>(
            new LinkedList<>()
    );


    //private Stream<BirthdayCard> stream = safeQueue.stream();

    private static HappyBirthdayApp hba = new HappyBirthdayApp();

    private HappyBirthdayApp() { }

    public static HappyBirthdayApp getApp() {
        return hba;
    }

    // send the card
    public void sendCard(BirthdayCard bc) {
        Dispatcher<BirthdayCard> d = (c)-> {
            this.safeQueue.add(c);
        };
        d.dispatch(bc);
    }

    // compare current month and day to user's data
    // to see if it is their birthday
    public boolean isBirthday(User u) {
        boolean result = false;

        LocalDate today = LocalDate.now();

        if (today.getMonth() == u.getBirthday().getMonth() &&
                today.getDayOfMonth() == u.getBirthday().getDayOfMonth())
            result = true;

        return result;
    }

    // add multiple birthdays
    public void addBirthdays(ArrayList<UserWithLocale> users) {
        birthdays.addAll(users);
    }

    public void processCards() {
        // show the birthdays
        if (!hba.birthdays.isEmpty()) {
            for (User u : hba.birthdays) {
                // see if today is their birthday
                if (hba.isBirthday(u)) {
                    BirthdayCard card;
                    // decorate and send a legacy card
                    if (u instanceof UserWithLocale)
                        card = new BirthdayCard_Localized(new BirthdayCard(u));
                    else
                        card = new BirthdayCard(u);

                    hba.sendCard(card);
                }
            }
        }
    }
    
    // write user ArrayList to save file
    public void writeUsers(ArrayList<UserWithLocale> ul) {
        try (ObjectOutputStream userData =  new ObjectOutputStream(
                new FileOutputStream(USER_FILE))) {
                userData.writeObject(ul);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // read saved user ArrayList
    public ArrayList<UserWithLocale> readUsers() {
        ArrayList<UserWithLocale> list = new ArrayList();

        try (ObjectInputStream userData =
                     new ObjectInputStream(
                             new FileInputStream(USER_FILE))) {
            list = (ArrayList<UserWithLocale>) (userData.readObject());
            for (UserWithLocale u : list)
                System.out.println("readUsers: read " + u);
        } catch (FileNotFoundException e) {
            // not  a problem if nothing was saved
            System.err.println("readUsers: no input file");
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return list;    
    }

    // main program
    public static void main(String[] args) {

        UserDB.createDB();
        ArrayList<UserWithLocale> userList = UserDB.readUserDB();

        // start the processor thread
        BirthdayCardProcessor processor = new BirthdayCardProcessor(hba.safeQueue);

        // use current date for testing, adjust where necessary
        ZonedDateTime currentDate = ZonedDateTime.now();

        hba.addBirthdays(userList);
        hba.processCards();

        // wait for a bit
        try {
            Thread.sleep(2000);
        } catch (InterruptedException ie) {
            System.out.println("sleep interrupted! " + ie);
        }

        processor.endProcessing();

        UserDB.deleteDB();
    }
}
