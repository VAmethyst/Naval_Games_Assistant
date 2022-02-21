package com.example.navalgamesassistant

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.navalgamesassistant.databinding.FragmentConverterBinding

class ConverterFragment : Fragment(), View.OnClickListener {
    lateinit var binding: FragmentConverterBinding
    private var enteredValue: Double = 0.0
    private var selectedId: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentConverterBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        getRadioId()
        setListeners()
    }

    private fun setListeners() {
        binding.checkedValue.setOnCheckedChangeListener { buttonView, isChecked -> getRadioId() }
        binding.buttonResult.setOnClickListener { onClickResult() }
        binding.buttonNull.setOnClickListener(this)
        binding.buttonMinus.setOnClickListener(this)
        binding.buttonPlus.setOnClickListener(this)
        binding.buttonMinus1.setOnClickListener(this)
        binding.buttonMinus10.setOnClickListener(this)
        binding.buttonPlus1.setOnClickListener(this)
        binding.buttonPlus10.setOnClickListener(this)
    }

    private fun getRadioId() {
        /*Смена подсказки EditText от выбранной кнопки*/
        selectedId = binding.checkedValue.checkedRadioButtonId
        when (selectedId) {
            R.id.check_inch -> {
                binding.editTextLayout.hint = resources.getString(R.string.input_in)
            }
            else -> {
                binding.editTextLayout.hint = resources.getString(R.string.input_mm)
            }
        }
    }

    override fun onClick(v: View?) {
        val buttonIndex = translateIdToIndex(v!!.id)
        enteredValue = binding.editTextValue.text.toString().toDouble()
        when (selectedId) {
            R.id.check_inch -> {
                when (buttonIndex) {
                    1 -> enteredValue -= 0.1
                    2 -> enteredValue += 0.1
                    4 -> enteredValue -= 1.0
                    5 -> enteredValue -= 10.0
                    6 -> enteredValue += 1.0
                    7 -> enteredValue += 10.0
                    else -> enteredValue = 0.0
                }
            }
            else -> {
                when (buttonIndex) {
                    1 -> enteredValue -= 0.25
                    2 -> enteredValue += 0.25
                    4 -> enteredValue -= 1.0
                    5 -> enteredValue -= 10.0
                    6 -> enteredValue += 1.0
                    7 -> enteredValue += 10.0
                    else -> enteredValue = 0.0
                }
            }
        }
        enteredValue = String.format("%.2f", enteredValue).toDouble()
        binding.editTextValue.setText(enteredValue.toString())
    }

    private fun translateIdToIndex(id: Int): Int {
        var index = -1
        when (id) {
            R.id.button_minus   -> index = 1
            R.id.button_plus    -> index = 2
            R.id.button_null    -> index = 3
            R.id.button_minus1  -> index = 4
            R.id.button_minus10 -> index = 5
            R.id.button_plus1   -> index = 6
            R.id.button_plus10  -> index = 7
        }
        return index
    }

    private fun onClickResult() {
        if (!isFieldCorrect()) {
            val result = getString(R.string.text_result) + getResult()
            binding.tvResult.text = result
        }
    }

    private fun getResult(): String {
        enteredValue = binding.editTextValue.text.toString().toDouble()
        val converterValue = when (selectedId) {
            R.id.check_inch -> 25.4
            else -> 0.0394
        }
        var targetValue = (enteredValue * converterValue)
        targetValue = String.format("%.3f", targetValue).toDouble()
        val targetValue2 = targetValue.toString()

        val unit = when (selectedId) {
            R.id.check_inch -> getString(R.string.millimetre_symbol)
            else -> getString(R.string.inch_symbol)
        }
        return targetValue2 + unit
    }

    private fun isFieldCorrect(): Boolean {
        binding.apply {
            if (editTextValue.text.isNullOrEmpty()) {
                editTextValue.error = getString(R.string.edit_field_empty)
                return editTextValue.text.isNullOrEmpty()
            } else if (editTextValue.text.toString().toDouble() < 0.0) {
                editTextValue.error = getString(R.string.edit_field_non_positive)
                return editTextValue.text.toString().toDouble() < 0.0
            } else return false
        }
    }

}