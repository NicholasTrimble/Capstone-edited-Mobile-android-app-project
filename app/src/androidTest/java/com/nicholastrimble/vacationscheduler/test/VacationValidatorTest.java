package com.nicholastrimble.vacationscheduler.test;

import org.junit.Test;
import static org.junit.Assert.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class VacationValidatorTest {

    @Test
    public void testEndDateBeforeStartDate() throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yy", Locale.US);
        Date start = sdf.parse("08/01/25");
        Date end = sdf.parse("07/01/25");

        assertTrue("End date is before start date", end.before(start));
    }

    @Test
    public void testValidInput_noInvalidCharacters() {
        String input = "Hawaii Trip";
        assertFalse(input.matches(".*[;'\"><].*"));
    }

    @Test
    public void testInvalidInput_withSpecialCharacters() {
        String input = "DROP TABLE;";
        assertTrue(input.matches(".*[;'\"><].*"));
    }
}

