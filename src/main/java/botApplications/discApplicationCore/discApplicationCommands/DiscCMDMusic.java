package botApplications.discApplicationCore.discApplicationCommands;

import botApplications.discApplicationCore.audioCore.AudioInfo;
import botApplications.discApplicationCore.audioCore.PlayerSendHandler;
import botApplications.discApplicationCore.audioCore.TrackManager;
import botApplications.discApplicationCore.discApplicationFiles.DiscApplicationServer;
import botApplications.discApplicationCore.discApplicationFiles.DiscApplicationUser;
import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;
import engines.Engine;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.core.events.message.priv.PrivateMessageReceivedEvent;
import utils.MESSAGES;

import java.util.*;
import java.util.stream.Collectors;

public class DiscCMDMusic implements DicCommand {

    private final int PLAYLIST_LIMIT = 1000;
    private final AudioPlayerManager MANAGER = new DefaultAudioPlayerManager();
    private final Map<Guild, Map.Entry<AudioPlayer, TrackManager>> PLAYERS = new HashMap<>();
    int added = 0;

    Engine engine;

    public DiscCMDMusic() {
        AudioSourceManagers.registerRemoteSources(MANAGER);
    }

    private AudioPlayer createPlayer(Member author) {
        //engine.getDiscEngine().getBotJDA().getGuildById(author.getGuild().getId());
        Guild g = author.getGuild();
        AudioPlayer p = MANAGER.createPlayer();
        TrackManager m = new TrackManager(p);
        p.addListener(m);

        g.getAudioManager().setSendingHandler(new PlayerSendHandler(p));

        PLAYERS.put(g, new AbstractMap.SimpleEntry<>(p, m));

        return p;
    }

    private boolean hasPlayer(Member author) {
        Guild g = author.getGuild();
        return PLAYERS.containsKey(g);
    }

    private AudioPlayer getPlayer(Member author) {
        Guild g = author.getGuild();
        if (hasPlayer(author)) {
            return PLAYERS.get(g).getKey();
        } else {
            return createPlayer(author);
        }
    }

    private TrackManager getManager(Member author) {
        Guild g = author.getGuild();
        return PLAYERS.get(g).getValue();
    }

    private boolean isIdle(Member author) {
        return !hasPlayer(author) || getPlayer(author).getPlayingTrack() == null;
    }

    /**
     * Läd aus der URL oder dem Search String einen Track oder eine Playlist in
     * die Queue.
     *
     * @param identifier URL oder Search String
     * @param author     Member, der den Track / die Playlist eingereiht hat
     * @param msg        Message des Contents
     */
    private void loadTrack(String identifier, Member author, Message msg) {

        Guild g = author.getGuild();
        getPlayer(author);
        boolean singleSong = false;

        MANAGER.setFrameBufferDuration(5000);
        if(identifier.startsWith("ytsearch:"))
            singleSong = true;
        final boolean finalSingleSong = singleSong;
        MANAGER.loadItemOrdered(g, identifier, new AudioLoadResultHandler() {

            @Override
            public void trackLoaded(AudioTrack track) {
                getManager(author).queue(track, author);
            }

            @Override
            public void playlistLoaded(AudioPlaylist playlist) {
                if(finalSingleSong){
                    getManager(author).queue(playlist.getTracks().get(0), author);
                    return;
                }
                for (int i = 0; i < (playlist.getTracks().size() > PLAYLIST_LIMIT ? PLAYLIST_LIMIT : playlist.getTracks().size()); i++) {
                    added++;
                    getManager(author).queue(playlist.getTracks().get(i), author);
                }
            }

            @Override
            public void noMatches() {
                engine.getDiscEngine().getTextUtils().sendError(":musical_note: Lied konnte nicht geladen werden, deine Suchanfrage ergab keine ergebnisse! :scream: ", msg.getTextChannel(), engine.getProperties().getLongTime(), false);
            }

            @Override
            public void loadFailed(FriendlyException exception) {
                engine.getDiscEngine().getTextUtils().sendError(":musical_note: Lied konnte nicht geladen werden :scream: ", msg.getTextChannel(), engine.getProperties().getMiddleTime(), false);
            }
        });
    }

