package com.example.newsapp.ui.sdui

import android.content.Context
import android.graphics.Typeface
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.widget.LinearLayout
import android.widget.Space
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.example.newsapp.R
import com.example.newsapp.domain.model.Action
import com.example.newsapp.domain.model.Align
import com.example.newsapp.domain.model.Article
import com.example.newsapp.domain.model.ButtonStyle
import com.example.newsapp.domain.model.ColorToken
import com.example.newsapp.domain.model.SduiComponent
import com.example.newsapp.domain.model.Source
import com.example.newsapp.domain.model.TextStyle
import com.example.newsapp.ui.components.ArticleCardView
import com.google.android.material.button.MaterialButton

class SduiComponentFactory(
    private val context: Context,
    private val actionHandler: SduiActionHandler
) {

    fun create(component: SduiComponent): View {
        return when (component) {
            is SduiComponent.Text -> createText(component)
            is SduiComponent.Button -> createButton(component)
            is SduiComponent.Card -> createCard(component)
            is SduiComponent.Spacer -> createSpacer(component)
            is SduiComponent.Column -> createColumn(component)
        }
    }

    private fun createText(component: SduiComponent.Text): TextView {
        return TextView(context).apply {
            text = component.text
            setTextColor(resolveColor(component.color))
            gravity = resolveGravity(component.align)
            when (component.style) {
                TextStyle.TITLE -> {
                    setTextSize(TypedValue.COMPLEX_UNIT_SP, 24f)
                    setTypeface(typeface, Typeface.BOLD)
                }
                TextStyle.SUBTITLE -> {
                    setTextSize(TypedValue.COMPLEX_UNIT_SP, 18f)
                    setTypeface(typeface, Typeface.BOLD)
                }
                TextStyle.BODY -> {
                    setTextSize(TypedValue.COMPLEX_UNIT_SP, 16f)
                }
                TextStyle.CAPTION -> {
                    setTextSize(TypedValue.COMPLEX_UNIT_SP, 12f)
                }
            }
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
        }
    }

    private fun createButton(component: SduiComponent.Button): MaterialButton {
        return MaterialButton(context, null, resolveButtonStyle(component.style)).apply {
            text = component.text
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            component.action?.let { action ->
                setOnClickListener { actionHandler.handle(action) }
            }
        }
    }

    private fun createCard(component: SduiComponent.Card): View {
        return ArticleCardView(context).apply {
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            setArticle(
                Article(
                    source = Source(id = null, name = component.title),
                    author = null,
                    title = component.title,
                    description = component.description,
                    url = component.action?.let { action ->
                        when (action) {
                            is Action.OpenUrl -> action.url
                            is Action.ShowToast -> "https://example.com/${action.message}"
                        }
                    } ?: "https://example.com/${component.title}",
                    urlToImage = component.imageUrl,
                    publishedAt = "2026-01-01T00:00:00Z",
                    content = component.description.orEmpty()
                )
            )
            component.action?.let { action ->
                setOnClickListener { actionHandler.handle(action) }
            }
        }
    }

    private fun createSpacer(component: SduiComponent.Spacer): Space {
        return Space(context).apply {
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                component.sizeDp.dpToPx()
            )
        }
    }

    private fun createColumn(component: SduiComponent.Column): LinearLayout {
        return LinearLayout(context).apply {
            orientation = LinearLayout.VERTICAL
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            component.children.forEach { child ->
                addView(create(child))
            }
        }
    }

    private fun resolveButtonStyle(style: ButtonStyle): Int {
        return when (style) {
            ButtonStyle.PRIMARY -> R.style.DS_Widget_Button_Primary
            ButtonStyle.DANGER -> R.style.DS_Widget_Button_Danger
            ButtonStyle.TEXT -> R.style.DS_Widget_Button_Text
        }
    }

    private fun resolveColor(colorToken: ColorToken): Int {
        return when (colorToken) {
            ColorToken.PRIMARY -> ContextCompat.getColor(context, R.color.ds_color_primary)
            ColorToken.ON_SURFACE -> ContextCompat.getColor(context, R.color.ds_color_on_surface)
            ColorToken.ON_SURFACE_MEDIUM -> ContextCompat.getColor(context, R.color.ds_color_on_surface_medium)
            ColorToken.ERROR -> ContextCompat.getColor(context, R.color.ds_color_error)
        }
    }

    private fun resolveGravity(align: Align): Int {
        return when (align) {
            Align.START -> Gravity.START
            Align.CENTER -> Gravity.CENTER_HORIZONTAL
            Align.END -> Gravity.END
        }
    }

    private fun Int.dpToPx(): Int {
        return (this * context.resources.displayMetrics.density).toInt()
    }
}
