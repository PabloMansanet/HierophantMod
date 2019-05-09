package hierophant.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import hierophant.powers.EmbezzlePower;

@SpirePatch(clz = AbstractPlayer.class, method = "loseGold")
public class LostGoldPatch
{
    public static void Prefix(int goldAmount)
    {
        AbstractPlayer p = AbstractDungeon.player;
        if (AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT
            && p.hasPower(EmbezzlePower.POWER_ID))
        {
            AbstractDungeon.actionManager.addToBottom(new ReducePowerAction(p, p, EmbezzlePower.POWER_ID, goldAmount));

        }
    }
}
