// Dispatcher.java
// D. Singletary
// 2/15/23
// A generic interface for dispatching messages.

package edu.fscj.cop3330c.dispatch;

public interface Dispatcher<T> {
    public void dispatch(T t);
}
