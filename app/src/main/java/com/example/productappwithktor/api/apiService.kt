
import com.example.productappwithktor.api.CategoryServiceImp
import com.example.productappwithktor.model.Categories
import io.ktor.client.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*

interface ApiService {
    suspend fun getCategory(): Categories
    companion object {
        fun create(): ApiService {
            return CategoryServiceImp(
                client = HttpClient {
                    install(JsonFeature) {
                        serializer = KotlinxSerializer()
                    }
                }
            )
        }
    }
}


