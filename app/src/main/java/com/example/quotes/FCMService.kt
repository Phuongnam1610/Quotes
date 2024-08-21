import com.google.gson.JsonObject
import org.apache.http.HttpException
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST
import java.io.IOException

class FCMService(private val apiKey: String) {
    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl("https://fcm.googleapis.com/v1/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val service: FCMAPIService = retrofit.create(FCMAPIService::class.java)

    fun sendPushNotification(
        registrationToken: String,
        notificationTitle: String,
        notificationBody: String
    ): Call<JsonObject> {
        val requestBody = JsonObject().apply {
            val message = JsonObject().apply {
                addProperty("token", registrationToken)
                val notification = JsonObject().apply {
                    addProperty("title", notificationTitle)
                    addProperty("body", notificationBody)
                }
                add("notification", notification)
            }
            add("message", message)
        }

        try {
            return service.sendMessage("Bearer $apiKey", "application/json", requestBody)
        } catch (e: Exception) {
            // Xử lý ngoại lệ
            when (e) {
                is IOException -> {
                }
                is HttpException -> {
                    // Xử lý lỗi HTTP
                }
                else -> {
                    // Xử lý các ngoại lệ khác
                }
            }
            // Ném lại ngoại lệ hoặc trả về một đối tượng Call mẫu
            throw e
        }
    }

    private interface FCMAPIService {
        @POST("projects/quotes-b9c8e/messages:send")
        fun sendMessage(
            @Header("Authorization") authHeader: String,
            @Header("Content-Type") contentTypeHeader: String,
            @Body requestBody: JsonObject
        ): Call<JsonObject>
    }
}