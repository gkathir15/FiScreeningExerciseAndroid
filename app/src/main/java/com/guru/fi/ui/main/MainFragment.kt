package com.guru.fi.ui.main

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.Spannable
import android.text.SpannableString
import android.text.TextWatcher
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModelProvider
import com.guru.fi.R
import com.guru.fi.ResultActivity
import com.guru.fi.databinding.MainFragmentBinding
import com.guru.fi.model.Date
import com.guru.fi.model.UserDetail
import java.lang.Exception
import java.util.regex.Matcher
import java.util.regex.Pattern


class MainFragment : Fragment() {
    private var _binding: MainFragmentBinding? = null

    companion object {
        fun newInstance() = MainFragment()
    }

    private lateinit var viewModel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = MainFragmentBinding.inflate(inflater, container, false)
        return _binding!!.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)


        val spannable = SpannableString(getString(R.string.providing_pan_amp_date_of_birth_helps_us_find_and_fetch_your_kyc_from_a_central_registry_by_the_government_of_india_learn_more))
        spannable.setSpan(
            ForegroundColorSpan(resources.getColor(R.color.button)),
            115, // start
            125, // end
            Spannable.SPAN_EXCLUSIVE_INCLUSIVE
        )
        _binding?.info?.text = spannable

        _binding?.panNo?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                viewModel.panNumber.value= s.toString()
            }
        })

        _binding?.dateEt?.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(s: Editable) {
                viewModel.date.value = viewModel.date.value.also {
                    it?.dd = s.toString().toIntSafe()
                }
            }
        })
        _binding?.monthEt?.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(s: Editable) {
                viewModel.date.value = viewModel.date.value.also {
                    it?.mm = s.toString().toIntSafe()
                }
            }
        })
        _binding?.yearEt?.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(s: Editable) {
                viewModel.date.value = viewModel.date.value.also {
                    it?.yyyy = s.toString().toIntSafe()
                }
            }
        })

        viewModel.panNumber.observe(viewLifecycleOwner,{
            _binding?.next?.isEnabled = it.validatePan()&&viewModel.date.value.validateDate()
        })
        viewModel.date.observe(viewLifecycleOwner,{
            _binding?.next?.isEnabled = it.validateDate()&& viewModel.panNumber.value?.validatePan() == true
        })

        _binding?.dntHavPan?.setOnClickListener {
            activity?.finish()
            startActivity(Intent(requireActivity(),ResultActivity::class.java).also {
                it.putExtra("result","dont have pan clicked")
            })
        }
        _binding?.next?.setOnClickListener {

            Toast.makeText(requireContext(),"Details submitted successfully",Toast.LENGTH_SHORT).show()

            activity?.finish()
            startActivity(Intent(requireActivity(),ResultActivity::class.java).also {
                it.putExtra("result","Details submitted successfully")
            })
        }


    }



}

private fun String.toIntSafe(): Int? {
      var value:Int? = null
    try {
        value = this.toInt()
    }catch (e:Exception)
    {

    }finally {
        return value
    }
}


fun String.validatePan(): Boolean {
    //sample "ABCDE1234F"

    val pattern="[A-Z]{5}[0-9]{4}[A-Z]{1}".toRegex()

      return pattern.matches(this)
}

 fun Date?.validateDate(): Boolean {
    val currDate = java.util.Date()

    return if(this==null)
        false
    else {
            if(this.yyyy!=null&&this.mm!=null&&this.dd!=null) {
                if (this.yyyy!! > 1900 && this.yyyy!! <= currDate.year+1900) {
                    if (this.mm!! < 12 && this.mm!! <= currDate.month) {
                        this.dd!! < 31 && this.dd!! <= currDate.date
                    } else {
                        false
                    }
                } else {
                    false
                }
            }else{
                return false
            }
    }

}

