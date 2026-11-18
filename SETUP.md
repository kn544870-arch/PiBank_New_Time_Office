# PiBank - Инструкция по настройке и сборке

Полное руководство по настройке и запуску Android-приложения PiBank.

## 📋 Содержание

1. [Требования](#требования)
2. [Настройка Firebase](#настройка-firebase)
3. [Сборка проекта](#сборка-проекта)
4. [Структура базы данных](#структура-базы-данных)
5. [Безопасность](#безопасность)
6. [GitHub Actions](#github-actions)

## 🔧 Требования

- **JDK:** 8 или выше
- **Android Studio:** Arctic Fox или новее
- **Min SDK:** 26 (Android 8.0 Oreo)
- **Target SDK:** 34
- **Gradle:** 7.0+

## 🔥 Настройка Firebase

### Шаг 1: Создание проекта

1. Откройте [Firebase Console](https://console.firebase.google.com/)
2. Нажмите **Add project** или выберите существующий
3. Следуйте инструкциям мастера создания проекта

### Шаг 2: Добавление Android приложения

1. В обзоре проекта нажмите **Android**
2. Укажите package name: `com.pibank.app`
3. (Опционально) Укажите nickname приложения: `PiBank`
4. Скачайте файл `google-services.json`
5. Замените файл `app/google-services.json` скачанным

### Шаг 3: Firebase Authentication

1. В боковом меню выберите **Build → Authentication**
2. Нажмите **Get started**
3. Во вкладке **Sign-in method** включите **Email/Password**
4. Сохраните изменения

### Шаг 4: Realtime Database

1. В боковом меню выберите **Build → Realtime Database**
2. Нажмите **Create Database**
3. Выберите регион (например, us-central1)
4. Выберите режим: **Start in test mode** (временно)
5. Нажмите **Enable**

### Шаг 5: Правила безопасности

**ВАЖНО:** Замените тестовые правила на безопасные!

1. В Realtime Database откройте вкладку **Rules**
2. Скопируйте содержимое файла `firebase-database-rules.json`
3. Вставьте в редактор правил
4. Нажмите **Publish**

Пример правил (из `firebase-database-rules.json`):

```json
{
  "rules": {
    "users": {
      "$userId": {
        ".read": "$userId === auth.uid",
        ".write": "$userId === auth.uid",
        "isAdmin": {
          ".write": "root.child('users').child(auth.uid).child('isAdmin').val() === true"
        },
        "balance": {
          ".write": "root.child('users').child(auth.uid).child('isAdmin').val() === true"
        }
      }
    }
  }
}
```

## 🏗️ Сборка проекта

### Вариант 1: Android Studio

1. Откройте Android Studio
2. **File → Open** → выберите папку проекта
3. Дождитесь синхронизации Gradle
4. Подключите Android устройство или запустите эмулятор
5. Нажмите **Run** (▶️) или `Shift + F10`

### Вариант 2: Командная строка

```bash
# Windows
gradlew assembleDebug

# Linux/Mac
./gradlew assembleDebug
```

APK будет создан в: `app/build/outputs/apk/debug/app-debug.apk`

### Установка APK

```bash
# Windows
gradlew installDebug

# Linux/Mac
./gradlew installDebug
```

## 🗄️ Структура базы данных

Firebase Realtime Database использует следующую структуру:

```
pibank-database/
│
├── users/
│   └── {userId}/
│       ├── userId: string
│       ├── email: string
│       ├── name: string
│       ├── balance: number
│       ├── isAdmin: boolean
│       └── createdAt: timestamp
│
├── cards/
│   └── {cardId}/
│       ├── cardId: string
│       ├── userId: string
│       ├── cardNumber: string
│       ├── cardType: "debit" | "credit" | "kids"
│       ├── cardHolder: string
│       ├── expiryDate: string (MM/YY)
│       ├── cvv: string (3 цифры)
│       ├── balance: number
│       ├── blocked: boolean
│       └── createdAt: timestamp
│
├── transactions/
│   └── {transactionId}/
│       ├── transactionId: string
│       ├── userId: string
│       ├── cardId: string
│       ├── type: "deposit" | "withdrawal" | "transfer" | "payment"
│       ├── amount: number
│       ├── description: string
│       └── timestamp: timestamp
│
├── news/
│   └── {newsId}/
│       ├── newsId: string
│       ├── title: string
│       ├── description: string
│       ├── link: string
│       └── timestamp: timestamp
│
└── crypto/
    └── {cryptoId}/
        ├── cryptoId: string
        ├── userId: string
        ├── cryptoName: string
        ├── symbol: string
        ├── amount: number
        ├── priceUSD: number
        └── lastUpdated: timestamp
```

## 🔐 Безопасность

### Права доступа

**Обычный пользователь может:**
- ✅ Создавать до 3 карт
- ✅ Просматривать свои карты
- ✅ Просматривать историю транзакций
- ✅ Просматривать новости
- ✅ Просматривать свои криптовалюты

**Обычный пользователь НЕ может:**
- ❌ Изменять баланс карт
- ❌ Блокировать/разблокировать карты
- ❌ Просматривать чужие данные
- ❌ Назначить себя администратором
- ❌ Создавать транзакции напрямую
- ❌ Управлять новостями

**Администратор может:**
- ✅ Искать любые карты по номеру
- ✅ Изменять баланс любых карт
- ✅ Блокировать/разблокировать карты
- ✅ Создавать новости
- ✅ Управлять криптовалютами пользователей
- ✅ Создавать транзакции

### Создание администратора

1. Зарегистрируйте пользователя в приложении
2. Откройте [Firebase Console](https://console.firebase.google.com/)
3. Перейдите в **Realtime Database**
4. Найдите пользователя: `users/{userId}`
5. Добавьте поле: `"isAdmin": true`

**Важно:** Это единственный способ назначить администратора. Приложение не позволяет пользователям самостоятельно получить права.

### Проверка безопасности

Проверьте, что правила работают:

1. Попробуйте изменить баланс через обычный аккаунт → должна быть ошибка
2. Попробуйте изменить `isAdmin` на `true` → должна быть ошибка
3. Попробуйте просмотреть чужую карту → должна быть ошибка
4. Войдите как админ и измените баланс → должно работать

## 🤖 GitHub Actions

Автоматическая сборка APK при каждом push в репозиторий.

### Настройка

1. В GitHub репозитории откройте **Settings**
2. Перейдите в **Secrets and variables → Actions**
3. Нажмите **New repository secret**
4. Добавьте секрет:
   - Name: `GOOGLE_SERVICES_JSON`
   - Value: содержимое файла `app/google-services.json`

### Использование

После каждого push:
1. Откройте **Actions** в репозитории
2. Выберите последний workflow run
3. Скачайте APK из секции **Artifacts**

## 📱 Тестирование

### Первый запуск

1. Запустите приложение
2. Нажмите **Регистрация**
3. Введите email, имя и пароль
4. После регистрации вы попадете на главный экран

### Создание карты

1. Перейдите в раздел **Мои карты**
2. Нажмите **Добавить карту**
3. Выберите тип карты (дебетовая/кредитная/детская)
4. Карта создастся с уникальным номером

### Просмотр CVV

Нажмите на карту для переворота и просмотра CVV и баланса.

### Админ-панель

1. Назначьте себе права администратора (см. выше)
2. Перезапустите приложение
3. В нижней навигации появится пункт **Admin**
4. Откройте админ-панель для управления

## ⚠️ Важные замечания

### Безопасность

1. **НЕ коммитьте** `google-services.json` в публичный репозиторий
2. **НЕ используйте** тестовый режим Firebase в продакшене
3. **ВСЕГДА проверяйте** права на стороне сервера
4. **НЕ храните** реальные финансовые данные

### Ограничения

- Максимум 3 карты на пользователя
- Номера карт генерируются случайно (не валидные для реальных платежей)
- Это учебное приложение, не предназначенное для реальных финансовых операций

## 🎨 Дизайн

- **Цветовая схема:** Черный (#000000) + Фиолетовый (#9C27B0)
- **Неоновые акценты:** #BB86FC, #FF10F0
- **Стиль карт:** Vida (градиентный фон)
- **Анимации:** Плавные переходы, переворот карт
- **Темная тема:** По умолчанию

## 📞 Поддержка

При возникновении проблем проверьте:

1. ✅ `google-services.json` установлен корректно
2. ✅ Firebase Authentication включен
3. ✅ Realtime Database создана
4. ✅ Правила безопасности применены
5. ✅ Package name совпадает: `com.pibank.app`

---

**Версия:** 1.0  
**Дата:** 18.11.2026  
**Минимальная версия Android:** 8.0 (API 26)