    private void skip(Member author) {
        getPlayer(author).stopTrack();
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
        added = 0;
        this.engine = engine;

        if (args.length < 1) {
            engine.getDiscEngine().getTextUtils().sendError("Kein argument gefunden!", event.getChannel(), engine.getProperties().getMiddleTime(), true);
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
                + "\n" + engine.getProperties().getDiscBotApplicationPrefix() + engine.getProperties().getCommandInvokeMusic() + " :arrow_forward: [p/play] [YT link/dl(default playlist)/sdl (server eigene playlist)/suchanfrage] stoppt die wiedergabe und fügt ein neuse lied hinzu!"
                + "\n" + engine.getProperties().getDiscBotApplicationPrefix() + engine.getProperties().getCommandInvokeMusic() + " :heavy_plus_sign:  [add] [YT link/dl(default playlist)/sdl (server eigene playlist)/suchanfrage] fügt ein lied zur queue hinzu!"
                + "\n" + engine.getProperties().getDiscBotApplicationPrefix() + engine.getProperties().getCommandInvokeMusic() + " :track_next: [skip/next/s/n] nächster song!"
                + "\n" + engine.getProperties().getDiscBotApplicationPrefix() + engine.getProperties().getCommandInvokeMusic() + " :stop_button: [stop] stopt die wiedergabe!"
                + "\n" + engine.getProperties().getDiscBotApplicationPrefix() + engine.getProperties().getCommandInvokeMusic() + " :twisted_rightwards_arrows: [shuffle/sh] shuffelt die songs in einer Playlist neu"
                + "\n" + engine.getProperties().getDiscBotApplicationPrefix() + engine.getProperties().getCommandInvokeMusic() + " :information_source: [info/now] zeigt den aktuellen song!"
                + "\n" + engine.getProperties().getDiscBotApplicationPrefix() + engine.getProperties().getCommandInvokeMusic() + " :information_source: [queue/playlist/pl] [site] zeigt die aktuelle Playlist!";
    }

    @Override
    public void actionTelegram(Member member, Engine engine, DiscApplicationUser user, String[] args) {
        switch (args[0]) {
            case "play":
                String input;
                if (isIdle(member)) {

                } else {
                    getManager(member).purgeQueue();
                    skip(member);
                }

                input = Arrays.stream(args).skip(1).map(s -> " " + s).collect(Collectors.joining()).substring(1);
                if (args.length < 2) {
                    engine.getTeleApplicationEngine().getTextUtils().sendMessage(user.getTelegramId(), "Bitte gebe eine gültige quelle ein!");
                    return;
                }
                if (!(input.startsWith("http://") || input.startsWith("https://"))) {
                    input = "ytsearch: " + input;
                }
                TrackManager.setEvent(null);
                loadTrack(input, member, null);

                engine.getTeleApplicationEngine().getTextUtils().sendMessage(user.getTelegramId(), ":arrow_forward: Lied wird abgespielt!");
                break;
        }
    }

    private void play(String[] args, GuildMessageReceivedEvent event, DiscApplicationServer server, DiscApplicationUser user, Engine engine) {
        String input;
        if (isIdle(event.getMember())) {

        } else {
            getManager(event.getMember()).purgeQueue();
            skip(event.getMember());
        }

        input = Arrays.stream(args).skip(1).map(s -> " " + s).collect(Collectors.joining()).substring(1);
        if (args.length < 2) {
            engine.getDiscEngine().getTextUtils().sendError(MESSAGES.CMDMSGERRMUSICPLAY, event.getChannel(), engine.getProperties().getMiddleTime(), true);
            return;
        }
        if (input.startsWith("dl")) {
            input = engine.getProperties().getDefaultYtPlaylist();
        } else if (input.startsWith("server")) {
            if (server.getServerYTPlaylist() != null) {
                input = server.getServerYTPlaylist();
            } else {
                engine.getDiscEngine().getTextUtils().sendError("Diese playlist existiert nicht!", event.getChannel(), engine.getProperties().getMiddleTime(), true);
                return;
            }
        } else if (input.startsWith("own")) {
            if (user.getYtPlaylist() != null) {
                input = user.getYtPlaylist();
            } else {
                engine.getDiscEngine().getTextUtils().sendError("Diese playlist existiert nicht!", event.getChannel(), engine.getProperties().getMiddleTime(), true);
                return;
            }
        } else if (input.startsWith("all")) {
            input = Arrays.stream(args).skip(2).map(s -> " " + s).collect(Collectors.joining()).substring(1);
        }
        if (!(input.startsWith("http://") || input.startsWith("https://"))) {
            input = "ytsearch: " + input;
        }
        TrackManager.setEvent(event);
        loadTrack(input, event.getMember(), event.getMessage());
        engine.getDiscEngine().getTextUtils().sendSucces(":arrow_forward: Lied wird abgespielt!", event.getChannel(), engine.getProperties().getShortTime());
    }

    private void add(String[] args, GuildMessageReceivedEvent event, DiscApplicationServer server, DiscApplicationUser user, Engine engine) {
        String input = Arrays.stream(args).skip(1).map(s -> " " + s).collect(Collectors.joining()).substring(1);
        if (args.length < 2) {
            engine.getDiscEngine().getTextUtils().sendError(MESSAGES.CMDMSGERRMUSICPLAY, event.getChannel(), engine.getProperties().getMiddleTime(), true);
            return;
        }
        if (!(input.startsWith("http://") || input.startsWith("https://"))) {
            input = "ytsearch: " + input;
        }
        if (input.startsWith("all")) {
            input = Arrays.stream(args).skip(2).map(s -> " " + s).collect(Collectors.joining()).substring(1);
        }
        TrackManager.setEvent(event);
        loadTrack(input, event.getMember(), event.getMessage());

        engine.getDiscEngine().getTextUtils().sendSucces(MESSAGES.CMDMSGMUSICPLAY, event.getChannel(), engine.getProperties().getShortTime());
    }

    private void skip(String[] args, GuildMessageReceivedEvent event, DiscApplicationServer server, DiscApplicationUser user, Engine engine) {
        if (isIdle(event.getMember())) {
            return;
        }
        for (int i = (args.length > 1 ? Integer.parseInt(args[1]) : 1); i == 1; i--) {
            skip(event.getMember());
        }
        engine.getDiscEngine().getTextUtils().sendWarining(MESSAGES.CMDMSGMUSICSKIP, event.getChannel(), engine.getProperties().getShortTime());
    }

    private void stop(String[] args, GuildMessageReceivedEvent event, DiscApplicationServer server, DiscApplicationUser user, Engine engine) {
        if (isIdle(event.getMember())) {
            return;
        }

        getManager(event.getMember()).purgeQueue();
        skip(event.getMember());
        Guild g = engine.getDiscEngine().getBotJDA().getGuildById(event.getMember().getGuild().getId());
        g.getAudioManager().closeAudioConnection();
        engine.getDiscEngine().getTextUtils().sendError(MESSAGES.CMDMSGMUSICSTOP, event.getChannel(), engine.getProperties().getShortTime(), false);
    }

    private void shuffle(String[] args, GuildMessageReceivedEvent event, DiscApplicationServer server, DiscApplicationUser user, Engine engine) {
        if (isIdle(event.getMember())) {
            return;
        }
        getManager(event.getMember()).shuffleQueue();
        engine.getDiscEngine().getTextUtils().sendSucces(MESSAGES.CMDMSGMUSICSHUFFLE, event.getChannel(), engine.getProperties().getShortTime());
    }

    private void info(String[] args, GuildMessageReceivedEvent event, DiscApplicationServer server, DiscApplicationUser user, Engine engine) {
        if (isIdle(event.getMember())) {
            return;
        }

        AudioTrack track = getPlayer(event.getMember()).getPlayingTrack();
        AudioTrackInfo info = track.getInfo();

        MessageEmbed fmsg = new EmbedBuilder().setDescription("** :musical_note: CURRENT TRACK INFO:** :information_source: ")
                .addField("Title", info.title, false)
                .addField("Duration", "`[ " + getTimestamp(track.getPosition()) + "/ " + getTimestamp(track.getDuration()) + " ]`", false)
                .addField("Author", info.author, false)
                .build();
        Message rfmsg = event.getChannel().sendMessage(fmsg).complete();
        engine.getDiscEngine().getTextUtils().deleteCustomMessage(rfmsg, engine.getProperties().getVeryLongTime());
    }

    private void showQueue(String[] args, GuildMessageReceivedEvent event, DiscApplicationServer server, DiscApplicationUser user, Engine engine) {
        if (isIdle(event.getMember())) {
            return;
        }

        int sideNumb = args.length > 1 ? Integer.parseInt(args[1]) : 1;

        List<String> tracks = new ArrayList<>();
        List<String> trackSublist;

        getManager(event.getMember()).getQueue().forEach(audioInfo -> tracks.add(buildQueueMessage(audioInfo)));

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
        engine.getDiscEngine().getTextUtils().deleteCustomMessage(rmsg, engine.getProperties().getVeryLongTime());
    }
}
