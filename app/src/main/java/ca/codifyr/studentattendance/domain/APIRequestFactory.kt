package ca.codifyr.studentattendance.domain

import okhttp3.*

class APIRequestFactory {
    companion object {
        val JSON = MediaType.get("application/json; charset=utf-8")

        fun buildAPIURL(suffix: String): String {
            return Constants.API_URL + "/" + suffix
        }

        fun buildGetRequest(suffix: String): Call {
            val client = OkHttpClient()
            val url = buildAPIURL(suffix)
            val request = Request.Builder()
                .url(url)
                .get()
                .build()
           return client.newCall(request)
        }

        fun buildPostRequest(suffix: String, body: String): Call {
            val client = OkHttpClient()
            val url = buildAPIURL(suffix)
            val body = RequestBody.create(JSON, body)
            val request = Request.Builder()
                .url(url)
                .post(body)
                .build()
            return client.newCall(request)
        }
    }
}