
import com.example.productappwithktor.api.CategoryServiceImp
import com.example.productappwithktor.model.Categories
import com.example.productappwithktor.model.ProductList
import io.ktor.client.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*

interface ApiService {
    suspend fun getCategory(): Categories
    suspend fun getProduct(): ProductList
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


