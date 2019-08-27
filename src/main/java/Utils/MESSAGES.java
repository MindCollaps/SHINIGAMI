package Utils;

import Engines.Engine;

public class MESSAGES {

    Engine engine;

    public static final String CMDMSGMUSICPLAY = ":arrow_forward: Ein Lied wurde der queue hinzugefügt!";
    public static final String CMDMSGMUSICSKIP = ":track_next: Lied wurde übersprungen!";
    public static final String CMDMSGMUSICSHUFFLE = ":twisted_rightwards_arrows: Wiedergabe wurde neu sortiert!";
    public static final String CMDMSGMUSICSTOP = ":stop_button: Wiedergabe wurde angehalten!";

    //Music Error
    public static final String CMDMSGERRMUSICPLAY = "Bitte gebe eine gültige quelle ein!";

    //Help
    public static final String HELPSERVERCOMMON
            = "\n**Commands:** :dark_sunglasses: \n"
            + "\n -help [command zu dem man hilfe benötigt]"
            + "\n [size] (löscht den chat verlauf)"
            + "\n (zeigt den ping des members)"
            + "\n (für mehr musikalische hilfe -m [help]) :musical_note:"
            + "\n (für mehr hilfe help):";

    public static final String HELPPRIVATECOMMON
            = "\n**Commands:** :dark_sunglasses: \n"
            + "\n [custom/color] (zeigt wie einzelne funktionen funktionieren)"
            + "\n [size] (löscht den chat verlauf)";

    public static final String HELPADMINCOMMANDSSERVER
            = "\n**Admin help:**\n"
            + "\nset defaultYoutubePlaylist [YoutubePlaylistLink](setze die default Youtube playlist)\n"
            + "\nset fileSlash [preference] (Setzt die datei trennung / oder \\ für Winows und Linux Systeme)\n"
            + "\nset cmdPrefix [preference] (Setzt den prefix der Chat befehle)\n"
            + "\nswitchTo [windows/linux] (wechselt das dateisystem und lädt die daten neu)\n"
            + "\nreload [all/data] (lädt den bot oder nur die daten neu)";

    public static final String HELPADMINCOMMANDSPRIVATE
            = "\n**Admin help:**\n"
            + "\nset defaultYoutubePlaylist [YoutubePlaylistLink](setze die default Youtube playlist)\n"
            + "\nset fileSlash [preference] (Setzt die datei trennung / oder \\ für Winows und Linux Systeme)\n"
            + "\nset cmdPrefix [preference] (Setzt den prefix der Chat befehle)\n"
            + "\nset emoLv [user id] [wert(10-10) (Ändert das emote level eines users)\n"
            + "\nset admin [user id] <true/false> (Ändert das emote level eines users)\n"
            + "\nget user emoLv [user id] (Zeig die emotionale bindung zu einem user an)\n"
            + "\nswitchTo [windows/linux] (wechselt das dateisystem und lädt die daten neu)\n"
            + "\nreload [all/data] (lädt den bot oder nur die daten neu)\n"
            + "\nsend [server/user/useriv/allserver] [sever id, server channel id/user id/user id] [Message content/ **custom** ] (sendet eine nachricht in einen server channel/sendet eine nachricht an einen user/sendet eine nachricht an einen user, der name des verfassers bleibt verboregen";

    public static final String HELPPREFERENCES
            = "\n**Preverence help:**\n"
            + "\n [generate/delete] [server] [default, onlyBot, full] Generiert alle notwendigen channels oder nur das Server file!\n"
            + "\n [set] [ytPlaylist] <yt link> Hiermit kannst du eine eigene Server YouTube playlist hinterlegen!"
            + "\n[set] [listener] <true/false> Hiermit kannst du den voicelistener an oder abschalten!"
            + "\n [set] [djNexus] <true/false> Hiermit kannst du den DJ Nexus an oder abschalten!"
            + "\n [set] [messageDelete] <true/false> Hiermit kannst du bestimmen ob die vom user geschriebenen commands gelöscht werden sollen!";

}
