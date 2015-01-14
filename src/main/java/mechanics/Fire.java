package mechanics;

import Users.UserProfile;
import base.AccountService;
import base.WebSocketService;
import org.json.simple.JSONObject;
import profiles.ItemProfile;

import java.util.Random;

/**
 * Created by boris on 16.12.14.
 */
public class Fire extends Thread {

    private WebSocketService socketService;
    private AccountService accountService;
    private UserProfile attacker;
    private UserProfile victimProfile;
    private  int gunId;

    public Fire (WebSocketService socketService, AccountService accountService, UserProfile attacker, UserProfile victimProfile, int gunId){
        this.socketService = socketService;
        this.accountService = accountService;
        this.attacker = attacker;
        this.victimProfile = victimProfile;
        this.gunId = gunId;
    }

    public void run() {
        //Пушка
        int curGunId = 0;
        switch (gunId) {
            case 1:
                curGunId = attacker.getW1();
                break;
            case 2:
                curGunId = attacker.getW2();
                break;
            case 3:
                curGunId = attacker.getW3();
                break;
            case 4:
                curGunId = attacker.getW4();
                break;
            case 5:
                curGunId = attacker.getW5();
                break;
        }
        ItemProfile curGun = accountService.getItem(curGunId);
        double gunRadius = Double.parseDouble(curGun.getP3());
        int minDamage = Integer.parseInt(curGun.getP1());
        int maxDamage = Integer.parseInt(curGun.getP2());
//        int critChance = 7; //Число процентов
//        float critLevel = 1.5f; //коэффициент при крите
        //Дистанция
        float distanceX = attacker.getX() - victimProfile.getX();
        float distanceY = attacker.getY() - victimProfile.getY();
        double distance = Math.sqrt(Math.pow(distanceX, 2) + Math.pow(distanceY, 2));
        if (distance < gunRadius) {
            //Вычислим урон
            Random rand = new Random();
            float damage = minDamage + rand.nextInt(maxDamage - minDamage);
            //В юзерпрофайле getskill1 - крит
            //Getskill2 - маневренность
            int ifBioStimulator = 0;
            int raceBonus = 0;
            if (attacker.getRace() == 0) { //маллок
                raceBonus = 5;
            }
            int critChance = attacker.getSkill1() * 5 + raceBonus + ifBioStimulator;
            int critVer = rand.nextInt(99);
            if (critVer < critChance) {
                damage *= 2;
            }

            //(D-B)*(1-G*0.01)*Rnd(M)
            int shipId = victimProfile.getShip();
            int generatorId = attacker.getGenerator();
            ItemProfile victimsShip = accountService.getItem(shipId);
            ItemProfile attackerGenerator = accountService.getItem(generatorId);
            int shipBlock = 0;
            if (victimsShip != null) {
                shipBlock = Integer.parseInt(victimsShip.getP2());
            }

            int generatorBlock = 0;
            if (attackerGenerator != null) {
                generatorBlock = Integer.parseInt(attackerGenerator.getP1());
            }
            float damageWithoutGenerator = damage - shipBlock;
            damage = damageWithoutGenerator * (1 - generatorBlock * 0.01f);

            //Шанс уворота
            int raceDodgeChance = 0;
            if (victimProfile.getRace() == 1) {
                raceDodgeChance = 5;
            }
            int mobilityBioStimulator = 0;
            int dodgeChance = victimProfile.getSkill1() * 5 + raceDodgeChance + mobilityBioStimulator;
            int dodgeVer = rand.nextInt(99);
            if (dodgeVer < dodgeChance) {
                damage = 0;
            }
            //Опыт
//            В*(D*(Lo-Lp+5) +Kf*(Lo-Lp+5))*Pf
            float balance = 0.01f;
            int attackerLevel = attacker.getLevel();
            int victimLevel = victimProfile.getLevel();
            float levelPart = victimLevel - attackerLevel + 5;
            if (levelPart < 0) {
                levelPart = 0;
            }
            int kF = 200; //Доминатор - 300, НПС - 100, РС своей фракции - 150, РС чужой фракции - 200
            int pF = 1; // премиум-фактор. Для не имеющих премиум-аккаунта равен 1, а для игроков с премиум-аккаунтом = 1.5
            int receivedExperience = (int) Math.round(balance * (damageWithoutGenerator * levelPart + kF * levelPart) * pF);
            int newExp = attacker.getExp() + receivedExperience;
            attacker.setExp(newExp);
            int roundDamage = (int)Math.ceil(damage);
            /*
            float newShipHealth = Float.parseFloat(victimsShip.getP1()) - roundDamage;
            if (newShipHealth < 0) {
                newShipHealth = 0f;
                //враг умер, обработать на сервере это
            }
            victimsShip.setP1(Float.toString(newShipHealth));
            */
            int newVictimHealth = victimProfile.updateHealth( -roundDamage );
            JSONObject output = new JSONObject();
            output.put("type", "fireToEnemy");
            output.put("attacker", attacker.getLogin());
            output.put("victim", victimProfile.getLogin());
            output.put("damage", roundDamage);
            output.put("newHealth", newVictimHealth);

            this.socketService.notifyOtherPlayers(attacker, output, true);
        }
    }
}
