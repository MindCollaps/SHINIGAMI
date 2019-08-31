package BotApplications.DiscApplicationCore.DiscApplicationCommands;

import BotApplications.DiscApplicationCore.AudioCore.AudioInfo;
import BotApplications.DiscApplicationCore.AudioCore.PlayerSendHandler;
import BotApplications.DiscApplicationCore.AudioCore.TrackManager;
import BotApplications.DiscApplicationCore.DiscApplicationFiles.DiscApplicationServer;
import BotApplications.DiscApplicationCore.DiscApplicationFiles.DiscApplicationUser;
import Engines.Engine;
import Utils.MESSAGES;
import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.core.events.message.priv.PrivateMessageReceivedEvent;

import java.util.*;
import java.util.stream.Collectors;

public class CMDMusic implements Command {

    private final int PLAYLIST_LIMIT = 1000;
    private Guild guild;
    private final AudioPlayerManager MANAGER = new DefaultAudioPlayerManager();
    private final Map<Guild, Map.Entry<AudioPlayer, TrackManager>> PLAYERS = new HashMap<>();
    int added = 0;
    boolean addAll = false;

    Engine engine;

    /**
     * Audio Manager als Audio-Stream-Recource deklarieren.
     */
    public CMDMusic() {
        AudioSourceManagers.registerRemoteSources(MANAGER);
    }

    /**
     * Erstellt einen Audioplayer und fügt diesen in die PLAYERS-Map ein.
     *
     * @param g Guild
     * @return AudioPlayer
     */
    private AudioPlayer createPlayer(Guild g) {
        AudioPlayer p = MANAGER.createPlayer();
        TrackManager m = new TrackManager(p);
        p.addListener(m);

        guild.getAudioManager().setSendingHandler(new PlayerSendHandler(p));

        PLAYERS.put(g, new AbstractMap.SimpleEntry<>(p, m));

        return p;
    }

    /**
     * Returnt, ob die Guild einen Eintrag in der PLAYERS-Map hat.
     *
     * @param g Guild
     * @return Boolean
     */
    private boolean hasPlayer(Guild g) {
        return PLAYERS.containsKey(g);
    }

    /**
     * Returnt den momentanen Player der Guild aus der PLAYERS-Map, oder
     * erstellt einen neuen Player für die Guild.
     *
     * @param g Guild
     * @return AudioPlayer
     */
    private AudioPlayer getPlayer(Guild g) {
        if (hasPlayer(g)) {
            return PLAYERS.get(g).getKey();
        } else {
            return createPlayer(g);
        }
    }

    /**
     * Returnt den momentanen TrackManager der Guild aus der PLAYERS-Map.
     *
     * @param g Guild
     * @return TrackManager
     */
    private TrackManager getManager(Guild g) {
        return PLAYERS.get(g).getValue();
    }

    /**
     * Returnt, ob die Guild einen Player hat oder ob der momentane Player
     * gerade einen Track spielt.
     *
     * @param g Guild
     * @return Boolean
     */
    private boolean isIdle(Guild g) {
        return !hasPlayer(g) || getPlayer(g).getPlayingTrack() == null;
    }

    /**
     * Läd aus der URL oder dem Search String einen Track oder eine Playlist in
     * die Queue.
     *
     * @param identifier URL oder Search String
     * @param author     Member, der den Track / die Playlist eingereiht hat
     * @param msg        Message des Contents
     */
    private void loadTrack(String identifier, Member author, Message msg, int results) {

        Guild g = author.getGuild();
        getPlayer(g);

        MANAGER.setFrameBufferDuration(5000);
        MANAGER.loadItemOrdered(g, identifier, new AudioLoadResultHandler() {

            @Override
            public void trackLoaded(AudioTrack track) {
                getManager(g).queue(track, author);
            }

            @Override
            public void playlistLoaded(AudioPlaylist playlist) {
                for (int i = 0; i < (playlist.getTracks().size() > PLAYLIST_LIMIT ? PLAYLIST_LIMIT : playlist.getTracks().size()); i++) {
                    if (results > added) {
                        added++;
                        getManager(g).queue(playlist.getTracks().get(i), author);
                    }
                }
            }

            @Override
            public void noMatches() {
                engine.getBotEngine().getTextUtils().sendError(":musical_note: Lied konnte nicht geladen werden :scream: ", msg.getTextChannel(), engine.getProperties().getLongTime(), false);
            }

            @Override
            public void loadFailed(FriendlyException exception) {
                engine.getBotEngine().getTextUtils().sendError(":musical_note: Lied konnte nicht geladen werden :scream: ", msg.getTextChannel(), engine.getProperties().getMiddleTime(), false);
            }
        });
    }

