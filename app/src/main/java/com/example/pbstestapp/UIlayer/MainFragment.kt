package com.example.pbstestapp.UIlayer

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pbstestapp.R
import com.example.pbstestapp.domain.InternetConnectionChecker
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Calendar

private const val UPDATE_DELAY_TIME: Long = 30000

class MainFragment : Fragment() {

    private var sharedPref: SharedPreferences? = null


    private val viewModel = MainFragmentViewModel()
    private val adapterRv = RvAdapter()
    private var needToStopUpdating = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.fragment_layout, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView = view.findViewById<RecyclerView>(R.id.RecyclerView__currency_list)
        recyclerView.apply {
            layoutManager = GridLayoutManager(context, 1)
            adapter = adapterRv
        }

        val updateButton = view.findViewById<Button>(R.id.button)
        updateButton.setOnClickListener { loadData(view, context) }

        val lastUpdateDate = view.findViewById<TextView>(R.id.textView__last_update)
        sharedPref?.let {
            lastUpdateDate.text = it.getString(getString(R.string.last_update_date), "")
        }

    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        sharedPref = context.getSharedPreferences("pref", Context.MODE_PRIVATE)
    }

    override fun onResume() {
        super.onResume()
        view?.let { loadData(it, context) }
        needToStopUpdating = false

        var needToStartTimer = true

        val timer = object : CountDownTimer(UPDATE_DELAY_TIME, 1000) {

            override fun onTick(millisUntilFinished: Long) {
                if (needToStopUpdating) cancel() //Останавливаем таймер, если сработал OnStop
            }

            override fun onFinish() {
                view?.let { loadData(it, context) }
                needToStartTimer = true
            }
        }
        viewModel.viewModelScope.launch {
            withContext(Dispatchers.IO)
            {
                while (!needToStopUpdating) { //Луп останавливается только в методе onStop
                    if (needToStartTimer) { //Когда таймер останавливается флаг needToStartTimer меняется и таймер стартует заново
                        needToStartTimer = false
                        timer.start()
                    }
                }
            }
        }
    }

    override fun onStop() {
        super.onStop()
        needToStopUpdating = true
    }

    @SuppressLint("SimpleDateFormat")
    private fun loadData(
        view: View,
        context: Context?,
    ) {
        val progressBar: ProgressBar = view.findViewById(R.id.progressBar)
        val errorTextView: TextView = view.findViewById(R.id.textView__error_text)
        val recyclerView = view.findViewById<RecyclerView>(R.id.RecyclerView__currency_list)
        val lastUpdateDate = view.findViewById<TextView>(R.id.textView__last_update)
        val updateButton = view.findViewById<Button>(R.id.button)

        progressBar.visibility = View.VISIBLE
        recyclerView.visibility = View.GONE
        errorTextView.visibility = View.GONE
        updateButton.visibility = View.GONE

        if (InternetConnectionChecker.isInternetAvailable(context)) {
            try {
                viewModel.viewModelScope.launch {
                    viewModel.currencyList.onEach {
                        adapterRv.submitList(it)
                    }.collect()
                    progressBar.visibility = View.GONE
                    recyclerView.visibility = View.VISIBLE

                    val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm")
                    val current = formatter.format(Calendar.getInstance().time)
                    lastUpdateDate.text = current

                    with(sharedPref!!.edit()) {
                        putString(getString(R.string.last_update_date), current)
                        apply()
                    }

                }
            } catch (error: Throwable) {
                progressBar.visibility = View.GONE
                errorTextView.visibility = View.VISIBLE
                errorTextView.text = error.printStackTrace().toString()

            }
        } else {
            progressBar.visibility = View.GONE
            errorTextView.visibility = View.VISIBLE
            errorTextView.text = "Проверьте подключение к интернету"
            updateButton.visibility = View.VISIBLE
        }


    }

    companion object {
        fun newInstance() = MainFragment()
    }
}
