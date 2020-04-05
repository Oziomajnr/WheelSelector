package ogbe.ozioma.com.wheelselector

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.*
import android.widget.FrameLayout
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.*
import ogbe.ozioma.com.wheelselector.databinding.LayoutSpeedSelectorBinding

class PlayerSpeedSelector : FrameLayout {

    lateinit var binding: LayoutSpeedSelectorBinding

    private val snapHelper = CenterSnapHelper()

    private var selectedValue = WheelSelectorItem(1f)
    private val wheelSelectorAdapter = WheelSelectorAdapter()
    private var items: List<WheelSelectorItem> = emptyList()

    private val gestureDetector =
        GestureDetector(context, object : OnSwipeListener() {
            override fun onSwipe(direction: Direction, e1: MotionEvent, e2: MotionEvent): Boolean {
                if (direction == Direction.LEFT || direction == Direction.RIGHT) {
                    return true
                }
                return super.onSwipe(direction, e1, e2)
            }
        })

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        loadLayout(context)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        loadLayout(context)
    }

    private fun loadLayout(context: Context) {
        val inflater = LayoutInflater.from(context)
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.layout_speed_selector,
            this,
            true
        )
    }

    fun setItems(items: List<WheelSelectorItem>) {
        this.items = items
        wheelSelectorAdapter.submitList(items)
    }

    /**
     * Scroll to an item if it exists in the list of items
     */
    fun selectedItem(selectedItem: WheelSelectorItem) {
        val index = items.indexOfFirst { it.value == selectedItem.value }
        if (index >= 0) {
            binding.speedScrollerRecyclerView.postDelayed({
                binding.speedScrollerRecyclerView.snapScrolltoPosition(index, snapHelper, true)
            }, 50)
        }
    }


    @SuppressLint("ClickableViewAccessibility")
    override fun onFinishInflate() {
        super.onFinishInflate()
        binding.speedScrollerRecyclerView.setOnTouchListener { v, event ->
            gestureDetector.onTouchEvent(event)
            false
        }
        binding.speedScrollerRecyclerView.apply {
            adapter = wheelSelectorAdapter
            layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
            addItemDecoration(CenterDecoration(0))
            snapHelper.attachToRecyclerView(this)
            attachSnapHelperWithListener(snapHelper = snapHelper,
                onSnapPositionChangeListener = object : OnSnapPositionChangeListener {
                    override fun onSnapPositionChange(position: Int) {
                        selectedValue = items[position]
                        binding.selectedValueTextView.text = "X"
                    }
                })
        }

        wheelSelectorAdapter.submitList(items)
    }
}

interface SpeedSelectorEvent {
    fun onItemSelected(wheelSelectorItem: WheelSelectorItem)
}