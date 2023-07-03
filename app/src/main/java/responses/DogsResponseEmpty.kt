package responses

import com.google.gson.annotations.SerializedName

data class DogsResponseEmpty(
    @SerializedName("status") var status: String,
    @SerializedName("message") var imagesDogEmpty: String
)