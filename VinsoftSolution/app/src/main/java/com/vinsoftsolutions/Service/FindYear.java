package com.vinsoftsolutions.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FindYear {
    public static String acYear(String mdate) {
        try {
            // Define the input date format
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
            Date date = dateFormat.parse(mdate);

            // Extract the month and year
            int month = Integer.parseInt(new SimpleDateFormat("MM").format(date));
            int year = Integer.parseInt(new SimpleDateFormat("yyyy").format(date));

            String yyyy;
            if (month < 4) {
                // If the month is before April
                int yyyy1 = year - 1;
                String yyyy2 = String.valueOf(year).substring(2);
                yyyy = yyyy1 + yyyy2;
            } else {
                // If the month is April or later
                int yyyy1 = year;
                String yyyy2 = String.valueOf(year + 1).substring(2);
                yyyy = yyyy1 + yyyy2;
            }
            return yyyy;
        } catch (ParseException e) {
            throw new RuntimeException("Invalid date format: " + mdate);
        }
    }

}
