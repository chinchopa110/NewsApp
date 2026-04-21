package com.example.newsapp.ui.sdui

import com.example.newsapp.domain.model.Action
import com.example.newsapp.domain.model.Align
import com.example.newsapp.domain.model.ButtonStyle
import com.example.newsapp.domain.model.ColorToken
import com.example.newsapp.domain.model.SduiComponent
import com.example.newsapp.domain.model.SduiScreen
import com.example.newsapp.domain.model.TextStyle

object SduiScreenSeeder {
    const val REMOTE_PATH = "news-app/sdui/second-screen"

    fun createHotNewsScreen(): SduiScreen {
        return SduiScreen(
            id = "lab-second-screen",
            title = "Спецвыпуск новостей",
            components = listOf(
                SduiComponent.Text(
                    text = "Горячие новости дня",
                    style = TextStyle.TITLE,
                    align = Align.CENTER,
                    color = ColorToken.PRIMARY
                ),
                SduiComponent.Spacer(12),
                SduiComponent.Text(
                    text = "Что же случилось за сегодня?",
                    style = TextStyle.BODY,
                    color = ColorToken.ON_SURFACE
                ),
                SduiComponent.Spacer(16),
                SduiComponent.Card(
                    title = "Специальные горячие новости",
                    description = "Подборка ключевых политических и общественных новостей дня.",
                    imageUrl = "https://images.unsplash.com/photo-1495020689067-958852a7765e?auto=format&fit=crop&w=1200&q=80",
                    action = Action.OpenUrl("https://ria.ru/politics/")
                ),
                SduiComponent.Spacer(16),
                SduiComponent.Text(
                    text = "Главная тема выпуска: оперативная лента важных заявлений и событий.",
                    style = TextStyle.SUBTITLE,
                    color = ColorToken.ON_SURFACE
                ),
                SduiComponent.Spacer(8),
                SduiComponent.Button(
                    text = "Открыть ленту новостей",
                    style = ButtonStyle.PRIMARY,
                    action = Action.OpenUrl("https://ria.ru/")
                ),
                SduiComponent.Spacer(16),
                SduiComponent.Text(
                    text = "Вы будете всегда в центре событий!",
                    style = TextStyle.CAPTION,
                    color = ColorToken.ON_SURFACE_MEDIUM,
                    align = Align.CENTER
                )
            )
        )
    }
}
