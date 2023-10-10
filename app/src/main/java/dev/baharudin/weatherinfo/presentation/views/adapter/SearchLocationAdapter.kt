package dev.baharudin.weatherinfo.presentation.views.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import dev.baharudin.weatherinfo.databinding.ItemSearchLocationBinding
import dev.baharudin.weatherinfo.domain.entities.Location
import java.util.Locale

class SearchLocationAdapter(
    private val locations: ArrayList<Location>,
    private val setOnClickListener: (Location) -> Unit,
) :
    RecyclerView.Adapter<SearchLocationAdapter.ViewHolder>() {
    class ViewHolder(var binding: ItemSearchLocationBinding) :
        RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemSearchLocationBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = locations.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val location = locations[position]
        with(holder.binding) {
            tvCity.text = location.city
            tvCounty.text = Locale("", location.country).displayCountry
            itemLocation.setOnClickListener {
                setOnClickListener(location)
            }
        }
    }
}