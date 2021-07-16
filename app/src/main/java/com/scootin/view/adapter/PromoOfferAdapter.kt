package com.scootin.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.scootin.R

class PromoOfferAdapter(private var img:List<Int>):
    RecyclerView.Adapter<PromoOfferAdapter.Pager2ViewHolder>() {

    inner class Pager2ViewHolder(item: View) : RecyclerView.ViewHolder(item) {
    val itemImg:ImageView=item.findViewById(R.id.promo_img)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PromoOfferAdapter.Pager2ViewHolder {
        return Pager2ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.adapter_promo_offers,parent,false))
    }

    override fun onBindViewHolder(holder: PromoOfferAdapter.Pager2ViewHolder, position: Int) {
     holder.itemImg.setImageResource(img[position])
    }

    override fun getItemCount(): Int {
    return 5
    }

}