package hierophant;

import basemod.BaseMod;
import basemod.ModLabeledToggleButton;
import basemod.ModPanel;
import basemod.interfaces.*;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.evacipated.cardcrawl.mod.stslib.Keyword;
import com.evacipated.cardcrawl.modthespire.lib.SpireConfig;
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;
import com.google.gson.Gson;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.TheCity;
import com.megacrit.cardcrawl.helpers.CardHelper;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.localization.*;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import hierophant.characters.Hierophant;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import hierophant.cards.*;
import hierophant.events.IdentityCrisisEvent;
import hierophant.util.IDCheckDontTouchPls;
import hierophant.util.TextureLoader;
import hierophant.variables.DefaultCustomVariable;
import hierophant.variables.DefaultSecondMagicNumber;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Properties;


/*
 * With that out of the way:
 * Welcome to this super over-commented Slay the Spire modding base.
 * Use it to make your own mod of any type. - If you want to add any standard in-game content (character,
 * cards, relics), this is a good starting point.
 * It features 1 character with a minimal set of things: 1 card of each type, 1 debuff, couple of relics, etc.
 * If you're new to modding, you basically *need* the BaseMod wiki for whatever you wish to add
 * https://github.com/daviscook477/BaseMod/wiki - work your way through with this base.
 * Feel free to use this in any way you like, of course. MIT licence applies. Happy modding!
 *
 * And pls. Read the comments.
 */

