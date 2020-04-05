package ogbe.ozioma.com.wheelselector

import android.view.View
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.OrientationHelper
import androidx.recyclerview.widget.RecyclerView

/**
 * A LinearSnapHelper that ignores item decorations to determine a view's center
 */
class CenterSnapHelper : LinearSnapHelper() {

    private var verticalHelper: OrientationHelper? = null
    private var horizontalHelper: OrientationHelper? = null
    private var scrolled = false
    private var recyclerView: RecyclerView? = null
    private val scrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            if (newState == RecyclerView.SCROLL_STATE_IDLE && scrolled) {
                if (recyclerView.layoutManager != null) {
                    val view = findSnapView(recyclerView.layoutManager)
                    if (view != null) {
                        val out = calculateDistanceToFinalSnap(recyclerView.layoutManager!!, view)
                        if (out != null) {
                            recyclerView.smoothScrollBy(out[0], out[1])
                        }
                    }
                }
                scrolled = false
            } else {
                scrolled = true
            }
        }
    }

    fun scrollTo(position: Int, smooth: Boolean) {
        if (recyclerView?.layoutManager != null) {
            val viewHolder = recyclerView!!.findViewHolderForAdapterPosition(position)
            if (viewHolder != null) {
                val distances = calculateDistanceToFinalSnap(recyclerView!!.layoutManager!!, viewHolder.itemView)
                if (smooth) {
                    recyclerView!!.smoothScrollBy(distances!![0], distances[1])
                } else {
                    recyclerView!!.scrollBy(distances!![0], distances[1])
                }
            } else {
                if (smooth) {
                    recyclerView!!.smoothScrollToPosition(position)
                } else {
                    recyclerView!!.scrollToPosition(position)
                }
            }
        }
    }

    override fun findSnapView(layoutManager: RecyclerView.LayoutManager?): View? {
        if (layoutManager == null) {
            return null
        }
        if (layoutManager.canScrollVertically()) {
            return findCenterView(layoutManager, getVerticalHelper(layoutManager))
        } else if (layoutManager.canScrollHorizontally()) {
            return findCenterView(layoutManager, getHorizontalHelper(layoutManager))
        }
        return null
    }

    override fun attachToRecyclerView(recyclerView: RecyclerView?) {
        this.recyclerView = recyclerView
        recyclerView?.addOnScrollListener(scrollListener)
    }

    override fun calculateDistanceToFinalSnap(
        layoutManager: RecyclerView.LayoutManager,
        targetView: View
    ): IntArray? {
        val out = IntArray(2)
        if (layoutManager.canScrollHorizontally()) {
            out[0] = distanceToCenter(layoutManager, targetView, getHorizontalHelper(layoutManager))
        } else {
            out[0] = 0
        }
        if (layoutManager.canScrollVertically()) {
            out[1] = distanceToCenter(layoutManager, targetView, getVerticalHelper(layoutManager))
        } else {
            out[1] = 0
        }
        return out
    }

    private fun findCenterView(
        layoutManager: RecyclerView.LayoutManager,
        helper: OrientationHelper
    ): View? {
        val childCount = layoutManager.childCount
        if (childCount == 0) {
            return null
        }
        var closestChild: View? = null
        val center: Int = if (layoutManager.clipToPadding) {
            helper.startAfterPadding + helper.totalSpace / 2
        } else {
            helper.end / 2
        }
        var absClosest = Integer.MAX_VALUE

        for (i in 0 until childCount) {
            val child = layoutManager.getChildAt(i)
            val childCenter = if (helper == horizontalHelper) {
                (child!!.x + child.width / 2).toInt()
            } else {
                (child!!.y + child.height / 2).toInt()
            }
            val absDistance = Math.abs(childCenter - center)

            if (absDistance < absClosest) {
                absClosest = absDistance
                closestChild = child
            }
        }
        return closestChild
    }

    private fun distanceToCenter(
        layoutManager: RecyclerView.LayoutManager,
        targetView: View,
        helper: OrientationHelper
    ): Int {
        val childCenter = if (helper == horizontalHelper) {
            (targetView.x + targetView.width / 2).toInt()
        } else {
            (targetView.y + targetView.height / 2).toInt()
        }
        val containerCenter = if (layoutManager.clipToPadding) {
            helper.startAfterPadding + helper.totalSpace / 2
        } else {
            helper.end / 2
        }
        return childCenter - containerCenter
    }

    private fun getVerticalHelper(layoutManager: RecyclerView.LayoutManager): OrientationHelper {
        if (verticalHelper == null || verticalHelper!!.layoutManager !== layoutManager) {
            verticalHelper = OrientationHelper.createVerticalHelper(layoutManager)
        }
        return verticalHelper!!
    }

    private fun getHorizontalHelper(
        layoutManager: RecyclerView.LayoutManager
    ): OrientationHelper {
        if (horizontalHelper == null || horizontalHelper!!.layoutManager !== layoutManager) {
            horizontalHelper = OrientationHelper.createHorizontalHelper(layoutManager)
        }
        return horizontalHelper!!
    }
}