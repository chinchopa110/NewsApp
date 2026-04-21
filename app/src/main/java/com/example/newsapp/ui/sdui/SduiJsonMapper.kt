package com.example.newsapp.ui.sdui

import com.example.newsapp.domain.model.Action
import com.example.newsapp.domain.model.Align
import com.example.newsapp.domain.model.ButtonStyle
import com.example.newsapp.domain.model.ColorToken
import com.example.newsapp.domain.model.SduiComponent
import com.example.newsapp.domain.model.SduiScreen
import com.example.newsapp.domain.model.TextStyle
import com.google.gson.JsonArray
import com.google.gson.JsonElement
import com.google.gson.JsonObject

object SduiJsonMapper {

    fun fromJson(json: JsonObject): SduiScreen {
        val components = json.getAsJsonArray("components")
            ?.mapNotNull { parseComponent(it) }
            .orEmpty()

        return SduiScreen(
            id = json.get("id")?.asString ?: "remote-screen",
            title = json.get("title")?.asString ?: "Remote screen",
            components = components
        )
    }

    fun toJson(screen: SduiScreen): JsonObject {
        return JsonObject().apply {
            addProperty("id", screen.id)
            addProperty("title", screen.title)
            add("components", JsonArray().apply {
                screen.components.forEach { add(componentToJson(it)) }
            })
        }
    }

    private fun parseComponent(element: JsonElement): SduiComponent? {
        val json = element.asJsonObject
        return when (json.get("type")?.asString) {
            "text" -> SduiComponent.Text(
                text = json.get("text")?.asString.orEmpty(),
                style = json.getEnum("style", TextStyle.BODY),
                color = json.getEnum("color", ColorToken.ON_SURFACE),
                align = json.getEnum("align", Align.START)
            )
            "button" -> SduiComponent.Button(
                text = json.get("text")?.asString.orEmpty(),
                style = json.getEnum("style", ButtonStyle.PRIMARY),
                action = parseAction(json.getAsJsonObject("action"))
            )
            "card" -> SduiComponent.Card(
                title = json.get("title")?.asString.orEmpty(),
                description = json.get("description")?.asString.orEmpty(),
                imageUrl = json.get("imageUrl")?.asString,
                action = parseAction(json.getAsJsonObject("action"))
            )
            "spacer" -> SduiComponent.Spacer(
                sizeDp = json.get("sizeDp")?.asInt ?: 16
            )
            "column" -> SduiComponent.Column(
                children = json.getAsJsonArray("children")
                    ?.mapNotNull { parseComponent(it) }
                    .orEmpty()
            )
            else -> null
        }
    }

    private fun componentToJson(component: SduiComponent): JsonObject {
        return when (component) {
            is SduiComponent.Text -> JsonObject().apply {
                addProperty("type", "text")
                addProperty("text", component.text)
                addProperty("style", component.style.name.lowercase())
                addProperty("color", component.color.name.lowercase())
                addProperty("align", component.align.name.lowercase())
            }
            is SduiComponent.Button -> JsonObject().apply {
                addProperty("type", "button")
                addProperty("text", component.text)
                addProperty("style", component.style.name.lowercase())
                component.action?.let { add("action", actionToJson(it)) }
            }
            is SduiComponent.Card -> JsonObject().apply {
                addProperty("type", "card")
                addProperty("title", component.title)
                addProperty("description", component.description)
                component.imageUrl?.let { addProperty("imageUrl", it) }
                component.action?.let { add("action", actionToJson(it)) }
            }
            is SduiComponent.Spacer -> JsonObject().apply {
                addProperty("type", "spacer")
                addProperty("sizeDp", component.sizeDp)
            }
            is SduiComponent.Column -> JsonObject().apply {
                addProperty("type", "column")
                add("children", JsonArray().apply {
                    component.children.forEach { add(componentToJson(it)) }
                })
            }
        }
    }

    private fun parseAction(json: JsonObject?): Action? {
        json ?: return null
        return when (json.get("type")?.asString) {
            "open_url" -> Action.OpenUrl(json.get("url")?.asString.orEmpty())
            "show_toast" -> Action.ShowToast(json.get("message")?.asString.orEmpty())
            else -> null
        }
    }

    private fun actionToJson(action: Action): JsonObject {
        return when (action) {
            is Action.OpenUrl -> JsonObject().apply {
                addProperty("type", "open_url")
                addProperty("url", action.url)
            }
            is Action.ShowToast -> JsonObject().apply {
                addProperty("type", "show_toast")
                addProperty("message", action.message)
            }
        }
    }

    private inline fun <reified T : Enum<T>> JsonObject.getEnum(key: String, default: T): T {
        val raw = get(key)?.asString ?: return default
        return enumValues<T>().firstOrNull { it.name.equals(raw, ignoreCase = true) } ?: default
    }
}
