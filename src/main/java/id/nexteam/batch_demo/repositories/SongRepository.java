package id.nexteam.batch_demo.repositories;

import id.nexteam.batch_demo.entities.Song;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

public interface SongRepository extends CrudRepository<Song, Integer> {
}
