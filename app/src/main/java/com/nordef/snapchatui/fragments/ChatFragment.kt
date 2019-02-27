package com.nordef.snapchatui.fragments

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.nordef.snapchatui.R
import com.nordef.snapchatui.view.NonScrollListView

class ChatFragment : Fragment() {
    internal lateinit var view: View
    internal lateinit var context: Context

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        view = inflater.inflate(R.layout.fragment_chat, container, false)
        context = inflater.context

        initListView()

        return view
    }

    private fun initListView() {

        val listView: NonScrollListView = view.findViewById(R.id.listView)
        val listAdapter: ArrayAdapter<String>
        val arrayList = ArrayList<String>()
        for (i in 1..50) {
            arrayList.add("Item $i")
        }
        listAdapter = ArrayAdapter(context, R.layout.support_simple_spinner_dropdown_item, arrayList)
        listView.adapter = listAdapter

    }

}