package hierophant.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.FastCardObtainEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndObtainEffect;
import hierophant.cards.Doubloon;

public class GildedPatch {
    @SpirePatch(clz = ShowCardAndObtainEffect.class, method = SpirePatch.CONSTRUCTOR, paramtypez = {
            AbstractCard.class,
            float.class,
            float.class,
            boolean.class
    })
    public static class ObtainCardConstructorPatch
    {
        public static void Prefix(ShowCardAndObtainEffect _instance, AbstractCard  card, float x, float y, boolean convergeCards)
        {
            if (card.hasTag(hierophant.tags.HierophantTags.HIEROPHANT_GILDED)) {
                AbstractDungeon.effectList.add(new ShowCardAndObtainEffect(new Doubloon(), x, y));
            } else if (card.hasTag(hierophant.tags.HierophantTags.HIEROPHANT_DOUBLE_GILDED)) {
                AbstractDungeon.effectList.add(new ShowCardAndObtainEffect(new Doubloon(), x, y));
                AbstractDungeon.effectList.add(new ShowCardAndObtainEffect(new Doubloon(), x, y));
            }
        }
    }

    @SpirePatch(clz = FastCardObtainEffect.class, method = SpirePatch.CONSTRUCTOR, paramtypez = {
            AbstractCard.class,
            float.class,
            float.class,
    })
    public static class FastCardObtainConstructorPatch
    {
        public static void Prefix(FastCardObtainEffect _instance, AbstractCard  card, float x, float y)
        {
            if (card.hasTag(hierophant.tags.HierophantTags.HIEROPHANT_GILDED)) {
                AbstractDungeon.effectList.add(new FastCardObtainEffect(new Doubloon(), x + 50, y));
            } else if (card.hasTag(hierophant.tags.HierophantTags.HIEROPHANT_DOUBLE_GILDED)) {
                AbstractDungeon.effectList.add(new ShowCardAndObtainEffect(new Doubloon(), x + 25, y));
                AbstractDungeon.effectList.add(new ShowCardAndObtainEffect(new Doubloon(), x + 50, y));
            }
        }
    }
}
