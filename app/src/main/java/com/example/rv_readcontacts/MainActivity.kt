package com.example.rv_readcontacts

import android.database.Cursor
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.ContactsContract
import android.provider.MediaStore
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.CursorAdapter
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.rv_readcontacts.adapter.ContactAdapter
import com.example.rv_readcontacts.databinding.ActivityMainBinding
import com.example.rv_readcontacts.model.Contact
import com.facebook.shimmer.ShimmerFrameLayout

class MainActivity : AppCompatActivity() {
    private val array = ArrayList<String>()
    private lateinit var bin: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bin = ActivityMainBinding.inflate(layoutInflater)
        setContentView(bin.root)

        bin.recyclerview.layoutManager = GridLayoutManager(this, 1)

        bin.btnReadContact.setOnClickListener {
            initViews()
            bin.btnReadContact.visibility = View.GONE
            bin.autoCompleteText.visibility = View.VISIBLE
            bin.shimmerLayout.visibility = View.VISIBLE
            Handler(Looper.getMainLooper()).postDelayed({
                bin.shimmerLayout.visibility = View.GONE
                bin.shimmerLayout.stopShimmer()
                bin.recyclerview.visibility = View.VISIBLE
            }, 3000)
        }
    }

    private fun initViews() {
        val contactList: MutableList<Contact> = ArrayList()
        val contacts = contentResolver.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            null,
            null,
            null,
            null
        )
        while (contacts!!.moveToNext()) {
            val name =
                contacts.let { contacts.getString(it.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)) }
            val number =
                contacts.let { contacts.getString(it.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.NUMBER)) }

            contactList.add(Contact(name, number))
            array.add(name)
        }
        val arrayAdapter = ArrayAdapter(this, android.R.layout.simple_expandable_list_item_1, array)
        bin.autoCompleteText.setAdapter(arrayAdapter)
        bin.recyclerview.adapter = ContactAdapter(this, contactList)
        contacts.close()
    }
}
