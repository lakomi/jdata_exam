package cn.edu.swpu.jdata_exam.utils.util.thread;


import java.util.Calendar;
import java.util.Timer;

public class TimerManager {


    public static void startManager() {
        //得到时间类

        Calendar date = Calendar.getInstance();
        //设置时间为 xx-xx-xx 00:00:00

        date.set(date.get(Calendar.YEAR), date.get(Calendar.MONTH), date.get(Calendar.DATE), 0, 0, 0);

        //一天的毫秒数

        long daySpan = 24 * 60 * 60 * 1000;

        //得到定时器实例

        Timer t = new Timer();

        //使用匿名内方式进行方法覆盖

        t.schedule(new Task(), date.getTime(), daySpan); //daySpan是一天的毫秒数，也是执行间隔

    }

}