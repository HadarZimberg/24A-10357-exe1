package com.example.a24a_10357_exe1.Logic;

import java.util.Arrays;
import java.util.Random;

public class DataManager {
    private static String[] toastMessages = new String[] {
            "Yum! That chicken was delicious!",
            "Oops! Looks like Bearry found a tasty treat",
            "Oops, sorry! I couldn't resist that delicious chicken!",
            "Uh-oh, my bad! Can't resist chicken, even in a game",
            "Well, there goes the chicken! Bearry's happy",
            "You lost a life? I guess I'm a hungry troublemaker!",
            "Chomp chomp! Wait, was that against the rules?",
            "Chicken snack time for Bearry",
            "Bearry's chicken radar activated! Life deducted"
    };

    public static String[] getToastMessages() {
        return toastMessages;
    }
}
