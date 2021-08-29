package com.webaddicted.kotlinproject.global.common

/**
 * Helper class for a list (or tree) of LoggerNodes.
 *
 *
 *
 * When this is set as the head of the list,
 * an instance of it can function as a drop-in replacement for [android.util.Log].
 * Most of the methods in this class server only to map a method call in Log to its equivalent
 * in LogNode.
 */
class Lg {
    // Grabbing the native values from Android's native logging facilities,
    // to make for easy migration and interop.
    companion object {
        val NONE = -1
        val VERBOSE = android.util.Log.VERBOSE
        val DEBUG = android.util.Log.DEBUG
        val INFO = android.util.Log.INFO
        val WARN = android.util.Log.WARN
        val ERROR = android.util.Log.ERROR
        val ASSERT = android.util.Log.ASSERT

        var ISDEBUG = true

        /**
         * Instructs the LogNode to print the log result provided. Other LogNodes can
         * be chained to the end of the LogNode as desired.
         *
         * @param priority Log level of the result being logged. Verbose, Error, etc.
         * @param tag      Tag for for the log result. Can be used to organize log statements.
         * @param msg      The actual message to be logged.
         * @param tr       If an exception was thrown, this can be sent along for the logging facilities
         * to extract and print useful information.
         */
        @JvmOverloads
        fun println(priority: Int, tag: String, msg: String?, tr: Throwable? = null) {
            try {
                if (ISDEBUG) {
                    when (priority) {
                        VERBOSE -> android.util.Log.v(tag, msg!!)
                        DEBUG -> android.util.Log.d(tag, msg!!)
                        INFO -> android.util.Log.i(tag, msg!!)
                        WARN -> android.util.Log.w(tag, msg!!)
                        ERROR -> android.util.Log.e(tag, msg!!)
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }

        /**
         * Prints a message at VERBOSE priority.
         *
         * @param tag Tag for for the log result. Can be used to organize log statements.
         * @param msg The actual message to be logged.
         * @param tr  If an exception was thrown, this can be sent along for the logging facilities
         * to extract and print useful information.
         */
        @JvmOverloads
        fun v(tag: String, msg: String, tr: Throwable? = null) {
            println(VERBOSE, tag, msg, tr)
        }


        /**
         * Prints a message at DEBUG priority.
         *
         * @param tag Tag for for the log result. Can be used to organize log statements.
         * @param msg The actual message to be logged.
         * @param tr  If an exception was thrown, this can be sent along for the logging facilities
         * to extract and print useful information.
         */
        @JvmOverloads
        fun d(tag: String, msg: String, tr: Throwable? = null) {
            println(DEBUG, tag, msg, tr)
        }

        /**
         * Prints a message at INFO priority.
         *
         * @param tag Tag for for the log result. Can be used to organize log statements.
         * @param msg The actual message to be logged.
         * @param tr  If an exception was thrown, this can be sent along for the logging facilities
         * to extract and print useful information.
         */
        @JvmOverloads
        fun i(tag: String, msg: String, tr: Throwable? = null) {
            println(INFO, tag, msg, tr)
        }

        /**
         * Prints a message at WARN priority.
         *
         * @param tag Tag for for the log result. Can be used to organize log statements.
         * @param msg The actual message to be logged.
         * @param tr  If an exception was thrown, this can be sent along for the logging facilities
         * to extract and print useful information.
         */
        @JvmOverloads
        fun w(tag: String, msg: String?, tr: Throwable? = null) {
            println(WARN, tag, msg, tr)
        }

        /**
         * Prints a message at WARN priority.
         *
         * @param tag Tag for for the log result. Can be used to organize log statements.
         * @param tr  If an exception was thrown, this can be sent along for the logging facilities
         * to extract and print useful information.
         */
        fun w(tag: String, tr: Throwable) {
            w(tag, null, tr)
        }

        /**
         * Prints a message at ERROR priority.
         *
         * @param tag Tag for for the log result. Can be used to organize log statements.
         * @param msg The actual message to be logged.
         * @param tr  If an exception was thrown, this can be sent along for the logging facilities
         * to extract and print useful information.
         */
        @JvmOverloads
        fun e(tag: String, msg: String, tr: Throwable? = null) {
            println(ERROR, tag, msg, tr)
        }

        /**
         * Prints a message at ASSERT priority.
         *
         * @param tag Tag for for the log result. Can be used to organize log statements.
         * @param msg The actual message to be logged.
         * @param tr  If an exception was thrown, this can be sent along for the logging facilities
         * to extract and print useful information.
         */
        @JvmOverloads
        fun wtf(tag: String, msg: String?, tr: Throwable? = null) {
            println(ASSERT, tag, msg, tr)
        }

        /**
         * Prints a message at ASSERT priority.
         *
         * @param tag Tag for for the log result. Can be used to organize log statements.
         * @param tr  If an exception was thrown, this can be sent along for the logging facilities
         * to extract and print useful information.
         */
        fun wtf(tag: String, tr: Throwable) {
            wtf(tag, null, tr)
        }
    }
}
/**
 * Instructs the LogNode to print the log result provided. Other LogNodes can
 * be chained to the end of the LogNode as desired.
 *
 * @param priority Log level of the result being logged. Verbose, Error, etc.
 * @param tag      Tag for for the log result. Can be used to organize log statements.
 * @param msg      The actual message to be logged. The actual message to be logged.
 */
/**
 * Prints a message at VERBOSE priority.
 *
 * @param tag Tag for for the log result. Can be used to organize log statements.
 * @param msg The actual message to be logged.
 */
/**
 * Prints a message at DEBUG priority.
 *
 * @param tag Tag for for the log result. Can be used to organize log statements.
 * @param msg The actual message to be logged.
 */
/**
 * Prints a message at INFO priority.
 *
 * @param tag Tag for for the log result. Can be used to organize log statements.
 * @param msg The actual message to be logged.
 */
/**
 * Prints a message at WARN priority.
 *
 * @param tag Tag for for the log result. Can be used to organize log statements.
 * @param msg The actual message to be logged.
 */
/**
 * Prints a message at ERROR priority.
 *
 * @param tag Tag for for the log result. Can be used to organize log statements.
 * @param msg The actual message to be logged.
 */
/**
 * Prints a message at ASSERT priority.
 *
 * @param tag Tag for for the log result. Can be used to organize log statements.
 * @param msg The actual message to be logged.
 */
