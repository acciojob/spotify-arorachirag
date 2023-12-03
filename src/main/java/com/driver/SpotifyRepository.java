package com.driver;

import java.util.*;

import org.springframework.stereotype.Repository;

@Repository
public class SpotifyRepository {
    public HashMap<Artist, List<Album>> artistAlbumMap;
    public HashMap<Album, List<Song>> albumSongMap;
    public HashMap<Playlist, List<Song>> playlistSongMap;
    public HashMap<Playlist, List<User>> playlistListenerMap;
    public HashMap<User, Playlist> creatorPlaylistMap;
    public HashMap<User, List<Playlist>> userPlaylistMap;
    public HashMap<Song, List<User>> songLikeMap;

    public List<User> users;
    public List<Song> songs;
    public List<Playlist> playlists;
    public List<Album> albums;
    public List<Artist> artists;

    public SpotifyRepository(){
        //To avoid hitting apis multiple times, initialize all the hashmaps here with some dummy data
        artistAlbumMap = new HashMap<>();
        albumSongMap = new HashMap<>();
        playlistSongMap = new HashMap<>();
        playlistListenerMap = new HashMap<>();
        creatorPlaylistMap = new HashMap<>();
        userPlaylistMap = new HashMap<>();
        songLikeMap = new HashMap<>();

        users = new ArrayList<>();
        songs = new ArrayList<>();
        playlists = new ArrayList<>();
        albums = new ArrayList<>();
        artists = new ArrayList<>();
    }

    public User createUser(String name, String mobile) {
        User user = new User(name,mobile);
        users.add(user);
        return user;
    }

    public Artist createArtist(String name) {

        Artist artist = new Artist(name);
        artists.add(artist);

        return artist;
    }

    public Album createAlbum(String title, String artistName) {
    // Artist does not exists
        Artist artist = null;
        if(!artistAlbumMap.containsKey(new Artist(artistName))){
            artist = createArtist(artistName);
            artists.add(artist);

            albums.add(new Album(title));

        }
            Album album = new Album(title);
            albums.add(album);

            artistAlbumMap.put(artist,albums);
            return album;

    }

    public Song createSong(String title, String albumName, int length) throws Exception{

        if(!albumSongMap.containsKey(new Album(albumName))){
            throw new Exception("Album does not exist");
        }
        Song song = new Song(title,length);
        songs.add(song);

        Album album = new Album(albumName);
        albumSongMap.put(album,songs);

        return song;
    }

    public Playlist createPlaylistOnLength(String mobile, String title, int length) throws Exception {

        Playlist playlist = new Playlist(title);

        List<Song> curated = new ArrayList<>();
        for(Song song : songs) {
            if(song.getLength()==length){
                curated.add(song);
            }

        }
        playlistSongMap.put(playlist,curated);
        return playlist;
    }

    public Playlist createPlaylistOnName(String mobile, String title, List<String> songTitles) throws Exception {
        Playlist playlist = new Playlist(title);

        List<Song> songsOnName = new ArrayList<>();
        for(Song song : songs){
            if(song.getTitle()==title){
                songTitles.add(song.getTitle());
            }
        }

        playlistSongMap.put(playlist,songsOnName);
        return playlist;
    }

    public Playlist findPlaylist(String mobile, String playlistTitle) throws Exception {
        List<User> userList = new ArrayList<>();
        Playlist curr = null;
            for(Playlist playlist : playlists){
                if(playlist.getTitle()==playlistTitle){
                    curr = playlist;
                    userList = playlistListenerMap.get(playlist);
                }
            }

            if(userList.isEmpty()) {
                throw new Exception("User does not exist");
            }

            if(curr==null) throw new Exception("Playlist does not exist");

            return curr;
    }

    public Song likeSong(String mobile, String songTitle) throws Exception {
        Song currSong = null;

        for(Song song : songs){
            if(song.getTitle().equals(songTitle)){
                currSong = song;
            }
        }


        return currSong;
    }

    public String mostPopularArtist() {

        String popArtist = "";
        int maxLikes = 0;

        for(Artist artist : artists){
            if(artist.getLikes()>maxLikes){
                maxLikes = artist.getLikes();
                popArtist = artist.getName();
            }
        }

        return popArtist;
    }

    public String mostPopularSong() {

        String popSong = "";
        int maxLikes = 0;

        for(Song song : songs){
            if(song.getLikes()>maxLikes){
                maxLikes = song.getLikes();
                popSong = song.getTitle();
            }
        }

        return popSong;
    }
}
