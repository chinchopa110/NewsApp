package com.example.newsapp.domain.model

data class SduiScreen(
    val id: String,
    val title: String,
    val components: List<SduiComponent>
)

sealed interface SduiComponent {
    data class Text(
        val text: String,
        val style: TextStyle = TextStyle.BODY,
        val color: ColorToken = ColorToken.ON_SURFACE,
        val align: Align = Align.START
    ) : SduiComponent

    data class Button(
        val text: String,
        val style: ButtonStyle = ButtonStyle.PRIMARY,
        val action: Action? = null
    ) : SduiComponent

    data class Card(
        val title: String,
        val description: String,
        val imageUrl: String? = null,
        val action: Action? = null
    ) : SduiComponent

    data class Spacer(
        val sizeDp: Int = 16
    ) : SduiComponent

    data class Column(
        val children: List<SduiComponent>
    ) : SduiComponent
}

enum class TextStyle {
    TITLE,
    SUBTITLE,
    BODY,
    CAPTION
}

enum class ButtonStyle {
    PRIMARY,
    DANGER,
    TEXT
}

enum class ColorToken {
    PRIMARY,
    ON_SURFACE,
    ON_SURFACE_MEDIUM,
    ERROR
}

enum class Align {
    START,
    CENTER,
    END
}

sealed interface Action {
    data class OpenUrl(val url: String) : Action
    data class ShowToast(val message: String) : Action
}
