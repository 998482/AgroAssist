import com.example.agroinnovexa20.data.model.weather.Astro
import com.example.agroinnovexa20.data.model.weather.Day
import com.example.agroinnovexa20.data.model.weather.Hour


data class Forecastday(
    val astro: Astro,
    val date: String,
    val date_epoch: Int,
    val day: Day,
    val hour: List<Hour>
)