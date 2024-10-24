package com.mandk.biblereasercher.utils

import androidx.compose.runtime.Composable
import com.mandk.biblereasercher.Book
import com.mandk.biblereasercher.Translation
import com.mandk.biblereasercher.UserSelection

// interface to implement ScraperClass on it
interface Scraper {

    /**
     * Function with logic to scrape for Bible chapters
     *
     * @param urlString string containing URL to scrape from
     * @return List of chapters(@type String) scraped from the website
     */
    @Composable
    fun scrapeForChapters(urlString : String): MutableList<String>

    /**
     * Function with logic to scrape for Bible books(e.g Księga Rodzaju)
     *
     * @param translationUrl string containing URL to scrape from
     * @param testament string containing from which testament to choose from
     * @return List of books(@type Book) scraped from the website
     */
    @Composable
    fun scrapeForBook(translationUrl: String, testament: String) : MutableList<Book>

    /**
     * Function with logic to scrape for Bible translations
     *
     * @return List of translations(@type Translation) scraped from the website
     */
    @Composable
    fun scrapeForTranslations() : MutableList<Translation>

    /**
     * Function with logic to scrape for Bible text with a specified value
     *
     * @param selectedValue - value selected by user in SelectionBox
     * @return Text from the website
     */
    fun skrapeText(selectedValue : UserSelection) : String
}