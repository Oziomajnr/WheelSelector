package ogbe.ozioma.com.wheelselector

import android.view.MotionEvent
import android.view.GestureDetector

open class OnSwipeListener : GestureDetector.SimpleOnGestureListener() {

    override fun onScroll(
            e1: MotionEvent,
            e2: MotionEvent,
            distanceX: Float,
            distanceY: Float
    ): Boolean {
        val x1 = e1.x
        val y1 = e1.y

        val x2 = e2.x
        val y2 = e2.y

        val direction = getDirection(x1, y1, x2, y2)
        return onSwipe(direction, e1, e2)
    }

    /** Override this method. The Direction enum will tell you how the user swiped.  */
    open fun onSwipe(direction: Direction, e1: MotionEvent, e2: MotionEvent): Boolean {
        return false
    }

    /**
     * Given two points in the plane p1=(x1, x2) and p2=(y1, y1), this method
     * returns the direction that an arrow pointing from p1 to p2 would have.
     * @param x1 the x position of the first point
     * @param y1 the y position of the first point
     * @param x2 the x position of the second point
     * @param y2 the y position of the second point
     * @return the direction
     */
    fun getDirection(x1: Float, y1: Float, x2: Float, y2: Float): Direction {
        val angle = getAngle(x1, y1, x2, y2)
        return Direction.fromAngle(angle)
    }

    /**
     *
     * Finds the angle between two points in the plane (x1,y1) and (x2, y2)
     * The angle is measured with 0/360 being the X-axis to the right, angles
     * increase counter clockwise.
     *
     * @param x1 the x position of the first point
     * @param y1 the y position of the first point
     * @param x2 the x position of the second point
     * @param y2 the y position of the second point
     * @return the angle between two points
     */
    fun getAngle(x1: Float, y1: Float, x2: Float, y2: Float): Double {

        val rad = Math.atan2((y1 - y2).toDouble(), (x2 - x1).toDouble()) + Math.PI
        return (rad * 180 / Math.PI + 180) % 360
    }


    enum class Direction {
        UP,
        DOWN,
        LEFT,
        RIGHT;


        companion object {

            /**
             * Returns a direction given an angle.
             * Directions are defined as follows:
             *
             * Up: [45, 135]
             * Right: [0,45] and [315, 360]
             * Down: [225, 315]
             * Left: [135, 225]
             *
             * @param angle an angle from 0 to 360 - e
             * @return the direction of an angle
             */
            fun fromAngle(angle: Double): Direction {
                return if (inRange(angle, 45f, 135f)) {
                    UP
                } else if (inRange(angle, 0f, 45f) || inRange(angle, 315f, 360f)) {
                    RIGHT
                } else if (inRange(angle, 225f, 315f)) {
                    DOWN
                } else {
                    LEFT
                }

            }

            /**
             * @param angle an angle
             * @param init the initial bound
             * @param end the final bound
             * @return returns true if the given angle is in the interval [init, end).
             */
            private fun inRange(angle: Double, init: Float, end: Float): Boolean {
                return angle >= init && angle < end
            }
        }
    }
}