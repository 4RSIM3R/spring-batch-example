package id.nexteam.batch_demo.entities;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "songs")
@Data
public class Song {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "spotify_id")
    private String spotifyId;

    @Column(name = "name")
    private String name;

}
