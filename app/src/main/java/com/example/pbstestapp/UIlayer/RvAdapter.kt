package com.example.pbstestapp.UIlayer

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.pbstestapp.R
import com.example.pbstestapp.datalayer.Currency

class RvAdapter() :
    ListAdapter<Currency, RvAdapter.ViewHolder>(
        DifferentCallback()
    ) {


    private class DifferentCallback : DiffUtil.ItemCallback<Currency>() {
        override fun areItemsTheSame(oldItem: Currency, newItem: Currency) =
            oldItem == newItem

        override fun areContentsTheSame(oldItem: Currency, newItem: Currency) =
            oldItem == newItem
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.currency_card, parent, false)
        return ViewHolder(view)
    }



    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currency: Currency = getItem(position)
        holder.bind(
            currency.name,
            currency.value,
        )

    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private val nameView = view.findViewById<TextView>(R.id.TextView__Currency_Name)
        private val valueView = view.findViewById<TextView>(R.id.TextView__Currency_Value)

        fun bind(name: String, value: Float) {
            nameView.text = name
            valueView.text = value.toString()

        }
    }
}