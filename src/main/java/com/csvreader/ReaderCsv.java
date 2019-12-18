package com.csvreader;

import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.opencsv.CSVReader;
import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.CsvToBean;

import java.io.File;
import java.io.FileReader;
import java.util.List;

public class ReaderCsv {

    public static void main(String[] args) {
        try {
            read();
        } catch (Exception e) {
        }
    }

    /**
     * Mapping CSV row positions to Person class fields
     * @return
     */
    private static ColumnPositionMappingStrategy mapper() {
        ColumnPositionMappingStrategy strategy = new ColumnPositionMappingStrategy();
        strategy.setType(Person.class);
        String[] columns = new String[]{"id", "firstName", "lastName", "email", "gender", "ipAddress", "description"};
        strategy.setColumnMapping(columns);
        return strategy;
    }

    /**
     * Reading the CSV File
     *
     * @throws Exception
     */
    public static void read() throws Exception {
        //Reading CSV File
        CSVReader csvReader = new CSVReader(new FileReader(new File("src/main/resources/MOCK_DATA.csv")));
        CsvToBean<Person> csv = new CsvToBean();
        //Setting mapping strategy for csv field to model field mapping
        csv.setMappingStrategy(mapper());
        csv.setCsvReader(csvReader);
        //Actually parsing of csv fields to model fields, and finally objects
        List<Person> list = csv.parse();
        //Creating object mapper for object->json conversion
        ObjectMapper obj = new ObjectMapper();
        ObjectWriter writer = obj.writer(new DefaultPrettyPrinter());
        //Writting object to pretty json in file
        writer.writeValue(new File("src/main/resources/MOCK_JSON_DATA.json"), list);
        csvReader.close();
    }
}
