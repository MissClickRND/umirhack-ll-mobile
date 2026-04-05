# Trust Edu
----

## Оглавление
- [Описание](#описание)
- [Структура и особенности кода](#структура-и-особенности-кода)
- [Скриншоты](#скриншоты)
- [Используемые технологии](#используемые-технологии)
- [Установка](#установка)

## Описание
**Trust Edu** — мобильное Android-приложение для цифровой верификации дипломов, разработанное в рамках кейса от Diasoft. Решение закрывает прикладную задачу заказчика: дать студенту удобный способ подтверждать свой диплом, а работодателю — быстро проверять его подлинность без ручной коммуникации с вузом. Интерфейс полностью построен на **Jetpack Compose**, поддерживает светлую и тёмную тему и может быть адаптирован под любую корпоративную дизайн-систему за счёт полной кастомной темизации.

### Основные функции
1. Анимированный Splash Screen с загрузкой приложения.
2. Онбординг с презентацией ценности платформы и пользовательского сценария.
3. Авторизация и регистрация студента.
4. Проверка подлинности диплома по связке **номер диплома + ФИО**.
5. Проверка диплома через **сканирование QR-кода камерой**.
6. Проверка диплома через **загрузку QR-кода из галереи**.
7. Экран результата проверки с отображением статуса диплома и его основных данных.
8. Раздел «Профиль» с вкладками **Мои дипломы**, **Активные ссылки** и **История**.
9. Добавление диплома в профиль по идентификатору.
10. Шаринг диплома через генерацию **ссылки** и **QR-кода** с выбором времени жизни доступа.
11. Просмотр всех активных ссылок с возможностью открыть QR, скопировать ссылку или **отозвать** её.
12. Просмотр истории истёкших и отозванных ссылок.

## Структура и особенности кода
Проект использует паттерн **MVI**. Каждая функциональность оформлена отдельным пакетом (`auth`, `scanner`, `diploma_check`, `profile`, `onboarding`) с подпакетами `data`, `domain` и `presentation` по чистой архитектуре.

- **Навигация** построена через `Navigation Compose` и nested **type-safe** маршруты. Это даёт безопасную масштабируемую структуру переходов между онбордингом, авторизацией, сценариями проверки диплома и профилем пользователя.
- **Данные пользователя** хранятся в `DataStore` с `ProtoBuf`, что обеспечивает type-safe хранение пользовательского состояния, статуса авторизации и локальных настроек.
- **DI** реализовано через **Hilt**. Модули внедрения зависимостей отвечают за предоставление сетевого слоя, репозиториев и feature-зависимостей для экранов приложения.
- **Дизайн-система** полностью кастомная: в проекте настроены собственные тема, типографика, шрифты, цвета и компоненты интерфейса. За счёт этого приложение можно быстро адаптировать под визуальный стиль любой компании без переписывания UI-слоя.
- **UI/UX** реализован на **Jetpack Compose** с поддержкой светлой и тёмной темы, а также с использованием **Lottie** для более живого и продуктового пользовательского опыта.
- **QR-сценарии** покрывают два практических кейса: потоковое сканирование через **Scanbot SDK** и распознавание QR-кода на изображении через **ML Kit Barcode Scanning**. В проекте также используется **ZXing** как дополнительный QR-инструментарий.
- **Сетевой слой** построен на **Retrofit**, **OkHttp** и **Kotlinx Serialization**. Это обеспечивает предсказуемую работу с API и удобное преобразование моделей данных.
- Приложение **не хранит чувствительные данные в открытом виде**, а конфиденциальные значения и ключи вынесены через **Secrets Gradle Plugin**.

Пример файла `local.properties`, если захотите скачать и запустить проект:
```properties
    sdk.dir=...
    BASE_API_URL=https://example.api/
```
## Скриншоты
### Светлая тема
<p align="center">
  <img src="screenshots/0_light.png" width="125" />
  <img src="screenshots/1_light.png" width="125" />
  <img src="screenshots/2_light.png" width="125" />
  <img src="screenshots/3_light.png" width="125" />
  <img src="screenshots/4_light.png" width="125" />
  <img src="screenshots/5_light.png" width="125" />
  <img src="screenshots/6_light.png" width="125" />
  <img src="screenshots/7_light.png" width="125" />
  <img src="screenshots/8_light.png" width="125" />
  <img src="screenshots/9_light.png" width="125" />
  <img src="screenshots/10_light.png" width="125" />
  <img src="screenshots/11_light.png" width="125" />
  <img src="screenshots/12_light.png" width="125" />
  <img src="screenshots/13_light.png" width="125" />
  <img src="screenshots/14_light.png" width="125" />
  <img src="screenshots/15_light.png" width="125" />
  <img src="screenshots/16_light.png" width="125" />
</p>

### Темная тема
<p align="center">
  <img src="screenshots/0_dark.png" width="125" />
  <img src="screenshots/1_dark.png" width="125" />
  <img src="screenshots/2_dark.png" width="125" />
  <img src="screenshots/3_dark.png" width="125" />
  <img src="screenshots/4_dark.png" width="125" />
  <img src="screenshots/5_dark.png" width="125" />
  <img src="screenshots/6_dark.png" width="125" />
  <img src="screenshots/7_dark.png" width="125" />
  <img src="screenshots/8_dark.png" width="125" />
  <img src="screenshots/9_dark.png" width="125" />
  <img src="screenshots/10_dark.png" width="125" />
  <img src="screenshots/11_dark.png" width="125" />
  <img src="screenshots/12_dark.png" width="125" />
  <img src="screenshots/13_dark.png" width="125" />
  <img src="screenshots/14_dark.png" width="125" />
  <img src="screenshots/15_dark.png" width="125" />
  <img src="screenshots/16_dark.png" width="125" />
</p>

## Используемые технологии
| Технология                    | Описание                                                                    |
|-------------------------------|-----------------------------------------------------------------------------|
| **Jetpack Compose**           | Построение пользовательского интерфейса и навигации                         |
| **Hilt**                      | Внедрение зависимостей                                                      |
| **Retrofit + OkHttp**         | Работа с HTTP API и сетевым слоем                                           |
| **DataStore (ProtoBuf)**      | Хранение пользовательских данных и локального состояния                     |
| **Kotlinx Serialization**     | Сериализация моделей и аргументов навигации                                 |
| **Lottie Compose**            | Анимации splash screen и UI-сценариев                                       |
| **Navigation Compose**        | Навигация между экранами                                                    |
| **Scanbot SDK**               | Сканирование QR-кодов через камеру                                          |
| **ML Kit Barcode Scanning**   | Распознавание QR-кодов на изображениях из галереи                           |
| **ZXing**                     | Дополнительная обработка QR-кодов                                           |
| **Secrets Plugin**            | Сокрытие конфиденциальной информации                                        |


## Установка
<p align="center">Ссылка на текущую версию <a href="https://github.com/MissClickRND/umirhack-ll-mobile/releases/tag/v1.0.0">Release v1.0.0</a></p>
<p align="center"><a href="https://github.com/MissClickRND/umirhack-ll-mobile/releases/download/v1.0.0/app-release.apk">Прямая ссылка на установку .apk</a></p>
<p align="center"><a href="#">Тут должна была быть ссылка на RuStore, но приложение на модерации</a></p>

<p align="center">
  <img src="screenshots/browser_j0St8bB2Eo.png"/>
</p>