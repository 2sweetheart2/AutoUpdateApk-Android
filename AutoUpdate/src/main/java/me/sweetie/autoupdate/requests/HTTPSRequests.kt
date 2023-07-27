package me.sweetie.autoupdate.requests

import me.sweetie.autoupdate.interfaces.IJson
import org.json.JSONException
import org.json.JSONObject
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.nio.charset.StandardCharsets
import javax.net.ssl.HttpsURLConnection

class HTTPSRequests {

    companion object{
        fun iamsoscary(str: String): String {
            return str
                .replace("%".toRegex(), "%25") // Процент
                .replace(" ".toRegex(), "%20") // Пробел
                .replace("\t".toRegex(), "%20") // Табуляция (заменяем на пробел)
                .replace("\n".toRegex(), "%20") // Переход строки (заменяем на пробел)
                .replace("\r".toRegex(), "%20") // Возврат каретки (заменяем на пробел)
                .replace("!".toRegex(), "%21") // Восклицательный знак
                .replace("\"".toRegex(), "%22") // Двойная кавычка
                .replace("#".toRegex(), "%23") // Октоторп, решетка
                .replace("\\$".toRegex(), "%24") // Знак доллара
                .replace("&".toRegex(), "%26") // Амперсанд
                .replace("'".toRegex(), "%27") // Одиночная кавычка
                .replace("\\(".toRegex(), "%28") // Открывающаяся скобка
                .replace("\\)".toRegex(), "%29") // Закрывающаяся скобка
                .replace("\\*".toRegex(), "%2a") // Звездочка
                .replace("\\+".toRegex(), "%2b") // Знак плюс
                .replace(",".toRegex(), "%2c") // Запятая
                .replace("-".toRegex(), "%2d") // Дефис
                .replace("\\.".toRegex(), "%2e") // Точка
                .replace("/".toRegex(), "%2f") // Слеш, косая черта
                .replace(":".toRegex(), "%3a") // Двоеточие
                .replace(";".toRegex(), "%3b") // Точка с запятой
                .replace("<".toRegex(), "%3c") // Открывающаяся угловая скобка
                .replace("=".toRegex(), "%3d") // Знак равно
                .replace(">".toRegex(), "%3e") // Закрывающаяся угловая скобка
                .replace("\\?".toRegex(), "%3f") // Вопросительный знак
                .replace("@".toRegex(), "%40") // At sign, по цене, собачка
                .replace("\\[".toRegex(), "%5b") // Открывающаяся квадратная скобка
                .replace("\\\\".toRegex(), "%5c") // Одиночный обратный слеш '\'
                .replace("]".toRegex(), "%5d") // Закрывающаяся квадратная скобка
                .replace("\\^".toRegex(), "%5e") // Циркумфлекс
                .replace("_".toRegex(), "%5f") // Нижнее подчеркивание
                .replace("`".toRegex(), "%60") // Гравис
                .replace("\\{".toRegex(), "%7b") // Открывающаяся фигурная скобка
                .replace("\\|".toRegex(), "%7c") // Вертикальная черта
                .replace("\\}".toRegex(), "%7d") // Закрывающаяся фигурная скобка
                .replace("\\~".toRegex(), "%7e") // Тильда
        }


        fun sendPost(url: String, parameters: JSONObject, callback: IJson) {
            Thread {
                try {
                    val httpClient: HttpURLConnection
                    httpClient = URL(url).openConnection() as HttpURLConnection
                    httpClient.requestMethod = "POST"
                    httpClient.setRequestProperty(
                        "User-Agent",
                        "VKAndroidApp/5.52-4543 (Android 5.1.1; SDK 22; x86_64; unknown Android SDK built for x86_64; en; 320x240)"
                    )
                    httpClient.setRequestProperty(
                        "Accept",
                        "image/gif, image/x-xbitmap, image/jpeg, image/pjpeg, */*"
                    )
                    httpClient.setRequestProperty("Content-Type", "application/json; utf-8")
                    httpClient.doOutput = true
                    httpClient.outputStream.use { os ->
                        val input =
                            parameters.toString().toByteArray(StandardCharsets.UTF_8)
                        os.write(input, 0, input.size)
                    }
                    val br: BufferedReader
                    br =
                        if (httpClient.responseCode < HttpURLConnection.HTTP_BAD_REQUEST) BufferedReader(
                            InputStreamReader(httpClient.inputStream)
                        ) else BufferedReader(InputStreamReader(httpClient.errorStream))
                    try {
                        var line: String?
                        val response = java.lang.StringBuilder()
                        while (br.readLine().also { line = it } != null) {
                            response.append(line)
                        }
                        callback.getJson(JSONObject(response.toString()))
                    } catch (e: IOException) {
                        e.printStackTrace()
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                } catch (e: IOException) {
                    e.printStackTrace()
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }.start()
        }
    }
}