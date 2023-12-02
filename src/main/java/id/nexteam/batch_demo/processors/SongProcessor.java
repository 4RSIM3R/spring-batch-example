package id.nexteam.batch_demo.processors;

import id.nexteam.batch_demo.entities.Song;
import jakarta.annotation.Nullable;
import org.springframework.batch.item.ItemProcessor;



public class SongProcessor implements ItemProcessor<Song, Song> {

    @Nullable
    @Override
    public Song process(Song item) throws Exception {
        return item;
    }
}
