package com.pbarbieri.mobiledevelopertest.adapters

import android.content.Context
import android.graphics.*
import android.graphics.drawable.ColorDrawable
import android.view.View
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView


abstract class SwipeToDeleteCallback(context: Context, private val delete_string: String) : ItemTouchHelper.Callback() {
    private var mainContext: Context = context
    private var clearPaint: Paint
    private var background: ColorDrawable = ColorDrawable()
    private var backgroundColor = 0

    init{
        backgroundColor = Color.parseColor("#ff0000")
        clearPaint = Paint()
        clearPaint.xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)
    }

    override fun getMovementFlags(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder): Int {
        val swipeFlag = ItemTouchHelper.LEFT
        return makeMovementFlags(0, swipeFlag)
    }

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        return false
    }

    override fun onChildDraw(
        canvas: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        super.onChildDraw(canvas, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
        val itemView: View = viewHolder.itemView
        val itemHeight: Int = itemView.height

        background.color = backgroundColor
        background.setBounds(
            itemView.right + dX.toInt(),
            itemView.top,
            itemView.right,
            itemView.bottom
        )
        background.draw(canvas)

        val textPaint = Paint()
        textPaint.color = Color.WHITE
        textPaint.textSize = 24 * mainContext.resources.displayMetrics.density

        val marginTop: Float = (itemView.top + itemHeight / 2).toFloat()
        val marginLeft: Float = itemView.right - 100 * mainContext.resources.displayMetrics.density

        canvas.drawText(delete_string, marginLeft, marginTop, textPaint)
        super.onChildDraw(canvas, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
    }

    override fun getSwipeThreshold(viewHolder: RecyclerView.ViewHolder): Float {
        return 0.3f
    }
}