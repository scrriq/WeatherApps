package com.example.weatherapp.adapters

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Parcel
import android.os.Parcelable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapp.Models.DataModel
import com.example.weatherapp.Models.DefaultWeatherModel
import com.example.weatherapp.R
import com.example.weatherapp.databinding.ListItemBinding
import com.squareup.picasso.Picasso


class WeatherAdapter(val listner: Listner?) : ListAdapter<DefaultWeatherModel, WeatherAdapter.Holder>(Comporator()){
    class Holder(view: View, val listner: Listner?) : RecyclerView.ViewHolder(view){
        val binding = ListItemBinding.bind(view)
        var itemTemp: DefaultWeatherModel? = null
        init{
            itemView.setOnClickListener{
                itemTemp?.let { it1 -> listner?.onClick(it1) }
            }
        }

        fun bind(item: DefaultWeatherModel) = with(binding){
            itemTemp = item
            tvDate.text = item.time
            tvCondition.text = item.condition
            tvTemp.text = item.currentTemp.ifEmpty {"${item.maxTemp}°С / ${item.minTemp}°С"}
            Picasso.get().load("https:" + item.imageUrl).into(im)
        }

    }

    class Comporator: DiffUtil.ItemCallback<DefaultWeatherModel>() {
        override fun areItemsTheSame(oldItem: DefaultWeatherModel, newItem: DefaultWeatherModel): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: DefaultWeatherModel, newItem: DefaultWeatherModel): Boolean {
            return oldItem == newItem
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)
        return Holder(view, listner)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(getItem(position))
    }

    interface Listner {
        fun onClick(item: DefaultWeatherModel){
        }
    }

}