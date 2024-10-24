package com.mandk.biblereasercher.utils

import androidx.compose.runtime.Composable
import com.mandk.biblereasercher.Book
import com.mandk.biblereasercher.Translation
import com.mandk.biblereasercher.UserSelection

interface Scraper {

    @Composable
    fun scrapeForChapters(urlString : String): MutableList<String>

    @Composable
    fun scrapeForBook(translationUrl: String, testament: String) : MutableList<Book>

    @Composable
    fun scrapeForTranslations() : MutableList<Translation>

    fun skrapeText(selectedValue : UserSelection) : String

//    enum class Translations(val message: String) {
//        Tysiaclecia("Tysiąclecia"),
//        UwspolczesnionaBibliaGdanska("Gdańska"),
//        Warszawska("Warszawska"),
//        JakubaWujka("Jakuba Wujka"),
//        Brzeska("Brzeska")
//    }
}