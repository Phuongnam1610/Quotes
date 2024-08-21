package com.example.quotes

import android.util.Log
import com.google.auth.oauth2.GoogleCredentials
import com.google.common.collect.Lists
import java.io.ByteArrayInputStream
import java.io.InputStream
import java.nio.charset.StandardCharsets

class AccessToken {
    fun getAccessToken(): String? {
        val firebaseMessageingScope="https://www.googleapis.com/auth/firebase.messaging";

        try {
            val jsonString = "{\n" +
                    "  \"type\": \"service_account\",\n" +
                    "  \"project_id\": \"quotes-b9c8e\",\n" +
                    "  \"private_key_id\": \"e8e46bc5d2f762f0d584d575db1ca7271e9cfb78\",\n" +
                    "  \"private_key\": \"-----BEGIN PRIVATE KEY-----\\nMIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQDTM6PtOr9NGGgn\\nBrTr7swy/9EE5T3SnfFWu6jwEVskeccklX2hBSJpzoBcBwFDVKDEhTI7bBsrWIZu\\nFZIGxo80IFioTgRXONWXSGgb/3ZWDSHEc1p7+HjljPDSGG6f0YDzSwlx6EugNoDX\\nikA7CZsfd83A6M81g3yl62fRwiidB1501C2J3LCSw0ptifZedFj7dkjy9qoqKgs6\\n1Y1MwD3rPfx69/HBrXbgWrxz1Y9gvQ3NhmHizkPbCusoOlGBHhdKkNfThmKBDdyZ\\nu3Rq5grWByzZmDTHo82tPxDzSxUzAwE+Dd2Vza7q1cUMFt3TEy9Cm555w0hwjWK4\\nrREXGGaVAgMBAAECggEAOWu+0863GCTcnE+sfMDN78excl1JM6wRY31d7vcpCn+5\\nbu7d2xtkQdQM/fhBlaa6lvOHtrQrpttIoUFoFEODx65LrGaZs4UWPS1IcORBrh+f\\n4KNkJHRnmjF5gAV4ficdogfyp296sDJhUTibQfHYpPOTkzE4tNns18dq0JKzLA3I\\nSlGlOT9voAGzAsDHsD5fKYTIIvsU3B2b3b+3/OnglnWqhlMNVnL8D1fNXa2TVHnz\\nrxKalz4yHrXzi7p1r2JcmCwHSuGfmTd514vORtkWCwdfIwDfl1K6YpQ9bFUkTQQs\\nW5HspHHTEbup1z4Iudxxs1nz95EBR/NgcL2piqpIuQKBgQDyaZ4PqGDOyRmyR9Af\\nj81pzZIhjtunDK6OO8+L8kPyVbB2TDql9Q0CbmIRMjIuQLUmkwMakXwgQY6edwl3\\nBy4bIUmab3bS+rIz1HZ2M/7jfoHkRJ1f+6DOQpUjnWxT2INMqyEufCoeyOqm8bvb\\nLtWd3XPibON7uRziRylAE+Q6dwKBgQDfCi2IEo3Fdoq35J2OuaVcxq0LjNzrC/nd\\ncKXGjXac5jytsX1dl+wT+iYAqgwwrVwfiM+/SdalLmr/V5hWJ2juTaXX1A59lQa0\\nE9YFY4mbxj61QJXDAdPnv701Tu/X79tDl3XrUsk914NVO30ehBkUsC/vD9Hok2V2\\nPaWTdRyeUwKBgQDqGtu9rVOIgCejx7yTrnGm+xNj+y3fnty4mcRvF5FM6DoZlLaS\\n7RwaO/XTO5HAn1cSoKk/sD6afBU6riTi4XlCVBxb28d6KweMDGQ29TH0C4lvpGAD\\ngOOkLiMPmQkz1AXBNgc+eETJJCmQ/iT4oLtF0McIZTYvlu7xC94nds5TMwKBgGrh\\nKLJ1GaTPSmgW0H2tbpWHyzKmvNKRuZmounfsIDPNjLQ7QnUQMRvdmkplMlQ9LtEZ\\nsVvcGVSsaq+TnTdjyT+wy9EiGBOLz1uJydxpN28DT9dxbJK3FIGQey3JdtuGYA8C\\nzIoI8NCGrrOwPyPZb19fkoi/jvohb9R6dBa2XxFrAoGBANMdfm+C2ahUi4wYUt5O\\ne075ErOYJ1sYfJZb2UHjpr5p5RQ1NE+HKivJZQ0y/I9vNrNtLL0ZcnAOk5dzS9+Q\\ned/sRpD4I1axVW/LFNPKB6oOT9kzpPflfLThaVJqRsKNCbf0nI49utbuzgJVHqPb\\nIgr1/CoaVqP5C06YftzWD9vH\\n-----END PRIVATE KEY-----\\n\",\n" +
                    "  \"client_email\": \"firebase-adminsdk-tkbcg@quotes-b9c8e.iam.gserviceaccount.com\",\n" +
                    "  \"client_id\": \"103381595978471664288\",\n" +
                    "  \"auth_uri\": \"https://accounts.google.com/o/oauth2/auth\",\n" +
                    "  \"token_uri\": \"https://oauth2.googleapis.com/token\",\n" +
                    "  \"auth_provider_x509_cert_url\": \"https://www.googleapis.com/oauth2/v1/certs\",\n" +
                    "  \"client_x509_cert_url\": \"https://www.googleapis.com/robot/v1/metadata/x509/firebase-adminsdk-tkbcg%40quotes-b9c8e.iam.gserviceaccount.com\",\n" +
                    "  \"universe_domain\": \"googleapis.com\"\n" +
                    "}\n"

            val stream = ByteArrayInputStream(jsonString.toByteArray(StandardCharsets.UTF_8))
            val ggcre = GoogleCredentials.fromStream(stream)
                .createScoped(Lists.newArrayList(firebaseMessageingScope))
            ggcre.refresh()
            return ggcre.accessToken.tokenValue
        }
        catch (e:Exception){
            return null
        }
    }
}