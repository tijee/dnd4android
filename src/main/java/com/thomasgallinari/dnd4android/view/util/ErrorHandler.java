package com.thomasgallinari.dnd4android.view.util;

import android.content.Context;
import android.widget.Toast;

/**
 * An error handler that logs the error and displays a {@link Toast}.
 */
public class ErrorHandler {

    /**
     * Logs the given error.
     * 
     * @param clazz
     *            the class that throws the error
     * @param logMsg
     *            the message to log
     * @param tr
     *            the thrown error
     */
    public static void log(Class<?> clazz, String logMsg, Throwable tr) {
	// Log.e(clazz.getName(), logMsg, tr);
    }

    /**
     * Logs and displays a toast with the given message.
     * 
     * @param clazz
     *            the class that throws the error
     * @param logMsg
     *            the message to log
     * @param tr
     *            the thrown error
     * @param toastMsg
     *            the message to display. will be displayed.
     * @param context
     *            the context who raised the error
     */
    public static void log(Class<?> clazz, String logMsg, Throwable tr,
	    String toastMsg, Context context) {
	// Log.e(clazz.getName(), logMsg, tr);
	if (toastMsg != null && toastMsg.length() > 0) {
	    Toast.makeText(context, toastMsg, Toast.LENGTH_LONG);
	}
    }
}
