/** Natasha Bornhorst nbornho1 Richard Ding rding2 Karl Tayeb ktayeb1 */
/** cs226 section 2 project 4 */
/** Exception class for empty Queues.
 */
public class QueueEmptyException extends RuntimeException {

    /** Create a default exception object.
     */
    public QueueEmptyException() {
        super("ERROR: queue is empty, invalid operation");
    }

    /** Create a specific exception object.
     *  @param err the error message
     */
    public QueueEmptyException(String err) {
        super(err);
    }
}
