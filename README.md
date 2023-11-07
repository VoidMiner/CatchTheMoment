# CatchTheMoment
First commit

Задание 1:
1. + Сделать проект в Android Studio, с пустым экраном, название "Лови момент"

2. - без ViewModel, не использовать классы MVVM, и без Fragment-ов, только Activity

3. + На экране расположить 3 элемента 1 под другим:

1. Картинка/Фото на всю ширину экрана

2. Поле для ввода текста, на 200 символов.

3. Круглая кнопка с иконкой галочки "Готово"

=== Практики и подходы в разработке

1. Исследовать "правило бойскаута" в программировании

2. Применить к своему коду
   
Задание 2:

1. + По нажатию на картинку-фото сделать фотографию или выбрать из галереи и показать

2. + По нажатию на "готово" создать "момент", перейти на новый экран со списком "моментов" и добавить в список этот новый "момент".

Каждый элемент списка состоит из фото, текста, даты и времени создания момента

3. + Список моментов реализовать используя вертикальный RecyclerView

4. + Моменты что были более часа назад пометить желтым индикатором.

5. + Моменты что были вчера и ранее пометить красным индикатором.

6. - Всю верстку экрана делать при помощи xml, не используя ConstraintLayout

7. - Индикаторы на элементах спсиска сделать без использования декораторов

Задание 3:

1. + Сохранять список моментов при добавлении нового в настройки приложения (SharedPreferences или подобное)

2. + Восстанавливать список моментов при открытии приложения

3. + Добавить Foreground Service, плашка в шторке с надписью "Поймать момент"

Задание 4:

1. + По нажатию на плашку сервиса "Поймать момент" - открыть приложение на главной странице, чтобы сделать момент

Задание 5:

1. + добавить кнопку меню в правом верхнем углу на главном экране приложения

2. + по клику на меню показать меню с двумя элементами "настройки" и "история"

3. + по клику на "история" показать экран со списком моментов

4. + по клику на "настройки" показать пустой экран настроек
