package hierophant.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import hierophant.powers.CommunityPower;

@SpirePatch(clz = AbstractCreature.class, method = "addBlock")
public class AddBlockPatch
{
    @SpirePostfixPatch
    public static void Postfix(AbstractCreature __instance, int blockAmount)
    {
        if (!__instance.isPlayer) {
            AbstractPlayer p = AbstractDungeon.player;
            if (p.hasPower(CommunityPower.POWER_ID)) {
                int amount = p.getPower(CommunityPower.POWER_ID).amount;
                p.addBlock(amount * blockAmount);
            }
        }
    }
}
