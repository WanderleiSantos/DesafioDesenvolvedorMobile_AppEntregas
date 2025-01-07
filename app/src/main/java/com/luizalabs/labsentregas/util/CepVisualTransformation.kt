package com.luizalabs.labsentregas.util

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation

class CepVisualTransformation : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        val transformedCep = buildString {
            for (i in text.text.indices) {
                append(text[i])
                if (i == 4) append("-")
            }
        }

        return TransformedText(AnnotatedString(transformedCep), cepMapping)
    }

    val cepMapping = object : OffsetMapping {
        override fun originalToTransformed(offset: Int): Int {
            return if (offset > 4) offset + 1 else offset
        }

        override fun transformedToOriginal(offset: Int): Int {
            return if (offset > 5) offset - 1 else offset
        }
    }
}