package anamapp.pro.belajar.helpers;

import java.text.NumberFormat;
import java.util.Locale;

public class Helper {
    public static String convertRupiah(int nominal){
        Locale localeID = new Locale("in", "ID");
        NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(localeID);
        return formatRupiah.format(nominal);
    }
}
