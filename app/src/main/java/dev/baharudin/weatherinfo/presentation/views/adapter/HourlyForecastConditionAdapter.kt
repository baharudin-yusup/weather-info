package dev.baharudin.weatherinfo.presentation.views.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import dev.baharudin.weatherinfo.R
import dev.baharudin.weatherinfo.databinding.ItemHourlyForecastBinding
import dev.baharudin.weatherinfo.domain.entities.Condition
import dev.baharudin.weatherinfo.presentation.common.getWeatherIconUrl
import java.text.SimpleDateFormat
import java.util.Locale

class HourlyForecastConditionAdapter(
    private val context: Context,
    private val conditions: ArrayList<Condition>,
) :
    RecyclerView.Adapter<HourlyForecastConditionAdapter.ViewHolder>() {
    class ViewHolder(var binding: ItemHourlyForecastBinding) :
        RecyclerView.ViewHolder(binding.root) {
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemHourlyForecastBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = conditions.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val condition = conditions[position]
        with(holder.binding) {
            val date = condition.date
            if (date != null) {
                val dtf = SimpleDateFormat("HH:mm", Locale.ENGLISH)
                tvHour.text = dtf.format(condition.date)
            }

            val weatherIconUrl = condition.getWeatherIconUrl()
            if (!weatherIconUrl.isNullOrBlank()) {
                Glide.with(context).load(weatherIconUrl).into(ivWeatherIcon)
            }

            val currentTemperature = condition.temperature?.current
            if (currentTemperature != null) {
                tvTemperature.text = context.getString(R.string.temperature, currentTemperature)
            }
        }
    }
}