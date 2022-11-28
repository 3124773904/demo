package com.robot.tool;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

//UUID生成器
public class CreateUuid {
    //UUID生成器
    public static String uuid(){
        String uuid = UUID.randomUUID().toString();
        return uuid;
    }
    //日期
    public static String prtdate(){
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");//格式化一下
        Calendar calendar1 = Calendar.getInstance();//获取对日期操作的类对象
        //两种写法都可以获取到前三天的日期
        // calendar1.set(Calendar.DAY_OF_YEAR,calendar1.get(Calendar.DAY_OF_YEAR) -3);
        //在当前时间的基础上获取前三天的日期
        calendar1.add(Calendar.DATE, -3);
        //add方法 参数也可传入 月份，获取的是前几月或后几月的日期
        //calendar1.add(Calendar.MONTH, -3);
        Date today = calendar1.getTime();
        return sdf.format(today);
    }
}
