package com.gerrysatria.jobbotapp.utils

import android.content.Context
import org.json.JSONObject
import org.tensorflow.lite.Interpreter
import org.tensorflow.lite.support.common.FileUtil

object ModelUtils {
    fun cleanText(text: String): String {
        return text.lowercase()
            .replace(Regex("\\d+"), "")      // Hapus angka
            .replace(Regex("[^\\w\\s]"), "") // Hapus tanda baca
            .replace(Regex("\\s+"), " ")     // Hapus spasi berlebih
            .trim()
    }

    fun loadTokenizer(context: Context): Map<String, Int> {
        val json = context.assets.open("tokenizer.json")
            .bufferedReader()
            .use { it.readText() }
        val wordIndex = JSONObject(json).getJSONObject("word_index")
        val map = mutableMapOf<String, Int>()
        wordIndex.keys().forEach { key ->
            map[key] = wordIndex.getInt(key)
        }
        return map
    }

    fun tokenizeText(text: String, wordIndex: Map<String, Int>, maxLen: Int = 200): IntArray {
        val tokens = text.lowercase().split("\\s+".toRegex())
        val sequence = tokens.map { wordIndex[it] ?: wordIndex.getOrDefault("<OOV>", 0) }
        val padded = IntArray(maxLen) { 0 }
        for (i in sequence.indices) {
            if (i < maxLen) {
                padded[i] = sequence[i]
            }
        }
        return padded
    }

    fun loadModel(context: Context): Interpreter {
        val modelBuffer = FileUtil.loadMappedFile(context, "cv_classification_model.tflite")
        return Interpreter(modelBuffer)
    }

    fun predictCV(model: Interpreter, input: IntArray): Boolean {
        val inputArray = arrayOf(input.map { it.toFloat() }.toFloatArray())
        val output = Array(1) { FloatArray(1) }
        model.run(inputArray, output)
        return output[0][0] > 0.5
    }
}