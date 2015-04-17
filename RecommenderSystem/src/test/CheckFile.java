package test;

import java.io.*;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.TreeMap;

/**
 * Created by didii on 4/3/15.
 */
public class CheckFile {
    private static BufferedReader dataReader;
    private static BufferedReader expectedDataReader;

    private static void fillExpectedData(HashMap<String, Integer> expectedData) throws IOException {
        String line;
        while ((line = expectedDataReader.readLine()) != null) {
            if(line.contains("???")) {
                expectedData.put(line.split(" ")[0], 1);
            } else {
                if(line.contains("??")) {
                    expectedData.put(line.split(" ")[0], 2);
                } else {
                    if(line.contains("?")) {
                        expectedData.put(line.split(" ")[0], 3);
                    } else {
                        expectedData.put(line.split(" ")[0], 4);
                    }
                }
            }
        }
    }

    private static void readAndCheck(HashMap<String, Integer> expectedData) throws IOException {


        Comparator<Double> reverseDoubleComparator = new Comparator<Double>() {
            @Override public int compare( Double s1, Double s2) {
                return (int)((s2 - s1) * 1000000.0d);
            }
        };
        TreeMap <Double, LinkedList<String>> bestResults =
                new TreeMap<Double, LinkedList<String>>(reverseDoubleComparator);

        // skip title
        while(dataReader.readLine() != null) {

            String title = "";

            for(int i = 0; i < 10; i++) {
                title += dataReader.readLine() + "\n";
            }

            String line;
            Double maxGrade = 1.0d;
            Double totalGrade = 0.0d;

            int linesNum = 100;
            while((line = dataReader.readLine()) != null && !line.isEmpty()) {
                linesNum--;

                String[] splitValues = line.split(" ");

                if(expectedData.containsKey(splitValues[0])) {
                    double importance = (double) expectedData.get(splitValues[0]);

                    Double grade = Double.parseDouble(splitValues[1]);

                    if(importance == 4) {
                        maxGrade = grade;
                        if(maxGrade == 0) {
                            maxGrade = 1.0d;
                        }
                    }
                    title += splitValues[0] + " " + importance +  " ;";
                    if(grade > maxGrade) {
                        grade = -100000.0d;
                    }
                    grade = linesNum * importance; //( grade/ maxGrade) * importance;

                    totalGrade += grade;

                }

            }
            LinkedList<String> currentList = bestResults.get(totalGrade);

            if(currentList == null) {
                currentList = new LinkedList<String>();
            }

            currentList.add(title);

            bestResults.put(totalGrade, currentList);

            // skip ending
            dataReader.readLine();
            dataReader.readLine();
        }

        BufferedWriter writer = new BufferedWriter(new FileWriter("out/bestOutputData"));

        int num = 0;
        for(Double key : bestResults.keySet()) {
            if(num >= 100) {
                break;
            }
            num++;
            for(String val : bestResults.get(key)) {
                writer.write(key + "\n");
                writer.write(val);
                writer.write("\n");
            }
            writer.write("---------------------------\n");
        }
        writer.close();

        System.out.println("STOP");
    }

    public static void test() {
        try {
            dataReader = new BufferedReader(new FileReader("out/resultedData"));
            expectedDataReader = new BufferedReader(new FileReader("res/fastCompany.res/expected"));

            HashMap<String, Integer> expectedData = new HashMap<String, Integer>();
            fillExpectedData(expectedData);

            readAndCheck(expectedData);


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                dataReader.close();
                expectedDataReader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
