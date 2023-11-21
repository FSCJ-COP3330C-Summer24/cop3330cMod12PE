// BirthdayCard.java
// D. Singletary
// 7/2/23
// Birthday cards

package edu.fscj.cop3330c.birthday;

import edu.fscj.cop3330c.image.*;

import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Arrays;
import java.util.ResourceBundle;

public class BirthdayCard implements BirthdayCardBuilder {
    protected User user;
    protected String message;
    private Image cardImage;

    public BirthdayCard() { }

    public BirthdayCard(User user) {
        this.user = user;
        this.message = "Happy Birthday, " + user.getName() + "!";
        this.buildCard(user, message);
    }

    public BirthdayCard(User user, Image cardImage) {
        this.user = user;
        this.cardImage = cardImage;
    }

    public User getUser() {
        return user;
    }

    public String getMessage() {
        return message;
    }

    // given a String containing a (possibly) multi-line message,
    // split the lines, find the longest line, and return its length
    public int getLongest(String s) {
        final String NEWLINE = "\n";
        int maxLength = 0;
        String[] splitStr = s.split(NEWLINE);
        for (String line : splitStr)
            if (line.length() > maxLength)
                maxLength = line.length();
        return maxLength;
    }

    public Image getCardImage() {
        return this.cardImage;
    }

    public void setCardImage(Image cardImage) {
        this.cardImage = cardImage;
    }

    public String buildBorder(String msg) {
        final String NEWLINE = "\n";
        String newMsg = "";

        // get the widest line and number of lines in the message
        int longest = getLongest(msg);

        // need to center lines
        // dashes on top (header) and bottom (footer)
        // vertical bars on the sides
        // |-----------------------|
        // | longest line in group |
        // |      other lines      |
        // |-----------------------|
        //
        // pad with an extra space if the length is odd

        int numDashes = (longest + 2) + (longest % 2);  // pad if odd length
        char[] dashes = new char[numDashes];  // header and footer
        char[] spaces = new char[numDashes];  // body lines
        Arrays.fill(dashes, '-');
        Arrays.fill(spaces, ' ');
        String headerFooter = "|" + new String(dashes) + "|\n";
        String spacesStr = "|" + new String(spaces) + "|\n";

        // start the card with the header
        newMsg = headerFooter;

        // split the message into separate strings
        String[] splitStr = msg.split(NEWLINE);
        for (String s : splitStr) {
            String line = spacesStr;  // start with all spaces

            // create a StringBuilder with all spaces,
            // then replace some spaces with the centered string
            StringBuilder buildLine = new StringBuilder(spacesStr);

            // start at  middle minus half the length of the string (0-based)
            int start = (spacesStr.length() / 2) - (s.length() / 2);
            // end at the starting index plus the length of the string
            int end = start + s.length();
            /// replace the spaces and create a String, then append
            buildLine.replace(start, end, s);
            line = new String(buildLine);
            newMsg += line;
        }
        // append the footer
        newMsg += headerFooter;
        return newMsg;
    }

    public void buildCard(User u, String message) {
        String msg = "";
        if (message == null)
            msg = "Happy Birthday, " + u.getName() + "!";
        else
            msg = message;
        msg = buildBorder(msg);
        this.message = msg;
    }

    @Override
    public String toString() {
        String s = (this.cardImage != null) ?
                (this.cardImage + "\n") :
                getMessage();
        return s;
    }
}

class BirthdayCard_Localized extends BirthdayCard {
    private BirthdayCard birthdayCard;

    public BirthdayCard_Localized(BirthdayCard birthdayCard) {
        this.birthdayCard = birthdayCard;
        // non-localized card will have bordered message, reset it here
        birthdayCard.message = "Happy Birthday, " +
                birthdayCard.user.getName() + "!";
        this.buildCard(birthdayCard.getUser(), birthdayCard.getMessage());
    }

    // overrides
    @Override
    public User getUser() {
        return birthdayCard.getUser();
    }
    @Override
    public String getMessage() {
        return birthdayCard.getMessage();
    }
    @Override
    public String toString() {
        String s = birthdayCard.getMessage();
        return s;
    }
    @Override
    public void buildCard(User u, String message) {
        String msg = null;
        try {
            // load the property and create the localized greeting
            ResourceBundle res = ResourceBundle.getBundle(
                    "edu.fscj.cop3330c.birthday.Birthday",
                    ((UserWithLocale)getUser()).getLocale());
            String happyBirthday = res.getString("HappyBirthday");
            message = message.replace("Happy Birthday", happyBirthday);

            // format and display the date
            DateTimeFormatter formatter =
                    DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM);
            formatter =
                    formatter.localizedBy(((UserWithLocale)getUser()).getLocale());
            String dateStr = getUser().getBirthday().format(formatter) + "\n";

            // add the localized greeting
            msg = dateStr + message;
        } catch (java.util.MissingResourceException e) {
            System.err.println(e);
            msg = "Happy Birthday, " + getUser().getName() + "!";
        }
        msg = buildBorder(msg);
        birthdayCard.message = msg;
    }
}