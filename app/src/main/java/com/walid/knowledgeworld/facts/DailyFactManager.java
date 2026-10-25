package com.walid.knowledgeworld.facts;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class DailyFactManager {

    private static final List<String> FACTS = new ArrayList<>();

    static {
        FACTS.add("هل تعلم أن قلب الروبيان (الجمبري) يوجد في رأسه؟");
        FACTS.add("هل تعلم أن عسل النحل الطبيعي هو الطعام الوحيد الذي لا يفسد أبداً ويمكن أن يدوم لآلاف السنين؟");
        FACTS.add("هل تعلم أن الخفافيش هي الثدييات الوحيدة القادرة على الطيران الحقيقي؟");
        FACTS.add("هل تعلم أن عين النعامة أكبر حجماً من دماغها؟");
        FACTS.add("هل تعلم أن التفاح أكثر فاعلية من القهوة في إيقاظ الإنسان بالصباح بفضل سكرياته الطبيعية؟");
        FACTS.add("هل تعلم أن أطول جدار بني في العالم هو سور الصين العظيم ويمتد لأكثر من 21 ألف كيلومتر؟");
        FACTS.add("هل تعلم أن الدلفين ينام بإغلاق عين واحدة فقط ويبقي نصف دماغه مستيقظاً؟");
        FACTS.add("هل تعلم أن الصوت يسافر في الماء أسرع بـ 4 مرات منه في الهواء؟");
        FACTS.add("هل تعلم أن أول من استعمل الساعة اليدوية هم النساء، وكانت تعتبر قطعة حلي وثمينة قبل أن يرتديها الرجال؟");
        FACTS.add("هل تعلم أن القرنية في عين الإنسان هي الجزء الوحيد في الجسم الذي لا يصله الدم ويحصل على الأكسجين مباشرة من الهواء؟");
        FACTS.add("هل تعلم أن الأخطبوط لديه 3 قلوب ودمه أزرق اللون؟");
        FACTS.add("هل تعلم أن الحوت الأزرق هو أكبر كائن حي على وجه الأرض ويصل وزن لسانه فقط لوزن فيل كامل؟");
        FACTS.add("هل تعلم أن أطول نهر في العالم هو نهر النيل ويمتد لمسافة 6650 كيلومتراً؟");
        FACTS.add("هل تعلم أن الابتسامة تحفز إفراز هرمون الإندورفين الذي يقلل التوتر ويرفع المناعة؟");
        FACTS.add("هل تعلم أن مدينة دمشق تعتبر من أقدم المدن المأهولة بالسكان باستمرار في التاريخ؟");
    }

    public static String getTodayFact() {
        Calendar calendar = Calendar.getInstance();
        int dayOfYear = calendar.get(Calendar.DAY_OF_YEAR);
        int index = dayOfYear % FACTS.size();
        return FACTS.get(index);
    }

    public static List<String> getAllFacts() {
        return new ArrayList<>(FACTS);
    }
}
