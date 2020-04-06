package ogbe.ozioma.com.wheelselector

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


class WheelSelectorView : FrameLayout {

    var itemSelectedEvent: SpeedSelectorEvent? = null
    private lateinit var speedScrollerRecyclerView: RecyclerView
    private lateinit var selectedValueText: TextView

    private val snapHelper = CenterSnapHelper()

    private var selectedItem: WheelSelectorItem? = null

    private lateinit var wheelSelectorAdapter: WheelSelectorAdapter
    private var items: List<WheelSelectorItem> = emptyList()

    private lateinit var selectedValueUnit: String

    private var itemBarColor: Int = ContextCompat.getColor(context, R.color.black)
    private  var markerDrawable: Drawable? = null


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
        loadLayout(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        loadLayout(context, attrs, defStyleAttr)
    }

    private fun loadLayout(context: Context, attrs: AttributeSet, defStyleAttr: Int? = null) {
        val inflater = LayoutInflater.from(context)
        val inflatedView: View = inflater.inflate(R.layout.layout_speed_selector, this, true)
        val typedArray = context.obtainStyledAttributes(
            attrs,
            R.styleable.WheelSelectorView,
            defStyleAttr ?: 0,
            0
        )
        try {
            selectedValueUnit =
                typedArray.getString(R.styleable.WheelSelectorView_selectedMarkerUnit) ?: ""
            itemBarColor = typedArray.getColor(
                R.styleable.WheelSelectorView_itemBarColor,
                ContextCompat.getColor(context, R.color.black)
            )

            markerDrawable = typedArray.getDrawable(
                R.styleable.WheelSelectorView_selectedMarkerDrawable
            ) ?: ContextCompat.getDrawable(context, R.drawable.arrow_down)
        } finally {
            typedArray.recycle()
        }

        speedScrollerRecyclerView = inflatedView.findViewById(R.id.speed_scroller_recycler_view)
        selectedValueText = inflatedView.findViewById(R.id.selected_value_text_view)
        selectedValueText.setCompoundDrawablesWithIntrinsicBounds(null, null, null, markerDrawable)
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
            wheelSelectorAdapter = WheelSelectorAdapter(BarItemModel(64f, itemBarColor))
            adapter = wheelSelectorAdapter
            layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
            addItemDecoration(CenterDecoration(0))
            snapHelper.attachToRecyclerView(this)
            attachSnapHelperWithListener(snapHelper = snapHelper,
                onSnapPositionChangeListener = object : OnSnapPositionChangeListener {
                    override fun onSnapPositionChange(position: Int) {
                        selectedItem = items[position]
                        itemSelectedEvent?.onItemSelected(items[position])
                        selectedValueText.text = context.resources.getString(
                            R.string.selected_item_text,
                            selectedItem?.value.toString(),
                            selectedValueUnit
                        )
                    }
                })
        }
    }
}

interface SpeedSelectorEvent {
    fun onItemSelected(wheelSelectorItem: WheelSelectorItem)
}