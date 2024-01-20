package org.example.step_definitions;

import com.opencsv.*;
import com.opencsv.exceptions.CsvException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.util.List;

public class CSVtoDataBankSparkasse {


    public static void main(String[] args) throws IOException, CsvException {


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

        /*    List<String[]> r = csvReader.readAll();

            int listIndex = 0;
            for (String[] arrays : r) {
                System.out.println("\narrays: "+arrays.length);
                System.out.println("String[" + listIndex++ + "] : " + Arrays.toString(arrays));

                int index = 1;
                for (String array : arrays) {
                    System.out.println("array: "+array.length());
                    System.out.println(index++ + " : " + array);
                }

            }*/
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
//            r.forEach(x -> System.out.println(Arrays.toString(x)));
            int listIndex = 0;
            float zwischenBetrag = 0;

            /*String someString1 = "(";
            String[] firstRow = r.get(0);
            System.out.println(Arrays.toString(firstRow));
            for (String string2 : firstRow) {
                someString1 += "'"+string2+"', ";
//                    System.out.println("'"+array+"',");
//                    ResultSet rs = st.executeQuery("SELECT * FROM sparkasseTable");
//                    System.out.println(index++ + " : " + array);
            }
            someString1 = someString1.substring(0, someString1.length() - 2);
            System.out.println(someString1 + ");");
            st.executeQuery("INSERT INTO \"sparkasseTable\" (auftragskonto, buchungstag, valutadatum, buchungstext, verwendungszweck, glaeubiger_id, mandatsreferenz, kundenreferenz, sammlerreferenz, lastschrift_ursprungsbetrag, auslagenersatz_ruecklastschrift, beguenstigter_zahlungspflichtiger, kontonummer_iban, bic, betrag, waehrung, infoo) VALUES ('DE91553500101200151460', DATE '2023-11-27', DATE '2023-11-27', 'KARTENZAHLUNG', '2023-11-24T17:45 Debitk.2 2026-12 ', '', '', '54334036190503241123174537', '', '', '', 'EDEKA BISCHOFF//HEIDELBERG/DE', 'DE71200907004231287071', 'EDEKDEHHXXX', -15.81, 'EUR', 'Umsatz gebucht');");
*/
            StringBuilder completeInsertString = new StringBuilder();
            for (String[] array : r) {
//                System.out.println("\nString[" + listIndex++ + "] : " + Arrays.toString(arrays));
                int index = 0;
                String betragString = array[14].replace(",",".");
                float betrag = Float.parseFloat(betragString);
//                System.out.println(arrays[3]);
//                System.out.println(arrays[11]+":");
//                System.out.println(betrag);
//                zwischenBetrag = zwischenBetrag+betrag;
//                System.out.println(zwischenBetrag);
                StringBuilder lineInsertString = new StringBuilder("(");
                for (String singleData : array) {
                    if (index==1 || index==2){
                        String  dayDate = singleData.substring(0,2);
                        String  monthDate = singleData.substring(3,5);
                        String  yearDate = singleData.substring(6,10);
                        lineInsertString.append("DATE '").append(yearDate).append("-").append(monthDate).append("-").append(dayDate).append("', ");
                        index++;
//                        System.out.println("OUTCAME: "+"DATE '"+lineInsertString+"', ");
                    } else if (index==14) {
                        lineInsertString.append(singleData.replace(",", ".")).append(", ");
                        index++;
                    } else {
                        lineInsertString.append("'").append(singleData).append("', ");
                        index++;
                    }

//                    System.out.println("'"+array+"',");
//                    ResultSet rs = st.executeQuery("SELECT * FROM sparkasseTable");
//                    System.out.println(index++ + " : " + singleData);
                }
                lineInsertString = new StringBuilder(lineInsertString.substring(0, lineInsertString.length() - 2) + "),\n");
//                System.out.println(lineInsertString + ")");
                completeInsertString.append(lineInsertString);

            }
            completeInsertString = new StringBuilder("INSERT INTO \"sparkasseTable\" (auftragskonto, buchungstag, valutadatum, buchungstext, verwendungszweck, glaeubiger_id, mandatsreferenz, kundenreferenz, sammlerreferenz, lastschrift_ursprungsbetrag, auslagenersatz_ruecklastschrift, beguenstigter_zahlungspflichtiger, kontonummer_iban, bic, betrag, waehrung, infoo) VALUES " + completeInsertString.substring(0, completeInsertString.length() - 2) + ";");

            System.out.println(completeInsertString);
            st.executeQuery(completeInsertString.toString());

        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
