package hanu.a2_2001040205.util;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Currency;
import java.util.Locale;

public class CurrencyFormatter {
//    public static String format(Long num) {
//
//        String COUNTRY = "VN";
//        String LANGUAGE = "vi";
//
//        return NumberFormat.getCurrencyInstance(new Locale(LANGUAGE, COUNTRY)).format(num);
//    }
public static String format(Long num) {
    String COUNTRY = "VN";
    String LANGUAGE = "vi";

    // Create a DecimalFormat object with the desired format pattern
    DecimalFormat decimalFormat = new DecimalFormat("#,###.###");

    // Set the grouping and decimal separators to match the locale
    DecimalFormatSymbols symbols = decimalFormat.getDecimalFormatSymbols();
    symbols.setGroupingSeparator('.');
    symbols.setDecimalSeparator(',');
    decimalFormat.setDecimalFormatSymbols(symbols);

    // Set the currency symbol to the desired symbol (in this case, the Vietnamese dong symbol)
    String currencySymbol = Currency.getInstance(new Locale(LANGUAGE, COUNTRY)).getSymbol(new Locale(LANGUAGE, COUNTRY));
    String desiredSymbol = "₫";
    String formattedPrice = currencySymbol.replace(currencySymbol, desiredSymbol) + " ";

    // Cast the input number to a double and divide by 1000 to get the decimal point in the correct position
    double numInCurrencyUnit = (double) num ;

    // Format the number as a string using the decimal format
    formattedPrice += decimalFormat.format(numInCurrencyUnit);

    return formattedPrice;
}

}
