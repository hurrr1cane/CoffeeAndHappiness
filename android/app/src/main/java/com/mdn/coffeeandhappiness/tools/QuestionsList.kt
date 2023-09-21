package com.mdn.coffeeandhappiness.tools

import com.mdn.coffeeandhappiness.model.Question

class QuestionsList {
    public fun getListOfQuestions(): MutableList<Question> {
        var questionsList = mutableListOf<Question>()

        questionsList.add(
            Question(
                "Як створити обліковий запис?",
                "How to create an account?",
                "Ви можете створити обліковий запис перейшовши на сторінку \"Акаунт\" та вибравши знизу \"Створити акаунт\"",
                "You can create an account by entering the \"Account\" page and then selecting \"Create account\" at the bottom."
            )
        )
        questionsList.add(
            Question(
                "Як скинути пароль для облікового запису?",
                "How to reset your account password?",
                "Для скидання паролю перейдіть до розділу \"Акаунт\", натисніть \"Забули пароль?\", та введіть інструкції, щоб скинути пароль.",
                "To reset your account password, go to the \"Account\" section, click \"Forgot password?\", and follow the instructions to reset your password."
            )
        );
        questionsList.add(
            Question(
                "Як переглянути меню?",
                "How to view the menu?",
                "Для перегляду меню перейдіть до розділу \"Меню\".",
                "To view the menu, go to the \"Menu\" section."
            )
        );
        questionsList.add(
            Question(
                "Як переглянути своє замовлення?",
                "How to view your order?",
                "Ви можете переглянути своє замовлення в розділі \"Мої замовлення\" у секції \"Код\". Там буде відображена інформація про всі ваші замовлення та нараховані бали.",
                "You can view your order in the \"My orders\" section in \"Code\" section. It will display information about your orders and your bonus points."
            )
        );
        questionsList.add(
            Question(
                "Як переглянути поточний баланс бонусних очків?",
                "How to check the current balance of bonus points?",
                "Щоб переглянути поточний баланс бонусних очків, увійдіть у свій акаунт та перейдіть на секцію \"Код\". Там ви знайдете інформацію про ваш баланс бонусних очків.",
                "To check the current balance of bonus points, log in to your account and go to the \"Code\" section. There, you will find information about your bonus points balance."
            )
        );

        questionsList.add(
            Question(
                "Як додати відгук до страви?",
                "How to add a review to a dish?",
                "Щоб додати відгук до страви, оберіть її в меню та натисніть \"Додати відгук\". Ви також маєте бути авторизовані в своєму обліковому записі, щоб залишити відгук.",
                "To add a review to a dish, select it from the menu and click \"Add Review.\" You must also be logged in to your account to leave a review."
            )
        );

        questionsList.add(
            Question(
                "Як потратити бонусні очки?",
                "How to redeem bonus points?",
                "Щоб потратити бонусні очки, просто скажіть про це офіціанту, коли він скануватиме ваш QR-код під час оформлення замовлення. Ви зможете визначити, скільки бонусних очок ви хочете використати на оплату.",
                "To redeem bonus points, simply inform the waiter when they scan your QR code during the order process. You can specify how many bonus points you wish to use for payment."
            )
        );

        return questionsList
    }
}