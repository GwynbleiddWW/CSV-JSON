package netology.ru;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.w3c.dom.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main {

    private static boolean bId;
    private static boolean bFirstName;
    private static boolean bLastName;
    private static boolean bCountry;
    private static boolean bAge;

    public static void main(String[] args) throws Exception {
        String[] columnMapping = {"id", "firstName", "lastName", "country", "age"};
        String[] employee = ("1,John,Smith,USA,25").split(",");
        String[] employee1 = ("2,Ivan,Petrov,RU,23").split(",");

        String fileName = "data.csv";
        createCSV(fileName, employee, employee1);
        List<Employee> list = parseCSV(columnMapping, fileName);
        createJSON("data.json", list);

        String json = readString("data.json");
        jsonToList(json);
        createXML("data.xml");

        List<Employee>listFromXML = xmlToList("data.xml");
        System.out.println(listFromXML);
        createJSON("data2.json", listFromXML);
    }

    private static List<Employee> xmlToList(String fileNameXml) {
        System.out.println("\nМетод, добавляющий в список любое количество работников: ");
        List<Employee> empList = new ArrayList<>();
        Employee emp = null;
        XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();
        try {
            XMLStreamReader xmlStreamReader = xmlInputFactory
                    .createXMLStreamReader(new FileInputStream(fileNameXml));
            int event = xmlStreamReader.getEventType();
            while (true) {
                switch (event) {
                    case XMLStreamConstants.START_ELEMENT:
                        switch (xmlStreamReader.getLocalName()) {
                            case "employee" -> emp = new Employee();
                            case "id" -> bId = true;
                            case "firstName" -> bFirstName = true;
                            case "lastName" -> bLastName = true;
                            case "country" -> bCountry = true;
                            case "age" -> bAge = true;
                        }
                        break;
                    case XMLStreamConstants.CHARACTERS:
                        assert emp != null;
                        if (bId) {
                            emp.setId(Long.parseLong(xmlStreamReader.getText()));
                            bId = false;
                        } else if (bFirstName) {
                            emp.setFirstName(xmlStreamReader.getText());
                            bFirstName = false;
                        } else if (bLastName) {
                            emp.setLastName(xmlStreamReader.getText());
                            bLastName = false;
                        } else if (bCountry) {
                            emp.setCountry(xmlStreamReader.getText());
                            bCountry = false;
                        }
                        else if (bAge) {
                            emp.setAge(Integer.parseInt(xmlStreamReader.getText()));
                            bAge = false;
                        }
                        break;
                    case XMLStreamConstants.END_ELEMENT:
                        if (xmlStreamReader.getLocalName().equals("employee")) {
                            empList.add(emp);
                        }
                        break;
                }
                if (!xmlStreamReader.hasNext())
                    break;

                event = xmlStreamReader.next();
            }

        } catch (FileNotFoundException | XMLStreamException e) {
            e.printStackTrace();
        }
        return empList;
    }

    private static void jsonToList(String jsonName) {
        final ObjectMapper objectMapper = new ObjectMapper();
        Employee[] employees = new Employee[0];
        try {
            employees = objectMapper.readValue(jsonName, Employee[].class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        List<Employee> employeeList = new ArrayList<>(Arrays.asList(employees));
        System.out.println("\nJSON to Java class (Task 3)");
        employeeList.forEach(x -> System.out.println(x.toString()));
    }

    private static void createCSV(String nameCSV, String[] worker1, String[] worker2) {
        try (CSVWriter writer = new CSVWriter(new FileWriter(nameCSV))) {
            writer.writeNext(worker1);
            writer.writeNext(worker2);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void createJSON(String nameJSON, List<Employee> choice) {
        Gson gson = new GsonBuilder().create();
        Type listType = new TypeToken<List<Employee>>() {
        }.getType();
        String json = gson.toJson(choice, listType);

        try (FileWriter file = new FileWriter(nameJSON)) {
            file.write(json);
            file.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void createXML(String name) throws ParserConfigurationException, TransformerException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.newDocument();

        Element staff = document.createElement("staff");
        document.appendChild(staff);

        Element employee = document.createElement("employee");
        staff.appendChild(employee);
        Element id = document.createElement("id");
        id.appendChild(document.createTextNode("1"));
        Element firstName = document.createElement("firstName");
        firstName.appendChild(document.createTextNode("Jhon"));
        Element lastName = document.createElement("lastName");
        lastName.appendChild(document.createTextNode("Smith"));
        Element country = document.createElement("country");
        country.appendChild(document.createTextNode("USA"));
        Element age = document.createElement("age");
        age.appendChild(document.createTextNode("25"));
        employee.appendChild(id);
        employee.appendChild(firstName);
        employee.appendChild(lastName);
        employee.appendChild(country);
        employee.appendChild(age);

        employee = document.createElement("employee");
        staff.appendChild(employee);
        id = document.createElement("id");
        id.appendChild(document.createTextNode("2"));
        firstName = document.createElement("firstName");
        firstName.appendChild(document.createTextNode("Ivan"));
        lastName = document.createElement("lastName");
        lastName.appendChild(document.createTextNode("Petrov"));
        country = document.createElement("country");
        country.appendChild(document.createTextNode("RU"));
        age = document.createElement("age");
        age.appendChild(document.createTextNode("23"));
        employee.appendChild(id);
        employee.appendChild(firstName);
        employee.appendChild(lastName);
        employee.appendChild(country);
        employee.appendChild(age);

        DOMSource domSource = new DOMSource(document);
        StreamResult streamResult = new StreamResult(new File(name));
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        transformer.transform(domSource, streamResult);
    }

    private static String readString(String nameJSON) {
        String json = null;
        try {
            JSONParser parser = new JSONParser();
            JSONArray jsonArray = (JSONArray) parser.parse(
                    new FileReader(nameJSON));
            json = jsonArray.toJSONString();
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        return json;
    }

    private static List<Employee> parseCSV(String[] choice, String name) {
        List<Employee> employeeList = null;
        try (CSVReader csvReader = new CSVReader(new FileReader(name))) {
            ColumnPositionMappingStrategy<Employee> strategy = new ColumnPositionMappingStrategy<>();
            strategy.setType(Employee.class);
            strategy.setColumnMapping(choice);
            CsvToBean<Employee> csv = new CsvToBeanBuilder<Employee>(csvReader)
                    .withMappingStrategy(strategy)
                    .build();
            employeeList = csv.parse();
            System.out.println("CSV to list and creating JSON further (Task 1)");
            employeeList.forEach(System.out::println);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return employeeList;
    }
}