package wardani.dika.gitsearch.util

import com.google.gson.Gson
import com.google.gson.GsonBuilder

@Suppress("unused")
object JSonHelper {
    private val gson: Gson = GsonBuilder()
        .setDateFormat("yyyy-MM-dd HH:mm:ss")
        .disableHtmlEscaping()
        .create()

    fun <T> fromJson(kClass: Class<T>, strJson: String): T {
        return gson.fromJson(strJson, kClass)
    }

    fun <T> fromJson(type: java.lang.reflect.Type, strJson: String): T {
        return gson.fromJson(strJson, type)
    }

    fun <T> toJson(kClass: Class<T>, obj: T): String {
        return gson.toJson(obj, kClass)
    }

    fun <T> toJson(obj: T): String {
        return gson.toJson(obj)
    }
}