@SpireInitializer
public class HierophantMod implements
        EditCardsSubscriber,
        EditRelicsSubscriber,
        EditStringsSubscriber,
        EditKeywordsSubscriber,
        EditCharactersSubscriber,
        PostInitializeSubscriber {
    // Make sure to implement the subscribers *you* are using (read basemod wiki). Editing cards? EditCardsSubscriber.
    // Making relics? EditRelicsSubscriber. etc., etc., for a full list and how to make your own, visit the basemod wiki.
    public static final Logger logger = LogManager.getLogger(HierophantMod.class.getName());
    private static String modID;

    // Mod-settings settings. This is if you want an on/off savable button
    public static Properties hierophantDefaultSettings = new Properties();
    public static final String ENABLE_PLACEHOLDER_SETTINGS = "enablePlaceholder";
    public static boolean enablePlaceholder = true; // The boolean we'll be setting on/off (true/false)

    //This is for the in-game mod settings panel.
    private static final String MODNAME = "Hierophant Mod";
    private static final String AUTHOR = "Contrast"; // And pretty soon - You!
    private static final String DESCRIPTION = "Hierophant Playable Character";
    
    // =============== INPUT TEXTURE LOCATION =================
    
    // Colors (RGB)
    // Character Color
    public static final Color HIEROPHANT_GOLD = CardHelper.getColor(255.0f, 215.0f, 0.0f);
  
    // Card backgrounds - The actual rectangular card.
    private static final String ATTACK_HIEROPHANT_GOLD = "hierophantResources/images/512/bg_attack_hierophant_gold.png";
    private static final String SKILL_HIEROPHANT_GOLD = "hierophantResources/images/512/bg_skill_hierophant_gold.png";
    private static final String POWER_HIEROPHANT_GOLD = "hierophantResources/images/512/bg_power_hierophant_gold.png";
    
    private static final String ENERGY_ORB_HIEROPHANT_GOLD = "hierophantResources/images/512/card_hierophant_gold_orb.png";
    private static final String CARD_ENERGY_ORB = "hierophantResources/images/512/card_small_orb.png";
    
    private static final String ATTACK_HIEROPHANT_GOLD_PORTRAIT = "hierophantResources/images/1024/bg_attack_hierophant_gold.png";
    private static final String SKILL_HIEROPHANT_GOLD_PORTRAIT = "hierophantResources/images/1024/bg_skill_hierophant_gold.png";
    private static final String POWER_HIEROPHANT_GOLD_PORTRAIT = "hierophantResources/images/1024/bg_power_hierophant_gold.png";
    private static final String ENERGY_ORB_HIEROPHANT_GOLD_PORTRAIT = "hierophantResources/images/1024/card_hierophant_gold_orb.png";

    // Character assets

    private static final String HIEROPHANT_BUTTON = "hierophantResources/images/charSelect/DefaultCharacterButton.png";
    private static final String HIEROPHANT_PORTRAIT = "hierophantResources/images/charSelect/hierophantPortrait.jpg";
    public static final String HIEROPHANT_SHOULDER_1 = "hierophantResources/images/char/shoulder.png";
    public static final String HIEROPHANT_SHOULDER_2 = "hierophantResources/images/char/shoulder2.png";
    public static final String HIEROPHANT_CORPSE = "hierophantResources/images/char/hierophantCharacter/corpse.png";
    
    //Mod Badge - A small icon that appears in the mod settings menu next to your mod.
    public static final String BADGE_IMAGE = "hierophantResources/images/Badge.png";
    
    // Atlas and JSON files for the Animations
    public static final String HIEROPHANT_SKELETON_ATLAS = "hierophantResources/images/char/hierophantCharacter/skeleton.atlas";
    public static final String HIEROPHANT_SKELETON_JSON = "hierophantResources/images/char/hierophantCharacter/skeleton.json";
    
    // =============== MAKE IMAGE PATHS =================
    
    public static String makeCardPath(String resourcePath) {
        return getModID() + "Resources/images/cards/" + resourcePath;
    }
    
    public static String makeRelicPath(String resourcePath) {
        return getModID() + "Resources/images/relics/" + resourcePath;
    }
    
    public static String makeRelicOutlinePath(String resourcePath) {
        return getModID() + "Resources/images/relics/outline/" + resourcePath;
    }
    
    public static String makeOrbPath(String resourcePath) {
        return getModID() + "Resources/orbs/" + resourcePath;
    }
    
    public static String makePowerPath(String resourcePath) {
        return getModID() + "Resources/images/powers/" + resourcePath;
    }
    
    public static String makeEventPath(String resourcePath) {
        return getModID() + "Resources/images/events/" + resourcePath;
    }
    
    // =============== /MAKE IMAGE PATHS/ =================
    
    // =============== /INPUT TEXTURE LOCATION/ =================
    
    
    // =============== SUBSCRIBE, CREATE THE COLOR_GOLD, INITIALIZE =================
    
    public HierophantMod() {
        logger.info("Subscribe to BaseMod hooks");
        
        BaseMod.subscribe(this);
        
      /*
           (   ( /(  (     ( /( (            (  `   ( /( )\ )    )\ ))\ )
           )\  )\()) )\    )\()))\ )   (     )\))(  )\()|()/(   (()/(()/(
         (((_)((_)((((_)( ((_)\(()/(   )\   ((_)()\((_)\ /(_))   /(_))(_))
         )\___ _((_)\ _ )\ _((_)/(_))_((_)  (_()((_) ((_|_))_  _(_))(_))_
        ((/ __| || (_)_\(_) \| |/ __| __| |  \/  |/ _ \|   \  |_ _||   (_)
         | (__| __ |/ _ \ | .` | (_ | _|  | |\/| | (_) | |) |  | | | |) |
          \___|_||_/_/ \_\|_|\_|\___|___| |_|  |_|\___/|___/  |___||___(_)
      */
      
        setModID("hierophant");
        // cool
        // TODO: NOW READ THIS!!!!!!!!!!!!!!!:
        
        // 1. Go to your resources folder in the project panel, and refactor> rename hierophantResources to
        // yourModIDResources.
        
        // 2. Click on the localization > eng folder and press ctrl+shift+r, then select "Directory" (rather than in Project)
        // replace all instances of hierophant with yourModID.
        // Because your mod ID isn't the default. Your cards (and everything else) should have Your mod id. Not mine.
        
        // 3. FINALLY and most importantly: Scroll up a bit. You may have noticed the image locations above don't use getModID()
        // Change their locations to reflect your actual ID rather than hierophant. They get loaded before getID is a thing.
        
        logger.info("Done subscribing");
        
        logger.info("Creating the color " + Hierophant.Enums.COLOR_GOLD.toString());
        
        BaseMod.addColor(Hierophant.Enums.COLOR_GOLD, HIEROPHANT_GOLD, HIEROPHANT_GOLD, HIEROPHANT_GOLD,
                HIEROPHANT_GOLD, HIEROPHANT_GOLD, HIEROPHANT_GOLD, HIEROPHANT_GOLD,
                ATTACK_HIEROPHANT_GOLD, SKILL_HIEROPHANT_GOLD, POWER_HIEROPHANT_GOLD, ENERGY_ORB_HIEROPHANT_GOLD,
                ATTACK_HIEROPHANT_GOLD_PORTRAIT, SKILL_HIEROPHANT_GOLD_PORTRAIT, POWER_HIEROPHANT_GOLD_PORTRAIT,
                ENERGY_ORB_HIEROPHANT_GOLD_PORTRAIT, CARD_ENERGY_ORB);
        
        logger.info("Done creating the color");
        
        
        logger.info("Adding mod settings");
        // This loads the mod settings.
        // The actual mod Button is added below in receivePostInitialize()
        hierophantDefaultSettings.setProperty(ENABLE_PLACEHOLDER_SETTINGS, "FALSE"); // This is the default setting. It's actually set...
        try {
            SpireConfig config = new SpireConfig("hierophantMod", "hierophantConfig", hierophantDefaultSettings); // ...right here
            // the "fileName" parameter is the name of the file MTS will create where it will save our setting.
            config.load(); // Load the setting and set the boolean to equal it
            enablePlaceholder = config.getBool(ENABLE_PLACEHOLDER_SETTINGS);
        } catch (Exception e) {
            e.printStackTrace();
        }
        logger.info("Done adding mod settings");
        
    }
    
    // ====== NO EDIT AREA ======
    // DON'T TOUCH THIS STUFF. IT IS HERE FOR STANDARDIZATION BETWEEN MODS AND TO ENSURE GOOD CODE PRACTICES.
    // IF YOU MODIFY THIS I WILL HUNT YOU DOWN AND DOWNVOTE YOUR MOD ON WORKSHOP
    
    public static void setModID(String ID) { // DON'T EDIT
        Gson coolG = new Gson(); // EY DON'T EDIT THIS
        //   String IDjson = Gdx.files.internal("IDCheckStringsDONT-EDIT-AT-ALL.json").readString(String.valueOf(StandardCharsets.UTF_8)); // i hate u Gdx.files
        InputStream in = HierophantMod.class.getResourceAsStream("/IDCheckStringsDONT-EDIT-AT-ALL.json"); // DON'T EDIT THIS ETHER
        IDCheckDontTouchPls EXCEPTION_STRINGS = coolG.fromJson(new InputStreamReader(in, StandardCharsets.UTF_8), IDCheckDontTouchPls.class); // OR THIS, DON'T EDIT IT
        logger.info("You are attempting to set your mod ID as: " + ID); // NO WHY
        if (ID.equals(EXCEPTION_STRINGS.DEFAULTID)) { // DO *NOT* CHANGE THIS ESPECIALLY, TO EDIT YOUR MOD ID, SCROLL UP JUST A LITTLE, IT'S JUST ABOVE
            throw new RuntimeException(EXCEPTION_STRINGS.EXCEPTION); // THIS ALSO DON'T EDIT
        } else if (ID.equals(EXCEPTION_STRINGS.DEVID)) { // NO
            modID = EXCEPTION_STRINGS.DEFAULTID; // DON'T
        } else { // NO EDIT AREA
            modID = ID; // DON'T WRITE OR CHANGE THINGS HERE NOT EVEN A LITTLE
        } // NO
        logger.info("Success! ID is " + modID); // WHY WOULD U WANT IT NOT TO LOG?? DON'T EDIT THIS.
    } // NO
    
    public static String getModID() { // NO
        return modID; // DOUBLE NO
    } // NU-UH
    
    private static void pathCheck() { // ALSO NO
        Gson coolG = new Gson(); // NOPE DON'T EDIT THIS
        //   String IDjson = Gdx.files.internal("IDCheckStringsDONT-EDIT-AT-ALL.json").readString(String.valueOf(StandardCharsets.UTF_8)); // i still hate u btw Gdx.files
        InputStream in = HierophantMod.class.getResourceAsStream("/IDCheckStringsDONT-EDIT-AT-ALL.json"); // DON'T EDIT THISSSSS
        IDCheckDontTouchPls EXCEPTION_STRINGS = coolG.fromJson(new InputStreamReader(in, StandardCharsets.UTF_8), IDCheckDontTouchPls.class); // NAH, NO EDIT
        String packageName = HierophantMod.class.getPackage().getName(); // STILL NO EDIT ZONE
        FileHandle resourcePathExists = Gdx.files.internal(getModID() + "Resources"); // PLEASE DON'T EDIT THINGS HERE, THANKS
        if (!modID.equals(EXCEPTION_STRINGS.DEVID)) { // LEAVE THIS EDIT-LESS
            if (!packageName.equals(getModID())) { // NOT HERE ETHER
                throw new RuntimeException(EXCEPTION_STRINGS.PACKAGE_EXCEPTION + getModID()); // THIS IS A NO-NO
            } // WHY WOULD U EDIT THIS
            if (!resourcePathExists.exists()) { // DON'T CHANGE THIS
                throw new RuntimeException(EXCEPTION_STRINGS.RESOURCE_FOLDER_EXCEPTION + getModID() + "Resources"); // NOT THIS
            }// NO
        }// NO
    }// NO
    
    // ====== YOU CAN EDIT AGAIN ======
    
    
    @SuppressWarnings("unused")
    public static void initialize() {
        logger.info("========================= Initializing Hierophant Mod. =========================");
        HierophantMod defaultmod = new HierophantMod();
        logger.info("========================= /Hierophant Mod Initialized. Hello World./ =========================");
    }
    
    // ============== /SUBSCRIBE, CREATE THE COLOR_GOLD, INITIALIZE/ =================
    
    
    // =============== LOAD THE CHARACTER =================
    
    @Override
    public void receiveEditCharacters() {
        logger.info("Beginning to edit characters. " + "Add " + Hierophant.Enums.HIEROPHANT.toString());
        
        BaseMod.addCharacter(new Hierophant("Hierophant", Hierophant.Enums.HIEROPHANT),
                HIEROPHANT_BUTTON, HIEROPHANT_PORTRAIT, Hierophant.Enums.HIEROPHANT);
        
        logger.info("Added " + Hierophant.Enums.HIEROPHANT.toString());
    }
    
    // =============== /LOAD THE CHARACTER/ =================
    
    
    // =============== POST-INITIALIZE =================
    
    @Override
    public void receivePostInitialize() {
        logger.info("Loading badge image and mod options");
        
        // Load the Mod Badge
        Texture badgeTexture = TextureLoader.getTexture(BADGE_IMAGE);
        
        // Create the Mod Menu
        ModPanel settingsPanel = new ModPanel();
        
        // Create the on/off button:
        ModLabeledToggleButton enableNormalsButton = new ModLabeledToggleButton("This is the text which goes next to the checkbox.",
                350.0f, 700.0f, Settings.CREAM_COLOR, FontHelper.charDescFont, // Position (trial and error it), color, font
                enablePlaceholder, // Boolean it uses
                settingsPanel, // The mod panel in which this button will be in
                (label) -> {}, // thing??????? idk
                (button) -> { // The actual button:
            
            enablePlaceholder = button.enabled; // The boolean true/false will be whether the button is enabled or not
            try {
                // And based on that boolean, set the settings and save them
                SpireConfig config = new SpireConfig("hierophantMod", "hierophantConfig", hierophantDefaultSettings);
                config.setBool(ENABLE_PLACEHOLDER_SETTINGS, enablePlaceholder);
                config.save();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        
        settingsPanel.addUIElement(enableNormalsButton); // Add the button to the settings panel. Button is a go.
        
        BaseMod.registerModBadge(badgeTexture, MODNAME, AUTHOR, DESCRIPTION, settingsPanel);
        logger.info("Done loading badge Image and mod options");
    }
    
    // =============== / POST-INITIALIZE/ =================
    
    // ================ ADD RELICS ===================
    
    @Override
    public void receiveEditRelics() {
        logger.info("Adding relics");
        
        // This adds a character specific relic. Only when you play with the mentioned color, will you get this relic.
        //BaseMod.addRelicToCustomPool(new PlaceholderRelic(), Hierophant.Enums.COLOR_GOLD);
        //BaseMod.addRelicToCustomPool(new BottledPlaceholderRelic(), Hierophant.Enums.COLOR_GOLD);
        //BaseMod.addRelicToCustomPool(new DefaultClickableRelic(), Hierophant.Enums.COLOR_GOLD);
        
        // This adds a relic to the Shared pool. Every character can find this relic.
        //BaseMod.addRelic(new PlaceholderRelic2(), RelicType.SHARED);
        
        // Mark relics as seen (the others are all starters so they're marked as seen in the character file
        // UnlockTracker.markRelicAsSeen(BottledPlaceholderRelic.ID);
        logger.info("Done adding relics!");
    }
    
    // ================ /ADD RELICS/ ===================
    
    
    // ================ ADD CARDS ===================
    
    @Override
    public void receiveEditCards() {
        logger.info("Adding variables");
        //Ignore this
        pathCheck();
        // Add the Custom Dynamic Variables
        logger.info("Add variabls");
        // Add the Custom Dynamic variabls
        BaseMod.addDynamicVariable(new DefaultCustomVariable());
        BaseMod.addDynamicVariable(new DefaultSecondMagicNumber());
        
        logger.info("Adding cards");

        // Attacks
        BaseMod.addCard(new Strike_Hierophant());
        BaseMod.addCard(new CoinFling());
        BaseMod.addCard(new HolyStrike());
        BaseMod.addCard(new Batter());
        BaseMod.addCard(new AuricLance());
        BaseMod.addCard(new Mercenaries());
        BaseMod.addCard(new SearingVirtue());
        BaseMod.addCard(new PassingBell());
        BaseMod.addCard(new SolarFlare());
        BaseMod.addCard(new LocustPlague());
        BaseMod.addCard(new WrathOfGod());
        BaseMod.addCard(new Admonish());
        BaseMod.addCard(new SealAway());
        BaseMod.addCard(new Smite());
        BaseMod.addCard(new Upheaval());
        BaseMod.addCard(new RodSlam());
        BaseMod.addCard(new Punish());
        BaseMod.addCard(new Anathema());
        BaseMod.addCard(new Blasphemy());
        BaseMod.addCard(new AuricBeam());
        BaseMod.addCard(new DisplayOfPower());

        // Skills
        BaseMod.addCard(new Defend_Hierophant());
        BaseMod.addCard(new Doubloon());
        BaseMod.addCard(new MorningPrayer());
        BaseMod.addCard(new HolyVerse());
        BaseMod.addCard(new Endure());
        BaseMod.addCard(new ConvertCurrency());
        BaseMod.addCard(new RecallFunds());
        BaseMod.addCard(new Dazzle());
        BaseMod.addCard(new Embezzle());
        BaseMod.addCard(new Entourage());
        BaseMod.addCard(new Blessing());
        BaseMod.addCard(new Shelter());
        BaseMod.addCard(new MirrorShield());
        BaseMod.addCard(new ChurchCoffers());
        BaseMod.addCard(new Sermon());
        BaseMod.addCard(new DivineIntervention());
        BaseMod.addCard(new Miracle());
        BaseMod.addCard(new Bribe());
        BaseMod.addCard(new Flagellation());
        BaseMod.addCard(new PristineSoul());
        BaseMod.addCard(new Prophecy());
        BaseMod.addCard(new Doomsaying());
        BaseMod.addCard(new Prayer());
        BaseMod.addCard(new Repentance());
        BaseMod.addCard(new SecretStash());
        BaseMod.addCard(new Levitation());
        BaseMod.addCard(new BulkyChest());
        BaseMod.addCard(new Cornucopia());
        BaseMod.addCard(new Racketeering());
        BaseMod.addCard(new TollTheBells());
        BaseMod.addCard(new Encroach());

        // Powers
        BaseMod.addCard(new VowOfPoverty());
        BaseMod.addCard(new VowOfSobriety());
        BaseMod.addCard(new VowOfSanctity());
        BaseMod.addCard(new VowOfPurity());
        BaseMod.addCard(new VowOfStability());
        BaseMod.addCard(new Martyrdom());
        BaseMod.addCard(new Generosity());
        BaseMod.addCard(new CursedGold());
        BaseMod.addCard(new AuricForm());
        BaseMod.addCard(new Phylactery());

        logger.info("Making sure the cards are unlocked.");

        // Attacks
        UnlockTracker.unlockCard(Strike_Hierophant.ID);
        UnlockTracker.unlockCard(CoinFling.ID);
        UnlockTracker.unlockCard(HolyStrike.ID);
        UnlockTracker.unlockCard(AuricLance.ID);
        UnlockTracker.unlockCard(Mercenaries.ID);
        UnlockTracker.unlockCard(SearingVirtue.ID);
        UnlockTracker.unlockCard(PassingBell.ID);
        UnlockTracker.unlockCard(SolarFlare.ID);
        UnlockTracker.unlockCard(LocustPlague.ID);
        UnlockTracker.unlockCard(WrathOfGod.ID);
        UnlockTracker.unlockCard(Admonish.ID);
        UnlockTracker.unlockCard(SealAway.ID);
        UnlockTracker.unlockCard(Smite.ID);
        UnlockTracker.unlockCard(Upheaval.ID);
        UnlockTracker.unlockCard(RodSlam.ID);
        UnlockTracker.unlockCard(Punish.ID);
        UnlockTracker.unlockCard(Anathema.ID);
        UnlockTracker.unlockCard(Blasphemy.ID);
        UnlockTracker.unlockCard(AuricBeam.ID);
        UnlockTracker.unlockCard(DisplayOfPower.ID);

        // Skills
        UnlockTracker.unlockCard(Defend_Hierophant.ID);
        UnlockTracker.unlockCard(Doubloon.ID);
        UnlockTracker.unlockCard(MorningPrayer.ID);
        UnlockTracker.unlockCard(HolyVerse.ID);
        UnlockTracker.unlockCard(Endure.ID);
        UnlockTracker.unlockCard(ConvertCurrency.ID);
        UnlockTracker.unlockCard(RecallFunds.ID);
        UnlockTracker.unlockCard(Dazzle.ID);
        UnlockTracker.unlockCard(Embezzle.ID);
        UnlockTracker.unlockCard(Entourage.ID);
        UnlockTracker.unlockCard(Blessing.ID);
        UnlockTracker.unlockCard(Shelter.ID);
        UnlockTracker.unlockCard(MirrorShield.ID);
        UnlockTracker.unlockCard(ChurchCoffers.ID);
        UnlockTracker.unlockCard(Sermon.ID);
        UnlockTracker.unlockCard(DivineIntervention.ID);
        UnlockTracker.unlockCard(Miracle.ID);
        UnlockTracker.unlockCard(Bribe.ID);
        UnlockTracker.unlockCard(Flagellation.ID);
        UnlockTracker.unlockCard(PristineSoul.ID);
        UnlockTracker.unlockCard(Prophecy.ID);
        UnlockTracker.unlockCard(Doomsaying.ID);
        UnlockTracker.unlockCard(Prayer.ID);
        UnlockTracker.unlockCard(Repentance.ID);
        UnlockTracker.unlockCard(SecretStash.ID);
        UnlockTracker.unlockCard(Levitation.ID);
        UnlockTracker.unlockCard(BulkyChest.ID);
        UnlockTracker.unlockCard(Cornucopia.ID);
        UnlockTracker.unlockCard(Racketeering.ID);
        UnlockTracker.unlockCard(TollTheBells.ID);
        UnlockTracker.unlockCard(Encroach.ID);

        // Powers
        UnlockTracker.unlockCard(VowOfPoverty.ID);
        UnlockTracker.unlockCard(VowOfSobriety.ID);
        UnlockTracker.unlockCard(VowOfSanctity.ID);
        UnlockTracker.unlockCard(VowOfPurity.ID);
        UnlockTracker.unlockCard(VowOfStability.ID);
        UnlockTracker.unlockCard(Martyrdom.ID);
        UnlockTracker.unlockCard(Generosity.ID);
        UnlockTracker.unlockCard(CursedGold.ID);
        UnlockTracker.unlockCard(AuricForm.ID);
        UnlockTracker.unlockCard(Phylactery.ID);

        logger.info("Done adding cards!");
    }
    
    // There are better ways to do this than listing every single individual card, but I do not want to complicate things
    // in a "tutorial" mod. This will do and it's completely ok to use. If you ever want to clean up and
    // shorten all the imports, go look take a look at other mods, such as Hubris.
    
    // ================ /ADD CARDS/ ===================
    
    
    // ================ LOAD THE TEXT ===================
    
    @Override
    public void receiveEditStrings() {
        logger.info("You seeing this?");
        logger.info("Beginning to edit strings for mod with ID: " + getModID());
        
        // CardStrings
        BaseMod.loadCustomStringsFile(CardStrings.class,
                getModID() + "Resources/localization/eng/HierophantMod-Card-Strings.json");
        
        // PowerStrings
        BaseMod.loadCustomStringsFile(PowerStrings.class,
                getModID() + "Resources/localization/eng/HierophantMod-Power-Strings.json");
        
        // RelicStrings
        BaseMod.loadCustomStringsFile(RelicStrings.class,
                getModID() + "Resources/localization/eng/HierophantMod-Relic-Strings.json");
        
        // Event Strings
        BaseMod.loadCustomStringsFile(EventStrings.class,
                getModID() + "Resources/localization/eng/HierophantMod-Event-Strings.json");
        
        // PotionStrings
        BaseMod.loadCustomStringsFile(PotionStrings.class,
                getModID() + "Resources/localization/eng/HierophantMod-Potion-Strings.json");
        
        // CharacterStrings
        BaseMod.loadCustomStringsFile(CharacterStrings.class,
                getModID() + "Resources/localization/eng/HierophantMod-Character-Strings.json");
        
        // OrbStrings
        BaseMod.loadCustomStringsFile(OrbStrings.class,
                getModID() + "Resources/localization/eng/HierophantMod-Orb-Strings.json");
        
        logger.info("Done edittting strings");
    }
    
    // ================ /LOAD THE TEXT/ ===================
    
    // ================ LOAD THE KEYWORDS ===================
    
    @Override
    public void receiveEditKeywords() {
        // Keywords on cards are supposed to be Capitalized, while in Keyword-String.json they're lowercase
        //
        // Multiword keywords on cards are done With_Underscores
        //
        // If you're using multiword keywords, the first element in your NAMES array in your keywords-strings.json has to be the same as the PROPER_NAME.
        // That is, in Card-Strings.json you would have #yA_Long_Keyword (#y highlights the keyword in yellow).
        // In Keyword-Strings.json you would have PROPER_NAME as A Long Keyword and the first element in NAMES be a long keyword, and the second element be a_long_keyword
        
        Gson gson = new Gson();
        String json = Gdx.files.internal(getModID() + "Resources/localization/eng/HierophantMod-Keyword-Strings.json").readString(String.valueOf(StandardCharsets.UTF_8));
        com.evacipated.cardcrawl.mod.stslib.Keyword[] keywords = gson.fromJson(json, com.evacipated.cardcrawl.mod.stslib.Keyword[].class);
        
        if (keywords != null) {
            for (Keyword keyword : keywords) {
                BaseMod.addKeyword(getModID().toLowerCase(), keyword.PROPER_NAME, keyword.NAMES, keyword.DESCRIPTION);
                //  getModID().toLowerCase() makes your keyword mod specific (it won't show up in other cards that use that word)
            }
        }
    }
    
    // ================ /LOAD THE KEYWORDS/ ===================    
    
    // this adds "ModName:" before the ID of any card/relic/power etc.
    // in order to avoid conflicts if any other mod uses the same ID.
    public static String makeID(String idText) {
        return getModID() + ":" + idText;
    }
}