package com.mandk.biblereasercher

import kotlinx.serialization.Serializable

/**
 * Screen used for navigation
 */
interface Screen

/**
 * HomeScreen route
 */
@Serializable
object HomeScreen : Screen

/**
 * ReaderScreen route
 */
@Serializable
object ReaderScreen : Screen

/**
 * BookmarkScreen route
 */
@Serializable
object BookmarkScreen : Screen

@Serializable
object ErrorScreen : Screen