package com.appsontap.bernie2020.debt_calculator

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.OvershootInterpolator
import androidx.annotation.LayoutRes
import androidx.constraintlayout.widget.ConstraintSet
import androidx.transition.ChangeBounds
import androidx.transition.TransitionManager
import com.appsontap.bernie2020.BaseFragment
import com.appsontap.bernie2020.R
import com.appsontap.bernie2020.favorites.FavoritesFragment
import kotlinx.android.synthetic.main.fragment_debt_calculator.*

class DebtCalculatorFragment : BaseFragment() {


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_debt_calculator, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        button_debt_calc_calculate.setOnClickListener {
            updateConstraints(R.layout.fragment_debt_calculator_calculating)
        }
    }

    fun updateConstraints(@LayoutRes id: Int) {
        val newConstraintSet = ConstraintSet()
        newConstraintSet.clone(requireContext(), id)
        newConstraintSet.applyTo(constraint_layout_debt_calc_root)
        val transition = ChangeBounds()
        transition.interpolator = OvershootInterpolator()
        TransitionManager.beginDelayedTransition(constraint_layout_debt_calc_root, transition)
    }

    companion object {
        fun newInstance(): DebtCalculatorFragment {
            return DebtCalculatorFragment()
        }
    }
}