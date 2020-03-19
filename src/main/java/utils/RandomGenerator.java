package utils;

import java.sql.Date;
import java.time.LocalDate;
import java.util.Random;

public class RandomGenerator {
    static private Random random = new Random();

    public static String getRandomStatus(){
        String[] statuses = {"nevybavená", "vybavená", "zamietnutá"};
        int randomIndex = random.nextInt(statuses.length);
        return statuses[randomIndex];
    }

    public static int getRandomIntFromInterval(int left, int right) {
        return left + (int) Math.round(Math.random() * (right - left));
    }

    public static Date getRandomDate(int startYear, int endYear){
        int day = getRandomIntFromInterval(1, 28);
        int month = getRandomIntFromInterval(1, 12);
        int year = getRandomIntFromInterval(startYear, endYear);
        return Date.valueOf(LocalDate.of(year, month, day));
    }

    public static Double getRandomPrice(){
        int number = random.nextInt(9999); //Generates random number
        return number / 100.0;
    }
}
