package eu.codearte.fairyland.producer.person.pl;

import eu.codearte.fairyland.producer.person.Sex;

import java.util.Calendar;
import java.util.GregorianCalendar;

import static java.lang.String.format;

public class Pesel {

    public static final int[] WEIGHTS = new int[]{1, 3, 7, 9, 1, 3, 7, 9, 1, 3};

    public String pesel(GregorianCalendar calendar, Sex sex) {

        int year = calculateYear(calendar.get(Calendar.YEAR));
        int month = calculateMonth(calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.YEAR));
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int serialNumber = (int) (Math.random() * 1000);
        int sexCode = calculateSexCode(sex);

        String pesel = format("%02d%02d%02d%03d%d", year, month, day, serialNumber, sexCode);

        return pesel + calculateChecksum(pesel);
    }

    /**
     *
     * @param pesel
     * @return
     */
    public static boolean isValidPesel(String pesel) {
        int size = pesel.length();
        if (size != 11) {
            return false;
        }
        int checksum = Integer.valueOf(pesel.substring(size - 1));
        int checkDigit = calculateChecksum(pesel);

        return checkDigit == checksum;

    }

    private int calculateSexCode(Sex sex) {
        return randomDigitFrom0To4() * 2 + (isMale(sex) ? 1 : 0);
    }

    private boolean isMale(Sex sex) {
        return sex == Sex.male;
    }

    private int randomDigitFrom0To4() {
        return ((int) (Math.random() * 5));
    }

    private int calculateYear(int year) {
        return year % 100;
    }

    private int calculateMonth(int month, int year) {
        if (year >= 1800 && year < 1900) {
            month += 80;
        } else if (year >= 2000 && year < 2100) {
            month += 20;
        } else if (year > 2100 && year < 2200) {
            month += 40;
        } else if (year > 2200 && year < 2300) {
            month += 60;
        }
        return month;
    }

    private static int calculateChecksum(String pesel) {
        int j = 0, sum = 0, control = 0;
        for (int i = 0; i < 10; i++) {
            char c = pesel.charAt(i);
            j = Integer.valueOf(String.valueOf(c));
            sum += j * WEIGHTS[i];
        }
        control = 10 - (sum % 10);
        return control % 10;
    }
}