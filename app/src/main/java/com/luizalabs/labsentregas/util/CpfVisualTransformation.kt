package com.luizalabs.labsentregas.util

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation

class CpfVisualTransformation : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        val transformedCpf = buildString {
            for (i in text.text.indices) {
                append(text[i])
                if (i == 2 || i == 5) append(".")
                if (i == 8) append("-")
            }
        }

        return TransformedText(AnnotatedString(transformedCpf), cpfMapping(text.text.length))
    }

    private fun cpfMapping(originalLength: Int) = object : OffsetMapping {
        override fun originalToTransformed(offset: Int): Int {
            return when {
                offset <= 2 -> offset
                offset <= 5 -> offset + 1
                offset <= 8 -> offset + 2
                else -> offset + 3
            }.coerceAtMost(originalLength + 3)
        }

        override fun transformedToOriginal(offset: Int): Int {
            return when {
                offset <= 3 -> offset
                offset <= 7 -> offset - 1
                offset <= 11 -> offset - 2
                else -> offset - 3
            }.coerceAtMost(originalLength)
        }
    }
}
