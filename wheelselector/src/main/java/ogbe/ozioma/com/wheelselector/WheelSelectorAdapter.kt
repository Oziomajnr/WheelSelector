package ogbe.ozioma.com.wheelselector

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ogbe.ozioma.com.wheelselector.databinding.WheelSelectorItemBinding


class WheelSelectorAdapter : ListAdapter<WheelSelectorItem,
        WheelSelectorAdapter.ViewHolder>(PodcastDiffCallback()) {

    private var actualNumberOfItems: Int? = null

    companion object {
        class PodcastDiffCallback : DiffUtil.ItemCallback<WheelSelectorItem>() {
            override fun areItemsTheSame(oldItem: WheelSelectorItem, newItem: WheelSelectorItem) = oldItem == newItem

            override fun areContentsTheSame(oldItem: WheelSelectorItem, newItem: WheelSelectorItem) = oldItem == newItem
        }
    }


    override fun submitList(list: List<WheelSelectorItem>?) {
        actualNumberOfItems = list?.size
        super.submitList(list)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        DataBindingUtil.inflate<WheelSelectorItemBinding>(LayoutInflater.from(parent.context), R.layout.wheel_selector_item, parent, false)
                .run {
                    return ViewHolder(this)
                }
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(getItem(position))

    class ViewHolder(val binding: WheelSelectorItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: WheelSelectorItem) {
            binding.executePendingBindings()
            if (item.highlight) {
                binding.valueTextView.text = item.value.toString()
            } else {
                binding.valueTextView.text = ""
            }
        }
    }
}