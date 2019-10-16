package botApplications.discApplicationCore.audioCore;

import botApplications.discApplicationCore.discApplicationFiles.DiscApplicationServer;
import engines.Engine;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.VoiceChannel;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;

public class TrackManager extends AudioEventAdapter {

    private final AudioPlayer PLAYER;
    private final Queue<AudioInfo> queue;
    private static GuildMessageReceivedEvent event;
    private static DiscApplicationServer server;
    private static Engine engine;

    public static void setEvent(GuildMessageReceivedEvent eventt) {
        event = eventt;
    }

    public static void setServer(DiscApplicationServer serverr){
       server = serverr;
    }

    public TrackManager(AudioPlayer player) {
        this.PLAYER = player;
        this.queue = new LinkedBlockingQueue<>();
    }

    public void queue(AudioTrack track, Member author) {
        AudioInfo info = new AudioInfo(track, author);
        queue.add(info);

        if (PLAYER.getPlayingTrack() == null) {
            PLAYER.playTrack(track);
        }
    }

    public Set<AudioInfo> getQueue() {
        return new LinkedHashSet<>(queue);
    }

    public AudioInfo getInfo(AudioTrack track) {
        return queue.stream()
                .filter(info -> info.getTrack().equals(track))
                .findFirst().orElse(null);
    }

    public void purgeQueue() {
        queue.clear();
    }

    public void shuffleQueue() {
        List<AudioInfo> cQueue = new ArrayList<>(getQueue());
        AudioInfo current = cQueue.get(0);
        cQueue.remove(0);
        Collections.shuffle(cQueue);
        cQueue.add(0, current);
        purgeQueue();
        queue.addAll(cQueue);
    }

    @Override
    public void onTrackStart(AudioPlayer player, AudioTrack track) {
        AudioInfo info = queue.element();
        VoiceChannel vChan = info.getAuthor().getVoiceState().getChannel();

        if (vChan == null) {
            player.stopTrack();
        } else {
            info.getAuthor().getGuild().getAudioManager().openAudioConnection(vChan);
        }
        boolean listenerEnabeld = false;
        try{
            listenerEnabeld = server.isMusicListenerEnabled();
        } catch (Exception e){

        }
        if (listenerEnabeld) {
            try {
                engine.getDiscEngine().getTextUtils().sendNewMusicInfo(info.getTrack().getInfo().uri, event.getGuild().getTextChannelsByName(server.getMusicListenerName(), true).get(0), engine.getProperties().getVeryLongTime(), track.getInfo().title, server);
            } catch (Exception e) {
                System.out.println("[on track start] chanel not found error!");
                engine.getDiscEngine().getTextUtils().sendError("Der Benutze den **" + engine.getProperties().getDiscBotApplicationPrefix() + engine.getProperties().getCommandInvokePreverences() + "** command um alle nötigen Chanels zu erstellen! Oder schalte diese funktion ab!", event.getChannel(), engine.getProperties().getVeryLongTime(), true);
            }
        }
        System.out.println("Now Playing " + track.getInfo().title);
    }

    @Override
    public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason) {
        Guild g = null;
        try {
            g = queue.poll().getAuthor().getGuild();
        } catch (Exception ignored) {
        }

        if (queue.isEmpty()) {
            try {
                g.getAudioManager().closeAudioConnection();
            } catch (Exception e) {
                System.out.println("[TrackManager] Next song dosnt exist!");
            }
        } else {
            try {
                player.playTrack(queue.element().getTrack());
            } catch (Exception e) {
                System.out.println("[TrackManager] Next song dosnt exist!");
            }
        }
    }
}
