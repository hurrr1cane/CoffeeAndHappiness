package com.mdn.coffeeandhappiness

import android.content.Context
import com.akexorcist.localizationactivity.ui.LocalizationApplication
import java.util.Locale

class ApplicationForLocalization: LocalizationApplication() {
    override fun getDefaultLanguage(context: Context): Locale {
        return Locale.getDefault()
    }
}