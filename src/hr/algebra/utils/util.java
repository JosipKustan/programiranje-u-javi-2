/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.utils;

import java.util.Random;

/**
 *
 * @author jkustan
 */
public class util {
    public static boolean getRandomBoolean() {
    Random random = new Random();
    return random.nextBoolean();
}
}
