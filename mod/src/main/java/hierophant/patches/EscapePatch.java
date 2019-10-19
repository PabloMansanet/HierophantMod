package hierophant.patches;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.tools.texturepacker.TexturePacker.Settings;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

@SpirePatch(clz = AbstractMonster.class, method = "updateEscapeAnimation")
public class EscapePatch
{
    @SpirePostfixPatch
    public static void Postfix(AbstractMonster __instance)
    {
       if (EscapeFieldPatch.escapingPiety.get(__instance)) {
            __instance.flipHorizontal = true;
            __instance.drawX += Gdx.graphics.getDeltaTime() * 200.0F;
        }
    }
}
