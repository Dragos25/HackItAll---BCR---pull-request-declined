
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;


import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvValidationException;
import yahoofinance.Stock;
import yahoofinance.YahooFinance;
import yahoofinance.histquotes.HistoricalQuote;
import yahoofinance.histquotes.Interval;

public class Main {

    private static String convertDate(Calendar cal) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String formatDate = format.format(cal.getTime());
        return formatDate;
    }

    private static Interval getInterval(String searchType) {
        Interval interval = null;
        if (searchType.toUpperCase() == "MONTHLY")
            interval = Interval.MONTHLY;
        else if (searchType.toUpperCase() == "WEEKLY")
            interval = Interval.WEEKLY;
        else
            interval = Interval.DAILY;

        return interval;
    }


    public static ArrayList<String> readAllCompanies() {
        ArrayList<String> records = new ArrayList<String>();
        try {

            CSVReader reader = new CSVReader(new FileReader("C:\\Users\\DRAGOS\\Desktop\\hackitall\\API forbes\\src\\main\\java\\500COMPANII_CSV.csv"));

            String[] nextLine;
            int rowNumber = 0;
            while ((nextLine = reader.readNext()) != null) {
                rowNumber++;
                String name = nextLine[0];
                records.add(name);
            }
        } catch (FileNotFoundException e) {

            e.printStackTrace();
        } catch (IOException e) {

            e.printStackTrace();
        } catch (CsvValidationException e) {
            e.printStackTrace();
        }
        return records;
    }

    public static void writeCompanyData(List<HistoricalQuote> history, String company) throws IOException {
        CSVWriter csvWrite = new CSVWriter(new FileWriter(company+".csv"));
        String[] entries = {"date, price"};
        csvWrite.writeNext(entries);
        for (HistoricalQuote quote : history) {

            System.out.println("symobol : " + quote.getSymbol());
            System.out.println("date : " + convertDate(quote.getDate()));
            System.out.println("Closed price : " + quote.getClose());
            String[] newLine = {convertDate(quote.getDate()), quote.getClose().toString()};
            csvWrite.writeNext(newLine);
        }
    }

    public static void getDataForCompany(String company) throws IOException {
        Calendar from = Calendar.getInstance();
        Calendar to = Calendar.getInstance();
        int year = 7;
        from.add(Calendar.YEAR, Integer.valueOf("-" + year));

        Stock stock = YahooFinance.get(company);
        List<HistoricalQuote> history = stock.getHistory(from, to, getInterval("WEEKLY"));
        writeCompanyData(history,company);
    }

    public static void main(String[] args) throws IOException {

        ArrayList<String> allCompanies = readAllCompanies();
        allCompanies.stream().forEach(System.out::println);
        getDataForCompany("AAPL");

    }
}
