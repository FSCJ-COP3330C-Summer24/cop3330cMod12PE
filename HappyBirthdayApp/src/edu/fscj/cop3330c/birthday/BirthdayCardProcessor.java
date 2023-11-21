// BirthdayCardProcessor.java
// D. Singletary
// 3/5/23
// Process birthday cards

package edu.fscj.cop3330c.birthday;

import edu.fscj.cop3330c.message.MessageProcessor;
import edu.fscj.cop3330c.log.Logger;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.ConcurrentLinkedQueue;


public class BirthdayCardProcessor extends Thread
        implements MessageProcessor, Logger<BirthdayCard> {

    private static final String LOGFILE = "cardlog.txt";

    private ConcurrentLinkedQueue<BirthdayCard> safeQueue;
    private boolean stopped = false;

    public BirthdayCardProcessor(ConcurrentLinkedQueue<BirthdayCard> safeQueue) {
        this.safeQueue = safeQueue;

        // start polling (invokes run(), below)
        this.start();
    }

    // remove messages from the queue and process them
    public void processMessages() {
        System.out.println("before processing, queue size is " + safeQueue.size());
        safeQueue.stream().forEach(e -> {
            // Do something with each element
            e = safeQueue.remove();
            System.out.print(e);
            log(e);
        });
        System.out.println("after processing, queue size is now " + safeQueue.size());
    }

    // allow external class to stop us
    public void endProcessing() {
        this.stopped = true;
        interrupt();
    }

    @Override
    public void log(BirthdayCard c) {
        LocalDateTime local =  LocalDateTime.from(
                Instant.now().atZone(ZoneId.systemDefault()));
        String msg = local.truncatedTo(ChronoUnit.MILLIS) +
                ":greeting:" + c.getUser().getName();
        try (BufferedWriter cardlog = Files.newBufferedWriter(Path.of(LOGFILE),
                StandardOpenOption.CREATE, StandardOpenOption.APPEND);) {
            cardlog.write(msg);
            cardlog.newLine();
        } catch (IOException e) {
            System.err.println("LOG FAIL" + msg);
            e.printStackTrace();
        }
    }

    // poll queue for cards
    public void run() {
        final int SLEEP_TIME = 1000; // ms
        while (true) {
            try {
                processMessages();
                Thread.sleep(SLEEP_TIME);
                System.out.println("polling");
            } catch (InterruptedException ie) {
                // see if we should exit
                if (this.stopped == true) {
                    System.out.println("poll thread received exit signal");
                    break;
                }
            }
        }
    }
}
