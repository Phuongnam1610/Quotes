package com.example.quotes.activities.showuser

import com.example.quotes.Model.User
import com.example.quotes.fragments.search.ISearchView

interface IShowUserView:ISearchView {
    fun showUser(user: User)
    fun vSingOut()
    fun openLinkFB(linkFB:String)
}