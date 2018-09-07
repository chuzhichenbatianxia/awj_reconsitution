package study;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Administrator on 2018/9/6.
 */
public class DateUtil {

    private final static SimpleDateFormat FORMAT = new SimpleDateFormat("yyyy/mm/dd");

    public static void main(String[] args) throws ParseException {

        ExecutorService executorService = Executors.newFixedThreadPool(100);

        while (true){
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    System.out.println(FORMAT.format(new Date()));
                }
            });
        }


    }

}
