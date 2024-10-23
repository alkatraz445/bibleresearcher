package com.mandk.biblereasercher

import kotlinx.serialization.Serializable

interface Screen

@Serializable
object HomeScreen : Screen

@Serializable
object ReaderScreen : Screen

@Serializable
object BookmarkScreen : Screen