package cn.edu.swpu.jdata_exam.utils.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateTimeUtil {

    public static String convertDateFileSstr(Date date){
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
        String dateString = formatter.format(date);
        return dateString;
    }
    public static String convertDateToString(Date date){
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String dateString = formatter.format(date);
        return dateString;
    }
    public static Date convertStringTodate(String dateStr){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = format.parse(dateStr);
            System.out.print(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    //得到当天应当上传的文夹名称
    public static String getNowDir(){
        return convertDateToString(new Date());
    }

}
