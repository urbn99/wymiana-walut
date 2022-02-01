package pl.marcinurbanski;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.math.RoundingMode;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        //adres URL kursów walut
        String url = "https://www.ecb.europa.eu/stats/eurofxref/eurofxref-daily.xml";

        boolean shoudContinue = true;
        Scanner scanner = new Scanner(System.in);

        //ekran początkowy
        System.out.println("Witaj, aby wyświetliść aktualną tablicę walut naciśnij ENTER ");
        String test = scanner.nextLine();

        try
        {
            while (shoudContinue){

                //dostęp do adresu URL z plikiem XML
                DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                DocumentBuilder db = dbf.newDocumentBuilder();
                Document document = db.parse(new URL(url).openStream());
                document.getDocumentElement().normalize();
                NodeList nList = document.getElementsByTagName("Cube");

                //tablica walut
                System.out.println("Nr Waluta Kurs");
                for (int i = 2; i < nList.getLength(); i++) {

                    Node nNode = nList.item(i);
                    if (nNode.getNodeType() == Node.ELEMENT_NODE) {

                        Element elem = (Element) nNode;
                        String currency = elem.getAttribute("currency");
                        String rateNum = elem.getAttribute("rate");

                        System.out.println((i - 1) +"  "+ currency + "  "+ rateNum);
                    }
                }

                //wymiana wybranej waluty
                System.out.print("Podaj kwotę do wymiany w EURO: ");
                int exchange = scanner.nextInt();
                if(exchange <1 || exchange > 100000){
                    System.out.println("kwota musi być w przedziale od 1€ do 100 000€ - spróbuj ponownie");
                    return;
                }

                System.out.print("Podaj numer waluty do wymiany: ");
                int currencySelected = scanner.nextInt();
                currencySelected++;
                if(currencySelected <1 || currencySelected > 33){
                    System.out.println("Numer waluty musi być w przedziale od 1 do 32 - spróbuj ponownie");
                    return;
                }

                //wyszukanie wybranej waluty z pliku XML
                Node nNode = nList.item(currencySelected );
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {

                    Element elem = (Element) nNode;
                    String currency = elem.getAttribute("currency");
                    String rateString = elem.getAttribute("rate");
                    double rateDouble = Double.parseDouble(rateString);
                    double roundPrice =  rateDouble * exchange;

                    //zaokrąglenie waluty
                    DecimalFormat df = new DecimalFormat("0.00");
                    df.setRoundingMode(RoundingMode.DOWN);

                    System.out.println("-----------------");
                    System.out.println("Transakcja udana");
                    System.out.println("Waluta: " +  currency);
                    System.out.println("Kwota przed przewalutowaniem: "+ exchange +" €" );
                    System.out.println("Kwota po przewalutowaniu:  "+ df.format(roundPrice )+" " +currency );
                    System.out.println("Przewalutowano po kursie: "+ rateString );
                    System.out.println("-----------------");
                    System.out.println("Wybierz 0 aby wyjść z programu, wybierz 1 żeby rozpocząć nową transakcję");

                    //wyjście z programu lub ponowna wymiana
                    int userChoice = scanner.nextInt();
                    if(userChoice == 0){
                        shoudContinue = false;
                    }
                }
            }
        }
        catch (InputMismatchException e)
        {
            System.out.println("ERROR: w zapisie wystąpiła litera lub inny znak, podaj tylko liczby całkowite - spróbuj ponownie");
        }
        catch (Exception e)
        {
            System.out.println("ERROR");
        }
    }
}
