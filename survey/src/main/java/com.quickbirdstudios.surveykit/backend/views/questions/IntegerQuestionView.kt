package com.quickbirdstudios.surveykit.backend.views.questions

import android.content.Context
import android.view.Gravity
import androidx.annotation.StringRes
import com.quickbirdstudios.survey.R
import com.quickbirdstudios.surveykit.AnswerFormat
import com.quickbirdstudios.surveykit.StepIdentifier
import com.quickbirdstudios.surveykit.backend.helpers.extensions.afterTextChanged
import com.quickbirdstudios.surveykit.backend.views.question_parts.IntegerTextField
import com.quickbirdstudios.surveykit.backend.views.step.QuestionView
import com.quickbirdstudios.surveykit.result.QuestionResult
import com.quickbirdstudios.surveykit.result.question_results.IntegerQuestionResult

internal class IntegerQuestionView(
    context: Context,
    id: StepIdentifier,
    isOptional: Boolean,
    @StringRes title: Int?,
    @StringRes text: Int?,
    @StringRes nextButtonText: Int,
    @StringRes private val hintText: Int = R.string.empty,
    private val answerFormat: AnswerFormat.IntegerAnswerFormat,
    private val preselected: Int? = answerFormat.defaultValue
) : QuestionView(context, id, isOptional, title, text, nextButtonText) {


    //region Members

    private lateinit var questionAnswerView: IntegerTextField

    //endregion


    //region Overrides

    override fun createResults(): QuestionResult =
        IntegerQuestionResult(
            id = id,
            startDate = startDate,
            answer = questionAnswerView.field.text.toString().parseToIntOrNull(),
            stringIdentifier = questionAnswerView.field.text.toString()
        )

    override fun setState() {
        val intState = (state as? IntegerQuestionResult)?.answer ?: return
        questionAnswerView.field.setText(intState)
    }

    override fun isValidInput(): Boolean = isOptional || questionAnswerView.field.text.isNotBlank()

    override fun setupViews() {
        super.setupViews()

        questionAnswerView = content.add(IntegerTextField.withHint(context, hintText))
        questionAnswerView.field.gravity = Gravity.CENTER
        questionAnswerView.field.setHint(answerFormat.hint)
        questionAnswerView.field.afterTextChanged { footer.canContinue = isValidInput() }
        questionAnswerView.field.setText(preselected?.toString() ?: "")
    }

    //endregion


    //region Private Helpers

    private fun String.parseToIntOrNull(): Int? {
        return try {
            this.toInt()
        } catch (e: NumberFormatException) {
            null
        }
    }

    //endregion

}