    /**
     * Stoppt den momentanen Track, worauf der nächste Track gespielt wird.
     *
     * @param g Guild
     */
    private void skip(Guild g) {
        getPlayer(g).stopTrack();
    }

    /**
     * Erzeugt aus dem Timestamp in Millisekunden ein hh:mm:ss - Zeitformat.
     *
     * @param milis Timestamp
     * @return Zeitformat
     */
    private String getTimestamp(long milis) {
        long seconds = milis / 1000;
        long hours = Math.floorDiv(seconds, 3600);
        seconds = seconds - (hours * 3600);
        long mins = Math.floorDiv(seconds, 60);
        seconds = seconds - (mins * 60);
        return (hours == 0 ? "" : hours + ":") + String.format("%02d", mins) + ":" + String.format("%02d", seconds);
    }

    /**
     * Returnt aus der AudioInfo eines Tracks die Informationen als String.
     *
     * @param info AudioInfo
     * @return Informationen als String
     */
    private String buildQueueMessage(AudioInfo info) {
        AudioTrackInfo trackInfo = info.getTrack().getInfo();
        String title = trackInfo.title;
        long length = trackInfo.length;
        return "`[ " + getTimestamp(length) + " ]` " + title + "\n";
    }


    @Override
    public boolean calledServer(String[] args, GuildMessageReceivedEvent event, DiscApplicationServer server, DiscApplicationUser user, Engine engine) {
        return true;
    }

    @Override
    public void actionServer(String[] args, GuildMessageReceivedEvent event, DiscApplicationServer server, DiscApplicationUser user, Engine engine) {
        TrackManager.setServer(server);
        guild = event.getGuild();
        added = 0;
        addAll = false;
        this.engine = engine;

        if (args.length < 1) {
            engine.getBotEngine().getTextUtils().sendError("Kein argument gefunden!", event.getChannel(), engine.getProperties().getMiddleTime(), true);
            return;
        }
        switch (args[0].toLowerCase()) {

            case "add":
                add(args, event, server, user, engine);
                break;

            case "play":
            case "p":
                play(args, event, server, user, engine);
                break;

            case "skip":
            case "s":
            case "next":
            case "n":
                skip(args, event, server, user, engine);
                break;

            case "stop":
                stop(args, event, server, user, engine);
                break;

            case "shuffle":
            case "sh":
                shuffle(args, event, server, user, engine);
                break;

            case "now":
            case "info":
                info(args, event, server, user, engine);
                break;

            case "queue":
            case "playlist":
            case "pl":
                showQueue(args, event, server, user, engine);
                break;
        }
    }

    @Override
    public boolean calledPrivate(String[] args, PrivateMessageReceivedEvent event, DiscApplicationUser user, Engine engine) {
        return false;
    }

    @Override
    public void actionPrivate(String[] args, PrivateMessageReceivedEvent event, DiscApplicationUser user, Engine engine) {

    }

