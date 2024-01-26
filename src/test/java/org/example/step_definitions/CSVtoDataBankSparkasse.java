package org.example.step_definitions;

import com.opencsv.*;
import com.opencsv.exceptions.CsvException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.util.List;

public class CSVtoDataBankSparkasse {


    protected void run() throws IOException, CsvException {


        String filePath = "C:\\Users\\ahalu\\IdeaProjects\\readExcel\\20231127-1200151460-umsatz.CSV";



        FileReader  fileReader = new FileReader(filePath);
        try {
            CSVParser parser = new CSVParserBuilder()
                    .withSeparator(',')
                    .withIgnoreQuotations(true)
                    .build();

            CSVReader csvReader = new CSVReaderBuilder(fileReader)
                    .withSkipLines(0)
                    .withCSVParser(parser)
                    .build();


        } catch (Exception e) {
            System.out.println("Something happened");
            e.printStackTrace();
        }

        CSVParser csvParser = new CSVParserBuilder().withSeparator(';').build(); // custom separator
        try(CSVReader reader = new CSVReaderBuilder(new FileReader(filePath))
                .withCSVParser(csvParser)   // custom CSV parser
                .withSkipLines(1)           // skip the first line, header info
                .build()){
            Class.forName("org.postgresql.Driver");
            String password = "jfskdfj&h39";
            String user = "somePostgresUser";
            String url = "jdbc:postgresql://192.168.0.115:5432/budgetBook";
            Connection db;
            try {
                db = DriverManager.getConnection(url, user, password);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            Statement st = db.createStatement();


            List<String[]> r = reader.readAll();

            int listIndex = 0;
            float zwischenBetrag = 0;
            System.out.println("Start of operation");

            StringBuilder completeInsertString = new StringBuilder();
            for (String[] array : r) {

                int index = 0;
                String betragString = array[14].replace(",",".");
                float betrag = Float.parseFloat(betragString);

                StringBuilder lineInsertString = new StringBuilder("(");
                for (String singleData : array) {
                    if (index==1 || index==2){
                        String  dayDate = singleData.substring(0,2);
                        String  monthDate = singleData.substring(3,5);
                        String  yearDate = singleData.substring(6,10);
                        lineInsertString.append("DATE '").append(yearDate).append("-").append(monthDate).append("-").append(dayDate).append("', ");
                        index++;

                    } else if (index==14) {
                        lineInsertString.append(singleData.replace(",", ".")).append(", ");
                        index++;
                    } else {
                        lineInsertString.append("'").append(singleData).append("', ");
                        index++;
                    }


                }
                lineInsertString = new StringBuilder(lineInsertString.substring(0, lineInsertString.length() - 2) + "),\n");

                completeInsertString.append(lineInsertString);

            }
            completeInsertString = new StringBuilder("INSERT INTO \"sparkasseTable\" (auftragskonto, buchungstag, valutadatum, buchungstext, verwendungszweck, glaeubiger_id, mandatsreferenz, kundenreferenz, sammlerreferenz, lastschrift_ursprungsbetrag, auslagenersatz_ruecklastschrift, beguenstigter_zahlungspflichtiger, kontonummer_iban, bic, betrag, waehrung, infoo) VALUES " + completeInsertString.substring(0, completeInsertString.length() - 2) + ";");


            st.executeUpdate(completeInsertString.toString());
            System.out.println("End of Operation");

        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
