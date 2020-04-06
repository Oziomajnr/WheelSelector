package ogbe.ozioma.com.wheelselector

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView


class WheelSelectorAdapter(private val barItemModel: BarItemModel) : ListAdapter<WheelSelectorItem,
        WheelSelectorAdapter.ViewHolder>(PodcastDiffCallback()) {

    private var actualNumberOfItems: Int? = null

    companion object {
        class PodcastDiffCallback : DiffUtil.ItemCallback<WheelSelectorItem>() {
            override fun areItemsTheSame(oldItem: WheelSelectorItem, newItem: WheelSelectorItem) =
                oldItem == newItem

            override fun areContentsTheSame(
                oldItem: WheelSelectorItem,
                newItem: WheelSelectorItem
            ) = oldItem == newItem
        }
    }


    override fun submitList(list: List<WheelSelectorItem>?) {
        actualNumberOfItems = list?.size
        super.submitList(list)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view: View =
            inflater.inflate(R.layout.wheel_selector_item, parent, false)
        return ViewHolder(view, barItemModel)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
        holder.bind(getItem(position))

    class ViewHolder(view: View, barItemModel: BarItemModel) :
        RecyclerView.ViewHolder(view) {
        private val valueTextView: TextView by lazy {
            view.findViewById<TextView>(R.id.value_text_view)
        }

        private val itemLineView: View by lazy {
            view.findViewById<View>(R.id.item_line_view)
        }

        init {
            itemLineView.setBackgroundColor(barItemModel.barColor)
            itemLineView.layoutParams.height = barItemModel.barHeight
            itemLineView.layoutParams.width = barItemModel.barThickness
        }

        fun bind(item: WheelSelectorItem) {
            if (item.highlight) {
                valueTextView.text = item.value.toString()
            } else {
                valueTextView.text = ""
            }
        }
    }
}