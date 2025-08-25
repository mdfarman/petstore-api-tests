package com.petstore.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PetRequest {
    private long id;
    private Category category;
    private String name;
    private List<String> photoUrls;
    private List<Tag> tags;
    private String status;


    @Data
    public static class Category {
        private long id;
        private String name;

    }
    @Data
    public static class Tag {
        private long id;
        private String name;

    }
}
