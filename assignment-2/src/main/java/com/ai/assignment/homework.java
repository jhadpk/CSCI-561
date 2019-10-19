package com.ai.assignment;

/**
 * @author deepakjha on 10/13/19
 * @project ai-assignments
 */
public class homework extends Controller {

    public static long START_TIME = System.currentTimeMillis();

    public static void main(String[] args) {
        new homework().play();
        final long endTime = System.currentTimeMillis();
        System.out.println(endTime - START_TIME);
    }
}
