package com.sxy.snote.helper;

import com.sxy.snote.dto.*;
import com.sxy.snote.model.Client;
import com.sxy.snote.model.Image;
import com.sxy.snote.model.Note;
import com.sxy.snote.model.NoteSet;

public class MapperService {
    public static ClientTokenDTO getClientTokenDTO(Client client,String token)
    {
        ClientTokenDTO clientTokenDTO=
                ClientTokenDTO.builder()
                .id(client.getId())
                .email(client.getEmail())
                .username(client.getUsername())
                .JwtToken(token)
                .expireTime("15")
                .build();

        return clientTokenDTO;
    }

    public static ClientDTO getClientDTO(Client client)
    {
        ClientDTO clientDTO=
                ClientDTO.builder()
                        .id(client.getId())
                        .email(client.getEmail())
                        .username(client.getUsername())
                        .build();;

        return clientDTO;
    }

    public  static Note getNote(NoteDTO noteDTO)
    {
        Note note=
                Note.builder()
                        .id(noteDTO.getId())
                        .content(noteDTO.getContent())
                        .code(noteDTO.getCode())
                        .title(noteDTO.getTitle())
                        .build();

        return note;
    }

    public  static NoteDTO getNoteDTO(Note note)
    {
        NoteDTO noteDTO=
               NoteDTO.builder()
                       .id(note.getId())
                       .title(note.getTitle())
                       .content(note.getContent())
                       .code(note.getCode())
                       .userId(note.getUser() != null ? note.getUser().getId() : null)
                       .noteSetId(note.getNoteSet() != null ? note.getNoteSet().getId() : null)
                       .build();

        return noteDTO;
    }

    public static ImageDTO getImageDTO(Image image)
    {
        ImageDTO imageDTO=ImageDTO.builder()
                .id(image.getId())
                .name(image.getName())
                .link(image.getLink())
                .build();

        return imageDTO;
    }

    public static Image getImage(ImageDTO imageDTO)
    {
        Image image=Image.builder()
                .id(imageDTO.getId())
                .name(imageDTO.getName())
                .link(imageDTO.getLink())
                .build();

        return image;
    }

    public static NoteSet getNoteSet(NoteSetDisplayDTO noteSetDisplayDTO)
    {
        NoteSet noteSet=NoteSet.builder()
                .id(noteSetDisplayDTO.getId())
                .name(noteSetDisplayDTO.getName())
                .build();

        return  noteSet;
    }

    public static NoteSetDisplayDTO getNoteSetDisplayDTO(NoteSet noteSet)
    {
        return NoteSetDisplayDTO.builder()
                .id(noteSet.getId())
                .name(noteSet.getName())
                .build();
    }

    public static Client convertClientUpdateDTO_Client(ClientUpdateDTO clientUpdateDTO)
    {
        return Client.builder()
                .id(clientUpdateDTO.getClientDTO().getId())
                .password(clientUpdateDTO.getPassword())
                .email(clientUpdateDTO.getClientDTO().getEmail())
                .roles(clientUpdateDTO.getClientDTO().getRoles())
                .username(clientUpdateDTO.getClientDTO().getUsername())
                .build();
    }
}
