package frontsnapk1ck.utility.event.annotation;

/**
 * For any method that has this tag, it is strongly recommend 
 * that you use a job in the event queue to execute it
 */
public @interface RequiredJob {
    
}
