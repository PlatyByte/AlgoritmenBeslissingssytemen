package controller;

import kraan.Job;
import kraan.Main;
import kraan.Problem;
import model.Yard;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Observable;
import java.io.FileWriter;
import java.util.Arrays;

/**
 * Created by Pieter-Jan on 20/10/2016.
 */
public class Controller extends Observable {


    private Problem huidigProbleem;

    private int limit, inLimit, outLimit, klok;

    private Yard yard;
    FileWriter writer;

    public Controller() throws IOException {

        if (huidigProbleem == null) {
            setFileSmall();
        }
        // String csvFile = "./data/csv/test.csv";
        // FileWriter writer = new FileWriter(csvFile);
        // CSVUtils.writeLine(writer, Arrays.asList("gID","T","x","y","itemInCraneID"));
        // writer.flush();
        reset();
    }

    public void doStep(boolean notify) throws IOException {


        if (klok == 0) {
            try {
                File hulp = new File(Main.class.getClassLoader().getResource("KRAANOPDRACHT1_Groep_Groep3.csv").toURI());
                writer = new FileWriter(hulp);
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
            CSVUtils.writeLine(writer, Arrays.asList("gID", "T", "x", "y", "itemInCraneID"));
            writer.flush();
        }


        if (klok < inLimit) {
            yard.executeJob(huidigProbleem.getInputJobSequence().get(klok), "INPUT");
//            yard.printOutYard();

            CSVUtils.writeLine(writer, Arrays.asList("" + Yard.csvparam1[0], "" + klok, "" + Yard.csvparam1[1], "" + Yard.csvparam1[2], "" + Yard.csvparam1[3]));
            writer.flush();
        }
        if (klok < outLimit) {
            yard.executeJob(huidigProbleem.getOutputJobSequence().get(klok), "OUTPUT");
//            yard.printOutYard();
            CSVUtils.writeLine(writer, Arrays.asList("" + Yard.csvparam1[0], "" + klok, "" + Yard.csvparam1[1], "" + Yard.csvparam1[2], "" + Yard.csvparam1[3]));
            writer.flush();
        }

        klok++;
        if (notify) {
            setChanged();
            notifyObservers();
        }

        if (klok >= inLimit && klok >= outLimit) {
            writer.close();
        }
    }

    public void reset() {
        yard = new Yard(huidigProbleem);
        //y.printOutYard();
        //y.printHash();

        inLimit = huidigProbleem.getInputJobSequence().size();
        outLimit = huidigProbleem.getOutputJobSequence().size();
        limit = Math.max(inLimit, outLimit);
        klok = 0;
    }

    public void setFileBigNietGeschrankt() {
        File file = null;

        try {
            file = new File(Main.class.getClassLoader().getResource("1_50_50_10_FALSE_60_25_100.json").toURI());
        } catch (URISyntaxException e) {
            System.out.println("Problem with loading file: 1_50_50_10_FALSE_60_25_100.json");
            System.out.println("Using a different method to load the file");
            file = new File("./data/1_50_50_10_FALSE_60_25_100.json");
        }

        try {
            huidigProbleem = Problem.fromJson(file);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            System.out.println("Problem with parsing the file: 1_50_50_10_FALSE_60_25_100.json");
        }
    }

    public void setFileBigGeschrankt() {
        File file = null;

        try {
            file = new File(Main.class.getClassLoader().getResource("1_50_50_10_TRUE_60_25_100.json").toURI());
        } catch (URISyntaxException e) {
            System.out.println("Problem with loading file: 1_50_50_10_TRUE_60_25_100.json");
            System.out.println("Using a different method to load the file");
            file = new File("./data/1_50_50_10_TRUE_60_25_100.json");
        }

        try {
            huidigProbleem = Problem.fromJson(file);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            System.out.println("Problem with parsing the file: 1_50_50_10_TRUE_60_25_100.json");
        }
    }

    public void setFileSmall() {
        File file = null;

        try {
            file = new File(Main.class.getClassLoader().getResource("testInput.json").toURI());
        } catch (URISyntaxException e) {
            System.out.println("Problem with loading file: testInput.json");
            System.out.println("Using a different method to load the file");
            file = new File("./data/testInput.json");
        }

        try {
            huidigProbleem = Problem.fromJson(file);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            System.out.println("Problem with parsing the file: testInput.json");
        }
    }

    public Problem getHuidigProbleem() {
        return huidigProbleem;
    }

    public int getLimit() {
        return limit;
    }

    public int getKlok() {
        return klok;
    }

    public Yard getYard() {
        return yard;
    }
}
