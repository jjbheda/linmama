package com.linmama.dinning.utils;

import com.elvishew.xlog.LogLevel;
import com.elvishew.xlog.Logger;
import com.elvishew.xlog.printer.AndroidPrinter;
import com.linmama.dinning.LmamaApplication;

public class XlogUtils {

    static Logger.Builder builder = new Logger.Builder();

    public static void printLog(String log) {
        builder.printers(new AndroidPrinter(), LmamaApplication.globalFilePrinter);
        builder.tag(log);
        Logger logger = builder.build();
        logger.v(LogLevel.VERBOSE);
    }
}
