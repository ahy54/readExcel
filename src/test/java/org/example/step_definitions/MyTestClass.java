package org.example.step_definitions;

import com.opencsv.exceptions.CsvException;
import org.junit.Test;

import java.io.IOException;

public class MyTestClass extends CSVtoDataBankSparkasse {

    @Test public void test1() {
        try {
            run();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (CsvException e) {
            throw new RuntimeException(e);
        }
    }


}
