package netology.ru;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) throws Exception {
        String[] columnMapping = {"id", "firstName", "lastName", "country", "age"};

        String[] employee = ("1,John,Smith,USA,25").split(",");
        String[] employee1 = ("2,Ivan,Petrov,RU,23").split(",");

        try (CSVWriter writer = new CSVWriter(new FileWriter("data.csv"))) {
            writer.writeNext(employee);
            writer.writeNext(employee1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String fileName = "data.csv";


        List<Employee> list = parseCSV(columnMapping, fileName);
        createJSON("data.json", list);
        createXML("data.xml");
        List <Employee> list2 = parseXML("data.xml");
        System.out.println(list2);

    }

    public static void createJSON(String nameJSON, List <Employee> choice) {
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


    private static List<Employee> parseXML(String file) throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(new File(file));
        Node root = doc.getDocumentElement();
        NodeList nodeList = root.getChildNodes();
        System.out.println("Root: " + root.getNodeName());
        List<Employee> employeeList = null;
        for (int i = 0; i < nodeList.getLength(); i++) {
            Element node = (Element) nodeList.item(i);
            if (Node.ELEMENT_NODE == node.getNodeType()) {
                System.out.println("Current: " + node.getNodeName());
                if (node.getNodeType() != Node.TEXT_NODE) {
                    NodeList elementProps = node.getChildNodes();
                    for (int j = 0; j < elementProps.getLength(); j++) {
                        Element elementProp = (Element) elementProps.item(j);
                        System.out.println(elementProp.getNodeName() + ": " + elementProp.getChildNodes().item(0).getTextContent());
                        employeeList = new ArrayList<>();
                    }
                }
            }
        }
        return employeeList;
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
            employeeList.forEach(System.out::println);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return employeeList;
    }
}