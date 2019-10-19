package hierophant.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpireField;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

@SpirePatch(clz = AbstractMonster.class, method = SpirePatch.CLASS)
public class EscapeFieldPatch
{
    public static SpireField<Boolean> escapingPiety = new SpireField<>(() -> false);
}


