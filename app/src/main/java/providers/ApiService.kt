package providers

import responses.DogsResponse
import responses.DogsResponseEmpty
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Url

interface ApiService {
    @GET
    suspend fun getDogsByBreeds(@Url url:String):Response<DogsResponse>
    suspend fun getDogsEmpty(@Url url:String):Response<DogsResponseEmpty>
}