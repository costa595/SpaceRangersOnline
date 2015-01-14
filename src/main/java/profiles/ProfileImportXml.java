package profiles;

import base.AccountService;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;

/**
 * Created by surkov on 28.11.14.
 */
public class ProfileImportXml {
    private AccountService accountService;
    private ProfileService profileService = new ProfileService();
    private String path = null;

    public ProfileImportXml(AccountService accountService) {
        this.accountService = accountService;
    }

    public ProfileImportXml(AccountService accountService, String path) {
        this.accountService = accountService;
        this.path = path;
    }

    public void importXml() throws ParserConfigurationException, IOException, org.xml.sax.SAXException {
        File fXmlFile = null;
        System.out.println(fXmlFile);
        if(path == null) {
            fXmlFile = new File("src/main/resources/systems.xml");
        } else {
            fXmlFile = new File(path);
        }
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document doc = db.parse(fXmlFile);
        doc.getDocumentElement().normalize();
        NodeList nList = doc.getElementsByTagName("system");
        for (int temp = 0; temp < nList.getLength(); temp++) {
            System.out.println("----------------------------\n");
            Node nNode = nList.item(temp);
            Element eElement = (Element) nNode;
            System.out.println("id : " + eElement.getElementsByTagName("id").item(0).getTextContent());
            System.out.println("name : " + eElement.getElementsByTagName("name").item(0).getTextContent());
            System.out.println("star : " + eElement.getElementsByTagName("star").item(0).getTextContent());
            System.out.println("background : " + eElement.getElementsByTagName("background").item(0).getTextContent());
            SystemProfile systemProfile = new SystemProfile(eElement.getElementsByTagName("id").item(0).getTextContent(),
                    eElement.getElementsByTagName("name").item(0).getTextContent(),
                    eElement.getElementsByTagName("star").item(0).getTextContent(),
                    eElement.getElementsByTagName("background").item(0).getTextContent());
            accountService.getSystems().put(Integer.valueOf(eElement.getElementsByTagName("id").item(0).getTextContent()), systemProfile);
        }
        //----------mobs-----------------------
        fXmlFile = new File("src/main/resources/mobs/mob.xml");
        dbf = DocumentBuilderFactory.newInstance();
        db = dbf.newDocumentBuilder();
        doc = db.parse(fXmlFile);
        doc.getDocumentElement().normalize();
        nList = doc.getElementsByTagName("mob");
        for (int temp = 0; temp < nList.getLength(); temp++) {
            Node nNode = nList.item(temp);
            Element eElement = (Element) nNode;
            MobProfile mobProfile = new MobProfile(Integer.valueOf(eElement.getElementsByTagName("id").item(0).getTextContent()),
                    eElement.getElementsByTagName("name").item(0).getTextContent(),
                    Integer.valueOf(eElement.getElementsByTagName("weapon0").item(0).getTextContent()),
                    Integer.valueOf(eElement.getElementsByTagName("weapon1").item(0).getTextContent()),
                    Integer.valueOf(eElement.getElementsByTagName("weapon2").item(0).getTextContent()),
                    Integer.valueOf(eElement.getElementsByTagName("weapon3").item(0).getTextContent()),
                    Integer.valueOf(eElement.getElementsByTagName("weapon4").item(0).getTextContent()),
                    Integer.valueOf(eElement.getElementsByTagName("block").item(0).getTextContent()),
                    Integer.valueOf(eElement.getElementsByTagName("blockbygen").item(0).getTextContent()),
                    Integer.valueOf(eElement.getElementsByTagName("speed").item(0).getTextContent()),
                    Integer.valueOf(eElement.getElementsByTagName("krit").item(0).getTextContent()),
                    Integer.valueOf(eElement.getElementsByTagName("manevr").item(0).getTextContent()),
                    Integer.valueOf(eElement.getElementsByTagName("drop0").item(0).getTextContent()),
                    Integer.valueOf(eElement.getElementsByTagName("drop1").item(0).getTextContent()),
                    Integer.valueOf(eElement.getElementsByTagName("drop2").item(0).getTextContent()),
                    Integer.valueOf(eElement.getElementsByTagName("drop3").item(0).getTextContent()),
                    Integer.valueOf(eElement.getElementsByTagName("drop4").item(0).getTextContent()),
                    Integer.valueOf(eElement.getElementsByTagName("drop5").item(0).getTextContent()));
            accountService.getMobs().put(Integer.valueOf(eElement.getElementsByTagName("id").item(0).getTextContent()), mobProfile);
        }
        //-------------------------------------
        //-----------------VIP-----------------
        fXmlFile = new File("src/main/resources/vip.xml");
        dbf = DocumentBuilderFactory.newInstance();
        db = dbf.newDocumentBuilder();
        doc = db.parse(fXmlFile);
        doc.getDocumentElement().normalize();
        nList = doc.getElementsByTagName("vip");
        for (int temp = 0; temp < nList.getLength(); temp++) {
            Node nNode = nList.item(temp);
            Element eElement = (Element) nNode;
            VipProfile vipProfile = new VipProfile(Integer.valueOf(eElement.getElementsByTagName("id").item(0).getTextContent()),
                    eElement.getElementsByTagName("name").item(0).getTextContent(),
                    eElement.getElementsByTagName("about").item(0).getTextContent(),
                    Integer.valueOf(eElement.getElementsByTagName("skill").item(0).getTextContent()),
                    Integer.valueOf(eElement.getElementsByTagName("count").item(0).getTextContent()),
                    Integer.valueOf(eElement.getElementsByTagName("price").item(0).getTextContent())
                    );
            profileService.getVip().put(Integer.valueOf(eElement.getElementsByTagName("id").item(0).getTextContent()), vipProfile);
        }


        //-------------------------------------
        for(int i = 0; i < 8; i++) {
            String source = "ship";
            String[] mass = new String[5];
            mass[0] = "hp";
            mass[1] = "block";
            mass[2] = null;
            mass[3] = null;
            mass[4] = null;
            switch (i) {
                case 1:
                    source = "engine";
                    mass[0] = "speed";
                    mass[1] = "giper";
                    mass[2] = "durability";
                    break;
                case 2:
                    source = "gun";
                    mass[0] = "min";
                    mass[1] = "max";
                    mass[2] = "radius";
                    mass[3] = "recharge";
                    mass[4] = "durability";
                    break;
                case 3:
                    source = "fuel";
                    mass[0] = "capacity";
                    mass[1] = "durability";
                    break;
                case 4:
                    source = "droid";
                    mass[0] = "repair";
                    mass[1] = "durability";
                    break;
                case 5:
                    source = "generator";
                    mass[0] = "block";
                    mass[1] = "durability";
                    break;
                case 6:
                    source = "radar";
                    mass[0] = "locate";
                    mass[1] = "durability";
                    break;
                case 7:
                    source = "scaner";
                    mass[0] = "scan";
                    mass[1] = "durability";
                    break;
            }
            fXmlFile = new File("src/main/resources/items/"+source+".xml");
            dbf = DocumentBuilderFactory.newInstance();
            db = dbf.newDocumentBuilder();


            doc = db.parse(fXmlFile);
            doc.getDocumentElement().normalize();
            nList = doc.getElementsByTagName(source);
            String[] mas = new String[6];

            for (int temp = 0; temp < nList.getLength(); temp++) {
                Node nNode = nList.item(temp);
                Element eElement = (Element) nNode;
                for(int j = 0; j < 5; j++) {
                    if(mass[j] != null) {
                        mas[j] = eElement.getElementsByTagName(mass[j]).item(0).getTextContent();
                    } else {
                        mas[j] = null;
                    }
                }
                ItemProfile itemProfile = new ItemProfile(Integer.parseInt(eElement.getElementsByTagName("id").item(0).getTextContent()), i, eElement.getElementsByTagName("name").item(0).getTextContent(), eElement.getElementsByTagName("price").item(0).getTextContent(), mas[0], mas[1], mas[2], mas[3], mas[4]);
                System.out.println("id : " + eElement.getElementsByTagName("id").item(0).getTextContent());
                System.out.println("name : " + eElement.getElementsByTagName("name").item(0).getTextContent());
                accountService.getItems().put(Integer.valueOf(eElement.getElementsByTagName("id").item(0).getTextContent()), itemProfile);
            }
        }
    }
}
