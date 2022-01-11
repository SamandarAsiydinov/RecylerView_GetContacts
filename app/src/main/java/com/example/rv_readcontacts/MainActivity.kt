package com.example.rv_readcontacts

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.ContactsContract
import android.view.View
import android.widget.ArrayAdapter
import androidx.recyclerview.widget.GridLayoutManager
import com.example.rv_readcontacts.adapter.ContactAdapter
import com.example.rv_readcontacts.databinding.ActivityMainBinding
import com.example.rv_readcontacts.model.Contact

class MainActivity : AppCompatActivity() {

    companion object {
        const val PERMISSIONS_REQUEST_READ_CONTACTS = 100
    }

    private lateinit var adapter: ContactAdapter
    private val array = ArrayList<String>()
    private lateinit var bin: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bin = ActivityMainBinding.inflate(layoutInflater)
        setContentView(bin.root)

        permission()
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

        adapter = ContactAdapter(this, contactList, ContactAdapter.ItemClickListener {
            val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:${it.number}"))
            startActivity(intent)
        })
        bin.recyclerview.adapter = adapter

        contacts.close()
    }

    private fun permission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(
                Manifest.permission.READ_CONTACTS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(
                arrayOf(Manifest.permission.READ_CONTACTS),
                PERMISSIONS_REQUEST_READ_CONTACTS
            )
        }
    }
}
