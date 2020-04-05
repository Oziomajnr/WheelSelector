package ogbe.ozioma.com.wheelselector

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


class WheelSelectorView : FrameLayout {


    private lateinit var speedScrollerRecyclerView: RecyclerView
    private lateinit var selectedValueText: TextView
    private val snapHelper = CenterSnapHelper()

    private var selectedValue: WheelSelectorItem = WheelSelectorItem(1f)
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
        val inflatedView: View = inflater.inflate(R.layout.layout_speed_selector, this, true)
        speedScrollerRecyclerView = inflatedView.findViewById(R.id.speed_scroller_recycler_view)
        selectedValueText = inflatedView.findViewById(R.id.selected_value_text_view)
    }

    fun setItems(items: List<WheelSelectorItem>) {
        this.items = items
        wheelSelectorAdapter.submitList(items)
    }

    /**
     * Scroll to an item if it exists in the list of items
     */
    fun setSelectedItem(selectedItem: WheelSelectorItem) {
        val index = items.indexOfFirst { it.value == selectedItem.value }
        if (index >= 0) {
            speedScrollerRecyclerView.postDelayed({
                speedScrollerRecyclerView.snapScrolltoPosition(index, snapHelper, true)
            }, 50)
        }
    }


    @SuppressLint("ClickableViewAccessibility")
    override fun onFinishInflate() {
        super.onFinishInflate()
        speedScrollerRecyclerView.setOnTouchListener { v, event ->
            gestureDetector.onTouchEvent(event)
            false
        }
        speedScrollerRecyclerView.apply {
            adapter = wheelSelectorAdapter
            layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
            addItemDecoration(CenterDecoration(0))
            snapHelper.attachToRecyclerView(this)
            attachSnapHelperWithListener(snapHelper = snapHelper,
                onSnapPositionChangeListener = object : OnSnapPositionChangeListener {
                    override fun onSnapPositionChange(position: Int) {
                        selectedValue = items[position]
                        selectedValueText.text = "X"
                    }
                })
        }
    }
}

interface SpeedSelectorEvent {
    fun onItemSelected(wheelSelectorItem: WheelSelectorItem)
}