    @Override
    public String help(Engine engine) {
        return "\n**Music help: :musical_note:**\n"
                + "\n" + engine.getProperties().getBotApplicationPrefix() + engine.getProperties().getCommandInvokeMusic() + " :arrow_forward: [p/play] [YT link/dl(default playlist)/sdl (server eigene playlist)/suchanfrage] stoppt die wiedergabe und fügt ein neuse lied hinzu!"
                + "\n" + engine.getProperties().getBotApplicationPrefix() + engine.getProperties().getCommandInvokeMusic() + " :heavy_plus_sign:  [add] [YT link/dl(default playlist)/sdl (server eigene playlist)/suchanfrage] fügt ein lied zur queue hinzu!"
                + "\n" + engine.getProperties().getBotApplicationPrefix() + engine.getProperties().getCommandInvokeMusic() + " :track_next: [skip/next/s/n] nächster song!"
                + "\n" + engine.getProperties().getBotApplicationPrefix() + engine.getProperties().getCommandInvokeMusic() + " :stop_button: [stop] stopt die wiedergabe!"
                + "\n" + engine.getProperties().getBotApplicationPrefix() + engine.getProperties().getCommandInvokeMusic() + " :twisted_rightwards_arrows: [shuffle/sh] shuffelt die songs in einer Playlist neu"
                + "\n" + engine.getProperties().getBotApplicationPrefix() + engine.getProperties().getCommandInvokeMusic() + " :information_source: [info/now] zeigt den aktuellen song!"
                + "\n" + engine.getProperties().getBotApplicationPrefix() + engine.getProperties().getCommandInvokeMusic() + " :information_source: [queue/playlist/pl] [site] zeigt die aktuelle Playlist!";
    }

    private void play(String[] args, GuildMessageReceivedEvent event, DiscApplicationServer server, DiscApplicationUser user, Engine engine) {
        String input;
        if (isIdle(guild)) {

        } else {
            getManager(guild).purgeQueue();
            skip(guild);
        }

        input = Arrays.stream(args).skip(1).map(s -> " " + s).collect(Collectors.joining()).substring(1);
        if (args.length < 2) {
            engine.getBotEngine().getTextUtils().sendError(MESSAGES.CMDMSGERRMUSICPLAY, event.getChannel(), engine.getProperties().getMiddleTime(), true);
            return;
        }
        if (input.startsWith("default")) {
            input = engine.getProperties().getCommandMusicDefaultYTPlaylist();
        } else if (input.startsWith("server")) {
            if (server.getServerYTPlaylist() != null) {
                input = server.getServerYTPlaylist();
            } else {
                engine.getBotEngine().getTextUtils().sendError("Diese playlist existiert nicht!", event.getChannel(), engine.getProperties().getMiddleTime(), true);
                return;
            }
        } else if (input.startsWith("own")) {
            if (user.getYtPlaylist() != null) {
                input = user.getYtPlaylist();
            } else {
                engine.getBotEngine().getTextUtils().sendError("Diese playlist existiert nicht!", event.getChannel(), engine.getProperties().getMiddleTime(), true);
                return;
            }
        } else if (input.startsWith("all")) {
            input = Arrays.stream(args).skip(2).map(s -> " " + s).collect(Collectors.joining()).substring(1);
            addAll = true;
        }
        if (!(input.startsWith("http://") || input.startsWith("https://"))) {
            input = "ytsearch: " + input;
        }
        TrackManager.setEvent(event);
        if (addAll) {
            loadTrack(input, event.getMember(), event.getMessage(), -10);
        } else {
            loadTrack(input, event.getMember(), event.getMessage(), 1);
        }
        engine.getBotEngine().getTextUtils().sendSucces(":arrow_forward: Lied wird abgespielt!", event.getChannel(), engine.getProperties().getShortTime());
    }

    private void add(String[] args, GuildMessageReceivedEvent event, DiscApplicationServer server, DiscApplicationUser user, Engine engine) {
        String input = Arrays.stream(args).skip(1).map(s -> " " + s).collect(Collectors.joining()).substring(1);
        if (args.length < 2) {
            engine.getBotEngine().getTextUtils().sendError(MESSAGES.CMDMSGERRMUSICPLAY, event.getChannel(), engine.getProperties().getMiddleTime(), true);
            return;
        }
        if (!(input.startsWith("http://") || input.startsWith("https://"))) {
            input = "ytsearch: " + input;
        }
        if (input.startsWith("all")) {
            input = Arrays.stream(args).skip(2).map(s -> " " + s).collect(Collectors.joining()).substring(1);
            addAll = true;
        }
        TrackManager.setEvent(event);

        if (addAll) {
            loadTrack(input, event.getMember(), event.getMessage(), -10);
        } else {
            loadTrack(input, event.getMember(), event.getMessage(), 1);
        }
        engine.getBotEngine().getTextUtils().sendSucces(MESSAGES.CMDMSGMUSICPLAY, event.getChannel(), engine.getProperties().getShortTime());
    }

