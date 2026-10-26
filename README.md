<p align="center">
  <img src="https://walid-fekry.com/img/aalam-almarifa/logo.png" width="90" height="90" alt="Logo"/>
</p>

<h1><p align="center">عالم المعرفة | Aalam Al-Marifa</p></h1>
<p align="center"><b>منصّة معرفية وثقافية شاملة تقدم الاقتباسات، الحقائق اليومية، والمحتوى الثقافي في تطبيق أندرويد حديث.</b></p>

<p align="center">
  <a href="#"><img src="https://img.shields.io/badge/Android-SDK%2037-3DDC84?style=flat&logo=android&logoColor=white" alt="Android SDK 37"/></a>
  <a href="#"><img src="https://img.shields.io/badge/Java-8%2B-007396?style=flat&logo=java&logoColor=white" alt="Java"/></a>
  <a href="#"><img src="https://img.shields.io/badge/Firebase-FCM-FFCA28?style=flat&logo=firebase&logoColor=black" alt="Firebase FCM"/></a>
  <a href="#"><img src="https://img.shields.io/badge/AdMob-Integrated-4285F4?style=flat&logo=googleadmob&logoColor=white" alt="AdMob"/></a>
</p>

---

## 📖 عن التطبيق
**تطبيق عالم المعرفة** هو منصّة إلكترونية رائدة تقدّم المحتوى العربي الترفيهي والمعلومات الموثوقة في مختلف المجالات. تم إعادة بناء وتحديث التطبيق ليعمل بأحدث تقنيات أندرويد (Android Target SDK 37) ويوفر تجربة تصفح سريعة ومميزة.

<p align="center">
  <img src="https://walid-fekry.com/img/aalam-almarifa/screen_1.jpg" width="45%" alt="Screen 1"/>
  &nbsp;
  <img src="https://walid-fekry.com/img/aalam-almarifa/screen_2.jpg" width="45%" alt="Screen 2"/>
</p>

---

## ✨ أبرز المميزات والوظائف

### 🎨 1. محرك تصميم الاقتباسات وتصدير الصور (`QuoteImageGenerator` & `QuoteImageDialog`)
- إمكانية تحويل أي اقتباس أو حقيقة يومية إلى **بطاقة صُورية أنيقة** عالية الجودة.
- دعم **5 ثيمات ألوان متدرجة** قابلة للاختيار بضغطة زر:
  - 🟠 **Deep Orange** (برتقالي دافئ)
  - 🔵 **Royal Blue** (أزرق ملكي)
  - 🟢 **Emerald Green** (زمردي ناعم)
  - 🟣 **Purple Night** (بنفسجي ليلي)
  - 🌑 **Dark Elegant** (داكن أنيق)
- خطوط عربية مخصصة (`jazeera.ttf`) مع علامات اقتباس مائية وهيدر احترافي.
- معاينة حية وفورية للبطاقة ومشاركتها مباشرة عبر وسائل التواصل الاجتماعي مع حماية أذونات المشاركة عبر `FileProvider`.

### 💡 2. موسوعة الحقائق اليومية (`Daily Fact`)
- استعراض حقائق ثقافية وعلمية متنوعة مع إمكانية التوليد العشوائي.
- أزرار سريعة للحفظ في المفضلة، النسخ، أو المشاركة كصورة أو نص.

### ⭐ 3. نظام المفضلة المحلي الشامل (`FavoritesManager`)
- حفظ الاقتباسات والحقائق المفضلة بآلية تخزين محلي سريعة وموثوقة (`SharedPreferences` + `JSON`).
- شاشة مفضلة مخصصة مع خيارات الإدارة الكاملة (استعراض، مشاركة، حذف، وتحويل إلى صور).

### 🌐 4. متصفح ويب متطور بالفيديو ملء الشاشة (`VideoEnabledWebView`)
- متصفح ويب مدعوم بتشغيل فيديوهات HTML5 بشاشة كاملة بدون مشاكل.
- شاشات تحميل مخصصة، معالجة أخطاء الشبكة، وإمكانية إعادة التحميل التلقائي.

### 🌙 5. دعم الوضع الداكن والتخصيص (`ThemeManager`)
- تبديل سلس بين الوضع الفاتح والوضع الداكن للحفاظ على راحة العين وتوفير البطارية.

### 📖 6. الرقية الشرعية والأذكار بدون إنترنت (`RoqiaNoIternet`)
- قسم خاص بالرقية والأذكار يتيح للمستخدم الوصول للمحتوى حتى عند عدم توفر اتصال بالإنترنت.

### 🔔 7. إشعارات التحديث والمحتوى اليومي (`Firebase Messaging`)
- استقبال إشعارات لحظية بالمحتوى الجديد عبر منصة Firebase Cloud Messaging مع التوافق الكامل مع أحدث أذونات أندرويد 13+ (`POST_NOTIFICATIONS`).

---

## 🛠️ التقنيات والمكتبات المستخدمة

| التقنية / المكتبة | الغرض والاستخدام |
| :--- | :--- |
| **Android SDK 37 (Java 8/17)** | أحدث معايير تطوير الأندرويد وأعلى مستويات الأمان والأداء |
| **ViewBinding** | ربط عناصر الواجهة بأمان وتفادي أخطاء `NullPointer` |
| **OkHttp 5.x** | إدارة طلبات الشبكة وجلب المحتوى بسرعة وكفاءة |
| **Firebase FCM & Analytics** | استقبال الإشعارات الفورية وتحليل الأداء |
| **Google Mobile Ads (AdMob)** | الإعلانات التكيّفية (Banner, Interstitial, Rewarded) |
| **Lottie Animations** | رسوم متحركة خفيفة وتفاعلية لشاشات التحميل والبداية |
| **PrettyDialog & Material Components** | واجهات حوار وأنماط تصميم جَذّابة متناسقة |
| **FileProvider & AndroidX Core** | مشاركة الصور والملفات بأمان وفق أحدث سياسات Android |

---


## 📲 التحميل والمتاجر الرسمية

- 📱 **متجر جوجل بلاي:** [تحميل تطبيق عالم المعرفة على Google Play](https://play.google.com/store/apps/details?id=com.walid.knowledgeworld)
- 🛍️ **متجر هواوي:** [تحميل تطبيق عالم المعرفة على Huawei AppGallery](https://appgallery.huawei.com/#/app/C102854275)

---

## 👨‍💻 المطور

**وليد فكري (Walid Fekry)**
- 🌐 **الموقع الرسمي:** [walid-fekry.com](https://walid-fekry.com)
- ✉️ **التواصل:** [walid_fekry@hotmail.com](mailto:walid_fekry@hotmail.com)
---
<p align="center">تم التطوير بـ ❤️ لتقديم تجربة إسلامية وثقافية فريدة للمستخدم العربي.</p>
