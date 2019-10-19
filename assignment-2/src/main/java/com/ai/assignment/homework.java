package com.ai.assignment;

import java.util.Date;


/**
 * @author deepakjha on 10/13/19
 * @project ai-assignments
 */
public class homework extends Controller {

    public static void main(String[] args) {
        final long startTime = new Date().getTime();
        new homework().play();
        final long endTime = new Date().getTime();
        System.out.println(endTime - startTime);
    }
}
