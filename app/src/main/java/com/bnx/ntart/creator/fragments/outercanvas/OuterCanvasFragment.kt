package com.bnx.ntart.creator.fragments.outercanvas

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bnx.ntart.R
import com.bnx.ntart.creator.customviews.transparentbackgroundview.TransparentBackgroundView
import com.bnx.ntart.databinding.FragmentOuterCanvasBinding
import com.bnx.ntart.creator.fragments.canvas.CanvasFragment
import com.bnx.ntart.creator.utility.IntConstants

class OuterCanvasFragment(val spanCount: Int, private val isEmpty: Boolean = false) : Fragment() {
    lateinit var canvasFragment: CanvasFragment
    lateinit var cardViewParent: View
    lateinit var fragmentHost: View

    private fun instantiateVariables() {
        cardViewParent = binding.fragmentOuterCanvasCanvasFragmentHostCardViewParent
        fragmentHost = binding.fragmentOuterCanvasCanvasFragmentHost
        canvasFragment = CanvasFragment.newInstance(spanCount, isEmpty)
    }

    private fun showCanvas() {
        val transparent = TransparentBackgroundView(requireContext(), spanCount)
        binding.defsq2.addView(transparent)
        requireActivity().supportFragmentManager.beginTransaction().add(R.id.fragmentOuterCanvas_canvasFragmentHost, canvasFragment).commit()
    }

    fun getCurrentRotation(): Float {
        return binding.fragmentOuterCanvasCanvasFragmentHostCardViewParent.rotation
    }

    fun rotate(by: Int = IntConstants.DEGREES_NINETY, animate: Boolean = true, clockwise: Boolean = true) {
        val rotationAmount = if (clockwise) {
                (getCurrentRotation() + by)
        } else {
                (getCurrentRotation() - by)
        }

        if (animate) {
            binding.fragmentOuterCanvasCanvasFragmentHostCardViewParent
                .animate()
                .rotation(rotationAmount)
        } else {
            binding.fragmentOuterCanvasCanvasFragmentHostCardViewParent.rotation = rotationAmount
        }
    }

    companion object {
        fun newInstance(spanCount: Int, isEmpty: Boolean = false) = OuterCanvasFragment(spanCount, isEmpty)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding_ = FragmentOuterCanvasBinding.inflate(inflater, container, false)

        instantiateVariables()
        showCanvas()

        return binding.root
    }


    override fun onDestroyView() {
        super.onDestroyView()
        binding_ = null
    }
}