    private void skip(String[] args, GuildMessageReceivedEvent event, DiscApplicationServer server, DiscApplicationUser user, Engine engine) {
        if (isIdle(guild)) {
            return;
        }
        for (int i = (args.length > 1 ? Integer.parseInt(args[1]) : 1); i == 1; i--) {
            skip(guild);
        }
        engine.getBotEngine().getTextUtils().sendWarining(MESSAGES.CMDMSGMUSICSKIP, event.getChannel(), engine.getProperties().getShortTime());
    }

    private void stop(String[] args, GuildMessageReceivedEvent event, DiscApplicationServer server, DiscApplicationUser user, Engine engine) {
        if (isIdle(guild)) {
            return;
        }

        getManager(guild).purgeQueue();
        skip(guild);
        guild.getAudioManager().closeAudioConnection();
        engine.getBotEngine().getTextUtils().sendError(MESSAGES.CMDMSGMUSICSTOP, event.getChannel(), engine.getProperties().getShortTime(), false);
    }

    private void shuffle(String[] args, GuildMessageReceivedEvent event, DiscApplicationServer server, DiscApplicationUser user, Engine engine) {
        if (isIdle(guild)) {
            return;
        }
        getManager(guild).shuffleQueue();
        engine.getBotEngine().getTextUtils().sendSucces(MESSAGES.CMDMSGMUSICSHUFFLE, event.getChannel(), engine.getProperties().getShortTime());
    }

    private void info(String[] args, GuildMessageReceivedEvent event, DiscApplicationServer server, DiscApplicationUser user, Engine engine) {
        if (isIdle(guild)) {
            return;
        }

        AudioTrack track = getPlayer(guild).getPlayingTrack();
        AudioTrackInfo info = track.getInfo();

        MessageEmbed fmsg = new EmbedBuilder().setDescription("** :musical_note: CURRENT TRACK INFO:** :information_source: ")
                .addField("Title", info.title, false)
                .addField("Duration", "`[ " + getTimestamp(track.getPosition()) + "/ " + getTimestamp(track.getDuration()) + " ]`", false)
                .addField("Author", info.author, false)
                .build();
        Message rfmsg = event.getChannel().sendMessage(fmsg).complete();
        engine.getBotEngine().getTextUtils().deleteCustomMessage(rfmsg, engine.getProperties().getVeryLongTime());
    }

    private void showQueue(String[] args, GuildMessageReceivedEvent event, DiscApplicationServer server, DiscApplicationUser user, Engine engine) {
        if (isIdle(guild)) {
            return;
        }

        int sideNumb = args.length > 1 ? Integer.parseInt(args[1]) : 1;

        List<String> tracks = new ArrayList<>();
        List<String> trackSublist;

        getManager(guild).getQueue().forEach(audioInfo -> tracks.add(buildQueueMessage(audioInfo)));

        if (tracks.size() > 20) {
            trackSublist = tracks.subList((sideNumb - 1) * 20, (sideNumb - 1) * 20 + 20);
        } else {
            trackSublist = tracks;
        }

        String out = trackSublist.stream().collect(Collectors.joining("\n"));
        int sideNumbAll = tracks.size() >= 20 ? tracks.size() / 20 : 1;
        MessageEmbed msg = new EmbedBuilder()
                .setDescription(
                        "**CURRENT QUEUE:** :information_source: \n"
                                + "*[" + " Tracks | Side " + sideNumb + " / " + sideNumbAll + "]*\n\n"
                                + out
                )
                .build();
        Message rmsg = event.getChannel().sendMessage(msg).complete();
        engine.getBotEngine().getTextUtils().deleteCustomMessage(rmsg, engine.getProperties().getVeryLongTime());
    }
}
