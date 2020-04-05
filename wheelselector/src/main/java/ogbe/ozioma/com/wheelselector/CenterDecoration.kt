package ogbe.ozioma.com.wheelselector

import android.graphics.Rect
import android.view.View
import androidx.annotation.Px
import androidx.core.view.doOnPreDraw
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class CenterDecoration(@Px private val spacing: Int) : RecyclerView.ItemDecoration() {

    private var firstViewWidth = -1
    private var lastViewWidth = -1

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        super.getItemOffsets(outRect, view, parent, state)
        val adapterPosition = (view.layoutParams as RecyclerView.LayoutParams).viewAdapterPosition
        val lm = parent.layoutManager as LinearLayoutManager
        if (adapterPosition == 0) {
            // Invalidate decorations when this view width has changed
            if (view.width != firstViewWidth) {
                view.doOnPreDraw { parent.invalidateItemDecorations() }
            }
            firstViewWidth = view.width
            outRect.left = parent.width / 2 - view.width / 2
            // If we have more items, use the spacing provided
            if (lm.itemCount > 1) {
                outRect.right = spacing / 2
            } else {
                // Otherwise, make sure this to fill the whole width with the decoration
                outRect.right = outRect.left
            }
        } else if (adapterPosition == lm.itemCount - 1) {
            // Invalidate decorations when this view width has changed
            if (view.width != lastViewWidth) {
                view.doOnPreDraw { parent.invalidateItemDecorations() }
            }
            lastViewWidth = view.width
            outRect.right = parent.width / 2 - view.width / 2
            outRect.left = spacing / 2
        } else {
            outRect.left = spacing / 2
            outRect.right = spacing / 2
        }
    }

}