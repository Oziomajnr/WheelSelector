package ogbe.ozioma.com.wheelselector

import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SnapHelper


fun SnapHelper.getSnapPosition(recyclerView: RecyclerView): Int {
    val layoutManager = recyclerView.layoutManager ?: return RecyclerView.NO_POSITION
    val snapView = findSnapView(layoutManager) ?: return RecyclerView.NO_POSITION
    return layoutManager.getPosition(snapView)
}

fun RecyclerView.attachSnapHelperWithListener(
    snapHelper: SnapHelper,
    behavior: SnapOnScrollListener.Behavior = SnapOnScrollListener.Behavior.NOTIFY_ON_SCROLL,
    onSnapPositionChangeListener: OnSnapPositionChangeListener
) {
    snapHelper.attachToRecyclerView(this)
    val snapOnScrollListener = SnapOnScrollListener(
        snapHelper,
        behavior,
        onSnapPositionChangeListener
    )
    addOnScrollListener(snapOnScrollListener)
}

fun RecyclerView.snapScrolltoPosition(position: Int, snapHelper: SnapHelper, smoothScroll: Boolean = false) {
    val recyclerLayoutManager = layoutManager
        ?: kotlin.error("Layout manager must be set before scrolling")
    if (smoothScroll) {
        smoothScrollToPosition(position)
    } else {
        scrollToPosition(position)
    }
    post {
        val view = recyclerLayoutManager.findViewByPosition(position)
        view?.let {
            val snapDistance = snapHelper.calculateDistanceToFinalSnap(recyclerLayoutManager, view)
            if (snapDistance!![0] != 0 || snapDistance[1] != 0) {
                if (smoothScroll) {
                    smoothScrollBy(snapDistance[0], snapDistance[1])
                } else {
                    scrollBy(snapDistance[0], snapDistance[1])
                }
            }
        }
    }
}