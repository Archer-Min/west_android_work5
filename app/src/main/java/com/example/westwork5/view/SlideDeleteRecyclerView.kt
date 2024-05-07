package com.example.westwork5.view

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.animation.LinearInterpolator
import android.widget.LinearLayout
import android.widget.Scroller
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.westwork5.adapter.MyAdapter

class SlideDeleteRecyclerView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
) : RecyclerView(context, attrs) {

    private var tvTop: TextView? = null
    private var tvDelete: TextView? = null
    private var mItemLayout: LinearLayout? = null
    private var mMaxLength: Int = 0
    private var mLastX: Int = 0
    private var mLastY: Int = 0
    private var mPosition: Int = 0
    private var isDragging: Boolean = false
    private var isItemMoving: Boolean = false
    private var isStartScroll: Boolean = false
    private var mMenuState: Int = 0
    private val MENU_CLOSED = 0
    private val MENU_WILL_CLOSED = 1
    private val MENU_OPEN = 2
    private val MENU_WILL_OPEN = 3
    private val mScroller: Scroller = Scroller(context, LinearInterpolator())
    private var mListener: OnItemActionListener? = null

    //监听 RecyclerView 上的触摸事件
    override fun onTouchEvent(e: MotionEvent): Boolean {
        val x = e.x.toInt()
        val y = e.y.toInt()
        when (e.action) {
            MotionEvent.ACTION_DOWN -> {
                if (mMenuState == MENU_CLOSED) {
                    val view: View? = findChildViewUnder(x.toFloat(), y.toFloat())
                    if (view == null) {
                        return false
                    }
                    val holder: MyAdapter.Holder = getChildViewHolder(view) as MyAdapter.Holder
                    mPosition = holder.adapterPosition
                    mItemLayout = holder.llLayout
                    tvDelete = holder.tvDelete
                    tvTop = holder.tvTop
                    //最大滑动距离
                    mMaxLength = tvDelete!!.width + tvTop!!.width
                    tvDelete?.setOnClickListener {
                        // 创建一个透明度动画
                        val anim = ObjectAnimator.ofFloat(mItemLayout, View.ALPHA, 1f, 0f)
                        anim.duration = 300 // 设置动画时长为0.3秒
                        anim.start()

                        anim.addListener(object : AnimatorListenerAdapter() {
                            override fun onAnimationEnd(animation: Animator?) {
                                // 执行删除操作
                                mItemLayout?.scrollTo(0, 0)
                                mMenuState = MENU_CLOSED
                                mListener?.OnItemDelete(mPosition)
                                // 恢复透明度
                                mItemLayout?.alpha = 1f
                            }
                        })
                    }
                    //置顶
                    tvTop!!.setOnClickListener {
                        mItemLayout!!.scrollTo(0, 0)
                        mMenuState = MENU_CLOSED
                        mListener?.OnItemTop(mPosition)
                    }
                } else if (mMenuState == MENU_OPEN) {
                    mScroller.startScroll(mItemLayout!!.scrollX, 0, -mMaxLength, 0, 200)
                    //点击按钮外区域恢复原来状态
                    invalidate()
                    mMenuState = MENU_CLOSED
                    return false
                } else {
                    return false
                }
            }
            MotionEvent.ACTION_MOVE -> {
                val dx = mLastX - x
                val dy = mLastY - y
                val scrollx = mItemLayout!!.scrollX
                if (Math.abs(dx) > Math.abs(dy)) {
                    isItemMoving = true
                    if (scrollx + dx <= 0) {
                        mItemLayout!!.scrollTo(0, 0)
                        return false
                    } else if (scrollx + dx >= mMaxLength) {
                        mItemLayout!!.scrollTo(mMaxLength, 0)
                        return false
                    }
                    mItemLayout!!.scrollBy(dx, 0)
                } else if (Math.abs(dx) > 30) {
                    return true
                }
                if (isItemMoving) {
                    mLastX = x
                    mLastY = y
                    return true
                }
            }
            MotionEvent.ACTION_UP -> {
                if (!isItemMoving && !isDragging && mListener != null) {
                    mListener!!.OnItemClick(mPosition)
                }
                isItemMoving = false
                var deltaX = 0
                val upScrollx = mItemLayout!!.scrollX
                if (upScrollx >= mMaxLength / 2) {
                    deltaX = mMaxLength - upScrollx
                    mMenuState = MENU_WILL_OPEN
                } else if (upScrollx < mMaxLength / 2) {
                    deltaX = -upScrollx
                    mMenuState = MENU_WILL_CLOSED
                }
                mScroller.startScroll(upScrollx, 0, deltaX, 0, 200)
                isStartScroll = true
                invalidate()
            }
        }
        mLastX = x
        mLastY = y
        return super.onTouchEvent(e)
    }

    override fun computeScroll() {
        if (mScroller.computeScrollOffset()) {
            mItemLayout!!.scrollTo(mScroller.currX, mScroller.currY)
            invalidate()
        } else if (isStartScroll) {
            isStartScroll = false
            if (mMenuState == MENU_WILL_CLOSED) {
                mMenuState = MENU_CLOSED
            }
            if (mMenuState == MENU_WILL_OPEN) {
                mMenuState = MENU_OPEN
            }
        }
    }

    override fun onScrollStateChanged(state: Int) {
        super.onScrollStateChanged(state)
        isDragging = state == SCROLL_STATE_DRAGGING
    }

    fun setOnItemActionListener(onItemActionListener: OnItemActionListener) {
        mListener = onItemActionListener
    }

    interface OnItemActionListener {
        fun OnItemClick(position: Int)
        fun OnItemTop(position: Int)
        fun OnItemDelete(position: Int)
    }
}

