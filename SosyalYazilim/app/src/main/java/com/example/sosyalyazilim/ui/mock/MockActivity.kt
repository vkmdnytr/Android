package com.example.sosyalyazilim.ui.mock

import android.arch.lifecycle.ViewModelProviders
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.widget.Toast
import com.example.sosyalyazilim.R
import com.example.sosyalyazilim.entity.model.data.MockResultItem
import com.example.sosyalyazilim.entity.model.sealed.Results
import com.example.sosyalyazilim.viewModel.MockViewModel
import kotlinx.android.synthetic.main.activity_mock.*



class MockActivity : AppCompatActivity() {


    private val viewModel by lazy {
        ViewModelProviders.of(this).get(MockViewModel::class.java)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mock)

        mockList.layoutManager = LinearLayoutManager(this)
        observeViewModel()
        viewModel.getMockListData()

        initToolbar()
    }

    private fun initToolbar() {
        toolbar.title = getString(R.string.action_mock_list)
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp)
        toolbar.setNavigationOnClickListener { finish() }
    }
    private fun observeViewModel() {

        viewModel.getProgresBarStateLiveData().observe(this, android.arch.lifecycle.Observer {
                        mockProgressBar.visibility=it!!
        })


        viewModel.getMockLiveData().observe(this, android.arch.lifecycle.Observer {
            when (it) {
                is Results.Success -> {
                    handleSuccess(it.data)
                }
                is Results.Error -> {
                    handleError()
                }
            }
        })
    }
    private fun handleSuccess(mDatasource: List<MockResultItem>?) {
       mockList.adapter=MockAdapter(mDatasource!!)
    }
    private fun handleError() {
        Toast.makeText(this, "HATA", Toast.LENGTH_SHORT).show()
    }



}


