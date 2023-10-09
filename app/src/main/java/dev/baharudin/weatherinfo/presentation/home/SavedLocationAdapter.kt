package dev.baharudin.weatherinfo.presentation.home

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import dev.baharudin.weatherinfo.R
import dev.baharudin.weatherinfo.databinding.ItemSavedLocationBinding
import dev.baharudin.weatherinfo.domain.entities.Condition
import dev.baharudin.weatherinfo.domain.entities.Location
import dev.baharudin.weatherinfo.presentation.common.getWeatherIconUrl
import kotlinx.coroutines.withContext
import java.util.Locale

class SavedLocationAdapter(
    private val context: Context,
    private val conditions: ArrayList<Condition>,
    private val setOnClickListener: (Condition) -> Unit,
) :
    RecyclerView.Adapter<SavedLocationAdapter.ViewHolder>() {
    class ViewHolder(var binding: ItemSavedLocationBinding) :
        RecyclerView.ViewHolder(binding.root) {
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemSavedLocationBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = conditions.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val condition = conditions[position]
        with(holder.binding) {
            tvCity.text = condition.location.city

            val currentTemperature = condition.temperature?.current
            if (currentTemperature != null) {
                progressBar.visibility = View.GONE
                tvCurrentTemperature.text =
                    context.getString(R.string.temperature, currentTemperature)
            }

            val weatherIconUrl = condition.getWeatherIconUrl()
            if (!weatherIconUrl.isNullOrBlank()) {
                Glide.with(context).load(weatherIconUrl)
                    .into(
                        object : CustomTarget<Drawable>(40, 40) {
                            override fun onLoadCleared(placeholder: Drawable?) {
                                tvCurrentTemperature.setCompoundDrawablesWithIntrinsicBounds(
                                    placeholder,
                                    null,
                                    null,
                                    null
                                )
                            }

                            override fun onResourceReady(
                                resource: Drawable,
                                transition: Transition<in Drawable>?
                            ) {
                                tvCurrentTemperature.setCompoundDrawablesWithIntrinsicBounds(
                                    resource,
                                    null,
                                    null,
                                    null
                                )
                            }
                        }
                    )
            }

            itemSavedCondition.setOnClickListener { setOnClickListener(condition) }
        }
    }
}